package parser_entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import config.config;

/** parser_src_imdb is initialized with an existing 
 * movie catalog and enriches it with IMDB data.  
 * It populates tag entities, and their relations to movies**/


public class praser_src_imdb {
	
	/*constants*/
	private static final String imdb_genres_list_start = "8: THE GENRES LIST";
	private static final String imdb_plots_list_start = "PLOT SUMMARIES LIST";
	private static final String imdb_plots_item_start = "MV:";
	private static final String imdb_plots_item_text = "PL:";
	private static final String imdb_languages_list_start = "LANGUAGE LIST";
	private static final String imdb_tags_list_start = "keywords in use:";
	private static final String imdb_tags_list_end = "5: Submission Rules";
	private static final String imdb_movie_tags_list_start = "8: THE KEYWORDS LIST";
	private static final String imdb_movie_directors_list_start = "Name			Titles";
	private static final Integer MAX_TAGS = 10; 
	private static final Integer MIN_TAG_COUNT = 100;
	
	
	
	/*main movie catalog to be enriched*/
	private HashMap<String,entity_movie> parser_movie_map;
	/*side objects - languges, genres*/
	private HashSet<String> parser_language_set; 
	private HashSet<String> parser_genre_set;
	private HashSet<String> parser_tag_set;
	
	
	
	/*helper hash, to find yago_name by imdb_name*/
	private HashMap<String,String> imdb_to_yago;
	/*helper hash, to get tag count in IMDB, for filtering*/
	private HashMap<String,Integer> parser_tag_count_map;
	/*helper hash, to get the IMDB director of a movie*/
	private HashMap<String,String> imdb_name_to_director; 
	
	config properties = new config();
	
	/* counters */
	private int c_plots = 0; 
	private int c_tags = 0;
	private int c_genres = 0;
	private int c_languages = 0;
	
	
	public void print_imdb_stats()
	{
		System.out.println("IMDB enrichment\n\t plots: " + c_plots + "\n\t tags: " + c_tags + "\n\t genres: " + c_genres + "\n\t languages: " + c_languages);
	}
	public HashSet<String> get_parser_languages()
	{
		return this.parser_language_set;
	}
	
	public HashSet<String> get_parser_genres()
	{
		return this.parser_genre_set;
	}

	public HashSet<String> get_parser_tags()
	{
		return this.parser_tag_set;
	}
	/*constructor*/
	public praser_src_imdb(HashMap<String,entity_movie> movie_map)
	{

		this.properties = new config();
		/*init movie catalog*/
		this.parser_movie_map = movie_map; 
		this.imdb_to_yago = new HashMap<String,String>();
		this.parser_tag_count_map = new HashMap<String,Integer>();
		this.imdb_name_to_director = new HashMap<String,String>();
		this.parser_genre_set = new HashSet<String>();
		this.parser_language_set = new HashSet<String>();
		this.parser_tag_set = new HashSet<String>();
		
		get_imdb_directors();
		
		/* yago names are "movie_name (sometext)", IMDB names are : "movie_name (movie year/num in year)"
		 * assume that no film with the same name and same director aired in the same year, to create a map
		 * given yago_name, year and director, we create a logical "key" : name (year) (director). 
		 * if IMDB record agrees on these three, we use it to enrich yago element
		 * with plot, language, tags, genres*/
		for (Entry<String,entity_movie> kvp : movie_map.entrySet())
		{
			/* get source entity_movie*/
			entity_movie modified_movie = kvp.getValue();
			/*get name, year, make new name and update*/
			String movie_name = modified_movie.get_movie_name();
			/*remove  (sometext) from yago name*/
			if (movie_name.contains(" ("))
			{
				String comments = movie_name.substring(movie_name.indexOf(" ("),movie_name.lastIndexOf(")")+1);
				movie_name = movie_name.replace(comments, "");
			}
			/*add (year)*/
			String movie_year = modified_movie.get_movie_year();
			if (movie_year == null)
				continue;
			entity_person director = modified_movie.get_movie_director();
			if (director == null)
				continue;
			String movie_director = director.get_person_name();
			if (movie_director == null)
				continue;
			
			String imdb_movie_name = movie_name + " (" + movie_year + ")" + " (" + movie_director + ")";
			/*update movie catalog with new imdb_name*/
			modified_movie.set_imdb_name(imdb_movie_name);
			kvp.setValue(modified_movie);
			
			/*set translation in hash*/
			imdb_to_yago.put(imdb_movie_name,modified_movie.get_movie_name());
		}
	}
	
