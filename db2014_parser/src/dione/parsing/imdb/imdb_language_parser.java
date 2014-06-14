package dione.parsing.imdb;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;

import dione.core.entities.entity_movie;
import dione.parsing.Importer;



/**
 * class that parses imdb language file
 * @author GUY
 *
 */
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
	 * @param parser_map_movie - yago movie
	 * @param imdb_name_to_yago_movie - imdb to yago map
	 * @param importer - caller
	 */
	public imdb_language_parser(HashMap<String, entity_movie> parser_map_movie,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			Importer importer) {
		super(parser_map_movie,imdb_name_to_yago_movie,importer);
		
		this.filepath = this.properties.get_imdb_languages_path();
		this.list_end = null;
		this.list_start = imdb_languages_list_start;
		this.imdb_object = "Languages";
		parser_language_set = new HashSet<String>();
	}

	/**
	 * tries to parse a genre and assign to movie
	 */
	protected int handle_single_line(String line, BufferedReader br) {
		try {
			/* clean up */
			line = line.trim();
			String splitted_line[] = line.split("\\t");
			if (splitted_line[0] == null || splitted_line[0].equals(""))
				return -1;
			String imdb_movie_name = clean_imdb_name(splitted_line[0]);

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
