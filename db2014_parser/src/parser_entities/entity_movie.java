package parser_entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class entity_movie  implements Serializable{
	
	/*private members*/
	private String 						entity_movie_name; 
	private String 						entity_movie_yago_id;
	private String 						entity_movie_imdb_name;
	private String 						entity_movie_length; 
	private String 						entity_movie_year; 
	private Set<String>					entity_movie_genres;
	private Set<String> 				entity_movie_tags;
	private String 						entity_movie_language; 
	private entity_person 				entity_movie_director;
	private List<entity_person> 		entity_movie_actors;
	private String 						entity_movie_wikipedia_url;
	private String 						entity_movie_plot; 
	private String 						entity_movie_youtube_url;
	private String 						entity_movie_poster_url;
	private double 						entity_movie_rating;

	/* constructor */	
	public entity_movie(String id)
	{
		entity_movie_genres = new LinkedHashSet<String>();
		entity_movie_actors = new ArrayList<entity_person>();
		entity_movie_tags = new LinkedHashSet<String>();
		this.entity_movie_yago_id = id;
	}

	
	public void add_to_actors(entity_person movie_actor) {
		if(movie_actor != null)
			this.entity_movie_actors.add(movie_actor);
	}
	
	/** adding genre to the genreList */
	public void add_to_genres(String movie_genre) {
		this.entity_movie_genres.add(movie_genre);
	}
	
	/** adding genre to the genreList */
	public void remove_from_tags(String movie_tag) {
		this.entity_movie_tags.remove(movie_tag);
	}
	
	/** adding genre to the genreList */
	public Set<String> get_movie_tags() {
		return entity_movie_tags;
	}	
	
	/** adding genre to the genreList */
	public void add_to_tags(String movie_tag) {
		this.entity_movie_tags.add(movie_tag);
	}
	
	@Override
	public String toString() {
		return "id= " + get_movie_yago_id() + ",   name=" + get_movie_name() + "\n" + "actors= " + entity_movie_actors +"\n"
				+ "director= " + get_movie_director() +"\n" + "year= " + get_movie_year()
				+ ", duration= " + get_movie_length() + "\n"+ "wiki= " + get_movie_wikipedia_url() +"\n" +
				"generes: " + get_movie_genres() + " language: "+ get_movie_language() + "\n"+
				"Plot: " + get_movie_plot() + "\n" + " tags: " + get_movie_tags();
	}

	/* getters & setters */

	public String get_movie_name() {
		return entity_movie_name;
	}

	public void set_movie_name(String movie_name) {
		this.entity_movie_name = movie_name;
	}

	public String get_movie_length() {
		return entity_movie_length;
	}

	public void set_movie_length(String movie_length) {
		this.entity_movie_length = movie_length;
	}

	public Set<String> get_movie_genres() {
		return entity_movie_genres;
	}

	public void set_movie_genres(Set<String> movie_genres) {
		this.entity_movie_genres = movie_genres;
	}

	public String get_movie_language() {
		return entity_movie_language;
	}

	public void set_movie_langage(String movie_langage) {
		this.entity_movie_language = movie_langage;
	}

	public entity_person get_movie_director() {
		return entity_movie_director;
	}

	public void set_movie_director(entity_person movie_director) {
		this.entity_movie_director = movie_director;
	}

	public List<entity_person> get_movie_actors() {
		return entity_movie_actors;
	}

	public String get_movie_wikipedia_url() {
		return entity_movie_wikipedia_url;
	}

	public void set_movie_wikipedia_url(String movie_wikipedia_url) {
		this.entity_movie_wikipedia_url = movie_wikipedia_url;
	}

	public String get_youtube_url() {
		return entity_movie_youtube_url;
	}

	public void set_youtube_url(String movie_youtube_url) {
		this.entity_movie_youtube_url = movie_youtube_url;
	}

	public String get_movie_year() {
		return entity_movie_year;
	}

	public void set_movie_year(String movie_year) {
		this.entity_movie_year = movie_year;
	}
	
	public String get_movie_plot() {
		return entity_movie_plot;
	}
	
	public void set_movie_plot(String movie_plot) {
		this.entity_movie_plot = movie_plot;
	}

	public String get_movie_yago_id() {
		return entity_movie_yago_id;
	}

	public void set_movie_yago_id(String movie_yago_id) {
		this.entity_movie_yago_id = movie_yago_id;
	}

	public String get_movie_poster_url() {
		return entity_movie_poster_url;
	}

	public void set_movie_poster_url(String movie_poster_url) {
		this.entity_movie_poster_url = movie_poster_url;
	}

	public double get_movie_rating() {
		return entity_movie_rating;
	}

	public void setGrade(double movie_rating) {
		this.entity_movie_rating = movie_rating;
	}
	
	public void set_imdb_name(String imdb_movie_name) {
		this.entity_movie_imdb_name = imdb_movie_name;
	}
	
	public String get_imdb_name() {
		return entity_movie_imdb_name;
	}

}
