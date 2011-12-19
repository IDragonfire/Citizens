package net.citizensnpcs.dpromoter;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.dpromoter.event.DPromoterListener;
import net.citizensnpcs.dpromoter.event.DPromoterSkinSetter;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Type;

public class DPromoterType extends CitizensNPCType {
	public static final String TYPE = "dragon_promoter";

	@Override
	public CommandHandler getCommands() {
		return DPromoterCommands.INSTANCE;
	}

	@Override
	public CitizensNPC getInstance() {
		return new DPromoter();
	}

	@Override
	public String getName() {
		return DPromoterType.TYPE;
	}

	@Override
	public Properties getProperties() {
		return DPromoterProperty.INSTANCE;
	}

	@Override
	public void registerEvents() {
		super.registerEvents();
		NPCTypeManager
				.registerEvent(Type.CUSTOM_EVENT, new DPromoterListener());
		startSkinSetter();
	}

	public static void startSkinSetter() {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Citizens.plugin,
				DPromoterSkinSetter.INSTANCE, 5l);
	}

}
