package de.dustplanet.pickuparrows;

import java.util.List;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

// WorldGuard
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.minecraft.server.v1_9_R1.EntityArrow.PickupStatus;

/**
 * PickupArrows for CraftBukkit/Spigot.
 * Handles activities (ProjectileHit)!
 *
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/pickuparrows/
 *
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrowsListener implements Listener {
    /**
     * PickupArrows instance.
     */
    private PickupArrows plugin;

    /**
     * Creates a new PickupArrowsListener instance.
     * @param instance the PickupArrows instance
     */
    public PickupArrowsListener(PickupArrows instance) {
        plugin = instance;
    }

    /**
     * Event called when a projectile lands.
     * @param event a ProjectileHitEvent
     */
    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        // Arrow?
        if (projectile == null || !(projectile instanceof Arrow)) {
            return;
        }
        // Get data
        Arrow arrow = (Arrow) projectile;
        ProjectileSource shooter = projectile.getShooter();
        boolean onFire = false;
        if (arrow.getFireTicks() > 0) {
            onFire = true;
        }
        boolean isSpectral = projectile instanceof SpectralArrow;
        boolean isTipped = projectile instanceof TippedArrow;

        String shooterName = "unknown";
        if (shooter instanceof Player) {
            shooterName = "player";
        } else if (shooter instanceof BlockProjectileSource) {
            shooterName = ((BlockProjectileSource) shooter).getBlock().getType().name().toLowerCase();
        } else if (shooter instanceof LivingEntity) {
            shooterName  = ((LivingEntity) shooter).getType().toString().toLowerCase();
        }

        // Return if arrow is creative
        if (plugin.getConfig().getBoolean("ignoreCreativeArrows", false) && getPickup(arrow) == PickupStatus.CREATIVE_ONLY) {
            return;
        }

        // Make WorldGuard check
        if (plugin.isUsingWorldGuard() && plugin.getWorldGuard() != null) {
            ApplicableRegionSet regionList = plugin.getWorldGuard().getRegionManager(arrow.getWorld()).getApplicableRegions(arrow.getLocation());
            // If we use a whitelist and no regions are here, cancel
            if (regionList.size() == 0 && !plugin.isBlacklist()) {
                return;
            }
            // Iterate through the regions
            for (ProtectedRegion region : regionList) {
                String regionName = region.getId();
                // Either it's on the blacklist or not on the whitelist --> cancel
                if (plugin.isBlacklist() && plugin.getRegions().contains(regionName) || !plugin.getRegions().contains(regionName)) {
                    return;
                }
            }
        }

        // First deny it & then check if we can allow it again
        setPickup(arrow, PickupStatus.DISALLOWED);

        // Check if shooterName is in config, otherwise fallback again
        if (!plugin.getConfig().contains("pickupFrom." + shooterName)) {
            shooterName = "unknown";
        }
        System.out.println("spectral " + isSpectral);
        System.out.println("tipped " + isTipped);
        // New check for flexible configuration
        if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".fire") && onFire) {
            if (!plugin.getConfig().getBoolean("usePermissions") || rangeCheck(arrow, shooterName, shooterName + ".fire")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            }
        } else if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".normal") && !onFire && !isSpectral && !isTipped) {
            if (!plugin.getConfig().getBoolean("usePermissions") || rangeCheck(arrow, shooterName, shooterName + ".normal")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            }
        } else if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".spectral") && isSpectral && !isTipped) {
            if (!plugin.getConfig().getBoolean("usePermissions") || rangeCheck(arrow, shooterName, shooterName + ".spectral")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            }
        } else if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".tipped") && !isSpectral && isTipped) {
            if (!plugin.getConfig().getBoolean("usePermissions") || rangeCheck(arrow, shooterName, shooterName + ".tipped")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            }
        }
    }

    /**
     * Sets whether the arrow is from a player or not.
     * @param arrow to change
     * @param status PickupStatus (allowed, disallowed, creative only)
     */
    private void setPickup(Arrow arrow, PickupStatus status) {
        ((CraftArrow) arrow).getHandle().fromPlayer = status;
    }

    /**
     * Returns the current pickup state of an arrow.
     * @param arrow the arrow
     * @return PickupStatus (allowed, disallowed, creative only)
     */
    private PickupStatus getPickup(Arrow arrow) {
        return ((CraftArrow) arrow).getHandle().fromPlayer;
    }

    /**
     * A simple range by nearby entities check.
     * @param arrow the shot arrow
     * @param shooterName the shooter name
     * @param permSuffix the shooter name with normal/fire suffix
     * @return if the pickup is allowed
     */
    private boolean rangeCheck(Arrow arrow, String shooterName, String permSuffix) {
        // Get the range
        double r = plugin.getConfig().getDouble("pickupFrom." + shooterName + ".range" , 10.0);
        // Check for near entities
        List<Entity> nearbyEntities = arrow.getNearbyEntities(r, r, r);
        for (Entity nearbyEntity: nearbyEntities) {
            // Player found
            if (nearbyEntity instanceof Player) {
                Player player = (Player) nearbyEntity;
                // Check his permission
                return player.hasPermission("pickuparrows.allow." + permSuffix) || player.hasPermission("pickuparrows.allow.*");
            }
        }
        return false;
    }
}
