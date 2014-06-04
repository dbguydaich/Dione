package parser_entities;
import java.sql.SQLException;


import java.util.HashMap;
import java.util.HashSet;

import parser_entities.imdb_parsers.imdb_parser_all;
import parser_entities.loaders.loader;
import parser_entities.parsers.abstract_yago_parser;
import parser_entities.parsers.parser_yago_all;
import db.db_queries_persons;




import config.config; 

/* A tester Class For Parsing Utilities*/
public class parser_tester {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Importer imp = new Importer();
		imp.run();
		
		// TODO Auto-generated method stub
		//parser_yago_all yago = new parser_yago_all();
		//abstract_yago_parser yago_data = yago.parse_all();
		//imdb_parser_all imdb_data = new imdb_parser_all(yago_data.get_yag_movie_map());
		//imdb_data.parse_all();

		/*peripherial data*/
		//try {
		//loader ldr = new loader();
			//ldr.load(imdb_data, yago_data);
		//} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		
	}
	
	

}
