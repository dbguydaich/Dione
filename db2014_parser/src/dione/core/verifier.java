package dione.core;

public class verifier {

	
	
	
	/**
	 * 
	 * @param username
	 * @return true if name is alpha numeric, and contains 1-10 chars.
	 */
	public static boolean verifyname(String username){
	if(!username.matches("[a-zA-Z0-9]{1,10}"))
			return false;
		return true;
	}
	
	
	/**
	 * 
	 * @param password
	 * @return  true if pass is alpha numeric, and contains 1-10 chars.
	 */
	public static boolean verifyPass(String password){ //pasword is 1-10 characters only alpha numeric

		if(!password.matches("[a-zA-Z0-9]{1,10}"))
			return false;
		return true;
	}
	/**
	 * 
	 * @param password
	 * @return  true if pass is alpha numeric, and contains 4-10 chars.
	 */
	public static boolean verifyPassSignUp(String password){ //pasword is 4-10 characters only alpha numeric

		if(!password.matches("[a-zA-Z0-9]{4,10}"))
			return false;
		return true;
	}
}