package wyj.bean;

import wyj.util.Constants;


public class RegResult {

	boolean regstatus;
	User newuser;
	public RegResult(User newuser){
		this.newuser=newuser;
		if(newuser==null) regstatus=Constants.registerFailed;
		else regstatus=Constants.registerSuccess;
	}
    public boolean getRegstatus() {
        return regstatus;
    }
    public void setRegstatus(boolean regstatus) {
        this.regstatus = regstatus;
    }
    public User getNewuser() {
        return newuser;
    }
    public void setNewuser(User newuser) {
        this.newuser = newuser;
    }
	
	
}
