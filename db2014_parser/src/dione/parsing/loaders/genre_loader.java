package dione.parsing.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.sql.Connection;

import dione.db.db_queries_movies;
import dione.parsing.Importer;

/**
 * class for loading genres
 * @author GUY
 *
 */
public class genre_loader extends abstract_loader {
	HashMap<String,Integer> entity_map;
	
	/**
	 * constructor
	 * @param importer
	 * @param parser_genre_set - genres to load
	 * @throws SQLException
	 */
	public genre_loader(Importer importer, Set<String> parser_genre_set) throws SQLException {
		super(importer,parser_genre_set);
		this.entity_table_name = "Genre";
		this.entity_map = new HashMap<String,Integer>(); 	
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		this.entity_map =  db_queries_movies.get_genre_names_and_ids();
		if (entity_map==null)
			entity_map = new HashMap<String,Integer>();		
	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("INSERT INTO genre(genreName) VALUES(?)");
	}

	@Override
	/**
	 * creates statements based on obj, if it needs to be inserted 
	 * or updated*/
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
