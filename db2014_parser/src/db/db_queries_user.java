package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import parser_entities.*;

/**
 * The communication with the db is being made by this class methods. 
 */
public abstract class db_queries_user extends db_operations
{		

// INSERTERS
	
	/**
	 * add user to the system
	 * @param pass - password is saved as hash of pass
	 * @return did succeed?
	 * @throws SQLException
	 */
	public static boolean add_user(String user, String pass) 
			throws SQLException
	{
		int rows_effected = insert("users", "`userName`, `hashPassword`" , user, Integer.toString(pass.hashCode()));
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	
	/**
	 * make two users friends
	 * @param user1_id, user2_id - are similar, no difference between 1 and 2
	 * @return did succeed
	 * @throws SQLException
	 */
	public static boolean add_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException 
	{
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		int rows_effected = insert("friend_relation", "`friend1`, `friend2`, `friendshipDate`" , user1_id , user2_id , date);
		
		// did select find souch user
		if (rows_effected > 0)
			return (true);
		else
			return (false);
	}
	 
// BOOLEANS
	
	public static boolean authenticate_user(String user, String pass) 
			throws SQLException
			{
		if (user == null || pass == null)
			return (false);
		
		String whereSegment = "userName = ? AND hashPassword = ?";
		ResultSet result = select("userName", "users", whereSegment, user,  Integer.toString(pass.hashCode()));
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
			}
	
	public static boolean user_exists(String user_name) 
			throws SQLException
	{
		if (user_name == null)
			return (false);
		
		String whereSegment = "userName = ?";
		ResultSet result = select("userName", "users", whereSegment, user_name);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
		
// GETTERS

	public static List<String> get_prefered_tags(int user_id, int limit) 
			throws SQLException
	{
		// where string includes "order by" field to get the prefered tags
		String where = 	"user_prefence.idTag = tag.idTag AND " +
						"userId = ? ORDER BY tag_user_rate DESC limit " + limit;
		ResultSet result = select("tagName", "user_prefence, tag", where, user_id);
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		List<String> returnedList = new ArrayList<String>();
		while (result.next())
		{
			String name = result.getString("tagName");
			
			returnedList.add(name);
		} 
		
		return (returnedList);
	}
	
	public static List<Integer> get_prefered_tags_ids(int user_id, int limit) 
			throws SQLException
	{
		// where string includes "order by" field to get the prefered tags
		String where = 	"userId = ? ORDER BY tag_user_rate DESC limit " + limit;
		ResultSet result = select("idTag", "user_prefence", where, user_id);
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		List<Integer> returnedList = new ArrayList<Integer>();
		while (result.next())
		{
			int name = result.getInt("idTag");
			
			returnedList.add(name);
		} 
		
		return (returnedList);
	}
	
	/**
	 * get a list of names of the user's friends
	 * @param current_user_id
	 * @throws SQLException 
	 */
	public static List<entity_user> get_user_friends(int current_user_id) 
			throws SQLException 
	{
		String where = 	"((users.idUsers = friend_relation.friend1 OR users.idUsers = friend_relation.friend2) AND " +
						"(friend_relation.friend1 = ? OR friend_relation.friend1 = ?))";
		ResultSet result = select("idUsers, userName", "users, friend_relation", where, current_user_id, current_user_id);
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		List<entity_user> returnedList = new ArrayList<entity_user>();
		String current_user_name = get_name_of_user(current_user_id);
		
		while (result.next())
		{
			int id = result.getInt(1);
			String name = result.getString(2);
			
			// Create a user entity
			entity_user curr = new entity_user(id, name);
			
			if (name != current_user_name)
			{
				if (returnedList.indexOf(curr) == -1)
					returnedList.add(curr);
			}
		} 
		
		return (returnedList);
	}
	
	public static String get_name_of_user(Integer user_id) 
			throws SQLException
	{
		String whereClause = "idUsers = ?";
	
		ResultSet results = select("userName" , "users" , whereClause, user_id);
	
		if (results.next())
			return (results.getString(1));
		else
			return (null);
	}
	
	public static List<rating_activity> get_user_recent_rank_activities(int user_id, int limit) 
			throws SQLException 
	{
		String whereClause = "idUser = ? ORDER BY rankDate LIMIT "+ limit;
		
		ResultSet results = select("idMovie, rank, rankDate" , "user_rank" , whereClause, user_id);
	
		if (!results.next())
		{
			return (null);
		}
		else
		{
			// Get the user's name
			String user_name = get_name_of_user(user_id);
			
			// Create a list of the recent activities
			List<rating_activity> retList = new ArrayList<rating_activity>();
			
			do
			{
				// create and add the activity
				retList.add(new rating_activity(user_name,
												results.getInt("idMovie"),
												results.getInt("rank"),
												results.getDate("rankDate")));
				
			} while(results.next());
			
			return (retList);
		}
	}
	
	/** get the latest friendships made
	 * @return	-	List<friendship_activity>
	 * @throws SQLException 
	 */
	public static List<friendship_activity> get_user_recent_friendship_activities(int user_id, int limit) 
			throws SQLException 
	{
		String whereClause = 	"((idUser <> ?) AND" + 
								"(friend1 = idUser OR friend2 = idUser) AND  " +
								"(friend1 = ? OR friend2 = ?)) ORDER BY friendshipDate LIMIT "+ limit;
		
		ResultSet results = select("userName, friendshipDate" , "users, friend_relation" , whereClause, user_id, user_id);
	
		if (!results.next())
		{
			return (null);
		}
		else
		{
			// Get the user's name
			String user_name = get_name_of_user(user_id);					
					
			// Create a list of the recent activities
			List<friendship_activity> retList = new ArrayList<friendship_activity>();
			
			do
			{
				// create and add the activity
				retList.add(new friendship_activity(user_name,
													results.getString("userName"),
													results.getDate("friendshipDate")));
				
			} while(results.next());
			
			return (retList);
		}
	}

	/** get the latest tags made by a user
	 * @return	-	List<tag_activity>
	 * @throws SQLException 
	 */
	public static List<tag_activity> get_user_recent_tag_activities(int user_id, int limit) 
			throws SQLException 
	{
		String whereClause = 	"((tag.idTag = user_tag_movie.idTag) AND (movie.idMovie = user_tag_movie.idMovie) AND"+
								"(user_id = ?)) ORDER BY rateDate LIMIT "+ limit;
		
		ResultSet results = select("userName, friendshipDate" , "tag, movie, user_tag_movie" , whereClause, user_id);
	
		if (!results.next())
		{
			return (null);
		}
		else
		{
			// Get the user's name
			String user_name = get_name_of_user(user_id);
						
			// Create a list of the recent activities
			List<tag_activity> retList = new ArrayList<tag_activity>();
			
			do
			{
				// create and add the activity
				retList.add(new tag_activity(user_name, 
											 results.getInt("rank"),
											 results.getString("movieName"), 
											 results.getString("tagName"), 
											 results.getDate("rateDate")));
				
			} while(results.next());
			
			return (retList);
		}
	}
	
// ID GETTERS
	
	public static int get_user_id(String user_name) 
			throws SQLException
	{
		if (user_name == null)
			return (0);
		
		String whereClause = "userName = ?";
		ResultSet results = select("idUsers", "users" , whereClause, user_name);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString(1)));
		else
			return (0);
	}
		
// REMOVERS
	
