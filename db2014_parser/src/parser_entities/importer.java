package parser_entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import db.*;

/** A Bussiness Logic class for import of data. 
 ** Recieves parsed and normalized data in sets and maps,
 ** updates db according to existing items, types of import
 ** (insert, update), etc. **/
public class importer extends db_operations{

	private static final Integer DEFAULT_TAG_SCORE = new Integer(5);
	public static final String OBJECT_GENRE = "genre";
	public static final String OBJECT_LANGUAGE = "language";
	public static final String OBJECT_TAG = "tag";
	public static final String PERSON_TYPE_ACTOR  = "actor";
	public static final String PERSON_TYPE_DIRECTOR  = "director";
	
	/*logical represantation of tables is the db*/
	HashMap<String,Integer> movie_table; 
	HashMap<String,Integer> actors_table;
	HashMap<String,Integer> directors_table;
	
	HashMap<String, Integer> genres_table;
	HashMap<String, Integer> tags_table;
	HashMap<String, Integer> languages_table;
	
	public importer()
			throws SQLException
	{
		/*get current database image*/
		movie_table = db_queries_movies.get_movie_names_and_ids();
		if (movie_table==null)
			movie_table = new HashMap<String,Integer>();
		
		actors_table = db_queries_persons.get_actor_names_and_ids();
		if (actors_table==null)
			actors_table = new HashMap<String,Integer>();
		
		directors_table = db_queries_persons.get_director_names_and_ids();
		if (directors_table==null)
			directors_table = new HashMap<String,Integer>();
		
		//genres_table = db_queries_movies.get_genre_names_and_ids();
		if (genres_table==null)
			genres_table = new HashMap<String,Integer>();
		
		//tags_table = db_queries_movies.get_tag_names_and_ids();
		if (tags_table==null)
			tags_table = new HashMap<String,Integer>();
		
		//languages_table = db_queries_movies.get_language_names_and_ids();
		if (languages_table==null)
			languages_table = new HashMap<String,Integer>();
		
	}
	
	/** updates movie tables, with new movies. 
	 ** If the movie already exists - overwrite it's details:
	 ** actors, directors, etc. we add tags only to new movies 
	 * @throws SQLException **/
	public void update_movies_table (HashMap<String,entity_movie> movies_map) throws SQLException
	{
		int batch_count = 0;
		
		/*we update existing movies, so as to keep user rating, etc.*/
		PreparedStatement movie_insert_stmt, movie_update_stmt;
		/*sync tables with db*/
		movie_table = db_queries_movies.get_movie_names_and_ids();
		actors_table = db_queries_persons.get_actor_names_and_ids();
		directors_table = db_queries_persons.get_director_names_and_ids();
				
		Connection db_conn = getConnection();
		
		movie_update_stmt = db_conn.prepareStatement("UPDATE Movie SET idLanguage=?,idDirector=?,movieName=?,year=?,wiki=?,duration=?,plot=? WHERE idMovie=?) VALUES(?,?,?,?,?,?,?,?)");
		movie_insert_stmt = db_conn.prepareStatement("INSERT INTO Movie(idLanguage,idDirector,movieName,year,wiki,duration,plot) VALUES(?,?,?,?,?,?,?,?)");
		
		for (entity_movie movie : movies_map.values())
		{
			/*if movie exists, add an update to the batch*/
			if (db_queries_movies.movie_exist(movie.get_movie_name(), movie.get_movie_year(), movie.get_movie_director().get_person_name()))
			{	
				movie_insert_stmt = set_values_movie_insert(movie_insert_stmt, movie);
				movie_insert_stmt.addBatch(); 
				batch_count++;
			}
			/*movie doesn't exists, insert new movie*/
			else
			{
				movie_update_stmt = set_values_movie_update(movie_update_stmt, movie);
				movie_update_stmt.addBatch();
				batch_count++;
			}
			
		if (batch_count % 1000 == 0)
		{
			movie_update_stmt.executeBatch();
			movie_insert_stmt.executeBatch();
		}		
		}
		/*execute remainder*/ 
		movie_update_stmt.executeBatch();
		movie_insert_stmt.executeBatch();
	}
	
	private static final String MOVIE_ATTRIBUTE_GENRES = "genres";
	private static final String MOVIE_ATTRIBUTE_TAGS = "tags";
	private static final String MOVIE_ATTRIBUTE_ACTORS = "actors";
	
