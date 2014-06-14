package dione.parsing.imdb;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import dione.core.entities.entity_movie;
import dione.parsing.Importer;



/**
 * class for parsing imdb multilangual names
 * @author GUY
 *
 */
public class imdb_movie_names_parser extends abstract_imdb_parser {
	private static final String imdb_names_list_start = "AKA TITLES LIST";

	/**
	 * 
	 * @param parser_movie_map - yago movies
	 * @param imdb_name_to_director - imdb directors of movies
	 * @param imdb_name_to_yago_movie - importer dict to fill
	 * @param importer - caller
	 */
	public imdb_movie_names_parser(
			HashMap<String, entity_movie> parser_movie_map,
			HashMap<String, String> imdb_name_to_director,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			Importer importer) {
		super(parser_movie_map,importer);
		
		/* using this results from earlier parser*/
		this.imdb_name_to_director = imdb_name_to_director;
		
		/*helper dicts*/
		this.imdb_movie_names = new HashMap<String,HashSet<String>>();
		this.imdb_to_yago = new HashMap<String,String>();
		
		/*enriching this dict*/
		this.imdb_name_to_yago_movie = imdb_name_to_yago_movie;
		
		/* misc */
		this.filepath = this.properties.get_imdb_names_path();
		this.list_end = null;
		this.list_start = imdb_names_list_start;
		this.imdb_object = "Movie Other Titles";

	}

	/**
	 * tries to parse a genre and assign to movie
	 */
	protected int handle_single_line(String line, BufferedReader br) {
		try {

			String imdb_movie_name = "";
			String aka_name = "";
			/* get name */
			if (!line.startsWith(" ") && !line.equals("")) { /* new title */
				imdb_movie_name = clean_imdb_name(line);
				this.imdb_movie_names.put(imdb_movie_name, new HashSet<String>());
				this.imdb_movie_names.get(imdb_movie_name).add(imdb_movie_name);
			} else if (line.startsWith(" ") && !line.equals("")) {
				aka_name = clean_aka_name((line.split("\t"))[0].trim());
				if (imdb_movie_names.get(imdb_movie_name) != null)
					imdb_movie_names.get(imdb_movie_name).add(aka_name);
			} else
				return -1;

			/* read until next item */
			try {
				while ((line = br.readLine()) != null) {
					if (line.equals(""))
						break;
					aka_name = clean_aka_name((line.split("\t"))[0].trim());
					if (imdb_movie_names.get(imdb_movie_name) != null)
						imdb_movie_names.get(imdb_movie_name).add(aka_name);
				}
			} catch (Exception ex) {
				return -1;
			}
		} catch (Exception ex) {
			return -1;
		}
	/*success enriching this title*/
	return 1;
	}

	/**
	 * cleans up the fact
	 * @param aka_name
	 * @return
	 */
	public String clean_aka_name(String aka_name) {
		aka_name = aka_name.replace("(aka", "");
		aka_name = aka_name.substring(0, aka_name.length() - 1);
		aka_name = clean_imdb_name(aka_name);
		return aka_name.trim();

	}

	@Override
	public Set<String> get_enrichment_set() {
		return null;
	}

	public void clear()
	{
		this.imdb_name_to_director = null;
		this.imdb_movie_names = null;
		this.imdb_name_to_director = null;
	}
	
}
