package dione.ui;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import dione.core.user_logics;
import dione.core.verifier;


/**
 * log in window
 */
public class log_in_window extends abstract_window {

	public static user_logics user; /* will represent current user */

	public log_in_window(final Display display) {
		super(display, SWT.MIN);

		this.setSize(300, 300);
		this.setText("Dione");

		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {

				if (gui_utils.EXIT_ON_LOGIN == true) /* EXIT the app */
				{
					display.dispose();
					gui_utils.exist_threads();
				} else
					gui_utils.EXIT_ON_LOGIN = true;
			}
		});
		

		// window layout
		this.setLayout(new GridLayout(2, false));

		// window background
		String imgURL = ".\\src\\gui\\images\\blue_300.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);

		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				background.dispose();
			}
		});
		

		// headline label
		Label log_in_label = new Label(this, SWT.NONE);
		log_in_label.setText("Log In Or Sign Up");
		log_in_label.setLayoutData(gui_utils.grid_data_factory(27, 15, 2, -1,
				SWT.CENTER, SWT.CENTER));
		final Font font_log_in_label = new Font(display, "Ariel", 20, SWT.NONE);
		log_in_label.setFont(font_log_in_label);

		/* Disposal Listener */
		log_in_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_log_in_label.dispose();
			}
		});

		// username label
		Label username_label = new Label(this, SWT.NONE);
		username_label.setText("Username:");
		username_label.setLayoutData(gui_utils.grid_data_factory(31, 20, -1,
				-1, -1, -1));
		final Font font_username_label = new Font(display, "Ariel", 12,
				SWT.NONE);
		username_label.setFont(font_username_label);

		/* Disposal Listener */
		username_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_username_label.dispose();
			}
		});

		// username text
		final Text username_text = new Text(this, SWT.BORDER);
		username_text.setLayoutData(gui_utils.grid_data_factory(100, 15, 0, 20,
				-1, -1, -1, -1));
		username_text.setTextLimit(10); /* username max limit */

		// password label
		Label password_label = new Label(this, SWT.NONE);
		password_label.setText("Password:");
		password_label.setLayoutData(gui_utils.grid_data_factory(31, 10, -1,
				-1, -1, -1));
		final Font font_password_label = new Font(display, "Ariel", 12,
				SWT.NONE);
		password_label.setFont(font_password_label);

		/* Dispoal Listener */
		password_label.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_password_label.dispose();
			}
		});

		//password text
		final Text password_text = new Text(this, SWT.PASSWORD | SWT.BORDER);
		password_text.setLayoutData(gui_utils.grid_data_factory(100, 15, 0, 10,
				-1, -1, -1, -1));
		password_text.setTextLimit(10); /* password max limit */

		//log in button
		Button log_in_button = new Button(this, SWT.PUSH);
		log_in_button.setText("Log In");
		final Font font_login_button = new Font(display, "Ariel", 12, SWT.NONE);
		log_in_button.setFont(font_login_button);

		/* Disposal Listener */
		log_in_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_login_button.dispose();
			}
		});
		log_in_button.setLayoutData(gui_utils.grid_data_factory(70, 35, 55, 30,
				-1, -1, -1, -1));

		/* log in Listener */
		log_in_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String username = username_text.getText();
				final String pass = password_text.getText();

				/* Illegal username */
				if (!verifier.verifyname(username)) {
					/* showing an informative message box */
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
					return;
				}

				/* Invalid password */
				else if (!verifier.verifyPass(pass)) {
					MessageBox messageBox = new MessageBox(display
							.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("Illegal Password");
					messageBox
							.setMessage("Password must contain 1-10 alphanumeric chars.");
					messageBox.open();
					return;
				}

				else {
					
					/* new thread to access the db */
					Thread t = new Thread(new Runnable() {
						public void run() {

							try {
								/* user and password mathces */
								if (user_logics.authenticate_user(username, pass)) {
									user = new user_logics(); /* initializing a new user object */
									user.login_user(username, pass); 

									display.asyncExec(new Runnable() {

										public void run() {
											
											/* closing log in window, display remains undisposed */
											gui_utils.EXIT_ON_LOGIN = false;
											gui_utils.login_win.dispose();

										}

									});

									/* user already made first movie rate */
									if (user.user_rated()) 
									{
										//shachar: join with previous async
										display.asyncExec(new Runnable() {

											public void run() {
												/* opening the main window */
												gui_utils.tabs_win = new all_tabs_window(gui_utils.display);
												gui_utils.tabs_win.open();
											}
										});
									}

									/* first rate is needed by the user  */
									else { 
										//shachar: join with previous async
										display.asyncExec(new Runnable() {
											public void run() {
												/* opening preferences window (first user rate) */
												gui_utils.pref_win = new preferences_window(gui_utils.display);
												/* in case there are no movies to rate in the db */
												if (gui_utils.pref_win.can_be_opened) {
													gui_utils.pref_win.open();
												}
												
												else {
													/* closing log in window, display remains undisposed */
													gui_utils.EXIT_ON_LOGIN = false;
													gui_utils.pref_win.dispose();
													
													/* showing an informative message box */
													MessageBox messageBox = new MessageBox(gui_utils.tabs_win, SWT.ICON_WARNING);
													messageBox.setText("Error");
													messageBox.setMessage("Couldn't find any movies to rate");
													messageBox.open();
													return;
												}
											}
										});
									}
									
								} else { /* user and password does not match */
									//shachar: join with previous async
									display.asyncExec(new Runnable() {

										public void run() {
											/* showing an informative message box */
											MessageBox messageBox = new MessageBox(display.getActiveShell(),SWT.ICON_WARNING);
											messageBox.setText("Error");
											messageBox.setMessage("Password and username do not match. Try again.");
											messageBox.open();
											return;
										}

									});

								}
							} catch (final SQLException e) {

								display.asyncExec(new Runnable() {
									public void run() {
										gui_utils.raise_sql_error_window(gui_utils.display);
										return;
										
									}
								});
							}
							return;
						}
					});

					/* executer will execute the thread */
					gui_utils.executor.execute(t);
				}
			}
		});

		//sign up button
		Button sign_up_button = new Button(this, SWT.PUSH);
		sign_up_button.setText("Sign Up");
		sign_up_button.setLayoutData(gui_utils.grid_data_factory(70, 35, 20,
				30, -1, -1, -1, -1));
		final Font font_signup_button = new Font(display, "Ariel", 12, SWT.NONE);
		sign_up_button.setFont(font_login_button);
		sign_up_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_signup_button.dispose();
			}
		});

		/* sign up Listener */
		sign_up_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String username = username_text.getText();
				final String pass = password_text.getText();
				if (!verifier.verifyname(username)) { /* Illegal username */
					/* showing an informative message box */
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
					return;
				} else if (!verifier.verifyPassSignUp(pass)) { /* Invalid password  */
					/* showing an informative message box */
					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("Illegal Password");
					messageBox.setMessage("Password must contain 4-10 alphanumeric chars.");
					messageBox.open();
					return;
				} else

				{
					/* new thread to access the db */
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								final boolean success = user_logics.add_user(username, pass);
								display.asyncExec(new Runnable() {
									
									public void run() {
										if (success) {
											/* showing an informative message box */
											MessageBox messageBox = new MessageBox(display.getActiveShell(),SWT.ICON_WORKING);
											messageBox.setText("SUCCESS");
											messageBox.setMessage("User " + username + 
													" has been succesfully signed up");
											messageBox.open();
											return;
										} else {
											/* showing an informative message box */
											MessageBox messageBox = new MessageBox(display.getActiveShell(),SWT.ICON_WARNING);
											messageBox.setText("Error");
											messageBox.setMessage("There was an error during signup. Sorry.");
											messageBox.open();
											return;
										}
									}
								});

							} catch (final SQLException e) {
								display.asyncExec(new Runnable() {

									public void run() {
										/* showing a sql error message box */
										gui_utils.raise_sql_error_window(gui_utils.display);
										return;
									}

								});
							}
						}
					});
					/* executer will execute the thread */
					gui_utils.executor.execute(t);
				}

			}
		});

		
	}


}
