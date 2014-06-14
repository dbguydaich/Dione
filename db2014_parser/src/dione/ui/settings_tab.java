package dione.ui;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import dione.core.user_logics;
import dione.core.verifier;
import dione.db.db_operations;
import dione.db.db_operations.invocation_code;

/**
 * Settings Tab
 */
public class settings_tab extends Composite {

	Timestamp time = null;
	
	public settings_tab(final Display display, Composite parent, int style) {
		super(parent, style);

		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);

		//window background
		String imgURL = gui_utils.my_config.get_image_640_480();
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				background.dispose();
			}
		});

		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Settings");
		final Font font_headline_label = new Font(display, "Ariel", 21,
				SWT.BOLD);
		headline_label.setFont(font_headline_label);
		headline_label.setLayoutData(gui_utils
				.form_data_factory(-1, -1, 2, 250));
		headline_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_headline_label.dispose();
			}
		});

		// update button
		Button update_button = new Button(this, SWT.PUSH);
		update_button.setText("Data Update");
		update_button
				.setLayoutData(gui_utils.form_data_factory(-1, -1, 70, 25));
		final Font font_update_button = new Font(display, "Ariel", 11, SWT.NONE);
		update_button.setFont(font_update_button);
		update_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_update_button.dispose();
			}
		});

		/* Update data action */
		update_button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				
				try {
				time = dione.db.db_operations.is_currently_invoked(invocation_code.YAGO_UPDATE);
				/* if not currently running an update */
				if(time == null)
				{
					if (gui_utils.import_progress_win != null)
					{
						if (!gui_utils.import_progress_win.isDisposed())
						{
							MessageBox messageBox = new MessageBox(display
									.getActiveShell(), SWT.ICON_WARNING);
							messageBox.setText("Error");
							messageBox
								.setMessage("Data import is already in progress.");
							messageBox.open();
						}
						else // running progress win
						{
							gui_utils.import_progress_win = new import_progress_window(
								display);
							gui_utils.import_progress_win.open();

						}
					}
					else // running progress win
					{
						gui_utils.import_progress_win = new import_progress_window(
								display);
						gui_utils.import_progress_win.open();
					}
				}
				
				/* currently running an update to the db */
				else
				{
			        MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			        messageBox.setMessage("An import process that began at " + time.toString() + " hasn't finished successfully. "
			        		+ "Do you wish to import again?");
			        messageBox.setText("Exiting Application");
			        int response = messageBox.open();
			        
			        /* user clicked yes */
			        if (response == SWT.YES)
			        {
						if (gui_utils.import_progress_win != null)
						{
							if (!gui_utils.import_progress_win.isDisposed())
							{
								MessageBox messageBox1 = new MessageBox(display
										.getActiveShell(), SWT.ICON_WARNING);
								messageBox1.setText("Error");
								messageBox1
									.setMessage("Data import is already in progress.");
								messageBox1.open();
							}
							else // running progress win
							{
								gui_utils.import_progress_win = new import_progress_window(
									display);
								gui_utils.import_progress_win.open();
								db_operations.confirm_invocation_performed(invocation_code.YAGO_UPDATE, time.toString());
							}
						}
						else // running progress win
						{
							gui_utils.import_progress_win = new import_progress_window(
									display);
							gui_utils.import_progress_win.open();
							db_operations.confirm_invocation_performed(invocation_code.YAGO_UPDATE, time.toString());
						}
			        }

				}
				
				} catch (SQLException e) {
					gui_utils.raise_sql_error_window(gui_utils.display);
					return;
				}
			}

		});

		// update label
		Label update_label = new Label(this, SWT.NONE);
		final Font font_update_label = new Font(display, "Ariel", 10, SWT.NONE);
		update_label.setFont(font_update_label);
		update_label.setText("Note: This operation may take a while...");
		update_label
				.setLayoutData(gui_utils.form_data_factory(-1, -1, 75, 140));
		update_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_update_label.dispose();
			}
		});

		// log out button
		Button log_out_button = new Button(this, SWT.PUSH);
		log_out_button.setText("Log Out");
		log_out_button.setLayoutData(gui_utils.form_data_factory(-1, -1, 10,
				550));
		final Font font_logout_button = new Font(display, "Ariel", 11, SWT.NONE);
		log_out_button.setFont(font_logout_button);
		log_out_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_logout_button.dispose();
			}
		});

		/* log out action */
		log_out_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(gui_utils.import_progress_win != null)
				{
					if(!gui_utils.import_progress_win.isDisposed()) /* if user currently loading data */
					{
						MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
						messageBox.setText("Failed");
						messageBox.setMessage("Please abort the data load first. ");
						messageBox.open();
					}
					
					else
					{
						handle_logout();
					}	
				}
				
				else
				{
					handle_logout();
				}
			}
		});

		// rate movies button
		Button rate_movies_button = new Button(this, SWT.PUSH);
		rate_movies_button.setText("Rate Random Movies");
		rate_movies_button.setLayoutData(gui_utils.form_data_factory(-1, -1,
				110, 25));
		final Font font_rate_movies_button = new Font(display, "Ariel", 11,
				SWT.NONE);
		rate_movies_button.setFont(font_rate_movies_button);

		/* open rate movies */
		rate_movies_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (gui_utils.pref_win != null) {
					if (!gui_utils.pref_win.isDisposed()) { /* pref win is open */
						MessageBox messageBox = new MessageBox(display
								.getActiveShell(), SWT.ICON_WARNING);
						messageBox.setText("Failed");
						messageBox
								.setMessage("Rating movies window is already open!");
						messageBox.open();
					}

					else
						/* open preferences window */
						open_pref_win();

				} else { /* open preferences window */
					open_pref_win();
				}
			}
		});

		// username area
		Composite username_area = new Composite(this, SWT.BORDER);
		username_area.setLayoutData(gui_utils.form_data_factory(250, 160, 150,
				25));
		username_area.setLayout(new GridLayout(2, false));

		// username label
		Label username_label = new Label(username_area, SWT.NONE);
		final Font font_username_label = new Font(display, "Ariel", 11,
				SWT.BOLD);
		username_label.setFont(font_username_label);
		username_label.setText("Change Username");
		username_label.setLayoutData(gui_utils.grid_data_factory(50, -1, 2, 1,
				-1, -1));
		username_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_username_label.dispose();
			}
		});

		// new username label
		Label new_username_label = new Label(username_area, SWT.NONE);
		final Font font_new_username_label = new Font(display, "Ariel", 10,
				SWT.NONE);
		new_username_label.setFont(font_new_username_label);
		new_username_label.setText("New username");
		new_username_label.setLayoutData(gui_utils.grid_data_factory(-1, 15,
				-1, -1, -1, -1));
		new_username_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_new_username_label.dispose();
			}
		});

		// username text
		final Text username_text = new Text(username_area, SWT.BORDER);
		username_text.setLayoutData(gui_utils.grid_data_factory(10, 15, -1, -1,
				-1, -1));
		username_text.setTextLimit(10);

		// username password label
		Label username_password_label = new Label(username_area, SWT.NONE);
		final Font font_username_password_label = new Font(display, "Ariel",
				10, SWT.NONE);
		username_password_label.setFont(font_username_password_label);
		username_password_label.setText("Password for verification");
		username_password_label.setLayoutData(gui_utils.grid_data_factory(-1,
				5, -1, -1, -1, -1));
		username_password_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_username_password_label.dispose();
			}
		});

		// username_password text
		final Text username_pass_text = new Text(username_area, SWT.PASSWORD
				| SWT.BORDER);
		username_pass_text.setLayoutData(gui_utils.grid_data_factory(10, 5, -1,
				-1, -1, -1));

		// username apply button
		Button username_upply_button = new Button(username_area, SWT.PUSH);
		username_upply_button.setText("Apply");
		final Font font_username_upply_button = new Font(display, "Ariel", 10,
				SWT.NONE);
		username_upply_button.setFont(font_username_upply_button);
		username_upply_button.setLayoutData(gui_utils.grid_data_factory(90, 15,
				2, -1, -1, -1));
		username_upply_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_username_upply_button.dispose();
			}
		});

		/* user change action */
		username_upply_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String username = username_text.getText();
				final String pass = username_pass_text.getText();
				if (!verifier.verifyname(username)) { // illegal user name
					MessageBox alertBox = new MessageBox(display
							.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
					return;
				}
				Thread t = new Thread(new Runnable() {

					public void run() {
						try {
							final boolean success = user_logics.update_name(
									username,
									log_in_window.user.get_current_user_id(),
									pass);
							display.asyncExec(new Runnable() {
								public void run() {

									if (success) {
										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);
										messageBox.setText("SUCCESS");
										messageBox
												.setMessage("Username has been successfully changed");
										messageBox.open();
										gui_utils.tabs_win.setText("Dione - Logged in As: " + username);
										return;
										
										
										
										
									} else {
										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);
										messageBox.setText("Failure");
										messageBox
												.setMessage("Username couldn't be changed! Make sure you enter the right password.");
										messageBox.open();
										return;
									}
								}
							});

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

				gui_utils.executor.execute(t);

			}
		});

		// password area
		Composite password_area = new Composite(this, SWT.BORDER);
		password_area.setLayoutData(gui_utils.form_data_factory(250, 160, 150,
				325));
		password_area.setLayout(new GridLayout(2, false));

		// password label
		Label password_label = new Label(password_area, SWT.NONE);
		final Font font_password_label = new Font(display, "Ariel", 11,
				SWT.BOLD);
		password_label.setFont(font_password_label);
		password_label.setText("Change Password");
		password_label.setLayoutData(gui_utils.grid_data_factory(50, -1, 2, 1,
				-1, -1));
		password_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_password_label.dispose();
			}
		});

		// new password label
		Label new_password_label = new Label(password_area, SWT.NONE);
		final Font font_new_password_label = new Font(display, "Ariel", 10,
				SWT.NONE);
		new_password_label.setFont(font_new_password_label);
		new_password_label.setText("New password");
		new_password_label.setLayoutData(gui_utils.grid_data_factory(-1, 15,
				-1, -1, -1, -1));
		new_password_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_new_username_label.dispose();
			}
		});

		// password text
		final Text password_text = new Text(password_area, SWT.PASSWORD
				| SWT.BORDER);
		password_text.setLayoutData(gui_utils.grid_data_factory(10, 15, -1, -1,
				-1, -1));

		// old password label
		Label old_password_label = new Label(password_area, SWT.NONE);
		final Font font_old_password_label = new Font(display, "Ariel", 10,
				SWT.NONE);
		old_password_label.setFont(font_old_password_label);
		old_password_label.setText("Current password");
		old_password_label.setLayoutData(gui_utils.grid_data_factory(-1, 5, -1,
				-1, -1, -1));
		old_password_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_old_password_label.dispose();
			}
		});

		// old password text
		final Text old_password_text = new Text(password_area, SWT.PASSWORD
				| SWT.BORDER);
		old_password_text.setLayoutData(gui_utils.grid_data_factory(10, 5, -1,
				-1, -1, -1));

		// password apply button
		Button password_upply_button = new Button(password_area, SWT.PUSH);
		password_upply_button.setText("Apply");
		final Font font_password_upply_button = new Font(display, "Ariel", 10,
				SWT.NONE);
		password_upply_button.setLayoutData(gui_utils.grid_data_factory(90, 15,
				2, -1, -1, -1));
		password_upply_button.setFont(font_username_upply_button);
		password_upply_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_password_upply_button.dispose();
			}
		});

		/* change password action */
		password_upply_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String pass_change_to = password_text.getText();
				final String pass = old_password_text.getText();
				if (!verifier.verifyPassSignUp(pass_change_to)) { // illegal user name
					MessageBox messageBox = new MessageBox(display
							.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("Illegal Password");
					messageBox
							.setMessage("Password must contain 4-10 alphanumeric chars.");
					messageBox.open();
					return;
				}
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							final boolean success = user_logics.update_pass(
									pass_change_to,
									log_in_window.user.get_current_user_id(),
									pass);
							display.asyncExec(new Runnable() {
								public void run() {
									if (success) {
										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);
										messageBox.setText("SUCCESS");
										messageBox
												.setMessage("password has been successfully changed");
										messageBox.open();
										return;
									} else {
										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);
										messageBox.setText("Failure");
										messageBox
												.setMessage("Password couldn't be changed!  Make sure you enter the right password");
										messageBox.open();
										return;
									}
								}
							});

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
				gui_utils.executor.execute(t);
			}
		});

	}

	/**
	 * open preference window
	 */
	private static void open_pref_win() {
		gui_utils.pref_win = new preferences_window(gui_utils.display);
		if (gui_utils.pref_win.can_be_opened) {
			gui_utils.pref_win.open();
		}

		else { /* close preferences window */
			close_pref_win();
		}
	}

	/**
	 * close preference window
	 */
	private static void close_pref_win() {
		gui_utils.EXIT_ON_LOGIN = false;
		gui_utils.pref_win.dispose();

		MessageBox messageBox = new MessageBox(gui_utils.tabs_win,
				SWT.ICON_WARNING);
		messageBox.setText("Error");
		messageBox.setMessage("Couldn't find any movies to rate");
		messageBox.open();
	}

	
	/**
	 * handle log out 
	 */
	private static void handle_logout()
	{
		log_in_window.user = null;
		gui_utils.EXIT_ON_LOGIN = false;
		gui_utils.tabs_win.dispose();

		// closing all current opened movie windows
		for (movie_details_window win : gui_utils.movie_windows) {
			if (!win.isDisposed())
				win.dispose();
		}

		gui_utils.movie_windows = new ArrayList<movie_details_window>();
		gui_utils.login_win = new log_in_window(gui_utils.display);
		gui_utils.login_win.open();

	}
}
