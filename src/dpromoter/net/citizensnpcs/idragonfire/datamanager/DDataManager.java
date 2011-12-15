package net.citizensnpcs.idragonfire.datamanager;

import net.citizensnpcs.idragonfire.keys.DKey;
import net.citizensnpcs.idragonfire.values.DValue;

/**
 * Interface for loading and store data
 * 
 * @author IDragonfire
 * 
 */
public abstract class DDataManager {
	public abstract DValue get(DKey key);

	public abstract boolean set(DKey key, DValue value);

}
