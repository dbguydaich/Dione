package parser_entities;

public class light_entity_movie
{	

// private members
	private int 						entity_movie_id;
	private String 						entity_movie_name;
	private String 						entity_movie_year;  
	private String		 				entity_movie_director;
	private String 						entity_movie_plot;

// constructor
	public light_entity_movie(int id, String name, String year, String director, String entity_movie_plot)
	{
		this.entity_movie_id = id;
		this.entity_movie_name = name;  
		this.entity_movie_year = year; 
		this.entity_movie_director = director;
		this.entity_movie_plot = entity_movie_plot; 
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

	public String get_movie_year() {
		return entity_movie_year;
	}

	public String get_movie_plot() {
		return entity_movie_plot;
	}
}
