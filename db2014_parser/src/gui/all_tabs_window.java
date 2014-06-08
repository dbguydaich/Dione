package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import parser_entities.light_entity_movie;
import bl.user_logics;
import config.config;


/**
 * All Tabs Window
 * This is the main window
 */
public class all_tabs_window extends abstract_window {

	config config = new config();
	int window_height = config.get_window_height();
	int window_width = config.get_window_width();
	List<String> user_activities_strings;
	List<light_entity_movie> movies_my_taste_entity;
	List<String> user_tags_string;

	public all_tabs_window(final Display display) {
	
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		this.setSize(window_width, window_height);
		String my_name = null;
		
		try {
			 my_name = log_in_window.user.get_my_name(); /* get current logged in username */
		} catch (SQLException e2) {
			gui_utils.raise_sql_error_window(display);
		}
	
		this.setText("Dione - Logged in As: " +  my_name); /* window title */
		this.setLayout(new FillLayout()); /* window layout */
		final TabFolder tab_folder = new TabFolder(this, SWT.NONE); /* the window consists of tabs */

		//tab1
		final TabItem tab1 = new TabItem(tab_folder, SWT.NONE);
		tab1.setText("Overview");

		// tab2
		TabItem tab2 = new TabItem(tab_folder, SWT.NONE);
		tab2.setText("Search Movie");

		// tab3
		TabItem tab3 = new TabItem(tab_folder, SWT.NONE);
		tab3.setText("Social Zone");

		// tab4
		TabItem tab4 = new TabItem(tab_folder, SWT.NONE);
		tab4.setText("Reccomended Movies");

		// tab5
		TabItem tab5 = new TabItem(tab_folder, SWT.NONE);
		tab5.setText("Settings");
			
		
		/* getting data from the db to the first tab */
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					/* get user recommended movies */
					movies_my_taste_entity = 
							user_logics.get_user_recommended_movies(log_in_window.user.get_current_user_id());
				} catch (SQLException e1) {
					gui_utils.raise_sql_error_window(display);

					e1.printStackTrace();
				}

				try {
					/* get user preferred tags */
					user_tags_string = log_in_window.user
							.get_user_popular_tags();
				} catch (SQLException e1) {
					user_tags_string = new ArrayList<String>();
					gui_utils.raise_sql_error_window(display);
					e1.printStackTrace();
				} 

				try {
					/* recent user activities */
					user_activities_strings = user_logics.get_user_recent_string_activities(
							log_in_window.user.get_current_user_id(), 6);

				} catch (SQLException e1) {
					user_activities_strings = new ArrayList<String>();
					gui_utils.raise_sql_error_window(display);
					e1.printStackTrace();
				}
				
				/* updating the gui thread  */
				display.asyncExec(new Runnable() {
					public void run() {
						/* opening the overview tab */
						overview_tab my_overview_tab = new overview_tab(
								display, tab_folder, SWT.NONE,
								movies_my_taste_entity, user_tags_string,
								user_activities_strings);
						tab1.setControl(my_overview_tab);
					}
				});
			}
		});

		
		gui_utils.executor.execute(t); /* executer will run the thread */

		/* search movie tab */
		search_movie_tab my_search_movie_tab = new search_movie_tab(display,
				tab_folder, SWT.NONE);
		tab2.setControl(my_search_movie_tab);

		/* social tab */
		social_tab my_social_tab = new social_tab(display, tab_folder, SWT.NONE);
		tab3.setControl(my_social_tab);

		/* recommendation tab */
		recommendation_tab my_recommendation_tab = new recommendation_tab(
				display, tab_folder, SWT.NONE);
		tab4.setControl(my_recommendation_tab);

		/* setting tab */
		settings_tab my_settings_tab = new settings_tab(display, tab_folder,
				SWT.NONE);
		tab5.setControl(my_settings_tab);

		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				for (movie_details_window win : gui_utils.movie_windows) { /* close all opened movie windows */
					if (!win.isDisposed())
						win.dispose();
				}
				if (gui_utils.pref_win != null) /* if pref_win exists */
					if (!gui_utils.pref_win.isDisposed()) { /* and opened */
						gui_utils.EXIT_ON_LOGIN = false;
						gui_utils.pref_win.dispose();
					}
				if (gui_utils.EXIT_ON_LOGIN == true) { /* EXIT */
					display.dispose();
					gui_utils.exist_threads();
				}
				else
					gui_utils.EXIT_ON_LOGIN = true;
			}
		});
	}


}