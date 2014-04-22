package bl;

import java.sql.SQLException;

import db.db_queries;

public class user_logics 
{
	public boolean add_user(String user, String pass)
			throws SQLException
	{
		return (db_queries.add_user(user, pass));
	}
	
	public boolean authenticate_user(String user, String pass) 
			throws SQLException
	{
		return (db_queries.authenticate_user(user, pass));
	}
	
	public boolean does_user_exists(String user_name) 
			throws SQLException
	{
		return (db_queries.user_exists(user_name));
	}

	public boolean add_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		return (db_queries.add_friendship(user1_id, user2_id));
	}

	public boolean remove_friendship(Integer user1_id, Integer user2_id) 
			throws SQLException
	{
		return (db_queries.remove_friendship(user1_id, user2_id));
	}

}
