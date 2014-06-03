package gui;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import db.db_operations;

public class cron implements Runnable {

	
	
	private int minute = 1000*60;
//	private Display display =new Display();
//	private Shell shell =new Shell(display);
	public cron()
	{
	//	display = new Display();
	//	shell = new Shell(display);
	}
	
//	private Display display;
//	public cron(Display display)
//	{
//		this.display= display;
//	}
	


    public void run() {
    	
    	Display display = new Display();
    	Shell shell =new Shell(display);
   
	    while(!Thread.currentThread().isInterrupted())
	    {
	        while (true)
	        {
	        	try {
	        		
	        		try {
						db_operations.fill_movie_tag_relation();
						///////matan please add here the second function you want
					} catch (SQLException e) {
						
			        	
		        		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING);
						messageBox.setText("Error");
						messageBox.setMessage("There was an error while making the preferances algorithm calculations. Sorry."); 
						//matan please phraze it as you wish..
						messageBox.open();
						
					}
	
	        		
	        	
	        	
				
	        		System.out.println("cron task has started");
					Thread.sleep(15*minute); ///15 minutes
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					return;
				}
	     
	           
	        	
	        }
	        
    }
    

    }




}