package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.swt.widgets.Link;

import config.config;

////////// functions the tab is waiting for: //////////////

// List<String> get_user_popular_tags()
// List<String> get_user_most_recommended_movies()
// List<String> get_user_recent_activities()
// List<String> get_friends_recent_activities()


//////// Listeners to be implemented: //////////////

/// for every item in reco_movies_links, add listener to switch tab to movie search results

public class overview_tab extends Composite
{
	
	public overview_tab(Display display, Composite parent, int style)
	{
		super(parent, style);
		
		List<String> user_tags_string;
		List<Label> user_tags_labels = new ArrayList<Label>();
		List<String> reco_movies_string;
		List<Link> reco_movies_links = new ArrayList<Link>();
		List<String> user_activities_strings;
		List<Label> user_activities_labels = new ArrayList<Label>();
		List<String> friends_activities_strings;
		List<Label> friends_activities_labels = new ArrayList<Label>();
		
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		Color color = display.getSystemColor(SWT.COLOR_GRAY);
		this.setBackground(color);
		color.dispose();

		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("MovieBook");
		FormData form_data_headline_label = new FormData();
		form_data_headline_label.top = new FormAttachment(0,2);
		form_data_headline_label.left = new FormAttachment(0,257);
		headline_label.setFont(new Font(display, "Ariel",15, java.awt.Font.PLAIN ));
		headline_label.setLayoutData(form_data_headline_label);	
		
		
		//taste area
		Composite taste_area = new Composite(this, SWT.NONE);
		FormData form_data_taste_area = new FormData(295, 185); 
		form_data_taste_area.top = new FormAttachment(0, 220);
		form_data_taste_area.left = new FormAttachment(0, 10);
		taste_area.setLayoutData(form_data_taste_area);
		GridLayout grid_layout_taste_area = new GridLayout(1, false);
		taste_area.setLayout(grid_layout_taste_area);
		
		
		//taste_headline
		Label taste_headline = new Label(taste_area, SWT.NONE);
		taste_headline.setText("We Found Out You Like Movies Tagged As:");
		GridData grid_data_taste_headline = new GridData();
		grid_data_taste_headline.verticalIndent = 0;
		taste_headline.setFont(new Font(display, "Ariel",11, java.awt.Font.PLAIN ));
		taste_headline.setLayoutData(grid_data_taste_headline);
		
		
		
		
		//taste_tags
		
		//user_tags_string = get_user_popular_tags(); //to be used when function exists
		
		//just for check
		user_tags_string = new ArrayList<String>();
		user_tags_string.add("tag1");
		user_tags_string.add("tag2");
		user_tags_string.add("tag3");
		user_tags_string.add("tag4");
		user_tags_string.add("tag5");
		user_tags_string.add("tag6");
		user_tags_string.add("tag7");
		//
		
		int i = 0;
		for(String str: user_tags_string)
		{
			user_tags_labels.add(new Label(taste_area, SWT.NONE));
			user_tags_labels.get(i).setText(user_tags_string.get(i));
			user_tags_labels.get(i).setFont(new Font(display, "Ariel", 11, java.awt.Font.PLAIN));
			i++;
		}
		
		
		
		//recommendation area
		Composite reco_area = new Composite(this, SWT.NONE);
		FormData form_data_reco_area = new FormData(295, 185); 
		form_data_reco_area.top = new FormAttachment(0, 30);
		form_data_reco_area.left = new FormAttachment(0, 10);
		reco_area.setLayoutData(form_data_reco_area);
		GridLayout grid_layout_reco_area = new GridLayout(1, false);
		reco_area.setLayout(grid_layout_reco_area);
		
		
		//recommendation headline
		Label reco_headline = new Label(reco_area, SWT.NONE);
		reco_headline.setText("Recommended Movies");
		GridData grid_data_reco_headline = new GridData();
		grid_data_reco_headline.verticalIndent = 0;
		reco_headline.setFont(new Font(display, "Ariel",14, java.awt.Font.PLAIN ));
		reco_headline.setLayoutData(grid_data_reco_headline);
		
		
		
		
		//recommended movie links
		
		//reco_movies_string = get_user_most_recommended_movies(); //to be used when function exists
		
		//just for check
		reco_movies_string = new ArrayList<String>();
		reco_movies_string.add("movie number 1");
		reco_movies_string.add("movie number 2");
		reco_movies_string.add("movie number 3");
		reco_movies_string.add("movie number 4");
		reco_movies_string.add("movie number 5");
		//
		
		i = 0;
		for(String str: reco_movies_string)
		{
			reco_movies_links.add(new Link(reco_area, SWT.BORDER));
			reco_movies_links.get(i).setText(reco_movies_string.get(i));
			
			
			reco_movies_links.get(i).addSelectionListener(new SelectionAdapter() {
			//	@Override
//				public void widgetSelected(SelectionEvent arg0) {
//				open_movie_details(str);
//
//				}

			});
			
			reco_movies_links.get(i).setFont(new Font(display, "Ariel", 11, java.awt.Font.PLAIN));
			color = display.getSystemColor(SWT.COLOR_GRAY);
			reco_movies_links.get(i).setBackground(color);
			GridData grid_data_reco_movie = new GridData(200,18);
			reco_movies_links.get(i).setLayoutData(grid_data_reco_movie);
			i++;
		}
		
		
		//recommendation buttom label
		Label reco_buttom_label = new Label(reco_area, SWT.NONE);
		reco_buttom_label.setText("Click on a Movie Name For Movie Details");
		GridData grid_data_reco_buttom_label = new GridData();
		grid_data_reco_buttom_label.verticalIndent = 0;
		reco_buttom_label.setFont(new Font(display, "Ariel",10, java.awt.Font.PLAIN ));
		reco_buttom_label.setLayoutData(grid_data_reco_buttom_label);
		
		
		
		
		//user recent activity area
		Composite user_activity_area = new Composite(this, SWT.NONE);
		FormData form_data_user_activity_area = new FormData(295, 185); 
		form_data_user_activity_area.top = new FormAttachment(0, 30);
		form_data_user_activity_area.left = new FormAttachment(0, 310);
		user_activity_area.setLayoutData(form_data_user_activity_area);
		GridLayout grid_layout_user_activity_area = new GridLayout(1, false);
		user_activity_area.setLayout(grid_layout_user_activity_area);
		
		//user recent activity headline
		Label user_activity_headline = new Label(user_activity_area, SWT.NONE);
		user_activity_headline.setText("Your Recent Activity");
		GridData grid_data_user_activity_headline = new GridData();
		grid_data_user_activity_headline.verticalIndent = 0;
		user_activity_headline.setFont(new Font(display, "Ariel",14, java.awt.Font.PLAIN ));
		user_activity_headline.setLayoutData(grid_data_user_activity_headline);
		
		
		
		
		//user recent activities labels
		
		//user_activities_strings = get_user_recent_activities(); //to be used when function exists
		
		//just for check
		user_activities_strings = new ArrayList<String>();
		user_activities_strings.add("recent activity 1");
		user_activities_strings.add("recent activity 2");
		user_activities_strings.add("recent activity 3");
		user_activities_strings.add("recent activity 4");
		user_activities_strings.add("recent activity 5");
		user_activities_strings.add("recent activity 6");
		//
		
		i = 0;
		for(String str: user_activities_strings)
		{
			user_activities_labels.add(new Label(user_activity_area, SWT.NONE));
			user_activities_labels.get(i).setText(user_activities_strings.get(i));
			user_activities_labels.get(i).setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
			i++;
		}
		
		
		
		
		//friends recent activity area
		Composite friends_activity_area = new Composite(this, SWT.NONE);
		FormData form_data_friends_activity_area = new FormData(295, 185); 
		form_data_friends_activity_area.top = new FormAttachment(0, 220);
		form_data_friends_activity_area.left = new FormAttachment(0, 310);
		friends_activity_area.setLayoutData(form_data_friends_activity_area);
		GridLayout grid_layout_friends_activity_area = new GridLayout(1, false);
		friends_activity_area.setLayout(grid_layout_friends_activity_area);
		
		
		//friends recent activity headline
		Label friends_activity_headline = new Label(friends_activity_area, SWT.NONE);
		friends_activity_headline.setText("Your Friends Recent Activity");
		GridData grid_data_friends_activity_headline = new GridData();
		grid_data_friends_activity_headline.verticalIndent = 0;
		friends_activity_headline.setFont(new Font(display, "Ariel",14, java.awt.Font.PLAIN ));
		friends_activity_headline.setLayoutData(grid_data_friends_activity_headline);
		
		
		
		//friends recent activities labels
		
		//friends_activities_strings = get_friends_recent_activities(); //to be used when function exists
		
		//just for check
		friends_activities_strings = new ArrayList<String>();
		friends_activities_strings.add("friend recent activity 1");
		friends_activities_strings.add("friend recent activity 2");
		friends_activities_strings.add("friend recent activity 3");
		friends_activities_strings.add("friend recent activity 4");
		friends_activities_strings.add("friend recent activity 5");
		friends_activities_strings.add("friend recent activity 6");
		//
		
		i = 0;
		for(String str: friends_activities_strings)
		{
			friends_activities_labels.add(new Label(friends_activity_area, SWT.NONE));
			friends_activities_labels.get(i).setText(friends_activities_strings.get(i));
			friends_activities_labels.get(i).setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
			i++;
		}
		
		
		
		
	}

}
