package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import parser_entities.light_entity_movie;

/**
 * Preferences Window
 */
public class preferences_window extends abstract_window {

	Label movie_label;
	boolean can_be_opened = true;
	light_entity_movie current_movie = null;

	public preferences_window(final Display display) {
		super(display, SWT.MIN);

		movie_label = new Label(this, SWT.NONE);

		// window background
		String imgURL = ".\\src\\gui\\images\\blue_400_300.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		this.addDisposeListener(new DisposeListener() {
			/*
			 * Possible Scenarios: 1. user has been rated movies for the first
			 * time - nothing is open, so we exit 2. user has been reted movies
			 * on demand (all tabs still open) - go back
			 */
			public void widgetDisposed(DisposeEvent e) {
				
				if (gui_utils.tabs_win == null) {
					gui_utils.tabs_win = new all_tabs_window(gui_utils.display);
					gui_utils.tabs_win.open();
				}

				else if (gui_utils.tabs_win.isDisposed()) {
					gui_utils.tabs_win = new all_tabs_window(gui_utils.display);
					gui_utils.tabs_win.open();
				}
			}
		});
		update_movie(); /* rate */

		if (can_be_opened == false) { /* there are no movies to rate */
			return;
		}
		this.setSize(400, 300);

		String my_name = null;
		try {
			 my_name = log_in_window.user.get_my_name();
		} catch (SQLException e2) {
			gui_utils.raise_sql_error_window(display);
		}
		this.setText("Movie Preferences - Logged in As: " +  my_name);
		this.setLayout(new FormLayout());

		
		// headline label
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Please Rate The Following Movies:");
		headline_label.setLayoutData(gui_utils.form_data_factory(360, 30, 10,
				20));
		final Font font_headline_label = new Font(display, "Ariel", 17,
				SWT.NONE);
		headline_label.setFont(font_headline_label);
		headline_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_headline_label.dispose();
			}
		});

		try {
			current_movie = log_in_window.user.get_unrated_movie();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// movie label
		movie_label.setAlignment(SWT.CENTER);
		movie_label.setText(current_movie.get_movie_name());
		movie_label.setLayoutData(gui_utils.form_data_factory(370, 22, 70, 10));
		final Font font_movie_label = new Font(display, "Ariel", 14, SWT.NONE);
		movie_label.setFont(font_movie_label);
		/* Disposal Listener */
		movie_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_movie_label.dispose();
			}
		});

		//area
		Composite radios_area = new Composite(this, SWT.NONE);
		radios_area.setLayoutData(gui_utils
				.form_data_factory(100, 143, 100, 90));
		GridLayout grid_layout_area = new GridLayout(1, false);
		radios_area.setLayout(grid_layout_area);

		// radios
		final List<Button> radios = new ArrayList<Button>();

		for (int i = 0; i < 6; i++) {
			radios.add(new Button(radios_area, SWT.RADIO));
			if (i == 1)
				radios.get(i).setLayoutData(
						gui_utils.grid_data_factory(-1, 10, -1, -1, -1, -1));
		}

		radios.get(0).setText("Don't Know");
		radios.get(1).setText("1 - Lowest");
		radios.get(2).setText("2");
		radios.get(3).setText("3");
		radios.get(4).setText("4");
		radios.get(5).setText("5 - Highest");

		// rate button
		Button rate_button = new Button(this, SWT.PUSH);
		rate_button.setText("Rate");
		rate_button
				.setLayoutData(gui_utils.form_data_factory(50, 30, 160, 200));
		final Font font_rate_button = new Font(display, "Ariel", 13, SWT.NONE);
		rate_button.setFont(font_rate_button);
		rate_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_rate_button.dispose();
			}
		});

		/* rate action */
		rate_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					int rate_desired = gui_utils.get_index_button(radios);
					handle_rating(rate_desired); /* threads are initialized within this function */
					update_movie(); /* get next movie to rate */
					if (current_movie==null)
					{
						gui_utils.pref_win.dispose();
					}
				} catch (NumberFormatException e) {
					gui_utils.raise_sql_error_window(display);
					e.printStackTrace();
				}

			}

		});

		// stop button
		Button stop_button = new Button(this, SWT.PUSH);
		stop_button.setText("Stop Rating");
		stop_button.setLayoutData(gui_utils
				.form_data_factory(100, 30, 160, 280));
		final Font font_stop_button = new Font(display, "Ariel", 13, SWT.NONE);
		stop_button.setFont(font_stop_button);
		stop_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_stop_button.dispose();

			}
		});


		/*
		 * Stop Listener:
		 * simply disposing the win
		 */
		stop_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				gui_utils.pref_win.dispose();
			}
		});
	}

	
	/**
	 * rating the movie with rate
	 * @param rate
	 */
	public void handle_rating(final int rate) {
		/* new thread to access the db */
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					log_in_window.user.rate_movie(current_movie.get_movie_id(),
							rate);
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								log_in_window.user.fill_user_prefence();
							} catch (SQLException e) {
							}
						}
					});

					gui_utils.executor.execute(t);

				} catch (final SQLException e) {

					gui_utils.display.asyncExec(new Runnable() {

						public void run() {
							gui_utils.raise_sql_error_window(gui_utils.display);
							e.printStackTrace();
						}
					});
				}
			}

		});
		gui_utils.executor.execute(t);
		
	}

	/**
	 * getting the next movie to rate
	 */
	public void update_movie() 
	{
		/* a new thread to access the db */
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					current_movie = log_in_window.user.get_unrated_movie();
					if (current_movie == null) {
						// go_to_overview();
						can_be_opened = false;
						return;
						
					}
					gui_utils.display.asyncExec(new Runnable() {
						public void run() {
							movie_label.setText(current_movie.get_movie_name());
						}
					});
				} catch (final SQLException e) {

					gui_utils.display.asyncExec(new Runnable() {
						public void run() {
							gui_utils.raise_sql_error_window(gui_utils.display);
							e.printStackTrace();
						}
					});
				}
			}
		});

		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
		
		}
	}


}