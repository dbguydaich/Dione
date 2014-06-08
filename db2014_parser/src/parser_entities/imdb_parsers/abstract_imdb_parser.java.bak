package parser_entities.imdb_parsers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import config.config;


import java.sql.BatchUpdateException;

import java.sql.SQLException;
import java.sql.Statement;

import parser_entities.Importer;
import parser_entities.entity_movie;

import db.db_operations;
import db.db_queries_movies;
import db.db_queries_persons;
import db.jdbc_connection_pooling;


/** parser_src_imdb is initialized with an existing 
 * movie catalog and enriches it with IMDB data.  
 * It populates tag entities, and their relations to movies**/


public abstract class abstract_imdb_parser {
	
	/*parser specific parameters*/
	protected String 	list_start;			/*imdb file list beggining string*/
	protected String 	list_end;			/*imdb file list end string*/
	protected String 	filepath;			/*imdb file to parse */
	protected String 	imdb_object; 		/*object imdb */
	
	/*counters*/
	protected int 		enrich_count = 0;	/*imdb list beggining string*/
	protected int 		reject_count = 0;	/**/
	
	/*entity maps*/
	protected HashMap<String,entity_movie> parser_movie_map;	/* yago movie catalog to be enriched*/
	
	/*helper maps*/
	protected HashMap<String,String> imdb_to_yago;			/*holds all possible imdb names, that are relevant to yago films*/
	protected HashMap<String,String> imdb_name_to_director;	/* maps imdb movie name to imdb director*/ 
	protected HashMap<String, HashSet<String>> imdb_names;		/* maps imdb movie name to its name in other languages*/
	
	protected Importer Caller; 
	
	protected List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

