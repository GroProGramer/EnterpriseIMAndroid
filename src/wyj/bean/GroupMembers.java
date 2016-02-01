package wyj.bean;

import java.util.Arrays;
import java.util.List;

public class GroupMembers {

	private List<String> members;

	public GroupMembers(String[] members){
	    this.members=Arrays.asList(members);
	}
	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	
	
}
