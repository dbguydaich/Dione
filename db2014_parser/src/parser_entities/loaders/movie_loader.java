package parser_entities.loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import parser_entities.entity_movie;


import db.db_queries_movies;
import db.db_queries_persons;

public class movie_loader extends abstract_loader {
	private PreparedStatement insert;
	private PreparedStatement update;
	HashMap<String,Integer> entity_map;
	
	HashMap<String,Integer> languages_table;
	HashMap<String,Integer> directors_table;
	
	
	public movie_loader() throws SQLException {
		super();
		this.entity_table_name = "movie";
		this.entity_map = new HashMap<String,Integer>();
		
	}

	@Override
	/** check existence against this **/
	protected void sync_update_tables() throws SQLException {
		
		this.entity_map = db_queries_movies.get_movie_names_and_ids();
		if (entity_map ==null)
			entity_map = new HashMap<String,Integer>();
		
		
		this.languages_table =  db_queries_movies.get_language_names_and_ids();
		if (languages_table==null)
			languages_table = new HashMap<String,Integer>();
		
		this.directors_table = db_queries_persons.get_director_names_and_ids();
		if (directors_table==null)
			directors_table = new HashMap<String,Integer>();
	}


	@Override
	protected void set_perpared_statments(Connection db_conn) throws SQLException {
		update = db_conn.prepareStatement("UPDATE Movie SET idLanguage=?,idDirector=?,movieName=?,year=?,wiki=?,duration=?,movie_qualified_name=?,plot=? WHERE idMovie=?");
		insert = db_conn.prepareStatement("INSERT INTO Movie(idLanguage,idDirector,movieName,year,wiki,duration,movie_qualified_name,plot) VALUES(?,?,?,?,?,?,?,?)");
	}

	@Override
	protected int create_statments(Object obj) throws SQLException {
		
		entity_movie movie = (entity_movie)obj;
		
		String movie_fq_name = movie.get_movie_qualified_name();
		Integer movie_id = null; 
		
		if ((movie_id = entity_map.get(movie_fq_name)) != null)
		{	
			update = set_values_movie_update(update, movie, movie_id);
			update.addBatch();
			return 1;	
		}
		/*movie doesn't exists, insert new movie*/
		else
		{				
			insert = set_values_movie_insert(insert, movie);
			insert.addBatch(); 
			return 1;
		}
	}
	
	
	/** set values in a prepared movie insert query
	 * 	return null on failure or if movie id not found **/
	public PreparedStatement set_values_movie_insert(PreparedStatement movie_insert_stmt, entity_movie movie)
	{
		/*auto incremental, no need to specify id!*/
		try {
			/*set statment parameters*/
			if (languages_table.get(movie.get_movie_language()) != null)
				movie_insert_stmt.setInt(1, languages_table.get(movie.get_movie_language()));
			else
				movie_insert_stmt.setNull(1, 2);
			
			if (movie.get_movie_director() != null)
				System.out.println(movie.get_movie_director());
				
			if (movie.get_movie_director() != null && directors_table.get(movie.get_movie_director().get_person_name()) != null)
				movie_insert_stmt.setInt(2, directors_table.get(movie.get_movie_director().get_person_name()));
			else
				movie_insert_stmt.setNull(2, 2);
			
			if (movie.get_movie_name() != null)
				movie_insert_stmt.setString(3, movie.get_movie_name());
			else
				movie_insert_stmt.setNull(3, 2);
			
			if (movie.get_movie_year() != null)
				movie_insert_stmt.setString(4, movie.get_movie_year());
			else
				movie_insert_stmt.setNull(4, 2);
			
			if (movie.get_movie_wikipedia_url() != null)
				movie_insert_stmt.setString(5, movie.get_movie_wikipedia_url());
			else 
				movie_insert_stmt.setNull(5, 2);
			
			if (movie.get_movie_plot() != null)
				movie_insert_stmt.setString(6, movie.get_movie_length());
			else
				movie_insert_stmt.setNull(6, 2);
			
			if (movie.get_movie_qualified_name() != null)
				movie_insert_stmt.setString(7, movie.get_movie_qualified_name());
			else
				movie_insert_stmt.setNull(7, 2);
			
			if (movie.get_movie_plot() != null)
				movie_insert_stmt.setString(8, movie.get_movie_plot());
			else
				movie_insert_stmt.setNull(8, 2);
			
			return movie_insert_stmt;
		}
		catch (Exception ex)
		{
			System.out.println("Error preparing statement update statment" +  ex.getStackTrace());
			return null;
		}
	}
	

	/** set values in a prepared movie insert query
	 * 	return null on failure or if movie id not found **/
	public PreparedStatement set_values_movie_update(PreparedStatement movie_update_stmt, entity_movie movie, Integer movie_id)
	{
		try {
			/* same as insert, just need to append and id for the Where clause*/
			set_values_movie_insert(movie_update_stmt,movie);
			if (movie_id != null)
				movie_update_stmt.setInt(9, movie_id);
			else /*cannot update without id*/
				return null;
			return movie_update_stmt;
		}
		catch (Exception ex)
		{
			System.out.println("Error preparing statement update statment" +  ex.getStackTrace());
			return null;
		}
	}
	
	@Override
	protected int execute_batches(int batch_size) {
		int fail_count=0;
		fail_count += execute_batch(insert, batch_size);
		fail_count += execute_batch(update, batch_size);
		return fail_count;
	}



}
