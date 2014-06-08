package bl;

import java.sql.SQLException;
import java.util.HashMap;
import db.*;

/**
 * this class is of the BL layer
 * it wraps the functions of db_queries_person and basically does nothing else
 * 
 * we do not  use direct reach to db_queries functions to stay in the layers model
 * @author MatanPoleg
 *
 */
public abstract class person_logics 
{

// BOOLEANS
	
	/**
	 * @return true iff actor_id played in movie_id 
	 * @throws SQLException
	 */
	public static boolean did_actor_play_in_movie(int movie_id, int actor_id) 
			throws SQLException
	{
		return (db_queries_persons.did_actor_play_in_movie(movie_id, actor_id));
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
		return (db_queries_persons.get_all_persons());
	}
	
	/**
	 * ID as listed on the DB
	 * @return - returnes HM<personId, personName>
	 * @throws SQLException
	 */
	public static HashMap<String, Integer> get_person_names_and_ids() 
			throws SQLException 
	{
		return (db_queries_persons.get_person_names_and_ids());
	}

	/** get actor names and IDs
	 * NOTE! 	theIDs are idActor, not idPerson
	 * @throws SQLException 
	 */
	public static HashMap <String,Integer> get_actor_names_and_ids() 
			throws SQLException
	{
		return (db_queries_persons.get_actor_names_and_ids());
	}
	
	/** get director names and IDs
	 * NOTE! 	the IDs are idDirector, not idPerson
	 * @throws SQLException 
	 */
	public static HashMap <String,Integer> get_director_names_and_ids() 
				throws SQLException
		{
			return (db_queries_persons.get_director_names_and_ids());
		}
			
// ID GETTERS
	
	/**
	 * @return - if not exists retutn 0, else return id
	 * @throws SQLException
	 */
	public static int get_person_id(String person_name) 
			throws SQLException
	{
		return (db_queries_persons.get_person_id(person_name));
	}
	
	/**
	 * @return - if not exists retutn 0, else return id
	 * @throws SQLException
	 */
	public static String get_person_name(int person_id) 
			throws SQLException
	{
		return (db_queries_persons.get_person_name(person_id));
	}
			
// REMOVERS
	
	/**
	 * remove movie_actors relation
	 * @return did succeed
	 * @throws SQLException
	 */
	public static boolean clear_movie_actors() 
			throws SQLException
	{
		return (db_queries_persons.clear_movie_actors());
	}
	
}