	/*expects imdb name: "movie (year/number)"
	 * return movie key: movie (year) (director)*/
	public String get_movie_key(String imdb_movie_name)
	{
		if (imdb_name_to_director.get(imdb_movie_name) == null)
			return clean_imdb_name(imdb_movie_name); 
		String movie_key = clean_imdb_name(imdb_movie_name) + " (" +imdb_name_to_director.get(imdb_movie_name) + ")";
		return movie_key;
		
	}
	
	/* helper function - we parse IMDB titles by directors file
	 * to establish a yago-IMDB key based on name,year,director */
	public void get_imdb_directors()
	{
		if (properties.get_imdb_directors_path() == null)
			return; 
		File fl = new File(properties.get_imdb_directors_path());
		if (! fl.exists())
			return;

		try {
			FileReader fr = new FileReader(properties.get_imdb_directors_path());
			BufferedReader br = new BufferedReader(fr);
			String line;
			String cur_director = "";
			try {
				/*read until start*/
				while ((line = br.readLine()) != null)
				{	
					if (line.contains(imdb_movie_directors_list_start))
					{
						line = br.readLine();
						break;
					}
				}
				while ((line = br.readLine()) != null)
				{
					if (line.equals(""))
						continue; 
					String[] splitted_line = line.split("\\t");
					if (splitted_line[0] == null)
						continue; 
					/*new director*/
					if (!splitted_line[0].equals("")) 
						{
						/*re-organize director name*/
						cur_director = "";
						String[] split_director = splitted_line[0].split(",");
						if (split_director.length >= 2)
							cur_director = split_director[1].trim() + " ";
						cur_director = cur_director + split_director[0].trim(); 
						/*movie also present in line*/
						if (splitted_line[splitted_line.length -1] != null) 
							imdb_name_to_director.put(clean_imdb_name(splitted_line[splitted_line.length -1]), cur_director);
						}
					else
					{
						/*only movie in line*/
						if (splitted_line[splitted_line.length -1] != null) 
							imdb_name_to_director.put(clean_imdb_name(splitted_line[splitted_line.length -1]), cur_director);
					}					
				}
						
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				}
			}
			catch (Exception ex) {
				
			}
	}

	/*remove bad characters from movie names*/
	private String clean_imdb_name (String imdb_movie_name)
	{
		imdb_movie_name =imdb_movie_name.replaceAll("\"", "");		
		imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
		imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
		imdb_movie_name = imdb_movie_name.substring(0,imdb_movie_name.indexOf(")")+1);
		return imdb_movie_name;
	}
	
	public void parse()
	{
		/* parse all yago files */
		if (properties.get_imdb_genres_path() != null)
			parse_src_imdb_genres(properties.get_imdb_genres_path());
		if (properties.get_imdb_plots_path() != null)
			parse_src_imdb_plots(properties.get_imdb_plots_path());
		if (properties.get_imdb_languages_path() != null)
			parse_src_imdb_languages(properties.get_imdb_languages_path());
		if (properties.get_imdb_tags_path() != null)
			parse_src_imdb_tags(properties.get_imdb_tags_path());
		
	}
	
	/**
	 * Parses the IMDB genres file and updates the movie table accordingly.
	 **/
	private void parse_src_imdb_genres(String imdb_genres_path)
	{		
		int genre_count = 0; 
		// assert file exists
		File fl = new File(imdb_genres_path);
		if (imdb_genres_path == null || ! fl.exists() )
			return;

		try {
			FileReader fr = new FileReader(imdb_genres_path);
			BufferedReader br = new BufferedReader(fr);
			String line; 
			try {
				/*read until start*/
				while ((line = br.readLine()) != null)
					if (line.contains(imdb_genres_list_start))
						break;
				/*parse*/
				while ((line = br.readLine()) != null)
				{
					String splitted_line[] = line.split("\\t");
					if (splitted_line[0] == null)
						continue;
					String imdb_movie_name = clean_imdb_name(splitted_line[0]);
					String imdb_movie_key = get_movie_key(imdb_movie_name);
					String yago_name = imdb_to_yago.get(imdb_movie_key);
					if (yago_name == null)
						continue;
					entity_movie movie = this.parser_movie_map.get(yago_name);
					if (movie == null)
						continue;
					movie.add_to_genres(splitted_line[splitted_line.length -1]);
					this.parser_movie_map.put(yago_name, movie);
					parser_genre_set.add(splitted_line[splitted_line.length -1]);
					c_genres++;
				}
			}catch (Exception ex){
			}
		}
		catch (Exception ex){}
	}
	
