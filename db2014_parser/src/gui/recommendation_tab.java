package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
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
		List<String> movies_top_rated;
		
		List<Label> movies_my_taste_labels = new ArrayList<Label>();
		List<Label> movies_friends_taste_labels = new ArrayList<Label>();
		List<Label>movies_top_rated_labels = new ArrayList<Label>();
		
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
	
		//window background
		String imgURL = ".\\src\\gui\\images\\blue_640_480_3.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				background.dispose();
			}		
		});
		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Highly Recommended Movies For You");
		headline_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 2, 55));	
		final Font font_headline_label = new Font(display, "Ariel",20, SWT.BOLD ); 
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
		area1.setLayoutData(gui_utils.form_data_factory(295, 185, 40, 10));
		GridLayout grid_layout_area1 = new GridLayout(1, false);
		area1.setLayout(grid_layout_area1);
		
		
		
		//based on what we have learned about you HEADLINE
		Label area1_headline = new Label(area1, SWT.NONE);
		area1_headline.setText("Based On What We have Learned About You");
		area1_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1, -1, -1));
		final Font font_area1_headline = new Font(display, "Ariel",10, SWT.BOLD);
		final Font font_based_on_learned_about_you = new Font(display, "Ariel", 11, SWT.NONE);
		area1_headline.setFont(font_area1_headline);
		area1_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_area1_headline.dispose();
				font_based_on_learned_about_you.dispose();
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
		

		//
		
		int i = 0;
		int j=0;
		final List<light_entity_movie> movies_my_taste_entity_for_annonymus = movies_my_taste_entity;
		for(j=0; j < movies_my_taste.size(); j++)
		{
			final int k = j;
			System.out.println(movies_my_taste.get(j));
			movies_my_taste_labels.add(new Label(area1, SWT.NONE));
			movies_my_taste_labels.get(j).setText(movies_my_taste.get(j));
			movies_my_taste_labels.get(j).setFont(font_based_on_learned_about_you);
			movies_my_taste_labels.get(j).setLayoutData(gui_utils.grid_data_factory(280, 20, -1, 3, -1, -1, -1, -1));
			movies_my_taste_labels.get(j).addMouseListener(new MouseAdapter() {
			//	@Override
				public void mouseUp(MouseEvent arg0) {
					System.out.println("hi-1");
					movie_details_window  movie_details= null;
				try {
					movie_details =new movie_details_window( display ,movies_my_taste_entity_for_annonymus.get(k).get_movie_id());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					movie_details.open();
				
				}

			});
			
			if(j == 4)
				break;
		}
		
		
		//based on my friends area
		Composite area2 = new Composite(this, SWT.NONE);
		area2.setLayoutData(gui_utils.form_data_factory(295, 185, 230, 10));
		GridLayout grid_layout_area2 = new GridLayout(1, false);
		area2.setLayout(grid_layout_area2);
				
				
		//based on my friends headline
		Label area2_headline = new Label(area2, SWT.NONE);
		area2_headline.setText("Based On Your Friends Taste");
		area2_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1, -1, -1));
		final Font font_area2_headline = new Font(display, "Ariel",10, SWT.BOLD ); 
		area2_headline.setFont(font_area2_headline);
		area2_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_area2_headline.dispose();
			}		
		});
		
		
		
		//based on my friends LINKS
		List<light_entity_movie> movies_friends_taste_entity = null;
		List<light_entity_movie> movies_freinds_taste_entity = null;
		try {
			movies_freinds_taste_entity = log_in_window.user.get_user_recommended_movies_entities_by_friends(log_in_window.user.get_current_user_id(),5);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		movies_friends_taste = gui_utils.convert_movies_entity_to_string(movies_freinds_taste_entity);
		

		//
		
		i = 0;
		j = 0;
		final List<light_entity_movie> movies_freinds_taste_entity_for_annonymus = movies_my_taste_entity;
		for(j=0; j< movies_friends_taste.size(); j++)
		{
			final int k =j;
		//	System.out.println(movies_my_taste.get(j));
			movies_friends_taste_labels.add(new Label(area2, SWT.NONE));
			movies_friends_taste_labels.get(j).setText(movies_friends_taste.get(j));
			movies_friends_taste_labels.get(j).setFont(font_based_on_learned_about_you);
			movies_friends_taste_labels.get(j).setLayoutData(gui_utils.grid_data_factory(280, 20, -1, 3, -1, -1, -1, -1));
			movies_friends_taste_labels.get(j).addMouseListener(new MouseAdapter() {
			//	@Override
				public void mouseUp(MouseEvent arg0) {
					movie_details_window  movie_details= null;
				try {
					movie_details =new movie_details_window( display ,movies_freinds_taste_entity_for_annonymus.get(k).get_movie_id());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					movie_details.open();
				
				}

			});
			

			if(j == 4)
				break;
		}
		
		
//		try {
//			movies_friends_taste =log_in_window.user.get_user_recommended_movies_entities_by_friends(log_in_window.user.get_current_user_id(), 5);
//		} catch (SQLException e1) {
//			gui_utils.raise_sql_error_window(display);
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		//just for check
////		movies_friends_taste = new ArrayList<String>();
////		movies_friends_taste.add("reco by friends taste1");
////		movies_friends_taste.add("reco by friends taste2");
////		movies_friends_taste.add("reco by friends taste3");
////		movies_friends_taste.add("reco by friends taste4");
//	//	movies_friends_taste.add("reco by friends taste5");
//		//
//		
//		i = 0;
//		final List<light_entity_movie> movies_friends_taste_for_anonymus = movies_friends_taste;
//		for(j=0;j<movies_friends_taste.size();j++)
//		{
//			final int k =j;
//			movies_friends_taste_labels.add(new Label(area2, SWT.BORDER));
//			movies_friends_taste_labels.get(j).setText(movies_friends_taste.get(j));
//			movies_friends_taste_labels.get(j).setLayoutData(gui_utils.grid_data_factory(200, 18, -1, 3, -1, -1, -1, -1));
//			
//			movies_friends_taste_labels.get(j).addMouseListener(new MouseAdapter() {
//			//	@Override
//				public void mouseUp(MouseEvent arg0) {
//					System.out.println("hi-2");
//					movie_details_window  movie_details= null;
//					try {
//						movie_details =new movie_details_window( display ,movies_friends_taste.get(k).get_movie_id());
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//						movie_details.open();
//					
//					}
//				
//
//			});
//			
//			i++;
//		}
//		
		
		//top rated area area
		Composite area3 = new Composite(this, SWT.NONE);
		area3.setLayoutData(gui_utils.form_data_factory(295, 185, 40, 310));
		GridLayout grid_layout_area3 = new GridLayout(1, false);
		area3.setLayout(grid_layout_area3);
		
		
		
		//top rated headline
		Label area3_headline = new Label(area3, SWT.NONE);
		area3_headline.setText("Top Rated Movies");
		area3_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1, -1, -1));
		final Font font_area3_headline = new Font(display, "Ariel",10, SWT.BOLD ); 
		area3_headline.setFont(font_area3_headline);
		area3_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_area3_headline.dispose();
			}		
		});
		
		
		//based on similar to me LINKS
		
		//movies_top_rated = get_top_rated_movies(); //to be used when function exists
		
		//just for check
		movies_top_rated = new ArrayList<String>();
		movies_top_rated.add("top rated movie 1");
		movies_top_rated.add("top rated movie 2 and some more shit");
		movies_top_rated.add("top rated movie 3 and some more shit");
		movies_top_rated.add("top rated movie 4 and some more shit");
		movies_top_rated.add("top rated movie 5 and some more shit");

		
		i = 0;
		for(String str: movies_top_rated)
		{
			movies_top_rated_labels.add(new Label(area3, SWT.NONE));
			movies_top_rated_labels.get(i).setText(str);
			movies_top_rated_labels.get(i).setFont(font_based_on_learned_about_you);
			movies_top_rated_labels.get(i).setLayoutData(gui_utils.grid_data_factory(280, 20, -1, 3, -1, -1, -1, -1));
			
			movies_top_rated_labels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent arg0) {
				System.out.println("hi-3");

				}

			});

			
			i++;
			if(i == 5)
				break;
		}
		
		
	}
}
