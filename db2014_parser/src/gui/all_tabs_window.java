package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import parser_entities.light_entity_movie;
import bl.user_logics;
import config.config;

public class all_tabs_window extends Shell {

	config config = new config();
	int window_height = config.get_window_height();
	int window_width = config.get_window_width();
	List<String> user_activities_strings;
	List<light_entity_movie> movies_my_taste_entity;
	List<String> user_tags_string;

	public all_tabs_window(final Display display) {
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));

		this.setSize(window_width, window_height);

		System.out.println("tabs win creating...");

		// String currnt_user_str = get_current_username();
		String current_user_str = "some username";
		this.setText("MovieBook - Logged in As: " + current_user_str);

		this.setLayout(new FillLayout());
		final TabFolder tab_folder = new TabFolder(this, SWT.NONE);

		// tab1
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
		
		
		
		
		
		//////getting data for tab 1/////
		Thread t = new Thread(new Runnable() {

			public void run() {

				// ////new/////

				List<String> movies_my_taste = null;
				try {
					movies_my_taste_entity = user_logics
							.get_user_recommended_movies(log_in_window.user
									.get_current_user_id());
					movies_my_taste = gui_utils
							.convert_movies_entity_to_string(movies_my_taste_entity);
				} catch (SQLException e1) {
					movies_my_taste = new ArrayList<String>();
					// TODO Auto-generated catch block
					gui_utils.raise_sql_error_window(display);

					e1.printStackTrace();
				}

				// taste_tags
				//

				try {
					user_tags_string = log_in_window.user
							.get_user_popular_tags();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					user_tags_string = new ArrayList<String>();
					gui_utils.raise_sql_error_window(display);
					e1.printStackTrace();
				} // to be used when function exists

				// user recent activities labels

				try {
					user_activities_strings = user_logics.get_user_recent_string_activities(
							log_in_window.user.get_current_user_id(), 6);

				} catch (SQLException e1) {
					user_activities_strings = new ArrayList<String>();
					gui_utils.raise_sql_error_window(display);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				display.asyncExec(new Runnable() {

					public void run() {

						overview_tab my_overview_tab = new overview_tab(
								display, tab_folder, SWT.NONE,
								movies_my_taste_entity, user_tags_string,
								user_activities_strings);
						tab1.setControl(my_overview_tab);

					}

				});

			}

		});

		gui_utils.executor.execute(t);

		search_movie_tab my_search_movie_tab = new search_movie_tab(display,
				tab_folder, SWT.NONE);
		tab2.setControl(my_search_movie_tab);

		social_tab my_social_tab = new social_tab(display, tab_folder, SWT.NONE);
		tab3.setControl(my_social_tab);

		recommendation_tab my_recommendation_tab = new recommendation_tab(
				display, tab_folder, SWT.NONE);
		tab4.setControl(my_recommendation_tab);

		settings_tab my_settings_tab = new settings_tab(display, tab_folder,
				SWT.NONE);
		tab5.setControl(my_settings_tab);

		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				for (movie_details_window win : gui_utils.movie_windows) {
					if (!win.isDisposed())
						win.dispose();
				}

				if (gui_utils.pref_win != null)
					if (!gui_utils.pref_win.isDisposed()) {
						gui_utils.EXIT_ON_LOGIN = false;
						gui_utils.pref_win.dispose();
					}
				if (gui_utils.EXIT_ON_LOGIN == true) {
					display.dispose();
					// shachar: app is exiting here
				}

				else
					gui_utils.EXIT_ON_LOGIN = true;
			}
		});
	}

	protected void checkSubclass() {
	}

}