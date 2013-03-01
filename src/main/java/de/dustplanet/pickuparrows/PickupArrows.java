package de.dustplanet.pickuparrows;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * PickupArrows for CraftBukkit/Bukkit
 * Handles general stuff!
 * Refer to the forum thread:
 * http://bit.ly/pathread
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/papagedev
 * 
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrows extends JavaPlugin {
    public FileConfiguration config;

    public void onEnable() {
	// Event
	getServer().getPluginManager().registerEvents(new PickupArrowsListener(this), this);
	config = getConfig();
	// Add defaults and copy them
	config.options().header("For help please either refer to the\nforum thread: http://bit.ly/pathread\nor the bukkit dev page: http://bit.ly/papagedev");
	config.addDefault("pickupFrom.skeleton", true);
	config.addDefault("pickupFrom.player", true);
	config.addDefault("pickupFrom.other", false);
	config.addDefault("pickupFrom.fire", false);
	config.addDefault("usePermissions", false);
	config.addDefault("range", 10.0);
	config.options().copyDefaults(true);
	saveConfig();
    }
}