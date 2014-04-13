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
public abstract class db_queries extends db_operations
{		
	// add the director to the db- to the Movie and Director table
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

	public static boolean does_user_exists(String user_name)
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

	public static boolean authenticate_user(String user, String pass)
	{
		String whereSegment = "userName = '" + user + "' AND userPassword = '" + pass + "'";
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

	public static boolean add_user(String user, String pass)
	{
		int rows_effected = insert("users", "`userName`, `userPassword`, `hashPassword`" , "'" +user + "'" , "'" + pass +  "'", Integer.toString(pass.hashCode()));
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
}

