package parser_entities.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import parser_entities.Importer;
import parser_entities.entity_person;


import db.db_queries_movies;
import db.db_queries_persons;

public class person_loader extends abstract_loader {

	HashMap<String,Integer> entity_map;
	
	public person_loader(Importer importer) throws SQLException {
		super(importer);
		this.entity_table_name = "person";
		this.entity_map = new HashMap<String,Integer>(); 	
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		this.entity_map =  db_queries_persons.get_person_names_and_ids();
		if (entity_map==null)
			entity_map = new HashMap<String,Integer>();		
	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("Insert INTO person(personName) VALUES (?)");
	}

	@Override
	protected int create_statments(Object obj) throws SQLException {
		entity_person person = (entity_person)obj; 
		
		if (entity_map.get(person.get_person_name()) != null)
			return 0; 
		/*no such person, add him to table*/
		else
		{
			insert.setString(1, person.get_person_name());
			insert.addBatch();
			return 1;
		}
		
	}
}
