package net.citizensnpcs.idragonfire.values;

import net.citizensnpcs.idragonfire.keys.DKey;

public class DIntValue extends DValue {
	private int state;

	public DIntValue(int state) {
		this.state = state;
	}

	public int getInt() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
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

	@Override
	public String toString() {
		return "[DIntValue:" + this.state + "]";
	}
}