	/**
	 * batch update of one-to-many relationships: movie-genre, movie-tag, movie-actor
	 * @param movies_map - a map of movie entities
	 * @param attribute_type - which one-to-many attribute we wish to update
	 * @throws SQLException
	 */
	public void update_movie_multiple_attributes(HashMap<String,entity_movie> movies_map, String attribute_type) throws SQLException
	{
		movie_table = db_queries_movies.get_movie_names_and_ids();
		int batch_count = 0;
		Set<String> attributes_set;
		
		Connection db_conn = getConnection();
		
		PreparedStatement table_movie_insert_stmt;
		switch (attribute_type) {
		case MOVIE_ATTRIBUTE_GENRES:  
				table_movie_insert_stmt = db_conn.prepareStatement("INSERT INTO GenreMovie(idMovie, idGenre) VALUES(?,?)");
				/*delete movie-genre table*/
				db_queries_movies.clear_movie_genres();
				break;
		case MOVIE_ATTRIBUTE_TAGS:  
				/*we do not delete the tags table, once it exists.*/	
				table_movie_insert_stmt = db_conn.prepareStatement("INSERT INTO tag_movie(idMovie, idTag, scoreTag) VALUES(?,?,?)");
		 		break;
		case MOVIE_ATTRIBUTE_ACTORS:
				table_movie_insert_stmt = db_conn.prepareStatement("INSERT INTO ActorMovie(idMovie, idActor) VALUES(?,?)");
				db_queries_persons.clear_movie_actors();
				break;
		default: table_movie_insert_stmt = null; 
		 		break;
		}
		
		/*for every movie*/
		for (entity_movie movie : movies_map.values())
		{
			Integer movie_id = movie_table.get(movie.get_movie_name());
			if (movie_id == null)
				return;
			/* select appropriate attribute set - the movie's genres, tags or actors*/
			switch (attribute_type) {
			case MOVIE_ATTRIBUTE_GENRES:
				attributes_set = movie.get_movie_genres();	
					break;
			case MOVIE_ATTRIBUTE_TAGS:  
				attributes_set = movie.get_movie_tags();
			 		break;
			case MOVIE_ATTRIBUTE_ACTORS:
				/*we need only actor names...*/
				attributes_set = new HashSet<String>();
				for (entity_person actor : movie.get_movie_actors())
				{
					attributes_set.add(actor.get_person_name());
				}
					break;
			default: attributes_set = null; 
				break; 
			}
			if (attributes_set == null)
				continue;
			
			/* create relevant statement */
			for (String attribute : attributes_set)
			{
				switch (attribute_type) {
				case MOVIE_ATTRIBUTE_GENRES:
					Integer genre_id = genres_table.get(attribute);
					if (genre_id == null)
						continue;
					table_movie_insert_stmt.setInt(0,movie_id);
					table_movie_insert_stmt.setInt(1,genre_id);
					break;
				case MOVIE_ATTRIBUTE_TAGS:  
					Integer tag_id = tags_table.get(attribute);
					if (tag_id == null)
						continue;
					table_movie_insert_stmt.setInt(0,movie_id);
					table_movie_insert_stmt.setInt(1,tag_id);
					table_movie_insert_stmt.setInt(1,DEFAULT_TAG_SCORE);
				 		break;
				case MOVIE_ATTRIBUTE_ACTORS:
					Integer actor_id = actors_table.get(attribute);
					if (actor_id == null)
						continue;
					table_movie_insert_stmt.setInt(0,movie_id);
					table_movie_insert_stmt.setInt(1,actor_id);
						break;
				default: break; 
				}
				table_movie_insert_stmt.addBatch();
				batch_count++;
				
				if (batch_count % 1000 == 0)
				{
					table_movie_insert_stmt.executeBatch();
				}
			}
			/*remainder*/
			table_movie_insert_stmt.executeBatch();
		}
	}
	
	
	/**
	 * 
	 * @param person_map - a map of person entities (actors or directors)
	 * @param person_type - type of person PERSON_TYPE_DIRECTOR or PERSON_TYPE_ACTOR
	 * 
	 * performs a batch update of person entities, in relevant db tables 
	 * first adds person to persons relation in batch, and then adds him to specific directors/actors table
	 * @throws SQLException
	 */
	public void update_person_entities_table(HashMap<String,entity_person> person_map, String person_type) throws SQLException
	{
		int batch_count = 0;
		/* will update relevant person table*/
		PreparedStatement person_insert_stmt = null;
		/*represents specific person subclass (actor/directo) persons and ids*/
		HashMap<String,Integer> person_table = new HashMap<String,Integer>();
		/*represents the general persons and ids entities*/
		HashMap<String,Integer> general_person_table = null;
		
		Connection db_conn = getConnection();
		
		/*wait for matan get_person_names_and_ids();*/
		general_person_table = db_queries_persons.get_person_names_and_ids();
		if (general_person_table == null)
			general_person_table = new HashMap<String,Integer>();
		person_insert_stmt = db_conn.prepareStatement("Insert INTO person(personName) VALUES (?)");
		
		for (entity_person person : person_map.values())
		{
			/*if person exists, ignore*/
			if (general_person_table.get(person.get_person_name()) != null)
				continue; 
			/*no such person, add him to table*/
			else
			{
				person_insert_stmt.setString(1, person.get_person_name());
				person_insert_stmt.addBatch();
				batch_count++;
			}
			
		if (batch_count % 1000 == 0)
			person_insert_stmt.executeBatch();
		
		}
		/*execute remainder*/ 
		person_insert_stmt.executeBatch();
		
		/*wait for matan*/
		/*sync general person table with db*/
		general_person_table = db_queries_persons.get_person_names_and_ids();
		
		/*we now use person ids, to link actor/director to it's person*/
		switch (person_type) {
		case PERSON_TYPE_ACTOR:  
				person_table = db_queries_persons.get_actor_names_and_ids();
				person_insert_stmt = db_conn.prepareStatement("Insert INTO actor(idPerson) VALUES (?)");
		 		break;
		case PERSON_TYPE_DIRECTOR:  
				db_queries_persons.get_director_names_and_ids();
				person_insert_stmt = db_conn.prepareStatement("Insert INTO director(idPerson) VALUES (?)");
		 		break;
		default: person_table = new HashMap<String,Integer>();
		 		break;
		}
		
		if (person_table == null)
			person_table = new HashMap<String,Integer>();
		
		if (person_insert_stmt == null)
			return;
				
		for (entity_person person : person_map.values())
		{
			
			Integer general_person_id = general_person_table.get(person.get_person_name()); 
			/*if person exists, and his id is already an actor/director - move on*/
			if (general_person_id != null && person_table.get(general_person_id) != null)
				continue; 
			/*no such person, add him to table*/
			else
			{
				person_insert_stmt.setInt(1, general_person_id);
				person_insert_stmt.addBatch();
				batch_count++;
			}
			
		if (batch_count % 1000 == 0)
			person_insert_stmt.executeBatch();
		
		}
		/*execute remainder*/ 
		person_insert_stmt.executeBatch();
		
		/*sync parser tables with db after update*/
		switch (person_type) {
		case PERSON_TYPE_ACTOR:  
				this.actors_table = db_queries_persons.get_actor_names_and_ids(); 
		 		break;
		case PERSON_TYPE_DIRECTOR:  
				this.directors_table = db_queries_persons.get_director_names_and_ids();
		 		break;
		default: break;
		}
	}
	
