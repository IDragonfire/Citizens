package net.citizensnpcs.idragonfire.storage;

import java.util.HashMap;

import net.citizensnpcs.idragonfire.datamanager.DDataManager;
import net.citizensnpcs.idragonfire.keys.DCachedKey;
import net.citizensnpcs.idragonfire.keys.DKey;
import net.citizensnpcs.idragonfire.values.DValue;

/**
 * Store Values into a HashMap,<br>
 * planned are a automatic schedule that remove unused objects.
 * 
 * @author IDragonfire
 * 
 */
public class DHashStore extends DStore {
	protected HashMap<DCachedKey, DValue> cache;

	public DHashStore(DDataManager dataManager) {
		super(dataManager);
		this.cache = new HashMap<DCachedKey, DValue>();
	}

	@Override
	public DValue getDValue(DKey key) {
		DValue value = this.cache.get(key);
		// TODO debug mode
		// System.out.println("cache size: " + this.cache.size() + "("
		// + key.getClass().getSimpleName() + ")");
		if (value != null) {
			((DCachedKey) key).used();
			return value;
		}
		value = super.getDValue(key);
		if (value == null) {
			return null;
		}
		this.cache.put((DCachedKey) key, value);
		return value;
	}

	public void resetCache() {
		// TODO Auto-generated method stub
	}

	public void startCleanup() {
		// TODO Auto-generated method stub
	}

	public void stopCleanup() {
		// TODO Auto-generated method stub
	}
}
