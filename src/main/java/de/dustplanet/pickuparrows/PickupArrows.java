package de.dustplanet.pickuparrows;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import lombok.Getter;
import lombok.Setter;

/**
 * PickupArrows for CraftBukkit/Spigot. Handles general stuff! Refer to the dev.bukkit.org page:
 * https://dev.bukkit.org/projects/pickuparrows
 *
 * @author xGhOsTkiLLeRx thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrows extends JavaPlugin {
    @Getter
    @Setter
    private boolean usingWorldGuard;
    @Getter
    @Setter
    private boolean blacklist;
    /**
     * List of WorldGuard regions used for black/whitelist.
     */
    @Getter
    @Setter
    private List<String> regions = new ArrayList<>();
    @Getter
    @Setter
    private WorldGuardPlugin worldGuard;

    @Getter
    private List<UUID> disabledPlayers = new ArrayList<>();

    @Override
    public void onDisable() {
        getRegions().clear();
        getDisabledPlayers().clear();
    }

    @SuppressWarnings("unused")
    @Override
    public void onEnable() {
        setConfigDefaults();
        setUsingWorldGuard(getConfig().getBoolean("useWorldGuard"));
        if (isUsingWorldGuard()) {
            setBlacklist(getConfig().getBoolean("useListAsBlacklist"));
            setRegions(getConfig().getStringList("regions"));
            Plugin worldGuardPlugin = getServer().getPluginManager().getPlugin("WorldGuard");
            if (worldGuardPlugin != null && worldGuardPlugin instanceof WorldGuardPlugin) {
                setWorldGuard((WorldGuardPlugin) worldGuardPlugin);
            }
        }

        getServer().getPluginManager().registerEvents(new PickupArrowsListener(this), this);

        getServer().getPluginCommand("pickuparrows").setExecutor(new PickupArrowsCommandExecutor(this));

        new Metrics(this);
    }

    private void setConfigDefaults() {
        FileConfiguration config = getConfig();
        config.options().header("For help please refer to the bukkit dev page: https://dev.bukkit.org/projects/pickuparrows");
        config.addDefault("usePermissions", false);
        String[] temp = { "skeleton", "player", "dispenser" };
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
    }
}
