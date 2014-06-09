package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class comment_window extends abstract_window
{
	public comment_window(final Display display, final int movie_id)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		
		this.setSize(250, 150);
		this.setText("Make a Comment");
				
		this.setLayout(new FormLayout());
		
		/* Window Background */
		String imgURL = ".\\src\\gui\\images\\blue_300.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);

		/* comment text */
		Text comment_text = new Text(this, SWT.WRAP);
		comment_text.setLayoutData(gui_utils.form_data_factory(245, 80, 0, 0));
		comment_text.setTextLimit(100);

		/* comment button */
		Button comment_button = new Button(this, SWT.PUSH);
		comment_button.setLayoutData(gui_utils.form_data_factory(70, 35, 85, 90));
		comment_button.setText("Comment");
		
		
		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				background.dispose();
			}		
		});
	}
}
