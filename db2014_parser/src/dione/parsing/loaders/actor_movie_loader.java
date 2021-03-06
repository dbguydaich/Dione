package dione.parsing.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;


import dione.core.entities.entity_movie;
import dione.core.entities.entity_person;
import dione.db.db_queries_movies;
import dione.db.db_queries_persons;
import dione.parsing.Importer;

/**
 * class for loading to actor-movie table
 * @author GUY
 *
 */
public class actor_movie_loader extends abstract_loader {
	HashMap<String,Integer> entity_map;
	HashMap<String,Integer> actors_table;
	
	/**
	 * constructor
	 * @param importer
	 * @param parser_map_movie
	 * @throws SQLException
	 */
	public actor_movie_loader(Importer importer, HashMap<String, entity_movie> parser_map_movie) throws SQLException {
		super(importer,parser_map_movie.values());
		this.entity_table_name = "actor-movie";
		this.entity_map = new HashMap<String,Integer>(); 	
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		this.entity_map = db_queries_movies.get_movie_names_and_ids();
		if (entity_map ==null)
			entity_map = new HashMap<String,Integer>();
		
		
		this.actors_table =  db_queries_persons.get_actor_names_and_ids();
		if (actors_table==null)
			actors_table = new HashMap<String,Integer>();
		
		db_queries_persons.clear_movie_actors();
	}


	@Override
	/**
	 * creates statements based on obj, if it needs to be inserted 
	 * or updated*/
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("INSERT INTO actor_movie(idActor, idMovie) VALUES(?,?)");
	}

	@Override
	protected int create_statments(Object obj) throws SQLException {
		int c_actor_movie = 0;
		Set<String> attribute_set = new HashSet<String>();
		
		entity_movie movie = (entity_movie)obj;
		if (movie.get_movie_actors() == null || movie.get_movie_actors().size() ==0 )
			return 0;
		for (entity_person actor : movie.get_movie_actors())
			attribute_set.add(actor.get_person_name());
		
		Integer movie_id = entity_map.get(movie.get_movie_qualified_name());
		
		if (movie_id == null)
			return 0;
		if (attribute_set == null || attribute_set.size() == 0)
			return 0;
		for (String attribute : attribute_set)
		{
			Integer actor_id = actors_table.get(attribute);
			if (actor_id == null)
				return 0;
			insert.setInt(1,actor_id);
			insert.setInt(2,movie_id);
			insert.addBatch();
			c_actor_movie++;
		}
		return c_actor_movie;
	}



}