	protected void notifyListeners(Object object, String property,
			String oldValue, String newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, property,
					oldValue, newValue));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}
	
	public void removeChangeListener(PropertyChangeListener newListener) {
		listener.remove(newListener);
	}

	public int get_file_line_count() {

		try {

			File file = new File(this.filepath);

			if (file.exists()) {
				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);
				int linenumber = 0;

				while (lnr.readLine() != null) {
					linenumber++;
				}
				lnr.close();
				return linenumber;
			} else {
				System.out.println("File does not exists!");
			}

		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}


	
	protected config properties = new config();
	
	/*genreal constructor, for initial build of misc data*/
	public abstract_imdb_parser(HashMap<String,entity_movie> movie_map, Importer caller)
	{
		/*init movie catalog*/
		this.parser_movie_map = movie_map; 
		this.imdb_to_yago = new HashMap<String,String>();
		this.imdb_name_to_director = new HashMap<String,String>();
		this.Caller = caller;
	}
	
	/**
	 * overloaded constructor - we have directors, and name maps
	 * @param movie_map
	 * @param director_map
	 * @param imdb_movie_names 
	 * @param imdb_to_yago
	 */
	public abstract_imdb_parser(HashMap<String,entity_movie> movie_map, HashMap<String,String> director_map, 
			HashMap<String, HashSet<String>> imdb_movie_names, HashMap<String,String> imdb_to_yago, Importer caller)
	{
		/*init movie catalog*/
		this.parser_movie_map = movie_map; 
		this.imdb_to_yago = imdb_to_yago;
		this.imdb_names = imdb_movie_names;
		this.imdb_name_to_director = director_map;
		this.Caller = caller; 
	}
	
	
	
	/**
	 * populates the YAGO possible name-resolutions to a dictionary, 
	 * we later search here, to find a YAGO film to enrich from IMDB
	 * this maps qualified_yago_name -> entity_movie_id, and there are several
	 * such names, depending on the yago label informations
	 */
	public void map_imdb_yago_names()
	{
		/* maps upper-case movie names, to their real fq-names */
		for (entity_movie movie : parser_movie_map.values())
		{
			/*set translation in hash*/
			imdb_to_yago.put(movie.get_movie_qualified_name().toUpperCase(),movie.get_movie_qualified_name());
			/*add also foreign names*/
			for (String label_fq_name : movie.get_label_fq_names())
					imdb_to_yago.put(label_fq_name.toUpperCase(),movie.get_movie_qualified_name());
		}
	}
	
	public HashMap<String,String> get_imdb_to_yago()
	{
		return this.imdb_to_yago;
	}

	/*return a set of enrichment items - tags, languages, etc.*/
	public abstract Set<String> get_enrichment_set();
	
	/**
	 * splits line in some manner, tries to extract relevant data
	 * and update appropriate datastructures accordingly
	 * @param line - line to handle
	 * @return
	 */
	protected abstract int handle_single_line(String line, BufferedReader br);

	/**
	 * Parses a class-specific file - reading line by line, sending it to 
	 * some specific parsing function, and recording rejections and enrcihments
	 * we loop until our caller has terminated 
	 */
	public void parse_imdb_file()
	{
		int progress = 0;
		// assert file exists
		File fl = new File(this.filepath);
		if (this.filepath == null || ! fl.exists() )
			return;

		try {
			FileReader fr = new FileReader(this.filepath);
			BufferedReader br = new BufferedReader(fr);
			String line; 
			try {
				/*read until start*/
				while ((line = br.readLine()) != null && !Caller.is_thread_terminated())
				{
					progress++;
					if (line.contains(this.list_start))
						break;
				}
				/*parse until EOF*/
				while ((line = br.readLine()) != null && !Caller.is_thread_terminated())
				{
					progress++;
					if (progress % 10000 == 0)
						this.notifyListeners(this, "progress", "0", (new Integer(progress)).toString());
					/*list end reached - terminate*/
					if (this.list_end != null && line.contains(this.list_end))
						break;
					int ret = handle_single_line(line, br); 
					
					if (ret>=1)
						enrich_count+=ret;
					else if (ret==0) 
						reject_count++;
				}
			}
			catch (Exception ex){}
		}
		catch (Exception ex){}
		System.out.println("added " + this.imdb_object + " to :" + enrich_count);
	}
	
	/**
	 * remove bad characters from movie names
	 * @param imdb_movie_name
	 * @return
	 */
	protected String clean_imdb_name (String imdb_movie_name)
	{
		imdb_movie_name =imdb_movie_name.replaceAll("\"", "");		
		imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
		imdb_movie_name = imdb_movie_name.replaceAll("$#*! ", "");
		imdb_movie_name = imdb_movie_name.substring(0,imdb_movie_name.indexOf(")")+1);
		return imdb_movie_name;
	}
	
	/**
	 * returns an array of strings, that consists of all Fully Qualified YAGO names
	 * That we consider to match this IMDB title: We allow the Yago name to have missing data
	 * so we add artificial NANs to title, where it is needed. We Use all imdb names of the title:
	 * including foreign names' International name.
	 * @param imdb_movie_name
	 * @return
	 */
	protected ArrayList<String> get_movie_keys(String imdb_movie_name)
	{
		ArrayList<String> keys = new ArrayList<String>();
		
		/*go over all names*/
		this.imdb_names.get(imdb_movie_name).add(imdb_movie_name);
		
		for (String imdb_other_name : this.imdb_names.get(imdb_movie_name))
		{
			String imdb_year = null;
			if (imdb_other_name == null || imdb_other_name.equals(""))
				continue; 
			
			String imdb_director = null;
			String imdb_name = clean_imdb_name(imdb_other_name);
			if (imdb_name.indexOf("(") > 0)
			{
				imdb_year = imdb_name.substring(imdb_name.indexOf("(") +1 , imdb_name.indexOf(")"));
				imdb_name = imdb_name.substring(0, imdb_name.indexOf("("));
			}
			imdb_name = imdb_name.trim();
			
			if (imdb_name_to_director.get(imdb_movie_name) == null)
				imdb_director = "NAN";
			else
				imdb_director = imdb_name_to_director.get(imdb_movie_name);
			if (imdb_year == null || imdb_year.equals(""))
				imdb_director = "NAN";
			
			/*we create all possible matches for this movie*/
			keys.add((imdb_name + " (" + imdb_year + ") (" + imdb_director +")").toUpperCase());
			keys.add((imdb_name + " (NAN) (" + imdb_director +")").toUpperCase());
			keys.add((imdb_name + " (" + imdb_year + ") (NAN)").toUpperCase());
			keys.add((imdb_name + " (NAN) (NAN)").toUpperCase());
		}
	
		return keys;
		
	}
	

	
	/**
	 * imdb_name -> FQ_imdb_names[]; 
	 * if exists i: FQ_yago_names(FQ_imdb_names[i]) != null
	 * then FQ_yago_names(FQ_imdb_names[i]) == yago_movie_id
	 * so, we can enrich movie_map(yago_movie_id), with some imdb data
	 * we work with hashmaps, to speed up searches, and we Hash names, 
	 * insted of objects, to avoid costly object hash() and equals() 
	 * @param imdb_movie_name - the imdb name we found during the parsing of some file
	 * @return the yago movie that represents this imdb movie name, null if n
	 */
	protected entity_movie get_movie_by_imdb_name(String imdb_movie_name)
	{
		ArrayList<String> movie_keys  = get_movie_keys(imdb_movie_name);
		String yago_name = null; 
		for (String key : movie_keys)
		{
			yago_name = imdb_to_yago.get(key);
			if (yago_name != null)
				break;
		}
		if (yago_name == null)
			return null;
		entity_movie movie = this.parser_movie_map.get(yago_name);
		return movie; 
	}
}

	