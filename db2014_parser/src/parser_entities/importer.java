package parser_entities;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
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

public class Importer implements Runnable,PropertyChangeListener{

	private HashMap<String, entity_movie> parser_map_movie = new HashMap<String, entity_movie>(); 
	private HashMap<String, entity_person> parser_map_actor =new  HashMap<String, entity_person>(); 
	private HashMap<String, entity_person> parser_map_director = new HashMap<String, entity_person>();

	private HashSet<String> parser_language_set = new HashSet<String>();			/* imdb entities - languge*/	 
	private HashSet<String> parser_genre_set = new HashSet<String>();				/* imdb entities - genres*/
	private HashSet<String> parser_tag_set = new HashSet<String>();					/* imdb entities - tags*/
	
	/*helper maps*/
	private HashMap<String,String> 	imdb_to_yago = new HashMap<String,String>();			/*holds all possible imdb names, that are relevant to yago films*/
	private HashMap<String,Integer> parser_tag_count_map = new HashMap<String,Integer>();	/* handles tag counts, to establish top 10 per movie*/
	private HashMap<String,String> 	imdb_name_to_director = new HashMap<String,String>();	/* maps imdb movie name to imdb director*/ 

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
		System.out.println("progress: "+progress_percent);
	
	}
	
	@Override
	public void run() {
	// TODO Auto-generated method stub
		
		try {
		/*parse all yago files*/
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
			this.task_weight = 2;
			parser.addChangeListener(this);
			parser.parse_file();
			this.offset_progress+= this.task_weight;
		}
		
		yago_parsers[yago_parsers.length-1].update_fq_name();
		parser_map_movie = yago_parsers[yago_parsers.length-1].get_parser_movies();
		
		
		/*parse all IMDB files*/
		abstract_imdb_parser[] imdb_parsers = {
				new imdb_director_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
				new imdb_genre_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
				new imdb_language_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
				new imdb_tag_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
				new imdb_plot_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago),
				new imdb_tag_movie_parser(parser_map_movie,imdb_name_to_director,imdb_to_yago,parser_tag_count_map),
		};
		
		//List<Set<String>> set_list = new ArrayList<Set<String>>();
		//set_list.add(1,parser_genre_set);
		//set_list.add(2,parser_language_set);
		//set_list.add(3,parser_tag_set);
		
		
		for (int i=0;i<imdb_parsers.length;i++)		
		{
			abstract_imdb_parser p = imdb_parsers[i];
			this.cur_progress_max = p.get_file_line_count();
			this.task_weight = 2;
			p.addChangeListener(this);
			p.parse_imdb_file();
			
			/*if there is a set to enrich - do it*/
			if (get_import_set(i) != null)
				get_import_set(i).addAll(p.get_enrichment_set());
			if (i==0)
				p.map_imdb_yago_names();
			if (i==3)
				this.parser_tag_count_map = ((imdb_tag_parser)p).get_parser_tag_counts();				

			this.offset_progress+= this.task_weight;				
		}
		
		/*all collections to load*/
		//ArrayList<Collection<?>> loader_data = new ArrayList<Collection<?>>();
		//loader_data.add(parser_language_set);
		//loader_data.add(parser_genre_set);
		//loader_data.add(parser_tag_set);
		//loader_data.add(parser_map_actor.values());
		//loader_data.add(parser_map_director.values());
		//loader_data.add(parser_map_actor.values());
		//loader_data.add(parser_map_director.values());
		//loader_data.add(parser_map_movie.values());
		//loader_data.add(parser_map_movie.values());
		//loader_data.add(parser_map_movie.values());
		//loader_data.add(parser_map_movie.values());
		
		/*all loader objects*/
		abstract_loader[] loaders = {
				new language_loader(),		/* languages 		*/
				new genre_loader(), 		/* genres			*/
				new tag_loader(), 			/* tags				*/
				new person_loader(),		/*actors: persons	*/
				new person_loader(),		/*directors: persons*/
				new actor_loader(),			/*actors			*/
				new director_loader(),		/*directors			*/
				new movie_loader(),			/*movies			*/
				new actor_movie_loader(),	/* actors in movies	*/
				new genre_movie_loader(),	/* genres of movies	*/
				new tag_movie_loader()		/* tags of movies	*/
				};	
		
		for (int i=0;i<loaders.length;i++)		
		{
			abstract_loader ldr = loaders[i];
			this.cur_progress_max = ldr.get_task_size();
			this.task_weight = 6;
			ldr.addChangeListener(this);
			ldr.load_batch(get_load_set(i));
				
		}
		}
		catch (Exception ex)
		{
		}
	}

	
	private Set<String> get_import_set(int i)
	{			
		Set<String> retset = new HashSet<String>();
		switch (i) {
			case 1:  retset= this.parser_genre_set;
			break;
			case 2:  retset= this.parser_language_set;
			break;
			case 3:  retset= this.parser_tag_set;
			break;
	        default: retset = null;
	        break;
		}
        return retset; 
	}
	
	private Collection<?> get_load_set(int i)
	{			
		Collection<?> retset;
		switch (i) {
			case 1:  retset= this.parser_language_set;
			break;
			case 2:  retset= this.parser_genre_set;
			break;
			case 3:  retset= this.parser_tag_set;
			break;
			case 4:  retset= this.parser_map_actor.values();
			break;
			case 5:  retset= this.parser_map_director.values();
			break;
			case 6:  retset= this.parser_map_actor.values();
			break;
			case 7:  retset= this.parser_map_director.values();
			break;
			case 8:  retset= this.parser_map_movie.values();
			break;
			case 9:  retset= this.parser_map_movie.values();
			break;
			case 10:  retset= this.parser_map_movie.values();
			break;
			case 11:  retset= this.parser_map_movie.values();
			break;
			default: retset = null;
	        break;
		}
        return retset; 
	}



}
