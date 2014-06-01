package parser_entities.loaders;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import parser_entities.entity_person;

import db.db_operations;
import db.db_queries_movies;
import db.db_queries_persons;
import db.jdbc_connection_pooling;


/** A Bussiness Logic class for import of data. 
 ** Recieves parsed and normalized data in sets and maps,
 ** updates db according to existing items, types of import
 ** (insert, update), etc. **/
public abstract class abstract_loader extends db_operations{

		public static final int 		BATCH_SIZE =			  	1000;
		protected String 				entity_table_name; 	/* the table updated*/
		public static final boolean 	DEBUG_LOAD = false; 
		
		
		public abstract_loader()
		{
			
		}
		
		abstract protected void sync_update_tables() throws SQLException;
		abstract protected void set_perpared_statments(Connection db_conn) throws SQLException;
		abstract protected int create_statments(Object obj) throws SQLException;
		abstract protected int execute_batches();
		
		/** performs a batch update, with 
		 * **/
		public void load_batch(Collection<?> update_entity)
		{
			int batch_count = 0;
			int fail_count=0;
			Iterable<Object> update_entities;
			try {
				/*update relevant tables with db*/
				sync_update_tables();
				
				/*set prepared statments against connection*/
				Connection db_conn = getConnection();
				set_perpared_statments(db_conn);
				for (Object obj : update_entity) 
				{
					batch_count += create_statments(obj);
					if (batch_count % BATCH_SIZE == 0)
					{
						fail_count += execute_batches();
						if (DEBUG_LOAD)
							break;
					}
				}
				/*execute remainder*/ 
				fail_count += execute_batches();
				}
				catch (Exception ex){
					
				}
			System.out.println(this.entity_table_name +"Table updates:\n\t failed: " + fail_count + " out of: " + batch_count);	
		}
			
		/**
		 * 
		 * @param 	stmt - the batch to perform
		 * @return	int fail_count - the amount of failed statements 
		 * executes a bath statement, and handles returns fail count
		 * in all possible scenarios
		 */
		protected int execute_batch(PreparedStatement stmt)
		{
			int fail_count=0;
			try{
				stmt.executeBatch();
			}
			catch(BatchUpdateException batch_ex){
				/* at least one command failed, see how many*/
				int[] batch_results = batch_ex.getUpdateCounts();
				int i=0;
				/* driver stopped execution after faulty statement*/
				if (batch_results.length != BATCH_SIZE)
					fail_count += BATCH_SIZE - batch_results.length; 
				else { /* driver tried executing all commands - find fails*/
					for (i=0; i< batch_results.length; i++)
						if (batch_results[i] == PreparedStatement.EXECUTE_FAILED)
							fail_count++;
				}
			}
			catch (Exception ex) { /*some other issue*/
				fail_count += BATCH_SIZE;
				ex.printStackTrace();
			}
			return fail_count; 
		}
		
				
	


}
