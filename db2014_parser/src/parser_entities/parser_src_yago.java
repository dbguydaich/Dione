package parser_entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File;

import config.config;


public class parser_src_yago {

	/* constants */
	
	/*tag type offsets for each file type*/
	public static final int yago_id_offset = 0;
	public static final int yago_person_offset = 1;
	public static final int yago_fact_offset = 2;
	public static final int yago_movie_offset = 3;
	public static final int yago_type_offset = 0;
	public static final int yago_literal_movie_offset = 1;
	public static final int yago_literal_fact_offset = 2; 
	public static final int yago_literal_value_offset = 3;
	public static final int yago_wiki_movie_offset = 0; 
	public static final int yago_wiki_url_offset = 2;
	public static final int yago_wiki_tag_offset = 1;
	
	/*expected number of elements in line per file type*/
	public static final int yago_facts_params = 4; 
	public static final int yago_types_params = 3;
	public static final int yago_literal_params = 5;
	public static final int yago_wiki_params = 3;
	
	/*config*/
	config properties; 
	
	/* members */
	private HashMap<String, entity_movie> parser_map_movie; 
	private HashMap<String, entity_person> parser_map_actor; 
	private HashMap<String, entity_person> parser_map_director;

	/* constructor */
	public parser_src_yago()
	{
		/*Init Parser members*/
		parser_map_movie = new HashMap<String, entity_movie>(); 
		parser_map_actor = new HashMap<String, entity_person>(); 
		parser_map_director = new  HashMap<String, entity_person>();
		/*Init Parser members*/
		this.properties = new config();
	}
	
	/* getter for movie table*/
	public HashMap<String, entity_movie> get_yag_movie_map()
	{
		return this.parser_map_movie;
	}
	
	/**general parsing function**/
	public void parse()
	{
		System.out.println(properties.get_imdb_tags_path());
		/* parse all yago files */
		if (properties.get_yago_simple_types_path() != null)
			parse_src_yago_types(properties.get_yago_simple_types_path(),yago_types_params);
		
			 	if (properties.get_yago_facts_path() != null)
			parse_src_yago_facts(properties.get_yago_facts_path(),yago_facts_params);
		
		if (properties.get_yago_literal_facts_path() != null)
			parse_src_yago_literal_facts(properties.get_yago_literal_facts_path(),yago_literal_params);
		
		/*try{
		if (properties.get_yago_wikipedia_path() != null)
			parse_src_yago_wikipedia(properties.get_yago_wikipedia_path(),yago_wiki_params);
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}*/
	}
	
	/*parse*/
	
	/** parse yagoSimpleTypes for Director, Actor, Movie entity */
	private void parse_src_yago_types(String yago_types_path, int num_params)
	{
		// assert file exists
		File fl = new File(yago_types_path);
		if (yago_types_path == null || ! fl.exists() )
			return;
		
		// open files for read
		try {
			FileReader fr = new FileReader(yago_types_path);
			BufferedReader br = new BufferedReader(fr);
			try {
				String splitted_line[];
				/* read line */
				while ((splitted_line = get_line_parsing(br,num_params)) != null)
				{				

					if (splitted_line[0] == null) //bad line
						continue;
					
					if(splitted_line[yago_fact_offset].contains(properties.get_yago_tag_movie()))
						add_to_parser_movies(clean_format(splitted_line[yago_type_offset]));
					
					if(splitted_line[yago_fact_offset].contains(properties.get_yago_tag_actor()))
						add_to_parser_actors(clean_format(splitted_line[yago_type_offset]));
					
					if(splitted_line[yago_fact_offset].contains(properties.get_yago_tag_director()))
						add_to_parser_directors(clean_format(splitted_line[yago_type_offset]));
				}
				fr.close();
				br.close();
				return;
			} 
			catch(Exception ex)
			{
				fr.close();
				br.close();
			}
	} catch(Exception ex) {}
}
	
	/*clean up tag*/
	public String clean_format(String str)
	{
		str = str.replace("_", " ");
		if (str.startsWith("<") && str.endsWith(">"))
			str = str.substring(1, str.length() - 1);
		return str;
	}
	
