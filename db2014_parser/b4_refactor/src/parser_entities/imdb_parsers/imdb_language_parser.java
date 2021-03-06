package parser_entities.imdb_parsers;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;

import parser_entities.Importer;
import parser_entities.entity_movie;

public class imdb_language_parser extends abstract_imdb_parser {

	/* cosntants */
	private static final String imdb_languages_list_start = "LANGUAGE LIST";

	/* members */
	HashSet<String> parser_language_set; /* imdb entities - languge */
	
	/* getters */
	public HashSet<String> get_enrichment_set() {
		return this.parser_language_set;
	}

	/**
	 * constructor
	 * 
	 * @param parser_movie_map
	 * @param imdb_name_to_director
	 * @param imdb_movie_names 
	 * @param imdb_to_yago
	 * @param importer
	 */
	public imdb_language_parser(HashMap<String, entity_movie> parser_movie_map,
			HashMap<String, String> imdb_name_to_director,
			HashMap<String, HashSet<String>> imdb_movie_names, HashMap<String, String> imdb_to_yago, Importer importer) {
		super(parser_movie_map, imdb_name_to_director, imdb_movie_names, imdb_to_yago, importer);
		parser_language_set = new HashSet<String>();

		this.filepath = this.properties.get_imdb_languages_path();
		this.list_end = null;
		this.list_start = imdb_languages_list_start;
		this.imdb_object = "Languages";
		parser_language_set = new HashSet<String>();
	}

	/**
	 * @param imdb_languages_path
	 *            - path to languages file Parse IMDB languages file. Parse
	 *            titles and their languags. if Parsed title agrees with yago
	 *            name, year and director, enrich Yago movie with the language
	 *            for the movie, update language set
	 **/
	protected int handle_single_line(String line, BufferedReader br) {
		try {
			/* clean up */
			line = line.trim();
			String splitted_line[] = line.split("\\t");
			if (splitted_line[0] == null || splitted_line[0].equals(""))
				return -1;
			String imdb_movie_name = clean_imdb_name(splitted_line[0]);

			if (imdb_movie_name.contains("Peau neuve"))
				imdb_movie_name = clean_imdb_name(splitted_line[0]);

			/* find title and enrich */
			entity_movie movie = get_movie_by_imdb_name(imdb_movie_name);

			if (movie == null)
				return 0;
			movie.set_movie_langage(splitted_line[1]);
			parser_language_set.add(splitted_line[1]);
			this.parser_movie_map.put(movie.get_movie_qualified_name(), movie);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}


}
