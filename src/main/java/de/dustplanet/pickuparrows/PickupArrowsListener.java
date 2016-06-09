package de.dustplanet.pickuparrows;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

// WorldGuard
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.minecraft.server.v1_10_R1.EntityArrow.PickupStatus;

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

        boolean onFire = onFire(arrow);
        boolean isSpectral = isSpectral(arrow);
        boolean isTipped = isTipped(arrow);

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
        // New check for flexible configuration
        if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".fire") && onFire) {
            setPickup(arrow, PickupStatus.ALLOWED);
        } else if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".normal") && !onFire && !isSpectral && !isTipped) {
            setPickup(arrow, PickupStatus.ALLOWED);
        } else if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".spectral") && isSpectral && !isTipped) {
            setPickup(arrow, PickupStatus.ALLOWED);
        } else if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".tipped") && !isSpectral && isTipped) {
            setPickup(arrow, PickupStatus.ALLOWED);
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

    private boolean onFire(Arrow arrow) {
        boolean onFire = false;
        if (arrow.getFireTicks() > 0) {
            onFire = true;
        }
        return onFire;
    }

    private boolean isTipped(Arrow arrow) {
        return arrow instanceof TippedArrow;

    }

    private boolean isSpectral(Arrow arrow) {
        return arrow instanceof SpectralArrow;
    }

    @EventHandler
    public void onPlayerPickupArrow(PlayerPickupArrowEvent event) {
        if (!plugin.getConfig().getBoolean("usePermissions", false)) {
            return;
        }
        Arrow arrow = event.getArrow();
        boolean onFire = onFire(arrow);
        boolean isSpectral = isSpectral(arrow);
        boolean isTipped = isTipped(arrow);

        Player player = event.getPlayer();

        if (onFire && !player.hasPermission("pickuparrows.allow.fire")) {
            event.setCancelled(true);
        } else if (!onFire && !isSpectral && !isTipped && !player.hasPermission("pickuparrows.allow.normal")) {
            event.setCancelled(true);
        } else if (isSpectral && !isTipped && !player.hasPermission("pickuparrows.allow.spectral")) {
            event.setCancelled(true);
        } else if (!isSpectral && isTipped && !player.hasPermission("pickuparrows.allow.tipped")) {
            event.setCancelled(true);
        }
    }
}
