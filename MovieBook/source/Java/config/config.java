package config;

import java.io.IOException;
import java.util.Properties;
 
/* cofiguration handler class*/ 
public class config {
 
	private Properties configFile = new Properties();
	
	public config() 
	{
		try {
			configFile.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** get the path of the file yagoSimpleTypes.ttl */
	public String get_yago_simple_types_path(){		
		return configFile.getProperty("YagoSimpleTypesFilePath");
	}

	/** get the path of the file yagoFacts.ttl */
	public String get_yago_facts_path(){
		return configFile.getProperty("YagoFactsFilePath");
	}

	/** get the path of the file yagoLiteralFacts.ttl */
	public String get_yago_literal_facts_path(){
		return configFile.getProperty("YagoLiteralFactsFilePath");
	}
	
	/** get the path of the file yagoWikipediaInfo.ttl */
	public String get_yago_wikipedia_path(){
		return configFile.getProperty("YagoWikipediaInfoFilePath");
	}
	
	public String get_yago_labels_path(){
		return configFile.getProperty("YagoMultilingualInfoFilePath");
	}
	
	public String get_imdb_genres_path(){
		return configFile.getProperty("IMDBgenreListFilePath");
	}
	
	/** get the path of the file plot.list provided by IMDB */
	public String get_imdb_plots_path(){
		return configFile.getProperty("IMDBplotListFilePath");
	}
	
	/** get the path of the file plot.list provided by IMDB */
	public String get_imdb_names_path(){
		return configFile.getProperty("IMDBnamesListFilePath");
	}
	
	
	/** get the path of the file l.list provided by IMDB */
	public String get_imdb_languages_path(){
		return configFile.getProperty("IMDBlanguageFilePat");
	}
	
	/** get the path of the file l.list provided by IMDB */
	public String get_imdb_tags_path(){
		return configFile.getProperty("IMDB_KEYWORD_FILE_PATH");
	}
	
	public String get_imdb_taglines_path() {
		return configFile.getProperty("IMDBtaglineFilePath");
	}
	
	/** get the server address */
	public String get_host_address(){
		return configFile.getProperty("Host");
	}
	
	/** get the port of the server */
	public String get_port(){
		return configFile.getProperty("Port");
	}
	
	/** get the dbName(Schema name) */
	public String get_db_name(){
		return configFile.getProperty("DbName");
	}
	
	/** get the user name */
	public String get_user_name(){
		return configFile.getProperty("UserName");
	}
	
	/** get user password */
	public String get_password(){
		return configFile.getProperty("Password");
	}
	
	/** get number of connections to create **/
	public int get_number_connection(){
		return Integer.parseInt(configFile.getProperty("NumOfConnections"));
	}
	
	/*parsing*/
	
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_actedin(){
		return configFile.getProperty("ACTED_IN");
	}
	
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_label(){
		return configFile.getProperty("LABEL");
	}
	
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_directed(){
		return configFile.getProperty("DIRECTED");
	}
	
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_created(){
		return configFile.getProperty("CREATED");
	}
 
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_movie(){
		return configFile.getProperty("YAGO_TYPES_MOVIE");
	}
	
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_actor(){
		return configFile.getProperty("YAGO_TYPES_ACTOR");
	}

	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_director(){
		return configFile.getProperty("YAGO_TYPES_DIRECTOR");
	}
	
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_year(){
		return configFile.getProperty("YAGO_LITERAL_YEAR");
	}

	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_length(){
		return configFile.getProperty("YAGO_LITERAL_LENGTH");
	}
	
	/** get the yago tag name for "acted in" fact **/
	public String get_yago_tag_wikipedia(){
		return configFile.getProperty("YAGO_WIKI_TAG");
	}
	
	public String get_imdb_directors_path(){
		return configFile.getProperty("IMDB_DIRECTOR_FILE_PATH");
	}
	/** get number of connections to create **/
	public  int get_window_height(){
		return Integer.parseInt(configFile.getProperty("WINDOW_HEIGHT"));
	}
	/** get number of connections to create **/
	public int get_window_width(){
		return Integer.parseInt(configFile.getProperty("WINDOW_WIDTH"));
	}

	public int get_default_small_limit() {
		return Integer.parseInt(configFile.getProperty("DEFAULT_SMALL_LIMIT"));
	}
	
	public int get_default_large_limit() {
		String x= configFile.getProperty("DEFAULT_BIG_LIMIT");
		return Integer.parseInt(x);
	}

	public int get_admin_userid() {
		return Integer.parseInt(configFile.getProperty("ADMIN_USERNAME"));
	}
	
	public int get_default_director_id() {
		return Integer.parseInt(configFile.getProperty("DEFAULT_DIRECTOR_ID"));
	}
}