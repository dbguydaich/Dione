package dione.parsing.imdb;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Set;

import dione.core.entities.entity_movie;
import dione.parsing.Importer;

/**
 * parses movie plots
 * @author GUY
 *
 */
public class imdb_plot_parser extends abstract_imdb_parser {
	private static final String imdb_plots_list_start = "PLOT SUMMARIES LIST";
	private static final String imdb_plots_item_start = "MV:";
	private static final String imdb_plots_item_text = "PL:";
	private String prev_title = "";
	
	
	int batch_count = 0;

	public imdb_plot_parser(HashMap<String, entity_movie> parser_map_movie,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			Importer importer) {
		super(parser_map_movie,imdb_name_to_yago_movie,importer);
		this.filepath = this.properties.get_imdb_plots_path();
		this.list_end = null;
		this.list_start = imdb_plots_list_start;
		this.imdb_object = "Plots";
	}

	/**
	 * tries to parse a plot and assign to movie. We read several plot lines
	 * until finished reading current plot. 
	 */
	protected int handle_single_line(String line, BufferedReader br) {
		
		try {
			
			String temp_name = "";
			String imdb_movie_name = "";
			String plot = "";

			/* get name */
			if (line.contains(imdb_plots_item_start)) {
				temp_name = (line.replace(imdb_plots_item_start, "")).trim();
				imdb_movie_name = clean_imdb_name(temp_name);
				this.prev_title = imdb_movie_name;
			} else if (line.contains(imdb_plots_item_text)
					&& !this.prev_title.equals("")) {
				temp_name = this.prev_title;
				plot = line.replace(imdb_plots_item_text, "");
			} else
				return -1;

			/* read until next item */
			try {
				while ((line = br.readLine()) != null) {
					if (line.contains(imdb_plots_item_text))
						plot += line.replace(imdb_plots_item_text, "");
					if (line.contains(imdb_plots_item_start)) {
						temp_name = (line.replace(imdb_plots_item_start, ""))
								.trim();
						imdb_movie_name = clean_imdb_name(temp_name);
						this.prev_title = imdb_movie_name;
						break;
					}
				}

				entity_movie movie = get_movie_by_imdb_name(imdb_movie_name);

				if (movie == null)
					 return 0;
				if (movie.get_movie_plot() != null)
					return 0;
				else {
					movie.set_movie_plot(plot);
					this.parser_movie_map.put(movie.get_movie_qualified_name(),
							movie);
					return 1;
				}
	
			} catch (Exception ex) {
				return -1;
			}
		
		} catch (Exception ex) {
			return -1;
		}
	}

	@Override
	public Set<String> get_enrichment_set() {
		// TODO Auto-generated method stub
		return null;
	}

}
