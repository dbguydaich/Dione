package parser_entities;

import java.sql.Timestamp;

public class tag_activity extends abstract_activity 
{
	private int rating;
	private String movie;
	private String tag;
	
	public tag_activity(String user, int rating, String movie, String tag, Timestamp date)
	{
		super("tagging", user, date);
		
		this.rating = rating;
		this.movie = movie;
		this.tag = tag;
	}
	
	public int get_rating()
	{
		return rating;
	}
	
	public String get_movie()
	{
		return movie;
	}
	
	public String get_tag()
	{
		return tag;
	}

	public String toString()
	{
		return ("On the " + get_activity_time() + " " + get_activity_user() + " rated the tag " + get_tag() + 
				" of the movie " + get_movie() + " with the score " + get_rating()); 
	}
}
