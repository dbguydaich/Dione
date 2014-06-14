package dione.parsing.imdb;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Set;

import dione.core.entities.entity_movie;
import dione.parsing.Importer;



/**
 * parses imdb taglines file, and tries to add taglines, to movies that lack plots
 * @author GUY
 *
 */
public class imdb_tagline_parser extends abstract_imdb_parser {
	private static final String imdb_tagline_list_start = "TAG LINES LIST";

	/**
	 * constructor
	 * @param parser_map_movie - yago movies
	 * @param imdb_name_to_yago_movie - imdb name to yago map
	 * @param importer - caller
	 */
	public imdb_tagline_parser(HashMap<String, entity_movie> parser_map_movie,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			Importer importer) {
		super(parser_map_movie,imdb_name_to_yago_movie,importer);
		
		this.filepath = this.properties.get_imdb_taglines_path();
		this.list_end = null;
		this.list_start = imdb_tagline_list_start;
		this.imdb_object = "Movie Taglines";
	}

	/**
	 * reads tagline, line-by-line, and tries to assign it to imdb title
	 */
	protected int handle_single_line(String line, BufferedReader br) {
		try {
			
			String tagline = ""; 
			String imdb_movie_name = "";
			/* get name */
			if (!line.startsWith(" ") && !line.equals("")) { /* new title */
				line = line.replace("# ","");
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
				
				entity_movie movie = get_movie_by_imdb_name(imdb_movie_name);
				/*no such movie, or already has plot*/
				if (movie == null)
					return -1; 
				else if (movie.get_movie_plot() != null)
						return -1;
				/* make changes */
				movie.set_movie_plot(tagline);
				this.parser_movie_map.put(movie.get_movie_qualified_name(),movie);
				return 1;

			} catch (Exception ex) {
				return -1;
			}
		} catch (Exception ex) {
			return -1;
		}
	
	}


	@Override
	public Set<String> get_enrichment_set() {
		return null;
	}

}
