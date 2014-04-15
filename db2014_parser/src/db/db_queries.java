package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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

	public static boolean add_user(String user, String pass)
	{
		int rows_effected = insert("users", "`userName`, `userPassword`, `hashPassword`" , "'" +user + "'" , Integer.toString(pass.hashCode()));
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}

	public static boolean rank_movie(Integer user_id, Integer movie_id, Integer rank)
	{
		int rows_effected = insert("user_ranks", "`user_id`, `movie_id`, `rank`" , user_id.toString(), movie_id.toString(), rank.toString());
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}

	// return - if not exists retutn 0, else return 
	public static int get_movie_id_by_name(String movie_name) 
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
	public static int get_person_id_by_name(String person_name) 
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
}

