package dione.parsing;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import dione.core.entities.entity_movie;
import dione.core.entities.entity_person;
import dione.db.db_operations;
import dione.db.db_queries_persons;
import dione.parsing.imdb.abstract_imdb_parser;
import dione.parsing.imdb.imdb_director_parser;
import dione.parsing.imdb.imdb_genre_parser;
import dione.parsing.imdb.imdb_language_parser;
import dione.parsing.imdb.imdb_movie_names_parser;
import dione.parsing.imdb.imdb_plot_parser;
import dione.parsing.imdb.imdb_tag_movie_parser;
import dione.parsing.imdb.imdb_tag_parser;
import dione.parsing.imdb.imdb_tagline_parser;
import dione.parsing.loaders.abstract_loader;
import dione.parsing.loaders.actor_loader;
import dione.parsing.loaders.actor_movie_loader;
import dione.parsing.loaders.director_loader;
import dione.parsing.loaders.genre_loader;
import dione.parsing.loaders.genre_movie_loader;
import dione.parsing.loaders.language_loader;
import dione.parsing.loaders.movie_loader;
import dione.parsing.loaders.person_loader;
import dione.parsing.loaders.tag_loader;
import dione.parsing.loaders.tag_movie_loader;
import dione.parsing.yago.abstract_yago_parser;
import dione.parsing.yago.parser_yago_facts;
import dione.parsing.yago.parser_yago_labels;
import dione.parsing.yago.parser_yago_literal_facts;
import dione.parsing.yago.parser_yago_types;
import dione.parsing.yago.parser_yago_wikipedia;


