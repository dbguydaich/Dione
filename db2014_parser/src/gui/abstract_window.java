package gui;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class abstract_window extends Shell
{

	protected abstract_window(final Display display, int style)
	{
		super(display, style);
		
		/* icon img 16*16 */
		String img16URL = ".\\src\\gui\\images\\icon16.jpg";
		final Image img16 = new Image(display, img16URL);
		
		/* icon img 32*32 */
		String img32URL = ".\\src\\gui\\images\\icon32.jpg";
		final Image img32 = new Image(display, img32URL);
		
		/* icon img 48*48 */
		String img48URL = ".\\src\\gui\\images\\icon48.jpg";
		final Image img48 = new Image(display, img48URL);
		
		Image[] icons = {img16, img32, img48};	
		this.setImages(icons);
		
		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				img16.dispose();
				img32.dispose();
				img48.dispose();
			}
		});
		
	}
	
	
	
	protected void checkSubclass() 
	{
	}
}


