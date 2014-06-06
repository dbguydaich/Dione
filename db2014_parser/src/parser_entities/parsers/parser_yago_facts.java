package parser_entities.parsers;

import java.util.HashMap;

import parser_entities.Importer;
import parser_entities.entity_movie;
import parser_entities.entity_person;

/**
 * parses facts between two entites: playrs in movies, directors, etc.
 * 
 * @author GUY
 * 
 */
public class parser_yago_facts extends abstract_yago_parser {

	public static final int yago_id_offset = 0;
	public static final int yago_person_offset = 1;
	public static final int yago_fact_offset = 2;
	public static final int yago_movie_offset = 3;
	public static final int yago_facts_params = 4;

	private int c_actor_movie = 0;
	private int c_director_movie = 0;

	public parser_yago_facts(HashMap<String, entity_movie> movie_map,
			HashMap<String, entity_person> actor_map,
			HashMap<String, entity_person> director_map, Importer Caller) {
		super(movie_map, actor_map, director_map, Caller);
		this.yago_file_path = properties.get_yago_facts_path();
		this.yago_file_params = yago_facts_params;

	}

	@Override
	public void handle_line_parsing(String[] splitted_line) {
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

		/* add actor to movie */
		if (fact_name.equals(properties.get_yago_tag_actedin())) {
			entity_person actor = this.parser_map_actor.get(person_name);
			movie.add_to_actors(actor);
			c_actor_movie++;
		}
		/* set director\creator of movie */
		else {
			if (fact_name.equals(properties.get_yago_tag_directed())) {
				if (movie.get_movie_director() == null)
					c_director_movie++;
				entity_person director = this.parser_map_director
						.get(person_name);
				if (director == null) /* this one doesn't exist */
					parser_map_director.put(person_name, new entity_person(
							person_name));
				movie.set_movie_director(director);

			} else /* use creator only if director missing */
			{
				if (movie.get_movie_director() == null) {
					entity_person director = this.parser_map_director
							.get(person_name);
					movie.set_movie_director(director);
					c_director_movie++;
				}
			}
		}

	}

	@Override
	/**
	 * accept only "directed","created","acted in" lines
	 */
	public boolean is_line_parseble(String[] splitted_line) {
		if (splitted_line[yago_fact_offset].contains(properties
				.get_yago_tag_actedin())
				|| splitted_line[yago_fact_offset].contains(properties
						.get_yago_tag_directed())
				|| splitted_line[yago_fact_offset].contains(properties
						.get_yago_tag_created()))
			return true;
		return false;
	}

	@Override
	/**
	 * print how many literal facts were created
	 */
	public void print_parse_stats() {
		StringBuilder sb = new StringBuilder();
		sb.append("literal facts parser:\n\t file: ");
		sb.append(this.yago_file_path);
		sb.append("\nenrichments:\n\tdirectors of movies:");
		sb.append(this.c_director_movie);
		sb.append("\n\tactors in movies:");
		sb.append(this.c_actor_movie);
		System.out.println(sb.toString());
	}
}