	/**
	 * @param imdb_tags_path - path to tags file
	 * Parse IMDB tags file. 
	 * first, Read all tags, and keep only ones  
	 * with more than MIN_TAG_COUNT appearnces. 
	 * Parse titles and their tags. if Parsed title agrees with 
	 * yago name, year and director, enrich Yago movie with 
	 * the MAX_TAGS most popular tags, for this movie 
	 **/
	private void parse_src_imdb_tags(String imdb_tags_path)
	{		
		int tags_count = 0; 
		// assert file exists
		File fl = new File(imdb_tags_path); 
		if (imdb_tags_path == null || ! fl.exists() )
			return;
		/*open file for both phases*/
		try {
			FileReader fr = new FileReader(imdb_tags_path);
			BufferedReader br = new BufferedReader(fr);
			String line; 
			try {
				/*read until start*/
				while ((line = br.readLine()) != null)
					if (line.contains(imdb_tags_list_start))
						break;
				
				/*phase 1 - parse all tags*/
				String splitted_line[];
				/*parse tag1 (count) tag2 (count) tag3 (count) ,....*/ 
				while ((line = br.readLine()) != null) 
				{
					/*if finished tags list, break*/
					if (line.contains(imdb_tags_list_end))
						break;
					
					/*if still tags list*/
					line = line.trim();
					splitted_line = line.split("\\t");
					
					int i;
					for (i=0; i< splitted_line.length; i++)
					{
						String tag_count = splitted_line[i];
						if (tag_count == null || tag_count == "" 
							|| !tag_count.contains("(") || !tag_count.contains(")"))
							continue;
						try { // try parse tag
							String tag = splitted_line[i].substring(0,splitted_line[i].indexOf("("));
							tag_count = tag_count.substring(tag_count.indexOf("(")+1 , tag_count.indexOf(")"));
							Integer count = new Integer(tag_count);
							if (count < MIN_TAG_COUNT)
								continue;
							this.parser_tag_count_map.put(tag.trim(), count);
							this.parser_tag_set.add(tag.trim());
						}
						catch (Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
				}
				
				/*phase 2 - attach tags to movies*/
				/*read until imdb_movie_name tag part*/
				while ((line = br.readLine()) != null)
					if (line.contains(imdb_movie_tags_list_start))
						break;
				
				while ((line = br.readLine()) != null)
				{

					/*if still tags list*/
					line = line.trim();
					splitted_line = line.split("\\t");
					if (splitted_line.length < 2)
						continue; 
					/*get imdb_name, tag*/
					String imdb_movie_name = clean_imdb_name(splitted_line[0]);
					String new_tag  = splitted_line[splitted_line.length -1];
					
					if (parser_tag_count_map.get(new_tag) == null)
						continue;
					
					/*get relevant movie in map*/
					String imdb_movie_key = get_movie_key(imdb_movie_name);
					String yago_name = imdb_to_yago.get(imdb_movie_key);
					
					if (yago_name == null)
						continue; 
					entity_movie movie = this.parser_movie_map.get(yago_name);
					if (movie == null)
						continue; 
					Set<String> tags = movie.get_movie_tags();
					tags_count++;
					
					/*if less then Max, simply add*/
					if (tags.size() < MAX_TAGS)
						tags.add(new_tag);
					else /*more than max - replace if less popular tag exists possible*/
					{
						for (String tag : tags)
						{
							Integer new_tag_count = parser_tag_count_map.get(new_tag);
							Integer old_tag_count = parser_tag_count_map.get(tag);
							
							if (new_tag_count == null)
								continue; 
							
							if (new_tag_count > old_tag_count)
							{
								/* swap tags */
								movie.add_to_tags(new_tag);
								movie.remove_from_tags(tag);
								/*update movie in map and look for next line*/
								this.parser_movie_map.put(yago_name, movie);
								c_tags++;
								break;
							}
						}
					}
				}
				
			}catch (Exception ex){
				System.out.println(ex.getMessage());
			}
		}
		catch (Exception ex){}
		System.out.println("added tags to:" + tags_count);
	}

	/**
	 * @param imdb_plots_path - path to tags file
	 * Parse IMDB plots file. 
	 * Parse titles and their plots. if Parsed title agrees with 
	 * yago name, year and director, enrich Yago movie with 
	 * the plot for the movie 
	 **/
	private void parse_src_imdb_plots(String imdb_plots_path)
	{
		int plot_count=0; 
		// assert file exists
		File fl = new File(imdb_plots_path);
		if (imdb_plots_path == null || ! fl.exists() )
			return;

		try {
			FileReader fr = new FileReader(imdb_plots_path);
			BufferedReader br = new BufferedReader(fr);
			String line; 
			try {
				/*read until start*/
				while ((line = br.readLine()) != null)
					if (line.contains(imdb_plots_list_start))
						break;
				String splitted_line[];
				
				/*parse*/
				while ((line = br.readLine()) != null)
				{
					if (line.contains(imdb_plots_item_start))
					{
						/*get name*/
						String temp_name = line.replace("MV: ", "");
						String imdb_movie_name = clean_imdb_name(temp_name);
						String imdb_movie_key = get_movie_key(imdb_movie_name);
						String yago_name = imdb_to_yago.get(imdb_movie_key);
						
						if (yago_name == null)
							continue; 
						
						/* read until next item*/
						String plot = br.readLine();
						plot.replace(imdb_plots_item_text, "");
						while((line = br.readLine()).contains(imdb_plots_item_text))
						{
							plot+= line.replace(imdb_plots_item_text, "");
						}
						
						entity_movie movie = this.parser_movie_map.get(yago_name);
						movie.set_movie_plot(plot);
						plot_count++;
						this.parser_movie_map.put(yago_name, movie);
						c_plots++;
					}
				}
			}catch (Exception ex){}
		}
		catch (Exception ex){}
		System.out.println("added plot to" + plot_count);
	}
	

	/**
	 * @param imdb_languages_path - path to languages file
	 * Parse IMDB languages file. 
	 * Parse titles and their languags. if Parsed title agrees with 
	 * yago name, year and director, enrich Yago movie with 
	 * the language for the movie, update language set
	 **/
	private void parse_src_imdb_languages(String imdb_languages_path)
		{
			int language_count = 0;
			// assert file exists
			File fl = new File(imdb_languages_path);
			if (imdb_languages_path == null || ! fl.exists() )
				return;

			try {
				FileReader fr = new FileReader(imdb_languages_path);
				BufferedReader br = new BufferedReader(fr);
				String line; 
				try {
					/*read until start*/
					while ((line = br.readLine()) != null)
						if (line.contains(imdb_languages_list_start))
							break;
					/*parse*/
					while ((line = br.readLine()) != null)
					{
						line = line.trim();
						String splitted_line[] = line.split("\\t");
						String temp_name = splitted_line[0];
						/* clean up*/
						String imdb_movie_name = clean_imdb_name(splitted_line[0]);
						String imdb_movie_key = get_movie_key(imdb_movie_name);
						String yago_name = imdb_to_yago.get(imdb_movie_key);
						
						if (yago_name==null)
							continue; 
						entity_movie movie = this.parser_movie_map.get(yago_name);
						if (movie == null)
							continue;
						movie.set_movie_langage(splitted_line[1]);
						parser_language_set.add(splitted_line[1]);
						language_count++;
						this.parser_movie_map.put(yago_name, movie);
						c_languages++;
					}
				}catch (Exception ex){}
			}
			catch (Exception ex){}
			System.out.println("added_language to :" + language_count);
		}
			
		private String[] get_line_parsing(BufferedReader br)
		{		
			String line;
			int i;
			
			try{
				if((line = br.readLine()) != null) 
				{ 
					/*split next line*/
					line = line.trim();
					String[] splitted_line = line.split("\\t");
					/*check for expected number of parameters*/
					if (splitted_line.length != 2)
						return new String[2];
					/*take "name (year)" part*/
					splitted_line[0] = splitted_line[0].substring(0,splitted_line[0].indexOf(")")+1);  
					
					return splitted_line;
				}
				else
					return null;
			}
			catch (Exception ex){
				System.out.println("error on parsing line:" + ex.getMessage());
			}
			return null;
		}

		
}

	