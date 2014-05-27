package parser_entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class entity_movie  implements Serializable{
	
	/*private members*/
	private String 						entity_movie_name;
	private String 						entity_movie_fq_name; 
	private String 						entity_movie_yago_id;
	private String 						entity_movie_imdb_name;
	private String 						entity_movie_length; 
	private String 						entity_movie_year; 
	private Set<String>					entity_movie_genres;
	private Set<String> 				entity_movie_tags;
	private Set<String> 				entity_movie_labels;
	private Set<String> 				entity_movie_labels_fq;
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
		entity_movie_labels= new LinkedHashSet<String>();
		entity_movie_labels_fq= new LinkedHashSet<String>();
		this.entity_movie_yago_id = id;
		/*entity_movie_name = new String(); 
		entity_movie_yago_id= new String();
		entity_movie_imdb_name= new String();
		entity_movie_length= new String(); 
		entity_movie_year= new String();
		entity_movie_language= new String(); 
		entity_movie_director= new entity_person("NAN");
		entity_movie_wikipedia_url = new String();
		entity_movie_plot= new String(); 
		entity_movie_youtube_url= new String();
		entity_movie_poster_url= new String();*/

	}

	/**
	 * both yago and IMDB have movie name, year and director. we use this as a unique identifier
	 * meaning, we enrich a yago title, with IMDB data iff they agree on these three parameters 
	 * @return a qualified name like <Marrie Antionette (2006) (Sofia Copola)>
	 */
	public String get_movie_qualified_name()
	{
		return this.entity_movie_fq_name;
	}
	
	public Set<String> get_movie_labels()
	{
		return this.entity_movie_labels;
	}
	
	public void add_to_labels(String label)
	{
		if (label!=null)
			this.entity_movie_labels.add(label);
	}
	/** 
	 * when called upon, update movie's fq name, 
	 * and all fq names for foreign names (labels) 
	 * **/
	public void set_fq_name()
	{
		/*major fq name*/
		this.entity_movie_fq_name = build_fq_name(this.get_movie_name());
		for (String label : this.entity_movie_labels)
		{
			String label_fq_name = build_fq_name(label);
			if (label_fq_name != null)
				this.entity_movie_labels_fq.add(label_fq_name);
		}
			
	}
	
	public Set<String> get_label_fq_names()
	{
		return this.entity_movie_labels_fq;
	}
	
	/**uses a passed name parameter, and director/year 
	 * attributes of the object itself to construct fq names**/
	private String build_fq_name(String movie_name)
	{
		StringBuilder sb = new StringBuilder();
		if (movie_name == null)
		{
			return null;
		}
		/* clean up name from extra data */
		if (movie_name.indexOf("(")>0)
			movie_name = (movie_name.substring(0,movie_name.indexOf("("))).trim();
		
		sb.append(movie_name);
		
		if (this.get_movie_year() != null)
			sb.append(" (" + this.get_movie_year() + ")");
		else
			sb.append(" (NAN)");
		
		if (this.get_movie_director() != null)
			sb.append(" (" + this.get_movie_director() + ")");
		else
			sb.append(" (NAN)");
		
		return sb.toString();
	}
	
	
	public void add_to_actors(entity_person movie_actor) {
		if(movie_actor != null)
			this.entity_movie_actors.add(movie_actor);
	}
	
	/** adding genre to the genreList */
	public void add_to_genres(String movie_genre) {
		if (movie_genre != null)
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
		if (movie_tag != null)
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
		if (movie_name != null)
			this.entity_movie_name = movie_name;
	}

	public String get_movie_length() {
		return entity_movie_length;
	}

	public void set_movie_length(String movie_length) {
		if (movie_length != null)
			this.entity_movie_length = movie_length;
	}

	public Set<String> get_movie_genres() {
		return entity_movie_genres;
	}

	public void set_movie_genres(Set<String> movie_genres) {
		if (movie_genres != null)
			this.entity_movie_genres = movie_genres;
	}

	public String get_movie_language() {
		return entity_movie_language;
	}

	public void set_movie_langage(String movie_langage) {
		if (movie_langage != null) 
			this.entity_movie_language = movie_langage;
	}

	public entity_person get_movie_director() {
		return entity_movie_director;
	}

	public void set_movie_director(entity_person movie_director) {
		if (movie_director != null)
			this.entity_movie_director = movie_director;
	}

	public List<entity_person> get_movie_actors() {
		return entity_movie_actors;
	}

	public String get_movie_wikipedia_url() {
		return entity_movie_wikipedia_url;
	}

	public void set_movie_wikipedia_url(String movie_wikipedia_url) {
		if (movie_wikipedia_url != null)
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
		if (movie_year != null)
			this.entity_movie_year = movie_year;
	}
	
	public String get_movie_plot() {
		return entity_movie_plot;
	}
	
	public void set_movie_plot(String movie_plot) {
		if (movie_plot != null)
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
