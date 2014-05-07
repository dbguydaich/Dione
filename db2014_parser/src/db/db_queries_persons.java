package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public abstract class db_queries_persons extends db_operations
{		

// INSERTERS

	
// BOOLEANS
	
	public static boolean did_actor_play_in_movie(int movie_id, int actor_id) 
			throws SQLException
	{
		String whereClause = "idMovie = ? AND idActor = ?";
		ResultSet result = select("idMovie", "actor_movie" , whereClause, movie_id, actor_id);
		
		// did select find souch user
		if (result.next())
			return (true);
		else
			return (false);
	}
	
// GETTERS

	/**
	 * ID as listed on the DB
	 * @return - returnes HM<personId, personName>
	 * @throws SQLException
	 */
	public static HashMap<String, String> get_all_persons() 
			throws SQLException 
	{
		ResultSet result = select("idPerson, personName", "person", "");
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		HashMap<String, String> returnedSet = new HashMap<String, String>();
		
		while (result.next())
		{
			String name = result.getString("personName");
			Integer id = result.getInt("idPerson");
			
			returnedSet.put(id.toString(), name);
		}

		return (returnedSet);
	}

	
	/**
	 * ID as listed on the DB
	 * @return - returnes HM<personId, personName>
	 * @throws SQLException
	 */
	public static HashMap<String, Integer> get_person_names_and_ids() 
			throws SQLException 
	{
		ResultSet result = select("idPerson, personName", "person", "");
		
		// is table empty
		if (result == null)
			return (null);
		
		// Enumerate all movies
		HashMap<String, Integer> returnedSet = new HashMap<String, Integer>();
		
		while (result.next())
		{
			String name = result.getString("personName");
			Integer id = result.getInt("idPerson");
			
			returnedSet.put(name,id);
		}

		return (returnedSet);
	}
	


	/** get actor names and IDs
	 * NOTE! 	theIDs are idActor, not idPerson
	 * @throws SQLException 
	 */
	public static HashMap <String,Integer> get_actor_names_and_ids() 
			throws SQLException
	{
		return (generic_get_two_values("idActor, personName", "actor, person", "person.idPerson = actor.idPerson"));
	}
	
	/** get director names and IDs
	 * NOTE! 	the IDs are idDirector, not idPerson
	 * @throws SQLException 
	 */
	public static HashMap <String,Integer> get_director_names_and_ids() 
				throws SQLException
		{
			return (generic_get_two_values("idDirector, personName", "person, director", "person.idPerson = director.idPerson"));
		}
			
// ID GETTERS
	
	// return - if not exists retutn 0, else return id 
	public static int get_person_id(String person_name) 
			throws NumberFormatException, SQLException
	{
		if (person_name == null)
			return (0);
		
		String whereClause = "personName = ?";
		ResultSet results = select("idMovie", "movie" , whereClause, person_name);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idPerson")));
		else
			return (0);
	}

	// return - if not exists retutn 0, else return  id
	public static int get_actor_id(String actor_name) 
			throws NumberFormatException, SQLException
	{
		if (actor_name == null)
			return (0);
		
		String whereClause = "person.idPerson = actor.idPerson AND personName = ?";
		ResultSet results = select("idActor", "person, actor" , whereClause, actor_name);
		
		// did select find souch user
		if (results.next())
			return (Integer.parseInt(results.getString("idActor")));
		else
			return (0);
	}
	
	
// REMOVERS
	
	// remove movie_actors relation
	public static boolean clear_movie_actors() throws SQLException
	{
		return (delete("actor_movie","") != -1);
	}
	
}

