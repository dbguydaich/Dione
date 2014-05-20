package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import parser_entities.light_entity_movie;
import bl.movie_logics;



//////////functions the tab is waiting for: //////////////

//List<String> get_recommended_movies_my_taste();
//List<String> get_recommended_movies_friends_taste();
//List<String> get_recommended_movies_similar_to_me_taste();


//////////Listeners to be implemented: ////////////////////

//every item in the following lists is clickable, click will show the movie details
//List<Link> movies_my_taste_links = new ArrayList<Link>();
//List<Link> movies_friends_taste_links = new ArrayList<Link>();
//List<Link> movies_similar_to_me_taste_links = new ArrayList<Link>();


public class recommendation_tab extends Composite
{
	
	public recommendation_tab(final Display display, Composite parent, int style)
	{
		super(parent, style);
		
		
		List<String> movies_my_taste= null;
		List<String> movies_friends_taste = null;
		List<String> movies_similar_to_me_taste;
		
		List<Link> movies_my_taste_links = new ArrayList<Link>();
		List<Link> movies_friends_taste_links = new ArrayList<Link>();
		List<Link> movies_similar_to_me_taste_links = new ArrayList<Link>();
		
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		final Color color_form_layout_tab = display.getSystemColor(SWT.COLOR_GRAY);
		this.setBackground(color_form_layout_tab);
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				color_form_layout_tab.dispose();
			}		
		});
		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Highly Recommended Movies For You");
		headline_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 2, 140));	
		final Font font_headline_label = new Font(display, "Ariel",15, java.awt.Font.PLAIN ); 
		headline_label.setFont(font_headline_label);
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline_label.dispose();
			}		
		});
		
		
		//based on what we have learned about you AREA
		Composite area1 = new Composite(this, SWT.NONE);
		area1.setLayoutData(gui_utils.form_data_factory(295, 185, 30, 10));
		GridLayout grid_layout_area1 = new GridLayout(1, false);
		area1.setLayout(grid_layout_area1);
		
		
		
		//based on what we have learned about you HEADLINE
		Label area1_headline = new Label(area1, SWT.NONE);
		area1_headline.setText("Based On What We have Learned About You");
		area1_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1, -1, -1));
		final Font font_area1_headline = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		area1_headline.setFont(font_area1_headline);
		area1_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_area1_headline.dispose();
			}		
		});
		
		
		//based on what we have learned about you LINKS
		List<light_entity_movie> movies_my_taste_entity = null;
		try {
			movies_my_taste_entity = log_in_window.user.get_user_recommended_movies(log_in_window.user.get_current_user_id());
			movies_my_taste = gui_utils.convert_movies_entity_to_string(movies_my_taste_entity);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			gui_utils.raise_sql_error_window(display);
			
			e1.printStackTrace();
		}
		
		//just for check
