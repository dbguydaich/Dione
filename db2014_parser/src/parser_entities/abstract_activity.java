package parser_entities;

import java.sql.Date;

public abstract class abstract_activity implements Iactivity{

	private String activity_type; 
	private String activity_user;
	private Date activity_time;
	
	@Override
	public String get_activity_type() {
		// TODO Auto-generated method stub
		return activity_type;
	}

	@Override
	public String get_activity_user() {
		// TODO Auto-generated method stub
		return activity_user;
	}
	
	@Override
	public Date get_activity_time() {
		// TODO Auto-generated method stub
		return activity_time;
	}

}
