package parser_entities.parsers;

import java.util.HashMap;


import parser_entities.Importer;
import parser_entities.entity_movie;
import parser_entities.entity_person;

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
	 * constructor - sets some constants to protected members
	 * and uses existing maps, where needed. 
	 * @param movie_map
	 * @param actor_map
	 * @param director_map
	 * @param importer
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
	public void handle_line_parsing(String[] splitted_line) {
		String movie_name, fact_value;
		
		fact_value = splitted_line[yago_label_value_offset];
		if (fact_value == null)
			return;
		
		movie_name = clean_format(splitted_line[yago_label_movie_offset]);
		if (movie_name == null)
			return;
		
		entity_movie movie = this.parser_map_movie.get(movie_name);
		if (movie == null)
			return; 
		
		movie.add_to_labels(parse_movie_label(fact_value));
		c_label_movie++;
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
