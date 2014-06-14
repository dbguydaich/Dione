package parser_entities.imdb_parsers;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import parser_entities.Importer;
import parser_entities.entity_movie;

public class imdb_movie_names_parser extends abstract_imdb_parser {
	private static final String imdb_names_list_start = "AKA TITLES LIST";
	private String prev_title = "";

	private HashMap<String, HashSet<String>> imdb_movie_names;

	/**
	 * constructor
	 * 
	 * @param parser_movie_map
	 * @param imdb_name_to_director
	 * @param imdb_to_yago
	 * @param importer
	 */
	public imdb_movie_names_parser(
			HashMap<String, entity_movie> parser_movie_map,
			HashMap<String, String> imdb_name_to_director,
			HashMap<String, HashSet<String>> imdb_names,
			HashMap<String, String> imdb_to_yago,
			Importer importer) {
		super(parser_movie_map, imdb_name_to_director, imdb_names, imdb_to_yago, importer);
		this.filepath = this.properties.get_imdb_names_path();
		this.list_end = null;
		this.list_start = imdb_names_list_start;
		this.imdb_object = "Movie Other Titles";
		this.imdb_movie_names = imdb_names;
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
			String temp_name = "";
			String imdb_movie_name = "";
			String aka_name = "";
			/* get name */
			if (!line.startsWith(" ") && !line.equals("")) { /* new title */
				imdb_movie_name = clean_imdb_name(line);
				this.prev_title = imdb_movie_name;
				this.imdb_movie_names.put(imdb_movie_name, new HashSet<String>());
				this.imdb_movie_names.get(imdb_movie_name).add(imdb_movie_name);
				if (imdb_movie_name.contains("Peau neuve"))
					this.prev_title = imdb_movie_name;
			} else if (line.startsWith(" ") && !line.equals("")) {
				temp_name = this.prev_title;
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

}
