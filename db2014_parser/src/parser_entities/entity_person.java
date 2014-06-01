package parser_entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class entity_person implements Serializable{
	
	private String entity_person_name;
	private String entity_person_id;
	private Set<String> entity_person_names;
	
	public entity_person(String person_id){
		this.entity_person_id = person_id;
		entity_person_names = new HashSet<String>();
	}

	public entity_person(String name, Integer id) 
	{
		entity_person_name = name;
		entity_person_id = (new Integer(id)).toString();
		entity_person_names = new HashSet<String>();
	}

	public String get_person_name() {
		return entity_person_name;
	}

	public void set_person_name(String person_name) {
		this.entity_person_name = person_name;
	}
	
	public String toString(){
		return this.entity_person_name;
		
	}

	public String get_person_id() {
		return entity_person_id;
	}
	
	public void set_person_id(String person_id) {
		this.entity_person_id = person_id;
	}
	
	public void add_to_person_names(String name)
	{
		if (name != null)
			this.entity_person_names.add(name);
	}
	
	public Set<String> get_person_names()
	{
		return this.entity_person_names;
	}

}

