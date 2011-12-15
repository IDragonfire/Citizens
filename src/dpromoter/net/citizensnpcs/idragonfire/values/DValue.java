package net.citizensnpcs.idragonfire.values;

import net.citizensnpcs.idragonfire.keys.DKey;

public abstract class DValue {

	public abstract String[][] getYMLStringForYMLSave(DKey key);

	public abstract String getSQL_UPDATE(DKey key);
}