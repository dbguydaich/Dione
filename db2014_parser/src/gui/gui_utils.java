package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import parser_entities.entity_person;
import parser_entities.light_entity_movie;


public class gui_utils
{
	
	static int RESULTS_OPEN = 0;
	static List<movie_details_window> movie_windows = new ArrayList<movie_details_window>();
	static all_tabs_window tabs_win = null;
	static log_in_window login_win = null;
	static preferences_window pref_win = null;
	static import_window import_win = null;
	static import_progress_window import_progress_win = null;
	static Display display;
	static boolean EXIT_ON_LOGIN = true;
	static parser_entities.Importer my_importer = null;
	public static ExecutorService executor;
	
	
	/**
	 * Launcher of the Dione application
	 */
	public static void launch()
	{
		
		executor = Executors.newFixedThreadPool(5);
		
		final Thread cron = new Thread(new cron());
		executor.execute(cron);
		
		display = new Display();
		
		
		try {
			/* If First Import is Required */
			if(!db.db_operations.was_there_an_invocation(db.db_operations.invocation_code.YAGO_UPDATE))
			{
				/* open import window */
				import_win = new import_window(display);
				import_win.open();
			}
			
			else
			{
				/* open log in window */
				login_win = new log_in_window(display);
				login_win.open();
			}
		} catch (SQLException e) {
			//shachar: handle catch, show mssg
			login_win = new log_in_window(display);
			login_win.open();
		}
	
		
		/* SWT Event Loop */
		while (!display.isDisposed()) 
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		
	}
	

	
	
	
	/**
	 * default value: -1 (for all params) 
	 * @param width
	 * @param height
	 * @param horizontalIndent
	 * @param verticalIndent
	 * @param horizontalSpan
	 * @param verticalSpan
	 * @param horizontalAlignment
	 * @param verticalAlignment
	 * @return GridData instance
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
	
	
	/**
	 * default value: -1 (for all params) 
	 * @param horizontalIndent
	 * @param verticalIndent
	 * @param horizontalSpan
	 * @param verticalSpan
	 * @param horizontalAlignment
	 * @param verticalAlignment
	 * @return GridData instance
	 */
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
	
	
	/**
	 * expects a valid width and height parameters.
	 * default value: -1 (for top and left only)  
	 * @param width
	 * @param height
	 * @param top
	 * @param left
	 * @return FormData instance
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
	
	
	/**
	 * Open a message box displaying "SQL ERROR" message
	 * @param display
	 */
	public static void raise_sql_error_window(Display display)
	{
		MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
		messageBox.setText("Error");
		messageBox.setMessage("SQL SERVER ERROR. Try again.");
		messageBox.open();
	}
	
	
	/**
	 * Creates a list of String out of a list of entity_person
	 * @param persons
	 * @return
	 */
	public static List<String> convert_person_string(List<entity_person> persons)
	{
		List<String> result = new ArrayList<String>();
		for (entity_person a: persons)
		{
			result.add(a.get_person_name());
		}
		return result;
	}
	
	
	/**
	 * Creates a list of String out of a list of light_entity_movie
	 * @param persons
	 * @return
	 */
	public static List<String> convert_movies_entity_to_string(List<light_entity_movie> movies)
	{
		List<String> result = new ArrayList<String>();
		
		
		for (light_entity_movie movie: movies)
		{
			result.add(movie.get_movie_name());
		}
		return result;
	}
	
	
	/**
	 * Creates a list of booleans out of a list of buttons (radios or checkboxes)
	 * @param from
	 * @param to
	 */
	public static void get_text_button(List<Button> from,List<Boolean> to)
	{
		for (Button a: from)
		{
			to.add((a.getSelection()));
		}
	}
	
	/**
	 * @param from
	 * @return first index of button chosen, -1 if no button selected
	 */
	public static int get_index_button(List<Button> from)
	{
		for (int i =0;i<from.size();i++)
		{
			if (from.get(i).getSelection()==true)
				return i;
		}
		return -1;//no button has been pressed
	}

	
	/**
	 * Converts a list of booleans into an array
	 * @param from
	 * @return
	 */
	public static boolean[] convert_list_array(List<Boolean> from)
	{
		boolean[] result = new boolean[from.size()];
		int i =0;
		for ( Boolean a : from)
		{
			result[i]= a;
			i++;
		}
		return result;
	}
	

	/** 
	 * @param pressed_genres
	 * @param genres
	 * @return a list of String represents the genres that have been selected 
	 */
	public static List<String> get_genres_string( List<Boolean> pressed_genres, List<String> genres)
	{
		List<String> result = new ArrayList<String>();
		for (int i=0; i< pressed_genres.size() ; i++)
		{
			if(pressed_genres.get(i)==true) //pressed
			{
					result.add(genres.get(i));	
			}
		}
		return result;
	}


	/**
	 * exiting all the threads handled by executer
	 */
	public static void exist_threads()
	{
		executor.shutdownNow();
	}



	
	
	

	
	
	
}
