package parser_entities;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import db.db_operations;

import parser_entities.imdb_parsers.abstract_imdb_parser;
import parser_entities.imdb_parsers.imdb_director_parser;
import parser_entities.imdb_parsers.imdb_genre_parser;
import parser_entities.imdb_parsers.imdb_language_parser;
import parser_entities.imdb_parsers.imdb_movie_names_parser;
import parser_entities.imdb_parsers.imdb_plot_parser;
import parser_entities.imdb_parsers.imdb_tag_movie_parser;
import parser_entities.imdb_parsers.imdb_tag_parser;
import parser_entities.imdb_parsers.imdb_tagline_parser;
import parser_entities.loaders.abstract_loader;
import parser_entities.loaders.actor_loader;
import parser_entities.loaders.actor_movie_loader;
import parser_entities.loaders.director_loader;
import parser_entities.loaders.genre_loader;
import parser_entities.loaders.genre_movie_loader;
import parser_entities.loaders.language_loader;
import parser_entities.loaders.movie_loader;
import parser_entities.loaders.person_loader;
import parser_entities.loaders.tag_loader;
import parser_entities.loaders.tag_movie_loader;
import parser_entities.parsers.abstract_yago_parser;
import parser_entities.parsers.parser_yago_facts;
import parser_entities.parsers.parser_yago_labels;
import parser_entities.parsers.parser_yago_literal_facts;
import parser_entities.parsers.parser_yago_types;
import parser_entities.parsers.parser_yago_wikipedia;

