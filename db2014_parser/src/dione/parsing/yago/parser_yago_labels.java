package dione.parsing.yago;

import java.util.HashMap;

import dione.core.entities.entity_movie;
import dione.core.entities.entity_person;
import dione.parsing.Importer;





/**
 * parses labels - mostly multilangual synonims of the movies and directors
 * this helps to find imdb data, especially for foreign movies
 * @author GUY
 *
 */
public class parser_yago_labels extends abstract_yago_parser{

	/* constants */
	public static final int yago_label_value_offset = 2;
	public static final int yago_label_movie_offset = 0; 
	public static final int yago_label_offset = 1; 
	public static final int yago_label_params = 3;
	
	/* members */
	private int c_label_movie = 0;
	

	/**
	 * constructor
	 * @param movie_map - movies to enrich
	 * @param actor_map - actors we know
	 * @param director_map - directors we know
	 * @param Caller
	 */
	public parser_yago_labels(HashMap<String, entity_movie> movie_map,
			HashMap<String, entity_person> actor_map,
			HashMap<String, entity_person> director_map, Importer importer) {
		super(movie_map, actor_map, director_map, importer);
		this.yago_file_path = properties.get_yago_labels_path();
		this.yago_file_params = yago_label_params;
	}

	
	/**
	 *  enrich movie with fact literals: year, duration
	 **/
	@Override
	public int handle_line_parsing(String[] splitted_line) {
		String object_name, fact_value;
		
		fact_value = splitted_line[yago_label_value_offset];
		if (fact_value == null)
			return 0 ;
		
		object_name = clean_format(splitted_line[yago_label_movie_offset]);
		if (object_name == null)
			return 0 ;
		
		try {
			/* is this the name of a movie or a director? */
			entity_movie movie = this.parser_map_movie.get(object_name);
			entity_person director = this.parser_map_director.get(object_name);
			
			/*if there was a match, add to this entity's name list*/
			if (movie != null)
				movie.add_to_labels(parse_movie_label(fact_value));
			else if (director != null)
				director.add_to_person_names(parse_movie_label(fact_value));
			else
				return 0;
		} catch (Exception ex) { /*error in one line*/
			return 0;
		}
		c_label_movie++;
		return 1;
	}
	
	/** 
	 * parse a specific label from its tag 
	 * @param label
	 * @return
	 */
	private String parse_movie_label(String label){
		if (label == null)
			return null;
		if (label.indexOf('\"') < 0 || label.indexOf('@') <= 0)
			return null;
		
		label = (label.substring(0,label.indexOf('@'))).trim();
		label = label.replace("\"","");
		if (label.indexOf("(") > 0)
			label = (label.substring(0,label.indexOf("("))).trim(); 
		return label;
	}

	@Override
	/**
	 * print the number of labels parsed
	 */
	public void print_parse_stats() {
		StringBuilder sb = new StringBuilder();
		sb.append("lables parser:\n\t file: ");
		sb.append(this.yago_file_path);
		sb.append("\nenrichments:\n\tlabels:");
		sb.append(this.c_label_movie);
		System.out.println(sb.toString());
		
	}

	@Override
	/** 
	 * we parse only rdfs:label lines
	 */
	public boolean is_line_parseble(String[] splitted_line) {
		if (splitted_line[yago_label_offset].contains(properties.get_yago_tag_label()))
			return true;
		return false;
	}

	


}
