package dione.parsing.imdb;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Set;

import dione.core.entities.entity_movie;
import dione.parsing.Importer;



/**
 * class for parsing the tag file, and extracting tag-movie relationships
 * @author GUY
 *
 */
public class imdb_tag_movie_parser extends abstract_imdb_parser {

	/* constants */
	private static final String imdb_movie_tags_list_start = "8: THE KEYWORDS LIST";
	private static final Integer MAX_TAGS = 10;

	/* members */
	/* handles tag counts, to establish top 10 per movie */
	private HashMap<String, Integer> parser_tag_count_map;  
															 
	/**
	 * constructor
	 * @param parser_map_movie - yago movies 
	 * @param imdb_name_to_yago_movie - imdb name to yago movie map
	 * @param parser_tag_count_map - tag frequencies in imdb
	 * @param importer - caller
	 */
	public imdb_tag_movie_parser(
			HashMap<String, entity_movie> parser_map_movie,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			HashMap<String, Integer> parser_tag_count_map, Importer importer) {
		
		super(parser_map_movie,imdb_name_to_yago_movie,importer);
		
		this.parser_tag_count_map = parser_tag_count_map;
		this.filepath = this.properties.get_imdb_tags_path();
		this.list_end = null;
		this.list_start = imdb_movie_tags_list_start;
		this.imdb_object = "Tags in Movies";
	}

	/* getters */
	@Override
	public Set<String> get_enrichment_set() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * tries to parse the tags related to this film, and decides
	 * which of them to add to its tag set, according to frequency
	 */
	protected int handle_single_line(String line, BufferedReader br) {
		try {
			int c_tags = 0;
			/* clean up */
			line = line.trim();
			String[] splitted_line = line.split("\\t");
			if (splitted_line.length < 2)
				return -1;

			/* get imdb_name, tag */
			try {
				String imdb_movie_name = clean_imdb_name(splitted_line[0]);
				String new_tag = splitted_line[splitted_line.length - 1];

				if (parser_tag_count_map.get(new_tag) == null)
					return -1;

				/* get relevant movie in map */
				entity_movie movie = get_movie_by_imdb_name(imdb_movie_name);
				if (movie == null)
					return 0;

				Set<String> tags = movie.get_movie_tags();

				/* if less then Max, simply add */
				if (tags.size() < MAX_TAGS) {
					movie.add_to_tags(new_tag);
					this.parser_movie_map.put(movie.get_movie_qualified_name(),
							movie);
					c_tags++;
				} else /*
						 * more than max - replace if less popular tag exists
						 * possible
						 */
				{
					for (String tag : tags) {
						Integer new_tag_count = parser_tag_count_map
								.get(new_tag);
						Integer old_tag_count = parser_tag_count_map.get(tag);

						if (new_tag_count == null)
							continue;

						if (new_tag_count > old_tag_count) {
							/* swap tags */
							movie.add_to_tags(new_tag);
							movie.remove_from_tags(tag);
							/* update movie in map and look for next line */
							this.parser_movie_map.put(
									movie.get_movie_qualified_name(), movie);
							break;
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return c_tags;
		} catch (Exception ex) {
			return 0;
		}
	}



}
