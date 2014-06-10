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
import bl.movie_logics;
import bl.user_logics;

/**
 * Recommendation Tab
 */
public class recommendation_tab extends Composite {
	List<String> movies_my_taste = null;
	List<light_entity_movie> movies_my_taste_entity = null;
	List<String> movies_friends_taste = null;
	List<light_entity_movie> movies_friends_taste_entity = null;
	List<String> movies_top_rated = null;
	List<light_entity_movie> top_movies_entity = null;
	int MOVIE_LIMIT = 5;
	
	public recommendation_tab(final Display display, Composite parent, int style) {
		super(parent, style);

		final List<Label> movies_my_taste_labels = new ArrayList<Label>();
		final List<Label> movies_friends_taste_labels = new ArrayList<Label>();
		final List<Label> movies_top_rated_labels = new ArrayList<Label>();

		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);

		//window background
		String imgURL = ".\\src\\gui\\images\\blue_640_480_3.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				background.dispose();
			}
		});

		// headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Highly Recommended Movies For You");
		headline_label
				.setLayoutData(gui_utils.form_data_factory(-1, -1, 2, 55));
		final Font font_headline_label = new Font(display, "Ariel", 20,
				SWT.BOLD);
		headline_label.setFont(font_headline_label);
		headline_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_headline_label.dispose();
			}
		});
		
		
		
		/* lower note */
		Label lower_label = new Label(this, SWT.NONE);
		lower_label.setText("Click on a movie name for movie details");
		lower_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 390, 315));
		final Font font_lower_label = new Font(display, "Ariel", 11, SWT.BOLD);
		lower_label.setFont(font_lower_label);
		lower_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_lower_label.dispose();
			}
		});

		
		// based on what we have learned about you AREA
		final Composite area1 = new Composite(this, SWT.NONE);
		area1.setLayoutData(gui_utils.form_data_factory(295, 185, 40, 10));
		GridLayout grid_layout_area1 = new GridLayout(1, false);
		area1.setLayout(grid_layout_area1);

		// based on what we have learned about you HEADLINE
		Label area1_headline = new Label(area1, SWT.NONE);
		area1_headline.setText("Based On What We have Learned About You");
		area1_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1,
				-1, -1));
		final Font font_area1_headline = new Font(display, "Ariel", 10,
				SWT.BOLD);
		final Font font_based_on_learned_about_you = new Font(display, "Ariel",
				10, SWT.NONE);
		area1_headline.setFont(font_area1_headline);
		area1_headline.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_area1_headline.dispose();
				font_based_on_learned_about_you.dispose();
			}
		});

		/* new thread to access the db */
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					movies_my_taste_entity = user_logics
							.get_user_recommended_movies(log_in_window.user
									.get_current_user_id());
					movies_my_taste = gui_utils
							.convert_movies_entity_to_string(movies_my_taste_entity);
				} catch (final SQLException e1) {
					display.asyncExec(new Runnable() {
						public void run() {
							gui_utils.raise_sql_error_window(gui_utils.display);
							return;
						}
					});
				}

				display.asyncExec(new Runnable() {

					public void run() {

						int j = 0;
						final List<light_entity_movie> movies_my_taste_entity_for_annonymus = movies_my_taste_entity;
						for (j = 0; j < movies_my_taste.size(); j++) {
							final int k = j;
							movies_my_taste_labels.add(new Label(area1,
									SWT.NONE));
							movies_my_taste_labels.get(j).setText(
									movies_my_taste.get(j));
							movies_my_taste_labels.get(j).setFont(
									font_based_on_learned_about_you);
							movies_my_taste_labels.get(j).setLayoutData(
									gui_utils.grid_data_factory(280, 20, -1, 3,
											-1, -1, -1, -1));
							movies_my_taste_labels.get(j).addMouseListener(
									new MouseAdapter() {
										// @Override
										public void mouseUp(MouseEvent arg0) {
											Thread t = new Thread(
													new Runnable() {

														public void run() {
															display.asyncExec(new Runnable() {
																movie_details_window movie_details = null;
																public void run() {
																	try {
																		movie_details = new movie_details_window(
																				display,
																				movies_my_taste_entity_for_annonymus
																						.get(k)
																						.get_movie_id());
																		movie_details
																				.open();
																	} catch (final SQLException e) {

																		gui_utils
																				.raise_sql_error_window(gui_utils.display);
																		return;
																	}
																}
															});
														}
													});
											gui_utils.executor.execute(t);

										}

									});

							if (j == 4)
								break;
						}
					}
				});
			}
		});
		gui_utils.executor.execute(t);

		// based on my friends area
		final Composite area2 = new Composite(this, SWT.NONE);
		area2.setLayoutData(gui_utils.form_data_factory(295, 185, 230, 10));
		GridLayout grid_layout_area2 = new GridLayout(1, false);
		area2.setLayout(grid_layout_area2);

		// based on my friends headline
		Label area2_headline = new Label(area2, SWT.NONE);
		area2_headline.setText("Based On Your Friends Taste");
		area2_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1,
				-1, -1));
		final Font font_area2_headline = new Font(display, "Ariel", 10,
				SWT.BOLD);
		area2_headline.setFont(font_area2_headline);
		area2_headline.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_area2_headline.dispose();
			}
		});

		/* new thread to access the db */
		Thread t2 = new Thread(new Runnable() {
			public void run() {

				try {
					movies_friends_taste_entity = user_logics.get_user_recommended_movies_entities_by_friends(
							log_in_window.user.get_current_user_id(), 5);
				} catch (final SQLException e1) {

					display.asyncExec(new Runnable() {

						public void run() {

							gui_utils.raise_sql_error_window(gui_utils.display);
							return;
						}
					});
				}
				display.asyncExec(new Runnable() {
					public void run() {

						movies_friends_taste = gui_utils
								.convert_movies_entity_to_string(movies_friends_taste_entity);
						int j = 0;
						final List<light_entity_movie> movies_freinds_taste_entity_for_annonymus = movies_friends_taste_entity;
						for (j = 0; j < movies_friends_taste.size(); j++) {
							final int k = j;
							movies_friends_taste_labels.add(new Label(area2,
									SWT.NONE));
							movies_friends_taste_labels.get(j).setText(
									movies_friends_taste.get(j));
							movies_friends_taste_labels.get(j).setFont(
									font_based_on_learned_about_you);
							movies_friends_taste_labels.get(j).setLayoutData(
									gui_utils.grid_data_factory(280, 20, -1, 3,
											-1, -1, -1, -1));
							movies_friends_taste_labels.get(j)
									.addMouseListener(new MouseAdapter() {
										 @Override
										public void mouseUp(MouseEvent arg0) {
											Thread t = new Thread(
													new Runnable() {

														public void run() {
															display.asyncExec(new Runnable() {

																public void run() {
																	movie_details_window movie_details = null;
																	try {
																		movie_details = new movie_details_window(
																				display,
																				movies_freinds_taste_entity_for_annonymus
																						.get(k)
																						.get_movie_id());
																		movie_details
																				.open();
																	} catch (SQLException e) {
																	
																	}

																}

															});

														}

													});
											gui_utils.executor.execute(t);

										}

									});

							if (j == 4)
								break;
						}

					}

				});

			}

		});

		gui_utils.executor.execute(t2);

		// top rated area area
		final Composite area3 = new Composite(this, SWT.NONE);
		area3.setLayoutData(gui_utils.form_data_factory(295, 185, 40, 310));
		GridLayout grid_layout_area3 = new GridLayout(1, false);
		area3.setLayout(grid_layout_area3);

		// top rated headline
		Label area3_headline = new Label(area3, SWT.NONE);
		area3_headline.setText("Top Rated Movies");
		area3_headline.setLayoutData(gui_utils.grid_data_factory(0, -1, -1, -1,
				-1, -1));
		final Font font_area3_headline = new Font(display, "Ariel", 10,
				SWT.BOLD);
		area3_headline.setFont(font_area3_headline);
		area3_headline.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_area3_headline.dispose();
			}
		});
		
		/* new thread to access the db */
		Thread t3 = new Thread(new Runnable() {
			public void run() {

				// top LINKS

				// List<light_entity_movie> movies_freinds_taste_entity = null;
				try {
					top_movies_entity = movie_logics.get_top_rated_movies(MOVIE_LIMIT); 
				} catch (final SQLException e1) {
					display.asyncExec(new Runnable() {
						public void run() {
							gui_utils.raise_sql_error_window(gui_utils.display);
							return;
						}
					});
				}

				display.asyncExec(new Runnable() {
					public void run() {
						movies_top_rated = gui_utils
								.convert_movies_entity_to_string(top_movies_entity);
						int j = 0;
						final List<light_entity_movie> top_movies_entity_for_annonymus = top_movies_entity;
						for (j = 0; j < movies_top_rated.size(); j++) {
							final int k = j;
							movies_top_rated_labels.add(new Label(area3,
									SWT.NONE));
							movies_top_rated_labels.get(j).setText(
									movies_top_rated.get(j));
							movies_top_rated_labels.get(j).setFont(
									font_based_on_learned_about_you);
							movies_top_rated_labels.get(j).setLayoutData(
									gui_utils.grid_data_factory(280, 20, -1, 3,
											-1, -1, -1, -1));
							movies_top_rated_labels.get(j).addMouseListener(
									new MouseAdapter() {
										// @Override
										public void mouseUp(MouseEvent arg0) {
											Thread t = new Thread(
													new Runnable() {
														public void run() {
															display.asyncExec(new Runnable() {

																public void run() {
																	movie_details_window movie_details = null;
																	try {
																		movie_details = new movie_details_window(
																				display,
																				top_movies_entity_for_annonymus
																						.get(k)
																						.get_movie_id());
																	} catch (SQLException e) {
																		gui_utils.raise_sql_error_window(gui_utils.display);
																		return;
																	}
																	movie_details
																			.open();
																}
															});
														}
													});
											gui_utils.executor.execute(t);
										}
									});

							if (j == 4)
								break;
						}

					}

				});

			}

		});

		gui_utils.executor.execute(t3);

	}
}
