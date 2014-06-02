package parser_entities.loaders;

import java.sql.SQLException;

import parser_entities.TBDs.parser_src_yago;
import parser_entities.TBDs.praser_src_imdb;
import parser_entities.imdb_parsers.imdb_parser_all;
import parser_entities.parsers.abstract_yago_parser;

public class loader {

	public void load(imdb_parser_all imdb_data, abstract_yago_parser yago_parser) throws SQLException{
		
		/*peripherial data*/
		language_loader languages = new language_loader();
		languages.load_batch(imdb_data.get_parser_languages());
		
		genre_loader genres = new genre_loader();
		genres.load_batch(imdb_data.get_parser_genres());
		
		tag_loader tags = new tag_loader();
		tags.load_batch(imdb_data.get_parser_tags());
		
		/*entities - persons, movies*/
		person_loader persons = new person_loader();
		persons.load_batch(yago_parser.get_yag_actor_map().values());
		persons.load_batch(yago_parser.get_yag_director_map().values());
		
		actor_loader actors = new actor_loader();
		actors.load_batch(yago_parser.get_yag_actor_map().values());
		
		director_loader directors = new director_loader();
		directors.load_batch(yago_parser.get_yag_director_map().values());
		
		movie_loader movies = new movie_loader();
		movies.load_batch(yago_parser.get_yag_movie_map().values());
		
		/*movie many to many*/
		actor_movie_loader actor_movie = new actor_movie_loader();
		actor_movie.load_batch(yago_parser.get_yag_movie_map().values());
		
		genre_movie_loader genre_movie = new genre_movie_loader();
		genre_movie.load_batch(yago_parser.get_yag_movie_map().values());
		
		tag_movie_loader tag_movie = new tag_movie_loader();
		tag_movie.load_batch(yago_parser.get_yag_movie_map().values());
		
	}

}
