package parser_entities.parsers;

import java.util.HashMap;

import parser_entities.entity_movie;
import parser_entities.entity_person;

public class parser_yago_all{

	private HashMap<String, entity_movie> parser_map_movie = new HashMap<String, entity_movie>(); 
	private HashMap<String, entity_person> parser_map_actor =new  HashMap<String, entity_person>(); 
	private HashMap<String, entity_person> parser_map_director = new HashMap<String, entity_person>();
	
	public abstract_yago_parser parse_all()
	{
		parser_yago_types types = new parser_yago_types(parser_map_movie,parser_map_actor,parser_map_director); 
		types.parse_file();
		
		parser_yago_facts facts = new parser_yago_facts(parser_map_movie,parser_map_actor,parser_map_director); 
		facts.parse_file();
		
		parser_yago_literal_facts literal_facts = new parser_yago_literal_facts(parser_map_movie,parser_map_actor,parser_map_director);
		literal_facts.parse_file();
		
		//parser_yago_wikipedia wikipedia = new parser_yago_wikipedia(parser_map_movie,parser_map_actor,parser_map_director);
		//wikipedia.parse_file();
		
		parser_yago_labels labels = new parser_yago_labels(parser_map_movie,parser_map_actor,parser_map_director);
		labels.parse_file();
		
		labels.update_fq_name();
		
		return labels;
	}

}
