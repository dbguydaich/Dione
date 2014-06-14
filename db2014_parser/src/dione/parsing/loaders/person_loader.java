package dione.parsing.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Connection;



import dione.core.entities.entity_person;
import dione.db.db_queries_persons;
import dione.parsing.Importer;

/** class for loading to the persons table 
 * 
 * @author GUY
 *
 */
public class person_loader extends abstract_loader {

	HashMap<String,Integer> entity_map;
	
	public person_loader(Importer importer, HashMap<String, entity_person> parser_map_person) throws SQLException {
		super(importer,parser_map_person.values());
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
	/**
	 * creates statements based on obj, if it needs to be inserted 
	 * or updated*/
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
