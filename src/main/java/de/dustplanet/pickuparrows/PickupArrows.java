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
    private FileConfiguration config;
    // WordGuard stuff
    private boolean usingWorldGuard;
    private boolean blacklist;
    private List<String> regions = new ArrayList<>();
    private WorldGuardPlugin wg;

    public void onDisable() {
        // Make our list empty
        getRegions().clear();
    }

    public void onEnable() {
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
        setUsingWorldGuard(config.getBoolean("useWorldGuard"));
        if (isUsingWorldGuard()) {
            setBlacklist(config.getBoolean("useListAsBlacklist"));
            setRegions(config.getStringList("regions"));
            // WorldGuard plugin
            Plugin worldGuard = getServer().getPluginManager().getPlugin("WorldGuard");
            if (worldGuard != null && worldGuard instanceof WorldGuardPlugin) {
                setWorldGuard((WorldGuardPlugin) worldGuard);
            }
        }

        // Event
        getServer().getPluginManager().registerEvents(new PickupArrowsListener(this), this);
    }

    public boolean isUsingWorldGuard() {
        return usingWorldGuard;
    }

    public void setUsingWorldGuard(boolean useWorldGuard) {
        this.usingWorldGuard = useWorldGuard;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public WorldGuardPlugin getWorldGuard() {
        return wg;
    }

    public void setWorldGuard(WorldGuardPlugin wg) {
        this.wg = wg;
    }
}
