package bl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import db.db_queries;

public class movie_logics 
{
	List<String> get_genres() 
			throws SQLException
	{
		// Get the list from the DB
		List<String> ret = db_queries.get_geners();
		
		// Sort the returned values
		Collections.sort(ret);
		
		return (ret);
	}

	boolean does_movie_exists(String title,String director,List<String>actor_list,List<String> tags_list, List<Boolean>	rating_radios_text,List<Boolean>genres_numbers) 
			throws NumberFormatException, SQLException
	{
		return (db_queries.does_movie_exists(title, director, actor_list, tags_list, rating_radios_text, genres_numbers));
	}
}
