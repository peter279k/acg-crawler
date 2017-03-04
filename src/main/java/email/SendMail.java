package email;

import auth.Auth;;

public class SendMail {
	public String[] getAuthenticate() {
		Auth auth = new Auth();
		
		return auth.getAuth();
	}
}
