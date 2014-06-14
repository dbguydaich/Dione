package dione.core.entities;

import java.sql.Timestamp;

public class rating_activity extends abstract_activity 
{
	private int rating;
	private String movie;
	
	public rating_activity(String user, String movie, int rating, Timestamp date) 
	{
		super("rating", user, date);
		
		this.movie = movie;
		this.rating = rating;
	}
	
	public int get_rating()
	{
		return rating;
	}
	
	public String get_movie()
	{
		return movie;
	}

	public String toString()
	{
		return ("On the " + get_activity_time() + " " + get_activity_user() + " rated the movie "+ get_movie() +
				" with the score " + get_rating()); 
	}
	
}
