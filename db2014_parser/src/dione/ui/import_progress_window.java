package dione.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;

public class import_progress_window extends abstract_window
{
	
	/* possible id's of events sent from importer */
	int USER_TERMINATE = -2;
	int FAILED_TERMINATE = -1;
	int FINE_TERMINATE = 0;
	int PROCESSING = 1;
	
	ProgressBar prog_bar = null;
	Label percent_label = null;
	boolean exit_import = false; /* indicates whether user clicked "abort" button */
	boolean exit_at_all = false; 
	
	public import_progress_window(final Display display)
	{
		super(display, SWT.TITLE);
		
		this.setSize(230, 100);
		this.setText("Loading data...");
				
		/* progress bar */
		prog_bar = new ProgressBar(this, SWT.NONE);
		prog_bar.setBounds(0, 0, 230, 30);
		prog_bar.setMaximum(100);
		prog_bar.setMinimum(0);
		prog_bar.setSelection(0);
		
		/* percent label */
		percent_label = new Label(this, SWT.NONE);
		percent_label.setBounds(20, 43, 90, 40);
		percent_label.setText("Progress: 0%");
		
		/* abort button */
		Button abort_button = new Button(this, SWT.PUSH);
		abort_button.setText("Abort");
		abort_button.setBounds(120, 35, 70, 35);
		final Font font_abort_button = new Font(display, "Ariel", 12, SWT.NONE);
		abort_button.setFont(font_abort_button);

		/* Disposal Listener */
		abort_button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				font_abort_button.dispose();
			}
		});
		

		/* new importer */
		gui_utils.my_importer = new dione.parsing.Importer();  
		gui_utils.my_importer.addActionListener(new ActionListener() {
		     @Override
		     /* the action that will take place when importer notify a progress that made */
		     public void actionPerformed(final ActionEvent event) {
		      Display.getDefault().asyncExec(new Runnable() 
		      {
		    	  public void run()
		          {
		    		  int event_id = event.getID(); /* event type */
		    		  int progress_status = (int) gui_utils.my_importer.get_progress_percent(); /* progress percent */
		    		  if(progress_status > 100)
		    			 progress_status = 100;
		    		  prog_bar.setSelection(progress_status);
		    		  percent_label.setText("Progress: " + progress_status +   "%");
		    		  
		    		  	/* data update has finished successfully */
				    	 if(event_id == FINE_TERMINATE) 
				    	 {
			    			 handle_finish_import_bar(true);
				    	 }
				    	 
				    	 /* update terminated unsuccessfully */	 
				    	 if(event_id == FAILED_TERMINATE)
				    	 {
				    		 MessageBox messageBox = new MessageBox(gui_utils.display.getActiveShell(), SWT.ICON_WARNING);
				    		 messageBox.setText("Failure");
				    		 messageBox.setMessage("Data import has failed");
				   			 messageBox.open();
				    			 
				   			 handle_finish_import_bar(false);
				   		 }
				    	
				    	 /* update terminated by user and thread already terminated */
				    	 if(exit_import && event_id == USER_TERMINATE)
				    	 {				  
				    		 exit_import = false;
				    		 MessageBox messageBox = new MessageBox(gui_utils.display.getActiveShell(), SWT.ICON_WORKING);
			    			 messageBox.setText("Import Aborted");
			    			 messageBox.setMessage("Data import Aborted");
			    			 messageBox.open();
			    			 
			    			 handle_finish_import_bar(false);
				    	 }
		          }
		 
		      });
		     }
		 });
		
		
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if(!gui_utils.my_importer.is_thread_terminated())
				{
					gui_utils.my_importer.terminate_thread();
				}
			}
		});
		
		
		/* abort Listener */
		abort_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				exit_import = true;
				exit_at_all = true;
				percent_label.setText("Aborting...");
				gui_utils.my_importer.terminate_thread(); /* terminating the importer thread  */
			}
		});
		
		/* importer will run in a new thread */
		Thread thread = new Thread(gui_utils.my_importer);
        thread.start();
		
	}

	
	/**
	 * if done == true: return to gui utils (or open log in window)
	 * if done == false: return to gui utils (or return to import window)
	 * @param done(boolean) - true if import succeeded, false otherwise
	 */
	static void handle_finish_import_bar(boolean done)
	{
		gui_utils.import_progress_win.dispose();
		
		if(done) /* import succeeded */
		{
			if(gui_utils.import_win != null) 
				if(!gui_utils.import_win.isDisposed()) /* it was the first data import */
				{
					gui_utils.EXIT_ON_LOGIN = false; 
					gui_utils.import_win.dispose(); /* disposing import window, display remains undisposed */
				
					gui_utils.login_win = new log_in_window(gui_utils.display);
					gui_utils.login_win.open(); /* opening log in window */
				}
		}
		
	}
	


}
