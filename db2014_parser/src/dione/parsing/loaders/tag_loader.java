package dione.parsing.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.sql.Connection;


import dione.db.db_queries_movies;
import dione.parsing.Importer;

/**
 * class for loading to the tag table 
 * @author GUY
 *
 */
public class tag_loader extends abstract_loader {

	HashMap<String, Integer> entity_map;

	/**
	 * constructor
	 * @param importer
	 * @param parser_tag_set
	 * @throws SQLException
	 */
	public tag_loader(Importer importer, Set<String> parser_tag_set) throws SQLException {
		super(importer,parser_tag_set);
		this.entity_table_name = "Tag";
		this.entity_map = new HashMap<String, Integer>();
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		this.entity_map = db_queries_movies.get_tag_names_and_ids();
		if (entity_map == null)
			entity_map = new HashMap<String, Integer>();
	}

	@Override
	protected void set_perpared_statments(Connection db_conn)
			throws SQLException {
		insert = db_conn.prepareStatement("INSERT INTO tag(tagName) VALUES(?)");
	}

	@Override
	/**
	 * creates statements based on obj, if it needs to be inserted 
	 * or updated*/
	protected int create_statments(Object obj) throws SQLException {
		if (entity_map.get(((String) obj).toString()) == null) {
			/* create and add */
			insert.setString(1, ((String) obj).toString());
			insert.addBatch();
			return 1;
		}
		return 0;
	}

}
