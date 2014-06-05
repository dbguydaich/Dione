package parser_entities.parsers;


import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import parser_entities.Importer;
import parser_entities.entity_movie;
import parser_entities.entity_person;

public class parser_yago_literal_facts extends abstract_yago_parser {
	
	public static final int yago_literal_movie_offset = 1;
	public static final int yago_literal_fact_offset = 2; 
	public static final int yago_literal_value_offset = 3;
	public static final int yago_literal_params = 5;
	
	private int c_length_movie = 0;
	private int c_year_movie = 0;
	
	public parser_yago_literal_facts(HashMap<String, entity_movie> movie_map,
			HashMap<String, entity_person> actor_map,
			HashMap<String, entity_person> director_map, Importer importer) {
		super(movie_map, actor_map, director_map, importer);
		this.yago_file_path = properties.get_yago_literal_facts_path();
		this.yago_file_params = yago_literal_params;
		
	}


	/** enrich movie with fact literals: year, duration**/
	@Override
	public void handle_line_parsing(String[] splitted_line) {		
		String movie_name, fact_name, fact_value;
		
		fact_name = splitted_line[yago_literal_fact_offset];
		if (fact_name == null)
			return;
		
		fact_value = splitted_line[yago_literal_value_offset];
		if (fact_value == null)
			return;
		
		movie_name = clean_format(splitted_line[yago_literal_movie_offset]);
		if (movie_name == null)
			return;
		
		entity_movie movie = this.parser_map_movie.get(movie_name);
		if (movie == null)
			return; 
		
		/*add length to movie*/
		if (fact_name.contains(properties.get_yago_tag_length()))
		{
			movie.set_movie_length(parse_movie_length(fact_value));
			c_length_movie++;
		}
		/*set year of movie*/
		else if (fact_name.equals(properties.get_yago_tag_year()))
		{
			movie.set_movie_year(parse_movie_year(fact_value));
			c_year_movie++;
		}
	}
	
	/** pull the time in minutes from the duration format */
	private String parse_movie_length(String literal_fact_length)
	{	
		try {
			String length = literal_fact_length.substring(1, literal_fact_length.indexOf("."));
			int length_secs = Integer.parseInt(length);
			length_secs = length_secs/60;
			Integer length_minutes = new Integer(length_secs);
			return length_minutes.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	
	/** pull the year from the date format of yago */
	private String parse_movie_year(String literal_fact_date){
		if (literal_fact_date == null || literal_fact_date.length() < 5)
			return null;
		String year = literal_fact_date.substring(1,5);
		return year;
	}

	@Override
	public void print_parse_stats() {
		StringBuilder sb = new StringBuilder();
		sb.append("literal facts parser:\n\t file: ");
		sb.append(this.yago_file_path);
		sb.append("\nenrichments:\n\tyears:");
		sb.append(this.c_year_movie);
		sb.append("\n\tlengths:");
		sb.append(this.c_length_movie);
		System.out.println(sb.toString());
	}

	@Override
	public boolean is_line_parseble(String[] splitted_line) {
		if (splitted_line[yago_literal_fact_offset].contains(properties.get_yago_tag_year())
				|| splitted_line[yago_literal_fact_offset].contains(properties.get_yago_tag_length()))
			return true;
		return false;
	}

	
}
