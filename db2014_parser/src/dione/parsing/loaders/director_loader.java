package dione.parsing.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Connection;

import dione.core.entities.entity_person;
import dione.db.db_queries_persons;
import dione.parsing.Importer;
/**
 * class for loading directors
 * @author GUY
 *
 */
public class director_loader extends abstract_loader {
	HashMap<String,Integer> entity_map;
	HashMap<String,Integer> person_table;
	
	/**
	 * constructor
	 * @param importer
	 * @param parser_map_director - directors to load
	 * @throws SQLException
	 */
	public director_loader(Importer importer, HashMap<String, entity_person> parser_map_director) throws SQLException {
		super(importer,parser_map_director.values());
		this.entity_table_name = "director";
		this.entity_map = new HashMap<String,Integer>(); 	
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		
		this.entity_map = db_queries_persons.get_director_names_and_ids();
		if (entity_map==null)
			entity_map = new HashMap<String,Integer>();
		
		this.person_table =  db_queries_persons.get_person_names_and_ids();
		if (person_table==null)
			person_table = new HashMap<String,Integer>();
				
	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("Insert INTO director(idPerson) VALUES (?)");
	}

	@Override
	/**
	 * creates statements based on obj, if it needs to be inserted 
	 * or updated*/
	protected int create_statments(Object obj) throws SQLException {
		entity_person director = (entity_person)obj; 
		
		Integer general_person_id = person_table.get(director.get_person_name()); 
		/*if person exists, and his id is already an actor/director - move on*/
		if (general_person_id != null && entity_map.get(director.get_person_name()) != null)
			return 0; 
		/*no such person, add him to table*/
		else if (general_person_id != null && entity_map.get(director.get_person_name()) == null)
		{
			insert.setInt(1, general_person_id);
			insert.addBatch();
			return 1;
		}
		return 0;
	}


}