//		movies_my_taste = new ArrayList<String>();
//		movies_my_taste.add("reco by my taste1");
//		movies_my_taste.add("reco by my taste2");
//		movies_my_taste.add("reco by my taste3");
//		movies_my_taste.add("reco by my taste4");
//		movies_my_taste.add("reco by my taste5");
		//
		
		int i = 0;
		int j=0;
		final List<light_entity_movie> movies_my_taste_entity_for_annonymus = movies_my_taste_entity;
		for(j=0; j< movies_my_taste.size(); j++)
		{
			final int k =j;
			System.out.println(movies_my_taste.get(j));
			movies_my_taste_links.add(new Link(area1, SWT.BORDER));
			movies_my_taste_links.get(j).setText(movies_my_taste.get(j));
			movies_my_taste_links.get(j).setLayoutData(gui_utils.grid_data_factory(200, 18, -1, 3, -1, -1, -1, -1));
			movies_my_taste_links.get(j).addSelectionListener(new SelectionAdapter() {
			//	@Override
				public void widgetSelected(SelectionEvent arg0) {
				System.out.println("hi");
				movie_details_window  movie_details =new movie_details_window( display ,movies_my_taste_entity_for_annonymus.get(k).get_movie_id());
				/////wtf? not working/ shahar what is going on in here????
				}

			});
			

			
			i++;
		}
		
		
		//based on my friends AREA
		Composite area2 = new Composite(this, SWT.NONE);
		area2.setLayoutData(gui_utils.form_data_factory(295, 185, 220, 10));
		GridLayout grid_layout_area2 = new GridLayout(1, false);
		area2.setLayout(grid_layout_area2);
				
				
		//based on my friends headline
		Label area2_headline = new Label(area2, SWT.NONE);
		area2_headline.setText("Based On Your Friends Taste");
		area2_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1, -1, -1));
		final Font font_area2_headline = new Font(display, "Ariel",11, java.awt.Font.PLAIN ); 
		area2_headline.setFont(font_area2_headline);
		area2_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_area2_headline.dispose();
			}		
		});
		
		
		
		//based on my friends LINKS
		
		try {
			movies_friends_taste =log_in_window.user.get_user_recommended_movies_by_friends(log_in_window.user.get_current_user_id(), 5);
		} catch (SQLException e1) {
			gui_utils.raise_sql_error_window(display);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//just for check
//		movies_friends_taste = new ArrayList<String>();
//		movies_friends_taste.add("reco by friends taste1");
//		movies_friends_taste.add("reco by friends taste2");
//		movies_friends_taste.add("reco by friends taste3");
//		movies_friends_taste.add("reco by friends taste4");
	//	movies_friends_taste.add("reco by friends taste5");
		//
		
		i = 0;
		for(String str: movies_friends_taste)
		{
			movies_friends_taste_links.add(new Link(area2, SWT.BORDER));
			movies_friends_taste_links.get(i).setText(str);
			movies_friends_taste_links.get(i).setLayoutData(gui_utils.grid_data_factory(200, 18, -1, 3, -1, -1, -1, -1));
			
			movies_friends_taste_links.get(i).addSelectionListener(new SelectionAdapter() {
			//	@Override
				public void widgetSelected(SelectionEvent arg0) {
				System.out.println("hi");

			}

			});

			
			i++;
		}
		
		
		//based on similar to me area
		Composite area3 = new Composite(this, SWT.NONE);
		area3.setLayoutData(gui_utils.form_data_factory(295, 185, 30, 310));
		GridLayout grid_layout_area3 = new GridLayout(1, false);
		area3.setLayout(grid_layout_area3);
		
		
		
		//based on similar to me headline
		Label area3_headline = new Label(area3, SWT.NONE);
		area3_headline.setText("Based On Users With Similar Taste");
		area3_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1, -1, -1));
		final Font font_area3_headline = new Font(display, "Ariel",11, java.awt.Font.PLAIN ); 
		area3_headline.setFont(font_area3_headline);
		area3_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_area3_headline.dispose();
			}		
		});
		
		
		//based on similar to me LINKS
		
		//movies_similar_to_me_taste = get_recommended_movies_similar_to_me_taste(); //to be used when function exists
		
		//just for check
		movies_similar_to_me_taste = new ArrayList<String>();
		movies_similar_to_me_taste.add("reco by similar to me taste1");
		movies_similar_to_me_taste.add("reco by similar to me taste2");
		movies_similar_to_me_taste.add("reco by similar to me taste3");
		movies_similar_to_me_taste.add("reco by similar to me taste4");
		movies_similar_to_me_taste.add("reco by similar to me taste5");
		//
		
		i = 0;
		for(String str: movies_similar_to_me_taste)
		{
			movies_similar_to_me_taste_links.add(new Link(area3, SWT.BORDER));
			movies_similar_to_me_taste_links.get(i).setText(str);
			movies_similar_to_me_taste_links.get(i).setLayoutData(gui_utils.grid_data_factory(200, 18, -1, 3, -1, -1, -1, -1));
			
			movies_similar_to_me_taste_links.get(i).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
				System.out.println("hi");

				}

			});

			
			i++;
		}
		
		
	}
}
