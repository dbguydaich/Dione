package bl;

public class verifier {

	public static boolean verifyname(String username){

	if(!username.matches("[a-zA-Z0-9]{1,10}"))
			return false;
		return true;
	}
	public static boolean verifyPass(String password){ //pasword is 1-6 characters only alpha numeric

		if(!password.matches("[a-zA-Z0-9]{1,6}"))
			return false;
		return true;
	}
}