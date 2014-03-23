package de.dustplanet.pickuparrows;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * PickupArrows for CraftBukkit/Bukkit
 * Handles general stuff!
 * 
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/pickuparrows/
 * 
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrows extends JavaPlugin {
    public FileConfiguration config;
    // WordGuard stuff
    public boolean useWorldGuard;
    public boolean blacklist;
    public List<String> regions = new ArrayList<String>();
    public WorldGuardPlugin wg;

    public void onDisable() {
	// Make our list empty
	regions.clear();
    }

    public void onEnable() {
	// Event
	getServer().getPluginManager().registerEvents(new PickupArrowsListener(this), this);
	config = getConfig();
	// Add defaults and copy them
	config.options().header("For help please either refer to the bukkit dev page:\nhttp://dev.bukkit.org/bukkit-plugins/pickuparrows/");
	config.addDefault("usePermissions", false);
	String[] temp = {"skeleton", "player", "dispenser"};
	for (String s : temp) {
	    config.addDefault("pickupFrom." + s + "range", 10.0);
	    config.addDefault("pickupFrom." + s + "fire", true);
	    config.addDefault("pickupFrom." + s + "normal", true);
	}
	config.addDefault("pickupFrom.unknown.range", 5.0);
	config.addDefault("pickupFrom.unknown.fire", false);
	config.addDefault("pickupFrom.unknown.normal", false);
	config.addDefault("useWorldGuard", false);
	config.addDefault("useListAsBlacklist", false);
	config.addDefault("regions", new ArrayList<String>());
	config.options().copyDefaults(true);
	saveConfig();
	// WorldGuard regions
	useWorldGuard = config.getBoolean("useWorldGuard");
	if (useWorldGuard) {
	    blacklist = config.getBoolean("useListAsBlacklist");
	    regions = config.getStringList("regions");
	    // WorldGuard plugin
	    Plugin worldGuard = getServer().getPluginManager().getPlugin("WorldGuard");
	    if (worldGuard != null && worldGuard instanceof WorldGuardPlugin) {
		wg = (WorldGuardPlugin) worldGuard;
	    }
	}
    }
}