package parser_entities.parsers;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import parser_entities.Importer;
import parser_entities.entity_movie;
import parser_entities.entity_person;


public class parser_yago_wikipedia extends abstract_yago_parser{
	
	public parser_yago_wikipedia(HashMap<String, entity_movie> movie_map,
			HashMap<String, entity_person> actor_map,
			HashMap<String, entity_person> director_map, Importer importer) {
		super(movie_map, actor_map, director_map, importer);
		this.yago_file_path = properties.get_yago_wikipedia_path();
		this.yago_file_params = yago_wiki_params;
		
	}

	public static final int yago_wiki_movie_offset = 0; 
	public static final int yago_wiki_url_offset = 2;
	public static final int yago_wiki_tag_offset = 1;
	public static final int yago_wiki_params = 3;
	
	private int c_wiki_movie = 0 ;
	
	/** add the wikiURL to the movie */
	@Override
	public void handle_line_parsing(String[] splitted_line) {
		String movie_name, fact_name;
		
		fact_name = splitted_line[yago_wiki_url_offset];
		if (fact_name == null)
			return;
		fact_name = fact_name.substring(1, fact_name.length() -1);
		
		movie_name = splitted_line[yago_wiki_movie_offset];
		if (fact_name == null)
			return;
		
		entity_movie movie = this.parser_map_movie.get(movie_name);
		if (movie == null)
			return; 
		
		movie.set_movie_wikipedia_url(fact_name);
		c_wiki_movie++; 
		

	}

	@Override
	public void print_parse_stats() {
		StringBuilder sb = new StringBuilder();
		sb.append("Wikipedia parser:\n\t file: ");
		sb.append(this.yago_file_path);
		sb.append("\nenrichments:\n\twikipedia URLs:");
		sb.append(this.c_wiki_movie);
		System.out.println(sb.toString());
	}

	@Override
	public boolean is_line_parseble(String[] splitted_line) {
		if (splitted_line[yago_wiki_tag_offset].contains(properties.get_yago_tag_wikipedia()))
			return true;
		return false;
	}

	
}
