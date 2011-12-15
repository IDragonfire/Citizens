package net.citizensnpcs.dpromoter.rank;

import java.util.HashMap;

public class DNPCPType {
	private HashMap<Integer, String> map;

	public DNPCPType() {
		loadTypes();
	}

	private void loadTypes() {
		this.map = new HashMap<Integer, String>();
		this.map.put(new Integer(2), "magic.rank.1");
		this.map.put(new Integer(3), "magic.rank.2");
		this.map.put(new Integer(4), "magic.rank.3");
	}

	public String getDNCP_Type(int uid) {
		return this.map.get(new Integer(uid));
	}
}
