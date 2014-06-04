package parser_entities.imdb_parsers;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import parser_entities.entity_movie;

public class imdb_plot_parser extends abstract_imdb_parser{
	private static final String imdb_plots_list_start = "PLOT SUMMARIES LIST";
	private static final String imdb_plots_item_start = "MV:";
	private static final String imdb_plots_item_text = "PL:";
	private static String prev_title = "";

	
	public imdb_plot_parser(HashMap<String, entity_movie> movie_map) {
		super(movie_map);
		this.filepath = this.properties.get_imdb_plots_path();
		this.list_end = null;
		this.list_start = imdb_plots_list_start;
		this.imdb_object = "Plots";
	}

	public imdb_plot_parser(HashMap<String, entity_movie> parser_movie_map,
			HashMap<String, String> imdb_name_to_director,
			HashMap<String, String> imdb_to_yago) {
		super(parser_movie_map,imdb_name_to_director,imdb_to_yago);
		this.filepath = this.properties.get_imdb_plots_path();
		this.list_end = null;
		this.list_start = imdb_plots_list_start;
		this.imdb_object = "Plots";
	}

	/**
	 * @param imdb_languages_path - path to languages file
	 * Parse IMDB languages file. 
	 * Parse titles and their languags. if Parsed title agrees with 
	 * yago name, year and director, enrich Yago movie with 
	 * the language for the movie, update language set
	 **/
	protected int handle_single_line(String line, BufferedReader br)
	{
		String temp_name = "";
		String imdb_movie_name = "";
		String plot = "";
		
		/*get name*/
		if (line.contains(imdb_plots_item_start)){
			temp_name = (line.replace(imdb_plots_item_start, "")).trim();
			imdb_movie_name = clean_imdb_name(temp_name);
			this.prev_title =imdb_movie_name;
		}
		else if (line.contains(imdb_plots_item_text) && !this.prev_title.equals(""))
		{
			temp_name = this.prev_title;
			plot = line.replace(imdb_plots_item_text, "");
		}
		else
			return -1;

		/* read until next item*/
		try{ 
			while((line = br.readLine()) != null)
			{
				if (line.contains(imdb_plots_item_text))
					plot+= line.replace(imdb_plots_item_text, "");
				if (line.contains(imdb_plots_item_start))
				{
					temp_name = (line.replace(imdb_plots_item_start, "")).trim();
					imdb_movie_name = clean_imdb_name(temp_name);
					this.prev_title =imdb_movie_name;
					break;
				}
			}
			
			ArrayList<String> movie_keys  = get_movie_keys(imdb_movie_name);
			entity_movie movie = get_movie_by_imdb_name(imdb_movie_name);
			
			if (movie == null)
				return 0;
			if (movie.get_movie_plot() != null)
				return 0;
			else
			{
				movie.set_movie_plot(plot);
				this.parser_movie_map.put(movie.get_movie_qualified_name(), movie);
				return 1;
			}
				
		}
		catch (Exception ex)
		{
			return -1;
		}
		/*finished this item, if possible, update*/
		//if (movie == null)
		//{
			//return 0;
			/*if (!reject_set.contains(imdb_movie_name))
			{
				reject_set.add(imdb_movie_name);
			
				stmt.setString(1, imdb_movie_name);
				stmt.addBatch(); 
				batch_count++;
				if (batch_count % 10000 == 0)
				{
					try{
						stmt.executeBatch();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			}*/
		//}
		
	//return 1;
	}

	@Override
	public Set<String> get_enrichment_set() {
		// TODO Auto-generated method stub
		return null;
	}

}
