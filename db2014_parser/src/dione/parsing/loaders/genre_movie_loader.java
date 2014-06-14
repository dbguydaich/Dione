package dione.parsing.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;

import dione.core.entities.entity_movie;
import dione.db.db_queries_movies;
import dione.parsing.Importer;



/**
 * class for loading genre-movies
 * @author GUY
 *
 */
public class genre_movie_loader extends abstract_loader {
	HashMap<String,Integer> entity_map;
	HashMap<String,Integer> genres_table;

	
	/**
	 * constructor
	 * @param importer
	 * @param parser_map_movie
	 * @throws SQLException
	 */
	public genre_movie_loader(Importer importer, HashMap<String, entity_movie> parser_map_movie) throws SQLException {
		super(importer, parser_map_movie.values());
		this.entity_table_name = "genre-movie";
		this.entity_map = new HashMap<String,Integer>(); 	
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		this.entity_map = db_queries_movies.get_movie_names_and_ids();
		if (entity_map ==null)
			entity_map = new HashMap<String,Integer>();
		
		this.genres_table =  db_queries_movies.get_genre_names_and_ids();
		if (genres_table==null)
			genres_table = new HashMap<String,Integer>();
		
		db_queries_movies.clear_movie_genres();
	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		insert = db_conn.prepareStatement("INSERT INTO genre_movie(idMovie, idGenre) VALUES(?,?)");
	}

	@Override
	/**
	 * creates statements based on obj, if it needs to be inserted 
	 * or updated*/
	protected int create_statments(Object obj) throws SQLException {
		Set<String> attribute_set = new HashSet<String>();
		int c_tag_movie = 0; 
		
		entity_movie movie = (entity_movie)obj;
		attribute_set = movie.get_movie_genres(); 
		Integer movie_id = entity_map.get(movie.get_movie_qualified_name());
		
		if (movie_id == null)
			return 0;
		if (attribute_set == null)
			return 0;
		for (String attribute : attribute_set)
		{
			Integer genre_id = genres_table.get(attribute);
			if (genre_id == null)
				return 0;
			insert.setInt(1,movie_id);
			insert.setInt(2,genre_id);
			insert.addBatch();
			c_tag_movie++;
		}
		return c_tag_movie;
	}

}
