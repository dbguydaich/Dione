package dione.parsing.loaders;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



import dione.config.config;
import dione.parsing.Iimport_task;
import dione.parsing.Importer;


/**
 * A Bussiness Logic class for import of data. Recieves parsed and normalized
 * data in sets and maps, updates db according to existing items, types of
 * import (insert, update), etc.
 **/
public abstract class abstract_loader extends dione.db.db_operations implements Iimport_task {

	public static final int BATCH_SIZE = 1000; /* how many statements in batch */
	protected String entity_table_name; /* the table updated */
	private int task_size = 0; /* total size of load entity */
	private Importer Caller;
	protected PreparedStatement insert;
	protected config properties;
	protected Collection<?> update_entity;
	
	/* setters */
	public abstract_loader(Importer caller, Collection<?> update_entity) {
		this.Caller = caller;
		properties = new config();
		this.update_entity = update_entity;
		this.task_size = update_entity.size();
	}

	/* getters */
	public int get_task_size() {
		return task_size;
	}

	/**
	 * interface wrapper
	 */
	public int perform_task() throws SQLException {		
		try { 
			load_batch(this.update_entity);
			return 1;
		} catch (SQLException se) {
			System.out.println("sql error in load: "  + se.getMessage());
			return -1; 
		}
	}
	
	/**
	 * we usually want to know what already exists in the DB
	 * @throws SQLException
	 */
	abstract protected void sync_update_tables() throws SQLException;

	/**
	 * chooses the statemnt that we will execute
	 * @param db_conn
	 * @throws SQLException
	 */
	abstract protected void set_perpared_statments(Connection db_conn)
			throws SQLException;

	/**
	 * given an object, creates an instance of the statement
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	abstract protected int create_statments(Object obj) throws SQLException;

	/**
	 * performs a batched update, loading obj to predetermined table
	 * 
	 * @throws SQLException
	 *             - if some connection error occured, we catch it at the main
	 *             importer class. 
	 * **/
	public void load_batch(Collection<?> update_entity) throws SQLException {
		int batch_count = 0;
		int fail_count = 0;
		int progress = 0;
		int prev_batch_count = 0; 
		
		task_size = update_entity.size();
		Connection db_conn = null;
		try {
			/* update relevant tables with db */
			sync_update_tables();

			/* set prepared statments against connection */
			db_conn = getConnection();
			db_conn.setAutoCommit(false); 
			
			set_perpared_statments(db_conn);
			for (Object obj : update_entity) {
				if (Caller.is_thread_terminated())
					break;
				progress++;
				if (progress % 10000 == 0)
					this.notifyListeners(this, "progress", "0", (new Integer(
							progress)).toString());
				batch_count += create_statments(obj);
				if (batch_count > 0 && batch_count % BATCH_SIZE == 0 && batch_count > prev_batch_count)
				{
					prev_batch_count = batch_count;
					fail_count += execute_batches(BATCH_SIZE);
					db_conn.commit();
				}
			}
			/* execute remainder - if not terminated */
			if (!Caller.is_thread_terminated())
			{
				fail_count += execute_batches(batch_count % BATCH_SIZE);
				db_conn.commit();
			}
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception ex) {
			System.out.println("Error in Batch, Attempt to continue");
		} finally {
			try {
				db_conn.commit();
				if (db_conn != null)
					db_conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("loading to table: ");
		sb.append(this.entity_table_name + "\n");
		sb.append("\tEntites Passed:  ");
		sb.append(this.task_size + "\n");
		sb.append("\tRequired statements: ");
		sb.append(batch_count + "\n");
		sb.append("\tFailed statements: ");
		sb.append(fail_count + "\n");
		System.out.println(sb.toString());
	}

	/**
	 * 
	 * @param stmt
	 *            - the batch to perform
	 * @return int fail_count - the amount of failed statements executes a bath
	 *         statement, and handles returns fail count in all possible
	 *         scenarios
	 * @throws SQLException
	 */
	protected int execute_batch(PreparedStatement stmt, int stmt_batch_size)
			throws SQLException {
		int fail_count = 0;
		try {
			stmt.executeBatch();
		} catch (BatchUpdateException batch_ex) {
			/* at least one command failed, see how many */
			int[] batch_results = batch_ex.getUpdateCounts();
			int i = 0;
			/* driver stopped execution after faulty statement */
			if (batch_results.length != stmt_batch_size)
				fail_count += stmt_batch_size - batch_results.length;
			else { /* driver tried executing all commands - find fails */
				for (i = 0; i < batch_results.length; i++)
					if (batch_results[i] == PreparedStatement.EXECUTE_FAILED)
						fail_count++;
			}
		}
		return fail_count;
	}

	/**
	 * executes user specific batched statements. overridden by extender
	 * */
	protected int execute_batches(int batch_size) throws SQLException {
		int fail_count = 0;
		fail_count += execute_batch(this.insert, batch_size);
		return fail_count;
	}
	
	/**
	 * executes closes used statements. overridden by extender with more
	 * than an insert statement
	 * */
	protected void close_stmt(){
		try{ 
			this.insert.close();
		} catch (SQLException se) {
			System.out.println("ERROR closing statemnt: " + se.getMessage());
		}
	}

	/* helper methods, listeners */

	/**
	 * We allow clients to register to property change events, that monitor the
	 * current file processing. This helps us display the import progress
	 **/
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

	/**
	 * notifies all listeners on event, mostly progress in parsing
	 * 
	 * @param object
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	public void notifyListeners(Object object, String property,
			String oldValue, String newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, property,
					oldValue, newValue));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}

	public void removeChangeListener(PropertyChangeListener newListener) {
		listener.remove(newListener);
	}

}
