package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class movie_comments_window extends abstract_window
{

	public movie_comments_window(final Display display, List<String> movie_comments)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		
		this.setSize(600, 300);
		this.setText("Recent Comments");
				
		this.setLayout(new GridLayout(1, false));
		
		/* Window Background */
		String imgURL = ".\\src\\gui\\images\\blue_640_480_3.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		final Font font_comments_labels = new Font(display, "Ariel", 10, SWT.NONE);
		
		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				background.dispose();
				font_comments_labels.dispose();
			}		
		});

		
		/* comments */
		
		List<Label> movie_comments_labels = new ArrayList<Label>();
		
		int i = 0;
		for(String str: movie_comments)
		{
			movie_comments_labels.add(new Label(this, SWT.NONE));
			movie_comments_labels.get(i).setFont(font_comments_labels);
			movie_comments_labels.get(i).setText(str);
			i++;
			if(i == 6)
				break;
		}
		
		

	}
}
