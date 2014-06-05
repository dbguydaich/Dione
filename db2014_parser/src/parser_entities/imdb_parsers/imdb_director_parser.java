package parser_entities.imdb_parsers;

import java.io.BufferedReader;
import java.util.HashMap;

import parser_entities.entity_movie;

public class imdb_director_parser extends abstract_imdb_parser{

	private static final String imdb_movie_directors_list_start = "Name			Titles";
	private HashMap<String,String> imdb_name_to_director;	/* maps imdb movie name to imdb director*/
	private static String cur_director = "";				/* keeps director from previous line...*/
	
	public imdb_director_parser(HashMap<String, entity_movie> movie_map) {
		super(movie_map);
		this.filepath = this.properties.get_imdb_directors_path();
		this.list_end = null;
		this.list_start = imdb_movie_directors_list_start;
		this.imdb_object = "Directors";
		imdb_name_to_director = new HashMap<String,String>();
	}

	public HashMap<String, String> get_directors()
	{
		return this.imdb_name_to_director;
	}
	/**
	 * parses film directors, according to imdb records. later used to 
	 * determine which yago movie entities to enrich, based on this  
	 */
	protected int handle_single_line(String line, BufferedReader br) {
		int x;
		if (line.contains("Bolton, Conor Liam"))
			x = 1;

		/* clean up */
		if (line.equals(""))
			return -1;

		/* two type of lines: 1. director + movie 2.\t\tmovie */
		String splitted_line[] = line.split("\\t");

		/* bad string */
		if (splitted_line[0] == null)
			return -1;
		/* if this is a movie record, assign it to previous director */
		else if (splitted_line[0].equals("")
				&& splitted_line[splitted_line.length - 1] != null
				&& splitted_line[splitted_line.length - 1] != "") {
			String movie_name = clean_imdb_name(splitted_line[splitted_line.length - 1]);
			if (imdb_name_to_director.get(movie_name) == null) {
				imdb_name_to_director.put(movie_name, cur_director);
				return 1;
			}
		}
		/* new director */
		else if (!splitted_line[0].equals("")) {
			/* re-organize director name */
			cur_director = "";
			String[] split_director = splitted_line[0].split(",");

			/* surname, family name */
			if (split_director.length >= 2)
				cur_director = split_director[1].trim() + " ";

			/* has some imdb rubbish */
			if (cur_director.indexOf("(") > 0)
				cur_director = (cur_director.substring(0,
						cur_director.indexOf("("))).trim()
						+ " ";

			/* add surname */
			cur_director = cur_director + split_director[0].trim();

			/* movie also present in line */
			if (splitted_line[splitted_line.length - 1] != null) {
				String movie_name = clean_imdb_name(splitted_line[splitted_line.length - 1]);
				if (imdb_name_to_director.get(movie_name) == null) {
					imdb_name_to_director.put(movie_name, cur_director);
					return 1;
				}
			}
		}
		return 0;
	}

}
