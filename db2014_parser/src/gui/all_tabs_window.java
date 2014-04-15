package gui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import config.config;


public class all_tabs_window extends Shell
{
	
	public all_tabs_window(final Display display)
	{
		super(display);
	
		//config config = new config();
		final int window_height = 480;
		final int window_width = 640;

		this.setSize(window_width, window_height);
		this.setText("MovieBook");
		
		this.setLayout(new FillLayout());
		TabFolder tab_folder = new TabFolder(this, SWT.NONE);
		
		//tab1
		TabItem tab1 = new TabItem(tab_folder, SWT.NONE);
	    tab1.setText("Search Movie");
	    
	   
	    //tab2
	    TabItem tab2 = new TabItem(tab_folder, SWT.NONE);
	    tab2.setText("Add a Friend");
	    
		
	    
	    //tab3
		TabItem tab3 = new TabItem(tab_folder, SWT.NONE);
	    tab3.setText("Tab 3");
	    
	  
	   
	    //tab4
	    TabItem tab4 = new TabItem(tab_folder, SWT.NONE);
	    tab4.setText("Tab 4");
	    
	    
	    search_movie_tab search_movie = new search_movie_tab(display, tab_folder, SWT.NONE);
	    tab1.setControl(search_movie);
	    
	    search_friends_window search_friends = new search_friends_window(display, tab_folder, SWT.NONE);
	    tab2.setControl(search_friends);
	}
	
	public static void main(String args[])
	{
		Display display = new Display();
		all_tabs_window tabs_win = new all_tabs_window(display);
		
		tabs_win.open();
		
		while (!display.isDisposed()) 
		{
			 if (!display.readAndDispatch())
			 {
				 display.sleep();
			 }
		}
	}



protected void checkSubclass()
{
}


}