	/** deletes previous actor-movie relation,
	 ** updates with current data from yago**/
	public void update_movie_actors_tabls(HashMap<String,entity_movie> movies_map) throws SQLException
	{
		/*sync tables*/
		movie_table = db_queries_movies.get_movie_names_and_ids();
		actors_table = db_queries_persons.get_actor_names_and_ids();
		
		int batch_count = 0;
		
		Connection db_conn = getConnection();
		
		PreparedStatement actor_movie_insert_stmt;
		actor_movie_insert_stmt = db_conn.prepareStatement("INSERT INTO actormovie(idMovie, idGenre) VALUES(?,?)");
		
		/*delete movie-actor table*/
		db_queries_persons.clear_movie_actors();
		
		for (entity_movie movie : movies_map.values())
		{
			Integer movie_id = movie_table.get(movie.get_movie_name());
			if (movie_id == null)
				return;
			for (entity_person actor : movie.get_movie_actors())
			{
				Integer actor_id = actors_table.get(actor.get_person_name());
				if (actor_id == null)
					continue;
				actor_movie_insert_stmt.setInt(0,movie_id);
				actor_movie_insert_stmt.setInt(1,actor_id);
				actor_movie_insert_stmt.addBatch();
				batch_count++;
				
				if (batch_count % 1000 == 0)
				{
					actor_movie_insert_stmt.executeBatch();
					actor_movie_insert_stmt.executeBatch();
				}		
			}
		}
		
		/*remainder*/
		actor_movie_insert_stmt.executeBatch();
		actor_movie_insert_stmt.executeBatch();
	}

	
	/** set values in a prepared movie insert query
	 * 	return null on failure or if movie id not found **/
	public PreparedStatement set_values_movie_update(PreparedStatement movie_update_stmt, entity_movie movie)
	{
		try {
			Integer movie_id = db_queries_movies.get_movie_id(movie.get_movie_name());
			/*set statment parameters*/
			if (languages_table.get(movie.get_movie_language()) != null)
				movie_update_stmt.setInt(0, languages_table.get(movie.get_movie_language()));
			else
				movie_update_stmt.setNull(0, 2);
			if (directors_table.get(movie.get_movie_director()) != null)
				movie_update_stmt.setInt(1, directors_table.get(movie.get_movie_director()));
			else
				movie_update_stmt.setNull(1, 2);
			
			if (movie.get_movie_name() != null)
				movie_update_stmt.setString(2, movie.get_movie_name());
			else
				movie_update_stmt.setNull(1, 2);
			
			if (movie.get_movie_year() != null)
				movie_update_stmt.setString(3, movie.get_movie_year());
			else
				movie_update_stmt.setNull(3, 2);
			
			if (movie.get_movie_wikipedia_url() != null)
				movie_update_stmt.setString(4, movie.get_movie_wikipedia_url());
			else 
				movie_update_stmt.setNull(4, 2);
			
			if (movie.get_movie_plot() != null)
				movie_update_stmt.setString(5, movie.get_movie_plot());
			else
				movie_update_stmt.setNull(5, 2);
			
			if (movie_id != null)
				movie_update_stmt.setInt(6, movie_id);
			else /*cannot update without id*/
				return null;
			
			return movie_update_stmt;
		}
		catch (Exception ex)
		{
			System.out.println("Error preparing statement update statment" +  ex.getStackTrace());
			return null;
		}
	}
	
	
	/** set values in a prepared movie insert query
	 * 	return null on failure or if movie id not found **/
	public PreparedStatement set_values_movie_insert(PreparedStatement movie_insert_stmt, entity_movie movie)
	{
		/*auto incremental, no need to specify id!*/
		try {
			/*set statment parameters*/
			if (languages_table.get(movie.get_movie_language()) != null)
				movie_insert_stmt.setInt(0, languages_table.get(movie.get_movie_language()));
			else
				movie_insert_stmt.setNull(0, 2);
			if (directors_table.get(movie.get_movie_director()) != null)
				movie_insert_stmt.setInt(1, directors_table.get(movie.get_movie_director()));
			else
				movie_insert_stmt.setNull(1, 2);
			
			if (movie.get_movie_name() != null)
				movie_insert_stmt.setString(2, movie.get_movie_name());
			else
				movie_insert_stmt.setNull(1, 2);
			
			if (movie.get_movie_year() != null)
				movie_insert_stmt.setString(3, movie.get_movie_year());
			else
				movie_insert_stmt.setNull(3, 2);
			
			if (movie.get_movie_wikipedia_url() != null)
				movie_insert_stmt.setString(4, movie.get_movie_wikipedia_url());
			else 
				movie_insert_stmt.setNull(4, 2);
			
			if (movie.get_movie_plot() != null)
				movie_insert_stmt.setString(5, movie.get_movie_plot());
			else
				movie_insert_stmt.setNull(5, 2);
			

			
			return movie_insert_stmt;
		}
		catch (Exception ex)
		{
			System.out.println("Error preparing statement update statment" +  ex.getStackTrace());
			return null;
		}
	}
	
	
	/**
	 * generic misc non-batch updater, for small maps like languages, genres, etc
	 * @param object_set
	 * @param object_name
	 * @throws SQLException
	 */
	public void update_object_by_name(HashSet<String> object_set, String object_name) 
			throws SQLException
	{
		int update_count = 0; 
		HashMap<String,Integer> object_map; 
		/* determine which misc id-name object we're updating*/
		switch (object_name) {
		case OBJECT_GENRE:  object_map = this.genres_table;
		 		break;
		case OBJECT_LANGUAGE:  object_map = this.languages_table;
		 		break;
		case OBJECT_TAG:  object_map = this.tags_table;
		 		break;
		default: object_map = null;
		 		break;
		}
		 
		for (String object : object_set)
		{
			Integer object_id = new Integer(0);
			/*if doesn't exists in DB*/
			if (object_map.get(object) == null)
			{
				/*create and add*/
				switch (object_name) {
				case OBJECT_GENRE:  object_id = db_queries_movies.create_genre(object);
				 		break;
				case OBJECT_LANGUAGE:  object_id = db_queries_movies.create_language(object);
				 		break;
				case OBJECT_TAG:  object_id = db_queries_movies.create_tag(object);
				 		break;
				default: object_id = -1;
				 		break;
				}
				
				if (object_id == -1)
					System.out.println("unable to add" + object_name + ":" + object);
				else
				{
					object_map.put(object,object_id);
					update_count++;
				}
			}
		}
		System.out.println("Updating Object " +  object_name + ":\n\tImported: " 
				+ update_count + " out of Total:" +  object_map.size());
	}
	
	
	/************************ TBD *************************************/
	/**
	 * @param genre_set a set of genres to load
	 * checks which genres exist in db, and load the new ones
	 * @throws SQLException 
	 */
	public void update_genres(HashSet<String> genre_set) 
			throws SQLException
	{
		for (String genre : genre_set)
		{
			Integer genre_id = new Integer(0);
			/*if exists, add to table*/
			if (db_queries_movies.genre_exists(genre))
			{
				genre_id = db_queries_movies.get_genre_id(genre);
				this.genres_table.put(genre, genre_id);
				continue;
			}
			/*create and add*/
			genre_id = db_queries_movies.create_genre(genre);
			if (genre_id == -1)
				System.out.println("unable to add genres:" + genre);
			else
				this.genres_table.put(genre,genre_id);
			
			
		}
	}

	
	/**
	 * @param language_set a set of languages to load
	 * checks which languages exist in db, and load the new ones
	 * @throws SQLException 
	 */
	public void update_languages(HashSet<String> language_set) 
			throws SQLException
	{
		int c_insert = 0;
		for (String language : language_set)
		{
			Integer language_id = new Integer(0);
			/*if exists, add to table*/
			if (db_queries_movies.language_exists(language))
			{
				language_id = db_queries_movies.get_language_id(language);
				this.languages_table.put(language, language_id);
				continue;
			}
			/*create and add*/
			language_id = db_queries_movies.create_language(language);
			this.languages_table.put(language,language_id);
		}
		System.out.println("Tags:\n\tImported: " + c_insert + " out of Total:" +  language_set.size());
	}
	
