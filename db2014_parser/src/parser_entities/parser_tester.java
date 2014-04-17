package parser_entities;
import config.config; 

/* A tester Class For Parsing Utilities*/
public class parser_tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		parser_src_yago yago_parser = new parser_src_yago();
		yago_parser.parse();
		
		praser_src_imdb imdb_parser = new praser_src_imdb(yago_parser.get_yag_movie_map());
		imdb_parser.parse();
		
		
		for (entity_movie mv : yago_parser.get_yag_movie_map().values())
		{
			System.out.println(mv.toString());
		}
		
	}

}
