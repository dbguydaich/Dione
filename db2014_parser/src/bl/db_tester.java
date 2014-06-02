package bl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.*;
import parser_entities.light_entity_movie;

public class db_tester 
{
	public static void main(String[] args)
	{
		try {
			
			db_operations.fill_user_prefence();
			
			List<String> gen = new ArrayList<String>();
			gen.add("act");
			
			
			List<light_entity_movie> movies = db_queries_movies.get_relevant_movies(null, null, null, null, null, null, null);
			
			for (light_entity_movie movie : movies)
			{
				System.out.println(movie.toString());
			}
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
