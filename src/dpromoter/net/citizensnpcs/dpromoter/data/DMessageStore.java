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
	public static final String ERROR = "Sag bitte einen Admin, dass der Text fehlt -> ";
	public static final String SEPERATOR = "$";

	private DMessageStore() {
		super(DMySQLManager.INSTANCE);
		this.random = new Random();
		this.randCountCache = new DHashStore(DMySQLManager.INSTANCE);
	}

	/**
	 * Gets the value for a specified key
	 * 
	 * @param normalReplacement
	 *            replace all from [i][0] with [i][1] (for dynamic varibles,
	 *            like price, time, ..)
	 * @param configReplacement
	 *            replace [i] with elements in the config (for static variables,
	 *            like serverName, currency, ..)
	 * 
	 * @return the d value
	 */
	// TODO use StringBuffer?
	public String getMessageForEventAnJobAndLevel(int job, int event,
			int level, String[][] normalReplacement) {
		String buf = getMessageForEventAnJobAndLevel(job, event, level);
		for (int i = 0; i < normalReplacement.length; i++) {
			buf = buf.replace(SEPERATOR + normalReplacement[i][0] + SEPERATOR,
					normalReplacement[i][1]);
			System.out.println(SEPERATOR + normalReplacement[i][0] + " - "
					+ normalReplacement[i][1]);
		}
		return buf;
	}

	public String getMessageForEventAnJobAndLevel(int job, int event, int level) {

		DStringValue tmpString = getStringValue(new DMessageKey(event, job,
				level));
		if (tmpString != null) {
			return tmpString.getString();
		}
		String infos = " [job:" + job + ",event:" + event + ",level:" + level
				+ "]";
		Messaging.log("Missing message:" + infos);
		return DMessageStore.ERROR + infos;
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
		private int jobid;
		private int randid;
		private int levelid;
		public static final String SQL_DB = "dp_messagestore";

		public DMessageKey(int event, int jobid, int levelid) {
			this.event = event;
			this.jobid = jobid;
			this.randid = 0;
			this.levelid = levelid;
		}

		@Override
		public DStringValue fetch(ResultSet rs) throws SQLException {
			return DKey.getDStringValueFromResultSet(rs);
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT msg FROM "
					+ DMessageKey.SQL_DB
					+ " WHERE event = "
					+ this.event
					+ " AND jobid = "
					+ this.jobid
					+ " AND randid = "
					+ this.randid
					+ ((this.levelid > 0) ? " AND leveid = " + this.levelid
							: "") + " LIMIT 1";
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

		public void setEvent(int event) {
			this.event = event;
		}

		public int getJobid() {
			return this.jobid;
		}

		public void setJobid(int jobid) {
			this.jobid = jobid;
		}

		public int getLevelid() {
			return this.levelid;
		}

		public void setLevelid(int levelid) {
			this.levelid = levelid;
		}

		@Override
		public int hashCode() {
			int result = 31 * 1 + this.event;
			result = 31 * result + this.jobid;
			result = 31 * result + this.levelid;
			result = 31 * result + this.randid;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			try {
				return this.event == ((DMessageKey) obj).getEvent()
						&& this.jobid == ((DMessageKey) obj).getJobid()
						&& this.randid == ((DMessageKey) obj).getRandid()
						&& this.levelid == ((DMessageKey) obj).getLevelid();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public String toString() {
			return "[event:" + this.event + ",npc:" + this.jobid + ",rand:"
					+ this.randid + "] " + getClass().getSimpleName();
		}
	}

	class DMaxRandCountKey extends DCachedKey {
		public static final String SQL_DB = "dp_messagestore";
		private DMessageKey msg;

		public DMaxRandCountKey(DMessageKey msgkey) {
			try {
				this.msg = msgkey;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public DIntValue fetch(ResultSet rs) throws SQLException {
			return DKey.getDIntValueFromResultSet(rs);
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT COUNT(randid) FROM " + DMaxRandCountKey.SQL_DB
					+ " WHERE event = " + this.msg.getEvent() + " AND jobid = "
					+ this.msg.getJobid() + " AND levelid = "
					+ this.msg.getLevelid() + " LIMIT 1";
		}

		public DMessageKey getMsg() {
			return this.msg;
		}

		// TODO make a better hashfunction
		@Override
		public int hashCode() {
			int result = 31 * 1 + this.msg.getEvent();
			result = 31 * result + this.msg.getJobid();
			result = 31 * result + this.msg.getLevelid();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			try {
				return this.msg.getEvent() == ((DMaxRandCountKey) obj).getMsg()
						.getEvent()
						&& this.msg.getJobid() == ((DMaxRandCountKey) obj)
								.getMsg().getJobid()
						&& this.msg.getLevelid() == ((DMaxRandCountKey) obj)
								.getMsg().getLevelid();
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
			return "[event:" + this.msg.getEvent() + ",job:"
					+ this.msg.getJobid() + ",rand:" + this.msg.getRandid()
					+ ",level:" + this.msg.getLevelid() + "] "
					+ getClass().getSimpleName();
		}
	}
}
