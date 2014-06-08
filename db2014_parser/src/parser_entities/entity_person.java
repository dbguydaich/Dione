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
		entity_person_name = person_id;
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
	
	public void normalize_name()
	{
		this.entity_person_name = removeDiacritic(entity_person_name);
	}

	
	 /**
	 * Mirror of the unicode table from 00c0 to 017f without diacritics.
	 */
	private static final String tab00c0 = "AAAAAAACEEEEIIII" +
	    "DNOOOOO\u00d7\u00d8UUUUYI\u00df" +
	    "aaaaaaaceeeeiiii" +
	    "\u00f0nooooo\u00f7\u00f8uuuuy\u00fey" +
	    "AaAaAaCcCcCcCcDd" +
	    "DdEeEeEeEeEeGgGg" +
	    "GgGgHhHhIiIiIiIi" +
	    "IiJjJjKkkLlLlLlL" +
	    "lLlNnNnNnnNnOoOo" +
	    "OoOoRrRrRrSsSsSs" +
	    "SsTtTtTtUuUuUuUu" +
	    "UuUuWwYyYZzZzZzF";
	
	/**
	 * Returns string without diacritics - 7 bit approximation.
	 *
	 * @param source string to convert
	 * @return corresponding string without diacritics
	 */
	public static String removeDiacritic(String source) {
		if (source == null)
			return null;
		try{
	    char[] vysl = new char[source.length()];
	    char one;
	    for (int i = 0; i < source.length(); i++) {
	        one = source.charAt(i);
	        if (one >= '\u00c0' && one <= '\u017f') {
	            one = tab00c0.charAt((int) one - '\u00c0');
	        }
	        vysl[i] = one;
	    }
	    return new String(vysl);
		} catch (Exception ex){
			return source; 
		}
	}

}

