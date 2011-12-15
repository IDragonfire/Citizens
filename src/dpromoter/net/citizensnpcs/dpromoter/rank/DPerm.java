package net.citizensnpcs.dpromoter.rank;

import java.util.logging.Logger;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DPerm {
	public static boolean hasPerm(Player player, String permission) {
		try {
			return PermissionsEx.getPermissionManager().has(player, permission);
		} catch (Exception e) {
			Logger.getLogger("Minecraft").warning("hasPerm.");
			e.printStackTrace();
		}
		return false;
	}

	public static boolean hasGroup(Player player, String group) {
		try {
			return PermissionsEx.getPermissionManager().getUser(player)
					.inGroup(group);
		} catch (Exception e) {
			Logger.getLogger("Minecraft").warning("hasPerm.");
			e.printStackTrace();
		}
		return false;
	}
}