	/**
	 * @param tag_set a set of tags to load
	 * checks which tags exist in db, and load the new ones
	 * @throws SQLException 
	 */
	public void update_tags(HashSet<String> tag_set) 
			throws SQLException
	{
		int c_tags = 0; 
		
		for (String tag : tag_set)
		{
			Integer tag_id = new Integer(0);
			/*if exists, add to table*/
			if (db_queries_movies.tag_exists(tag))
			{
				tag_id = db_queries_movies.get_tag_id(tag);
				this.tags_table.put(tag, tag_id);
				continue;
			}
			/*create and add*/
			tag_id = db_queries_movies.create_tag(tag);
			this.tags_table.put(tag, tag_id);
			c_tags++;
		}
		System.out.println("Tags:\n\tImported: " + c_tags + " out of Total:" +  tag_set.size());
	}
	
	
	
	public void update_actors_table(HashMap<String,entity_person> actors_map) throws SQLException
	{
		// TODO: add person table handeling...
		int batch_count = 0;
		
		PreparedStatement actor_inser_stmt;

		/*sync tables with db*/
		actors_table = db_queries_persons.get_actor_names_and_ids();
		
		Connection db_conn = getConnection();
				
		actor_inser_stmt = db_conn.prepareStatement("Insert INTO actor(actorName) VALUES (?)");			
		
		for (entity_person actor : actors_map.values())
		{
			/*if person exists, ignore*/
			if (actors_table.get(actor.get_person_name()) != null)
				continue; 
			
			/*no such actor, add him to table*/
			else
			{
				actor_inser_stmt.setString(0, actor.get_person_name());
				actor_inser_stmt.addBatch();
				batch_count++;
			}
			
		if (batch_count % 1000 == 0)
			actor_inser_stmt.executeBatch();
		
		}
		/*execute remainder*/ 
		actor_inser_stmt.executeBatch();
	}
	
