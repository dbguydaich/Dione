package parser_entities.imdb_parsers;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;

import parser_entities.Importer;
import parser_entities.entity_movie;

public class imdb_genre_parser extends abstract_imdb_parser{

	private static final String imdb_genres_list_start = "8: THE GENRES LIST";
	private HashSet<String> parser_genre_set;					/* imdb entities - tags*/

	public imdb_genre_parser(HashMap<String, entity_movie> parser_movie_map,
			HashMap<String, String> imdb_name_to_director,
			HashMap<String, String> imdb_to_yago, Importer importer) {
		super(parser_movie_map,imdb_name_to_director,imdb_to_yago,importer);
		this.filepath = this.properties.get_imdb_genres_path();
		this.list_end = null;
		this.list_start = imdb_genres_list_start;
		this.imdb_object = "Genres";
		this.parser_genre_set = new HashSet<String>();
	}

	/**
	 * @param imdb_languages_path - path to languages file
	 * Parse IMDB languages file. 
	 * Parse titles and their languags. if Parsed title agrees with 
	 * yago name, year and director, enrich Yago movie with 
	 * the language for the movie, update language set
	 **/
	protected int handle_single_line(String line, BufferedReader br)
	{
		/* clean up*/
		line = line.trim();
		String splitted_line[] = line.split("\\t");
		if (splitted_line[0] == null || splitted_line[0].equals(""))
			return -1;
		String imdb_movie_name = clean_imdb_name(splitted_line[0]);
		
		/*find title and enrich*/
		entity_movie movie = get_movie_by_imdb_name(imdb_movie_name);
		
		if (movie == null)		
			return 0;
		
		movie.add_to_genres(splitted_line[splitted_line.length -1]);
		this.parser_movie_map.put(movie.get_movie_qualified_name(), movie);
		parser_genre_set.add(splitted_line[splitted_line.length -1]);
		return 1;
	}
	public HashSet<String> get_enrichment_set()
	{
		return this.parser_genre_set;
	}


}
