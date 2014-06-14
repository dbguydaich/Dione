package parser_entities.imdb_parsers;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import parser_entities.Importer;
import parser_entities.entity_movie;

public class imdb_tagline_parser extends abstract_imdb_parser {
	private static final String imdb_tagline_list_start = "AKA TITLES LIST";

	/**
	 * constructor
	 * 
	 * @param parser_movie_map
	 * @param imdb_name_to_director
	 * @param imdb_to_yago
	 * @param importer
	 */
	public imdb_tagline_parser(
			HashMap<String, entity_movie> parser_movie_map,
			HashMap<String, String> imdb_name_to_director,
			HashMap<String, HashSet<String>> imdb_names,
			HashMap<String, String> imdb_to_yago,
			Importer importer) {
		super(parser_movie_map, imdb_name_to_director, imdb_names, imdb_to_yago, importer);
		this.filepath = this.properties.get_imdb_taglines_path();
		this.list_end = null;
		this.list_start = imdb_tagline_list_start;
		this.imdb_object = "Movie Taglines";
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
			
			String tagline = ""; 
			String imdb_movie_name = "";
			/* get name */
			if (!line.startsWith(" ") && !line.equals("")) { /* new title */
				imdb_movie_name = clean_imdb_name(line);
			} else if (line.startsWith(" ") && !line.equals("")) {
				tagline += line.trim();
			} else
				return -1;

			/* read until next item */
			try {
				while ((line = br.readLine()) != null) {
					if (line.equals(""))
						break;
					tagline += line.trim(); 
				}
				
				entity_movie movie = parser_movie_map.get(imdb_movie_name);
				/*no such movie, or already has plot*/
				if (movie == null)
					return -1; 
				else if (movie.get_movie_plot() != null)
						return -1;
				/* make changes */
				movie.set_movie_plot(tagline);
				this.parser_movie_map.put(movie.get_movie_qualified_name(),movie);

			} catch (Exception ex) {
				return -1;
			}
		} catch (Exception ex) {
			return -1;
		}
	/*success enriching this title*/
	return 1;
	}


	@Override
	public Set<String> get_enrichment_set() {
		return null;
	}

}
