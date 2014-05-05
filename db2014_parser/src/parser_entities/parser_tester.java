package parser_entities;
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
		
		praser_src_imdb imdb_parser = new praser_src_imdb(yago_parser.get_yag_movie_map());
		imdb_parser.parse();
		
		importer db_importer = new importer();
		/* after parsing, start loading data to db */
		/*peripherial data*/
		db_importer.update_genres(imdb_parser.get_parser_genres());
		db_importer.update_languages(imdb_parser.get_parser_languages());
		db_importer.update_genres(imdb_parser.get_parser_languages());
		db_importer.update_tags(imdb_parser.get_parser_languages());
		
		try
		{
			/*entities*/
			db_importer.update_actors_table(yago_parser.get_yag_actor_map());
			db_importer.update_directors_table(yago_parser.get_yag_director_map());
			db_importer.update_movies_table(yago_parser.get_yag_movie_map());
			/*movie many to many*/
			db_importer.update_movies_genres_table(yago_parser.get_yag_movie_map());
			db_importer.update_movies_tags_table(yago_parser.get_yag_movie_map());
			db_importer.update_movie_actors_tabls(yago_parser.get_yag_movie_map());
			
			
			
			
		}
		catch (Exception ex)
		{
			
		}
		
	}
	
	

}
