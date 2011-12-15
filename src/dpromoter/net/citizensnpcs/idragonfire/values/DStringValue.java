package net.citizensnpcs.idragonfire.values;

import net.citizensnpcs.idragonfire.keys.DKey;

public class DStringValue extends DValue {
	private String s;

	public DStringValue(String s) {
		this.s = s;
	}

	public String getString() {
		return this.s;
	}

	@Override
	public String getSQL_UPDATE(DKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getYMLStringForYMLSave(DKey key) {
		// TODO Auto-generated method stub
		return null;
	}

}