	public void update_directors_table(HashMap<String,entity_person> directors_map) throws SQLException
	{
		// TODO: add person table handeling...
		int batch_count = 0;
		
		PreparedStatement director_insert_stmt;

		/*sync tables with db*/
		directors_table = db_queries_persons.get_director_names_and_ids();
		
		Connection db_conn = getConnection();
				
		director_insert_stmt = db_conn.prepareStatement("Insert INTO director(directorName) VALUES (?)");			
		
		for (entity_person director : directors_map.values())
		{
			/*if person exists, ignore*/
			if (directors_table.get(director.get_person_name()) != null)
				continue; 
			
			/*no such director, add him to table*/
			else
			{
				director_insert_stmt.setString(0, director.get_person_name());
				director_insert_stmt.addBatch();
				batch_count++;
			}
			
		if (batch_count % 1000 == 0)
			director_insert_stmt.executeBatch();
		
		}
		/*execute remainder*/ 
		director_insert_stmt.executeBatch();
	}
	
	public void update_movies_genres_table(HashMap<String,entity_movie> movies_map) throws SQLException
	{
		/*sync tables*/
		movie_table = db_queries_movies.get_movie_names_and_ids();
		int batch_count = 0;
		
		Connection db_conn = getConnection();
		
		PreparedStatement genre_movie_insert_stmt;
		genre_movie_insert_stmt = db_conn.prepareStatement("INSERT INTO GenreMovie(idMovie, idGenre) VALUES(?,?)");
		
		/*delete movie-genre table*/
		db_queries_movies.clear_movie_genres();
		
		for (entity_movie movie : movies_map.values())
		{
			Integer movie_id = movie_table.get(movie.get_movie_name());
			if (movie_id == null)
				return;
			for (String genre_name : movie.get_movie_genres())
			{
				Integer genre_id = genres_table.get(genre_name);
				if (genre_id == null)
					continue;
				genre_movie_insert_stmt.setInt(0,movie_id);
				genre_movie_insert_stmt.setInt(1,genre_id);
				genre_movie_insert_stmt.addBatch();
				batch_count++;
				
				if (batch_count % 1000 == 0)
				{
					genre_movie_insert_stmt.executeBatch();
					genre_movie_insert_stmt.executeBatch();
				}		
			}
		}
		
		/*remainder*/
		genre_movie_insert_stmt.executeBatch();
		genre_movie_insert_stmt.executeBatch();
	}
	
