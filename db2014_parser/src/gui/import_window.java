package gui;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import db.db_operations.*;

/**
 * Import Data Window
 */
public class import_window extends abstract_window
{
	
	Timestamp time = null;

	public import_window(final Display display)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
	
		this.setSize(250, 230);
		this.setText("Import Data");
				
		this.setLayout(new FormLayout());
		
		/* Window Background */
		String imgURL = ".\\src\\gui\\images\\blue_300.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				background.dispose();				
				if(gui_utils.EXIT_ON_LOGIN == true) /* EXIT */
				{
					display.dispose();
					gui_utils.exist_threads();
				}
				else
					gui_utils.EXIT_ON_LOGIN = true;
			}		
		});
		
		
		
		//headline label
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Before you run Dione on\na new device, a massive data\nimport is needed");
		headline_label.setLayoutData(gui_utils.form_data_factory(225, 65, 10, 10));
		final Font font_headline_label = new Font(display, "Ariel",13, SWT.NONE );
		headline_label.setFont(font_headline_label);
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline_label.dispose();
			}		
		});
		
		
		//import button
		Button import_button = new Button(this, SWT.PUSH);
		import_button.setText("Import Data");
		import_button.setLayoutData(gui_utils.form_data_factory(100, 50, 100, 70));
		final Font font_import_button = new Font(display, "Ariel",13, SWT.NONE);
		import_button.setFont(font_import_button);
		import_button.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_import_button.dispose();
			}		
		});

		//import button listener
		import_button.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent arg0)
			{
				try {
					time = db.db_operations.is_currently_invoked(invocation_code.YAGO_UPDATE);
					/* if not currently running an update */
					if(time == null)
					{
						if(gui_utils.import_progress_win != null)
						{
							/* if progress bar is already open */
							if(!gui_utils.import_progress_win.isDisposed())
							{
								MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
								messageBox.setText("Error");
								messageBox.setMessage("Import is already in progress.");
								messageBox.open();
							}
							else //running progress bar
							{
								gui_utils.import_progress_win = new import_progress_window(display);
								gui_utils.import_progress_win.open();
							}
						}
					
						else //running progress bar
						{	
							gui_utils.import_progress_win = new import_progress_window(display);
							gui_utils.import_progress_win.open();
						}
					}
					/* someone is currently updating the db  */
					else
					{
						MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
						messageBox.setText("Error");
						messageBox.setMessage("Can't start an import: Another data update is already in progress. Please exit ant try again later");
						messageBox.open();
					}
					
				} catch (SQLException e) {
					gui_utils.raise_sql_error_window(gui_utils.display);
					return;
				}
			}
			
		});
		
		
		
		//note label
		Label note_label = new Label(this, SWT.NONE);
		note_label.setText("Note: This operation may take a while...");
		note_label.setLayoutData(gui_utils.form_data_factory(225, 20, 160, 10));
		final Font font_note_label = new Font(display, "Ariel",10, SWT.NONE);
		note_label.setFont(font_note_label);
		note_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_note_label.dispose();
			}		
		});
		
	}
	
	
}
