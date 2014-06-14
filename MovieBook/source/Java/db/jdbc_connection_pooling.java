package db;

import java.util.*;
import java.sql.*;

import config.config;

public class jdbc_connection_pooling
{
	// The singletone connection
	private static jdbc_connection_pooling instance = null;
	
	private int initialConnections;
	private Vector<Connection> connectionsAvailable = new Vector<Connection>();
	private Vector<Connection> connectionsUsed = new Vector<Connection>();

	private String connectionUrl;
	private String userName;
	private String userPassword ;


	/**
	 * A defauld constructor for connection pool, 
	 * Is private for usage as singeltone 
	 * @throws SQLException
	 */
	private jdbc_connection_pooling() 
			throws SQLException 
	{
		try 
		{
			// Import DB configuration
			config settings = new config();
			String hostAddress = settings.get_host_address();
			String port = settings.get_port();
			
			// Init the attributes
			this.connectionUrl = "jdbc:mysql://"+ hostAddress + ":" + port + "/"+ settings.get_db_name();
			this.userName = settings.get_user_name();
			this.userPassword = settings.get_password();
			initialConnections = settings.get_number_connection();

			// ask the driver for a connection
			Class.forName("com.mysql.jdbc.Driver");
			for (int count = 0 ; count < initialConnections ; count++) 
			{
				connectionsAvailable.addElement(makeNewConnection());
			}
		} catch (ClassNotFoundException e) 
		{
			System.out.println("JDBC driver not found Or the DB is inreachable");
			System.out.println("please check your internet connection and connection string");
		}
	}

	/**
	 * a singltone for a pooling connection
	 * @throws SQLException
	 */
	public static jdbc_connection_pooling get_instance() 
			throws SQLException
	{
		if (instance == null)
		{
			instance = new jdbc_connection_pooling();
		}
		
		return (instance);
	}
	
	/**
	 * Move the conn from the used to the avilable list
	 * @param conn
	 */
	public synchronized void close(Connection conn)
	{
		// Is it currently been used
		if(!connectionsUsed.contains(conn)) 
			return;
		
		// Remove the connection from the used connection list
		Connection connToRemove = null;
		int indexToRemove = connectionsUsed.indexOf(conn);
		connToRemove = (Connection) connectionsUsed.remove(indexToRemove);
		
		// Add it to the avilable list
		connectionsAvailable.add(connToRemove);
	}
	
	/**
	 * returns a java.sql Connection from the available vector
	 * @return Connection to be used with the DB
	 * @throws SQLException
	 */
	public synchronized Connection get_connection() 
			throws SQLException 
	{
		Connection newConnection = null;
		
		// Is there an avilable connection
		if (connectionsAvailable.size() == 0) 
		{
			// Creating and adding a new Connection
			newConnection = makeNewConnection();
			connectionsUsed.addElement(newConnection);
		} 
		else 
		{
			// Move the last connection to the used list and return it
			newConnection = (Connection) connectionsAvailable.lastElement();
			connectionsAvailable.removeElement(newConnection);
			connectionsUsed.addElement(newConnection);
		}
		
		return (newConnection);
	}

	/**
	 * Return the number of currently avilable connections
	 */
	public int availableCount() 
	{
		return connectionsAvailable.size();
	}
	
	/**
	 * Asks the driver for a connection
	 * @throws SQLException
	 */
	private Connection makeNewConnection() 
			throws SQLException 
	{
		return DriverManager.getConnection(connectionUrl, userName, userPassword);
	}

}