public class Importer extends db_operations implements Runnable,
		PropertyChangeListener {

	private volatile boolean done = false; /* thread termination signal */
	
	/* yago maps, that we enrich and use */
	private HashMap<String, entity_movie> parser_map_movie = new HashMap<String, entity_movie>();
	private HashMap<String, entity_person> parser_map_actor = new HashMap<String, entity_person>();
	private HashMap<String, entity_person> parser_map_director = new HashMap<String, entity_person>();

	/* imdb entities - langauge */
	private HashSet<String> parser_language_set = new HashSet<String>();
	/* imdb entities - genre */
	private HashSet<String> parser_genre_set = new HashSet<String>();
	/* imdb entities - tag */
	private HashSet<String> parser_tag_set = new HashSet<String>();
	
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
	 * imdb names, mapped to movie's name, in other languages, based on IMDB records
	 */
	private HashMap<String, HashSet<String>> imdb_movie_names = new HashMap<String, HashSet<String>>();

	
	private float progress_percent; /* final percentage of progress */
	private int cur_progress_max; /* the total amount of work for current task */
	private int cur_task; /* the current parse,load made */
	private int task_weight; /* the amount of 100% assigned to this task */
	private int offset_progress; /* the amount already completed, before this task */

	/* progress bar interface */
	private List<ActionListener> listeners = new ArrayList<ActionListener>();

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	/**
	 * notify listeners (GUI), about import events: 
	 * @param message - information about event, error message, if exists
	 * @param return_code - 
	 *				 	-2: terminated by user
	 * 					-1: failed and terminated
	 *					 0: terminated normally
	 *				 	 1: still processing
	 */
	public void fireEvent(String message, int return_code) {
		ActionEvent event = new ActionEvent(this, return_code,
				message);
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
		/* update curren progress */
		Float progress = Float.parseFloat(evt.getNewValue().toString());
		String s_max = String.valueOf(this.cur_progress_max);
		Float max = Float.parseFloat(s_max);
		this.progress_percent = this.offset_progress + (progress / max)
				* this.task_weight;
		/* Notify Listeners in GUI about the progress */
		fireEvent("progress made",1);
		System.out.println("progress: " + progress_percent);

	}

	@Override
	/**
	 * The Importer thread's main logic - we run all parsers and loaders
	 */
	public void run() {
		String ts = null;
		try {
			/* mark process start in db */
			ts = set_invocation();
			if (ts == null)
			{
				fireEvent("Cannot create an invocation",-1);
				return;
			}
				
			this.offset_progress = 0;			
			
			/* parse all yago files  */
			abstract_yago_parser[] yago_parsers = {
					new parser_yago_types(parser_map_movie, parser_map_actor,
							parser_map_director, this), /* types */
					new parser_yago_facts(parser_map_movie, parser_map_actor,
							parser_map_director, this), /* facts */
					new parser_yago_literal_facts(parser_map_movie,
							parser_map_actor, parser_map_director, this), /* literal facts */
					new parser_yago_wikipedia(parser_map_movie,
							parser_map_actor, parser_map_director, this), /* wiki links */
					new parser_yago_labels(parser_map_movie, parser_map_actor, /* labels */
							parser_map_director, this) };
			
			/* go over parsers, parse and update progress */
			for (abstract_yago_parser parser : yago_parsers) {
				this.cur_progress_max = parser.get_file_line_count();
				this.task_weight = 2;
				parser.addChangeListener(this);
				parser.parse_file();
				if (done) {
					System.out
							.print("Termination Signal Caught, Importer Thread Exiting");
					//fireEvent("Parser Terminated forcefully",-2);
					//return;
					break;
				}
				this.offset_progress += this.task_weight;
			}
			
			/* having parsed multilangual labels, we update names and dict refrences*/
			yago_parsers[yago_parsers.length - 1].normalize_directors();
			yago_parsers[yago_parsers.length - 1].update_fq_name();
			
			
			parser_map_director = yago_parsers[yago_parsers.length - 1]
					.get_yag_director_map();
			parser_map_actor = yago_parsers[yago_parsers.length - 1]
					.get_yag_actor_map();
			
			parser_map_movie = yago_parsers[yago_parsers.length - 1]
					.get_parser_movies();

			/* parse all IMDB files */
			abstract_imdb_parser[] imdb_parsers = {
					new imdb_director_parser(parser_map_movie,
							imdb_name_to_director, imdb_to_yago, this),
					new imdb_movie_names_parser(parser_map_movie,
							imdb_name_to_director, imdb_movie_names, imdb_to_yago, this),
					new imdb_genre_parser(parser_map_movie,
							imdb_name_to_director, imdb_movie_names, imdb_to_yago, this),
					new imdb_language_parser(parser_map_movie,
							imdb_name_to_director, imdb_movie_names, imdb_to_yago, this),
					new imdb_tag_parser(parser_map_movie,
							imdb_name_to_director, imdb_movie_names, imdb_to_yago,
							parser_tag_count_map, this),
					new imdb_tag_movie_parser(parser_map_movie,
							imdb_name_to_director, imdb_movie_names, imdb_to_yago,
							parser_tag_count_map, this),
					new imdb_plot_parser(parser_map_movie,
							imdb_name_to_director, imdb_movie_names, imdb_to_yago, this),
					new imdb_tagline_parser(parser_map_movie,
							imdb_name_to_director, imdb_movie_names, imdb_to_yago, this),};

			for (int i = 0; i < imdb_parsers.length; i++) {
				abstract_imdb_parser p = imdb_parsers[i];
				this.cur_progress_max = p.get_file_line_count();
				this.task_weight = 2;
				
				p.addChangeListener(this);
				p.parse_imdb_file();
				
				/* create the yago to imdb map, after getting imdb directors*/
				if (i==0)
					p.map_imdb_yago_names();
				
				if (done) {
					System.out
							.print("Termination Signal Caught, Importer Thread Exiting");
					//fireEvent("Parser Terminated forcefully",-2);
					//return;
					break;
				}
				/* if there is a set to enrich - do it */
				if (get_import_set(i) != null)
					get_import_set(i).addAll(p.get_enrichment_set());
				this.offset_progress += this.task_weight;
			}

			/* all loader objects */
			abstract_loader[] loaders = { new language_loader(this), /* languages */
			new genre_loader(this), 	/* genres */
			new tag_loader(this), 		/* tags */
			new person_loader(this), 	/* actors: persons */
			new person_loader(this), 	/* directors: persons */
			new actor_loader(this), 	/* actors */
			new director_loader(this), 	/* directors */
			new movie_loader(this), 	/* movies */
			new actor_movie_loader(this), /* actors in movies */
			new genre_movie_loader(this), /* genres of movies */
			new tag_movie_loader(this) /* tags of movies */
			};

			for (int i = 0; i < loaders.length; i++) {
				abstract_loader ldr = loaders[i];
				this.cur_progress_max = get_load_set(i).size();
				this.task_weight = 7;
				ldr.addChangeListener(this);
				ldr.load_batch(get_load_set(i));
				if (done) {
					System.out
							.print("Termination Signal Caught, Importer Thread Exiting");
					//fireEvent("Parser Terminated forcefully",-2);
					//return;
					break;
				}
				this.offset_progress += this.task_weight;
			}

			/* check if terminated forcefully */
			if (!done) {
				this.progress_percent = 100;
				System.out.println("Parser Finished Successfully");
				fireEvent("Parser Finished Successfully",0);
			} else {
				System.out.println("Parser Terminated forcefully");
				fireEvent("Parser Terminated forcefully",-2);
				
			}
		} catch (SQLException ex) {
			System.out.println("SQL exception Caught, trying next load");
		} catch (Exception ex) {
			System.out.println("Error in major Importer Component");
			ex.printStackTrace();
			/* process run was unssucessfull, don't remember it */
			remove_last_invocation(ts);
			/* notify GUI of error */
			fireEvent(ex.getMessage(),-1);
		}
	}

	/**
	 * returns the set we want to import into imdb entites
	 * 
	 * @param i
	 * @return
	 */
	private Set<String> get_import_set(int i) {
		Set<String> retset = new HashSet<String>();
		switch (i) {
		case 2:
			retset = this.parser_genre_set;
			break;
		case 3:
			retset = this.parser_language_set;
			break;
		case 4:
			retset = this.parser_tag_set;
			break;
		default:
			retset = null;
			break;
		}
		return retset;
	}

	/**
	 * returns the collection of entites, that we need to load to the database
	 * 
	 * @param i
	 * @return
	 */
	private Collection<?> get_load_set(int i) {
		Collection<?> retset;
		switch (i) {
		case 0:
			retset = this.parser_language_set;
			break;
		case 1:
			retset = this.parser_genre_set;
			break;
		case 2:
			retset = this.parser_tag_set;
			break;
		case 3:
			retset = this.parser_map_actor.values();
			break;
		case 4:
			retset = this.parser_map_director.values();
			break;
		case 5:
			retset = this.parser_map_actor.values();
			break;
		case 6:
			retset = this.parser_map_director.values();
			break;
		case 7:
			retset = this.parser_map_movie.values();
			break;
		case 8:
			retset = this.parser_map_movie.values();
			break;
		case 9:
			retset = this.parser_map_movie.values();
			break;
		case 10:
			retset = this.parser_map_movie.values();
			break;
		default:
			retset = null;
			break;
		}
		return retset;
	}
	/**
	 * determines whether there was some import in the history
	 * @return
	 */
	public boolean was_imported()
	{
		Timestamp ts;
		try {
			ts = get_last_invocation(invocation_code.YAGO_UPDATE);
			if (ts == null)
				return false; 
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * checks if the yago update has benn run recently
	 * 
	 * @return
	 */
	private boolean can_run() {
		try {
			Timestamp ts = get_last_invocation(invocation_code.YAGO_UPDATE);

			// Is it ok not to redo this
			if (ts != null) {
				Timestamp now = new Timestamp(
						new java.util.Date().getTime() - 1000 * 60 * 7);

				// was this performed in the last 15 minutes
				if (now.before(ts))
					return false;
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	/**
	 * makes and invocation for the process
	 * 
	 * @return
	 */
	private String set_invocation() {
		try {
			String ts = perform_invocation(invocation_code.YAGO_UPDATE);
			if (ts == null)
				return null;
			return ts;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * removes last YAGO invocation
	 * 
	 * @return
	 */
	private int remove_last_invocation(String ts) {
		try {
			delete_last_invocation(invocation_code.YAGO_UPDATE, ts);
		} catch (Exception ex) {
			return 0;
		}
		return 1;
	}

}
