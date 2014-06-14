package dione.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dione.config.config;
import dione.core.entities.*;
import dione.db.*;
import dione.db.db_operations.invocation_code;
import dione.parsing.*;

/**
 * this is the module to get user related data
 * every "user_loics" holds one current user, but has data on all users
 *
 * @NOTE 	-	before you use any method that uses current_user_id you are expected to login_user!
 * @author Matan Poleg
 *
 */
public class user_logics 
{
	private int current_user_id = 0;

// BASICS
	
	/**
	 * login as current user
	 * @return 	0 		- if credentials are wrong
	 * 			user id - if true
	 * @throws SQLException 
	 */
	public int login_user(String user_name, String pass) 
			throws SQLException
	{
		if (!authenticate_user(user_name, pass))
		{
			return (0);
		}
		else
		{
			current_user_id = db_queries_user.get_user_id(user_name);
			
			return (current_user_id);
		}
	}
					
	/**
	 * make the current user id 0 - there can not be such a user
	 */
	public void logout()
	{
		current_user_id = 0;
	}
	
	/**
	 * add user to "users" table
	 * @return true iff succedded adding the user
	 * @throws SQLException
	 */
	public static boolean add_user(String user, String pass)
			throws SQLException
	{
		if (!db_queries_user.user_exists(user))
			return (db_queries_user.add_user(user, pass));
		else
			return (false);
	}
	
	/**
	 * @return true iff there is a user with those user name and password
	 * @throws SQLException
	 */
	public static boolean authenticate_user(String user, String pass) 
			throws SQLException
	{
		return (db_queries_user.authenticate_user(user, pass));
	}
	
	/**
	 * @return true iff there's a user with such the name user_name (regardless of password)
	 * @throws SQLException
	 */
	public static boolean does_user_exists(String user_name) 
			throws SQLException
	{
		return (db_queries_user.user_exists(user_name));
	}

	/**
	 * make a friendship between user2_id and user1_id
	 * @return
	 * 		1  - if this friendship was already found
	 * 		-1 - if friendship was unable to be created
	 * 		0  - on success
	 * @throws SQLException
	 */
	public static int add_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		if (db_queries_user.are_friends(user1_id, user2_id))
			return (1);
		else
			if (db_queries_user.add_friendship(user1_id, user2_id))
				return(0);
			else
				return(-1);
	}

	/**
	 * cancle the friendship between user2_id and user1_id
	 * @return did succeed
	 * @throws SQLException
	 */
	public static boolean remove_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		return (db_queries_user.remove_friendship(user1_id, user2_id));
	}

	/**
	 * cancle the friendship between user2_id and user1_id
	 * @return did succeed
	 * @throws SQLException
	 */
	public static boolean remove_friendship(Integer current_user, String name_of_friend) 
			throws SQLException
	{
		// Get friend id
		int friend_id = db_queries_user.get_user_id(name_of_friend);
		
		// Remove friendship, if exists
		return (remove_friendship(current_user, friend_id));
	}
	
	/**
	 * get current user's id
	 * @return the user id as saved in "current_user_id"
	 */
	public int get_current_user_id() 
	{
		return (current_user_id);
	}

	/**
	 * @return - the name of current_user
	 * @throws SQLException
	 */
	public String get_my_name() 
			throws SQLException
	{
		return (get_user_name(current_user_id));
	}
	
	/**
	 * @return - the userName of user_id
	 * @throws SQLException
	 */
	private String get_user_name(int user_id) 
			throws SQLException 
	{
		return (db_queries_user.get_name_of_user(user_id));
	}

	/**
	 * @return get the id of the user valled - user
	 * @throws SQLException
	 */
	public static Integer get_user_id(String user) 
			throws SQLException
	{
		return (db_queries_user.get_user_id(user));
	}

	/**
	 * change the userName of a user
	 * @param new_name the new name of the user with the userId - id
	 * @param pass - to make sure this is actualy the user
	 * @return did succeed
	 * @throws SQLException
	 */
	public static boolean update_name(String new_name, int id, String pass) 
			throws SQLException
	{
		return (db_queries_user.update_name(new_name, id, pass));
	}

	/**
	 * change the password of a user
	 * @param new_pass - the new password of the user with the userId - id
	 * @param old_pass - to make sure this is actualy the user
	 * @return did succeed
	 * @throws SQLException
	 */
	public static boolean update_pass(String new_pass, int id, String old_pass) 
			throws SQLException
	{
		return (db_queries_user.update_pass(new_pass, id, old_pass));
	}
	
