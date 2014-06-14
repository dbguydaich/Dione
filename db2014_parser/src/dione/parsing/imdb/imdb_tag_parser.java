package dione.parsing.imdb;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;

import dione.core.entities.entity_movie;
import dione.parsing.Importer;



/**
 * 
 * @author GUY
 * parses genereal tags from tag file
 */
public class imdb_tag_parser extends abstract_imdb_parser {

	/* cosntants */
	private static final String imdb_tags_list_start = "keywords in use:";
	private static final String imdb_tags_list_end = "5: Submission Rules";
	private static final Integer MIN_TAG_COUNT = 200;

	/* members */
	private HashSet<String> parser_tag_set; /* imdb entities - tags */
	/* handles tag counts, to establish top 10 per movie */
	private HashMap<String, Integer> parser_tag_count_map;

	/**
	 * constructor
	 * @param parser_map_movie - yago movies
	 * @param imdb_name_to_yago_movie - imdb name to yago film
	 * @param parser_tag_count_map - tag frequency, to be filled 
	 * @param importer - caller
	 */
	public imdb_tag_parser(HashMap<String, entity_movie> parser_map_movie,
			HashMap<String, entity_movie> imdb_name_to_yago_movie,
			HashMap<String, Integer> parser_tag_count_map, Importer importer) {
		super(parser_map_movie,imdb_name_to_yago_movie,importer);
		parser_tag_set = new HashSet<String>();
		this.parser_tag_count_map = parser_tag_count_map;

		this.filepath = this.properties.get_imdb_tags_path();
		this.list_end = imdb_tags_list_end;
		this.list_start = imdb_tags_list_start;
		this.imdb_object = "Tags";

		
	}

	public HashSet<String> get_enrichment_set() {
		return this.parser_tag_set;
	}

	public HashMap<String, Integer> get_parser_tag_counts() {
		return this.parser_tag_count_map;
	}
	
	/**
	 * @param line
	 *            - a tag line to parse Parse IMDB tags file. Read all tags, and
	 *            keep only ones with more than MIN_TAG_COUNT appearnces.
	 **/
	protected int handle_single_line(String line, BufferedReader br) {
		try {
			/* clean up */
			line = line.trim();
			String splitted_line[] = line.split("\\t");

			int i;
			int tag_pass = 0;
			for (i = 0; i < splitted_line.length; i++) {
				try {
					String tag_count = splitted_line[i];
					/* is this really a tag? */
					if (tag_count == null || tag_count == ""
							|| !tag_count.contains("(")
							|| !tag_count.contains(")"))
						continue;

					String tag = splitted_line[i].substring(0,
							splitted_line[i].indexOf("("));
					tag_count = tag_count.substring(tag_count.indexOf("(") + 1,
							tag_count.indexOf(")"));
					/* is numeric? */
					if (!tag_count.matches("-?\\d+(\\.\\d+)?"))
						continue;
					Integer count = new Integer(tag_count);

					/* is this an intersting tag? */
					if (count < MIN_TAG_COUNT)
						continue;

					this.parser_tag_count_map.put(tag.trim(), count);
					this.parser_tag_set.add(tag.trim());
					tag_pass++;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return tag_pass;
		} catch (Exception ex) {
			return 0;
		}
	}

}
