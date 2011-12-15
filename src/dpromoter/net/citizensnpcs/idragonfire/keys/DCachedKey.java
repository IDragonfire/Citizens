package net.citizensnpcs.idragonfire.keys;


/**
 * Sepcial Key, to delete old entries in the HashMap
 * 
 * @author Gerhardt
 * 
 */
public abstract class DCachedKey extends DKey {
	private long lastuse;

	public DCachedKey() {
		this.lastuse = System.currentTimeMillis();
	}

	public void used() {
		this.lastuse = System.currentTimeMillis();
	}

	// TODO getter with time range
}
