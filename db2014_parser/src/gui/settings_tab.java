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
import org.eclipse.swt.widgets.Text;

import bl.verifier;



/////**** Listeners to be implemented ****/////

//update_button
//log out button
//username upply button
//password upply button




public class settings_tab extends Composite
{
	
	public settings_tab(final Display display, Composite parent, int style)
	{
		super(parent, style);
		
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		
		
		//window background
		String imgURL = ".\\src\\gui\\images\\blue_640_480_3.jpg";
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
		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Settings");
		final Font font_headline_label = new Font(display, "Ariel",15, java.awt.Font.PLAIN ); 
		headline_label.setFont(font_headline_label);
		headline_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 2, 275));	
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline_label.dispose();
			}		
		});
		
		
		//update button
		Button update_button = new Button(this, SWT.PUSH);
		update_button.setText("Data Update");
		update_button.setLayoutData(gui_utils.form_data_factory(-1, -1, 70, 25));
		final Font font_update_button = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		update_button.setFont(font_update_button);
		update_button.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_update_button.dispose();
			}		
		});
		
		
		//update label
		Label update_label = new Label(this, SWT.NONE);
		final Font font_update_label = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		update_label.setFont(font_update_label);
		update_label.setText("Note: This operation may take a while...");
		update_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 75, 140));
		update_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_update_label.dispose();
			}		
		});
		
		
		//log out button
		Button log_out_button = new Button(this, SWT.PUSH);
		log_out_button.setText("Log Out");
		log_out_button.setLayoutData(gui_utils.form_data_factory(-1, -1, 110, 25));
		final Font font_logout_button = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		log_out_button.setFont(font_logout_button);
		log_out_button.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_logout_button.dispose();
			}		
		});
		
		
		
		
		log_out_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			log_in_window.user=null;
			log_in_window login_win = new log_in_window(display);
			login_win.open();
				
			}
		});
		
		
		
		
		//username area
		Composite username_area = new Composite(this, SWT.BORDER);
		username_area.setLayoutData(gui_utils.form_data_factory(250, 160, 150, 25));
		username_area.setLayout(new GridLayout(2, false));
		
		
		
		//username label
		Label username_label = new Label(username_area, SWT.NONE);
		final Font font_username_label = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		username_label.setFont(font_username_label);
		username_label.setText("Change Username");
		username_label.setLayoutData(gui_utils.grid_data_factory(50, -1, 2, 1, -1, -1));
		username_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_username_label.dispose();
			}		
		});
		
		
		//new username label
		Label new_username_label = new Label(username_area, SWT.NONE);
		final Font font_new_username_label = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		new_username_label.setFont(font_new_username_label);
		new_username_label.setText("New username");
		new_username_label.setLayoutData(gui_utils.grid_data_factory(-1, 15, -1, -1, -1, -1));
		new_username_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_new_username_label.dispose();
			}		
		});
				
		
		
		//username text
		final Text username_text = new Text(username_area, SWT.BORDER);
		username_text.setLayoutData(gui_utils.grid_data_factory(10, 15, -1, -1, -1, -1));

		
		
		//username password label
		Label username_password_label = new Label(username_area, SWT.NONE);
		final Font font_username_password_label = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		username_password_label.setFont(font_username_password_label);
		username_password_label.setText("Password for verification");
		username_password_label.setLayoutData(gui_utils.grid_data_factory(-1, 5, -1, -1, -1, -1));
		username_password_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_username_password_label.dispose();
			}		
		});
		
		
		//username_password text
		final Text username_pass_text = new Text(username_area, SWT.PASSWORD | SWT.BORDER);
		username_pass_text.setLayoutData(gui_utils.grid_data_factory(10, 5, -1, -1, -1, -1));

				
	
		
		//username apply button
		Button username_upply_button = new Button(username_area, SWT.PUSH);
		username_upply_button.setText("Apply");
		final Font font_username_upply_button = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		username_upply_button .setFont(font_username_upply_button);
		username_upply_button.setLayoutData(gui_utils.grid_data_factory(90, 15, 2, -1, -1, -1));
		username_upply_button .addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_username_upply_button.dispose();
			}		
		});
		
		username_upply_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String username = username_text.getText();
				final String pass = username_pass_text.getText();
				if(!verifier.verifyname(username))
				{ // illegal user name
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("user name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
				}
				try {
					if(log_in_window.user.update_name(username, log_in_window.user.get_current_user_id(),pass))
					{
						MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
						messageBox.setText("SUCCESS");
						messageBox.setMessage("username has been successfully changed" );
						messageBox.open();
					}
					else
					{
						MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
						messageBox.setText("Failure");
						messageBox.setMessage("username couldn't be changed!" );
						messageBox.open();

					}
				} catch (SQLException e) {
					gui_utils.raise_sql_error_window(display);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
				
		
		
		//password area
		Composite password_area = new Composite(this, SWT.BORDER);
		password_area.setLayoutData(gui_utils.form_data_factory(250, 160, 150, 325));
		password_area.setLayout(new GridLayout(2, false));
		
		
		//password label
		Label password_label = new Label(password_area, SWT.NONE);
		final Font font_password_label = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		password_label.setFont(font_password_label);
		password_label.setText("Change Password");
		password_label.setLayoutData(gui_utils.grid_data_factory(50, -1, 2, 1, -1, -1));
		password_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_password_label.dispose();
			}		
		});
		
		
		
		//new password label
		Label new_password_label = new Label(password_area, SWT.NONE);
		final Font font_new_password_label = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		new_password_label.setFont(font_new_password_label);
		new_password_label.setText("New password");
		new_password_label.setLayoutData(gui_utils.grid_data_factory(-1, 15, -1, -1, -1, -1));
		new_password_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_new_username_label.dispose();
			}		
		});
		
		
		//password text
		final Text password_text = new Text(password_area, SWT.PASSWORD | SWT.BORDER);
		password_text.setLayoutData(gui_utils.grid_data_factory(10, 15, -1, -1, -1, -1));
		
		
		//old password label
		Label old_password_label = new Label(password_area, SWT.NONE);
		final Font font_old_password_label = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		old_password_label.setFont(font_old_password_label);
		old_password_label.setText("Current password");
		old_password_label.setLayoutData(gui_utils.grid_data_factory(-1, 5, -1, -1, -1, -1));
		old_password_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_old_password_label.dispose();
			}		
		});
		
		
		//old password text
		final Text old_password_text = new Text(password_area, SWT.PASSWORD | SWT.BORDER);
		old_password_text.setLayoutData(gui_utils.grid_data_factory(10, 5, -1, -1, -1, -1));
		
		//password apply button
		Button password_upply_button = new Button(password_area, SWT.PUSH);
		password_upply_button.setText("Apply");
		final Font font_password_upply_button = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		password_upply_button.setLayoutData(gui_utils.grid_data_factory(90, 15, 2, -1, -1, -1));
		password_upply_button .setFont(font_username_upply_button);
		password_upply_button .addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_password_upply_button.dispose();
			}		
		});
		
		password_upply_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final String pass_change_to = password_text.getText();
				final String pass = old_password_text .getText();
				if(!verifier.verifyPass(pass_change_to))
				{ // illegal user name
					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("Illegal Password");
					messageBox.setMessage("Password must contain 1-6 alphanumeric chars.");
					messageBox.open();
				}
				try {
					if(log_in_window.user.update_pass(pass_change_to, log_in_window.user.get_current_user_id(),pass))
					{
						MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
						messageBox.setText("SUCCESS");
						messageBox.setMessage("password has been successfully changed" );
						messageBox.open();
					}
					else
					{
						MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
						messageBox.setText("Failure");
						messageBox.setMessage("Password couldn't be changed!" );
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

}
