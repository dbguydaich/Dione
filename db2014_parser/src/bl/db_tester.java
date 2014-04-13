package bl;

import java.sql.SQLException;
import java.util.HashMap;

import db.db_queries;

public class db_tester {

	public static void main(String[] args) throws SQLException 
	{
		HashMap<String, String> movies = db_queries.get_all_movies();
		
		for  (String key : movies.keySet())
		{
			System.out.println("id: " + key + " name: " + movies.get(key));
		}
		
		String name = "Matan";
		if (db_queries.does_user_exists(name))
			System.out.println("the user " + name + " exists");
		else
			System.out.println("the user " + name + " does not exists");
		
		name = "Schachar";
		if (db_queries.does_user_exists(name))
			System.out.println("the user " + name + " exists");
		else
			System.out.println("the user " + name + " does not exists");
	}

}
