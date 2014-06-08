package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import parser_entities.light_entity_movie;


/**
 *Overview Tab
 */
public class overview_tab extends Composite
{

	public overview_tab(final Display display, Composite parent, int style, List<light_entity_movie> movies_my_taste_entity,
			List<String> user_tags_string, List<String> user_activities_strings)
	{
		super(parent, style);
		
		List<Label> user_tags_labels = new ArrayList<Label>();
		List<Label> reco_movies_labels = new ArrayList<Label>();
		List<Label> user_activities_labels = new ArrayList<Label>();
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		
		/* window background */
		String imgURL = ".\\src\\gui\\images\\blue_640_480_3.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				background.dispose();
			}		
		});
	
		
		//recommendation area
		Composite reco_area = new Composite(this, SWT.NONE);
		reco_area.setLayoutData(gui_utils.form_data_factory(295, 200, 5, 10));
		GridLayout grid_layout_reco_area = new GridLayout(1, false);
		reco_area.setLayout(grid_layout_reco_area);
		
		//recommendation headline
		Label reco_headline = new Label(reco_area, SWT.NONE);
		reco_headline.setText("Recommended Movies For You");
		reco_headline.setLayoutData(gui_utils.grid_data_factory(-1, 0, -1, -1, -1, -1));
		final Font font_reco_headline = new Font(display, "Ariel",13, SWT.BOLD );
		final Font font_movie_label = new Font(display, "Ariel", 12, SWT.NONE);
		reco_headline.setFont(font_reco_headline);
		
		/* Disposal Listener */
		reco_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_reco_headline.dispose();
				font_movie_label.dispose();
			}		
		});
		
		/* recommended movies by user taste */ 
		List<String> movies_my_taste = null;
		movies_my_taste = gui_utils.convert_movies_entity_to_string(movies_my_taste_entity);
		int i = 0;
		int j=0;
		final List<light_entity_movie> movies_my_taste_entity_for_annonymus = movies_my_taste_entity;
		for(j=0; j< movies_my_taste.size(); j++)
		{
			final int k =j;
			reco_movies_labels.add(new Label(reco_area, SWT.NONE));
			reco_movies_labels.get(j).setText(movies_my_taste.get(j));
			reco_movies_labels.get(j).setFont(font_movie_label);
			reco_movies_labels.get(j).setLayoutData(gui_utils.grid_data_factory(280, 20, -1, 3, -1, -1, -1, -1));
			reco_movies_labels.get(j).addMouseListener(new MouseAdapter() {
				public void mouseUp(MouseEvent arg0) {
					movie_details_window  movie_details= null;
				try {
					movie_details =new movie_details_window( display ,movies_my_taste_entity_for_annonymus.get(k).get_movie_id());
				} catch (SQLException e) {
					e.printStackTrace();
				}
					movie_details.open();
				}
			});
			
			i++;
		}
		
		//recommendation buttom label
		Label reco_bottom_label = new Label(reco_area, SWT.NONE);
		reco_bottom_label.setText("Click on a Movie Name For Movie Details");
		reco_bottom_label.setLayoutData(gui_utils.grid_data_factory(-1, 0, -1, -1, -1, -1));
		final Font font_reco_bottom_label = new Font(display, "Ariel",10, SWT.NONE );
		reco_bottom_label.setFont(font_reco_bottom_label);
		
		/* Disposal Listener */
		reco_bottom_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_reco_bottom_label.dispose();
			}		
		});
		
		//taste area
		Composite taste_area = new Composite(this, SWT.NONE);
		taste_area.setLayoutData(gui_utils.form_data_factory(305, 160, 5, 314));	
		GridLayout grid_layout_taste_area = new GridLayout(1, false);
		taste_area.setLayout(grid_layout_taste_area);
		
		//taste_headline
		Label taste_headline = new Label(taste_area, SWT.NONE);
		taste_headline.setText("We Found Out You Like Movies Tagged As:");
		taste_headline.setLayoutData(gui_utils.grid_data_factory(-1, 2, -1, -1, -1, -1));
		final Font font_taste_headline = new Font(display, "Ariel",10 ,SWT.BOLD);
		taste_headline.setFont(font_taste_headline);
		
		/* Disposal Listener */
		taste_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_taste_headline.dispose();
			}		
		});

		
		/* user tags taste */
		final Font font_user_tags_label = new Font(display, "Ariel", 12, SWT.NONE);
		i = 0;
		for(String str: user_tags_string)
		{
			user_tags_labels.add(new Label(taste_area, SWT.NONE));
			user_tags_labels.get(i).setText(str);
			user_tags_labels.get(i).setFont(font_user_tags_label);
			if(i == 0)
			{
				/* Disposal Listener */
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
		
		if(i == 0) /* if no label was created, dispose the font */
			font_user_tags_label.dispose(); 
		
		//user recent activity area
		Composite user_activity_area = new Composite(this, SWT.NONE);
		user_activity_area.setLayoutData(gui_utils.form_data_factory(600, 185, 220, 10));
		GridLayout grid_layout_user_activity_area = new GridLayout(1, false);
		user_activity_area.setLayout(grid_layout_user_activity_area);
		
		//user recent activity headline
		Label user_activity_headline = new Label(user_activity_area, SWT.NONE);
		user_activity_headline.setText("Your Recent Activity");
		user_activity_headline.setLayoutData(gui_utils.grid_data_factory(190, 0, -1, -1, -1, -1));
		final Font font_user_activity_headline = new Font(display, "Ariel",15, SWT.BOLD );
		user_activity_headline.setFont(font_user_activity_headline);
		
		/* Disposal Listener */
		user_activity_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_user_activity_headline.dispose();
			}		
		});
				
		
		/* user recent activities */
		final Font font_user_activities_labels = new Font(display, "Ariel", 9, SWT.NONE);
		i = 0;
		for(String str: user_activities_strings)
		{
			user_activities_labels.add(new Label(user_activity_area, SWT.NONE));
			user_activities_labels.get(i).setText(str);
			if (i == 0)
			{
				/* Disposal Listener */
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
		
		if(i == 0) /* if no label was created, dispose the font */
			font_user_activities_labels.dispose();
	}

}
