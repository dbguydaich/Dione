package bl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import config.config;
import parser_entities.abstract_activity;
import db.db_queries_user;

/**
 * this is the module to get user related data
 * every "user_loics" holds one current user, but has data on all users
 * @author Matan Poleg
 *
 */
public class user_logics 
{
	private int current_user_id;

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
	 * add user to "users" table
	 * @return true iff succedded adding the user
	 * @throws SQLException
	 */
	public boolean add_user(String user, String pass)
			throws SQLException
	{
		return (db_queries_user.add_user(user, pass));
	}
	
	public boolean authenticate_user(String user, String pass) 
			throws SQLException
	{
		return (db_queries_user.authenticate_user(user, pass));
	}
	
	public boolean does_user_exists(String user_name) 
			throws SQLException
	{
		return (db_queries_user.user_exists(user_name));
	}

	public boolean add_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		return (db_queries_user.add_friendship(user1_id, user2_id));
	}

	public boolean remove_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		return (db_queries_user.remove_friendship(user1_id, user2_id));
	}

	public boolean remove_friendship(Integer current_user, String name_of_friend) 
			throws SQLException
	{
		// Get friend id
		int friend_id = db_queries_user.get_user_id(name_of_friend);
		
		// Remove friendship, if exists
		return (remove_friendship(current_user, friend_id));
	}
	
//SOCIACLS
	
	/** get the recent activity of the current user
	 * @return		- list of at most 'limit' activities
	 * @throws SQLException 
	 */
	public abstract_activity get_user_recent_social_activity() 
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
		int limit = settings.get_default_activity_limit();
		
		return (get_user_recent_social_activities(current_user_id, limit));
	}
		
	/**
	 * get the recent activities
	 * @param limit	- maximum number of social activities
	 * @return		- list of at mos 'limit' activities
	 * @throws SQLException 
	 */
	public List<abstract_activity> get_user_recent_social_activities(int user, int limit) 
			throws SQLException
	{
		// Init the returned list
		List<abstract_activity> returnedList = new ArrayList<abstract_activity>();
		
		// Get the lists of all recent activities in the returned list
		returnedList.addAll(db_queries_user.get_user_recent_rank_activities(user, limit));
		returnedList.addAll(db_queries_user.get_user_recent_friendship_activities(user, limit));
		returnedList.addAll(db_queries_user.get_user_recent_tag_activities(user, limit));
		
		// Sorting by date, using compateTo()
		Collections.sort(returnedList);
		
		// Trim the list to fit the limit
		returnedList = returnedList.subList(0, limit);
		
		return (returnedList);
	}
	
	/** get list of strings represanting tags
	 * @param limit - max tags you want to see 
	 * @return		- a list of the tags, ordered by popularity
	 * @throws SQLException 
	 */
	public List<String> get_user_popular_tags(int user_id, int limit) 
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
		return (db_queries_user.get_prefered_tags(current_user_id, 5));
	}
	
	/**
	 * get current user's id
	 * @return the user id as saved in "current_user_id"
	 */
	public int get_current_user_id() 
	{
		return (current_user_id);
	}
}
