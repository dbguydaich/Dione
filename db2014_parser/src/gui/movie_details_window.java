package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import config.config;


////////////////////// functions the tab is waiting for: ///////////////////////

//String get_result_movie_name()
//List<String> get_result_movie_genres()
//String get_result_movie_director()
//String get_result_movie_language()
//String get_result_movie_year()
//String get_result_movie_duration()
//List<String> get_result_movie_stars()
//String get_result_movie_rating()
//String get_result_movie_wiki()
//List<String> get_result_movie_tags()


////////Listeners to be implemented: //////////////

/// tags_button - should allow the user to rate the movie tags. 
/// rate info is in radios_lists. kinda complicated (needed to be generic...)
/// List<List<Button>> radios_lists - list number 3 (inner lists size - always 5) is for tags.get(3), etc
/// talk to me for more expl.



public class movie_details_window extends Shell
{
	config config = new config();
	int window_height = config.get_window_height();
	int window_width = config.get_window_width();
	
	public movie_details_window(final Display display)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		
		Color color = display.getSystemColor(SWT.COLOR_GRAY);
		this.setBackground(color);
		
		Font font_ariel_11 = new Font(display, "Ariel",10, java.awt.Font.PLAIN ); //free resource ?
		
		this.setSize(window_width, window_height);
		this.setText("Movie Details");
		
		this.setLayout(new FormLayout());
		
