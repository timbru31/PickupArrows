package de.dustplanet.pickuparrows;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class PickupArrows extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	public FileConfiguration config;

	public void onEnable() {
		// Event
		getServer().getPluginManager().registerEvents(new PickupArrowsListener(this), this);
		config = getConfig();
		// Add defaults and copy ;)
		config.addDefault("skeletonsOnly", true);
		config.addDefault("usePermissions", false);
		config.addDefault("range", 10.0);
		config.addDefault("debug", false);
		config.options().copyDefaults(true);
		saveConfig();
	}

	public void onDisable() {}

	// Debug
	public void log(String message) {
		if (!config.getBoolean("debug")) {
			log.info(message);
		}
	}
}
