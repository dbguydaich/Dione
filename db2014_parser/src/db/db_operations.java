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
	 */
	protected static int insert(String table, String... values) {

		int i;

		String state = "INSERT INTO " + table + " VALUES (";

		for (i = 0; i < Array.getLength(values); i++) {
			state = state+values[i];

			if (i != Array.getLength(values) - 1)
				state = state + ",";
		}
		state += ")";

		System.out.println(state);
		Connection conn = null;
		try {
			conn = jdbc_connection_pooling.get_conn().connectionCheck();

			Statement stmt = null;

			stmt = conn.createStatement();

			stmt.executeUpdate(state);
		}

		catch (SQLException e) {
			System.out
			.println("Check that the number of values matches the number of coulmns in the table+ "
					+ table);

			e.printStackTrace();
			return ERR;
		}
		jdbc_connection_pooling.get_conn().close(conn);
		return OK;

	}

	// columns needs to look like "'col1', 'col2', 'col3'..."
	protected static int insert_columns(String table,String columns, String... values) {

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
		try {
			conn = jdbc_connection_pooling.get_conn().connectionCheck();

			Statement stmt = null;

			stmt = conn.createStatement();

			stmt.executeUpdate(state);
		}

		catch (SQLException e) {
			System.out
			.println("Check that the number of values matches the number of coulmns in the table+ "
					+ table);

			e.printStackTrace();
			return ERR;
		}
		jdbc_connection_pooling.get_conn().close(conn);
		return OK;

	}
	
	/** select query 
	 * @param select- the string that should come after "SELECT"
	 * @param from- the string that should come after "FROM"
	 * @param where- the string that should come after "WHER"
	 * @return ResultSet with the result to that query
	 */
	protected static ResultSet select(String select, String from, String where) {

		ResultSet result = null;
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = jdbc_connection_pooling.get_conn().connectionCheck();
			stmt = conn.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();

		}

		try {
			if (where == "")
				result = stmt.executeQuery("SELECT " + select + " FROM " + from);
			else
				result = stmt.executeQuery("SELECT " + select + " FROM " + from
						+ " WHERE " + where);
		} catch (SQLException e) {
			System.out.println("Select Error");
			e.printStackTrace();
		}
		jdbc_connection_pooling.get_conn().close(conn);
		return result;
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
		jdbc_connection_pooling.get_conn().close(conn);
		return OK;

	}

	/** delete tuple
	 * @param tableName- the name of the table we want to delete from
	 * @param whereCol- string that comes after "WHERE"- tells which tuple to delete
	 */
	protected static int delete(String tableName, String whereCol) {
		Connection conn = null;
		try {
			conn = jdbc_connection_pooling.get_conn().connectionCheck();

			Statement stmt = null;

			stmt = conn.createStatement();

			stmt.executeUpdate("DELETE FROM " + tableName + " WHERE "
					+ whereCol );
		}

		catch (SQLException e) {

			e.printStackTrace();
			return ERR;
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

	/** check for updates made by users and update the tables accordingly */
	protected void commitUpdates(Statement stmt) {

		ResultSet updateSet = null;
		// get All the tuples from the Update table
		updateSet = select("*", "Updates", "");
		String tableName=null;
		String columnValue=null;
		int newVal=0;
		int key1=0;
		int key2=0;
		String str = null, str2=null;

		try{
			while (updateSet.next()) {

				tableName=updateSet.getString(1);
				columnValue=updateSet.getString(2);
				newVal=updateSet.getInt(3);
				key1=updateSet.getInt(4);
				key2=updateSet.getInt(5);


				if ((newVal==0) && !(tableName.equals("Movie")) ){ // Delete operation- Table
					// name, key1, key2 - only
					// for ActorMovie,
					// GenreMovie
					if (tableName.equals("ActorMovie"))
						str= "idActor="+key2+" AND idMovie="+key1;
					else str= "idGenre="+key2+" AND idMovie="+key1;
					delete(tableName,str);


				}
				else if ((key2==0) && !(tableName.equals("Movie")))  // Insert operation -
					// Table name, key1,
					// key2- only for
					// ActorMovie,GenreMovie
					insert(tableName, Integer.toString(key1), Integer.toString(newVal));

				else{
					str=columnValue+"="+newVal;

					if (tableName.equals("Movie"))
						str2="idMovie="+key1;


					else if (tableName.equals("ActorMovie"))
						str2="idMovie="+key1+" AND "+ "idActor="+key2;
					else
						str2="idMovie="+key1+" AND "+ "idGenre="+key2;
					update(tableName, str,
							str2);
				}
			}
		}
		catch (SQLException e) {
			System.out.println("Failed to merge the users Updates");
			e.printStackTrace();
		}
		finally{
			try {
				updateSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}