		List<Label> movie_details_labels = new ArrayList<Label>();
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);	
		String movie_name = "Best movie everrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr11r"; //just for check
		//String movie name = get_result_movie_name();  to be used
		headline_label.setText(movie_name);
		FormData form_data_headline_label = new FormData();
		form_data_headline_label.top = new FormAttachment(0,4);
		form_data_headline_label.left = new FormAttachment(0,50);
		form_data_headline_label.width = 530;
		headline_label.setAlignment(SWT.CENTER);
		headline_label.setFont(new Font(display, "Ariel",15, java.awt.Font.PLAIN ));
		headline_label.setLayoutData(form_data_headline_label);	

		
		//left area
		Composite left_area = new Composite(this, SWT.NONE);
		FormData form_data = new FormData(350, 190); 
		form_data.top = new FormAttachment(0, 60);
		form_data.left = new FormAttachment(0, 10);
		left_area.setLayoutData(form_data);
		GridLayout grid_layout_area = new GridLayout(2, false);
		left_area.setLayout(grid_layout_area);
		
		
		//all left labels
		for(int i = 0; i < 12; i ++)
		{
				 movie_details_labels.add(new Label(left_area, SWT.NONE));
				 if (i > 1)
				 {
					GridData my_grid_data = new GridData();
					my_grid_data.verticalIndent = 5;
					movie_details_labels.get(i).setLayoutData(my_grid_data);
					 
				 }
				 movie_details_labels.get(i).setFont(font_ariel_11);

		}
		
		
		
		//genres1
		movie_details_labels.get(0).setText("Genres:");	
					
		//genres2	
		List<String> genres;
		genres = new ArrayList<String>(); //just for check
		//genres = get_result_movie_genres();  to be used
		genres.add("Comedy"); //
		genres.add("Action"); //
		genres.add("Best Genre Ever"); //
		
	
		String genres_str = "";
		for(String str: genres) //maybe limit
			genres_str = genres_str + str + ", " ;
		
		if(genres_str.length() >= 2)
			genres_str = genres_str.substring(0, genres_str.length() - 2);
			
		movie_details_labels.get(1).setText(genres_str);
		
		
		
		//director1
		movie_details_labels.get(2).setText("Director:");	
			
		//director2	
		String director_name = "Best Director Ever"; //just for check
		//String director_name = get_result_movie_director();  to be used
		
		movie_details_labels.get(3).setText(director_name);

		
		
		//language1
		movie_details_labels.get(4).setText("Language:");	
			
		//language2	
		String language = "Best Language Ever"; //just for check
		//String language = get_result_movie_language();  to be used
		
		movie_details_labels.get(5).setText(language);
		
		
		
		
		//year
		movie_details_labels.get(6).setText("Year:");	
			
		//year
		String year = "Best Year Ever"; //just for check
		//String director_name = get_result_movie_year();  to be used
		
		movie_details_labels.get(7).setText(year);
		
		
		
		
		//duration
		movie_details_labels.get(8).setText("Duration:");	
			
		//duration
		String duration = "Best Duration Ever"; //just for check
		//String director_name = get_result_movie_duration();  to be used
		
		movie_details_labels.get(9).setText(duration);
		
		
		//stars1
		movie_details_labels.get(10).setText("Stars:");	
							
		//stars2	
		List<String> stars;
		stars = new ArrayList<String>(); //just for check
		//stars = get_result_movie_stars();  to be used
		stars.add("Star1"); //
		stars.add("Star2"); //
		stars.add("Star3"); //
				
			
		String stars_str = "";
		for(String str: stars)
			stars_str = stars_str + str + ", " ;
		
		if(stars_str.length() >= 2)
			stars_str = stars_str.substring(0, stars_str.length() - 2);
					
		movie_details_labels.get(11).setText(stars_str);
		
		
		
		//rating1
		movie_details_labels.get(8).setText("Rating(1-5):");	
		//rating2
		String rating_str = "3"; //just for check
		//String rating_str = get_result_movie_rating();  to be used	
		movie_details_labels.get(9).setText(rating_str);
		
		
		//wiki label
		Label wiki_label = new Label(left_area, SWT.H_SCROLL);
		wiki_label.setText("Wiki Link:");
		GridData grid_data_wiki_label = new GridData();
		grid_data_wiki_label.verticalIndent = 5;
		wiki_label.setLayoutData(grid_data_wiki_label);
		wiki_label.setFont(font_ariel_11);
		
		
		//wiki link
		Link wiki_link = new Link(left_area, SWT.NONE);
		String wiki_str;
		//wiki_str = get_result_movie_wiki(); to be used
		wiki_str = "<a>Best Link Ever</a>";
		wiki_link.setText(wiki_str);
		GridData grid_data_wiki_link = new GridData();
		grid_data_wiki_link.verticalIndent = 5;
		wiki_link.setLayoutData(grid_data_wiki_link);
		wiki_link.setFont(font_ariel_11);

		
		
		//tags area
		Composite tags_area = new Composite(this, SWT.NONE);
		FormData form_data_tags_area = new FormData(240, 250); 
		form_data_tags_area.top = new FormAttachment(0, 60);
		form_data_tags_area.left = new FormAttachment(0, 380);
		tags_area.setLayoutData(form_data_tags_area);
		GridLayout grid_layout_area2 = new GridLayout(2, false);
		tags_area.setLayout(grid_layout_area2);

		
		
		//headline tags
		Label headline_tags = new Label(tags_area, SWT.NONE);
		headline_tags.setText("Tags");
		GridData grid_data_headline_tags = new GridData();
		grid_data_headline_tags.horizontalSpan = 2;
		grid_data_headline_tags.horizontalIndent = 95;
		headline_tags.setLayoutData(grid_data_headline_tags);
		Font font_tags = new Font(display, "Ariel",12, java.awt.Font.PLAIN );
		headline_tags.setFont(font_tags);
		
		
		
		//tags labels and radios
		List<Label> tags_labels = new ArrayList<Label>();
		List<List<Button>> radios_lists = new ArrayList<List<Button>>();
		List<String> tags;
		List<Composite> radios_areas = new ArrayList<Composite>();
		//tags = get_result_movie_tags(); to be used
		tags = new ArrayList<String>(); //just for check
		tags.add("tag1");
		tags.add("tag2");
		tags.add("tag3");
		tags.add("tag4");
		tags.add("tag5");
		tags.add("tag6");
		tags.add("tag7");
		//
		
		int count = 0;
		for(String str: tags)
		{
			if (count > 4)
				break;
			tags_labels.add(new Label(tags_area, SWT.NONE));
			tags_labels.get(count).setText(str);		
			radios_areas.add(new Composite(tags_area, SWT.NONE));			
			GridLayout grid_layout_area3 = new GridLayout(5, false);
			radios_areas.get(count).setLayout(grid_layout_area3);
			
			radios_lists.add(new ArrayList<Button>());
			for(int k = 0; k < 5; k++)
			{
				
				Button radio = new Button(radios_areas.get(count), SWT.RADIO);
				String temp_str = "" + (k+1);
				radio.setText(temp_str);
				radios_lists.get(count).add(radio);
			}
			
			count++;
		}
		
		
		
		//tags rate button
		Button tags_button = new Button(tags_area, SWT.PUSH);
		tags_button.setText("Rate");
		GridData grid_data_tags_button = new GridData();
		grid_data_tags_button.horizontalSpan = 2;
		grid_data_tags_button.horizontalIndent = 95;
		tags_button.setLayoutData(grid_data_tags_button);
		
		
		
		//tags bottom label
		Label tags_bottom_label = new Label(tags_area, SWT.NONE);
		tags_bottom_label.setText("Please rate these tags for the movie \n(1=disagree, 5=agree)");
		GridData grid_data_tags_bottom_label = new GridData();
		grid_data_tags_bottom_label.horizontalSpan = 2;
		tags_bottom_label.setLayoutData(grid_data_tags_bottom_label);
		Font font_bottom_tags = new Font(display, "Ariel",8, java.awt.Font.PLAIN );
		tags_bottom_label.setFont(font_bottom_tags);
		
		
		
	}
	
	
	
	

	protected void checkSubclass()
	{
	}
	
	
}



