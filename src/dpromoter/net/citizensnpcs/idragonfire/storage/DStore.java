package net.citizensnpcs.idragonfire.storage;

import net.citizensnpcs.idragonfire.datamanager.DDataManager;
import net.citizensnpcs.idragonfire.keys.DKey;
import net.citizensnpcs.idragonfire.values.DValue;

/**
 * SUPER Store class
 * 
 * @author IDragonfire
 * 
 */
public class DStore {
	protected DDataManager datamanger;

	public DStore(DDataManager dataManager) {
		this.datamanger = dataManager;
	}

	public DValue getDValue(DKey key) throws NullPointerException {
		// TODO debug mode
		// System.out.println("load db " + key);
		return this.datamanger.get(key);
	}
}
