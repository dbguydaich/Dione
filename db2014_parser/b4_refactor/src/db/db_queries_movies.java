package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import config.config;
import parser_entities.*;

/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public abstract class db_queries_movies extends db_operations
{		
	
// INSERTERS	
	
	/** 
	 * insert language
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
		String date = get_curr_time();
		int rows_effected = insert("user_rank", "`idUser`, `idMovie`, `rank`, 'rankDate'" , user_id.toString(), movie_id.toString(), rank.toString(), date);
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	 
	/** 
	 * create new genre
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
	
	/**
	 * @return true iff theres a tag with the name tag_name 
	 * @throws SQLException
	 */
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
	 * @return true iff theres a tag with the name tag_name 
	 * @throws SQLException
	 */
	public static boolean tag_exists(Integer id_tag)
		throws SQLException
	{
		if (id_tag == null)
			return (false);
		
		String whereSegment = "idTag = ?";
		ResultSet result = select("idTag", "tag", whereSegment, id_tag);
		
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
	public static boolean movie_exists(String title, String director, Integer year, List<String> actor_list, 
			List<Integer> tags_list, boolean[] rating_radios_text, boolean[] genres) 
					throws NumberFormatException, SQLException 
	{
		// Are there more than 0 movies that fits
		return (false);//get_relevant_movies(title, director, actor_list, tags_list, rating_radios_text, genres).size() != 0);
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
	
	/**
	 * @return true iff theres a language with the name language_name
	 * @throws SQLException
	 */
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
	
	/**
	 * @return true iff theres a genre with the name genre_name
	 * @throws SQLException
	 */
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

	/**
	 * @return a list of all genre names
	 * @throws SQLException
	 */
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

	/** 
	 * get movie IDs and tag count for each movie 
	 * **/
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
	
	/** 
	 * get movie names and IDs 
	**/
	public static HashMap <String,Integer> get_movie_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idMovie, movie_qualified_name", "movie", ""));
	}

	/**
	 * get all movies that match all asked values
	 * @param  - all parameters can be null
	 * @return - a list of all relevand movie_ids
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public static List<light_entity_movie> get_relevant_movies(String title, String director, String language, Integer min_year, 
			Integer max_year, List<String> actor_list, List<String> tags_list, List<String> genre_list, boolean[] rating) 
					throws SQLException 
	{
		String select = "idMovie, movieName, year, wiki, duration, plot";
		String from	 = "movie";
		String where = "1 = 1 ";
		List<Object> values = new ArrayList<Object>();
		
		// Title filter
		if (title != null && title != "")
		{
			where += " AND (movie.movieName like ?)";
			title = "%" + title + "%";
			values.add(title);
		}
		
		// Director filter
		if (director != null && director != "")
		{
			select += ", personName";
			from += ", person ";
			where += " AND person.idPerson = movie.idDirector  " + 
						" AND (person.personName like ?) ";
			director = "%" + director + "%";
			values.add(director);
		}
		else
		{
			select += ", '' as personName ";
		}
		
		// Language filter
		if (language != null && language != "")
		{
			from += ", language";
			where += " AND language.idLanguage = movie.idLanguage" + 
					" AND (language.languageName like ?)";
			values.add("%" + language + "%");
		}
		
		// year filter
		if (min_year != null)
		{
			where += " AND (movie.year > ?)";
			values.add(min_year);
		}
		
		if (max_year != null)
		{
			where += " AND (movie.year < ?)";
			values.add(max_year);
		}
		
		// Actor filter
		if (actor_list != null && actor_list.size() > 0)
		{
			int actorcount = 0;
			for (String actor : actor_list)
				actorcount += actor.length();
			
			if (actorcount > 0)
			{
				String actorsCond = " AND idMovie IN (SELECT actorMovie.idMovie FROM movie as actorMovie WHERE 'a' = 'a' ";
				
				// For every actor add a constraint
				for (String actor : actor_list)
				{
					if (actor != null)
					{
						actorsCond += " AND EXISTS (SELECT * FROM actor_movie, person as actors WHERE " + 
											" actor_movie.idMovie = actorMovie.idMovie AND " +		
											" actor_movie.idActor = actors.idPerson AND " +
											" actors.personName like ?)";
						
						// Add it in order 
						values.add( "%" + actor + "%");
					}
				}
				
				// This closes the IN clause
				actorsCond += ")";
				
				where += actorsCond;
			}
		}
		
		// Tag filter
		if (tags_list != null && tags_list.size() > 0)
		{
			int tagcount = 0;
			for (String tag : tags_list)
				tagcount += tag.length();
			
			if (tagcount > 0)
			{
				String tagsCond = " AND idMovie IN (SELECT tagMovie.idMovie FROM movie as tagMovie WHERE 'a' = 'a' ";
				
				// For every actor add a constraint
				for (String tag : tags_list)
				{
					if (tag != null)
					{
						tagsCond += "AND EXISTS (SELECT * FROM movie_tag, tag WHERE " + 
										" movie_tag.idTag = tag.idTag AND " +
										" movie_tag.idMovie = tagMovie.idMovie AND " +
										" tag.tagName like ?)";
						
						// Add it in order 
						values.add( "%" + tag + "%");
					}
				}
				
				// This closes the IN clause
				tagsCond += ")";
				
				where += tagsCond;
			}
		}
		
		// Genre filter
		if (genre_list != null && genre_list.size() > 0)
		{
			String genreCond = " AND idMovie IN (SELECT genreMovie.idMovie FROM movie as genreMovie WHERE 'a' = 'a' ";
			
			// For every actor add a constraint
			for (String genre : genre_list)
			{
				genreCond += " AND EXISTS (SELECT * FROM genre_movie, genre WHERE " + 
								" genre.idGenre = genre_movie.idGenre AND " +
								" genre_movie.idMovie = genreMovie.idMovie AND " +
								" genre.genreName like ?)";
				
				// Add it in order 
				values.add( "%" + genre + "%");
			}
			
			// This closes the IN clause
			genreCond += ")";
			where += genreCond;
		}
		
		// Rating filter
		if (rating != null && rating.length != 0)
		{
			// Build having clause
			String having = "(";
			
			for (int i = 0 ; i < rating.length ; i++)
			{
				if (rating[i] == true)
				{
					// Is this not the first true
					if (having.compareTo("(") != 0)
						having += " OR ";
					
					having += "round(avg(rank)) = " + i;
				}
			}
			having += ")";
			
			// Was there a single rating true
			if (having.compareTo("()") != 0)
			{
				where += " AND idMovie IN (SELECT idMovie FROM user_rank" +
						" GROUP BY idMovie" +
						" HAVING " + having + ") ";
			}
					
		}
		
		// Make the querey
		config settings = new config();
		int limit = settings.get_default_large_limit();
		
		ResultSet result = select(select, from, where + " ORDER BY RAND()  limit " + limit , values);
		
		List<light_entity_movie> returnedList = new ArrayList<light_entity_movie>();
		
		// Add all values to the return list
		if (result != null)
		{
			while (result.next())
			{
				returnedList.add(get_light_entity_movie(result));
			}
		}
		
		return (returnedList);
	}

	/**
	 * @return a list of all the genres of the movie movie_id
	 * @throws SQLException
	 */
	public static List<String> get_movie_geners(int movie_id) 
			throws SQLException 
	{
		String where = "genre_movie.idMovie = ? AND genre.idGenre = genre_movie.idGenre";
		ResultSet result = select("genreName", "genre_movie, genre", where, movie_id);
	
		// Enumerate all movies
		List<String> returnedList = new ArrayList<String>();
		
		// is table empty
		if (result != null)
		{		
			while (result.next())
			{
				String name = result.getString(1);
				
				returnedList.add(name);
			}
		}
		
		return (returnedList);
	}
	
	/**
	 * @return a list of all of the tags of the movie movie_id
	 * @throws SQLException
	 */
	public static List<String> get_movie_tags(int movie_id) throws SQLException 
	{
		String where = "movie_tag.idMovie = ? AND tag.idTag = movie_tag.idTag";
		ResultSet result = select("tagName", "movie_tag, tag", where, movie_id);
	
		// Enumerate all movies
		List<String> returnedList = new ArrayList<String>();
		
		// is table empty
		if (result != null)
		{		
			while (result.next())
			{
				String name = result.getString(1);
				
				returnedList.add(name);
			}
		}
		
		return (returnedList);
	}
	
	/**
	 * uses a random of (-3) - 3 so the top wont be the same all the time
	 * @return
	 * @throws SQLException
	 */
	public static List<String> get_movie_top_tags(int movie_id, int limit) 
			throws SQLException 
	{
		String where = " user_tag_movie.idTag = tag.idTag AND " +
						" tag.idmovie = ? " +
						" GROUP BY idTag " +
						" ORDER BY relatedness "+
						" LIMIT " + limit;
		ResultSet result = select("tagName, (SUM(rate) + (FLOOR(RAND() * (7)) - 3)) as relatedness", "tag, user_tag_movie", where, movie_id);
	
		// Enumerate all movies
		List<String> returnedList = new ArrayList<String>();
		
		// is table empty
		if (result != null)
		{		
			while (result.next())
			{
				String name = result.getString(1);
				
				returnedList.add(name);
			}
		}
		
		return (returnedList);

	}
	
	/**
	 * 	Get movie entity by id
	 * @return If user id does not exists, return an empty movie
	 * @throws SQLException
	 */
	public static light_entity_movie get_movie_details(int movie_id) 
			throws SQLException 
	{
		String where = "movie.idMovie = ? AND movie.idDirector = person.idPerson";
		ResultSet result = select("idMovie, movieName, year, wiki, personName, duration, plot", "movie, person", where, movie_id);
		
		// is table empty
		if (result != null && result.next())
		{		
			return (get_light_entity_movie(result));
		}
		
		// If there is user that feets
		return (new light_entity_movie(0, "", 0, "", "", 0, ""));
	}
	
	/**
	 * @return the language of the movie movie_id or "None" if it is not listed
	 * @throws SQLException
	 */
	public static String get_movie_language(int movie_id) 
			throws SQLException 
	{
		String where = "movie.idMovie = ? AND movie.idLanguage = .language.idLanguage";
		ResultSet result = select("languageName", "language, movie", where, movie_id);
		
		// is table empty
		if (result != null && result.next())
		{		
			return (result.getString(1));
		}
		
		return ("None");
	}
	
	/**
	 * @return the avarage of rates of the movie movie_id
	 * @throws SQLException
	 */
	public static int get_movie_rating(int movie_id) 
			throws SQLException 
	{
		// old jdbc driver does not know double and forces usage of CAST()
		String where = "idMovie = ? ";
		ResultSet result = select("CAST(round(avg(rank)) AS char(15)) as rank_avg", "user_rank", where, movie_id);
		
		// is table empty
		if (result != null && result.next())
		{		
			String temp = result.getString(1);
			
			if (temp != null)
				return (int) (Integer.parseInt(temp));
			else
				return (0);
		}
		
		return (-1);
	}
		

	/** 
	 * @return list of movies ordered by user rates with a limit
	 * @throws SQLException
	 */
	public static List<light_entity_movie> get_top_rated_movies(int limit) 
			throws SQLException 
	{
		String select = "movie.idMovie as idMovie, movieName, year, wiki, personName, duration, plot, AVG(rank) rate";
		String from	 = "movie, person, user_rank";
		String where = " person.idPerson = movie.idDirector AND user_rank.idMovie = movie.idMovie " + 
						" GROUP BY idMovie ORDER BY rate LIMIT " + limit;
		
		// Make the querey
		ResultSet result = select(select, from, where);
		
		List<light_entity_movie> returnedList = new ArrayList<light_entity_movie>();
		
		// Add all values to the return list
		if (result != null)
		{
			while (result.next())
			{
				returnedList.add(get_light_entity_movie(result));
			}
		}
		
		return (returnedList);
	}
	
	
// ID GETTERS
	public static HashMap <String,Integer> get_genre_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idGenre, genreName", "genre", ""));
	}
	
	public static HashMap <String,Integer> get_language_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idLanguage, languageName", "language", ""));
	}
	
	public static HashMap <String,Integer> get_tag_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idTag, tagName", "tag", ""));
	}
	
	public static int get_tag_id(String tag_name) 
			throws SQLException
	{
		return (generic_id_getter("Tag", tag_name));
	}
		
	public static int get_movie_id(String movie_name, String director) 
			throws SQLException 
	{
		if (movie_name == null || director == null)
			return (0);
		
		String whereClause = 	"movieName = ? AND director = ?";
		ResultSet results = select("idMovie", "movie" , whereClause, movie_name, director);
		
		// did select find souch user
		if (results.next())
			return (results.getInt("idMovie"));
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
		return (delete("genre_movie","") != -1);
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

