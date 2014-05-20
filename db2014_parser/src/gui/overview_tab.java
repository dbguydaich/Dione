package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
		
		final Color color_window = display.getSystemColor(SWT.COLOR_GRAY);
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				color_window.dispose();
			}		
		});
		
		this.setBackground(color_window);

		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("MovieBook");
		headline_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 2, 257));	
		final Font font_headline = new Font(display, "Ariel",15, java.awt.Font.PLAIN );
		headline_label.setFont(font_headline);
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline.dispose();
			}		
		});
	
		
		
		//taste area
		Composite taste_area = new Composite(this, SWT.NONE);
		taste_area.setLayoutData(gui_utils.form_data_factory(295, 185, 220, 10));	
		GridLayout grid_layout_taste_area = new GridLayout(1, false);
		taste_area.setLayout(grid_layout_taste_area);
		
		
		//taste_headline
		Label taste_headline = new Label(taste_area, SWT.NONE);
		taste_headline.setText("We Found Out You Like Movies Tagged As:");
		taste_headline.setLayoutData(gui_utils.grid_data_factory(-1, 0, -1, -1, -1, -1));
		final Font font_taste_headline = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		taste_headline.setFont(font_taste_headline);
		taste_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_taste_headline.dispose();
			}		
		});
		
		
		
		
		//taste_tags
		
		try {
			user_tags_string =log_in_window.user.get_user_popular_tags() ;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			user_tags_string = new ArrayList<String>();
			gui_utils.raise_sql_error_window(display);
			e1.printStackTrace();
		} //to be used when function exists
		
