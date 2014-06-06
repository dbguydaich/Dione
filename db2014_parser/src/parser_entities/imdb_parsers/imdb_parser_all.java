package parser_entities.imdb_parsers;

import java.util.HashMap;
import java.util.HashSet;

import parser_entities.entity_movie;

public class imdb_parser_all {

	/*entity maps*/
	private HashMap<String,entity_movie> parser_movie_map;	/* yago movie catalog to be enriched*/
	private HashSet<String> parser_language_set;			/* imdb entities - languge*/	 
	private HashSet<String> parser_genre_set;				/* imdb entities - genres*/
	private HashSet<String> parser_tag_set;					/* imdb entities - tags*/
	
	/*helper maps*/
	private HashMap<String,String> imdb_to_yago;			/* holds all possible imdb names, that are relevant to yago films*/
	private HashMap<String,Integer> parser_tag_count_map;	/* handles tag counts, to establish top 10 per movie*/
	private HashMap<String,String> imdb_name_to_director;	/* maps imdb movie name to imdb director*/ 
	
	public imdb_parser_all(HashMap<String,entity_movie> parser_movie_map)
	{
		this.parser_movie_map = parser_movie_map;
	}
	
	public HashSet<String> get_parser_languages()
	{
		return this.parser_language_set;
	}
	
	public HashSet<String> get_parser_genres()
	{
		return this.parser_genre_set;
	}

	public HashSet<String> get_parser_tags()
	{
		return this.parser_tag_set;
	}
	
	
	public void parse_all()
	{
		/*first, get directors, and set all yago names*/
		imdb_director_parser misc = new imdb_director_parser(parser_movie_map);
		misc.parse_imdb_file();
		misc.map_imdb_yago_names();
		imdb_name_to_director = misc.get_directors();
		imdb_to_yago = misc.get_imdb_to_yago();
		
		/*parse all relevant data*/
		
		/*parser genres*/
		imdb_genre_parser genres = new imdb_genre_parser(parser_movie_map,imdb_name_to_director,imdb_to_yago);
		genres.parse_imdb_file();
		
		/*create genre set*/
		parser_genre_set = genres.get_enrichment_set();
		
		/*update movies with plots*/
		imdb_plot_parser plots = new imdb_plot_parser(parser_movie_map,imdb_name_to_director,imdb_to_yago);
		plots.parse_imdb_file();
		
		/*update movies with langauges*/
		imdb_language_parser languages = new imdb_language_parser(parser_movie_map,imdb_name_to_director,imdb_to_yago);
		languages.parse_imdb_file();
		
		/*create genre set*/
		parser_language_set = languages.get_enrichment_set();
		
		/*get tags and their scores*/
		imdb_tag_parser tags = new imdb_tag_parser(parser_movie_map,imdb_name_to_director,imdb_to_yago);
		tags.parse_imdb_file();
		
		this.parser_tag_set = tags.get_enrichment_set();
		this.parser_tag_count_map = tags.get_parser_tag_counts();
		
		/*updates movie entites with tags*/
		imdb_tag_movie_parser tag_movie = 
				new imdb_tag_movie_parser(parser_movie_map,imdb_name_to_director,imdb_to_yago,parser_tag_count_map);
		tag_movie.parse_imdb_file();
		
		
		
	}
	
	
}
