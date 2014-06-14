package parser_entities.activities;

import java.sql.Timestamp;

public interface Iactivity {

	public String get_activity_type();
	public String get_activity_user();
	public Timestamp get_activity_time();
	
}
