package com.citizens.resources.sk89q;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import com.citizens.Permission;

public class CitizensCommandsManager<T extends Player> extends
		CommandsManager<T> {

	@Override
	public boolean hasPermission(T player, String perm) {
		return Permission.generic(player, "citizens." + perm);
	}

	public String[] getAllCommandModifiers(String command) {
		Set<String> cmds = new HashSet<String>();
		for (Map<CommandIdentifier, Method> enclosing : super.commands.values()) {
			for (CommandIdentifier identifier : enclosing.keySet()) {
				if (identifier.getCommand().equals(command)) {
					cmds.add(identifier.getModifier());
				}
			}
		}
		return cmds.toArray(new String[cmds.size()]);
	}
}