package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class import_progress_window extends Shell
{

	public import_progress_window(final Display display)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		
		this.setSize(230, 50);
		this.setText("Importing data...");
		
		ProgressBar prog_bar = new ProgressBar(this, SWT.NONE);
		prog_bar.setBounds(0, 0, 230, 50);
		prog_bar.setMaximum(100);
		prog_bar.setMinimum(0);
		
		int progress_status = 0;
		prog_bar.setSelection(0);
		int i = 0; //just for check
		
		while(progress_status < 100)
		{
			//progress_status = get_import_progress_status();
			progress_status = i; //just for check
			prog_bar.setSelection(progress_status);
			
			i++; //just for check
		}
		
		
			
	}
	
	
	protected void checkSubclass()
	{
	}
		


}
