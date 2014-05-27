package parser_entities;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import db.db_queries_persons; 

import config.config; 

/* A tester Class For Parsing Utilities*/
public class parser_tester {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		parser_src_yago yago_parser = new parser_src_yago();
		yago_parser.parse();
		yago_parser.print_parse_stats();
		
		praser_src_imdb imdb_parser = new praser_src_imdb(yago_parser.get_yag_movie_map());
		imdb_parser.parse();
		imdb_parser.print_imdb_stats();
		
		/* after parsing, start loading data to db */
		/*peripherial data*/
		try {
			importer db_importer = new importer();
			/*new*/
			db_importer.update_objects_by_name_batch(imdb_parser.get_parser_languages(), importer.OBJECT_LANGUAGE);
			db_importer.update_objects_by_name_batch(imdb_parser.get_parser_genres(), importer.OBJECT_GENRE);
			db_importer.update_objects_by_name_batch(imdb_parser.get_parser_tags(), importer.OBJECT_TAG);
			/*entities*/
			db_importer.update_person_entities_table(yago_parser.get_yag_actor_map(), importer.PERSON_TYPE_ACTOR);
			db_importer.update_person_entities_table(yago_parser.get_yag_director_map(), importer.PERSON_TYPE_DIRECTOR);
			db_importer.update_movies_table(yago_parser.get_yag_movie_map());
			/*movie many to many*/
			db_importer.update_movie_multiple_attributes(yago_parser.get_yag_movie_map(), importer.MOVIE_ATTRIBUTE_ACTORS);
			db_importer.update_movie_multiple_attributes(yago_parser.get_yag_movie_map(), importer.MOVIE_ATTRIBUTE_GENRES);
			db_importer.update_movie_multiple_attributes(yago_parser.get_yag_movie_map(), importer.MOVIE_ATTRIBUTE_TAGS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
