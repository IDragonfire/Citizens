package com.fullwall.Citizens;

import java.util.HashMap;

public class Defaults {
	public static HashMap<String, String> defaultSettings = writeDefaultSettings();
	public static HashMap<String, String> defaultEconomySettings = writeEconomySettings();
	public static HashMap<String, String> settingsRenames = writeSettingsRenames();
	public static HashMap<String, String> economyRenames = writeEconomyRenames();

	private static HashMap<String, String> writeDefaultSettings() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		nodes.put("tick-delay", "1");
		nodes.put("save-tick-delay", "72000");
		nodes.put("look-range", "4");
		nodes.put("item-list-on", "true");
		nodes.put("items", "340,");
		nodes.put("select-item", "*");
		nodes.put("default-enable-following", "true");
		nodes.put("default-talk-when-close", "false");
		nodes.put("use-npc-colours", "true");
		nodes.put("npc-colour", "�f");
		nodes.put("chat-format", "[%name%]: ");
		nodes.put("slashes-to-spaces", "true");
		nodes.put("max-NPCs-per-player", "10");
		nodes.put("healer-take-health-item", "276");
		nodes.put("healer-give-health-item", "35");
		nodes.put("healer-health-regen-increment", "12000");
		nodes.put("wizard-interact-item", "288");
		nodes.put("wizard-max-locations", "10");
		nodes.put(
				"default-text",
				"Hello.;How are you today?;Having a nice day?;Good weather today.;Stop hitting me!;I'm bored.;");
		return nodes;
	}

	private static HashMap<String, String> writeSettingsRenames() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		return nodes;
	}

	private static HashMap<String, String> writeEconomyRenames() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		nodes.put("-iconomy", "-econplugin");
		return nodes;
	}

	private static HashMap<String, String> writeEconomySettings() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		nodes.put("use-economy", "true");
		nodes.put("use-iconomy", "false");
		nodes.put("basic-npc-create-item-currency-id", "37");
		nodes.put("basic-npc-create-item", "10");
		nodes.put("basic-npc-create-econplugin", "100");
		nodes.put("trader-npc-create-item-currency-id", "37");
		nodes.put("trader-npc-create-item", "20");
		nodes.put("trader-npc-create-econplugin", "250");
		nodes.put("healer-npc-create-item-currency-id", "37");
		nodes.put("healer-npc-create-item", "20");
		nodes.put("healer-npc-create-econplugin", "100");
		nodes.put("healer-level-up-item-currency-id", "37");
		nodes.put("healer-level-up-item", "20");
		nodes.put("healer-level-up-econplugin", "100");
		nodes.put("wizard-npc-create-item-currency-id", "37");
		nodes.put("wizard-npc-create-item", "20");
		nodes.put("wizard-npc-create-econplugin", "250");
		nodes.put("wizard-teleport-item-currency-id", "4");
		nodes.put("wizard-teleport-item", "10");
		nodes.put("wizard-teleport-econplugin", "25");
		nodes.put("blacksmith-npc-create-item-currency-id", "37");
		nodes.put("blacksmith-npc-create-item", "20");
		nodes.put("blacksmith-npc-create-econplugin", "100");
		nodes.put("blacksmith-tool-repair-item-currency-id", "266");
		nodes.put("blacksmith-tool-repair-item", "5");
		nodes.put("blacksmith-tool-repair-econplugin", "50");
		nodes.put("blacksmith-armor-repair-item-currency-id", "266");
		nodes.put("blacksmith-armor-repair-item", "5");
		nodes.put("blacksmith-armor-repair-econplugin", "50");
		return nodes;
	}
}