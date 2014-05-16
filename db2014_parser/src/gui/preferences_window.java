package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;





///////////// ******* Listeners to be implemented: ******** //////////////

// rate button
// note: when rating is over, first dispose gui_utils.pref_wiw, then create new display, and then use it to create a new all_tabs_win




public class preferences_window extends Shell
{

	public preferences_window(final Display display)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		
		this.setSize(400, 300);
		this.setText("Movies Preferences");
				
		this.setLayout(new FormLayout());
		
		final Color preferences_window_color = display.getSystemColor(SWT.COLOR_GRAY);
		this.setBackground(preferences_window_color);
		
		//headline label
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Please Rate The Following Movies:");
		headline_label.setLayoutData(gui_utils.form_data_factory(360, 30, 10, 20));
		final Font font_headline_label = new Font(display, "Ariel",17, java.awt.Font.PLAIN );
		headline_label.setFont(font_headline_label);
		headline_label.setBackground(preferences_window_color);
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline_label.dispose();
			}		
		});
		
		
		//movie label
		Label movie_label = new Label(this, SWT.NONE);
		movie_label.setAlignment(SWT.CENTER);
		movie_label.setBackground(preferences_window_color);
		movie_label.setText("this is a movie to be rated");
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
		List<Button> radios = new ArrayList<Button>();
		
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
		
		
		
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				display.dispose();
			}		
		});
		
	}
	
	
	



protected void checkSubclass()
{
}

}