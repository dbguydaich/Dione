package gui;


import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import bl.user_logics;
import bl.verifier;
import config.config;
import db.db_queries_user;

//import bl.verifier;
//import runnableLogic.AddUser;
//import viewModelLayer.InputVerifier;

//import db.IdbOparations;







public class log_in_window extends Shell
{
	

	///good idea to put it here?
	public static user_logics user;
	
	/////
	config config = new config();
	int window_height = config.get_window_height();
	int window_width = config.get_window_width();


	
	public log_in_window(final Display display)
	{
		super(display);
		
		this.setSize(300, 300);
		this.setText("MovieBook");
		
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				display.dispose();
			}		
		});
		
		this.setLayout(new FormLayout());
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.setLayout(new GridLayout(2, false));
		
		
		//window background
		String imgURL = ".\\src\\gui\\images\\blue_300.jpg";
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
		

		
		//headline label
		Label log_in_label = new Label(this, SWT.NONE);
		log_in_label.setText("Log In Or Sign Up");
		log_in_label.setLayoutData(gui_utils.grid_data_factory(27, 15, 2, -1, SWT.CENTER, SWT.CENTER));
		final Font font_log_in_label = new Font(display, "Ariel",20, SWT.NONE);
		log_in_label.setFont(font_log_in_label);
		log_in_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_log_in_label.dispose();
			}		
		});
		
		
		
		//label username
		Label username_label = new Label(this, SWT.NONE);
		username_label.setText("Username:");
		username_label.setLayoutData(gui_utils.grid_data_factory(31, 20, -1, -1, -1, -1));
		final Font font_username_label = new Font(display, "Ariel", 12, SWT.NONE);
		username_label.setFont(font_username_label);
		username_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_username_label.dispose();
			}		
		});
		
		
		
		//text username
		final Text username_text = new Text(this, SWT.BORDER);
		username_text.setLayoutData(gui_utils.grid_data_factory(100, 15, 0, 20, -1, -1, -1, -1));
		username_text.setTextLimit(10);
		
		
		
		//label password
		Label password_label = new Label(this, SWT.NONE);
		password_label.setText("Password:");
		password_label.setLayoutData(gui_utils.grid_data_factory(31, 10, -1, -1, -1, -1));
		final Font font_password_label = new Font(display, "Ariel", 12, SWT.NONE);
		password_label.setFont(font_password_label);
		password_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_password_label.dispose();
			}		
		});
		
		
		
		//text password
		final Text password_text = new Text(this, SWT.PASSWORD | SWT.BORDER);
		password_text.setLayoutData(gui_utils.grid_data_factory(100, 15, 0, 10, -1, -1, -1, -1));
		password_text.setTextLimit(6);
		
		
		
		//log in button
		Button log_in_button = new Button(this, SWT.PUSH);
		log_in_button.setText("Log In");
		final Font font_login_button = new Font(display, "Ariel", 12, SWT.NONE);
		log_in_button.setFont(font_login_button);
		log_in_button.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_login_button.dispose();
			}		
		});
		
		log_in_button.setLayoutData(gui_utils.grid_data_factory(70, 35, 55, 30, -1, -1, -1, -1));
		////
		log_in_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String username = username_text.getText();
				final String pass = password_text.getText();
				if(!verifier.verifyname(username)){ // illegal user name
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
				}else if(!verifier.verifyPass( pass)){ // invalid password
					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("Illegal Password");
					messageBox.setMessage("Password must contain 1-6 alphanumeric chars.");
					messageBox.open();
				} else
					try {
						if( db_queries_user.authenticate_user(username,pass)){
							user = new user_logics(); //Initializing user to be worked with all session long
							user.login_user(username, pass);
							
							
							//addition needed: in case it is the first log in, go to some other window
							
							
							if (user.user_rated()) //user already rated movies
							{
								gui_utils.login_win.dispose(); //closing log in window (display is closed along with it)
								
								gui_utils.display = Display.getDefault();
								gui_utils.tabs_win = new all_tabs_window(gui_utils.display); 
								gui_utils.tabs_win.open();
							}
							
							else
							{
								gui_utils.login_win.dispose(); //closing log in window (display is closed along with it)
								gui_utils.display = Display.getDefault();
								gui_utils.pref_win = new preferences_window(gui_utils.display); 
								
								if(gui_utils.pref_win.can_be_opened)
								{
									gui_utils.pref_win.open();
								}
								
								else
								{
									gui_utils.pref_win.dispose();
									gui_utils.tabs_win = new all_tabs_window(gui_utils.display); 
									gui_utils.tabs_win.open();
								}
						
							}
							
					
							
							
						}
						else{ //no user found
							MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
							messageBox.setText("Error");
							messageBox.setMessage("Password and username do not match. Try again.");
							messageBox.open();
						}
					} catch (SQLException e) {
						gui_utils.raise_sql_error_window(display);
						e.printStackTrace();
					}
				
		
			
		
			}

		});
		////

		
		
		
		
		//sign up button
		Button sign_up_button = new Button(this, SWT.PUSH);
		sign_up_button.setText("Sign Up");
		sign_up_button.setLayoutData(gui_utils.grid_data_factory(70, 35, 20, 30, -1, -1, -1, -1));
		final Font font_signup_button = new Font(display, "Ariel", 12, SWT.NONE);
		sign_up_button.setFont(font_login_button);
		sign_up_button.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_signup_button.dispose();
			}		
		});
		
		sign_up_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String username = username_text.getText();
				final String pass = password_text.getText();
				if(!verifier.verifyname(username)){ // illegal user name
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
				}else if(!verifier.verifyname( pass)){ // invalid password
					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("Illegal Password");
					messageBox.setMessage("Password must contain 1-6 alphanumeric chars.");
					messageBox.open();
				} else
					try {
						if ( user_logics.add_user(username, pass))
						{
							MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
							messageBox.setText("SUCCESS");
							messageBox.setMessage("User "+ username + "has been succesfully signed up");
							messageBox.open();
						}
						else {  //there was error during registration
							MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
							messageBox.setText("Error");
							messageBox.setMessage("There was an error during signup. Sorry.");
							messageBox.open();
						}
					} catch (SQLException e) {
						gui_utils.raise_sql_error_window(display);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		
		
}

		
	
	
	
	protected void checkSubclass()
	{
	}
	
}
