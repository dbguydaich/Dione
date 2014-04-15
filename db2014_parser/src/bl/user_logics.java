package bl;

import java.sql.SQLException;

import db.db_queries;

public class user_logics 
{
	public boolean add_user(String user, String pass)
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
		return (db_queries.does_user_exists(user_name));
	}

}
