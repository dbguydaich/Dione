package parser_entities;

import java.io.Serializable;

public class entity_person implements Serializable{
	
	private String entity_person_name;
	private int entity_person_id;
	
	public entity_person(int person_id)
	{
		this.entity_person_id = person_id;
	}

	public entity_person(String name, Integer id) 
	{
		entity_person_name = name;
		entity_person_id = id;
	}

	public String get_person_name() {
		return entity_person_name;
	}
	
	public int get_person_id() {
		return entity_person_id;
	}
	
	public String toString(){
		return this.entity_person_name;
		
	}
}

