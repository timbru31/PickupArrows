package de.dustplanet.pickuparrows;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

/**
 * Handles plugin enabling and disabling and configuration things.
 *
 * @author timbru31
 * @author mushroomhostage
 */

@SuppressWarnings({ "checkstyle:MultipleStringLiterals", "PMD.AtLeastOneConstructor", "checkstyle:MissingCtor" })
@SuppressFBWarnings({ "OPM_OVERLY_PERMISSIVE_METHOD", "CD_CIRCULAR_DEPENDENCY", "FCCD_FIND_CLASS_CIRCULAR_DEPENDENCY" })
public class PickupArrows extends JavaPlugin {
    private static final int BSTATS_PLUGIN_ID = 284;
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
    private final List<UUID> disabledPlayers = new ArrayList<>();

    @Override
    public void onDisable() {
        getRegions().clear();
        getDisabledPlayers().clear();
    }

    @Override
    @SuppressWarnings({ "unused", "PMD.UnnecessaryAnnotationValueElement" })
    @SuppressFBWarnings({ "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "SEC_SIDE_EFFECT_CONSTRUCTOR" })
    public void onEnable() {
        setConfigDefaults();
        setUsingWorldGuard(getConfig().getBoolean("useWorldGuard"));
        if (isUsingWorldGuard()) {
            setBlacklist(getConfig().getBoolean("useListAsBlacklist"));
            setRegions(getConfig().getStringList("regions"));
            final Plugin worldGuardPlugin = getServer().getPluginManager().getPlugin("WorldGuard");
            if (worldGuardPlugin == null) {
                setUsingWorldGuard(false);
            }
        }

        getServer().getPluginManager().registerEvents(new PickupArrowsListener(this), this);

        getServer().getPluginCommand("pickuparrows").setExecutor(new PickupArrowsCommandExecutor(this));
        
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        	new de.dustplanet.pickuparrows.PickupArrowsPAPI(this).register();
        	Bukkit.getServer().getLogger().info("Hooked into PlaceholderAPI!");
        }
        
        new Metrics(this, BSTATS_PLUGIN_ID);

    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    @SuppressFBWarnings(value = "SACM_STATIC_ARRAY_CREATED_IN_METHOD", justification = "Only called once")
    private void setConfigDefaults() {
        final FileConfiguration config = getConfig();
        config.options().header("For help please refer to the bukkit dev page: https://dev.bukkit.org/projects/pickuparrows");
        config.addDefault("usePermissions", Boolean.TRUE);
        final String[] temp = { "skeleton", "player", "dispenser", "drowned", "pillager" };
        for (final String s : temp) {
            config.addDefault("pickupFrom." + s + ".fire", Boolean.TRUE);
            config.addDefault("pickupFrom." + s + ".normal", Boolean.TRUE);
            config.addDefault("pickupFrom." + s + ".spectral", Boolean.TRUE);
            config.addDefault("pickupFrom." + s + ".tipped", Boolean.TRUE);
            config.addDefault("pickupFrom." + s + ".trident", Boolean.TRUE);
        }
        config.addDefault("pickupFrom.player.crossbow.fire", Boolean.TRUE);
        config.addDefault("pickupFrom.player.crossbow.normal", Boolean.TRUE);
        config.addDefault("pickupFrom.player.crossbow.spectral", Boolean.TRUE);
        config.addDefault("pickupFrom.player.crossbow.tipped", Boolean.TRUE);
        config.addDefault("pickupFrom.player.crossbow.multishot.fire", Boolean.FALSE);
        config.addDefault("pickupFrom.player.crossbow.multishot.normal", Boolean.FALSE);
        config.addDefault("pickupFrom.player.crossbow.multishot.spectral", Boolean.FALSE);
        config.addDefault("pickupFrom.player.crossbow.multishot.tipped", Boolean.FALSE);
        config.addDefault("pickupFrom.unknown.fire", Boolean.FALSE);
        config.addDefault("pickupFrom.unknown.normal", Boolean.FALSE);
        config.addDefault("pickupFrom.unknown.spectral", Boolean.FALSE);
        config.addDefault("pickupFrom.unknown.tipped", Boolean.FALSE);
        config.addDefault("pickupFrom.unknown.trident", Boolean.FALSE);
        config.addDefault("ignoreCreativeArrows", Boolean.FALSE);
        config.addDefault("useWorldGuard", Boolean.FALSE);
        config.addDefault("useListAsBlacklist", Boolean.FALSE);
        config.addDefault("regions", new ArrayList<String>());
        config.options().copyDefaults(true);
        saveConfig();
    }
}
