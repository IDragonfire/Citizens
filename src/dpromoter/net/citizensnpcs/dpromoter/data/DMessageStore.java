package net.citizensnpcs.dpromoter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import net.citizensnpcs.idragonfire.datamanager.DMySQLManager;
import net.citizensnpcs.idragonfire.keys.DCachedKey;
import net.citizensnpcs.idragonfire.keys.DKey;
import net.citizensnpcs.idragonfire.storage.DHashStore;
import net.citizensnpcs.idragonfire.values.DIntValue;
import net.citizensnpcs.idragonfire.values.DStringValue;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

/**
 * Load the messages for the NPC, multiple msg support.
 * 
 * @author IDragonfire
 * 
 */
// TODO cleanup also randCountCache
public class DMessageStore extends DHashStore {
	public static final DMessageStore INSTANCE = new DMessageStore();
	private Random random;
	private DHashStore randCountCache;
	public static final String ERROR = "Please contact an admin that the text is missing for";
	private DMessageKey tmp;

	private DMessageStore() {
		super(DMySQLManager.INSTANCE);
		this.random = new Random();
		this.randCountCache = new DHashStore(DMySQLManager.INSTANCE);
		this.tmp = new DMessageKey(-1, -1);
	}

	public String getMessageForEventAndNpc(HumanNPC npc, int event) {
		try {
			this.tmp.setNpcid(npc.getUID());
			this.tmp.setEvent(event);
			return getStringValue(this.tmp).getString();
			// return getStringValue(new DMessageKey(event, npc.getUID()))
			// .getString();
		} catch (NullPointerException e) {
			Messaging.log("Missing message:" + " [event:" + event + ",npc:"
					+ npc.getUID() + "]");
		}
		return DMessageStore.ERROR + " [event:" + event + ",npc:"
				+ npc.getUID() + "]";
	}

	public DStringValue getStringValue(DMessageKey key)
			throws NullPointerException {
		// get number of messages per key
		DMaxRandCountKey randkey = new DMaxRandCountKey(key);
		DIntValue rand = (DIntValue) this.randCountCache.getDValue(randkey);
		if (rand.getInt() <= 0) {
			rand.setState(1);
		}
		// set new random message
		(key).setRandid(this.random.nextInt(rand.getInt()));
		return (DStringValue) super.getDValue(key);
	}

	class DMessageKey extends DCachedKey {
		private int event;
		private int npcid;
		private int randid;
		public static final String SQL_DB = "dmessagestore";

		public DMessageKey(int event, int npcid) {
			this.event = event;
			this.npcid = npcid;
			this.randid = 0;
		}

		@Override
		public DStringValue fetch(ResultSet rs) throws SQLException {
			return DKey.getDStringValueFromResultSet(rs);
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT msg FROM " + DMessageKey.SQL_DB + " WHERE event = "
					+ this.event + " AND npcid = " + this.npcid
					+ " AND randid = " + this.randid + " LIMIT 1";
		}

		@Override
		public String[] getYMLCall() {
			// TODO Auto-generated method stub
			return null;
		}

		public int getEvent() {
			return this.event;
		}

		public int getRandid() {
			return this.randid;
		}

		public void setRandid(int randid) {
			this.randid = randid;
		}

		public int getNpcid() {
			return this.npcid;
		}

		public void setEvent(int event) {
			this.event = event;
		}

		public void setNpcid(int npcid) {
			this.npcid = npcid;
		}

		@Override
		public int hashCode() {
			int result = 1;
			result = 31 * result + this.event;
			result = 31 * result + this.npcid;
			result = 31 * result + this.randid;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			try {
				return this.event == ((DMessageKey) obj).getEvent()
						&& this.npcid == ((DMessageKey) obj).getNpcid()
						&& this.randid == ((DMessageKey) obj).getRandid();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public String toString() {
			return "[event:" + this.event + ",npc:" + this.npcid + ",rand:"
					+ this.randid + "] " + getClass().getSimpleName();
		}
	}

	class DMaxRandCountKey extends DCachedKey {
		public static final String SQL_DB = "dmessagestore";
		private DMessageKey msg;

		public DMaxRandCountKey(DMessageKey msgkey) {
			try {
				this.msg = msgkey;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public DIntValue fetch(ResultSet rs) {
			try {
				return DKey.getDIntValueFromResultSet(rs);
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("EXP found #########################");
			}
			System.out.println("error found #########################");
			return new DIntValue(1);
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT COUNT(randid) FROM " + DMaxRandCountKey.SQL_DB
					+ " WHERE event = " + this.msg.getEvent() + " AND npcid = "
					+ this.msg.getNpcid() + " LIMIT 1";
		}

		public DMessageKey getMsg() {
			return this.msg;
		}

		@Override
		public int hashCode() {
			return 31 * (31 + this.msg.getEvent()) + this.msg.getNpcid();
		}

		@Override
		public boolean equals(Object obj) {
			try {
				return this.msg.getEvent() == ((DMaxRandCountKey) obj).getMsg()
						.getEvent()
						&& this.msg.getNpcid() == ((DMaxRandCountKey) obj)
								.getMsg().getNpcid();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public String[] getYMLCall() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String toString() {
			return "[event:" + this.msg.getEvent() + ",npc:"
					+ this.msg.getNpcid() + ",rand:" + this.msg.getRandid()
					+ "] " + getClass().getSimpleName();
		}
	}
}
