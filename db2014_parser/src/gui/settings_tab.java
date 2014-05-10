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
		
		
		
		//update area
		Composite update_area = new Composite(this, SWT.NONE);
		update_area.setLayoutData(gui_utils.form_data_factory(195, 110, 40, 40));
		GridLayout grid_layout_update_area = new GridLayout(1, false);
		update_area.setLayout(grid_layout_update_area);
		
		
		
		//update button
		Button update_button = new Button(update_area, SWT.PUSH);
		update_button.setText("Data \nUpdate");
		update_button.setLayoutData(gui_utils.grid_data_factory(80, 35, 55, 10, -1, -1, -1, -1));
		
		
		
		//update label
		Label update_label = new Label(update_area, SWT.NONE);
		final Font font_update_label = new Font(display, "Ariel",11, java.awt.Font.PLAIN );
		update_label.setFont(font_update_label);
		update_label.setText("Note: This operation may\n          take a while...");
		update_label.setLayoutData(gui_utils.grid_data_factory(180, 35, -1, -1, -1, -1, -1, -1));
		update_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_update_label.dispose();
			}		
		});
		
		
	}

}
