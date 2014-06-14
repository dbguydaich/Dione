package parser_entities;
import java.sql.SQLException;


import java.util.HashMap;
import java.util.HashSet;

import parser_entities.parsers.abstract_yago_parser;
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
		
	}
	
	

}