//ACTIVITIES
	
	/**
	 * get a list a of at most limit of user's friends recent activities
	 * @return activity list
	 * @throws SQLException 
	 */
	public static List<abstract_activity> get_friends_recent_activities(int user_id, int limit) 
			throws SQLException
	{
		// Get a list of friend's IDs
		List<Integer> friends = get_user_friends_ids(user_id);
		
		// Init a list of activities
		List<abstract_activity> activities = new  ArrayList<abstract_activity>();
		
		// Iterate all of user's friends
		for (Integer friend : friends)
		{
			// Add the current friend's activities
			activities.addAll(get_user_recent_social_activities(friend, limit));
			
			// Sort and trim the list
			Collections.sort(activities);
			
			// Trim and exit if needed
			if (activities.size() > limit)
			{
				activities = activities.subList(0, limit);
				break;
			}
		}
		
		// Return the list
		return (activities);
	}
	
	/**
	 * get_friends_recent_activities
	 * @return
	 * @throws SQLException 
	 */
	public List<abstract_activity> get_friends_recent_activities() 
			throws SQLException
	{
		config settings = new config();
		int limit = settings.get_default_small_limit();
				
		return (get_friends_recent_activities(current_user_id, limit));
	}
	
	/**
	 * get string representation of friends recent activities
	 * @return
	 * @throws SQLException 
	 */
	public List<String> get_friends_recent_string_activities() throws SQLException
	{
		return (list_activity_to_list_string(get_friends_recent_activities()));
	}
	
	/**
	 * get the string representation of social activities
	 * @param limit - max length of the List
	 * @return - pretty much List<abstract_activities>.toString()
	 * @throws SQLException 
	 */
	public static List<String> get_user_recent_string_activities(int user, int limit) 
			throws SQLException
	{
		return (list_activity_to_list_string(get_user_recent_social_activities(user, limit)));
	}
	
	/** get the recent activity of the current user
	 * @return		- list of at most 'limit' activities
	 * 					NULL there is not even ine activity(!!!)
	 * @throws SQLException 
	 */
	public  abstract_activity get_user_recent_social_activity() 
			throws SQLException
	{
		// Get a list of a single activity
		List<abstract_activity> act_list = get_user_recent_social_activities(current_user_id, 1);
		
		// Return the first and only activity
		if (act_list.size() > 0)
			return (act_list.get(0));
		else
			return (null);
	}
	
	/** get the recent activities of the current user and the default limit
	 * @return		- list of at most 'limit' activities
	 * @throws SQLException 
	 */
	public List<abstract_activity> get_user_recent_social_activities() 
			throws SQLException
	{
		config settings = new config();
		int limit = settings.get_default_small_limit();
		
		return (get_user_recent_social_activities(current_user_id, limit));
	}
		
	/**
	 * get the recent activities
	 * @param limit	- NULL will get the default limit
	 * @return		- list of the activities that happend the latest
	 * @throws SQLException 
	 */
	public static List<abstract_activity> get_user_recent_social_activities(int user, int limit) 
			throws SQLException
	{
		// Init the returned list
		List<abstract_activity> returnedList = new ArrayList<abstract_activity>();
		
		// Get the lists of all recent activities in the returned list
		returnedList.addAll(db_queries_user.get_user_recent_rank_activities(user, limit));
		returnedList.addAll(db_queries_user.get_user_recent_friendship_activities(user, limit));
		returnedList.addAll(db_queries_user.get_user_recent_tag_activities(user, limit));
		returnedList.addAll(db_queries_user.get_user_recent_note_activities(user, limit));
		
		// Sorting by date, using compateTo()
		Collections.sort(returnedList);
		
		// Trim the list to fit the limit (if needed)
		if (returnedList.size() > limit)
		{
			returnedList = returnedList.subList(0, limit);
		}
		
		return (returnedList);
	}
	
	/**
	 * get the recent activities
	 * @param limit NULL will get the default limit
	 * @throws SQLException
	 */
	public List<String> get_user_recent_friendship_activities(Integer limit) 
			throws SQLException
	{
		if (limit == null)
		{
			config settings = new config();
			limit = settings.get_default_small_limit();
		}
		
		return(list_activity_to_list_string(db_queries_user.get_user_recent_friendship_activities(current_user_id, limit)));
	}
	
	/** get all notes that the user wrote
	 * @param limit NULL will get the default limit
	 * @throws SQLException
	 */
	public static List<String> get_movie_notes(int movie_id, Integer limit) 
			throws SQLException
	{
		if (limit == null)
			limit = new config().get_default_small_limit();
		
		return(db_queries_user.get_movie_notes(movie_id, limit));
	}
	 
	/** create a new note by user_id to novie_id with the note text at current time
	 * @param limit NULL will get the default limit
	 * @throws SQLException
	 */
	public boolean add_movies_notes(int movie_id, String text) 
			throws SQLException
	{
		String time = db_operations.get_curr_time();
		
		return(db_queries_user.addNote(current_user_id, movie_id, text, time));
	}
	
	
