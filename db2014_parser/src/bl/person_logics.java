package bl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import parser_entities.*;
import db.*;

public abstract class person_logics 
{
	// INSERTERS

	
	// BOOLEANS
		
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
		
		// return - if not exists retutn 0, else return id 
		public static int get_person_id(String person_name) 
				throws NumberFormatException, SQLException
		{
			return (db_queries_persons.get_person_id(person_name));
		}
		
		// return - if not exists retutn 0, else return id 
		public static String get_person_name(int person_id) 
				throws SQLException
		{
			return (db_queries_persons.get_person_name(person_id));
		}
				
	// REMOVERS
		
		// remove movie_actors relation
		public static boolean clear_movie_actors() 
				throws SQLException
		{
			return (db_queries_persons.clear_movie_actors());
		}
	
}