	/** creates a new movie in map **/
	void add_to_parser_movies (String movie_name)
	{
		String clean_name = clean_format(movie_name);
		entity_movie movie = new entity_movie(clean_name);
		movie.set_movie_name(clean_name);
		this.parser_map_movie.put(movie_name, movie);
	}
	
	/** creates a new actor in map **/
	void add_to_parser_actors (String person_name)
	{
		entity_person person = new entity_person(person_name);
		person.set_person_name(person_name);
		this.parser_map_actor.put(person_name, person);
	}
	
	/** creates a new director in map **/
	void add_to_parser_directors (String person_name)
	{
		entity_person person = new entity_person(person_name);
		person.set_person_name(person_name);
		this.parser_map_director.put(person_name, person);
	}
	
	
	/**parse the following all "acted in" and "directed" 
	 * tags from yagofacts.tsv **/
	private void parse_src_yago_facts(String yago_facts_path, int num_params)
	{
		// assert file exists
		File fl = new File(yago_facts_path);
		if (yago_facts_path == null || ! fl.exists() )
			return;
		
		// open files for read
		try {
			FileReader fr = new FileReader(yago_facts_path);
			BufferedReader br = new BufferedReader(fr);
			try {
				
				String splitted_line[];
				/* read line */
				while ((splitted_line = get_line_parsing(br,num_params)) != null)
				{
					if (splitted_line[0] == null) //bad line
						continue;
					/*check the fact is relevant*/
					if (splitted_line[yago_fact_offset].contains(properties.get_yago_tag_actedin())
						|| splitted_line[yago_fact_offset].contains(properties.get_yago_tag_directed()))
					{
						update_movies_by_fact(splitted_line);
					}
				}
				br.close();
				return; 
			}
			catch(Exception ex){
				fr.close();
				br.close();
				System.out.println(ex.toString());
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}
	
	/*parses a TSV line of format : <yago_id> <yago_name> <fact> <yago_name>, e.g.
	 * <id_1e7i0ut_1gi_50uey2>	<Paul_Redford>	<created>	<The_Portland_Trip> */
	private String[] get_line_parsing(BufferedReader br, int num_params)
	{		
		String line;
		int i;
		try{
			if((line = br.readLine()) != null) 
			{ 
				/*split next line*/
				line = line.trim();
				String[] splitted_line = line.split("\\t");
				/*check for expected number of parameters*/
				if (splitted_line.length != num_params)
					return new String[num_params];
				for (i=0; i< num_params; i++)
					if (splitted_line[i] != null) 
					{
						if (splitted_line[i].startsWith("<") && splitted_line[i].endsWith(">"))
							splitted_line[i] = splitted_line[i].substring(1, splitted_line[i].length() - 1);
					}
					else 
						return new String[num_params];
					
				return splitted_line;
			}
			else
				return null;
		}
		catch (Exception ex){
			System.out.println("error on parsing line:" + ex.getMessage());
		}
		return null;
	}

		
	private void parse_src_yago_literal_facts(String yago_literal_facts_path, int num_params)
	{
		// assert file exists
		File fl = new File(yago_literal_facts_path);
		if (yago_literal_facts_path == null || ! fl.exists() )
			return;
		
		// open files for read
		try {
			FileReader fr = new FileReader(yago_literal_facts_path);
			BufferedReader br = new BufferedReader(fr);
			try {
				
				String splitted_line[];
				/* read line */
				while ((splitted_line = get_line_parsing(br,num_params)) != null)
				{
					if (splitted_line[0] == null) //bad line
						continue;
					/*check the fact is relevant*/
					if (splitted_line[yago_fact_offset].contains(properties.get_yago_tag_year())
						|| splitted_line[yago_fact_offset].contains(properties.get_yago_tag_length()))
					{
						update_movies_by_literal(splitted_line);
					}
				}
				br.close();
				return; 
			}
			catch(Exception ex){
				fr.close();
				br.close();
				System.out.println(ex.toString());
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}	
		
	private void parse_src_yago_wikipedia(String yago_literal_facts_path, int num_params)
		{
			// assert file exists
			File fl = new File(yago_literal_facts_path);
			if (yago_literal_facts_path == null || ! fl.exists() )
				return;
			
			// open files for read
			try {
				FileReader fr = new FileReader(yago_literal_facts_path);
				BufferedReader br = new BufferedReader(fr);
				try {
					
					String splitted_line[];
					HashSet<String> tags = new HashSet<String>();  
					/* read line */
					while ((splitted_line = get_line_parsing(br,num_params)) != null)
					{
						if (splitted_line[0] == null) //bad line
							continue;
						/*check the fact is relevant*/
						if (!tags.contains(splitted_line[yago_wiki_tag_offset]))
						{
							tags.add(splitted_line[yago_wiki_tag_offset]);
							System.out.println(splitted_line[yago_wiki_tag_offset]);
						}
						if (splitted_line[yago_wiki_tag_offset].contains(properties.get_yago_tag_wikipedia()))
						{
							update_movies_by_wikipedia(splitted_line);
						}
					}
					br.close();
					return; 
				}
				catch(Exception ex){
					fr.close();
					br.close();
					System.out.println(ex.toString());
				}
			}
			catch(Exception ex)
			{
				System.out.println(ex.toString());
			}
	
		}
	
	/** add the fact: <yago_id> <entity_person> <fact> <entity_movie>**/
	private void update_movies_by_fact(String[] splitted_line)
	{		
		String movie_name, fact_name, person_name, yago_id;
		
		yago_id = splitted_line[yago_id_offset];
		if (yago_id == null)
			return; 
		
		fact_name = splitted_line[yago_fact_offset];
		if (fact_name == null)
			return;
		
		person_name = clean_format(splitted_line[yago_person_offset]);
		if (person_name == null)
			return;
		
		movie_name = clean_format(splitted_line[yago_movie_offset]);
		if (movie_name == null)
			return;
		
		entity_movie movie = this.parser_map_movie.get(movie_name);
		if (movie == null)
			return; 
		
		/*add actor to movie*/
		if (fact_name.equals(properties.get_yago_tag_actedin()))
		{
			entity_person actor = this.parser_map_actor.get(person_name);
			movie.add_to_actors(actor);
		}
		/*set director of movie*/
		else if (fact_name.equals(properties.get_yago_tag_directed()))
		{
			entity_person director = this.parser_map_director.get(person_name);
			movie.set_movie_director(director);
		}
	}
		
	/** enrich movie with fact literals: year, duration**/
	private void update_movies_by_literal(String[] splitted_line)
	{		
		String movie_name, fact_name, fact_value;
		
		fact_name = splitted_line[yago_literal_fact_offset];
		if (fact_name == null)
			return;
		
		fact_value = splitted_line[yago_literal_value_offset];
		if (fact_name == null)
			return;
		
		movie_name = clean_format(splitted_line[yago_literal_movie_offset]);
		if (movie_name == null)
			return;
		
		entity_movie movie = this.parser_map_movie.get(movie_name);
		if (movie == null)
			return; 
		
		/*add actor to movie*/
		if (fact_name.equals(properties.get_yago_tag_length()))
		{
			movie.set_movie_length(parse_movie_length(fact_value));
		}
		/*set director of movie*/
		else if (fact_name.equals(properties.get_yago_tag_year()))
		{
			movie.set_movie_year(parse_movie_year(fact_value));
		}
	}
	
	/** pull the year from the date format of yago */
	private String parse_movie_year(String literal_fact_date){
		if (literal_fact_date == null || literal_fact_date.length() < 5)
			return null;
		String year = literal_fact_date.substring(1,5);
		return year;
	}
	
	/** pull the time in minutes from the duration format */
	private String parse_movie_length(String literal_fact_length)
	{	
		try {
			String length = literal_fact_length.substring(1, literal_fact_length.indexOf("."));
			int length_secs = Integer.parseInt(length);
			length_secs = length_secs/60;
			Integer length_minutes = new Integer(length_secs);
			return length_minutes.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	/** add the wikiURL to the movie */
	private void update_movies_by_wikipedia(String[] splitted_line) 
	{
		String movie_name, fact_name;
		
		fact_name = splitted_line[yago_wiki_url_offset];
		if (fact_name == null)
			return;
		fact_name = fact_name.substring(1, fact_name.length() -1);
		
		movie_name = splitted_line[yago_wiki_movie_offset];
		if (fact_name == null)
			return;
		
		entity_movie movie = this.parser_map_movie.get(movie_name);
		if (movie == null)
			return; 
		movie.set_wikipedia_url(fact_name);

	}

	
}


