package bl;

import java.sql.SQLException;
import java.util.HashMap;

import db.db_queries;
import db.jdbc_connection_pooling;

public class db_tester {

	public static void main(String[] args) throws SQLException 
	{
		jdbc_connection_pooling conn = new jdbc_connection_pooling();
		
		db_queries specialOps = new db_queries(conn);
		
		HashMap<String, String> movies = specialOps.get_all_movies();
		
		for  (String key : movies.keySet())
		{
			System.out.println("id: " + key + " name: " + movies.get(key));
		}
		
		String name = "Matan";
		if (specialOps.does_user_exists(name))
			System.out.println("the user " + name + " exists");
		else
			System.out.println("the user " + name + " does not exists");
		
		name = "Schachar";
		if (specialOps.does_user_exists(name))
			System.out.println("the user " + name + " exists");
		else
			System.out.println("the user " + name + " does not exists");
	}

}
