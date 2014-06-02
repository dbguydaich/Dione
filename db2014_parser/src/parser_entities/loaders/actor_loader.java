package parser_entities.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import parser_entities.entity_person;


import db.db_queries_movies;
import db.db_queries_persons;

public class actor_loader extends abstract_loader {
	private PreparedStatement insert;
	HashMap<String,Integer> entity_map;
	HashMap<String,Integer> person_table;
	
	public actor_loader() throws SQLException {
		super();
		this.entity_table_name = "actor";
		this.entity_map = new HashMap<String,Integer>(); 	
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		
		this.entity_map = db_queries_persons.get_actor_names_and_ids();
		if (entity_map==null)
			entity_map = new HashMap<String,Integer>();
		
		this.person_table =  db_queries_persons.get_person_names_and_ids();
		if (person_table==null)
			person_table = new HashMap<String,Integer>();
				
	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("Insert INTO actor(idPerson) VALUES (?)");
	}

	@Override
	protected int create_statments(Object obj) throws SQLException {
		entity_person actor = (entity_person)obj; 
		
		Integer general_person_id = person_table.get(actor.get_person_name()); 
		/*if person exists, and his id is already an actor/director - move on*/
		if (general_person_id != null && entity_map.get(general_person_id) != null)
			return 0; 
		/*no such person, add him to table*/
		else if (general_person_id != null)
		{
			insert.setInt(1, general_person_id);
			insert.addBatch();
			return 1;
		}
		return 0;
	}

	@Override
	protected int execute_batches() {
		int fail_count=0;
		fail_count += execute_batch(insert);
		return fail_count;
	}



}
