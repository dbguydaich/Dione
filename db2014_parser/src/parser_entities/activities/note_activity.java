package parser_entities.activities;

import java.sql.Timestamp;

public class note_activity extends abstract_activity 
{
	private String movie;
	private String note;
	
	public note_activity(String user, String movie, String note, Timestamp date)
	{
		super("note writing", user, date);
		
		this.movie = movie;
		this.note = note;
	}
	
	public String get_note()
	{
		return note;
	}
	
	public String get_movie()
	{
		return movie;
	}

	public String fullNote()
	{
		return (get_activity_user() + " wrote a comment on the movie " + '"' + get_movie() + '"' + " saying:\n " + get_note());
	}
	
	public String toString()
	{
		return ("On the " + get_activity_time() + " " + get_activity_user() + " wrote a note on the movie " + get_movie()); 
	}
}
