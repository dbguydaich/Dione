package parser_entities.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import parser_entities.Importer;


import db.db_queries_movies;

public class language_loader extends abstract_loader {

	HashMap<String,Integer> entity_map;
	
	public language_loader(Importer importer) throws SQLException {
		super(importer);
		this.entity_table_name = "Language";
		this.entity_map = new HashMap<String,Integer>(); 	
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		this.entity_map =  db_queries_movies.get_language_names_and_ids();
		if (entity_map==null)
			entity_map = new HashMap<String,Integer>();		
	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("INSERT INTO language(languageName) VALUES(?)");
	}

	@Override
	protected int create_statments(Object obj) throws SQLException {
		if (entity_map.get(((String)obj).toString()) == null)
		{
			/*create and add*/
			insert.setString(1, ((String)obj).toString());
			insert.addBatch();
			return 1;
		}
		return 0;
	}

}
