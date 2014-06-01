package parser_entities;

import java.sql.Timestamp;

public class friendship_activity extends abstract_activity 
{
	private String 	friendName;
	
	public friendship_activity(String user, String name, Timestamp date) 
	{
		super("friending", user, date);
		
		this.friendName = name;
	}

	public String get_friend()
	{
		return friendName;
	}
	
	public String toString()
	{
		return ("On the " + get_activity_time() + " " + get_activity_user() + " and " + get_friend() +
				" became friends"); 
	}

}
