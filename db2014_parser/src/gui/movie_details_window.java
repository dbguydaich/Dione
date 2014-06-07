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
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.Platform;
import org.eclipse.swt.layout.FillLayout;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import bl.movie_logics;
import bl.user_logics;
import parser_entities.entity_movie;
import parser_entities.light_entity_movie;
import config.config;

////////////////////// functions the tab is waiting for: ///////////////////////

//String get_result_movie_name()
//List<String> get_result_movie_genres()
//String get_result_movie_director()
//String get_result_movie_language()
//String get_result_movie_year()
//String get_result_movie_duration()
//List<String> get_result_movie_stars()
//String get_result_movie_rating()
//String get_result_movie_wiki()
//List<String> get_result_movie_tags()

//// new : ////
//get_result_movie_plot()

////////Listeners to be implemented: //////////////

/// tags_button - should allow the user to rate the movie tags. 
/// rate info is in radios_lists. kinda complicated (needed to be generic...)
/// List<List<Button>> radios_lists - list number 3 (inner lists size - always 5) is for tags.get(3), etc
/// talk to me for more expl.

public class movie_details_window extends Shell {
	config config = new config();
	int window_height = config.get_window_height();
	int window_width = config.get_window_width();
	int movie_id_number;

	// //trying///

	light_entity_movie movie;
	String movie_name;
	List<String> genres;
	String movie_plot;
	String director_name;
	String language;
	String year;
	String duration;
	List<String> stars;
	String rating_str;
	String wiki_str;
	List<String> tags;

