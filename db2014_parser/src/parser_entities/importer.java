package parser_entities;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import parser_entities.imdb_parsers.abstract_imdb_parser;
import parser_entities.imdb_parsers.imdb_director_parser;
import parser_entities.imdb_parsers.imdb_genre_parser;
import parser_entities.imdb_parsers.imdb_language_parser;
import parser_entities.imdb_parsers.imdb_plot_parser;
import parser_entities.imdb_parsers.imdb_tag_movie_parser;
import parser_entities.imdb_parsers.imdb_tag_parser;
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

public class Importer implements Runnable,PropertyChangeListener{

	private HashMap<String, entity_movie> parser_map_movie = new HashMap<String, entity_movie>(); 
	private HashMap<String, entity_person> parser_map_actor =new  HashMap<String, entity_person>(); 
	private HashMap<String, entity_person> parser_map_director = new HashMap<String, entity_person>();

	private HashSet<String> parser_language_set;			/* imdb entities - languge*/	 
	private HashSet<String> parser_genre_set;				/* imdb entities - genres*/
	private HashSet<String> parser_tag_set;					/* imdb entities - tags*/
	
	/*helper maps*/
	private HashMap<String,String> 	imdb_to_yago;			/*holds all possible imdb names, that are relevant to yago films*/
	private HashMap<String,Integer> parser_tag_count_map;	/* handles tag counts, to establish top 10 per movie*/
	private HashMap<String,String> 	imdb_name_to_director;	/* maps imdb movie name to imdb director*/ 

	private float progress_percent; 	/* final percentage of progress */
	private int cur_progress_max; 	/* the total amount of work for current task*/
	private int cur_task; 			/* the current parse,load made*/
	private int task_weight;		/* the amount of 100% assigned to this task*/
	private int offset_progress;	/* the amount already completed, before this task*/
	
	/*progress bar interface*/
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	public void addActionListener(ActionListener listener) {
		  listeners.add(listener);
		 }

