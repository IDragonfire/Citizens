package net.citizensnpcs.idragonfire.values;

import net.citizensnpcs.idragonfire.keys.DKey;

public class DIntArrayValue extends DValue {
	private int[] intValues;

	public DIntArrayValue(int[] intValues) {
		this.intValues = intValues;
	}

	public int[] getIntValues() {
		return this.intValues;
	}

	public void setState(int[] intValues) {
		this.intValues = intValues;
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