package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.swt.widgets.Text;



/////**** Listeners to be implemented ****/////

//update_button
//log out button
//username upply button
//password upply button




public class settings_tab extends Composite
{
	
	public settings_tab(Display display, Composite parent, int style)
	{
		super(parent, style);
		
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		
		final Color color_window = display.getSystemColor(SWT.COLOR_GRAY);
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				color_window.dispose();
			}		
		});
		
		this.setBackground(color_window);
		
		
		
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
		
		
		//username label
		Label username_label = new Label(this, SWT.NONE);
		final Font font_username_label = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		username_label.setFont(font_username_label);
		username_label.setText("Change username:");
		username_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 150, 25));
		username_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_username_label.dispose();
			}		
		});
		
		//username text
		Text username_text = new Text(this, SWT.BORDER);
		username_text.setLayoutData(gui_utils.form_data_factory(-1, -1, 149, 165));
		
		//username apply button
		//log out button
		Button username_upply_button = new Button(this, SWT.PUSH);
		username_upply_button.setText("Apply");
		username_upply_button.setLayoutData(gui_utils.form_data_factory(-1, -1, 147, 250));
		final Font font_username_upply_button = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		username_upply_button .setFont(font_username_upply_button);
		username_upply_button .addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_username_upply_button.dispose();
			}		
		});
		
		
		
		//password label
		Label password_label = new Label(this, SWT.NONE);
		final Font font_password_label = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		password_label.setFont(font_password_label);
		password_label.setText("Change password:");
		password_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 190, 25));
		password_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_password_label.dispose();
			}		
		});
		
		//username text
		Text password_text = new Text(this, SWT.BORDER);
		password_text.setLayoutData(gui_utils.form_data_factory(-1, -1, 189, 165));
		
		//username apply button
		//log out button
		Button password_upply_button = new Button(this, SWT.PUSH);
		password_upply_button.setText("Apply");
		password_upply_button.setLayoutData(gui_utils.form_data_factory(-1, -1, 187, 250));
		final Font font_password_upply_button = new Font(display, "Ariel",10, java.awt.Font.PLAIN );
		password_upply_button .setFont(font_username_upply_button);
		password_upply_button .addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_password_upply_button.dispose();
			}		
		});
		
		
	}

}
