package dione.core.entities;

import java.sql.Timestamp;

public abstract class abstract_activity implements Iactivity, Comparable<abstract_activity>
{
	private String activity_type; 
	private String activity_user;
	private Timestamp activity_time;
	
	protected abstract_activity(String activity_type, String activity_user, Timestamp activity_time)
	{
		this.activity_type = activity_type;
		this.activity_user = activity_user;
		this.activity_time = activity_time;
	}
	
	@Override
	public String get_activity_type() 
	{
		return activity_type;
	}

	@Override
	public String get_activity_user() 
	{
		return activity_user;
	}
	
	@Override
	public Timestamp get_activity_time() 
	{
		return activity_time;
	}

	public int compareTo(abstract_activity other)
	{
		// activities are ordered by date only
		return (other.get_activity_time().compareTo(this.get_activity_time())); 
	}
}
