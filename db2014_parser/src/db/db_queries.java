package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	{
		ResultSet result = select("idmovie, movieName", "movie", "");
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		HashMap<String, String> returnedSet = new HashMap<String, String>();
		try 
		{
			while (result.next())
			{
				String name = result.getString("movieName");
				Integer id = result.getInt("idMovie");
				
				returnedSet.put(id.toString(), name);
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (returnedSet);
	}

	// returnes HM<personId, personName>  -  ID as listed on the DB
	public static HashMap<String, String> get_all_persons() 
	{
		ResultSet result = select("idperson, personName", "person", "");
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		HashMap<String, String> returnedSet = new HashMap<String, String>();
		try 
		{
			while (result.next())
			{
				String name = result.getString("personName");
				Integer id = result.getInt("idPerson");
				
				returnedSet.put(id.toString(), name);
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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

// Inserters	
	
	public static boolean rank_movie(Integer user_id, Integer movie_id, Integer rank)
	{
		int rows_effected = insert("user_ranks", "`user_id`, `movie_id`, `rank`" , user_id.toString(), movie_id.toString(), rank.toString());
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	
	public static boolean add_user(String user, String pass)
	{
		int rows_effected = insert("users", "`userName`, `userPassword`, `hashPassword`" , "'" +user + "'" , Integer.toString(pass.hashCode()));
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	
// Booleans
	
	public static boolean does_user_exists(String user_name) 
			throws SQLException
	{
		String whereSegment = "userName = '" + user_name + "'";
		ResultSet result = select("userName", "users", whereSegment);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}

	public static boolean authenticate_user(String user, String pass) 
			throws SQLException
	{
		String whereSegment = "userName = '" + user + "' AND userPassword = '" + Integer.toString(pass.hashCode()) + "'";
		ResultSet result = select("userName", "users", whereSegment);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
	
	public static boolean does_movie_exists(String title, String director, List<String> actor_list, 
			List<String> tags_list, List<Boolean> rating_radios_text, List<Boolean> genres_numbers) 
					throws NumberFormatException, SQLException 
	{
		int movie_id = get_movie_id(title, director);
		
		// Does movie title exists
		if (movie_id == 0)
			return (false);
		
		// did all actors play in this title
		for (String actor_name : actor_list)
		{
			if (did_actor_play_in_movie(movie_id, get_actor_id(actor_name)))
				return (false);
		}
		
		// TODO:do all tags fit
		
		// TODO:is rating true
		
		// TODO:do all genres fit
		
		// if got here everything fits and movie exists
		return (true);
	}

	public static boolean did_actor_play_in_movie(int movie_id, int actor_id) 
			throws SQLException
	{
		String whereClause = "idMovie = '" + movie_id + "' AND idActor = '" + actor_id + "'";
		ResultSet result = select("idMovie", "actormovie" , whereClause);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
	
// ID GETTERS
	
	private static int get_movie_id(String movie_name, String director) 
			throws NumberFormatException, SQLException 
	{
		String whereClause = 	"movieName = '" + movie_name + "' AND " +
								"director = '" + director + "'";
		ResultSet results = select("idMovie", "movie" , whereClause);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idMovie")));
		else
			return (0);
	}

	// return - if not exists retutn 0, else return 
	public static int get_movie_id(String movie_name) 
			throws NumberFormatException, SQLException
	{
		String whereClause = "movieName = '" + movie_name + "'";
		ResultSet results = select("idMovie", "movie" , whereClause);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idMovie")));
		else
			return (0);
	}

	// return - if not exists retutn 0, else return 
	public static int get_person_id(String person_name) 
			throws NumberFormatException, SQLException
	{
		String whereClause = "personName = '" + person_name + "'";
		ResultSet results = select("idMovie", "movie" , whereClause);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idPerson")));
		else
			return (0);
	}

	// return - if not exists retutn 0, else return 
	public static int get_actor_id(String actor_name) 
			throws NumberFormatException, SQLException
	{
		String whereClause = 	"person.idPerson = actor.idPerson AND " +
								"personName = '" + actor_name + "'";
		ResultSet results = select("idActor", "person, actor" , whereClause);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idActor")));
		else
			return (0);
	}

	/*parser functions*/
	
	public static boolean genre_exists(String genre_name)
	{
		return false; 
	}
	
	
	public static Integer get_genre_id(String genre_name)
	{
		return new Integer(0); 
	}
	
	public static Integer create_genre(String genre_name)
	{
		return new Integer(0); 
	}
	
	public static boolean language_exists(String language_name)
	{
		return false; 
	}
	
	public static Integer get_language_id(String language_name)
	{
		return new Integer(0); 
	}
	
	public static Integer create_language(String language_name)
	{
		return new Integer(0); 
	}
	
	public static boolean tag_exists(String tag_name)
	{
		return false; 
	}

	public static Integer get_tag_id(String tag_name)
	{
		return new Integer(0); 
	}
	
	public static Integer create_tag(String tag_name)
	{
		return new Integer(0); 
	}

	public static HashMap <String,Integer> get_movie_names_and_ids()
	{
		return new HashMap <String,Integer>();
	}
	
	public static HashMap <String,Integer> get_actor_names_and_ids()
	{
		return new HashMap <String,Integer>();
	}
	
	public static HashMap <String,Integer> get_director_names_and_ids()
	{
		return new HashMap <String,Integer>();
	}
	
	public static HashMap <Integer,Integer> get_movie_id_tag_count()
	{
		return new HashMap <Integer,Integer>();
	}
	
	/*remove movie_actors relation*/
	public static void clear_movie_actors()
	{
		
	}
	
	/*removie movie_genre relation*/
	public static void clear_movie_genres()
	{
	
	}
	
	public static boolean does_movie_exist(String movie_name, String movie_year, String movie_director)
	{
		return false;
		
	}
	
}