	public void fireEvent(String message) {
		  ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, message);
		  for(ActionListener listener: listeners){
		   listener.actionPerformed(event);
		  }
		 }
	
	public float get_progress_percent()
	{
		return this.progress_percent;
	}
	
	/*property change interface*/
	@Override
	/**
	 * we get a change event, concerning the amount of lines read, 
	 * batches made, etc, and update the progress counter accordingly
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		Float progress = Float.parseFloat(evt.getNewValue().toString());
		String s_max = String.valueOf(this.cur_progress_max);
		Float max = Float.parseFloat(s_max);
		
		this.progress_percent = this.offset_progress + (progress / max )*this.task_weight;
	
	}
	
	@Override
	public void run() {
	// TODO Auto-generated method stub
		
		try {
		abstract_yago_parser[] yago_parsers = 
			{new parser_yago_types(parser_map_movie,parser_map_actor,parser_map_director),
			 new parser_yago_facts(parser_map_movie,parser_map_actor,parser_map_director),
			 new parser_yago_literal_facts(parser_map_movie,parser_map_actor,parser_map_director),
			 new parser_yago_wikipedia(parser_map_movie,parser_map_actor,parser_map_director),
			 new parser_yago_labels(parser_map_movie,parser_map_actor,parser_map_director),
			};
		
		this.offset_progress = 0;
		for (abstract_yago_parser parser: yago_parsers)
		{
			this.cur_progress_max = parser.get_file_line_count();
			this.task_weight = 5;
			parser.addChangeListener(this);
			parser.parse_file();
			this.offset_progress+= this.task_weight;
		}
		
		yago_parsers[yago_parsers.length-1].update_fq_name();
		
		 /*
		parser_yago_types types = new parser_yago_types(parser_map_movie,parser_map_actor,parser_map_director); 
		types.parse_file();
		
		parser_yago_facts facts = new parser_yago_facts(parser_map_movie,parser_map_actor,parser_map_director); 
		facts.parse_file();
		
		parser_yago_literal_facts literal_facts = new parser_yago_literal_facts(parser_map_movie,parser_map_actor,parser_map_director);
		literal_facts.parse_file();
		*/
		//parser_yago_wikipedia wikipedia = new parser_yago_wikipedia(parser_map_movie,parser_map_actor,parser_map_director);
		//wikipedia.parse_file();
		
		//parser_yago_labels labels = new parser_yago_labels(parser_map_movie,parser_map_actor,parser_map_director);
		//labels.parse_file();
		
		//labels.update_fq_name();
		
		/*first, get directors, and set all yago names*/
		imdb_director_parser misc = new imdb_director_parser(parser_map_movie);
		misc.parse_imdb_file();
		misc.map_imdb_yago_names();
		imdb_name_to_director = misc.get_directors();
		imdb_to_yago = misc.get_imdb_to_yago();
		
		/*parse all relevant data*/
		abstract_imdb_parser[] imdb_parsers = {
				new imdb_director_parser(parser_map_movie),
				new imdb_genre_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
				new imdb_plot_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
				new imdb_language_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
		};
		
		
		for (int i=0;i<imdb_parsers.length;i++)		
		{
			abstract_imdb_parser p = imdb_parsers[i]; 
			
			
			this.cur_progress_max = imdb_parser.get_file_line_count();
			this.task_weight = 5;
			imdb_parser.addChangeListener(this);
			imdb_parser.parse_imdb_file();
			this.offset_progress+= this.task_weight;
			
			if (i == 0)
				imdb_to_yago = misc.get_imdb_to_yago();
				
				
		}
		
		/*parser genres*/
		imdb_genre_parser genres = new imdb_genre_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago);
		genres.parse_imdb_file();
		
		/*create genre set*/
		parser_genre_set = genres.get_parser_genres();
		
		/*update movies with plots*/
		imdb_plot_parser plots = new imdb_plot_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago);
		plots.parse_imdb_file();
		
		/*update movies with langauges*/
		imdb_language_parser languages = new imdb_language_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago);
		languages.parse_imdb_file();
		
		/*create genre set*/
		parser_language_set = languages.get_parser_languages();
		
		/*get tags and their scores*/
		imdb_tag_parser tags = new imdb_tag_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago);
		tags.parse_imdb_file();
		
		this.parser_tag_set = tags.get_parser_tags();
		this.parser_tag_count_map = tags.get_parser_tag_counts();
		
		/*updates movie entites with tags*/
		imdb_tag_movie_parser tag_movie = 
				new imdb_tag_movie_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago,parser_tag_count_map);
		tag_movie.parse_imdb_file();
		
		/*peripherial data*/
		language_loader load_languages = new language_loader();
		load_languages.load_batch(parser_language_set);
		
		genre_loader load_genres = new genre_loader();
		load_genres.load_batch(parser_genre_set);
		
		tag_loader load_tags = new tag_loader();
		load_tags.load_batch(parser_tag_set);
		
		/*entities - persons, movies*/
		person_loader load_persons = new person_loader();
		load_persons.load_batch(parser_map_actor.values());
		load_persons.load_batch(parser_map_director.values());
		
		actor_loader load_actors = new actor_loader();
		load_actors.load_batch(parser_map_actor.values());
		
		director_loader load_directors = new director_loader();
		load_directors.load_batch(parser_map_director.values());
		
		movie_loader movies = new movie_loader();
		movies.load_batch(parser_map_movie.values());
		
		/*movie many to many*/
		actor_movie_loader load_actor_movie = new actor_movie_loader();
		load_actor_movie.load_batch(parser_map_movie.values());
		
		genre_movie_loader load_genre_movie = new genre_movie_loader();
		load_genre_movie.load_batch(parser_map_movie.values());
		
		tag_movie_loader load_tag_movie = new tag_movie_loader();
		load_tag_movie.load_batch(parser_map_movie.values());
		}
		catch (Exception ex)
		{
			
		}
	}

	



}
