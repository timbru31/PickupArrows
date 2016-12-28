package de.dustplanet.pickuparrows;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import lombok.Getter;
import lombok.Setter;

/**
 * PickupArrows for CraftBukkit/Spigot.
 * Handles general stuff!
 *
 * Refer to the dev.bukkit.org page:
 * https://dev.bukkit.org/projects/pickuparrows
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
    @Getter
    @Setter
    private boolean usingWorldGuard;
    /**
     * Current status of the blacklist.
     */
    @Getter
    @Setter
    private boolean blacklist;
    /**
     * List of WorldGuard regions used for black/whitelist.
     */
    @Getter
    @Setter
    private List<String> regions = new ArrayList<>();
    /**
     * WorldGuard instance.
     */
    @Getter
    @Setter
    private WorldGuardPlugin worldGuard;

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
        config.options().header("For help please refer to the bukkit dev page: https://dev.bukkit.org/projects/pickuparrows");
        config.addDefault("usePermissions", false);
        String[] temp = {"skeleton", "player", "dispenser"};
        for (String s : temp) {
            config.addDefault("pickupFrom." + s + ".fire", true);
            config.addDefault("pickupFrom." + s + ".normal", true);
            config.addDefault("pickupFrom." + s + ".spectral", true);
            config.addDefault("pickupFrom." + s + ".tipped", true);
        }
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
}
