package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
 
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
	public String get_yago_literalFacts(){
		return configFile.getProperty("YagoLiteralFactsFilePath");
	}
	
	/** get the path of the file yagoWikipediaInfo.ttl */
	public String getYagoWikipediaInfo(){
		return configFile.getProperty("YagoWikipediaInfoFilePath");
	}
	
	public String get_imdb_genres(){
		return configFile.getProperty("IMDBgenreListFilePath");
	}
	
	/** get the path of the file plot.list provided by IMDB */
	public String get_imdb_plots(){
		return configFile.getProperty("IMDBplotListFilePath");
	}
	
	/** get the path of the file l.list provided by IMDB */
	public String get_imdb_languages(){
		return configFile.getProperty("IMDBlanguageFilePat");
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
	
	/** get number of connections to create **/
	public  int get_window_height(){
		return Integer.parseInt(configFile.getProperty("WINDOW_HEIGHT"));
	}
	/** get number of connections to create **/
	public int get_window_width(){
		return Integer.parseInt(configFile.getProperty("WINDOW_WIDTH"));
	}

	public int get_default_activity_limit() {
		return Integer.parseInt(configFile.getProperty("DEFAULT_ACTIVITY_LIMIT"));
	}
	


 
}