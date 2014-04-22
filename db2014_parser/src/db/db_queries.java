package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public abstract class db_queries extends db_operations
{		
	// returnes HM<MovieId, movieName>  -  ID as listed on the DB
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

	// returnes HM<personId, personName>  -  ID as listed on the DB
	public static HashMap<String, String> get_all_persons() 
			throws SQLException 
	{
		ResultSet result = select("idPerson, personName", "person", "");
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		HashMap<String, String> returnedSet = new HashMap<String, String>();
		
		while (result.next())
		{
			String name = result.getString("personName");
			Integer id = result.getInt("idPerson");
			
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

	/** internaly used in the folloing functions
	 * @param table		- table name
	 * @param values	- "IntegerVal, StringVal", exactly these values!
	 * @return a HashMap of wanted values from the table
	 * @throws SQLException 
	 */
	private static HashMap <String,Integer> generic_get_two_values(String values, String table, String where) 
			throws SQLException
	{
		ResultSet result = select(values, table, where);
		
		// is table empty
		if (!result.next())
			return (null);
		
		// Enumerate all movies
		HashMap <String,Integer> retMap =  new HashMap<String,Integer>(); 
		
		do
		{
			Integer id = result.getInt(1);
			String name = result.getString(2);
			
			retMap.put(name,id);
		} while (result.next());
		
		return (retMap);
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
	
	/** get actor names and IDs
	 * NOTE! 	theIDs are idActor, not idPerson
	 * @throws SQLException 
	 */
	public static HashMap <String,Integer> get_actor_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idActor, personName", "actor, person", "person.idPerson = actor.idPerson"));
	}
	
	/** get director names and IDs
	 * NOTE! 	the IDs are idDirector, not idPerson
	 * @throws SQLException 
	 */
	public static HashMap <String,Integer> get_director_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idDirector, personName", "person, director", "person.idPerson = director.idPerson"));
	}
	
// Inserters	
	/** insert language
	 * return 	-1	- insert SQL error
	 * 			0	- insert success, id retrieve error
	 * 			id	- else
	 */
	public static Integer create_language(String language_name)
	{
		try {
			insert_columns("language", "'languageName'", language_name);
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
	
	public static Integer create_tag(String tag_name)
	{
		try {
			insert_columns("tag", "'tagName'", tag_name);
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
	
	public static boolean rank_movie(Integer user_id, Integer movie_id, Integer rank) 
			throws SQLException
	{
		String date = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		int rows_effected = insert("user_rank", "`idUser`, `idMovie`, `rank`, 'rankDate'" , user_id.toString(), movie_id.toString(), rank.toString(), date);
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	
	public static boolean add_user(String user, String pass) 
			throws SQLException
	{
		int rows_effected = insert_columns("users", "`userName`, `hashPassword`" , "'" +user + "'" , Integer.toString(pass.hashCode()));
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	
	public static boolean add_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException 
	{
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		int rows_effected = insert_columns("friend_relation", "`friend1`, `friend2`, `friendshipDate`" , "'" + user1_id + "'" , "'" + user2_id + "'" , "'" + date + "'");
		
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
			rows_effected = insert_columns("genre", "`genreName`" , "'" + genre_name + "'");
			
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
	
// Booleans
	public static boolean tag_exists(String tag_name)
		throws SQLException
	{
		if (tag_name == null)
			return (false);
		
		String whereSegment = "tagName = '" + tag_name + "'";
		ResultSet result = select("tagName", "tag", whereSegment);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
	
	public static boolean authenticate_user(String user, String pass) 
			throws SQLException
			{
		if (user == null || pass == null)
			return (false);
		
		String whereSegment = "userName = '" + user + "' AND hashPassword = '" + Integer.toString(pass.hashCode()) + "'";
		ResultSet result = select("userName", "users", whereSegment);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
			}
	
	public static boolean user_exists(String user_name) 
			throws SQLException
	{
		if (user_name == null)
			return (false);
		
		String whereSegment = "userName = '" + user_name + "'";
		ResultSet result = select("userName", "users", whereSegment);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
	
	public static boolean movie_exists(String title, String director, List<String> actor_list, 
			List<String> tags_list, List<Boolean> rating_radios_text, List<Boolean> genres) 
					throws NumberFormatException, SQLException 
	{
		int movie_id = get_movie_id(title, director);
		
		// Does movie title exists
		if (movie_id == 0)
			return (false);
		
		// did all actors play in this title
		for (String actor_name : actor_list)
		{
			if (!did_actor_play_in_movie(movie_id, get_actor_id(actor_name)))
				return (false);
		}
		
		// do all tags fit
		if (!is_movie_of_tags(movie_id, tags_list))
			return (false);
		
		// is movie of rating
		
		// TODO:do all genres fit
		
		// if got here everything fits and movie exists
		return (true);
	}
	
	// return false if either param is null
	public static boolean movie_exist(String movie_name, String movie_year, String movie_director) 
			throws SQLException
			{
		if (movie_name == null || movie_director == null || movie_year == null)
			return (false);
		
		String whereClause = 	"movieName = '" + movie_name + "' AND " +
				"director = '" + movie_director + "' AND " +
				"year = '" + movie_year + "'";
		ResultSet results = select("idMovie", "movie" , whereClause);
		
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
		
		String whereClause = 	"languageName = '" + language_name + "'";
		ResultSet results = select("idLanguage", "language" , whereClause);
		
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
		
		String whereSegment = "genreName = '" + genre_name + "'";
		ResultSet result = select("genreName", "genre", whereSegment);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false); 
			}
	
	// return false if either tags_list is null od movie_id is 0
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

	public static boolean did_actor_play_in_movie(int movie_id, int actor_id) 
			throws SQLException
	{
		String whereClause = "idMovie = '" + movie_id + "' AND idActor = '" + actor_id + "'";
		ResultSet result = select("idMovie", "actor_movie" , whereClause);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
	
// GETTERS

	// returns a list of all relevand movie_ids
	public static List<Integer> get_relevant_movies(String title, String director, List<String> actor_list, 
			List<String> tags_list, List<Boolean> rating_radios_text, List<Boolean> genres) 
					throws NumberFormatException, SQLException 
	{
		//TODO
		return (null);
	}
	
	public static List<String> get_movie_geners(int movie_id) 
			throws SQLException 
	{
		String where = "movie_tag.idmovie = '" + movie_id + "' AND tag.idTag = movie_tag.idTag";
		ResultSet result = select("tagName", "tag, movie_tag", where);
		
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
	
	public static List<Integer> get_prefered_tags(int user_id) 
			throws SQLException
	{
		// where string includes "order by" field to get the prefered tags
		String where = 	"userId = '" + user_id + "' " +
					 	"order by tag_user_rate desc limit 5";
		ResultSet result = select("idTag", "user_prefence", where);
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		List<Integer> returnedList = new ArrayList<Integer>();
		while (result.next())
		{
			int name = result.getInt("idTag");
			
			returnedList.add(name);
		} 
		
		return (returnedList);
	}
	
// ID GETTERS
	private static int generic_id_getter(String value, String value_name) 
			throws SQLException
	{
		if (value == null)
			return (-1);
		
		String whereClause = value.toLowerCase() + "Name = '" + value_name + "'";
		ResultSet results = select("id"+ value, value.toLowerCase() , whereClause);
		
		try{
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("id" + value)));
		else
			return (0);
		} catch (NumberFormatException e)
		{
			return (-1);
		}
	}
	
	public static int get_tag_id(String tag_name) 
			throws SQLException
	{
		return (generic_id_getter("Tag", tag_name));
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
	
	private static int get_movie_id(String movie_name, String director) 
			throws NumberFormatException, SQLException 
	{
		if (movie_name == null || director == null)
			return (0);
		
		String whereClause = 	"movieName = '" + movie_name + "' AND " +
								"director = '" + director + "'";
		ResultSet results = select("idMovie", "movie" , whereClause);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idMovie")));
		else
			return (0);
	}

	// return - if not exists retutn 0, else return id 
	public static int get_person_id(String person_name) 
			throws NumberFormatException, SQLException
	{
		if (person_name == null)
			return (0);
		
		String whereClause = "personName = '" + person_name + "'";
		ResultSet results = select("idMovie", "movie" , whereClause);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idPerson")));
		else
			return (0);
	}

	// return - if not exists retutn 0, else return  id
	public static int get_actor_id(String actor_name) 
			throws NumberFormatException, SQLException
	{
		if (actor_name == null)
			return (0);
		
		String whereClause = 	"person.idPerson = actor.idPerson AND " +
								"personName = '" + actor_name + "'";
		ResultSet results = select("idActor", "person, actor" , whereClause);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idActor")));
		else
			return (0);
	}
	
	
// REMOVERS
	
	// remove friendship
	public static boolean remove_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		if (user1_id == null || user2_id == null)
			return (false);
		
		// friendship is irelevand of who is 1 and who is 2..
		String whereCol = 	"(friend1 = '" + user1_id + "' AND friend2 ='" + user2_id + "') OR " +
							"(friend2 = '" + user1_id + "' AND friend1 ='" + user2_id + "')";
		
		return (delete("friend_relation", whereCol) > 0);
	}

	// delete all values from genre_movie
	public static boolean clear_movie_genres()
	{
		return (delete("genre_movie","") != -1);
	}
	
	// remove movie_actors relation
	public static boolean clear_movie_actors()
	{
		return (delete("actor_movie","") != -1);
	}
}

