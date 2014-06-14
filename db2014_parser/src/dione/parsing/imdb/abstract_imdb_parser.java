package dione.parsing.imdb;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import dione.config.config;
import dione.core.entities.entity_movie;
import dione.parsing.Iimport_task;
import dione.parsing.Importer;


/**
 * parser_src_imdb is initialized with an existing movie catalog and enriches it
 * with IMDB data. It populates tag entities, and their relations to movies
 **/

public abstract class abstract_imdb_parser implements Iimport_task {

	/* parser specific parameters */
	protected String list_start; /* imdb file list beggining string */
	protected String list_end; /* imdb file list end string */
	protected String filepath; /* imdb file to parse */
	protected String imdb_object; /* object imdb */

	/* counters */
	protected int enrich_count = 0; /* imdb list beggining string */
	protected int reject_count = 0; /**/

	/* entity maps */
	protected HashMap<String, entity_movie> parser_movie_map; /*
															 * yago movie
															 * catalog to be
															 * enriched
															 */

	/* helper maps */
	protected HashMap<String, String> imdb_to_yago; 
	/* holds all possible imdb
													 * names, that are relevant
													 * to yago films
													 */
	protected HashMap<String, String> imdb_name_to_director; /*
															 * maps imdb movie
															 * name to imdb
															 * director
															 */
	protected HashMap<String, HashSet<String>> imdb_movie_names; /*
															 * maps imdb movie
															 * name to its name
															 * in other
															 * languages
															 */
	protected HashMap<String, entity_movie> imdb_name_to_yago_movie; /*
																	 * final
																	 * mapping
																	 * of
																	 */

	protected Importer Caller;

	protected config properties = new config();
	
	/**
	 * We allow clients to register to property change events, that monitor the
	 * current file processing. This helps us display the import progress
	 **/
	
