package parser_entities.imdb_parsers;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import parser_entities.entity_movie;

public class imdb_tag_movie_parser extends abstract_imdb_parser{

	private static final String imdb_movie_tags_list_start = "8: THE KEYWORDS LIST";
	private static final Integer MAX_TAGS = 10; 
	private HashMap<String,Integer> parser_tag_count_map;	/* handles tag counts, to establish top 10 per movie*/
	
	public imdb_tag_movie_parser(HashMap<String, entity_movie> movie_map, 
			HashMap<String,Integer> parser_tag_count_map) {
		
		super(movie_map);
		
		this.parser_tag_count_map = parser_tag_count_map;
		
		this.filepath = this.properties.get_imdb_tags_path();
		this.list_end = null;
		this.list_start = imdb_movie_tags_list_start;
		this.imdb_object = "Tags in Movies";
	}

	public imdb_tag_movie_parser(
			HashMap<String, entity_movie> parser_movie_map,
			HashMap<String, String> imdb_name_to_director,
			HashMap<String, String> imdb_to_yago,
			HashMap<String, Integer> parser_tag_count_map) {
		 super(parser_movie_map);
		 
		 this.parser_tag_count_map = parser_tag_count_map;
			
			this.filepath = this.properties.get_imdb_tags_path();
			this.list_end = null;
			this.list_start = imdb_movie_tags_list_start;
			this.imdb_object = "Tags in Movies";
		 
		
	}

	/**
	 * @param line - a line to handle
	 * Parse IMDB tags file. 
	 * Parse titles and their tags. if Parsed title agrees with 
	 * yago name, year and director, enrich Yago movie with 
	 * the MAX_TAGS most popular tags, for this movie
	 **/
	protected int handle_single_line(String line, BufferedReader br)
	{
		int c_tags=0;
		/* clean up*/
		line = line.trim();
		String[] splitted_line = line.split("\\t");
		if (splitted_line.length < 2)
			return -1;

		/*get imdb_name, tag*/
		try{
		String imdb_movie_name = clean_imdb_name(splitted_line[0]);
		String new_tag  = splitted_line[splitted_line.length -1];
		
		if (parser_tag_count_map.get(new_tag) == null)
			return -1;
		
		/*get relevant movie in map*/
		entity_movie movie = get_movie_by_imdb_name(imdb_movie_name);
		if (movie == null)
			return 0;
					
		Set<String> tags = movie.get_movie_tags();
		
		/*if less then Max, simply add*/
		if (tags.size() < MAX_TAGS)
		{
			tags.add(new_tag);
			c_tags++;
		}
		else /*more than max - replace if less popular tag exists possible*/
		{
			for (String tag : tags)
			{
				Integer new_tag_count = parser_tag_count_map.get(new_tag);
				Integer old_tag_count = parser_tag_count_map.get(tag);
				
				if (new_tag_count == null)
					continue; 
				
				if (new_tag_count > old_tag_count)
				{
					/* swap tags */
					movie.add_to_tags(new_tag);
					movie.remove_from_tags(tag);
					/*update movie in map and look for next line*/
					this.parser_movie_map.put(movie.get_movie_qualified_name(), movie);
					break;
				}
			}
		}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return c_tags;
	}

}
