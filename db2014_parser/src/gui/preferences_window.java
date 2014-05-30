package gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import db.db_queries_user;
import parser_entities.entity_movie;
import parser_entities.light_entity_movie;
import bl.movie_logics;
import bl.user_logics;
import bl.verifier;





///////////// ******* Listeners to be implemented: ******** //////////////

// rate button
// note: when rating is over, first dispose gui_utils.pref_wiw, then create new display, and then use it to create a new all_tabs_win




public class preferences_window extends Shell
{

	
	boolean can_be_opened=true;
	light_entity_movie current_movie = null;
	public preferences_window(final Display display)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		update_movie();
		if(current_movie == null)
		{		
			MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
			messageBox.setText("Error");
			messageBox.setMessage("Couldn't find any movies to rate");
			messageBox.open();
			go_to_overview();
			can_be_opened =false;
			return;
			
		}
		this.setSize(400, 300);
		this.setText("Movies Preferences");
				
		this.setLayout(new FormLayout());

		
	
		//window background
		String imgURL = ".\\src\\gui\\images\\blue_400_300.jpg";
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
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Please Rate The Following Movies:");
		headline_label.setLayoutData(gui_utils.form_data_factory(360, 30, 10, 20));
		final Font font_headline_label = new Font(display, "Ariel",17, java.awt.Font.PLAIN );
		headline_label.setFont(font_headline_label);
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline_label.dispose();
			}		
		});
		
		
		try {
			current_movie = log_in_window.user.get_unrated_movie();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//movie label
		Label movie_label = new Label(this, SWT.NONE);
		movie_label.setAlignment(SWT.CENTER);
		movie_label.setText(current_movie.get_movie_name());
		movie_label.setText(current_movie.get_movie_name());
		movie_label.setLayoutData(gui_utils.form_data_factory(370, 22, 70, 10));
		final Font font_movie_label = new Font(display, "Ariel",14, java.awt.Font.PLAIN );
		movie_label.setFont(font_movie_label);
		movie_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_movie_label.dispose();
			}		
		});
		
		
		
		//area
		Composite radios_area = new Composite(this, SWT.NONE);
		radios_area.setLayoutData(gui_utils.form_data_factory(100, 143, 100, 90));
		GridLayout grid_layout_area = new GridLayout(1, false);
		radios_area.setLayout(grid_layout_area);
		
		
		//radios
		final List<Button> radios = new ArrayList<Button>();
		
		for(int i = 0; i < 6; i++)
		{
			radios.add(new Button(radios_area, SWT.RADIO));
			if (i == 1)
				radios.get(i).setLayoutData(gui_utils.grid_data_factory(-1, 10, -1, -1, -1, -1));
		}
		
		radios.get(0).setText("Don't Know");
		radios.get(1).setText("1 - Lowest");
		radios.get(2).setText("2");
		radios.get(3).setText("3");
		radios.get(4).setText("4");
		radios.get(5).setText("5 - Highest");
		
		
		
		//rate button
		Button rate_button = new Button(this, SWT.PUSH);	
		rate_button.setText("Rate");
		rate_button.setLayoutData(gui_utils.form_data_factory(50, 30, 160, 200));
		final Font font_rate_button = new Font(display, "Ariel",13, java.awt.Font.PLAIN );
		rate_button.setFont(font_rate_button);
		rate_button.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_rate_button.dispose();
			}		
		});
		
		rate_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					int rate_desired = gui_utils.get_index_button(radios);
					handle_rating(rate_desired);
					gui_utils.raise_sql_error_window(getDisplay());
			//		current_movie = movie_logics.get_unrated_movie_by_user(log_in_window.user.get_current_user_id());
					//shahar that's is final we have to see how we make the next movie be availble///
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					gui_utils.raise_sql_error_window(display);
					e.printStackTrace();
				//} catch (SQLException e) {
					// TODO Auto-generated catch block
				//	gui_utils.raise_sql_error_window(display);
				//	e.printStackTrace();
				}
				
			
		
			
		
			}

		});
		
		

		//stop button
		Button stop_button = new Button(this, SWT.PUSH);
		stop_button.setText("Stop");
		stop_button.setLayoutData(gui_utils.form_data_factory(50, 30, 160, 300));
		final Font font_stop_button = new Font(display, "Ariel",13, java.awt.Font.PLAIN );
		stop_button.setFont(font_stop_button);
		stop_button.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_stop_button.dispose();
			}		
		});
		
		
		
		
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				display.dispose();
			}		
		});
		
	}
	
	public void handle_rating(int rate)
	{
		try {
			log_in_window.user.rate_movie(current_movie.get_movie_id(),rate );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			gui_utils.raise_sql_error_window(getDisplay());
			e.printStackTrace();
		}
	}
	public void update_movie()
	{
		try {
			current_movie = log_in_window.user.get_unrated_movie();
		} catch (SQLException e) {
			gui_utils.raise_sql_error_window(getDisplay());
			e.printStackTrace();
		}
	}
	
	
	public void go_to_overview()
	{
		gui_utils.login_win.dispose(); //closing log in window (display is closed along with it)
		
		gui_utils.display = Display.getDefault();
		gui_utils.tabs_win = new all_tabs_window(gui_utils.display); 
		gui_utils.tabs_win.open();
	}
	
	



protected void checkSubclass()
{
}

}