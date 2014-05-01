package parser_entities;

import java.sql.Date;

public abstract class abstract_activity implements Iactivity, Comparable<abstract_activity>
{
	private String activity_type; 
	private String activity_user;
	private Date activity_time;
	
	protected abstract_activity(String activity_type, String activity_user, Date activity_time)
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
	public Date get_activity_time() 
	{
		return activity_time;
	}

	public int compareTo(abstract_activity other)
	{
		// activities are ordered by date only
		return (this.get_activity_time().compareTo(other.get_activity_time())); 
	}
}
