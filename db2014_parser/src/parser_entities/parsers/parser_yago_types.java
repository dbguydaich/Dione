package parser_entities.parsers;

import java.util.HashMap;

import parser_entities.entity_movie;
import parser_entities.entity_person;


public class parser_yago_types extends abstract_yago_parser {
	
	
	public parser_yago_types(HashMap<String, entity_movie> movie_map,
			HashMap<String, entity_person> actor_map,
			HashMap<String, entity_person> director_map) {
		super(movie_map, actor_map, director_map);
		this.yago_file_path = properties.get_yago_simple_types_path();
		this.yago_file_params = yago_types_params;
	}

	private int c_directors = 0; 
	private int c_actors = 0;
	private int c_movies = 0;
	
	public static final int yago_type_offset = 0;
	public static final int yago_fact_offset = 2;
	public static final int yago_types_params = 3;

	
	/** creates a new movie in map **/
	void add_to_parser_movies (String movie_name)
	{
		String clean_name = clean_format(movie_name);
		entity_movie movie = new entity_movie(clean_name);
		movie.set_movie_name(clean_name);
		this.parser_map_movie.put(movie_name, movie);
		c_movies++;
	}
	
	/** creates a new actor in map **/
	void add_to_parser_actors (String person_name)
	{
		entity_person person = new entity_person(person_name);
		person.set_person_name(person_name);
		this.parser_map_actor.put(person_name, person);
		c_actors++;
	}
	
	/** creates a new director in map **/
	void add_to_parser_directors (String person_name)
	{
		entity_person person = new entity_person(person_name);
		person.set_person_name(person_name);
		this.parser_map_director.put(person_name, person);
		c_directors++;
	}

	@Override
	public void print_parse_stats() {
		StringBuilder sb = new StringBuilder();
		sb.append("types parser:\n\t file: ");
		sb.append(this.yago_file_path);
		sb.append("\nObjects:\n\tmovies:");
		sb.append(this.c_movies);
		sb.append("\n\tactors:");
		sb.append(this.c_actors);
		sb.append("\n\tdirectors:");
		sb.append(this.c_directors);
		System.out.println(sb.toString());
		
	}

	@Override
	public boolean is_line_parseble(String[] splitted_line) {
		if(splitted_line[yago_fact_offset].contains(properties.get_yago_tag_movie())
		|| splitted_line[yago_fact_offset].contains(properties.get_yago_tag_actor())
		|| splitted_line[yago_fact_offset].contains(properties.get_yago_tag_director()))
			return true; 
		return false;
	}

	@Override
	public void handle_line_parsing(String[] splitted_line) {
		if(splitted_line[yago_fact_offset].contains(properties.get_yago_tag_movie()))
			add_to_parser_movies(clean_format(splitted_line[yago_type_offset]));
		
		else if(splitted_line[yago_fact_offset].contains(properties.get_yago_tag_actor()))
			add_to_parser_actors(clean_format(splitted_line[yago_type_offset]));
		
		else if(splitted_line[yago_fact_offset].contains(properties.get_yago_tag_director()))
			add_to_parser_directors(clean_format(splitted_line[yago_type_offset]));
		
	}
	
}
