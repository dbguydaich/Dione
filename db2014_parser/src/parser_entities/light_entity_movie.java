package parser_entities;

import java.sql.SQLException;
import java.util.List;

import db.db_queries_movies;
import db.db_queries_persons;

public class light_entity_movie
{	

// private members
	private int 						entity_movie_id;
	private String 						entity_movie_name;
	private int 						entity_movie_year;  
	private String						entity_movie_wiki;
	private String		 				entity_movie_director;
	private int							entity_movie_duration;
	private String 						entity_movie_plot;

// constructor
	public light_entity_movie(Integer id, String name, Integer year,String wiki, String director, Integer duration, String plot)
	{
		if (id != null)
			this.entity_movie_id = id;
		else
			this.entity_movie_id = 0;
		
		if (year != null)
			this.entity_movie_year = year;
		else
			this.entity_movie_year = 0;
		
		if (duration != null)
			this.entity_movie_duration = duration;
		else
			this.entity_movie_duration = 0;
		
		if (name != null)
			this.entity_movie_name = name;
		else
			this.entity_movie_name = "";
		
		if (wiki != null)
			this.entity_movie_wiki = wiki;
		else
			this.entity_movie_wiki = "";
		
		if (director != null)
			this.entity_movie_director = director;
		else
			this.entity_movie_director = "";

		if (plot != null)
			this.entity_movie_plot = plot;
		else
			this.entity_movie_plot = "";
	}

	@Override
	public String toString() 
	{
		return "name=" + get_movie_name() + ", director= " + get_movie_director() + "\n year= " + get_movie_year() +
				", Plot:\n" + get_movie_plot() + "\n";
	}

// getters & setters
	public int get_movie_id() {
		return entity_movie_id;
	}
	
	public String get_movie_name() {
		return entity_movie_name;
	}

	public void set_movie_name(String movie_name) {
		this.entity_movie_name = movie_name;
	}

	public String get_movie_director() 
	{
		return entity_movie_director;
	}

	public int get_movie_year() {
		return entity_movie_year;
	}

	public String get_movie_plot() {
		return entity_movie_plot;
	}

	public String get_movie_language() 
		throws SQLException 
	{
		
		return (db_queries_movies.get_movie_language(this.entity_movie_id));
	}

	
	public int get_movie_duration() 
	{
		return (entity_movie_duration);
	}

	public List<entity_person> get_movie_actors() 
			throws SQLException 
	{
		return (db_queries_persons.get_movie_actors(entity_movie_id));
	}


	public int get_movie_rating() 
			throws SQLException
	{
		return (db_queries_movies.get_movie_rating(entity_movie_id));
	}

	
	public String get_movie_wikipedia_url() 
	{
		return (this.entity_movie_wiki);
	}


	public List<String> get_movie_tags() 
			throws SQLException 
	{
		return (db_queries_movies.get_movie_tags(entity_movie_id));
	}
}
