package parser_entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
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
	private static final Integer MAX_TAGS = 10; 
	
	
	
	/*main movie catalog to be enriched*/
	private HashMap<String,entity_movie> parser_movie_map;
	private HashMap<String,Integer> parser_tag_count_map;
	
	/*helper hash, to find yago_name by imdb_name*/
	private HashMap<String,String> imdb_to_yago; 
	config properties = new config();
	
	public praser_src_imdb(HashMap<String,entity_movie> movie_map)
	{

		this.properties = new config();
		/*init movie catalog*/
		this.parser_movie_map = movie_map; 
		this.imdb_to_yago = new HashMap<String,String>();
		this.parser_tag_count_map = new HashMap<String,Integer>(); 
		
		/*yago names are "movie_name (sometext)", IMDB names are : "movie_name (movie year)"*/
		/*modify movie.imdb_name to "movie_name (year)"*/
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
			
			String imdb_movie_name = movie_name + " (" + movie_year + ")";
			/*update movie catalog with new imdb_name*/
			modified_movie.set_imdb_name(imdb_movie_name);
			kvp.setValue(modified_movie);
			
			/*set translation in hash*/
			imdb_to_yago.put(imdb_movie_name,modified_movie.get_movie_name());
		}
	}
	
	public void parse()
	{
		/* parse all yago files */
		/*if (properties.get_imdb_genres_path() != null)
			parse_src_imdb_genres(properties.get_imdb_genres_path());
		if (properties.get_imdb_plots_path() != null)
			parse_src_imdb_plots(properties.get_imdb_plots_path());*/
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
					String imdb_movie_name = splitted_line[0];
					imdb_movie_name =imdb_movie_name.replaceAll("\"", "");
					imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
					imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
					imdb_movie_name = imdb_movie_name.substring(0,imdb_movie_name.indexOf(")")+1);
					String yago_name = imdb_to_yago.get(imdb_movie_name);
					if (yago_name == null)
						continue;
					entity_movie movie = this.parser_movie_map.get(yago_name);
					if (movie == null)
						continue;
					movie.add_to_genres(splitted_line[splitted_line.length -1]);
					this.parser_movie_map.put(yago_name, movie);
				}
			}catch (Exception ex){
			}
		}
		catch (Exception ex){}
	}
	
	/**
	 * Parses the IMDB genres file and updates the movie table accordingly.
	 **/
	private void parse_src_imdb_tags(String imdb_tags_path)
	{		

		// assert file exists
		File fl = new File(imdb_tags_path); 
		if (imdb_tags_path == null || ! fl.exists() )
			return;

		try {
			FileReader fr = new FileReader(imdb_tags_path);
			BufferedReader br = new BufferedReader(fr);
			String line; 
			try {
				/*read until start*/
				while ((line = br.readLine()) != null)
					if (line.contains(imdb_tags_list_start))
						break;
				
				
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
							if (count < 100)
								continue;
							this.parser_tag_count_map.put(tag.trim(), count);
						}
						catch (Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
				}
				
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
					String imdb_movie_name = splitted_line[0];
					imdb_movie_name =imdb_movie_name.replaceAll("\"", "");
					imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
					imdb_movie_name = imdb_movie_name.substring(0,imdb_movie_name.indexOf(")")+1);
					String new_tag  = splitted_line[splitted_line.length -1];
					
					if (new_tag == "wrench")
						System.out.print("");
					
					if (parser_tag_count_map.get(new_tag) == null)
						continue;
					
					/*get relevant movie in map*/
					String yago_name = imdb_to_yago.get(imdb_movie_name);
					if (yago_name == null)
						continue; 
					entity_movie movie = this.parser_movie_map.get(yago_name);
					if (movie == null)
						continue; 
					Set<String> tags = movie.get_movie_tags();
					
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
	}
			
	private void parse_src_imdb_plots(String imdb_plots_path)
	{
		//set the file path
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
						temp_name = temp_name.replaceAll("\"", "");
						temp_name = temp_name.replaceAll("$#*! ", "");
						temp_name = temp_name.substring(0,temp_name.indexOf(")")+1);
						
						String yago_name = imdb_to_yago.get(temp_name);
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
						this.parser_movie_map.put(yago_name, movie);
						
					}
				}
			}catch (Exception ex){}
		}
		catch (Exception ex){}
	}
		
		private void parse_src_imdb_languages(String imdb_languages_path)
		{
			//set the file path
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
						temp_name = temp_name.replaceAll("\"", "");
						temp_name = temp_name.replaceAll("$#*! ", "");
						temp_name = temp_name.substring(0,temp_name.indexOf(")")+1);
						
						String yago_name = imdb_to_yago.get(temp_name);
						if (yago_name==null)
							continue; 
						entity_movie movie = this.parser_movie_map.get(yago_name);
						if (movie == null)
							continue;
						movie.set_movie_langage(splitted_line[1]);
						this.parser_movie_map.put(yago_name, movie);
					}
				}catch (Exception ex){}
			}
			catch (Exception ex){}
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

	