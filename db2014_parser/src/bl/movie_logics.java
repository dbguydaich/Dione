package bl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import parser_entities.entity_movie;
import db.db_queries_movies;

public class movie_logics 
{
	public List<String> get_genres() 
			throws SQLException
	{
		// Get the list from the DB
		List<String> ret = db_queries_movies.get_geners();
		
		// Sort the returned values
		Collections.sort(ret);
		
		return (ret);
	}

	public boolean does_movie_exists(String title,String director,List<String>actor_list,List<String> tags_list, List<Boolean>	rating_radios_text,List<Boolean>genres_numbers) 
			throws NumberFormatException, SQLException
	{
		return (db_queries_movies.movie_exists(title, director, actor_list, tags_list, rating_radios_text, genres_numbers));
	}

	public List<Integer> get_relevant_movies(String title,String director,List<String>actor_list,List<String> tags_list, List<Boolean>	rating_radios_text,List<Boolean>genres_numbers) 
			throws NumberFormatException, SQLException
	{
		return (db_queries_movies.get_relevant_movies(title, director, actor_list, tags_list, rating_radios_text, genres_numbers));
	}

	public entity_movie get_movie_entity_by_id(Integer id)
	{
		//TODO:
		return (null);
	}

	public static boolean is_movie_of_tags(int movie_id, List<String> tags_list) 
			throws SQLException 
	{
		return(db_queries_movies.is_movie_of_tags(movie_id, tags_list));
	}

}
