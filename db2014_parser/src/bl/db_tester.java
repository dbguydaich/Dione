package bl;

import java.sql.SQLException;

import db.db_operations;
import db.db_operations.invocation_code;

public class db_tester {

	public static void main(String[] args) 
	{
		try 
		{
			System.out.println(
			db_operations.was_there_an_invocation(invocation_code.YAGO_UPDATE));		
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