public class Importer extends db_operations implements Runnable,
		PropertyChangeListener {

	private volatile boolean done = false; /* thread termination signal */

	/* yago maps, that we enrich and use */
	private HashMap<String, entity_movie> parser_map_movie = new HashMap<String, entity_movie>();
	private HashMap<String, entity_person> parser_map_actor = new HashMap<String, entity_person>();
	private HashMap<String, entity_person> parser_map_director = new HashMap<String, entity_person>();

	/* imdb entities - langauge */
	private Set<String> parser_language_set = new HashSet<String>();
	/* imdb entities - genre */
	private Set<String> parser_genre_set = new HashSet<String>();
	/* imdb entities - tag */
	private Set<String> parser_tag_set = new HashSet<String>();

	/* helper maps */
	/*
	 * holds all possible movie names, crossing multilangual movie titles, and
	 * director names
	 */
	private HashMap<String, String> imdb_to_yago = new HashMap<String, String>();

	/* holds MOKA tag count, to establish top-10 popular per movie */
	private HashMap<String, Integer> parser_tag_count_map = new HashMap<String, Integer>();
	/*
	 * imdb names, mapped to imdb directors, to help establish qualifed name to
	 * imdb titles
	 */
	private HashMap<String, String> imdb_name_to_director = new HashMap<String, String>();
	/*
	 * imdb names, mapped to movie's name, in other languages, based on IMDB
	 * records
	 */
	private HashMap<String, HashSet<String>> imdb_movie_names = new HashMap<String, HashSet<String>>();

	/*
	 * finally maps imdb titles to their yago movie
	 */
	private HashMap<String, entity_movie> imdb_name_to_yago_movie = new HashMap<String, entity_movie>();

	private float progress_percent; /* final percentage of progress */
	private int cur_progress_max; /* the total amount of work for current task */
	private int task_weight; /* the amount of 100% assigned to this task */
	private int offset_progress; /*
								 * the amount already completed, before this
								 * task
								 */

	/* progress bar interface */
	private List<ActionListener> listeners = new ArrayList<ActionListener>();

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	/**
	 * notify listeners (GUI), about import events:
	 * 
	 * @param message
	 *            - information about event, error message, if exists
	 * @param return_code
	 *            - -2: terminated by user -1: failed and terminated 0:
	 *            terminated normally 1: still processing
	 */
	public void fireEvent(String message, int return_code) {
		ActionEvent event = new ActionEvent(this, return_code, message);
		for (ActionListener listener : listeners) {
			listener.actionPerformed(event);
		}
	}

	/**
	 * called by client, to get curren import progress
	 * 
	 * @return
	 */
	public float get_progress_percent() {
		return this.progress_percent;
	}

	/**
	 * called by client, to terminate thread
	 */
	public void terminate_thread() {
		this.done = true;
	}

	public boolean is_thread_terminated() {
		return this.done;
	}

	/* property change interface */
	@Override
	/**
	 * we get a change event, concerning the amount of lines read, 
	 * batches made, etc, and update the progress counter accordingly
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		/* update current progress */
		Float progress = Float.parseFloat(evt.getNewValue().toString());
		String s_max = String.valueOf(this.cur_progress_max);
		Float max = Float.parseFloat(s_max);
		this.progress_percent = this.offset_progress + (progress / max)
				* this.task_weight;
		/* Notify Listeners in GUI about the progress */
		fireEvent("progress made", 1);
		//System.out.println("progress: " + progress_percent);

	}

	@Override
	/**
	 * The Importer thread's main logic - we run all parsers and loaders
	 */
	public void run() {
		String ts = null;
		int retval = 0;
		boolean is_failed = false; 
		
		try {
			try {
				/* mark process start in db */
				ts = db_operations
						.perform_invocation(invocation_code.YAGO_UPDATE);
				if (ts == null) {
					fireEvent("Cannot create an invocation", -1);
					return;
				}
			} catch (Exception ex) {
				fireEvent("error with invocations table", -1);
				return;
			}

			/* progress at 0 % */
			this.offset_progress = 0;

			/* these are the critical yago parsers */
			try {
				/* parse all movie, actor and directors entities */
				abstract_yago_parser types = new parser_yago_types(
						parser_map_movie, parser_map_actor,
						parser_map_director, this);
				retval = handle_single_task(types, 6);
				if (done) /*if we terminated prematurely, because of the user*/
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Critical Parser Failed");

				/*
				 * parse relationships between movie. without movie->director,
				 * we cannot identify imdb records
				 */
				abstract_yago_parser facts = new parser_yago_facts(
						parser_map_movie, parser_map_actor,
						parser_map_director, this);
				retval = handle_single_task(facts, 6);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Critical Parser Failed");

				/*
				 * parses all foreign names for movies and directors, critical
				 * for imdb identification
				 */
				abstract_yago_parser labels = new parser_yago_labels(
						parser_map_movie, parser_map_actor,
						parser_map_director, this);
				retval = handle_single_task(labels, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Critical Parser Failed");

				/*
				 * parses years and lengths. withou years, we cannot identify
				 * imdb titles
				 */
				abstract_yago_parser literal_facts = new parser_yago_literal_facts(
						parser_map_movie, parser_map_actor,
						parser_map_director, this);
				retval = handle_single_task(literal_facts, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Critical Parser Failed");

			} catch (Exception ex) { /* we cannot continue, cancel import */
				System.out.println("Major Import Failure, Canceling Import:"
						+ ex.getMessage());
				fireEvent(ex.getMessage(), -1);
				return;
			}

			try { /* non critical parser - wikipedia links */
				abstract_yago_parser wiki = new parser_yago_wikipedia(
						parser_map_movie, parser_map_actor,
						parser_map_director, this);
				handle_single_task(wiki, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
			} catch (Exception ex) {
				System.out
						.println("Error Parsing Wikipedia links, Importer continues");
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			try { /*
				 * Assign every movie all its multilangual names and derived
				 * lookup keys */
				abstract_yago_parser normalizer = new parser_yago_facts(
						parser_map_movie, parser_map_actor,
						parser_map_director, this);
				normalizer.normalize_directors();
				normalizer.update_fq_name();

				/* refhresh importer maps tp reflect changes! */
				parser_map_director = normalizer.get_yag_director_map();
				parser_map_actor = normalizer.get_yag_actor_map();
				parser_map_movie = normalizer.get_parser_movies();
			} catch (Exception ex) {
				System.out.println("Major Import Failure, Canceling Import:"
						+ ex.getMessage());
				fireEvent(ex.getMessage(), -1);
				return;

			}
			
			/* parse imdb names and directors - critical for finding keys later*/
			try {
				abstract_imdb_parser directors = new imdb_director_parser(
						parser_map_movie, imdb_name_to_director,
						imdb_name_to_yago_movie, this);
				retval = handle_single_task(directors, 6);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Critical Parser Failed");

				abstract_imdb_parser names = new imdb_movie_names_parser(
						parser_map_movie, imdb_name_to_director,
						imdb_name_to_yago_movie, this);
				retval = handle_single_task(names, 6);

				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Critical Parser Failed");
				
				names.map_yago_fq_to_imdb_name();
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				};
				
				/*dispose of helper dicts*/
				this.imdb_movie_names.clear();
				this.imdb_movie_names = null;
				this.imdb_name_to_director.clear();
				this.imdb_name_to_director = null;
				this.imdb_to_yago.clear();
				this.imdb_to_yago = null;
				directors.clear();
				names.clear();			

			} catch (Exception ex) {
				System.out.println("Major Import Failure, Canceling Import:"
						+ ex.getMessage());
				fireEvent(ex.getMessage(), -1);
				return;
			}

			/* parse genres - not critical*/
			try {
				abstract_imdb_parser genres = new imdb_genre_parser(
						parser_map_movie, imdb_name_to_yago_movie, this);
				handle_single_task(genres, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				this.parser_genre_set = genres.get_enrichment_set();
			} catch (Exception ex) {
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}
			
			/* parse languages - not critical*/
			try {
				abstract_imdb_parser laguages = new imdb_language_parser(
						parser_map_movie, imdb_name_to_yago_movie, this);
				retval = handle_single_task(laguages, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				this.parser_language_set = laguages.get_enrichment_set();
			} catch (Exception ex) {
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			/* parse tags - not critical*/
			try { /* tag_movie parser depends on tag_parser */
				abstract_imdb_parser tags = new imdb_tag_parser(
						parser_map_movie, imdb_name_to_yago_movie,
						parser_tag_count_map, this);
				handle_single_task(tags, 1);
				this.parser_tag_set = tags.get_enrichment_set();
				
				abstract_imdb_parser tag_movie = new imdb_tag_movie_parser(
						parser_map_movie, imdb_name_to_yago_movie,
						parser_tag_count_map, this);
				handle_single_task(tag_movie, 1);
				
			} catch (Exception ex) {
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			/* parse plots - not critical*/
			try {
				abstract_imdb_parser plots = new imdb_plot_parser(
						parser_map_movie, imdb_name_to_yago_movie, this);
				handle_single_task(plots, 6);
			} catch (Exception ex) {
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			try {
				abstract_imdb_parser taglines = new imdb_tagline_parser(
						parser_map_movie, imdb_name_to_yago_movie, this);
				handle_single_task(taglines, 6);
			} catch (Exception ex) {
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			/* all loader objects */

			/*
			 * to insert movie, we first need to make sure directors and
			 * languages are inserted
			 */
			try { /* insert languagues */
				abstract_loader lang_ldr = new language_loader(this, parser_language_set);
				handle_single_task(lang_ldr, 1);
			} catch (Exception ex) {
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			try { /* insert directors */
				abstract_loader person_ldr_d = new person_loader(this,
						this.parser_map_director);
				retval = handle_single_task(person_ldr_d, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception(
							"Loading directors to person table failed");

				abstract_loader director_ldr = new director_loader(this,
						this.parser_map_director);
				retval = handle_single_task(director_ldr, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception(
							"Loading directors to directors table failed");

			} catch (Exception ex) { /*
									 * directors failed - clean up person
									 * records with no director
									 */
				db_queries_persons.delete_non_related_persons();
				System.out
						.println("Major Import Failure, cleaning directors and persons tables:"
								+ ex.getMessage());
				fireEvent(ex.getMessage(), -1);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			try { /* attempt to load movies and subsequent objects */
				abstract_loader movie_ldr = new movie_loader(this,
						this.parser_map_movie);
				handle_single_task(movie_ldr, 7);

				try { /* insert actors : persons, actors */
					abstract_loader person_ldr_a = new person_loader(this,
							this.parser_map_actor);
					retval = handle_single_task(person_ldr_a, 4);
					if (done)
					{
						fireEvent("Parser Terminated forcefully", -2);
						return;
					}
					else if (retval < 0)
						throw new Exception(
								"Loading directors to person table failed");

					abstract_loader actor_ldr = new actor_loader(this,
							this.parser_map_actor);
					retval = handle_single_task(actor_ldr, 4);
					if (done)
					{
						fireEvent("Parser Terminated forcefully", -2);
						return;
					}
					else if (retval < 0)
						throw new Exception(
								"Loading directors to directors table failed");
				} catch (Exception ex) { /*
										 * actors failed - clean up person
										 * records with no director
										 */
					db_queries_persons.delete_non_related_persons();
					System.out
							.println("Major Import Failure, cleaning actors and persons tables:"
									+ ex.getMessage());
					fireEvent(ex.getMessage(), -1);
					if (done)
					{
						fireEvent("Parser Terminated forcefully", -2);
						return;
					}
					is_failed = true; 
				}

				abstract_loader actor_movie_ldr = new actor_movie_loader(this,
						this.parser_map_movie);
				retval = handle_single_task(actor_movie_ldr, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Loading to movie-actors table failed");

				abstract_loader genre_ldr = new genre_loader(this,
						this.parser_genre_set);
				retval = handle_single_task(genre_ldr, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Loading to genres table failed");

				abstract_loader genre_movie_ldr = new genre_movie_loader(this,
						this.parser_map_movie);
				retval = handle_single_task(genre_movie_ldr, 4);
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				else if (retval < 0)
					throw new Exception("Loading to genre-movie table failed");

				/* if parser already ran onces - don't change the tags*/
				if (!was_there_an_invocation(invocation_code.YAGO_UPDATE)) {
					abstract_loader tag_ldr = new tag_loader(this,
							this.parser_tag_set);
					retval = handle_single_task(tag_ldr, 4);
					if (done)
					{
						fireEvent("Parser Terminated forcefully", -2);
						return;
					}
					else if (retval < 0)
						throw new Exception("Loading to tags table failed");

					abstract_loader tag_movie_ldr = new tag_movie_loader(this,
							this.parser_map_movie);
					retval = handle_single_task(tag_movie_ldr, 4);
					if (done)
					{
						fireEvent("Parser Terminated forcefully", -2);
						return;
					}
					else if (retval < 0)
						throw new Exception("Loading to tag-movie table failed");
	
				}
				
			} catch (Exception ex) { /* movies related inserts failed - will go to final check
									  and terminated import */
				if (done)
				{
					fireEvent("Parser Terminated forcefully", -2);
					return;
				}
				is_failed = true; 
			}

			/* check if terminated forcefully */
			if (!done) {
				this.progress_percent = 100;
				
				System.out.println("Parser Finished Successfully");
				fireEvent("Parser Finished Successfully", 0);
				
				if (!is_failed) /*mark success in db*/
					db_operations.confirm_invocation_performed(invocation_code.YAGO_UPDATE, ts);
			} else {
				System.out.println("Parser Terminated forcefully");
				fireEvent("Parser Terminated forcefully", -2);
			}
		} catch (Exception ex) {
			/* process run was unssucessfull, erase it from invocations */
			try {
				db_operations.delete_last_invocation(
						invocation_code.YAGO_UPDATE, ts);
			} catch (Exception ex2) {
				fireEvent("could not delete invocation!" + ex2.getMessage(), -1);
				return;
			} finally {
				/* output error to console */
				System.out.println("Error in major Importer Component" + ex.getMessage());
				/* notify clients of the error importing error */
				fireEvent(ex.getMessage(), -1);
			}

			return;
		}
	}

	/**
	 * recieves a task - some parser/loader class, registers the importer as a
	 * progress listener to the task, and handles the progress calculations
	 * 
	 * @param task
	 * @param task_weight
	 * @return
	 * @throws Exception
	 */

	private int handle_single_task(Iimport_task task, int cur_task_weight)
			throws Exception {
		int task_retval = 0;
		/* set up progress */
		this.cur_progress_max = task.get_task_size();
		this.task_weight = cur_task_weight;
		task.addChangeListener(this);

		/* do the work */
		task_retval = task.perform_task();
		if (done) {
			System.out
					.print("Termination Signal Caught, Importer Thread Exiting");
		}

		/* clean up */
		this.offset_progress += this.task_weight;
		task.removeChangeListener(this);

		/* notify importer of task termination status */
		return task_retval;
	}

}
