package net.citizensnpcs.dpromoter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import net.citizensnpcs.idragonfire.datamanager.DMySQLManager;
import net.citizensnpcs.idragonfire.keys.DCachedKey;
import net.citizensnpcs.idragonfire.keys.DIntKey;
import net.citizensnpcs.idragonfire.keys.DKey;
import net.citizensnpcs.idragonfire.storage.DHashStore;
import net.citizensnpcs.idragonfire.values.DIntValue;
import net.citizensnpcs.idragonfire.values.DValue;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

public class DNPCjobLookup extends DHashStore {
	public static final DNPCjobLookup INSTANCE = new DNPCjobLookup();

	// TODO remove
	public void test() {
		Set<DCachedKey> keys = super.cache.keySet();
		int i = 0;
		for (Iterator<?> iterator = keys.iterator(); iterator.hasNext();) {
			DCachedKey dCachedKey = (DCachedKey) iterator.next();
			System.out.println(i + ":" + dCachedKey + ":"
					+ this.cache.get(dCachedKey));
			i++;
		}
	}

	// ### JOBlookup

	private DNPCjobLookup() {
		super(DMySQLManager.INSTANCE);
	}

	public int getJobOfNpc(HumanNPC npc) throws NullPointerException {
		return ((DIntValue) super.getDValue(new DNPCjobKey(npc.getUID())))
				.getInt();
	}

	public class DNPCjobKey extends DIntKey {
		public static final String SQL_DB = "dnpc";

		public DNPCjobKey(int id) {
			super(id);
		}

		@Override
		public DIntValue fetch(ResultSet rs) throws SQLException {
			return DKey.getDIntValueFromResultSet(rs);
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT jobid FROM " + DNPCjobKey.SQL_DB + " WHERE npcid = "
					+ this.id + " LIMIT 1";
		}

		@Override
		public String[] getYMLCall() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	// ### JOBinfoLookup
	private DJobInfoKey tmpinfo = new DJobInfoKey(-1);

	public DJobInfoValue getJobInfoFromNpc(HumanNPC npc) {
		try {
			this.tmpinfo.setId(npc.getUID());
			return (DJobInfoValue) super.getDValue(this.tmpinfo);
		} catch (Exception e) {
			Messaging.log("No JobInfoData for npc " + npc.getUID());
		}
		return null;
	}

	public class DJobInfoKey extends DIntKey {
		public static final String SQL_DB = "djobs";
		public static final String SQL_DB2 = "dnpc";

		public DJobInfoKey(int id) {
			super(id);
		}

		@Override
		public DJobInfoValue fetch(ResultSet rs) throws SQLException {
			return new DJobInfoValue(rs.getString(1), rs.getInt(2), rs
					.getInt(3), rs.getInt(4), rs.getInt(5));
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT name, rank_needed, skillpoints_needed, numberbasicitems_needed, money_needed FROM "
					+ SQL_DB2
					+ " AS n LEFT JOIN "
					+ SQL_DB
					+ " AS j ON n.jobid = j.jobid WHERE npcid = "
					+ this.id
					+ " LIMIT 1";
		}

		@Override
		public String[] getYMLCall() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String toString() {
			return "[npc:" + this.id + "] " + getClass().getSimpleName();
		}
	}

	public class DJobInfoValue extends DValue {
		private String name;
		private int neededRank;
		private int neededSkillpoints;
		private int neededBasicItems;
		private int neededMoney;

		public DJobInfoValue(String name, int neededRank,
				int neededSkillpoints, int neededBasicItems, int neededMoney) {
			super();
			this.name = name;
			this.neededRank = neededRank;
			this.neededSkillpoints = neededSkillpoints;
			this.neededBasicItems = neededBasicItems;
			this.neededMoney = neededMoney;
		}

		public String getName() {
			return this.name;
		}

		public int getNeededRank() {
			return this.neededRank;
		}

		public int getNeededSkillpoints() {
			return this.neededSkillpoints;
		}

		public int getNeededBasicItems() {
			return this.neededBasicItems;
		}

		public int getNeededMoney() {
			return this.neededMoney;
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
			return "[name=" + this.name + ", neededBasicItems="
					+ this.neededBasicItems + ", neededMoney="
					+ this.neededMoney + ", neededRank=" + this.neededRank
					+ ", neededSkillpoints=" + this.neededSkillpoints + "] "
					+ getClass().getSimpleName();
		}
	}
}
