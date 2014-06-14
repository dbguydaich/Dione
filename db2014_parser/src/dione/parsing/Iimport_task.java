package dione.parsing;

import java.beans.PropertyChangeListener;

/**
 * a simple interface for all importer tasks - parsers and loaders
 * @author GUY
 *
 */
public interface Iimport_task {
	
	/** import tasks must allow the importer to listen on proprty change events - 
	 * namely, tasks notify the importer, of their progress within the current task
	 * be it a file parsing, or a batch-creation and load task. 
	 ** */
	
	public void notifyListeners(Object object, String property,
			String oldValue, String newValue);

	/**
	 * adds listener to list
	 * @param newListener
	 */
	public void addChangeListener(PropertyChangeListener newListener);
	/**
	 *  removes listener
	 * @param newListener
	 */
	public void removeChangeListener(PropertyChangeListener newListener);
	
	/**
	 * this is the main task logic - parsing a file, loading to the db
	 * returns -1 on failure 
	 * @throws Exception **/
	public int perform_task() throws Exception;
	
	/**
	 * gets the size of the task at hand. 
	 * if parsing - the line count of the file
	 * if loading  - the collection length
	 * **/
	public int get_task_size();
	
	
	
}
