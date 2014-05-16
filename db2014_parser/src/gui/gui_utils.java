package gui;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;


public class gui_utils
{
	
	static int RESULTS_OPEN = 0;
	static all_tabs_window tabs_win = null;
	static log_in_window login_win = null;
	static preferences_window pref_win = null;
	static Display display;
	
	
	
	
	
	public static void main(String args[])
	{

		display = new Display();

		
		login_win = new log_in_window(display);
		//movie_details_window movie_win = new movie_details_window(display);
		//tabs_win = new all_tabs_window(display);
		//pref_win = new preferences_window(display);
		
		login_win.open();
		//movie_win.open();
		//tabs_win.open();
		//pref_win.open();
		
		
		while (!display.isDisposed()) 
		{
			 if (!display.readAndDispatch())
			 {
				 display.sleep();
			 }
		}
	}
	
	
	
	
	
	
	/*
	 * -1 for disabling any parameter
	 */

	public static GridData grid_data_factory(int width, int height, int horizontalIndent, int verticalIndent, int horizontalSpan, int verticalSpan, 
											int horizontalAlignment, int verticalAlignment)
	{
		
		GridData my_grid;
		
		if(width != -1 && height != -1)
			my_grid = new GridData(width, height);
		else
			my_grid = new GridData();
		
		if(verticalIndent != -1)
			my_grid.verticalIndent = verticalIndent;
		if(horizontalIndent != -1)
			my_grid.horizontalIndent = horizontalIndent;
		if(horizontalSpan != -1)
			my_grid.horizontalSpan = horizontalSpan;
		if(verticalSpan != -1)
			my_grid.verticalSpan = verticalSpan;
		if(horizontalAlignment != -1)
			my_grid.horizontalAlignment = horizontalAlignment;
		if(verticalAlignment != -1)
			my_grid.verticalAlignment = verticalAlignment;
		
		return my_grid;
		
	}
	
	
	
	public static GridData grid_data_factory(int horizontalIndent, int verticalIndent, int horizontalSpan, int verticalSpan, 
			int horizontalAlignment, int verticalAlignment)
	{
	
		GridData my_grid = new GridData();
		
		if(verticalIndent != -1)
			my_grid.verticalIndent = verticalIndent;
		if(horizontalIndent != -1)
			my_grid.horizontalIndent = horizontalIndent;
		if(horizontalSpan != -1)
			my_grid.horizontalSpan = horizontalSpan;
		if(verticalSpan != -1)
			my_grid.verticalSpan = verticalSpan;
		if(horizontalAlignment != -1)
			my_grid.horizontalAlignment = horizontalAlignment;
		if(verticalAlignment != -1)
			my_grid.verticalAlignment = verticalAlignment;
		
		return my_grid;
	
	}
	
	
	/*
	 * expects a valid width and height parameters.
	 * -1 for disabling other parameters
	 */
	public static FormData form_data_factory(int width, int height, int top, int left)
	{
		FormData my_form;
		
		if(width != -1 && height != -1)
			my_form = new FormData(width, height);
		else
		{
			my_form = new FormData();
			if(width != -1)
				my_form.width = width;
			if(height != -1)
				my_form.height = height;
		}
		
		if(top != -1)
			my_form.top = new FormAttachment(0, top);
		if(left != -1)
			my_form.left = new FormAttachment(0, left);
		
		return my_form;
	}
	
	
	
	
}
