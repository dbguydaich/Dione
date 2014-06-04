package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class import_progress_window extends Shell
{

	ProgressBar prog_bar;
	
	public import_progress_window(final Display display)
	{
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));
		
		this.setSize(230, 50);
		this.setText("Loading data...");
		
		prog_bar = new ProgressBar(this, SWT.NONE);
		prog_bar.setBounds(0, 0, 230, 50);
		prog_bar.setMaximum(100);
		prog_bar.setMinimum(0);
		
		prog_bar.setSelection(0);
		int i = 0; //just for check
		
		/////********* WAITING for guy to finish importer ***********/////
		
//		final parser_entities.importer importer = new parser_entities.importer();  
//		importer.addActionListener(new ActionListener() {
//		     @Override
//		     public void actionPerformed(final ActionEvent event)
//		     {
//		    	 int progress_status = (int) importer.get_progress_percent();
//		    	 prog_bar.setSelection(progress_status);
//		    	 
//		    	 if(progress_status == 100) /* data importing is done */
//		    	 {
//		    		//shachar: spell check the following
//		    		MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WORKING);
//					messageBox.setText("Import Completed");
//					messageBox.setMessage("Data import has finished successfully");
//					messageBox.open();
//					
//		    		gui_utils.import_progress_win.dispose();
//		    		
//		    	    if(!gui_utils.import_win.isDisposed()) /* it was the first data import */
//		    	    {
//		    	    	gui_utils.EXIT_ON_LOGIN = false;
//		    	    	gui_utils.import_win.dispose();
//		    	     	
//			    		gui_utils.login_win = new log_in_window(gui_utils.display);
//			    		gui_utils.login_win.open();
//		    	    }
//		    	   
//		    	 }
//		     }
//		});
		
		
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				
				/* should stop the importer thread */
				
			}		
		});
		
		
		
//		while(progress_status < 100)
//		{
//			//progress_status = get_import_progress_status();
//			progress_status = i; //just for check
//			prog_bar.setSelection(progress_status);
//			
//			i++; //just for check
//		}
		
		
		
		
	}


	protected void checkSubclass()
	{
	}
		


}
