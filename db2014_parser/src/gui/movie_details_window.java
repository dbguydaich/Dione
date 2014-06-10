package gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import bl.movie_logics;
import bl.user_logics;
import parser_entities.light_entity_movie;
import config.config;

/**
 * Movie Window
 */
public class movie_details_window extends abstract_window {
	config config = new config();
	int window_height = config.get_window_height();
	int window_width = config.get_window_width();
	int movie_id_number;

	movie_comments_window recent_comments_win = null;
	comment_window comment_win = null;

	/* movie data */
	light_entity_movie movie;
	String movie_name;
	List<String> genres;
	String movie_plot;
	String director_name;
	String language;
	String year;
	List<String> stars;
	String rating_str;
	String wiki_str;
	List<String> tags;

	public movie_details_window(final Display display, final int movie_id)
			throws SQLException {

		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		

		/* Getting the movie data in a new thread */
		Thread t = new Thread(new Runnable() {
			public void run() {
				light_entity_movie movie = null;
				try {
					/* getting the data */
					movie = movie_logics.get_movie_details(movie_id);
					genres = movie_logics.get_movie_genres(movie.get_movie_id());
					director_name = movie.get_movie_director();
					movie_name = movie.get_movie_name();
					movie_plot = (movie.get_movie_plot());
					language = movie.get_movie_language();
					year = Integer.toString(movie.get_movie_year());
					stars = gui_utils.convert_person_string(movie
							.get_movie_actors());
					rating_str = String.valueOf(movie.get_movie_rating());
					wiki_str = movie.get_movie_wikipedia_url();
					tags = (List<String>) movie.get_movie_tags();

				} catch (final SQLException e) {
					display.asyncExec(new Runnable() {
						public void run() {
							gui_utils.raise_sql_error_window(gui_utils.display);
							return;
						}

					});

				}
			}

		});

		t.start();
		try {
			t.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
	
		}

		movie_id_number = movie_id;
		gui_utils.movie_windows.add(this);
		this.setLayout(new FormLayout());
		this.setSize(window_width + 100, window_height);

		String my_name = null;
		try {
			my_name = log_in_window.user.get_my_name();
		} catch (SQLException e2) {
			gui_utils.raise_sql_error_window(gui_utils.display);
		}
		this.setText("Movie Details - Logged in As: " + my_name);

		/* window background */
		String imgURL = ".\\src\\gui\\images\\blue_740_480.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.setLayout(new FormLayout());

		final Font font_ariel_11 = new Font(display, "Ariel", 10, SWT.NONE);

		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				background.dispose();
				font_ariel_11.dispose();

				if (comment_win != null) {
					if (!comment_win.isDisposed()) {
						comment_win.dispose();
					}
				}

				if (recent_comments_win != null) {
					if (!recent_comments_win.isDisposed()) {
						recent_comments_win.dispose();
					}
				}

			}
		});

		List<Label> movie_details_labels = new ArrayList<Label>();

		// headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText(movie_name);
		headline_label.setLayoutData(gui_utils
				.form_data_factory(-1, -1, 10, 10));
		headline_label.setAlignment(SWT.CENTER);
		final Font font1 = new Font(display, "Ariel", 15, SWT.BOLD);
		headline_label.setFont(font1);

		/* Disposal Listener */
		headline_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font1.dispose();
			}
		});

		// left area
		Composite left_area = new Composite(this, SWT.NONE);
		left_area.setLayoutData(gui_utils.form_data_factory(390, 195, 60, 10));
		GridLayout grid_layout_area = new GridLayout(2, false);
		left_area.setLayout(grid_layout_area);

		// all left labels
		for (int i = 0; i < 12; i++) {
			movie_details_labels.add(new Label(left_area, SWT.NONE));
			if (i > 1) {
				GridData my_grid_data = new GridData();
				my_grid_data.verticalIndent = 5;
				movie_details_labels.get(i).setLayoutData(
						gui_utils.grid_data_factory(-1, 5, -1, -1, -1, -1));
			}
			movie_details_labels.get(i).setFont(font_ariel_11);
		}

		// genres labels
		movie_details_labels.get(0).setText("Genres:");
		String genres_str = "";
		int i = 0;
		for (String str : genres) {
			genres_str = genres_str + str + ", ";

			i++;
			if (i == 3)
				break;
		}

		if (genres_str.length() >= 2)
			genres_str = genres_str.substring(0, genres_str.length() - 2);

		movie_details_labels.get(1).setText(genres_str);

		// director1
		movie_details_labels.get(2).setText("Director:");

		// director2
		movie_details_labels.get(3).setText(director_name);

		// language1
		movie_details_labels.get(4).setText("Language:");

		// language2
		movie_details_labels.get(5).setText(language);

		// year1
		movie_details_labels.get(6).setText("Year:");

		// year2
		movie_details_labels.get(7).setText(year);

		// stars1
		movie_details_labels.get(10).setText("Stars:");

		// stars2
		String stars_str = "";
		for (String str : stars)
			stars_str = stars_str + str + ", ";

		if (stars_str.length() >= 2)
			stars_str = stars_str.substring(0, stars_str.length() - 2);

		movie_details_labels.get(11).setText(stars_str);

		// rating1
		movie_details_labels.get(8).setText("Rating(1-5):");

		// rating2
		movie_details_labels.get(9).setText(rating_str);

		// wiki label
		Label wiki_label = new Label(left_area, SWT.H_SCROLL);
		wiki_label.setText("Wiki Link:\n(clickable)");
		wiki_label.setLayoutData(gui_utils.grid_data_factory(-1, 5, -1, -1, -1,
				-1));
		wiki_label.setFont(font_ariel_11);

		// wiki url
		Label wiki_url = new Label(left_area, SWT.NONE);
		wiki_url.setText(wiki_str);
		final Color blue_color = new Color(display, 0, 0, 255);
		wiki_url.setLayoutData(gui_utils.grid_data_factory(-1, 5, -1, -1, -1,
				-1));
		final Font font_wiki_url = new Font(display, "Ariel", 10,
				SWT.UNDERLINE_DOUBLE);
		wiki_url.setFont(font_wiki_url);
		wiki_url.setForeground(blue_color);

		/* Disposal Listener */
		wiki_url.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_wiki_url.dispose();
				blue_color.dispose();
			}
		});

		/* wiki click action */
		wiki_url.addMouseListener(new MouseAdapter() {
			// @Override
			public void mouseUp(MouseEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URI(wiki_str)); /*
																	 * open wiki
																	 * page on
																	 * default
																	 * browser
																	 */

				} catch (IOException e) {
					MessageBox messageBox = new MessageBox(display
							.getActiveShell(), SWT.ICON_ERROR);
					messageBox.setText("Failure");
					messageBox.setMessage("Couldn't open the desired link.");
					messageBox.open();

				} catch (URISyntaxException e) {
					MessageBox messageBox = new MessageBox(display
							.getActiveShell(), SWT.ICON_ERROR);
					messageBox.setText("Failure");
					messageBox.setMessage("Couldn't open the desired link.");
					messageBox.open();
				}
			}
		});

		// tags area
		Composite tags_area = new Composite(this, SWT.NONE);
		tags_area.setLayoutData(gui_utils.form_data_factory(320, 250, 45, 405));
		GridLayout grid_layout_area2 = new GridLayout(2, false);
		tags_area.setLayout(grid_layout_area2);

		// headline tags
		Label headline_tags = new Label(tags_area, SWT.NONE);
		headline_tags.setText("Tags");
		headline_tags.setLayoutData(gui_utils.grid_data_factory(95, -1, 2, -1,
				-1, -1));
		final Font font_tags = new Font(display, "Ariel", 12, SWT.NONE);
		headline_tags.setFont(font_tags);
		headline_tags.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_tags.dispose();
			}
		});

		// tags labels and radios
		List<Label> tags_labels = new ArrayList<Label>();
		final List<List<Button>> radios_lists = new ArrayList<List<Button>>();
		List<Composite> radios_areas = new ArrayList<Composite>();

		int count = 0;
		for (String str : tags) {
			if (count > 4)
				break;
			tags_labels.add(new Label(tags_area, SWT.NONE));
			tags_labels.get(count).setText(str);
			radios_areas.add(new Composite(tags_area, SWT.NONE));
			GridLayout grid_layout_area3 = new GridLayout(5, false);
			radios_areas.get(count).setLayout(grid_layout_area3);

			radios_lists.add(new ArrayList<Button>());
			for (int k = 0; k < 5; k++) {

				Button radio = new Button(radios_areas.get(count), SWT.RADIO);
				if (k == 2)
					radio.setSelection(true);
				String temp_str = "" + (k + 1);
				radio.setText(temp_str);
				radios_lists.get(count).add(radio);
			}
			count++;
		}

		/*
		 * if the movie has now tags related with it, set the tags area to be
		 * invisible
		 */
		if (count == 0)
			tags_area.setVisible(false);

		// tags rate button
		Button tags_button = new Button(tags_area, SWT.PUSH);
		tags_button.setText("Rate");
		tags_button.setLayoutData(gui_utils.grid_data_factory(95, -1, 2, -1,
				-1, -1));

		/* tags rate button */
		tags_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {

				/* new thread to access the db */
				Thread t = new Thread(new Runnable() {
					public void run() {
						display.asyncExec(new Runnable() {
							int rate_number;
							boolean success = true;

							public void run() {
								for (int i = 0; i < radios_lists.size(); i++) {
									rate_number = gui_utils
											.get_index_button(radios_lists
													.get(i)) + 1;
									try {
										if (user_logics.rate_tag_movie(
												movie_id_number,
												log_in_window.user
														.get_current_user_id(),
												tags.get(i), rate_number) == false)

										{
											success = false;
										}

										Thread t3 = new Thread(new Runnable() {

											public void run() {
												try {
													log_in_window.user
															.fill_user_prefence(); /* rating */
												} catch (SQLException e) {

												}
											}

										});
										gui_utils.executor.execute(t3);

									} catch (SQLException e) {
										gui_utils
												.raise_sql_error_window(gui_utils.display);
										return;
									}
								}
								if (success) {
									MessageBox messageBox = new MessageBox(
											display.getActiveShell(),
											SWT.ICON_WORKING);
									messageBox.setText("SUCCESS");
									messageBox
											.setMessage("Movie-Tag has been rated succesfully!");
									messageBox.open();
								}
							}

						});

					}

				});
				gui_utils.executor.execute(t);

			}

		});

		// tags bottom label
		Label tags_bottom_label = new Label(tags_area, SWT.NONE);
		tags_bottom_label
				.setText("Please rate these tags for the movie \n(1=disagree, 5=agree)");
		tags_bottom_label.setLayoutData(gui_utils.grid_data_factory(-1, -1, 2,
				-1, -1, -1));
		final Font font_bottom_tags = new Font(display, "Ariel", 8, SWT.NONE);
		tags_bottom_label.setFont(font_bottom_tags);

		/* Disposal Listener */
		tags_bottom_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_bottom_tags.dispose();
			}
		});

		// plot headline
		Label plot_headline_label = new Label(this, SWT.NONE);
		plot_headline_label.setLayoutData(gui_utils.form_data_factory(32, 20,
				260, 10));
		plot_headline_label.setText("Plot:");
		final Font font_plot_headline_label = new Font(display, "Ariel", 11,
				SWT.NONE);
		plot_headline_label.setFont(font_plot_headline_label);

		/* Disposal Listener */
		plot_headline_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_plot_headline_label.dispose();
			}
		});

		// plot scroller
		ScrolledComposite scroller_plot = new ScrolledComposite(this,
				SWT.V_SCROLL);
		FormData form_data_scroller_plot = new FormData(290, 100);
		form_data_scroller_plot.top = new FormAttachment(0, 260);
		form_data_scroller_plot.left = new FormAttachment(0, 53);
		scroller_plot.setLayoutData(gui_utils.form_data_factory(290, 100, 260,
				53));

		scroller_plot.setExpandHorizontal(true);
		scroller_plot.setExpandVertical(true);
		scroller_plot.setAlwaysShowScrollBars(true);
		scroller_plot.setMinWidth(100);
		scroller_plot.setMinHeight(260);

		// plot label
		Label plot_label = new Label(scroller_plot, SWT.WRAP);
		plot_label.setText(movie_plot);
		scroller_plot.setContent(plot_label);

		// movie rate area
		Composite rate_movie_area = new Composite(this, SWT.NONE);
		rate_movie_area.setLayoutData(gui_utils.form_data_factory(300, 60, 370,
				50));
		GridLayout grid_layout_rate_movie_area = new GridLayout(6, false);
		rate_movie_area.setLayout(grid_layout_rate_movie_area);

		// movie rate label
		Label movie_rate_label = new Label(rate_movie_area, SWT.NONE);
		movie_rate_label.setLayoutData(gui_utils.grid_data_factory(0, 0, 6, -1,
				-1, -1));
		movie_rate_label.setText("Rate This Movie (1=lowest, 5=highest)");
		final Font font_movie_rate_label = new Font(display, "Ariel", 13,
				SWT.NONE);
		movie_rate_label.setFont(font_movie_rate_label);

		/* Disposal Listener */
		movie_rate_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_movie_rate_label.dispose();
			}
		});

		// movie rate radios
		final List<Button> movie_rate_radios = new ArrayList<Button>();
		for (i = 0; i < 5; i++) {
			movie_rate_radios.add(new Button(rate_movie_area, SWT.RADIO));
			movie_rate_radios.get(i).setText("" + (i + 1));

			if (i == 2)
				movie_rate_radios.get(i).setSelection(true);
		}

		// movie rate button
		Button movie_rate_button = new Button(rate_movie_area, SWT.PUSH);
		movie_rate_button.setText("Rate Movie");
		movie_rate_button.setLayoutData(gui_utils.grid_data_factory(80, 30, 10,
				-1, -1, -1, -1, -1));
		final Font font_movie_rate_button = new Font(display, "Ariel", 11,
				SWT.NONE);
		movie_rate_button.setFont(font_movie_rate_button);

		/* Disposal Listener */
		movie_rate_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_movie_rate_button.dispose();
			}
		});

		/* rate movie action */
		movie_rate_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				/* a new thread to access the db */
				Thread t = new Thread(new Runnable() {
					public void run() {
						display.asyncExec(new Runnable() {
							public void run() {
								final int rate_number = gui_utils
										.get_index_button(movie_rate_radios) + 1;
								boolean success = true;
								try {
									if (log_in_window.user.rate_movie(
											movie_id_number, rate_number) == false) {
										success = false;
									}
									Thread t2 = new Thread(new Runnable() {

										public void run() {
											try {
												log_in_window.user
														.fill_user_prefence();
											} catch (SQLException e) {

											}
										}
									});

									gui_utils.executor.execute(t2);

								} catch (final SQLException e) {

									gui_utils.raise_sql_error_window(gui_utils.display);
									return;
								}

								if (success) {

									MessageBox messageBox = new MessageBox(
											display.getActiveShell(),
											SWT.ICON_WORKING);
									messageBox.setText("SUCCESS");
									messageBox
											.setMessage("Movie has been rated succesfully!");
									messageBox.open();
								}
							}
						});
					}
				});

				gui_utils.executor.execute(t);

			}
		});

		/* make a comment button */
		Button make_comment_button = new Button(this, SWT.PUSH);
		make_comment_button.setText("Make a Comment");
		make_comment_button.setLayoutData(gui_utils.form_data_factory(140, 30,
				410, 530));
		final Font font_comments_button = new Font(display, "Ariel", 11,
				SWT.NONE);
		make_comment_button.setFont(font_comments_button);

		/* Disposal Listener */
		make_comment_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_comments_button.dispose();
			}
		});

		/* make comment action */
		make_comment_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				/* opening comment win, unless it is already open */
				if (comment_win != null) {
					if (!comment_win.isDisposed()) {
						MessageBox messageBox = new MessageBox(
								gui_utils.display.getActiveShell(),
								SWT.ICON_WARNING);
						messageBox.setText("Failure");
						messageBox
								.setMessage("Comment window is already open.");
						messageBox.open();
					} else {
						comment_win = new comment_window(display, movie_id);
						comment_win.open();
					}
				}

				else {
					comment_win = new comment_window(display, movie_id);
					comment_win.open();
				}

			}
		});

		/* recent comments button */
		Button recent_comments_button = new Button(this, SWT.PUSH);
		recent_comments_button.setText("Recent Comments");
		recent_comments_button.setLayoutData(gui_utils.form_data_factory(140,
				30, 410, 370));
		recent_comments_button.setFont(font_comments_button);

		/* recent comments action */
		recent_comments_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				Thread t = new Thread(new Runnable() {

					public void run() {

						display.asyncExec(new Runnable() {

							public void run() {

								List<String> movie_comments;
								/*
								 * opening recent comments win, unless it is
								 * already open
								 */
								if (recent_comments_win != null) {
									if (!recent_comments_win.isDisposed()) {
										MessageBox messageBox = new MessageBox(
												gui_utils.display
														.getActiveShell(),
												SWT.ICON_WARNING);
										messageBox.setText("Failure");
										messageBox
												.setMessage("Movie comments window is already open.");
										messageBox.open();
									} else {

										try {
											movie_comments = user_logics
													.get_movie_notes(movie_id,
															12);

											recent_comments_win = new movie_comments_window(
													display, movie_comments);

											recent_comments_win.open();
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											gui_utils
													.raise_sql_error_window(gui_utils.display);
											return;
										}

									}
								}

								else {

									try {
										movie_comments = user_logics
												.get_movie_notes(movie_id, 6);
										recent_comments_win = new movie_comments_window(
												display, movie_comments);
										recent_comments_win.open();
									} catch (SQLException e) {
									
										gui_utils
												.raise_sql_error_window(gui_utils.display);
										return;
									
									}

								}

							}

						});

					}

				});
				gui_utils.executor.execute(t);

			}
		});

		/* make tag area */
		Composite make_tag_area = new Composite(this, SWT.NONE);
		make_tag_area.setLayoutData(gui_utils.form_data_factory(300, 105, 300,
				405));
		GridLayout grid_layout_make_tag_area = new GridLayout(5, false);
		make_tag_area.setLayout(grid_layout_make_tag_area);

		/* add tag label */
		Label add_tag_label = new Label(make_tag_area, SWT.NONE);
		add_tag_label.setLayoutData(gui_utils.grid_data_factory(25, -1, 5, -1,
				-1, -1));
		add_tag_label.setText("Add a New Tag(1=agree, 5=disagree)");
		final Font font_add_tag_label = new Font(display, "Ariel", 11, SWT.NONE);
		add_tag_label.setFont(font_add_tag_label);

		/* Disposal Listener */
		add_tag_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_add_tag_label.dispose();
			}
		});

		/* add tag text */
		final Text add_tag_text = new Text(make_tag_area, SWT.BORDER);
		add_tag_text.setLayoutData(gui_utils.grid_data_factory(80, 15, 93, -1,
				5, -1, -1, -1));
		add_tag_text.setTextLimit(15);

		/* add tag radios */
		final List<Button> new_tag_radios = new ArrayList<Button>();
		for (i = 0; i < 5; i++) {
			new_tag_radios.add(new Button(make_tag_area, SWT.RADIO));
			new_tag_radios.get(i).setText("" + (i + 1));
			if (i == 0) {
				new_tag_radios.get(i).setLayoutData(
						gui_utils.grid_data_factory(60, -1, -1, -1, -1, -1));
			}
		}
		new_tag_radios.get(2).setSelection(true);

		/* add tag button */
		Button add_tag_button = new Button(make_tag_area, SWT.PUSH);
		add_tag_button.setLayoutData(gui_utils.grid_data_factory(115, -1, 5,
				-1, -1, -1));
		add_tag_button.setText("Add");
		final Font font_add_tag_button = new Font(display, "Ariel", 11,
				SWT.NONE);
		add_tag_button.setFont(font_add_tag_button);

		add_tag_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {

				Thread t = new Thread(new Runnable() {

					public void run() {

						display.syncExec(new Runnable() {

							public void run() {

								int rate = gui_utils
										.get_index_button(new_tag_radios) +1;
								String tag_name = add_tag_text.getText();
								System.out.println(rate);
								System.out.println(tag_name);

								try {
									if(user_logics.rate_tag_movie(movie_id,  // ////matan is this the funciton
											log_in_window.user
													.get_current_user_id(),
											tag_name, rate))
									{
									
													log_in_window.user.fill_user_prefence(); // rating algorithm///
												
										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);

										messageBox.setText("Success");

										messageBox
												.setMessage("New tag added succefully!");

										messageBox.open();
									}
									
									else

									{
										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);

										messageBox.setText("Failure");

										messageBox.setMessage("Couldn't add new tag");

										messageBox.open();

									}
									
								} catch (SQLException e) {
								
									gui_utils.raise_sql_error_window(gui_utils.display);
									e.printStackTrace(); ////remember to remove!!!!!
									return;

								}

							}

						});

					}

				});

				gui_utils.executor.execute(t);

			}
		});

		/* Disposal Listener */
		add_tag_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_add_tag_button.dispose();
			}
		});

	}

}
