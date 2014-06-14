package dione.parsing.loaders;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;




import dione.core.entities.entity_movie;
import dione.db.db_queries_movies;
import dione.parsing.Importer;

/**
 * class for loadinf to the tag_movie table
 * @author GUY
 *
 */
public class tag_movie_loader extends abstract_loader {
	
	public static int DEFAULT_TAG_SCORE = 3;

	HashMap<String,Integer> entity_map;
	HashMap<String,Integer> tag_table;
	
	private PreparedStatement insert_rating = null;

	/**
	 * constructor
	 * @param importer
	 * @param parser_map_movie
	 * @throws SQLException
	 */
	public tag_movie_loader(Importer importer, HashMap<String, entity_movie> parser_map_movie) throws SQLException {
		super(importer,parser_map_movie.values());
		this.entity_table_name = "tag-movie";
		this.entity_map = new HashMap<String,Integer>(); 
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		this.entity_map = db_queries_movies.get_movie_names_and_ids();
		if (entity_map ==null)
			entity_map = new HashMap<String,Integer>();
		
		this.tag_table =  db_queries_movies.get_tag_names_and_ids();
		if (tag_table==null)
			tag_table = new HashMap<String,Integer>();

	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("INSERT INTO movie_tag(idMovie, idTag) VALUES(?,?)");
		insert_rating =  db_conn.prepareStatement("INSERT INTO user_tag_movie (idUser,idTag,idMovie,rate,reteDate) VALUES(?,?,?,?,?);");
	}

	@Override
	/**
	 * creates statements based on obj, if it needs to be inserted 
	 * or updated*/
	protected int create_statments(Object obj) throws SQLException {
		
		Set<String> attribute_set = new HashSet<String>();
		entity_movie movie = (entity_movie)obj;
		attribute_set = movie.get_movie_tags(); 
		int c_tags = 0;
		Integer movie_id = entity_map.get(movie.get_movie_qualified_name());
		
		if (movie_id == null)
			return 0;
		if (attribute_set == null)
			return 0;
		
		for (String attribute : attribute_set)
		{
			Integer tag_id = tag_table.get(attribute);
			if (tag_id == null)
				continue;
			
			insert.setInt(1,movie_id);
			insert.setInt(2,tag_id);
			insert.addBatch();
			
			insert_rating.setInt(1,this.properties.get_admin_userid());
			insert_rating.setInt(2,tag_id);
			insert_rating.setInt(3,movie_id);
			insert_rating.setInt(4,DEFAULT_TAG_SCORE);
			insert_rating.setTimestamp(5,(new Timestamp(new Date().getTime())));
			insert_rating.addBatch();
			
			c_tags++;
			
		}
		return c_tags;
	}
	
	@Override
	/** overrides, since there is more than an insert*/
	protected int execute_batches(int batch_size) throws SQLException {
		int fail_count=0;
		fail_count += execute_batch(insert, batch_size/2);
		fail_count += execute_batch(insert_rating, batch_size/2);
		return fail_count;
	}	
	
	@Override
	/**
	 * closes the special statements 
	 */
	protected void close_stmt(){
		try{ 
			this.insert.close();
			this.insert_rating.close();
		} catch (SQLException se) {
			System.out.println("ERROR closing statemnt: " + se.getMessage());
		}
	}
		

}