	protected List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	
	public void notifyListeners(Object object, String property,
			String oldValue, String newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, property,
					oldValue, newValue));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}

	public void removeChangeListener(PropertyChangeListener newListener) {
		listener.remove(newListener);
	}

	public int perform_task() {
		if (parse_imdb_file() > 0)
			return 1;
		return -1;
	}

	public int get_task_size() {
		return get_file_line_count();
	}

	/**
	 * returns the amount of lines in the parsed file
	 * @return
	 */
	public int get_file_line_count() {

		try {

			File file = new File(this.filepath);

			if (file.exists()) {
				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);
				int linenumber = 1;

				while (lnr.readLine() != null) {
					linenumber++;
				}
				lnr.close();
				return linenumber;
			} else {
				System.out.println("File does not exists!");
			}

		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
		return 1;
	}


	/**
	 * constructor, gets only entities to enrich
	 * @param movie_map
	 * @param caller
	 */
	public abstract_imdb_parser(HashMap<String, entity_movie> movie_map,
			Importer caller) {
		/* init movie catalog */
		this.parser_movie_map = movie_map;
		this.Caller = caller;
	}

	/**
	 * common constructor, uses the imdb_to_yago map, to enrich movies
	 * @param parser_map_movie
	 * @param imdb_name_to_yago_movie
	 * @param importer
	 */
	public abstract_imdb_parser(HashMap<String, entity_movie> parser_map_movie,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			Importer importer) {
		this.parser_movie_map = parser_map_movie;
		this.imdb_name_to_yago_movie = imdb_name_to_yago_movie;
		this.Caller = importer;
	}

	/**
	 * overloaded constructor used for names parser, to create
	 * the imdb_to_yago mapping
	 * @param movie_map
	 * @param director_map
	 * @param imdb_movie_names
	 * @param imdb_to_yago
	 */
	public abstract_imdb_parser(HashMap<String, entity_movie> movie_map,
			HashMap<String, String> director_map,
			HashMap<String, HashSet<String>> imdb_movie_names,
			HashMap<String, String> imdb_to_yago, Importer caller) {
		/* init movie catalog */
		this.parser_movie_map = movie_map;
		this.imdb_to_yago = imdb_to_yago;
		this.imdb_name_to_director = director_map;
		this.Caller = caller;
	}

	/**
	 * a YAGO film may have several descriptors, depending on its
	 * available details, and multilnagual labels. this maps sends
	 * all yago name, to their FQ name. 
	 */
	public void map_imdb_yago_names() {
		/* maps upper-case movie names, to their real fq-names */
		for (entity_movie movie : parser_movie_map.values()) {
			if (this.Caller.is_thread_terminated())
				return;
			/* set translation in hash */
			imdb_to_yago.put(movie.get_movie_qualified_name().toUpperCase(),
					movie.get_movie_qualified_name());
			/* add also foreign names */
			for (String label_fq_name : movie.get_label_fq_names())
				imdb_to_yago.put(label_fq_name.toUpperCase(),
						movie.get_movie_qualified_name());
		}
	}

	/**
	 * we look at all possible descriptors of yago title - made up of : D1 =
	 * {names}X{director_names}X{year}. we build, based on imdb data, descriptors
	 * of imdb title : D2 ={imdb_names}X{imdb_director}X{year} if
	 * |intersect(D1,D2)| > 0, we say that yago title and imdb title match
	 **/
	public void map_yago_fq_to_imdb_name() {
		/* build a map of all yago keys -> yago fqdn */
		map_imdb_yago_names();

		/* try to match according to full key */
		for (String imdb_name : this.imdb_movie_names.keySet()) {
			if (this.Caller.is_thread_terminated())
				return; 
			entity_movie yago_movie = get_movie_by_imdb_name_full(imdb_name);
			if (yago_movie != null) /* match */
				if (imdb_name_to_yago_movie.get(imdb_name) == null) /*
																	 * first
																	 * match
																	 */
					imdb_name_to_yago_movie.put(imdb_name, yago_movie);
		}

		/* if no better match, try matching by name and year */
		for (String imdb_name : this.imdb_movie_names.keySet()) {
			if (this.Caller.is_thread_terminated())
				return;
			entity_movie yago_movie = get_movie_by_imdb_name_year(imdb_name);
			if (yago_movie != null) /* match */
				if (imdb_name_to_yago_movie.get(imdb_name) == null) /*
																	 * first
																	 * match
																	 */
					imdb_name_to_yago_movie.put(imdb_name, yago_movie);
		}
		
		/*now just by name*/
		for (String imdb_name : this.imdb_movie_names.keySet()) {
			if (this.Caller.is_thread_terminated())
				return;
			entity_movie yago_movie = get_movie_by_imdb_name_only(imdb_name);
			if (yago_movie != null) /* match */
				if (imdb_name_to_yago_movie.get(imdb_name) == null) /*
																	 * first
																	 * match
																	 */
					imdb_name_to_yago_movie.put(imdb_name, yago_movie);
		}
		System.out.println("Parser found matches to " + imdb_name_to_yago_movie.size() + "YAGO titles, in IMDB");
	}

	public HashMap<String, String> get_imdb_to_yago() {
		return this.imdb_to_yago;
	}

	/**
	 *  return a set of enrichment items - tags, languages, etc. 
	 *  */
	public abstract Set<String> get_enrichment_set();

	/**
	 * splits line in some manner, tries to extract relevant data and update
	 * appropriate datastructures accordingly
	 * 
	 * @param line
	 *            - line to handle
	 * @return
	 */
	protected abstract int handle_single_line(String line, BufferedReader br);

	/**
	 * Parses a class-specific file - reading line by line, sending it to some
	 * specific parsing function, and recording rejections and enrcihments we
	 * loop until our caller has terminated
	 */
	public int parse_imdb_file() {
		int progress = 0;
		// assert file exists
		File fl = new File(this.filepath);
		if (this.filepath == null || !fl.exists())
			return -1;

		try {
			FileReader fr = new FileReader(this.filepath);
			BufferedReader br = new BufferedReader(fr);
			String line;
			/* read until start */
			while ((line = br.readLine()) != null
					&& !Caller.is_thread_terminated()) {
				progress++;
				if (line.contains(this.list_start))
					break;
			}
			/* parse until EOF */
			while ((line = br.readLine()) != null
					&& !Caller.is_thread_terminated()) {
				progress++;
				if (progress % 10000 == 0)
					this.notifyListeners(this, "progress", "0", (new Integer(
							progress)).toString());
				/* list end reached - terminate */
				if (this.list_end != null && line.contains(this.list_end))
					break;
				try {

					int ret = handle_single_line(line, br);
					if (ret >= 1)
						enrich_count += ret;
					else if (ret == 0)
						reject_count++;

				} catch (Exception ex) {
					reject_count++;
					continue;
				}
			}
		} catch (Exception ex) {
			System.out.println("ERROR parsing file:" + this.filepath);
			ex.printStackTrace();
			return -1;
		}

		System.out
				.println("Found " + enrich_count+ " enrichments for: " + this.imdb_object + " in imdb titles");
		return enrich_count;
	}

	/**
	 * remove bad characters from movie names
	 * 
	 * @param imdb_movie_name
	 * @return
	 */
	protected String clean_imdb_name(String imdb_movie_name) {
		imdb_movie_name = imdb_movie_name.replaceAll("\"", "");
		imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
		imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
		imdb_movie_name = imdb_movie_name.substring(0,
				imdb_movie_name.indexOf(")") + 1);
		return imdb_movie_name;
	}

	/**
	 * returns an array of strings, that consists of all full (name, year, director)
	 * names That we consider to match this IMDB title, based on the names
	 * and directors maps. 
	 * @param imdb_movie_name
	 * @return
	 */
	protected ArrayList<String> get_movie_keys(String imdb_movie_name) {
		ArrayList<String> keys = new ArrayList<String>();

		/* go over all names */
		Set<String> movie_names = this.imdb_movie_names.get(imdb_movie_name);

		if (movie_names == null)
			movie_names = new HashSet<String>();

		movie_names.add(imdb_movie_name);

		for (String imdb_other_name : movie_names) {
			String imdb_year = null;
			if (imdb_other_name == null || imdb_other_name.equals(""))
				continue;

			String imdb_director = null;
			String imdb_name = clean_imdb_name(imdb_other_name);
			if (imdb_name.indexOf("(") > 0) {
				imdb_year = imdb_name.substring(imdb_name.indexOf("(") + 1,
						imdb_name.indexOf(")"));
				imdb_name = imdb_name.substring(0, imdb_name.indexOf("("));
			}
			imdb_name = imdb_name.trim();

			if (imdb_name_to_director.get(imdb_movie_name) == null)
				imdb_director = "NAN";
			else
				imdb_director = imdb_name_to_director.get(imdb_movie_name);
			if (imdb_year == null || imdb_year.equals(""))
				imdb_director = "NAN";

			/* we create all possible matches for this movie */
			keys.add((imdb_name + " (" + imdb_year + ") (" + imdb_director + ")")
					.toUpperCase());
			keys.add((imdb_name + " (NAN) (" + imdb_director + ")")
					.toUpperCase());
			keys.add((imdb_name + " (" + imdb_year + ") (NAN)").toUpperCase());
			keys.add((imdb_name + " (NAN) (NAN)").toUpperCase());
		}

		return keys;

	}

	/**
	 *  Here, we create imdb descriptors, based on film name and year, only
	 **/
	protected ArrayList<String> get_movie_soft_keys(String imdb_movie_name) {
		ArrayList<String> keys = new ArrayList<String>();

		/* go over all names */
		Set<String> movie_names = this.imdb_movie_names.get(imdb_movie_name);

		if (movie_names == null)
			movie_names = new HashSet<String>();

		movie_names.add(imdb_movie_name);

		for (String imdb_other_name : movie_names) {
			String imdb_year = null;

			if (imdb_other_name == null || imdb_other_name.equals(""))
				continue;

			String imdb_name = clean_imdb_name(imdb_other_name);
			if (imdb_name.indexOf("(") > 0) {
				imdb_year = imdb_name.substring(imdb_name.indexOf("(") + 1,
						imdb_name.indexOf(")"));
				imdb_name = imdb_name.substring(0, imdb_name.indexOf("("));
			}
			imdb_name = imdb_name.trim();

			if (imdb_year == null)
				continue;

			/* we create all possible matches for this movie */
			keys.add((imdb_name + " (" + imdb_year + ")").toUpperCase());
		}

		return keys;

	}

	
	/**
	 * Here, we create imdb descriptors, based on film name only
	 **/
	protected ArrayList<String> get_movie_keys_name_only(String imdb_movie_name) {
		ArrayList<String> keys = new ArrayList<String>();

		/* go over all names */
		Set<String> movie_names = this.imdb_movie_names.get(imdb_movie_name);

		if (movie_names == null)
			movie_names = new HashSet<String>();

		movie_names.add(imdb_movie_name);

		for (String imdb_other_name : movie_names) {
			if (imdb_other_name == null || imdb_other_name.equals(""))
				continue;
			String imdb_name = clean_imdb_name(imdb_other_name);
			if (imdb_name.indexOf("(")>0)
				imdb_name = imdb_name.substring(0, imdb_name.indexOf("(")).trim();
			/* we create all possible matches for this movie */
			keys.add((imdb_name).toUpperCase());
		}
		return keys;

	}

	/**
	 * just a wrapper, all logic passed to filling this
	 **/
	protected entity_movie get_movie_by_imdb_name(String imdb_movie_name) {
		return this.imdb_name_to_yago_movie.get(imdb_movie_name);
	}

	/**
	 * we try to match imdb full descriptors, to the YAGO names of a title
	 * @param imdb_movie_name
	 *            - the imdb name we found during the parsing of some file
	 * @return the yago movie that represents this imdb movie name, null if n
	 */
	protected entity_movie get_movie_by_imdb_name_full(String imdb_movie_name) {
		ArrayList<String> movie_keys = get_movie_keys(imdb_movie_name);
		String yago_name = null;
		for (String key : movie_keys) {
			yago_name = imdb_to_yago.get(key);
			if (yago_name != null)
				break;
		}
		if (yago_name == null)
			return null;
		entity_movie movie = this.parser_movie_map.get(yago_name);
		return movie;
	}

	/**
	 * we try to match imdb name-only descriptors, to the YAGO names of a title
	 * @param imdb_movie_name
	 *            - the imdb name we found during the parsing of some file
	 * @return the yago movie that represents this imdb movie name, null if n
	 */
	protected entity_movie get_movie_by_imdb_name_only(String imdb_movie_name) {
		ArrayList<String> movie_keys = get_movie_keys_name_only(imdb_movie_name);
		String yago_name = null;
		for (String key : movie_keys) {
			yago_name = imdb_to_yago.get(key);
			if (yago_name != null)
				break;
		}
		if (yago_name == null)
			return null;
		entity_movie movie = this.parser_movie_map.get(yago_name);
		return movie;
	}

	
	/**
	 * we try to match imdb year-name descriptors, to the YAGO names of a title
	 * @param imdb_movie_name
	 *            - the imdb name we found during the parsing of some file
	 * @return the yago movie that represents this imdb movie name, null if n
	 */
	protected entity_movie get_movie_by_imdb_name_year(String imdb_movie_name) {
		ArrayList<String> movie_keys = get_movie_soft_keys(imdb_movie_name);
		String yago_name = null;
		for (String key : movie_keys) {
			yago_name = imdb_to_yago.get(key);
			if (yago_name != null)
				break;
		}
		if (yago_name == null)
			return null;
		entity_movie movie = this.parser_movie_map.get(yago_name);
		return movie;
	}

	/**
	 * silly finalizer - override where needed*/
	public void clear() {
		return;
	}
}
