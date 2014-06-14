package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parser_entities.entity_person;

/**
 * The communication with the db is being made
 * by this class methods. All the operation against the DB:
 */
public class db_queries_persons extends db_operations 
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
			ResultSet result = select_no_limit("idPerson, personName", "person", "");
			
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
			ResultSet result = select_no_limit("idPerson, personName", "person", "");
			
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
		
		public static List<entity_person> get_movie_actors(int movie_id) 
				throws SQLException 
		{
			String where = "person.idPerson = actor_movie.idActor AND actor_movie.idMovie = ?";
			ResultSet result = select("idPerson, personName", "person, actor_movie", where, movie_id);
			
			// Enumerate all movies
			List<entity_person> returnedSet = new ArrayList<entity_person>();			
			
			// is table empty
			if (result != null)
			{
				while (result.next())
				{
					Integer id = result.getInt(1);
					String name = result.getString(2);
					
					returnedSet.add(new entity_person(name, id));
				}
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
			return (generic_get_two_values("person.idPerson, personName", "actor, person", "person.idPerson = actor.idPerson"));
		}
		
		/** get director names and IDs
		 * NOTE! 	the IDs are idDirector, not idPerson
		 * @throws SQLException 
		 */
		public static HashMap <String,Integer> get_director_names_and_ids() 
					throws SQLException
			{
				return (generic_get_two_values("person.idPerson, personName", "person, director", "person.idPerson = director.idPerson"));
			}
				
	// ID GETTERS
		
		// return - if not exists retutn 0, else return id 
		public static int get_person_id(String person_name) 
				throws SQLException
		{
			if (person_name == null)
				return (0);
			
			String whereClause = "personName = ?";
			ResultSet results = select("idPerson", "person" , whereClause, person_name);
			
			// did select find souch user
			if (results.next())
				return (results.getInt("idPerson"));
			else
				return (0);
		}

		public static String get_person_name(Integer person_id) 
				throws SQLException 
		{
			if (person_id == null || person_id == 0)
				return ("");
			
			String whereClause = "idPerson = ?";
			ResultSet results = select("personName", "person" , whereClause, person_id);
			
			// did select find souch user
			if (results.next())
				return (results.getString("personName"));
			else
				return ("");
		}
		
	// REMOVERS
		
		// remove movie_actors relation
		public static boolean clear_movie_actors() throws SQLException
		{
			return (delete("actor_movie","") != -1);
		}

		/**
		 * delete from person everybody who is not an actor nor director
		 * @return true if succeeded
		 * @throws SQLException 
		 */
		public static boolean delete_non_related_persons() 
				throws SQLException
		{
			String where = " idPerson NOT IN (select idPerson from director) AND " +
							" idPerson NOT IN (select idPerson from actor)" ;
			
			return (delete("person",where) >= 0);
		}
			
}
