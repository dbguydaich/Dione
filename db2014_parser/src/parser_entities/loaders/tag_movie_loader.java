package parser_entities.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import parser_entities.entity_movie;


import db.db_queries_movies;
import db.db_queries_persons;

public class tag_movie_loader extends abstract_loader {
	
	public static int DEFAULT_TAG_SCORE = 5;
	
	private PreparedStatement insert;
	HashMap<String,Integer> entity_map;
	HashMap<String,Integer> tag_table;
	Set<String> attribute_set;
	
	public tag_movie_loader() throws SQLException {
		super();
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
		insert = db_conn.prepareStatement("INSERT INTO movie_tag(idMovie, idTag, scoreTag) VALUES(?,?,?)");
	}

	@Override
	protected int create_statments(Object obj) throws SQLException {
		
		entity_movie movie = (entity_movie)obj;
		attribute_set = movie.get_movie_tags(); 
		Integer movie_id = entity_map.get(movie.get_movie_qualified_name());
		
		if (movie_id == null)
			return 0;
		if (attribute_set == null)
			return 0;
		for (String attribute : attribute_set)
		{
			Integer tag_id = tag_table.get(attribute);
			if (tag_id == null)
				return 0;
			insert.setInt(1,movie_id);
			insert.setInt(2,tag_id);
			insert.setInt(3,DEFAULT_TAG_SCORE);
		}
		return 1;
	}

	@Override
	protected int execute_batches() {
		int fail_count=0;
		fail_count += execute_batch(insert);
		return fail_count;
	}


}
