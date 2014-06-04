package parser_entities.parsers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import parser_entities.entity_movie;
import parser_entities.entity_person;

import config.config;

public abstract class abstract_yago_parser implements Iyago_parser {

	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

	private void notifyListeners(Object object, String property,
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

	public int get_file_line_count() {

		try {

			File file = new File(this.yago_file_path);

			if (file.exists()) {
				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);
				int linenumber = 0;

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
			return 0;
		}
		return 0;
	}

	public abstract void print_parse_stats();

	/* config */
	config properties;

	/* members */
	protected HashMap<String, entity_movie> parser_map_movie;
	protected HashMap<String, entity_person> parser_map_actor;
	protected HashMap<String, entity_person> parser_map_director;

	protected String yago_file_path;
	protected int yago_file_params;
	protected int file_line_progress = 0;

	/* constructor */
	public abstract_yago_parser(HashMap<String, entity_movie> movie_map,
			HashMap<String, entity_person> actor_map,
			HashMap<String, entity_person> director_map) {
		/* Init Parser members */
		if (movie_map != null)
			parser_map_movie = movie_map;
		else
			parser_map_movie = new HashMap<String, entity_movie>();

		if (actor_map != null)
			parser_map_actor = actor_map;
		else
			parser_map_actor = new HashMap<String, entity_person>();

		if (director_map != null)
			parser_map_director = director_map;
		else
			parser_map_director = new HashMap<String, entity_person>();

		/* Init Parser members */
		this.properties = new config();
	}

	/* getter for movie table */
	public HashMap<String, entity_movie> get_yag_movie_map() {
		return this.parser_map_movie;
	}

	public HashMap<String, entity_person> get_yag_actor_map() {
		return this.parser_map_actor;
	}

	public HashMap<String, entity_person> get_yag_director_map() {
		return this.parser_map_director;
	}

	/**
	 * each parses implement a different check, if the line is appropriate for
	 * parsing
	 * 
	 * @param splitted_line
	 *            - the splitted, cleaned line
	 * @return
	 */
	public abstract boolean is_line_parseble(String splitted_line[]);

	/**
	 * each parses extracts different data in different ways from lines
	 * 
	 * @param splitted_line
	 *            - the splitted, cleaned line
	 */
	public abstract void handle_line_parsing(String splitted_line[]);

	/**
	 * generic parsing method, goes through parser-specific file, line-by-line,
	 * checking if line should be parsed and parses, according to parser
	 * specific implementation
	 */
	public void parse_file() {
		// assert file exists
		File fl = new File(this.yago_file_path);
		if (this.yago_file_path == null || !fl.exists())
			return;
		// open files for read
		try {
			FileReader fr = new FileReader(this.yago_file_path);
			BufferedReader br = new BufferedReader(fr);
			try {

				String splitted_line[];
				/* read line */
				while ((splitted_line = split_and_clean_line(br,
						this.yago_file_params)) != null) {
					file_line_progress++;
					
					/*notify importer we've made some progress*/
					if (file_line_progress % 1000000 == 0)
						this.notifyListeners(this, "file_line_progress", "0", (new Integer(file_line_progress)).toString());
					
					if (splitted_line[0] == null) // bad line
						continue;
					/* check the fact is relevant */

					if (is_line_parseble(splitted_line)) {
						handle_line_parsing(splitted_line);
					}
				}
				br.close();
			} catch (Exception ex) {
				System.out.println("ERROR parsing file:" + this.yago_file_path);
				ex.printStackTrace();
				fr.close();
				br.close();
			}
		} catch (Exception ex) {
			System.out.println("ERROR parsing file:" + this.yago_file_path);
			ex.printStackTrace();
		}

		print_parse_stats();
	}

	/**
	 * when all data is available, we update our film map to work with fully
	 * qualified names, instead of the temporary yago names
	 **/
	public void update_fq_name() {
		HashMap<String, entity_movie> fq_movie_map = new HashMap<String, entity_movie>();
		for (entity_movie mv : this.parser_map_movie.values()) {
			mv.set_fq_name();
			fq_movie_map.put(mv.get_movie_qualified_name(), mv);
		}
		this.parser_map_movie = fq_movie_map;

	}

	/* helper functions */

	/* clean up tag */
	public String clean_format(String str) {
		str = str.replace("_", " ");
		if (str.startsWith("<") && str.endsWith(">"))
			str = str.substring(1, str.length() - 1);
		return str;
	}

	/*
	 * parses a TSV line in yago format, expecting a certain number of
	 * parameters
	 */
	protected String[] split_and_clean_line(BufferedReader br, int num_params) {
		String line;
		int i;
		try {

			if ((line = br.readLine()) != null) {
				/* split next line */
				line = line.trim();
				String[] splitted_line = line.split("\\t");
				/* check for expected number of parameters */
				if (splitted_line.length != num_params)
					return new String[num_params];
				for (i = 0; i < num_params; i++)
					if (splitted_line[i] != null) {
						if (splitted_line[i].startsWith("<")
								&& splitted_line[i].endsWith(">"))
							splitted_line[i] = splitted_line[i].substring(1,
									splitted_line[i].length() - 1);
					} else
						return new String[num_params];

				return splitted_line;
			} else
				return null;
		} catch (Exception ex) {
			System.out.println("error on parsing line:" + ex.getMessage());
		}
		return null;
	}
	
	public HashMap<String,entity_movie> get_parser_movies()
	{
		return this.parser_map_movie;
	}
	

}