	public void update_movies_tags_table(HashMap<String,entity_movie> movies_map) throws SQLException
	{
		/*sync tables*/
		movie_table = db_queries_movies.get_movie_names_and_ids();
		
		/*helper map to determine if movie alread has tags*/
		HashMap <Integer, Integer> movie_tag_count = db_queries_movies.get_movie_id_tag_count();
		int batch_count = 0;
		
		Connection db_conn = getConnection();
		
		PreparedStatement tag_movie_insert_stmt;
		tag_movie_insert_stmt = db_conn.prepareStatement("INSERT INTO tag_movie(idMovie, idTag, scoreTag) VALUES(?,?,?)");
		
		for (entity_movie movie : movies_map.values())
		{
			Integer movie_id = movie_table.get(movie.get_movie_name());
			if (movie_id == null)
				continue;
			
			/*something wrong, or movie already has tags*/
			if (movie_tag_count.get(movie_id) == null || movie_tag_count.get(movie_id) > 0)
				continue; 
			
			/*movie doesn't have tags in db*/
			for (String tag_name : movie.get_movie_tags())
			{
				Integer tag_id = tags_table.get(tag_name);
				if (tag_id == null)
					continue;
				tag_movie_insert_stmt.setInt(0,movie_id);
				tag_movie_insert_stmt.setInt(1,tag_id);
				tag_movie_insert_stmt.setInt(1,DEFAULT_TAG_SCORE);
				tag_movie_insert_stmt.addBatch();
				batch_count++;
				
				if (batch_count % 1000 == 0)
				{
					tag_movie_insert_stmt.executeBatch();
					tag_movie_insert_stmt.executeBatch();
				}		
			}
		}
		
		/*remainder*/
		tag_movie_insert_stmt.executeBatch();
		tag_movie_insert_stmt.executeBatch();
	}
	
	
}