// MISC
	
	/**
	 * @return the unit of get_user_recommended_movies() lists of the user_id's friends
	 * @param limit - NULL will get the default limit
	 * @throws SQLException
	 */
	public static List<light_entity_movie> get_user_recommended_movies_entities_by_friends(int user_id, int limit) 
			throws SQLException 
	{
		List<Integer> friends = get_user_friends_ids(user_id);
		List<light_entity_movie> movies = new ArrayList<light_entity_movie>();
		
		for (Integer friend : friends)
		{
			List<light_entity_movie> friends_movies = get_user_recommended_movies(friend);
			
			movies.addAll(friends_movies);
			
			// Do we have enough recomended
			if (movies.size() > limit)
			{
				movies = movies.subList(0, limit);
				break;
			}
		}
		
		return (movies);
	}
	
	/**
	 * get the user_id's random friends most recomended movies
	 * 		this according to the tags he like from the user_preference
	 * @return
	 * @throws SQLException 
	 */
	public static List<String> get_user_recommended_movies_by_friends(int user_id, int limit) 
			throws SQLException 
	{
		return (list_activity_to_list_string(get_user_recommended_movies_entities_by_friends(user_id, limit)));
	}
	
	/**
	 * get the current user's most recomended movies
	 * 		this according to the tags he like from the user_preference
	 * @return
	 * @throws SQLException 
	 */
	public static List<String> get_user_recommended_movie_names(int user_id) 
			throws SQLException
	{
		List<light_entity_movie> movies = db_queries_user.get_movies_prefered_by_user(user_id, 50);
		List<String> names = new ArrayList<String>();
		
		for(light_entity_movie movie : movies)
		{
			names.add(movie.get_movie_name());
		}	
		
		return (names);
	}
	
	/**
	 * get the current user's most recomended movies
	 * 		this according to the tags he like from the user_preference
	 * @return
	 * @throws SQLException 
	 */
	public static List<light_entity_movie> get_user_recommended_movies(int user_id) 
			throws SQLException
	{
		return (db_queries_user.get_movies_prefered_by_user(user_id, 50));
	}
	
	/** get list of strings represanting tags
	 * @param limit - max tags you want to see 
	 * @return		- a list of the tags, ordered by popularity
	 * @throws SQLException 
	 */
	public static List<String> get_popular_tags(int user_id, int limit) 
			throws SQLException
	{
		return (db_queries_user.get_prefered_tags(user_id, limit));
	}
	
	/**
	 * defaults are current user id, and 5
	 * @throws SQLException
	 */
	public List<String> get_user_popular_tags() 
			throws SQLException
	{
		int limit = new config().get_default_small_limit();
		
		return (db_queries_user.get_prefered_tags(current_user_id, limit));
	}
		
	/**
	 * @return a list of users that have friendship with current_user
	 * @throws SQLException
	 */
	public List<entity_user> get_current_user_friends() 
			throws SQLException
	{
		return (db_queries_user.get_user_friends(current_user_id));
	}
	
	/**
	 * @return a list of names of users that have friendship with current_user
	 * @throws SQLException
	 */
	public List<String> get_current_user_friends_names() 
			throws SQLException
	{
		List<entity_user> users = get_current_user_friends();
		List<String> names = new ArrayList<String>();
		
		// Convert entities to Names 
		for(entity_user user: users)
		{
			names.add(user.get_user_name());
		}
		
		return names;
	}
		
	/**
	 * get a list of IDs of a user's friends
	 * @param user_id
	 * @throws SQLException 
	 */
	public static List<Integer> get_user_friends_ids(int user_id) 
			throws SQLException
	{
		List<entity_user> users = db_queries_user.get_user_friends(user_id);
		List<Integer> ids = new ArrayList<Integer>();
		
		// Convert entities to Names 
		for(entity_user user: users)
		{
			ids.add(user.get_user_id());
		}
		
		return ids;
	}
		
	/**
	 * @param limit - can be NULL
	 * @return a list of at most limit random movies taht was not rated by user_id
	 * @throws SQLException
	 */
	public static List<light_entity_movie> get_unrated_movies(int user_id, Integer limit) 
			throws SQLException
	{
		// load default limit if needed
		if (limit == null)
			limit = new config().get_default_small_limit();
		
		return (db_queries_user.get_unrated_movies(user_id, limit));
	}

	/**
	 * @return a list of random movies that current_user has not rated ye
	 * @param limit - null will get the default limit
	 * @throws SQLException
	 */
	public List<light_entity_movie> get_unrated_movies(Integer limit) 
			throws SQLException
	{
		return (get_unrated_movies(current_user_id, limit));
	}
	
	/**
	 * @return - a random movie that current user has not rated yet 
	 * @throws SQLException
	 */
	public light_entity_movie get_unrated_movie()
			throws SQLException
	{
		List<light_entity_movie> movies = get_unrated_movies(1); 
		
		if (movies != null && movies.size() > 0)
			return  (movies.get(0));
		else
			return (null);
	}
	
	/**
	 * Fills the table user_prefence with the useres preference of each tag
	 * @return true on success
	 * @throws SQLException
	 */
	public boolean fill_user_prefence() 
			throws SQLException
	{
		if (db_operations.was_there_an_invocation(invocation_code.YAGO_UPDATE))
			return (db_queries_user.fill_user_prefence(current_user_id));
		else
			return (true);
	}
	
