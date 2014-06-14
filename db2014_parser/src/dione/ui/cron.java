package dione.ui;

import dione.db.db_operations;

public class cron implements Runnable
{
	
	private int minute = 1000*60;

	/**
	 * A background thread, runs every 15 minutes in order to update what the users preferences
	 */
	public cron(){}

    public void run() {

	    while(!Thread.currentThread().isInterrupted())
	    {
	        while (true)
	        {
	        	try {
	        		db_operations.fill_movie_tag_relation();
					Thread.sleep(15 * minute); /* sleep for 15 minutes */
				} catch (InterruptedException e) {
					return;
				}
	     
	           
	        	
	        }
	        
    }
    

    }




}