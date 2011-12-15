package net.citizensnpcs.dpromoter.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.citizensnpcs.dpromoter.DPromoter;
import net.citizensnpcs.idragonfire.datamanager.DMySQLManager;
import net.citizensnpcs.idragonfire.keys.DCachedKey;
import net.citizensnpcs.idragonfire.storage.DStore;
import net.citizensnpcs.idragonfire.values.DIntArrayValue;

import org.bukkit.entity.Player;

/**
 * Saves a state of a player npc interaction
 * 
 * @author IDragonfire
 * 
 */
public class DStatesStorage extends DStore {
	public static final DStatesStorage INSTANCE = new DStatesStorage();
	public static final int ERROR = 0;

	private DStatesStorage() {
		super(DMySQLManager.INSTANCE);
	}

	public int[] getStatOfNpcForPlayer(int jobid, Player player) {
		DIntArrayValue tmpState = ((DIntArrayValue) super
				.getDValue(new DStateKey(jobid, player.getName())));
		if (tmpState != null) {
			return tmpState.getIntValues();
		}
		// new player set defautl state
		int[] firstStates = new int[] { DPromoter.USER_INTRO, 0 };
		DMySQLManager.INSTANCE.insertState("INSERT INTO " + DStateKey.SQL_DB
				+ " (playerid,jobid,state,level) VALUES(?,?,?,?)", firstStates,
				player.getName(), jobid);
		return firstStates;
	}

	public void setStateAndLevel(int jobid, Player player, int[] values) {
		DMySQLManager.INSTANCE.updateState("UPDATE " + DStateKey.SQL_DB
				+ " SET state = ?, level = ? WHERE playerid = ? AND jobid = ?",
				values, player.getName(), jobid);
	}

	/**
	 * Key to get the state of a NPC, depend on player and npc<br>
	 * PRIMARY KEY = (npcid, playerid)
	 * 
	 * @author IDragonfire
	 * 
	 */
	public class DStateKey extends DCachedKey {
		private int npcid;
		private String playerid;
		public static final String SQL_DB = "dp_playerstates";

		public DStateKey(int npcid, String playerid) {
			this.npcid = npcid;
			this.playerid = playerid;
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT state,level FROM " + DStateKey.SQL_DB
					+ " WHERE playerid = '" + this.playerid + "' AND jobid = "
					+ this.npcid + " LIMIT 1";
		}

		@Override
		public String[] getYMLCall() {
			// TODO not implement
			return null;
		}

		public int getNpcid() {
			return this.npcid;
		}

		public String getPlayerid() {
			return this.playerid;
		}

		@Override
		public int hashCode() {
			return this.npcid ^ this.playerid.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			try {
				return this.npcid == ((DStateKey) obj).getNpcid()
						&& this.playerid
								.equals(((DStateKey) obj).getPlayerid());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public DIntArrayValue fetch(ResultSet rs) throws SQLException {
			return new DIntArrayValue(new int[] { rs.getInt(1), rs.getInt(2) });
		}

		@Override
		public String toString() {
			return "[npc:" + this.npcid + ",player:" + this.playerid + "] "
					+ getClass().getSimpleName();
		}
	}

}
