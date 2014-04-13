package db;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.sql.Connection;


/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public class db_queries extends db_operations{

	static jdbc_connection_pooling connPull;
	
	// TODO: mabye delete..
	private static final int IdMovie = 1;
	private static final int IdLanguage = 2;
	private static final int IdDirector = 3;
	private static final int MovieName = 4;
	private static final int Year = 5;
	private static final int Wiki = 6;
	private static final int Duration = 7;
	private static final int Plot = 8;
	
	// constructor //
	public db_queries(jdbc_connection_pooling connParam) {
		super(connParam);
	}

	// add the director to the db- to the Movie and Director table
	public HashMap<String, String> get_all_movies() 
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

	public boolean does_user_exists(String user_name)
	{
		String whereSegment = "userName = '" + user_name + "'";
		ResultSet result = select("userName", "users", whereSegment);
		
		// try querey
		try 
		{
			// did select find souch user
			if (result.next())
				return (true);
			else
				return (false);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Error: " + e.getMessage());
			return (false);
		}
	}
}

