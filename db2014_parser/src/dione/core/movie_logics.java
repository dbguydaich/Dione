package dione.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dione.config.config;
import dione.core.entities.light_entity_movie;
import dione.db.*;
import dione.parsing.*;

public abstract class movie_logics 
{
	
// DATA GETTERS	
	
	/**
	 * @return a list of all genres
	 * @throws SQLException
	 */
	public static List<String> get_genres() 
			throws SQLException
	{
		// Get the list from the DB
		List<String> ret = db_queries_movies.get_geners();
		
		// Sort the returned values
		Collections.sort(ret);
		
		return (ret);
	}

	/**
	 * @return true if theres even a single movie that fits the description
	 * @param actor_list	- all actors must appear in the movie
	 * @param tags_list		- all tags must apear in the movie
	 * @param genre_list	- all genres must apear in the movie
	 * @param rating	- if the movie is of one of these tags 
	 * @throws SQLException
	 */
	public static boolean does_movie_exists(String title, String director, String language, Integer min_year, Integer max_year,
			List<String> actor_list, List<String> tags_list, List<String> genre_list, boolean[] rating) 
			throws SQLException
	{
		return (get_relevant_movies(title, director,language, min_year, max_year, actor_list, tags_list, genre_list, rating).size() > 0);
	}
	
	/**
	 * @return a list of the names of movies that fit the description
	 * @param actor_list	- all actors must appear in the movie
	 * @param tags_list		- all tags must apear in the movie
	 * @param genre_list	- all genres must apear in the movie
	 * @param rating	- if the movie is of one of these tags 
	 * @throws SQLException
	 */
	public static List<String> get_relevant_movies_names(String title, String director, String language, Integer min_year, Integer max_year,
			List<String> actor_list, List<String> tags_list, List<String> genre_list, boolean[] rating)
			throws SQLException
	{
		List<String> result = new ArrayList<String>();
		List<light_entity_movie> movies = get_relevant_movies( title, director ,language, min_year, max_year , actor_list, tags_list, genre_list, rating);
		
		for (light_entity_movie movie: movies)
		{
			result.add(movie.get_movie_name());
		}
		
		return (result);
	}

	/**
	 * This is a key function.
	 * @return a list of movies that fit the description
	 * @param actor_list	- all actors must appear in the movie
	 * @param tags_list		- all tags must apear in the movie
	 * @param genre_list	- all genres must apear in the movie
	 * @param rating	- if the movie is of one of these tags 
	 * @throws SQLException
	 */
	public static List<light_entity_movie> get_relevant_movies(String title, String director, String language, Integer min_year, Integer max_year,
			List<String> actor_list, List<String> tags_list, List<String> genre_list, boolean[] rating) 
			throws SQLException
	{
		return (db_queries_movies.get_relevant_movies(title, director, language, min_year, max_year, actor_list, tags_list, genre_list, rating));
	}
	
	/**
	 * @return true iff the movie movie_id is taged with all the tags from the list
	 * @throws SQLException
	 */
	public static boolean is_movie_of_tags(int movie_id, List<String> tags_list) 
			throws SQLException 
	{
		return(db_queries_movies.is_movie_of_tags(movie_id, tags_list));
	}

	/**
	 * @return a list of the movies that are most related to this movie
	 * @throws SQLException
	 */
	public static List<String> get_movie_top_tags(int movie_id, int limit) 
			throws SQLException
	{
		return (db_queries_movies.get_movie_top_tags(movie_id, limit));				
	}

	/** ordered by user rates
	 * @return
	 * @throws SQLException
	 */
	public static List<light_entity_movie> get_top_rated_movies(Integer limit) 
			throws SQLException 
	{
		if (limit == null)
		{
			config settings = new config();
			limit = settings.get_default_small_limit();
		}
			
		return (db_queries_movies.get_top_rated_movies(limit));
	}
	
	/**
	 * @return - Get the full data of a movie with the id moive_id 
	 * @throws SQLException
	 */
	public static light_entity_movie get_movie_details(int movie_id) 
			throws SQLException
	{
		return (db_queries_movies.get_movie_details(movie_id));
	}
	
	/**
	 * @return - a list of all genres related to this movie
	 * @throws SQLException
	 */
	public static List<String> get_movie_genres(int movie_id) 
			throws SQLException
	{
		return (db_queries_movies.get_movie_geners(movie_id));
	}
	
// ID Getters
	
	public static int get_tag_id(String tag_name) 
			throws SQLException
	{
		return (db_queries_movies.get_tag_id(tag_name));
	}
		
	public static int get_movie_id(String movie_name, String director) 
			throws NumberFormatException, SQLException 
	{
		return (db_queries_movies.get_movie_id(movie_name, director));
	}

	public static int get_movie_id(String movie_name) 
			throws SQLException
	{
		return (db_queries_movies.get_movie_id(movie_name));
	}

	public static int get_genre_id(String genre_name) 
			throws SQLException
	{
		
		return (db_queries_movies.get_genre_id(genre_name));
	}

	public static int get_language_id(String language_name) 
			throws SQLException
	{
		return (db_queries_movies.get_language_id(language_name));
	}

// DELETERS
	
	/**
	 * delete all values from genre_movie
	 * @return did succeed
	 * @throws SQLException 
	 */
	public static boolean clear_movie_genres() 
			throws SQLException
	{
		return (db_queries_movies.clear_movie_genres());
	}
	
	/**
	 * remove movie_actors relation
	 * @return did succeedd
	 * @throws SQLException 
	 */
	public static boolean clear_movie_actors() 
			throws SQLException
	{
		return (db_queries_movies.clear_movie_actors());
	}

}