//		//just for check
//		user_tags_string = new ArrayList<String>();
//		user_tags_string.add("tag1");
//		user_tags_string.add("tag2");
//		user_tags_string.add("tag3");
//		user_tags_string.add("tag4");
//		user_tags_string.add("tag5");
//		user_tags_string.add("tag6");
//		user_tags_string.add("tag7");
		//
		
		final Font font_user_tags_label = new Font(display, "Ariel", 11, java.awt.Font.PLAIN);
		int i = 0;
		for(String str: user_tags_string)
		{
			user_tags_labels.add(new Label(taste_area, SWT.NONE));
			user_tags_labels.get(i).setText(str);
			user_tags_labels.get(i).setFont(font_user_tags_label);
			if(i == 0)
			{
				user_tags_labels.get(i).addDisposeListener(new DisposeListener()
				{
					public void widgetDisposed(DisposeEvent e) 
					{
						font_user_tags_label.dispose();
					}		
				});
			}
			
			i++;
		}
		
		if(i == 0)
			font_user_tags_label.dispose();
		
		
		
		//recommendation area
		Composite reco_area = new Composite(this, SWT.NONE);
		reco_area.setLayoutData(gui_utils.form_data_factory(295, 185, 30, 10));
		GridLayout grid_layout_reco_area = new GridLayout(1, false);
		reco_area.setLayout(grid_layout_reco_area);
		
		
		
		//recommendation headline
		Label reco_headline = new Label(reco_area, SWT.NONE);
		reco_headline.setText("Recommended Movies");
		reco_headline.setLayoutData(gui_utils.grid_data_factory(-1, 0, -1, -1, -1, -1));
		final Font font_reco_headline = new Font(display, "Ariel",14, java.awt.Font.PLAIN );
		reco_headline.setFont(font_reco_headline);
		reco_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_reco_headline.dispose();
			}		
		});
		
		
		
		
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
		
		final Color color_reco_movie_link = display.getSystemColor(SWT.COLOR_GRAY);
		i = 0;
		for(String str: reco_movies_string)
		{
			reco_movies_links.add(new Link(reco_area, SWT.BORDER ));
			reco_movies_links.get(i).setText(str);
			reco_movies_links.get(i).setFont(new Font(display, "Ariel", 9, java.awt.Font.PLAIN));
			if(i == 0)
			{
				reco_movies_links.get(i).addDisposeListener(new DisposeListener()
				{
					public void widgetDisposed(DisposeEvent e) 
					{
						color_reco_movie_link.dispose();
					}		
				});
			}
			
			reco_movies_links.get(i).addSelectionListener(new SelectionAdapter() {
			//	@Override
//				public void widgetSelected(SelectionEvent arg0) {
//				open_movie_details(str);
//
//				}

			});

			
			reco_movies_links.get(i).setBackground(color_reco_movie_link);
			reco_movies_links.get(i).setLayoutData(gui_utils.grid_data_factory(200, 18, -1, -1, -1, -1, -1, -1));
			i++;
		}
		
		if( i == 0)
			color_reco_movie_link.dispose();
		
		
		//recommendation buttom label
		Label reco_bottom_label = new Label(reco_area, SWT.NONE);
		reco_bottom_label.setText("Click on a Movie Name For Movie Details");
		reco_bottom_label.setLayoutData(gui_utils.grid_data_factory(-1, 0, -1, -1, -1, -1));
		final Font font_reco_bottom_label = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		reco_bottom_label.setFont(font_reco_bottom_label);
		reco_bottom_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_reco_bottom_label.dispose();
			}		
		});
		
		
		
		//user recent activity area
		Composite user_activity_area = new Composite(this, SWT.NONE);
		user_activity_area.setLayoutData(gui_utils.form_data_factory(295, 185, 30, 310));
		GridLayout grid_layout_user_activity_area = new GridLayout(1, false);
		user_activity_area.setLayout(grid_layout_user_activity_area);
		
		
		//user recent activity headline
		Label user_activity_headline = new Label(user_activity_area, SWT.NONE);
		user_activity_headline.setText("Your Recent Activity");
		user_activity_headline.setLayoutData(gui_utils.grid_data_factory(-1, 0, -1, -1, -1, -1));
		final Font font_user_activity_headline = new Font(display, "Ariel",14, java.awt.Font.PLAIN );
		user_activity_headline.setFont(font_user_activity_headline);
		user_activity_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_user_activity_headline.dispose();
			}		
		});
				
		
		
		
		//user recent activities labels
		
		try {
			user_activities_strings = log_in_window.user.get_user_recent_string_activities(log_in_window.user.get_current_user_id(), 6);
			
		} catch (SQLException e1) {
			user_activities_strings = new ArrayList<String>();
			gui_utils.raise_sql_error_window(display);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		//just for check
//		user_activities_strings = new ArrayList<String>();
//		user_activities_strings.add("recent activity 1");
//		user_activities_strings.add("recent activity 2");
//		user_activities_strings.add("recent activity 3");
//		user_activities_strings.add("recent activity 4");
//		user_activities_strings.add("recent activity 5");
//		user_activities_strings.add("recent activity 6");
//		//
		
		final Font font_user_activities_labels = new Font(display, "Ariel", 10, java.awt.Font.PLAIN);
		i = 0;
		for(String str: user_activities_strings)
		{
			user_activities_labels.add(new Label(user_activity_area, SWT.NONE));
			user_activities_labels.get(i).setText(str);
			if (i == 0)
			{
				user_activities_labels.get(i).addDisposeListener(new DisposeListener()
				{
					public void widgetDisposed(DisposeEvent e) 
					{
						font_user_activities_labels.dispose();
					}		
				});
			}
			
			user_activities_labels.get(i).setFont(font_user_activities_labels);
			i++;
		}
		
		if(i == 0)
			font_user_activities_labels.dispose();
		
		
		
		
		//friends recent activity area
		Composite friends_activity_area = new Composite(this, SWT.NONE);
		friends_activity_area.setLayoutData(gui_utils.form_data_factory(295, 185, 220, 310));
		GridLayout grid_layout_friends_activity_area = new GridLayout(1, false);
		friends_activity_area.setLayout(grid_layout_friends_activity_area);
		
		
		//friends recent activity headline
		Label friends_activity_headline = new Label(friends_activity_area, SWT.NONE);
		friends_activity_headline.setText("Your Friends Recent Activity");
		friends_activity_headline.setLayoutData(gui_utils.grid_data_factory(-1, 0, -1, -1, -1, -1));
		final Font font_friends_activity_headline = new Font(display, "Ariel",14, java.awt.Font.PLAIN );
		friends_activity_headline.setFont(font_friends_activity_headline);
		friends_activity_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_friends_activity_headline.dispose();
			}		
		});
		
		
		
		//friends recent activities labels
		
		try {
			friends_activities_strings =log_in_window.user.get_friends_recent_string_activities();
		} catch (SQLException e1) {
			friends_activities_strings = new ArrayList<String>();
			// TODO Auto-generated catch block
			gui_utils.raise_sql_error_window(display);
			e1.printStackTrace();
		}
		
		//just for check
//		friends_activities_strings = new ArrayList<String>();
//		friends_activities_strings.add("friend recent activity 1");
//		friends_activities_strings.add("friend recent activity 2");
//		friends_activities_strings.add("friend recent activity 3");
//		friends_activities_strings.add("friend recent activity 4");
//		friends_activities_strings.add("friend recent activity 5");
//		friends_activities_strings.add("friend recent activity 6");
//		//
		
		final Font font_friends_activities_labels = new Font(display, "Ariel", 12, java.awt.Font.PLAIN);
		i = 0;
		for(String str: friends_activities_strings)
		{
			friends_activities_labels.add(new Label(friends_activity_area, SWT.NONE));
			friends_activities_labels.get(i).setText(str);
			friends_activities_labels.get(i).setFont(font_friends_activities_labels);
			if (i == 0)
			{
				friends_activities_labels.get(i).addDisposeListener(new DisposeListener()
				{
					public void widgetDisposed(DisposeEvent e) 
					{
						font_friends_activities_labels.dispose();
					}		
				});
			}
			i++;
		}
		
		if(i == 0)
			font_friends_activities_labels.dispose();
		
		
		
		
	}

}