// INTERNALS
	
	/** Rate a movie tag
	 * @param 0-5, 0 dont know 1 lowest
	 * @return did succeed?
	 * @throws SQLException 
	 */
	public static boolean rate_movie_tag(int movie_id,int tag_id, int user_id,int rate) 
			throws SQLException
	{
		// is there a tag like this
		if (!db_queries_movies.tag_exists(tag_id))
			return (false);
		
		if (!db_queries_user.rated_tag(movie_id, user_id, tag_id))
			return (db_queries_user.rate_tag(movie_id, user_id, tag_id ,rate));
		else
			return (db_queries_user.update_rate_tag(movie_id, user_id, tag_id ,rate));
	}
	
	/**
	 * Rate a movie
	 * @param rate (-5) - 5
	 * @return did succeed?
	 * @throws SQLException 
	 */
	public static boolean rate_movie(int movie_id, int user_id,int rate) 
			throws SQLException
	{
		return (db_queries_user.rate_movie(movie_id, user_id, rate));
	}
		
	/** Rate a movie
	 * @return did succeed?
	 * @throws SQLException 
	 */
	public boolean rate_movie(int movie_id,int rate) 
			throws SQLException
	{
		if (!db_queries_user.did_user_rate_movie(movie_id, current_user_id))
			return (db_queries_user.rate_movie(movie_id, current_user_id, rate));
		else
			return (db_queries_user.update_rate_movie(movie_id, current_user_id, rate));
	}
	
	/**
	 * did user rate any movies yet
	 * @return true iff user have already rated movies
	 * @throws SQLException 
	 */
	public boolean user_rated() 
			throws SQLException
	{
		return (user_rated_count() > 0);
	}
	
	/**
	 * how many movies did the current user rate
	 * @throws SQLException 
	 */
	public int user_rated_count() 
			throws SQLException
	{
		return (user_rated_count(current_user_id));
	}

	/**
	 * how many movies did a user rate
	 * @throws SQLException 
	 */
	public static int user_rated_count(int user_id) 
			throws SQLException
	{
		return (db_queries_user.user_rated_count(user_id));
	}
	
	/**
	 * add a row to user_tag_movie
	 * in which user_id will rate the tag called tag_name for movie_id with rate at current time
	 * @param rate - expected 0-5, 0 is "Dont know"
	 * @return did succeed
	 * @throws SQLException
	 */
	public static boolean rate_tag_movie(int movie_id, int user_id, String tag_name, int rate) 
			throws SQLException
	{
		int tag_id = db_queries_movies.get_tag_id(tag_name);
		
		return (rate_movie_tag(movie_id, tag_id, user_id, rate));
	}
	
	/**
	 * Iterate over all activities in activity_list and toString() them to a String list
	 * @param activity_list
	 */
	@SuppressWarnings("rawtypes")
	private static List<String> list_activity_to_list_string(List activity_list)
	{
		if (activity_list == null || activity_list.size() == 0)
			return (new ArrayList<String>());
		
		// Init the return list
		List<String> retList = new ArrayList<String>();
		
		// Go over all activities and add their string representation
		for(Object act : activity_list)
		{
			retList.add(act.toString());
		}
		
		return (retList);
	}

}
