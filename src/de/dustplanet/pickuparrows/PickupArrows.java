package de.dustplanet.pickuparrows;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * PickupArrows for CraftBukkit/Bukkit
 * Handles general stuff!
 * Refer to the forum thread:
 * http://bit.ly/pathread
 * Refer to the dev.bukkit.org page: http://bit.ly/papagedev
 * 
 * @author  xGhOsTkiLLeRx
 * @thanks  to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrows extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	public FileConfiguration config;

	public void onDisable() {}
	
	public void onEnable() {
		// Event
		getServer().getPluginManager().registerEvents(new PickupArrowsListener(this), this);
		config = getConfig();
		// Add defaults and copy ;)
		config.options().header("For help please either refer to the\nforum thread: http://bit.ly/pathread\nor the bukkit dev page: http://bit.ly/papagedev");
		config.addDefault("skeletonsOnly", true);
		config.addDefault("usePermissions", false);
		config.addDefault("range", 10.0);
		config.addDefault("debug", false);
		config.options().copyDefaults(true);
		saveConfig();
	}

	// Debug
	public void log(String message) {
		if (config.getBoolean("debug")) {
			log.info(message);
		}
	}
}
