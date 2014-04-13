package parser_entities;

import java.io.Serializable;

public class entity_person implements Serializable{
	
	private String entity_person_name;
	private String entity_person_id;
	
	public entity_person(String person_id){
		this.entity_person_id = person_id;
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
}