	/**
	 * remove friendship, as in the insert 1 and 2 are the same and will only be found once
	 * @return did succeed?
	 * @throws SQLException
	 */
	public static boolean remove_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		if (user1_id == null || user2_id == null)
			return (false);
		
		// friendship is irelevand of who is 1 and who is 2..
		String whereCol = 	"(friend1 = ? AND friend2 = ? ) OR " +
							"(friend2 = ? AND friend1 = ? )";
		
		return (delete("friend_relation", whereCol, user1_id, user2_id, user1_id, user2_id) > 0);
	}

	/**
	 * 
	 * @param userName - if there are two userNames they will both be deleted
	 * @return - did succeed
	 * @throws SQLException
	 */
	public static boolean delete_user(String userName) 
			throws SQLException 
	{
		if (userName == null)
			return (false);
		
		// friendship is irelevand of who is 1 and who is 2..
		String whereCol = 	"userName = ?";
		
		return (delete("users", whereCol, userName) > 0);
	}

	/**
	 * update a name of user that has that id, only if the pass is correct
	 * @param pass	- non hashed pass (will hash it here)
	 * @return 0 if there is no such user or pass is incorect
	 * @throws SQLException
	 */
	public static boolean update_name(String new_name, int id, String pass) 
			throws SQLException 
	{
		String querey = "UPDATE users SET userName = ? WHERE idUsers = ? AND hashPassword = ?;";
		
		return (run_querey(querey, new_name, id, Integer.toString(pass.hashCode())) > 0);
	}

	/**
	 * update a pass of user that has that id, only if the pass is correct
	 * @param pass	- non hashed pass (will hash it here)
	 * @return 0 if there is no such user or pass is incorect
	 * @throws SQLException
	 */
	public static boolean update_pass(String new_pass, int id, String olp_pass) 
			throws SQLException 
	{
		String querey = "UPDATE users SET hashPassword = ? WHERE idUsers = ? AND hashPassword = ?;";
		
		return (run_querey(querey, Integer.toString(new_pass.hashCode()) , id, Integer.toString(olp_pass.hashCode())) > 0);
	}
	
}

