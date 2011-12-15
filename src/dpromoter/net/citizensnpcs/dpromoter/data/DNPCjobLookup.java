package net.citizensnpcs.dpromoter.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.citizensnpcs.idragonfire.datamanager.DMySQLManager;
import net.citizensnpcs.idragonfire.keys.DIntKey;
import net.citizensnpcs.idragonfire.keys.DKey;
import net.citizensnpcs.idragonfire.storage.DHashStore;
import net.citizensnpcs.idragonfire.values.DIntValue;
import net.citizensnpcs.idragonfire.values.DValue;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;

public class DNPCjobLookup extends DHashStore {
	public static final DNPCjobLookup INSTANCE = new DNPCjobLookup();

	// ### JOBlookup

	private DNPCjobLookup() {
		super(DMySQLManager.INSTANCE);
	}

	public int getJobOfNpc(HumanNPC npc) {
		DIntValue jobid = ((DIntValue) super.getDValue(new DNPCjobKey(npc
				.getUID())));
		if (jobid != null) {
			return jobid.getInt();
		}
		return -1;
	}

	public class DNPCjobKey extends DIntKey {
		public static final String SQL_DB = "dp_npc";

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
	public DJobInfoValue getJobInfoFromNpc(int jobid) {
		try {
			return (DJobInfoValue) super.getDValue(new DJobInfoKey(jobid));
		} catch (Exception e) {
			Messaging.log("No JobInfoData for job  " + jobid);
		}
		return null;
	}

	public class DJobInfoKey extends DIntKey {
		public static final String SQL_DB = "dp_jobs";

		public DJobInfoKey(int id) {
			super(id);
		}

		@Override
		public DJobInfoValue fetch(ResultSet rs) throws SQLException {
			return new DJobInfoValue(rs.getString(1), rs.getInt(3),
					rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(2));
		}

		// TODO implement jobLevel
		@Override
		public String getSQL_SELECT() {
			return "SELECT name, pex, rank_needed, skillpoints_needed, numberbasicitems_needed, money_needed FROM "
					+ SQL_DB
					+ " WHERE job_parent = "
					+ super.id
					+ " AND job_level = 1 " + " LIMIT 1";
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
		private String pex;

		public DJobInfoValue(String name, int neededRank,
				int neededSkillpoints, int neededBasicItems, int neededMoney,
				String pex) {
			super();
			this.name = name;
			this.neededRank = neededRank;
			this.neededSkillpoints = neededSkillpoints;
			this.neededBasicItems = neededBasicItems;
			this.neededMoney = neededMoney;
			this.pex = pex;
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

		public String getPex() {
			return this.pex;
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
					+ ", neededSkillpoints=" + this.neededSkillpoints + ",pex:"
					+ this.pex + "] " + getClass().getSimpleName();
		}
	}
}
