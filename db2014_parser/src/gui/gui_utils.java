package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import bl.movie_logics;
import parser_entities.entity_person;
import parser_entities.light_entity_movie;


public class gui_utils
{
	
	static int RESULTS_OPEN = 0;
	static all_tabs_window tabs_win = null;
	static log_in_window login_win = null;
	static preferences_window pref_win = null;
	static import_window import_win = null;
	static Display display;
	
	
	
	public static void main(String args[])
	{

		display = new Display();

		
		login_win = new log_in_window(display);
		//movie_details_window movie_win = new movie_details_window(display, 1);
		//tabs_win = new all_tabs_window(display);
		//pref_win = new preferences_window(display);
		//import_win = new import_window(display);
		
		
		login_win.open();
		//movie_win.open();
		//tabs_win.open();
		//pref_win.open();
		//import_win.open();
		
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
	
	
	
	public static void raise_sql_error_window(Display display)
	{
		MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
		messageBox.setText("Error");
		messageBox.setMessage("SQL SERVER ERROR. Try again.");
		messageBox.open();
	}
	
	
	public static List<String> convert_person_string(List<entity_person> persons)
	{
		List<String> result = new ArrayList();
		for (entity_person a: persons)
		{
			result.add(a.get_person_name());
		}
		return result;
	}
	
	public static List<String> convert_movies_entity_to_string(List<light_entity_movie> movies)
	{
		List<String> result = new ArrayList<String>();
		
		
		for (light_entity_movie movie: movies)
		{
			result.add(movie.get_movie_name());
		}
		return result;
	}
	
	public static void get_text_button(List<Button> from,List<Boolean> to)
	{
		for (Button a: from)
		{
			to.add((a.getSelection()));
		}
	}
	
	public static int get_index_button(List<Button> from)
	{
		for (int i =0;i<from.size();i++)
		{
			if (from.get(i).getSelection()==true)
				return i;
		
		}
		return -1;//no button has been pressed
	}

	
	
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
	
	public static List<Integer> get_genres_id( List<Boolean> pressed_genres, List<String> genres)
	{
		List<Integer> result = new ArrayList<Integer>();
		for (int i=0; i< pressed_genres.size() ; i++)
		{
			if(pressed_genres.get(i)==true) //pressed
			{
				try {
					result.add(movie_logics.get_genre_id(genres.get(i)));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}






	
	
	

	
	
	
}
