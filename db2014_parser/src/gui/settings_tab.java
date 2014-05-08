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
		FormData form_data_headline_label = new FormData();
		form_data_headline_label.top = new FormAttachment(0,2);
		form_data_headline_label.left = new FormAttachment(0,275);
		final Font font_headline_label = new Font(display, "Ariel",15, java.awt.Font.PLAIN ); 
		headline_label.setFont(font_headline_label);
		headline_label.setLayoutData(form_data_headline_label);	
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline_label.dispose();
			}		
		});
		
		
		//update area
		Composite update_area = new Composite(this, SWT.NONE);
		FormData form_data_update_area = new FormData(195, 110); 
		form_data_update_area.top = new FormAttachment(0, 40);
		form_data_update_area.left = new FormAttachment(0, 40);
		update_area.setLayoutData(form_data_update_area);
		GridLayout grid_layout_update_area = new GridLayout(1, false);
		update_area.setLayout(grid_layout_update_area);
		
		
		
		//update button
		Button update_button = new Button(update_area, SWT.PUSH);
		GridData grid_data_update_button = new GridData(80, 35);
		grid_data_update_button.horizontalIndent = 55;
		grid_data_update_button.verticalIndent = 10;
		update_button.setText("Data \nUpdate");
		update_button.setLayoutData(grid_data_update_button);
		
		
		
		//update label
		Label update_label = new Label(update_area, SWT.NONE);
		GridData grid_data_update_label = new GridData(180, 35);
		//grid_data_update_label.horizontalIndent = 55;
		//grid_data_update_label.verticalIndent = 10;
		final Font font_update_label = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		update_label.setFont(font_update_label);
		update_label.setText("Note: This operation may\n          take a while...");
		update_label.setLayoutData(grid_data_update_label);
		update_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_update_label.dispose();
			}		
		});
		
		
		
	}

}
