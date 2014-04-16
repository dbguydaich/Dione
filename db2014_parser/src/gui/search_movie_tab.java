package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import bl.verifier;
import config.config;
import db.db_queries;


//////// functions the tab is waiting for: ////////

// List<String> get_genres()



////// Listeners to be implemented: //////////

// search_button
// perhaps auto-complete for text fields



public class search_movie_tab extends Composite
{

	public search_movie_tab(final Display display, Composite parent, int style)
	{
		super(parent, style);
		

		List<Label> tags_labels = new ArrayList<Label>();	
		List<Label> actors_labels = new ArrayList<Label>();
		
		final List<Text> tags_texts = new ArrayList<Text>();
		final List<Text> actors_texts = new ArrayList<Text>();
		
		final List<Button> rating_radios = new ArrayList<Button>();
		final List<Button> genres_checkboxes;
		
		List<String> genres;
		
		
		//this.setSize(window_width, window_height);
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		Color color = display.getSystemColor(SWT.COLOR_GRAY);
		this.setBackground(color);
		color.dispose();
		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Search Movie");
		FormData form_data_headline_label = new FormData();
		form_data_headline_label.top = new FormAttachment(0,0);
		form_data_headline_label.left = new FormAttachment(0,200);
		headline_label.setFont(new Font(display, "Ariel",20, java.awt.Font.PLAIN ));
		headline_label.setLayoutData(form_data_headline_label);		
		
		
		//left area
		Composite left_area = new Composite(this, SWT.NONE);
		FormData form_data_left_area = new FormData(165, 350); 
		form_data_left_area.top = new FormAttachment(0, 40);
		form_data_left_area.left = new FormAttachment(0, 30);
		left_area.setLayoutData(form_data_left_area);
		GridLayout grid_layout_left_area = new GridLayout(2, false);
		left_area.setLayout(grid_layout_left_area);
		
		
		
		//title label
		Label title_label = new Label(left_area, SWT.NONE);
		title_label.setText("Title");
		GridData grid_data_title_label = new GridData();
		grid_data_title_label.verticalIndent = 0;
		title_label.setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		title_label.setLayoutData(grid_data_title_label);
		
		
		//title text
		final Text title_text = new Text(left_area, SWT.BORDER);
		GridData grid_data_title_text = new GridData();
		grid_data_title_text.verticalIndent = 0;
		grid_data_title_text.horizontalIndent = 15;
		title_text.setLayoutData(grid_data_title_text);
		title_text.setBounds(100, 300, 50, 100);

		
		//director label
		Label director_label = new Label(left_area, SWT.NONE);
		director_label.setText("Director");
		GridData grid_data_director_label = new GridData();
		grid_data_director_label.verticalIndent = 20;
		director_label.setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		director_label.setLayoutData(grid_data_director_label);
		
		
		//director text
		final Text director_text = new Text(left_area, SWT.BORDER);
		GridData grid_data_director_text = new GridData();
		grid_data_director_text.verticalIndent = 20;
		grid_data_director_text.horizontalIndent = 15;
		director_text.setLayoutData(grid_data_director_text);
		
		
		//actor1 label
		actors_labels.add(new Label(left_area, SWT.NONE));
		actors_labels.get(0).setText("Actor1");
		GridData grid_data_actor0_label = new GridData();
		grid_data_actor0_label.verticalIndent = 20;
		actors_labels.get(0).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		actors_labels.get(0).setLayoutData(grid_data_actor0_label);
		
		
		//actor1 text
		actors_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_actor0_text = new GridData();
		grid_data_actor0_text.verticalIndent = 20;
		grid_data_actor0_text.horizontalIndent = 15;
		actors_texts.get(0).setLayoutData(grid_data_actor0_text);
		
		
		//actor2 label
		actors_labels.add(new Label(left_area, SWT.NONE));
		actors_labels.get(1).setText("Actor2");
		GridData grid_data_actor1_label = new GridData();
		grid_data_actor1_label.verticalIndent = 5;
		actors_labels.get(1).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		actors_labels.get(1).setLayoutData(grid_data_actor1_label);
				
				
		//actor2 text
		actors_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_actor1_text = new GridData();
		grid_data_actor1_text.verticalIndent = 5;
		grid_data_actor1_text.horizontalIndent = 15;
		actors_texts.get(1).setLayoutData(grid_data_actor1_text);

		
		//actor3 label
		actors_labels.add(new Label(left_area, SWT.NONE));
		actors_labels.get(2).setText("Actor3");
		GridData grid_data_actor2_label = new GridData();
		grid_data_actor2_label.verticalIndent = 5;
		actors_labels.get(2).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		actors_labels.get(2).setLayoutData(grid_data_actor2_label);
				
				
		//actor3 text
		actors_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_actor2_text = new GridData();
		grid_data_actor2_text.verticalIndent = 5;
		grid_data_actor2_text.horizontalIndent = 15;
		actors_texts.get(2).setLayoutData(grid_data_actor2_text);
		
		
		
		//tag1 label
		tags_labels.add(new Label(left_area, SWT.NONE));
		tags_labels.get(0).setText("Tag1");
		GridData grid_data_tag0_label = new GridData();
		grid_data_tag0_label.verticalIndent = 20;
		tags_labels.get(0).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		tags_labels.get(0).setLayoutData(grid_data_tag0_label);
				
				
		//tag1 text
		tags_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_tag0_text = new GridData();
		grid_data_tag0_text.verticalIndent = 20;
		grid_data_tag0_text.horizontalIndent = 15;
		tags_texts.get(0).setLayoutData(grid_data_tag0_text);
		
		
		//tag2 label
		tags_labels.add(new Label(left_area, SWT.NONE));
		tags_labels.get(1).setText("Tag2");
		GridData grid_data_tag1_label = new GridData();
		grid_data_tag1_label.verticalIndent = 5;
		tags_labels.get(1).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		tags_labels.get(1).setLayoutData(grid_data_tag1_label);
						
						
		//tag2 text
		tags_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_tag1_text = new GridData();
		grid_data_tag1_text.verticalIndent = 5;
		grid_data_tag1_text.horizontalIndent = 15;
		tags_texts.get(1).setLayoutData(grid_data_tag1_text);
		
		
		//tag3 label
		tags_labels.add(new Label(left_area, SWT.NONE));
		tags_labels.get(2).setText("Tag3");
		GridData grid_data_tag2_label = new GridData();
		grid_data_tag2_label.verticalIndent = 5;
		tags_labels.get(2).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		tags_labels.get(2).setLayoutData(grid_data_tag2_label);
						
						
		//tag3 text
		tags_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_tag2_text = new GridData();
		grid_data_tag2_text.verticalIndent = 5;
		grid_data_tag2_text.horizontalIndent = 15;
		tags_texts.get(2).setLayoutData(grid_data_tag2_text);
		
		
		//tag4 label
		tags_labels.add(new Label(left_area, SWT.NONE));
		tags_labels.get(3).setText("Tag4");
		GridData grid_data_tag3_label = new GridData();
		grid_data_tag3_label.verticalIndent = 5;
		tags_labels.get(3).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		tags_labels.get(3).setLayoutData(grid_data_tag3_label);
						
						
		//tag4 text
		tags_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_tag3_text = new GridData();
		grid_data_tag3_text.verticalIndent = 5;
		grid_data_tag3_text.horizontalIndent = 15;
		tags_texts.get(3).setLayoutData(grid_data_tag3_text);
		
		
		//tag5 label
		tags_labels.add(new Label(left_area, SWT.NONE));
		tags_labels.get(4).setText("Tag5");
		GridData grid_data_tag4_label = new GridData();
		grid_data_tag4_label.verticalIndent = 5;
		tags_labels.get(4).setFont(new Font(display, "Ariel",12, java.awt.Font.PLAIN ));
		tags_labels.get(4).setLayoutData(grid_data_tag4_label);
						
						
		//tag5 text
		tags_texts.add(new Text(left_area, SWT.BORDER));
		GridData grid_data_tag4_text = new GridData();
		grid_data_tag4_text.verticalIndent = 5;
		grid_data_tag4_text.horizontalIndent = 15;
		tags_texts.get(4).setLayoutData(grid_data_tag4_text);
		
	
		
		
		
		//rating area
		Composite rating_area = new Composite(this, SWT.NONE);
		FormData form_data_rating_area = new FormData(200, 60); 
		form_data_rating_area.top = new FormAttachment(0, 40);
		form_data_rating_area.left = new FormAttachment(0, 220);
		rating_area.setLayoutData(form_data_rating_area);
		GridLayout grid_layout_rating_area = new GridLayout(5, false);
		rating_area.setLayout(grid_layout_rating_area);
		
		
		//rating label
		Label rating_label = new Label(rating_area, SWT.NONE);
		rating_label.setText("Rating (in stars)");
		GridData grid_data_rating_label = new GridData();
		grid_data_rating_label.horizontalSpan = 5;
		rating_label.setLayoutData(grid_data_rating_label);
		rating_label.setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
		
		
		//rating radios
		GridData grid_data_rating_radios = new GridData();
		grid_data_rating_radios.horizontalIndent = 5;
		for(int i = 0; i < 5; i++)
		{
			rating_radios.add(new Button(rating_area, SWT.RADIO));
			rating_radios.get(i).setText(Integer.toString(i+1));
			rating_radios.get(i).setLayoutData(grid_data_rating_radios);
		}
		
		
		
		
		//genres area
		Composite genres_area = new Composite(this, SWT.BORDER);
		FormData form_data_genres_area = new FormData(198, 280); 
		form_data_genres_area.top = new FormAttachment(0, 101);
		form_data_genres_area.left = new FormAttachment(0, 219);
		genres_area.setLayoutData(form_data_genres_area);
		GridLayout grid_layout_genres_area = new GridLayout(2, false);
		genres_area.setLayout(grid_layout_genres_area);
		
		
		//genres label
		Label genres_label = new Label(genres_area, SWT.NONE);
		genres_label.setText("Genres");
		GridData grid_data_genres_label = new GridData();
		grid_data_genres_label.horizontalSpan = 2;
		grid_data_genres_label.horizontalAlignment = SWT.CENTER;
		genres_label.setLayoutData(grid_data_genres_label);
		genres_label.setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
		
		
		//genres check-boxes

		//genres = get_genres();  //to be used when function exists
		
		//just for check
		genres_checkboxes = new ArrayList<Button>();
		genres = new ArrayList<String>();	
		for(int i = 1; i < 20; i ++)
		{
			genres.add("Genre" + i + " ");
;
		}
		//
		
		GridData grid_data_genres_checkbox = new GridData();
		grid_data_genres_checkbox.horizontalIndent = 20;
		for(int i = 0; i < genres.size(); i++)
		{
			Button checkbox = new Button(genres_area, SWT.CHECK);		
			checkbox.setLayoutData(grid_data_genres_checkbox);
			checkbox.setText(genres.get(i));
			genres_checkboxes.add(checkbox);
		}
		
		
		//search button
		Button search_button = new Button(this, SWT.PUSH);
		FormData form_data_search_button = new FormData(80, 50); 
		form_data_search_button.top = new FormAttachment(0, 330);
		form_data_search_button.left = new FormAttachment(0, 460);
		search_button.setLayoutData(form_data_search_button);
		search_button.setText("Search");
		search_button.setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
		
		//Listener
		search_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
//				if(!verifier.verifyname(search_text.getText())){ 
//					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					alertBox.setText("Illegal Username");
//					alertBox.setMessage("Friend name length is 1-10 chars \n Only letters or numbers allowed.");
//					alertBox.open();
//				}
				String title = title_text.getText();
				String director =  director_text.getText();
				
				List<String> actor_list = new  ArrayList<String>();
				get_text(actors_texts, actor_list);
				
				List<String> tags_list = new  ArrayList<String>();
				get_text(tags_texts, tags_list);
				
				
				List<Boolean> rating_radios_text = new ArrayList<Boolean>();
				get_text_button(rating_radios, rating_radios_text);
				
	

	
			
				List<Boolean> genres_numbers =  new ArrayList<Boolean>();
				get_text_button (genres_checkboxes, genres_numbers );
//				else if(db_queries.does_movie_exists(title,director,actor_list,tags_list,
//							rating_radios_text,genres_numbers)){ //to be implemented next on
//	
//		
//
//
//					
//				}
//				else //no movie found
//				{
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("No Movie Found");
//					messageBox.setMessage("No movie Found. Please try again.");
//					messageBox.open();
//				}
			}

		});
		
		
		
	}
	
	 void get_text(List<Text> from,List<String> to)
	{
		for (Text a: from)
		{
			to.add((a.getText()));
		}
	}
	 void get_text_button(List<Button> from,List<Boolean> to)
		{
			for (Button a: from)
			{
				to.add((a.getSelection()));
			}
		}
	

}
