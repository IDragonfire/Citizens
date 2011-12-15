package net.citizensnpcs.dpromoter.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.citizensnpcs.idragonfire.datamanager.DMySQLManager;
import net.citizensnpcs.idragonfire.keys.DCachedKey;
import net.citizensnpcs.idragonfire.storage.DHashStore;
import net.citizensnpcs.idragonfire.values.DIntValue;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.entity.Player;

/**
 * Saves a state of a player npc interaction
 * 
 * @author IDragonfire
 * 
 */
public class DStatesStorage extends DHashStore {
	public static final DStatesStorage INSTANCE = new DStatesStorage();
	public static final int ERROR = 0;

	private DStatesStorage() {
		super(DMySQLManager.INSTANCE);
	}

	public int getStatOfNpcForPlayer(HumanNPC npc, Player player) {
		try {
			return ((DIntValue) super.getDValue(new DStateKey(npc.getUID(),
					player.getUniqueId().toString()))).getInt();
		} catch (Exception e) {
			// give error code
		}
		return DStatesStorage.ERROR;
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
		public static final String SQL_DB = "dplayerstates";

		public DStateKey(int npcid, String playerid) {
			this.npcid = npcid;
			this.playerid = playerid;
		}

		@Override
		public String getSQL_SELECT() {
			return "SELECT state FROM " + DStateKey.SQL_DB
					+ " WHERE playerid = '" + this.playerid + "' AND npcid = "
					+ this.npcid + " LIMIT 1";
		}

		@Override
		public String[] getYMLCall() {
			// TODO not implementd
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
		public DIntValue fetch(ResultSet rs) throws SQLException {
			return new DIntValue(rs.getInt(1));
		}

		@Override
		public String toString() {
			return "[npc:" + this.npcid + ",player:" + this.playerid + "] "
					+ getClass().getSimpleName();
		}
	}

}
