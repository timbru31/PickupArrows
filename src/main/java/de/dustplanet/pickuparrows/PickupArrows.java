package de.dustplanet.pickuparrows;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * PickupArrows for CraftBukkit/Spigot.
 * Handles general stuff!
 *
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/pickuparrows/
 *
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrows extends JavaPlugin {
    /**
     * FileConfiguration used for config values.
     */
    private FileConfiguration config;
    // WordGuard stuff
    /**
     * Boolean to determine if WorldGuard is used.
     */
    private boolean usingWorldGuard;
    /**
     * Current status of the blacklist.
     */
    private boolean blacklist;
    /**
     * List of WorldGuard regions used for black/whitelist.
     */
    private List<String> regions = new ArrayList<>();
    /**
     * WorldGuard instance.
     */
    private WorldGuardPlugin wg;

    /**
     * Disables PickupArrows and clears region list.
     */
    @Override
    public void onDisable() {
        // Make our list empty
        getRegions().clear();
    }

    /**
     * Enables PickupArrows and loads config values.
     */
    @Override
    public void onEnable() {
        config = getConfig();
        // Add defaults and copy them
        config.options().header("For help please refer to the bukkit dev page: http://dev.bukkit.org/bukkit-plugins/pickuparrows/");
        config.addDefault("usePermissions", false);
        String[] temp = {"skeleton", "player", "dispenser"};
        for (String s : temp) {
            config.addDefault("pickupFrom." + s + ".range", 10.0);
            config.addDefault("pickupFrom." + s + ".fire", true);
            config.addDefault("pickupFrom." + s + ".normal", true);
            config.addDefault("pickupFrom." + s + ".spectral", true);
            config.addDefault("pickupFrom." + s + ".tipped", true);
        }
        config.addDefault("pickupFrom.unknown.range", 5.0);
        config.addDefault("pickupFrom.unknown.fire", false);
        config.addDefault("pickupFrom.unknown.normal", false);
        config.addDefault("pickupFrom.unknown.spectral", false);
        config.addDefault("pickupFrom.unknown.tipped", false);
        config.addDefault("ignoreCreativeArrows", false);
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

    /**
     * Returns whether WorldGuard is used for regions hook.
     * @return true or false if whether WorldGuard is used
     */
    public boolean isUsingWorldGuard() {
        return usingWorldGuard;
    }

    /**
     * Sets if WorldGuad integration should be used.
     * @param useWorldGuard boolean to set if WorldGuard should be used
     */
    public void setUsingWorldGuard(boolean useWorldGuard) {
        this.usingWorldGuard = useWorldGuard;
    }

    /**
     * Returns the status of the blacklist.
     * @return the current blacklist status
     */
    public boolean isBlacklist() {
        return blacklist;
    }

    /**
     * Enabled or disables the blacklist.
     * @param blacklist boolean value
     */
    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    /**
     * Gets the WorldGuard regions.
     * @return the WorldGuard regions list
     */
    public List<String> getRegions() {
        return regions;
    }

    /**
     * Sets for the WorldGuard regions.
     * @param regions a list of WorldGuard regions
     */
    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    /**
     * Gets the WorldGuard instance.
     * @return the WorldGuard instance or null
     */
    public WorldGuardPlugin getWorldGuard() {
        return wg;
    }

    /**
     *  Sets the WorldGuard instance.
     * @param wg a WorldGuard instance
     */
    public void setWorldGuard(WorldGuardPlugin wg) {
        this.wg = wg;
    }
}
