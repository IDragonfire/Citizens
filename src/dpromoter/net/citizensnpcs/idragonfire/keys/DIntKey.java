package net.citizensnpcs.idragonfire.keys;

public abstract class DIntKey extends DCachedKey {

	protected int id;

	public DIntKey(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return this.id == ((DIntKey) obj).getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[npc:" + this.id + "] " + getClass();
	}
}
