package gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
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

import bl.verifier;
import config.config;

//import bl.verifier;
//import runnableLogic.AddUser;
//import viewModelLayer.InputVerifier;

//import db.IdbOparations;


public class log_in_window extends Shell
{
	
	
	config config = new config();
	int window_height = config.get_window_height();
	int window_width = config.get_window_width();


	
	public log_in_window(final Display display)
	{
		super(display);
		
		this.setSize(window_width, window_height);
		final Color log_in_window_color = display.getSystemColor(SWT.COLOR_DARK_CYAN);
		this.setBackground(log_in_window_color);
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				log_in_window_color.dispose();
			}		
		});
		
		this.setLayout(new FormLayout());
		
		
		//area
		Composite area = new Composite(this, SWT.NONE);
		area.setLayoutData(gui_utils.form_data_factory(300, 300, 70, 160));
		GridLayout grid_layout_area = new GridLayout(2, false);
		area.setLayout(grid_layout_area);
		
		
		//headline label
		Label log_in_label = new Label(area, SWT.NONE);
		log_in_label.setText("    Log In Or Sign Up:");
		log_in_label.setLayoutData(gui_utils.grid_data_factory(-1, -1, 2, -1, SWT.CENTER, SWT.CENTER));
		final Font font_log_in_label = new Font(display, "Ariel",20, java.awt.Font.PLAIN );
		log_in_label.setFont(font_log_in_label);
		log_in_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_log_in_label.dispose();
			}		
		});
		
		
		
		//label username
		Label username_label = new Label(area, SWT.NONE);
		username_label.setText("Username:");
		username_label.setLayoutData(gui_utils.grid_data_factory(40, -1, -1, -1, -1, -1));
		final Font font_username_label = new Font(display, "Ariel", 12, java.awt.Font.PLAIN);
		username_label.setFont(font_username_label);
		username_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_username_label.dispose();
			}		
		});
		
		
		
		//text username
		final Text username_text = new Text(area, SWT.BORDER);
		username_text.setLayoutData(gui_utils.grid_data_factory(100, 15, 0, -1, -1, -1, -1, -1));
		username_text.setTextLimit(10);
		
		
		
		//label password
		Label password_label = new Label(area, SWT.NONE);
		password_label.setText("Password:");
		password_label.setLayoutData(gui_utils.grid_data_factory(40, -1, -1, -1, -1, -1));
		final Font font_password_label = new Font(display, "Ariel", 12, java.awt.Font.PLAIN);
		password_label.setFont(font_password_label);
		password_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_password_label.dispose();
			}		
		});
		
		
		
		//text password
		final Text password_text = new Text(area, SWT.PASSWORD | SWT.BORDER);
		password_text.setLayoutData(gui_utils.grid_data_factory(100, 15, 0, -1, -1, -1, -1, -1));
		password_text.setTextLimit(6);
		
		
		
		//log in button
		Button log_in_button = new Button(area, SWT.PUSH);
		log_in_button.setText("Log In");
		log_in_button.setLayoutData(gui_utils.grid_data_factory(80, -1, -1, -1, -1, -1));
		//////
//		log_in_button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				final String username = username_text.getText();
//				final String pass = password_text.getText();
//				if(!verifier.verifyname(username)){ // illegal user name
//					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					alertBox.setText("Illegal Username");
//					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
//					alertBox.open();
//				}else if(!verifier.verifyPass( pass)){ // invalid password
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Illegal Password");
//					messageBox.setMessage("Password must contain 1-6 alphanumeric chars.");
//					messageBox.open();
//				}
//
//				else if( db_queries.authenticate_user(username,pass)){
//					
//					//implement
//				}
//				else{ //no user found
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Error");
//					messageBox.setMessage("Password and username do not match. Try again.");
//					messageBox.open();
//				}
//				
//		
//			
//		
//			}
//
//		});
		//////

		
		
		
		
		//sign up button
		Button sign_up_button = new Button(area, SWT.PUSH);
		sign_up_button.setText("Sign Up");
		sign_up_button.setLayoutData(gui_utils.grid_data_factory(10, -1, -1, -1, -1, -1));
		
//		sign_up_button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				final String username = username_text.getText();
//				final String pass = password_text.getText();
//				if(!verifier.verifyname(username)){ // illegal user name
//					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					alertBox.setText("Illegal Username");
//					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
//					alertBox.open();
//				}else if(!verifier.verifyname( pass)){ // invalid password
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Illegal Password");
//					messageBox.setMessage("Password must contain 1-6 alphanumeric chars.");
//					messageBox.open();
//				}
//				else if ( db_queries.add_user(username, pass))
//				{
//					///implement
//				}
//				else {  //there was error during registration
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Error");
//					messageBox.setMessage("There was an error during signup. Sorry.");
//					messageBox.open();
//				}
//			}
//		});
		
		
}

		
	
	
	
	protected void checkSubclass()
	{
	}
	
}
