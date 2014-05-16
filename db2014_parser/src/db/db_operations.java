package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;


/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public abstract class db_operations
{
	/** insert tuple
	 * @param table - the table name we want to insert to
	 * @param 	values 	- the values to insert in the correct order
	 * 					- for the time stamp insert the string "TimeStamp"
	 * @return -1 error, else number of rows efected
	 * @throws SQLException 
	 */
	protected static int insert(String table, String columns, Object... values) 
			throws SQLException 
	{
		if (table == null || values == null || values.length == 0)
			return (-1);
		
		// Set the connection
		Connection conn = null;
		conn = jdbc_connection_pooling.get_conn().connectionCheck();
		
		// Build insert string
		String insert_string;
		if 	(columns == null  || columns == "")
			insert_string = "INSERT INTO " + table + " VALUES (";
		else
			insert_string = "INSERT INTO " + table + " (" + columns + ") VALUES (";
		
		// Isert the currect number of '?' for values to be inserted
		for (int i = 0 ; i < values.length - 1 ; i++)
			insert_string += "?, ";
		insert_string += "?)";
			
		// Set the statment
		PreparedStatement stmt = null; 
		stmt = conn.prepareStatement(insert_string);

		for (int i = 0; i < values.length; i++) 
		{
			if (values[i] instanceof Integer)
			{
				stmt.setInt((i + 1), (int) values[i]);
				continue;
			} 
			if (values[i] instanceof String)
			{
				// when the current time is needed
				if ((String)values[i] == "TimeStamp")
				{
					stmt.setTimestamp((i + 1), new Timestamp((new Date()).getTime()));
					continue;
				}
				
				stmt.setString((i + 1), (String) values[i]);
				continue;
			}
		}
		
		// execute insert SQL stetement
		int rows_effected = stmt.executeUpdate();
		
		// Close connection
		jdbc_connection_pooling.get_conn().close(conn);
		return (rows_effected);
	}

	/** insert tuple
	 * @param querey - an sql querey fully wrote, using '?' for values
	 * @param values - an array of values to put where the ?s are
	 * @throws SQLException 
	 */
	protected static int run_querey(String querey, Object... values) 
			throws SQLException 
	{
		if (querey == null || values == null)
			return (-1);
		
		if (charCount(querey, '?') != values.length)
			throw(new SQLException("wrong number of parameters for querey"));
		
		// Set the connection
		Connection conn = null;
		conn = jdbc_connection_pooling.get_conn().connectionCheck();
			
		// Set the statment
		PreparedStatement stmt = null; 
		stmt = conn.prepareStatement(querey);

		for (int i = 0; i < values.length; i++) 
		{
			if (values[i] instanceof Integer)
			{
				stmt.setInt((i + 1), (int) values[i]);
				continue;
			} 
			if (values[i] instanceof String)
			{
				// when the current time is needed
				if ((String)values[i] == "TimeStamp")
				{
					stmt.setTimestamp((i + 1), new Timestamp((new Date()).getTime()));
					continue;
				}
				
				stmt.setString((i + 1), (String) values[i]);
				continue;
			}
		}
		
		// execute insert SQL stetement
		int rows_effected = stmt.executeUpdate();
		
		// Close connection
		jdbc_connection_pooling.get_conn().close(conn);
		return (rows_effected);
	}
	
	/**
	 *  Count the number of times c appears in str
	 */
	private static int charCount(String str, char c) 
	{
		int count = 0;
		
		for (int i=0;i<str.length();i++)
		{
			if (c == str.charAt(i))
				++count;
		}
		
		return (count);
	}

	/** delete tuple/s
	 * @param tableName	- the name of the table we want to delete from
	 * @param whereCol	- "WHERE column1 = ? AND column 2 = ? Or ..."
	 * @param values	- the objects to switch '?' in the where clause 
	 * @throws SQLException 
	 */
	protected static int delete(String table, String whereClause, Object... values) throws SQLException 
	{
		if (table == null)
			return (-1);
		
		// Does value count match the number of "?"
		if (values != null)
			if (countChar(whereClause, '?') != values.length)
				return (-1);
		
		// Set the connection
		Connection conn = null;
		conn = jdbc_connection_pooling.get_conn().connectionCheck();
		
		// Build insert string
		String delete_string;
		if 	(whereClause == null || whereClause == "")
			delete_string = "DELETE FROM " + table;
		else
			delete_string = "DELETE FROM " + table + " WHERE " + whereClause;
			
		// Set the statment
		PreparedStatement stmt = null; 
		stmt = conn.prepareStatement(delete_string);

		// Fill the question marks
		if (values != null)
		{
			for (int i = 0; i < values.length; i++) 
			{
				if (values[i] instanceof Integer)
				{
					stmt.setInt((i+1), (int) values[i]);
					continue;
				} 
				if (values[i] instanceof String)
				{	
					stmt.setString((i+1), (String) values[i]);
					continue;
				}
			}
		}
		
		// execute insert SQL stetement
		int rows_effected = stmt.executeUpdate();
		
		// Close connection
		jdbc_connection_pooling.get_conn().close(conn);
		return (rows_effected);
	}

	/** select tuple/s
	 * @param tableName	- the name of the table we want to delete from
	 * @param whereCol	- "WHERE column1 = ? AND column 2 = ? Or ..."
	 * @param values	- the objects to switch '?' in the where clause 
	 * @throws SQLException 
	 */
	protected static ResultSet select(String select, String table , String whereClause, Object... values) throws SQLException 
	{
		if (table == null)
			throw (new SQLException("null table name"));
		
		// Does value count match the number of "?"
		if (values != null)
			if (countChar(whereClause, '?') != values.length)
				throw (new SQLException("wrong number of values entered"));
		
		// Set the connection
		Connection conn = null;
		conn = jdbc_connection_pooling.get_conn().connectionCheck();
		
		// Build insert string
		String insert_string;
		if 	(whereClause == null || whereClause == "")
			insert_string = "SELECT " + select + " FROM " + table;
		else
			insert_string = "SELECT " + select + " FROM " + table + " WHERE " + whereClause;
			
		// Set the statment
		PreparedStatement stmt = null; 
		stmt = conn.prepareStatement(insert_string);

		// Fill the question marks
		if (values != null)
		{
			for (int i = 0; i < values.length; i++) 
			{
				if (values[i] instanceof Integer)
				{
					stmt.setInt((i+1), (int) values[i]);
					continue;
				} 
				if (values[i] instanceof String)
				{	
					stmt.setString((i+1), (String) values[i]);
					continue;
				}
			}
		}
		
		// execute insert SQL stetement
		ResultSet rows_effected = stmt.executeQuery();
		
		// Close connection
		jdbc_connection_pooling.get_conn().close(conn);
		return (rows_effected);
	}

// GENERICS

	/** internaly used in functions that retrieve HM<Str, Int>
	 * @param table		- table name
	 * @param values	- "IntegerVal, StringVal", exactly these values!
	 * @return a HashMap of wanted values from the table
	 * @throws SQLException 
	 */
	protected static HashMap <String,Integer> generic_get_two_values(String values, String table, String where) 
			throws SQLException
	{
		ResultSet result = select(values, table, where);
		
		// is table empty
		if (!result.next())
			return (null);
		
		// Enumerate all movies
		HashMap <String,Integer> retMap =  new HashMap<String,Integer>(); 
		
		do
		{
			Integer id = result.getInt(1);
			String name = result.getString(2);
			
			retMap.put(name,id);
		} while (result.next());
		
		return (retMap);
	}
	
	/** internally used by functions that exchange name for id
	 * @param value 		- name of table with capital first latter
	 * @param value_name	- actual content of string you wish to get
	 * @return 				- the ID of what you wanted
	 * @throws SQLException
	 */
	protected static int generic_id_getter(String value, String value_name) 
			throws SQLException
	{
		if (value == null)
			return (-1);
		
		String whereClause = value.toLowerCase() + "Name = '" + value_name + "'";
		ResultSet results = select("id"+ value, value.toLowerCase() , whereClause);
		
		try{
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("id" + value)));
		else
			return (0);
		} catch (NumberFormatException e)
		{
			return (-1);
		}
	}
	
	/** get a connection 
	 * @throws SQLException */
	protected Connection getConnection() 
			throws SQLException 
	{
		jdbc_connection_pooling jdbc_conn = jdbc_connection_pooling.get_conn();

		return (jdbc_conn.connectionCheck());
	}
	
	/** get statement from the conn 
	 * @throws SQLException */
	protected Statement getStatement() 
			throws SQLException 
	{
		jdbc_connection_pooling jdbc_con = jdbc_connection_pooling.get_conn();
		Connection conn = jdbc_con.connectionCheck();
		
		return (conn.createStatement());
	}
	
// Internal usage
	private static int countChar(String str, char c)
	{
		int count = 0;
		
		for (int i = 0 ; i < str.length() ; i ++)
		{
			if (str.charAt(i) == c)
				++count;
		}
		
		return (count);
	}
	
}