	public movie_details_window(final Display display, final int movie_id)
			throws SQLException {

		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));

		Thread t = new Thread(new Runnable() {

			public void run() {
				light_entity_movie movie = null;
				try {
					movie = movie_logics.get_movie_details(movie_id);

					genres = movie_logics.get_movie_genres(movie.get_movie_id());

					director_name = movie.get_movie_director();
					movie_name = movie.get_movie_name();
					System.out.println("got it!");

					movie_plot = (movie.get_movie_plot());

					// language2

					language = movie.get_movie_language();

					year = Integer.toString(movie.get_movie_year());

					// duration
					duration = Integer.toString(movie.get_movie_duration());

					stars = gui_utils.convert_person_string(movie
							.get_movie_actors());

					rating_str = String.valueOf(movie.get_movie_rating());

					wiki_str = movie.get_movie_wikipedia_url();

					tags = (List<String>) movie.get_movie_tags();
				} catch (final SQLException e) {

					display.asyncExec(new Runnable() {

						public void run() {

							gui_utils.raise_sql_error_window(display);
							// TODO Auto-generated catch block
							e.printStackTrace();

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
			e1.printStackTrace();
		}
		// ///////// String movie_name = movie.get_movie_name();
		// /// String movie_plot= (movie.get_movie_plot());

		// public movie_details_window(final Display display, int
		// movie_id,String movie_name ,light_entity_movie movie, List<String>
		// genres
		// ,String director_name,String language,String duration,List<String>
		// stars,String year,String rating_str
		// ,String wiki_str,final List<String> tags) throws SQLException
		// {
		// ////// light_entity_movie movie =
		// movie_logics.get_movie_details(movie_id);///////////////////////////////////
		// /////// List<String> genres;
		// ///////////////////////// genres =
		// movie_logics.get_movie_genres(movie.get_movie_id());////
		// /////////////////////////////////// String director_name =
		// movie.get_movie_director();
		// String director_name = get_result_movie_director(); to be used
		// language2
		// ///////////// String language = movie.get_movie_language();
		// String language = get_result_movie_language(); to be used
		// /////////String year = Integer.toString(movie.get_movie_year());
		// String director_name = get_result_movie_year(); to be used
		// year
		// /////////String year = Integer.toString(movie.get_movie_year());
		// String director_name = get_result_movie_year(); to be used
		// duration
		// ///////String duration =
		// Integer.toString(movie.get_movie_duration());
		// String director_name = get_result_movie_duration(); to be used
		// ////////// List<String> stars;
		// ////stars =
		// gui_utils.convert_person_string(movie.get_movie_actors());
		// //////////////String rating_str
		// =String.valueOf(movie.get_movie_rating());
		// String rating_str = get_result_movie_rating(); to be used
		// ////////// String wiki_str;
		// /////////// wiki_str = movie.get_movie_wikipedia_url();
		// ///////// ////////final List<String> tags;

		// ////////// tags = (List<String>) movie.get_movie_tags();

		movie_id_number = movie_id;
		gui_utils.movie_windows.add(this);

		this.setLayout(new FormLayout());
		this.setSize(window_width + 100, window_height);

		String my_name = null;
		try {
			my_name = log_in_window.user.get_my_name();
		} catch (SQLException e2) {
			gui_utils.raise_sql_error_window(display);
		}
		//
		this.setText("Movie Details - Logged in As: " + my_name);

		String imgURL = ".\\src\\gui\\images\\blue_740_480.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);

		this.setLayout(new FormLayout());

		final Font font_ariel_11 = new Font(display, "Ariel", 10, SWT.NONE);
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				background.dispose();
				font_ariel_11.dispose();
			}
		});

		// ////// light_entity_movie movie =
		// movie_logics.get_movie_details(movie_id);///////////////////////////////////
		List<Label> movie_details_labels = new ArrayList<Label>();

		// headline
		Label headline_label = new Label(this, SWT.NONE);
		// ///////// String movie_name = movie.get_movie_name();
		headline_label.setText(movie_name);
		headline_label.setLayoutData(gui_utils
				.form_data_factory(-1, -1, 10, 10));
		headline_label.setAlignment(SWT.CENTER);
		final Font font1 = new Font(display, "Ariel", 15, SWT.BOLD);
		headline_label.setFont(font1);
		headline_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font1.dispose();
			}
		});

		// left area
		Composite left_area = new Composite(this, SWT.NONE);
		left_area.setLayoutData(gui_utils.form_data_factory(390, 190, 60, 10));
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

		// genres1
		movie_details_labels.get(0).setText("Genres:");

		// genres2
		// /////// List<String> genres;
		// ///////////////////////// genres =
		// movie_logics.get_movie_genres(movie.get_movie_id());////

		String genres_str = "";
		for (String str : genres)
			// maybe limit
			genres_str = genres_str + str + ", ";

		if (genres_str.length() >= 2)
			genres_str = genres_str.substring(0, genres_str.length() - 2);

		movie_details_labels.get(1).setText(genres_str);

		// director1
		movie_details_labels.get(2).setText("Director:");

		// director2
		// /////////////////////////////////// String director_name =
		// movie.get_movie_director();
		// String director_name = get_result_movie_director(); to be used

		movie_details_labels.get(3).setText(director_name);

		// language1
		movie_details_labels.get(4).setText("Language:");

		// language2
		// ///////////// String language = movie.get_movie_language();
		// String language = get_result_movie_language(); to be used

		movie_details_labels.get(5).setText(language);

		// year
		movie_details_labels.get(6).setText("Year:");

		// year
		// /////////String year = Integer.toString(movie.get_movie_year());
		// String director_name = get_result_movie_year(); to be used

		movie_details_labels.get(7).setText(year);

		// duration
		movie_details_labels.get(8).setText("Duration:");

		// duration
		// ///////String duration =
		// Integer.toString(movie.get_movie_duration());
		// String director_name = get_result_movie_duration(); to be used

		movie_details_labels.get(9).setText(duration);

		// stars1
		movie_details_labels.get(10).setText("Stars:");

		// stars2
		// ////////// List<String> stars;
		// ////stars =
		// gui_utils.convert_person_string(movie.get_movie_actors());

		String stars_str = "";
		for (String str : stars)
			stars_str = stars_str + str + ", ";

		if (stars_str.length() >= 2)
			stars_str = stars_str.substring(0, stars_str.length() - 2);

		movie_details_labels.get(11).setText(stars_str);

		// rating1
		movie_details_labels.get(8).setText("Rating(1-5):");
		// rating2

		// //////////////String rating_str
		// =String.valueOf(movie.get_movie_rating());
		// String rating_str = get_result_movie_rating(); to be used
		movie_details_labels.get(9).setText(rating_str);

		// wiki label
		Label wiki_label = new Label(left_area, SWT.H_SCROLL);
		wiki_label.setText("Wiki Link:");
		wiki_label.setLayoutData(gui_utils.grid_data_factory(-1, 5, -1, -1, -1,
				-1));
		wiki_label.setFont(font_ariel_11);

		//
		// Link link = new Link(left_area, SWT.NONE);
		// String message = wiki_str;
		// link.setText("wtf");
		// link.setSize(1000, 100);
		// link.addSelectionListener(new SelectionAdapter(){
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// System.out.println("You have selected: "+e.text);
		//
		// // Open default external browser
		// //
		// PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new
		// URL(e.text));
		// URI uri;
		// try {
		// uri = new URI( wiki_str);
		// } catch (URISyntaxException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// return;
		// }
		// try {
		// Desktop.getDesktop().browse( uri);
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		//
		// }
		// });

		//
		//
		//
		// wiki url
		Label wiki_url = new Label(left_area, SWT.NONE);
		// ////////// String wiki_str;
		// /////////// wiki_str = movie.get_movie_wikipedia_url();
		wiki_url.setText(wiki_str);
		wiki_url.setLayoutData(gui_utils.grid_data_factory(-1, 5, -1, -1, -1,
				-1));
		wiki_url.setFont(font_ariel_11);

		wiki_url.addMouseListener(new MouseAdapter() {
			// @Override
			public void mouseUp(MouseEvent arg0) {

			
				try {
					//Desktop.getDesktop().browse(new URI("www.walla.co.il"));/// shachar this is working
					Desktop.getDesktop().browse(new URI(wiki_str));
					
				} catch (IOException e) {
					MessageBox messageBox = new MessageBox(
							display.getActiveShell(),
							SWT.ICON_ERROR);
					messageBox.setText("Failure");
					messageBox
							.setMessage("Couldn't open the desired link.");
					messageBox.open();
					
				} catch (URISyntaxException e) {
					MessageBox messageBox = new MessageBox(
							display.getActiveShell(),
							SWT.ICON_ERROR);
					messageBox.setText("Failure");
					messageBox
							.setMessage("Couldn't open the desired link.");
					messageBox.open();
					
				}


			}

		});

		// tags area
		Composite tags_area = new Composite(this, SWT.NONE);
		tags_area.setLayoutData(gui_utils.form_data_factory(320, 250, 60, 405));
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
		// ///////// ////////final List<String> tags;
		List<Composite> radios_areas = new ArrayList<Composite>();
		// ////////// tags = (List<String>) movie.get_movie_tags();

		//

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

		if (count == 0)
			tags_area.setVisible(false);

		// tags rate button
		Button tags_button = new Button(tags_area, SWT.PUSH);
		tags_button.setText("Rate");
		tags_button.setLayoutData(gui_utils.grid_data_factory(95, -1, 2, -1,
				-1, -1));
		// ////////////////
		tags_button.addMouseListener(new MouseAdapter() {
			// @Override
			public void mouseUp(MouseEvent arg0) {
				Thread t = new Thread(new Runnable() {

					public void run() {

						System.out.println("rated");

						display.asyncExec(new Runnable() {
							int rate_number;
							boolean success = true;

							public void run() {

								for (int i = 0; i < radios_lists.size(); i++) {
									rate_number = gui_utils
											.get_index_button(radios_lists
													.get(i)) + 1;
									try {
										System.out.println("rating "
												+ tags.get(i) + "   "
												+ rate_number);

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
															.fill_user_prefence();
												} catch (SQLException e) {

												}
											}

										});

										gui_utils.executor.execute(t3);

									} catch (SQLException e) {
										// TODO Auto-generated catch block
										gui_utils
												.raise_sql_error_window(display);
										e.printStackTrace();
									}
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

		// //////////

		// tags bottom label
		Label tags_bottom_label = new Label(tags_area, SWT.NONE);
		tags_bottom_label
				.setText("Please rate these tags for the movie \n(1=disagree, 5=agree)");
		tags_bottom_label.setLayoutData(gui_utils.grid_data_factory(-1, -1, 2,
				-1, -1, -1));
		final Font font_bottom_tags = new Font(display, "Ariel", 8, SWT.NONE);
		tags_bottom_label.setFont(font_bottom_tags);
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
		// String movie_plot= (movie.get_movie_plot());
		plot_label.setText(movie_plot);
		// plot_label.setText(get_result_movie_plot()) //to be used

		scroller_plot.setContent(plot_label);

		// movie rate area
		Composite rate_movie_area = new Composite(this, SWT.NONE);
		rate_movie_area.setLayoutData(gui_utils.form_data_factory(300, 60, 370,
				250));
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
		movie_rate_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_movie_rate_label.dispose();
			}
		});

		// movie rate radios
		final List<Button> movie_rate_radios = new ArrayList<Button>();
		for (int i = 0; i < 5; i++) {
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
		movie_rate_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_movie_rate_button.dispose();
			}
		});

		movie_rate_button.addMouseListener(new MouseAdapter() {
			// @Override
			public void mouseUp(MouseEvent arg0) {

				Thread t = new Thread(new Runnable() {

					public void run() {

						display.asyncExec(new Runnable() {

							public void run() {

								final int rate_number = gui_utils
										.get_index_button(movie_rate_radios) + 1;

								boolean success = true;

								System.out.println("rated");

								try {

									if (log_in_window.user.rate_movie(
											movie_id_number, rate_number) == false)

									{
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

									gui_utils.raise_sql_error_window(display);
									// TODO Auto-generated catch block
									e.printStackTrace();

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

		// rate_movie(int movie_id,int rate)

	}

	private void get_movie_details(int movie_id) {
		// TODO Auto-generated method stub

	}

	protected void checkSubclass() {
	}

}
