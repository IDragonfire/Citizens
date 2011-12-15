package net.citizensnpcs.dpromoter.old;

import net.citizensnpcs.dpromoter.rank.DNPCPType;
import net.citizensnpcs.dpromoter.rank.DPerm;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DRank {
	public static DRank INSTANCE = new DRank();
	public static Permission permission = null;
	public static final int MEMBER = 31;
	public static final int MEMBER_INTO = 30;
	public static final int TRIAL_NEED = 21;
	public static final int TRIAL_INTRO = 20;
	public static final int USER_NO = 11;
	public static final int USER_INTRO = 10;
	public static final int NONE = 0;
	public static final String TRIALGROUP = ".trial";
	private DNPCPType map;

	private DRank() {
		this.map = new DNPCPType();

		// setup permission
		RegisteredServiceProvider<Permission> permissionProvider = Bukkit
				.getServer().getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
	}

	public void action(Player player, HumanNPC npc) {
		int state = 0;
		try {
			// state = DPlayerStates.INSTANCE.getState(player, npc);
		} catch (Exception e) {
			// TODO Player not found, set 0
		}
		System.out.println(state);
		// player.sendMessage(DMySQLMessageStore.INSTANCE.getMessage(state));
	}

	public int playerGroupRank(Player player, String group) {
		if (DPerm.hasGroup(player, group)) {
			return DRank.MEMBER;
		}
		if (DPerm.hasGroup(player, group + DRank.TRIALGROUP)) {
			return DRank.TRIAL_INTRO;
		}
		if (allowToTalk(player, group)) {
			return DRank.USER_INTRO;
		}
		return DRank.NONE;
	}

	public boolean allowToTalk(Player player, String group) {
		int level_start = group.lastIndexOf(".") + 1;
		try {
			int g_level = Integer.parseInt(group.substring(level_start));
			if (g_level == 1) {
				return true;
			}
			return DPerm.hasGroup(player, group.substring(0, level_start)
					+ (g_level - 1));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
