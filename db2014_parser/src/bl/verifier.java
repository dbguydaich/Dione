package bl;

public class verifier {

	public static boolean verifyname(String username){

	if(!username.matches("[a-zA-Z0-9]{1,10}"))
			return false;
		return true;
	}

}