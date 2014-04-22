package db;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public abstract class db_operations
{
	private static final int OK = 1;
	private static final int ERR = 0;

	
	//*** db operations- select, insert, update, delete ***//
	/** insert tuple
	 * @param table - the table name we want to insert to
	 * @param values - the values to insert in the correct order
	 * @throws SQLException 
	 */
	protected static int insert(String table, String... values) 
			throws SQLException {

		int i;

		String state = "INSERT INTO " + table + " VALUES (";

		for (i = 0; i < values.length; i++) 
		{
			state = state + values[i];

			if (i != values.length - 1)
				state = state + ",";
		}
		state += ")";

		System.out.println(state);
		Connection conn = null;

		conn = jdbc_connection_pooling.get_conn().connectionCheck();

		Statement stmt = null;

		stmt = conn.createStatement();

		stmt.executeUpdate(state);

		jdbc_connection_pooling.get_conn().close(conn);
		return OK;
	}

	// columns needs to look like "'col1', 'col2', 'col3'..."
	protected static int insert_columns(String table,String columns, String... values) 
			throws SQLException {

		int i;

		String state = "INSERT INTO " + table + " (" + columns + ") VALUES (";

		for (i = 0; i < Array.getLength(values); i++) {
			state = state+values[i];

			if (i != Array.getLength(values) - 1)
				state = state + ",";
		}
		state += ")";

		System.out.println(state);
		Connection conn = null;
		
		conn = jdbc_connection_pooling.get_conn().connectionCheck();

		Statement stmt = null;

		stmt = conn.createStatement();

		stmt.executeUpdate(state);
			
		jdbc_connection_pooling.get_conn().close(conn);
		return OK;

	}
	
	/** select query 
	 * @param select- the string that should come after "SELECT"
	 * @param from- the string that should come after "FROM"
	 * @param where- the string that should come after "WHER"
	 * @return ResultSet with the result to that query
	 * @throws SQLException 
	 */
	protected static ResultSet select(String select, String from, String where) 
			throws SQLException 
	{

		ResultSet result = null;
		Connection conn = null;
		Statement stmt = null;
		jdbc_connection_pooling connPool;
		
		// Get connection pool
		connPool = jdbc_connection_pooling.get_conn();
		if (connPool == null)
			return (null);
		
		// confirm connection
		conn = connPool.connectionCheck();
		if (conn == null)
			return (null);
		
		// Start the statment
		stmt = conn.createStatement();
			
		if (where == "")
			result = stmt.executeQuery("SELECT " + select + " FROM " + from);
		else
			result = stmt.executeQuery("SELECT " + select + " FROM " + from + " WHERE " + where);
		
		// Close connection
		jdbc_connection_pooling.get_conn().close(conn);
		
		return (result);
	}

	/** update tuple
	 * @param tablrNamr- the table name as appear in the db. Comes after "UODATE"
	 * @param columnSet- string that should come after "SET"
	 * @param predicatesSet - string that should come after "WHERE"
	 */
	protected static int update(String tableName, String columnSet, String predicatesSet) {
		Connection conn = null;
		try {
			conn = jdbc_connection_pooling.get_conn().connectionCheck();

			Statement stmt = null;

			stmt = conn.createStatement();

			stmt.executeUpdate("UPDATE " + tableName + " SET " + columnSet
					+" WHERE "+ predicatesSet);
		}

		catch (SQLException e) {

			e.printStackTrace();
			return ERR;
		}
		
		try {
			jdbc_connection_pooling.get_conn().close(conn);
		} catch (SQLException e) {
			System.out.println("An opened connection could not be closed");
		}
		return OK;

	}

	/** delete tuple
	 * @param tableName- the name of the table we want to delete from
	 * @param whereCol- string that comes after "WHERE"- tells which tuple to delete
	 * @return -1 for error, else the number of rows efected
	 */
	protected static int delete(String tableName, String whereCol) 
	{
		if (tableName == null)
			return (0);
		
		Connection conn = null;
		try {
			conn = jdbc_connection_pooling.get_conn().connectionCheck();

			Statement stmt = null;

			stmt = conn.createStatement();
			
			if (whereCol != null && whereCol != "")
			{
				stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + whereCol );
			}
			else
			{
				stmt.executeUpdate("DELETE FROM " + tableName);
			}
		}

		catch (SQLException e) 
		{
			return (-1);
		}

		return OK;
	}

	//*** helper function ***//

	/** get a connection */
	protected Connection getConnection() {
		Connection conn = null;
		try {
			conn = jdbc_connection_pooling.get_conn().connectionCheck();
		} catch (SQLException e1) {			
			e1.printStackTrace();
		}
		return conn;
	}

	/** get statement from the conn */
	protected Statement getStatement(Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return stmt;
	}
	
	/** close safely the resources */
	protected void safelyClose(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			try {
				resource.close();
				System.out.println(resources + "was closed safely");
			}
			catch (Exception e) {
			}
		}
	}

}