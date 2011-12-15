package net.citizensnpcs.dpromoter.old;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DRankProperties {
	public static final DRankProperties INSTANCE = new DRankProperties();
	private FileConfiguration customConfig = null;
	private File customConfigFile = new File("plugins/Citizens/", "drank.yml");

	public FileConfiguration getCustomConfig() {
		if (this.customConfig == null) {
			reloadCustomConfig();
		}
		return this.customConfig;
	}

	@SuppressWarnings("null")
	public void reloadCustomConfig() {
		this.customConfig = YamlConfiguration
				.loadConfiguration(this.customConfigFile);

		// TODO Look for defaults in the jar
		InputStream defConfigStream = null; // getResource(this.customConfigFile);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);

			this.customConfig.setDefaults(defConfig);
		}
	}

	public void saveYourConfig() {
		try {
			this.customConfig.save(this.customConfigFile);
		} catch (IOException ex) {
			Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE,
					"Could not save config to " + this.customConfigFile, ex);
			ex.printStackTrace();
		}
	}

}
