package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import parser_entities.*;

/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public abstract class db_queries_movies extends db_operations
{		
	
// INSERTERS	
	
	/** insert language
	 * return 	-1	- insert SQL error
	 * 			0	- insert success, id retrieve error
	 * 			id	- else
	 */
	public static Integer create_language(String language_name)
	{
		try {
			insert("language", "`languageName`", language_name);
		} catch (SQLException e) 
		{
			return (-1);
		}
		
		try {
			return (get_language_id(language_name));
		} catch (NumberFormatException | SQLException e) {
			return (0);
		}
	}

	/**
	 * create a new tag
	 * @param tag_name
	 * @return - new tag id
	 */
	public static Integer create_tag(String tag_name)
	{
		try {
			insert("tag", "`tagName`", tag_name);
		} catch (SQLException e) 
		{
			return (-1);
		}
		
		try {
			return (get_tag_id(tag_name));
		} catch (NumberFormatException | SQLException e) {
			return (0);
		}
	}

	/**
	 * user can rang a every movie only once
	 * @return did succeed?
	 * @throws SQLException
	 */
	public static boolean rank_movie(Integer user_id, Integer movie_id, Integer rank) 
			throws SQLException
	{
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		int rows_effected = insert("user_rank", "`idUser`, `idMovie`, `rank`, 'rankDate'" , user_id.toString(), movie_id.toString(), rank.toString(), date);
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	 
	/** create new genre
	 * @return 	-1 for insert faliure, 
	 * 			0 for insert success and user id return failure
				idGenre if successfull
	 */
	public static Integer create_genre(String genre_name)
	{
		if (genre_name == null)
			return (-1);
		
		int rows_effected;
		try 
		{
			// try to insert
			rows_effected = insert("genre", "`genreName`" , genre_name);
			
			// was insert successfull
			if (rows_effected <= 0)
				return (-1);
		} catch (SQLException e) {
			return (-1);
		}
		
		try 
		{
			return (get_genre_id(genre_name));
		} catch (NumberFormatException | SQLException e) {
			return (0);
		}
		
	}
	
// BOOLEANS
	
	public static boolean tag_exists(String tag_name)
		throws SQLException
	{
		if (tag_name == null)
			return (false);
		
		String whereSegment = "tagName = ?";
		ResultSet result = select("tagName", "tag", whereSegment, tag_name);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
	
	/**
	 * is there a movie that has some of these
	 * @param params - all params may be NULL
	 * @return - true only if there is one movie that answers all parameters inserted
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public static boolean movie_exists(String title, String director, List<String> actor_list, 
			List<String> tags_list, List<Boolean> rating_radios_text, List<Boolean> genres) 
					throws NumberFormatException, SQLException 
	{
		// Are there more than 0 movies that fits
		return (get_relevant_movies(title, director, actor_list, tags_list, rating_radios_text, genres).size() != 0);
	}
	
	/**
	 * get movie id by name, year and director
	 * @return - false if either param is null
	 * @throws SQLException
	 */
	public static boolean movie_exist(String movie_name, String movie_year, String movie_director) 
			throws SQLException
			{
		if (movie_name == null || movie_director == null || movie_year == null)
			return (false);
		
		String whereClause = "movieName = ? AND director = ? AND year = ?";
		ResultSet results = select("idMovie", "movie" , whereClause, movie_name, movie_director, movie_year);
		
		// did select find souch user
		if (results.next())
			return (true);
		else
			return (false);		
			}
	
	public static boolean language_exists(String language_name) 
			throws SQLException
	{
		if (language_name == null)
			return (false);
		
		String whereClause = 	"languageName = ?";
		ResultSet results = select("idLanguage", "language" , whereClause, language_name);
		
		// did select find souch user
		if (results.next())
			return (true);
		else
			return (false);
	}
	
	public static boolean genre_exists(String genre_name) 
			throws SQLException
			{
		if (genre_name == null)
			return (false);
		
		String whereSegment = "genreName = ?";
		ResultSet result = select("genreName", "genre", whereSegment, genre_name);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false); 
			}
	
	/**
	 * does the movie fit ALL tags in list
	 * @return false if either tags_list is null od movie_id is 0
	 * @throws SQLException
	 */
	public static boolean is_movie_of_tags(int movie_id, List<String> tags_list) 
			throws SQLException 
	{
		if (tags_list == null || movie_id == 0)
			return (false);
		
		List<String> movie_tags = get_movie_geners(movie_id);
		
		for (String tag : tags_list)
		{
			if (movie_tags.indexOf(tag) == -1)
				return (false);
		}
		
		return (true);
	}
	
// GETTERS

	/**
	 * ID as listed on the DB
	 * @return - returnes HM<MovieId, movieName>
	 * @throws SQLException
	 */
	public static HashMap<String, String> get_all_movies() 
			throws SQLException 
	{
		ResultSet result = select("idmovie, movieName", "movie", "");
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		HashMap<String, String> returnedSet = new HashMap<String, String>();

		while (result.next())
		{
			String name = result.getString("movieName");
			Integer id = result.getInt("idMovie");
			
			returnedSet.put(id.toString(), name);
		}

		return (returnedSet);
	}

	public static List<String> get_geners() 
			throws SQLException 
	{
		ResultSet result = select("distinct(genreName)", "genre", "");
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		List<String> returnedList = new ArrayList<String>();
		
		while (result.next())
		{
			String name = result.getString("genreName");
			
			returnedList.add(name);
		} 
		
		return (returnedList);
	}

	/** get movie IDs and tag count for each movie **/
	public static HashMap <Integer,Integer> get_movie_id_tag_count() 
			throws SQLException
	{
		// Used the FROM segment to inject group-by param
		ResultSet result = select("idMovie, count(idTag)", "movie_tag GROUP BY idMovie", "");
		
		// is table empty
		if (!result.next())
			return (null);
		
		// Enumerate all movies
		HashMap <Integer,Integer> retMap =  new HashMap<Integer,Integer>(); 
		
		do
		{
			Integer id = result.getInt(1);
			Integer count = result.getInt(2);
			
			retMap.put(id,count);
		} while (result.next());
		
		return (retMap);
	}
	
	/** get movie names and IDs **/
	public static HashMap <String,Integer> get_movie_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idMovie, movieName", "movie", ""));
	}

	/**
	 * get all movies that match all asked values
	 * @param  - all parameters can be null
	 * @return - a list of all relevand movie_ids
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public static List<light_entity_movie> get_relevant_movies(String title, String director, List<String> actor_list, 
			List<String> tags_list, List<Boolean> rating_radios_text, List<Boolean> genres) 
					throws NumberFormatException, SQLException 
	{
		String select = "idMovie, movieName, year, director, plot";
		String from	 = "movie, director";
		String where = "director.idPerson = movie.director ";
		List<Object> values = new ArrayList<Object>();
		
		// Title filter
		if (title != null && title != "")
		{
			where += " AND (movie.name like '%?%')";
			values.add(title);
		}
		
		// Director filter
		if (director != null && director != "")
		{
			where += " AND (movie.director like '%?%')";
			values.add(director);
		}
			
		// Actor filter
		if (actor_list != null && actor_list.size() > 0)
		{
			String actorsCond = "idMovie IN (SELECT idMovie FROM movie WHERE 'a' = 'a' ";
			
			// For every actor add a constraint
			for (String actor : actor_list)
			{
				actorsCond += "AND EXISTS (SELECT * FROM actor_movie WHERE " + 
								" actor_movie.idMovie = movie.idMovie AND " +
								" actor_movie.idActor = '?')";
				
				// Add it in order 
				values.add(actor);
			}
			
			// This closes the IN clause
			actorsCond += ")";
			
			where += actorsCond;
		}
		
		// Tag filter
		if (tags_list != null && tags_list.size() > 0)
		{

		}
		
		// Genre filter
		if (tags_list != null && tags_list.size() > 0)
		{

		}
		
		// Make the querey
		ResultSet result = select(select, from, where + "limit 50;", values);
		
		List<light_entity_movie> returnedList = new ArrayList<light_entity_movie>();
		
		// Add all values to the return list
		if (result != null)
		{
			while (result.next())
			{
				int id = result.getInt("idMovie");
				String name = result.getString("movieName");
				String year = result.getString("year");
				String movie_director = result.getString("personName");
				String plot = result.getString("plot");
				
				light_entity_movie movie = new light_entity_movie(id, name, year, movie_director, plot);
				
				returnedList.add(movie);
			}
		}
		
		return (returnedList);
	}
	
	public static List<String> get_movie_geners(int movie_id) 
			throws SQLException 
	{
		String where = "movie_tag.idmovie = ? AND tag.idTag = movie_tag.idTag";
		ResultSet result = select("tagName", "tag, movie_tag", where, movie_id);
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		List<String> returnedList = new ArrayList<String>();
		
		while (result.next())
		{
			String name = result.getString("tagName");
			
			returnedList.add(name);
		} 
		
		return (returnedList);
	}
	
// ID GETTERS
	
	public static int get_tag_id(String tag_name) 
			throws SQLException
	{
		return (generic_id_getter("Tag", tag_name));
	}
		
	public static int get_movie_id(String movie_name, String director) 
			throws NumberFormatException, SQLException 
	{
		if (movie_name == null || director == null)
			return (0);
		
		String whereClause = 	"movieName = ? AND director = ?";
		ResultSet results = select("idMovie", "movie" , whereClause, movie_name, director);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idMovie")));
		else
			return (0);
	}

	public static int get_movie_id(String movie_name) 
			throws SQLException
	{
		return (generic_id_getter("Movie", movie_name));
	}

	public static int get_genre_id(String genre_name) 
			throws SQLException
	{
		return (generic_id_getter("Genre", genre_name));
	}

	public static int get_language_id(String language_name) 
			throws SQLException
	{
		return (generic_id_getter("Language", language_name));
	}
	
// REMOVERS

	/**
	 * delete all values from genre_movie
	 * @return did succeed
	 * @throws SQLException 
	 */
	public static boolean clear_movie_genres() 
			throws SQLException
	{
		return (delete("genre_movie",null) != -1);
	}
	
	/**
	 * remove movie_actors relation
	 * @return did succeedd
	 * @throws SQLException 
	 */
	public static boolean clear_movie_actors() 
			throws SQLException
	{
		return (delete("actor_movie","") != -1);
	}
}

