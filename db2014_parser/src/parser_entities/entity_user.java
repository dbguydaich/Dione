package parser_entities;

public class entity_user
{
	private int entity_user_id;
	private String entity_user_name;
	
	public entity_user(int user_id, String user_name)
	{
		this.entity_user_id = user_id;
		this.entity_user_name = user_name;
	}

	public String toString()
	{
		return this.entity_user_name;
	}

	public String get_user_name() 
	{
		return entity_user_name;
	}
	
	public int get_user_id() 
	{
		return entity_user_id;
	}
}

