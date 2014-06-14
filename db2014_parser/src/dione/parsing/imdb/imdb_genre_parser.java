package dione.parsing.imdb;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;

import dione.core.entities.entity_movie;
import dione.parsing.Importer;

/**
 * class that parses imdb genre file
 * @author GUY
 *
 */
public class imdb_genre_parser extends abstract_imdb_parser {

	/* constants */
	private static final String imdb_genres_list_start = "8: THE GENRES LIST";

	/* members */
	private HashSet<String> parser_genre_set; /* imdb entities - tags */

	/**
	 * constructor
	 * @param parser_map_movie
	 * @param imdb_name_to_yago_movie
	 * @param importer
	 */
	public imdb_genre_parser(HashMap<String, entity_movie> parser_map_movie,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			Importer importer) {
		super(parser_map_movie,imdb_name_to_yago_movie,importer);
		this.filepath = this.properties.get_imdb_genres_path();
		this.list_end = null;
		this.list_start = imdb_genres_list_start;
		this.imdb_object = "Genres";
		this.parser_genre_set = new HashSet<String>();
	}

	/* getters */
	public HashSet<String> get_enrichment_set() {
		return this.parser_genre_set;
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

			movie.add_to_genres(splitted_line[splitted_line.length - 1]);
			this.parser_movie_map.put(movie.get_movie_qualified_name(), movie);
			parser_genre_set.add(splitted_line[splitted_line.length - 1]);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

}
