package parser_entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.io.File;

import config.config;

public class parser_src_yago {

	private HashMap<String, parser_entity_movie> parser_map_movie; 
	private HashMap<String, parser_entity_person> parser_map_actor; 
	private HashMap<String, parser_entity_person> parser_map_director;

	/* constructor */
	public parser_src_yago()
	{
		/*Init Parser members*/
		parser_map_movie = new HashMap<String, parser_entity_movie>(); 
		parser_map_actor = new HashMap<String, parser_entity_person>(); 
		parser_map_director = new  HashMap<String, parser_entity_person>();
	}
	
	public void parse()
	{
		/* init configurations */
		config properties = new config();
		if (properties.get_yago_facts_path() != null && File(path).)
			parse_src_yago_facts(properties.get_yago_facts_path());
		
	}
	
	/*parse the following all "acted in" and "directed" 
	 * tags from yagofacts.tsv */
	private void parse_src_yago_facts(String yago_facts_path)
	{
		// assert file exists
		if (yago_facts_path == null || ! new File(yago_facts_path).exists())
			return;
		
		// open files for read
		FileReader fr = new FileReader(yago_facts_path);
		BufferedReader br = new BufferedReader(fr);
		String splitted_line[];
		
		try {
			while (splitted_line = get_line_parsing(br))
			{
				
			}
			br.close();
			return; 
		}
		catch(Exception ex){
			fr.close();
			br.close();
			System.out.println(ex.toString());
			
		}
		//make sure the path is not null or empty and that it's the correct file
		if (isFileCorrect(path,YAGO_FACTS_FILE)){			
			try{
				// create a buffered reader for the current file
				BufferedReader br = new BufferedReader(new FileReader(path));
				String[] strArr;	
				//read line and parse it by tabs	
				while((strArr = parseLine2Array(br)) != null){ 			
					//if it contains "actedIn" or "directed" add that fact
					if(strArr.length >= 3 && (strArr[1].contains(ACTED_IN) || strArr[1].contains(DIRECTED))){
						addFact(strArr);
					}
				}
				br.close();
				return;
			} 
			catch(Exception ex){
				System.out.println(ex.toString());
			}	
		}

	}
	
	/*parses a TSV line of format : <yago_id> <yago_name> <fact> <yago_name>, e.g.
	 * <id_1e7i0ut_1gi_50uey2>	<Paul_Redford>	<created>	<The_Portland_Trip> */
	private String[] get_line_parsing(BufferedReader br){		
		String line;
		String[] splitted_line = new String[4];
		
		try{
			if((line = br.readLine()) != null) 
			{ 
				splitted_line = line.split("\\t");				
				return splitted_line;
			}
			else
				return null;
		}
		catch (Exception ex){
			System.out.println("Could not read line");
		}
		return null;
	}

}

