package de.dustplanet.pickuparrows;

import java.util.List;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

// WorldGuard
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 * PickupArrows for CraftBukkit/Bukkit
 * Handles activities (ProjectileHit)!
 * 
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/pickuparrows/
 * 
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrowsListener implements Listener {
    private PickupArrows plugin;

    public PickupArrowsListener(PickupArrows instance) {
	plugin = instance;
    }

    @SuppressWarnings("deprecation")
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
	boolean onFire = arrow.getFireTicks() > 0 ? true : false;
	String shooterName = "unknown";
	if (shooter instanceof Player) {
	    shooterName = "player";
	} else if (shooter instanceof BlockProjectileSource) {
	    shooterName = ((BlockProjectileSource) shooter).getBlock().getType().name().toLowerCase();
	} else if (shooter instanceof LivingEntity) {
	    shooterName  = ((LivingEntity) shooter).getType().getName().toLowerCase();
	}
		
	// Make WorldGuard check
	if (plugin.useWorldGuard && plugin.wg != null) {
	    ApplicableRegionSet regionList = plugin.wg.getRegionManager(arrow.getWorld()).getApplicableRegions(arrow.getLocation());
	    // If we use a whitelist and no regions are here, cancel
	    if (regionList.size() == 0 && !plugin.blacklist) {
		return;
	    }
	    // Iterate through the regions
	    for (ProtectedRegion region : regionList) {
		String regionName = region.getId();
		// Either it's on the blacklist or not on the whitelist --> cancel
		if ((plugin.blacklist && plugin.regions.contains(regionName)) || !plugin.regions.contains(regionName)) {
		    return;
		}
	    }
	}
	
	// First deny it & then check if we can allow it again
	setPickup(arrow, 0);
	
	// Check if shooterName is in config, otherwise fallback again
	shooterName = plugin.config.contains("pickupFrom." + shooterName)? shooterName : "unkown";

	// New check for flexible configuration
	if (plugin.config.getBoolean("pickupFrom." + shooterName + ".fire") && onFire) {
	    if (!plugin.config.getBoolean("usePermission") || rangeCheck(arrow, shooterName, (shooterName + ".fire"))) {
		setPickup(arrow, 1);
	    }
	} else if (plugin.config.getBoolean("pickupFrom." + shooterName + ".normal") && !onFire) {
	    if (!plugin.config.getBoolean("usePermission") || rangeCheck(arrow, shooterName, (shooterName + ".normal"))) {
		setPickup(arrow, 1);
	    }
	}
    }

    /* 
     * Reflection call
     * 0 = disabled
     * 1 = enabled
     */
    private void setPickup(Arrow arrow, int i) {
	((CraftArrow)arrow).getHandle().fromPlayer = i;
    }

    private boolean rangeCheck(Arrow arrow, String rangeSuffix, String permSuffix) {
	// Get the range
	double r = plugin.config.getDouble("range." + rangeSuffix , 10.0);
	// Check for near entities
	List<Entity> nearbyEntities = arrow.getNearbyEntities(r, r, r);
	for (Entity nearbyEntity: nearbyEntities) {
	    // Player found
	    if (nearbyEntity instanceof Player) {
		Player player = (Player) nearbyEntity;
		// Check his permission
		if (player.hasPermission("pickuparrows.allow." + permSuffix) || player.hasPermission("pickuparrows.allow.*")) {
		    return true;
		} else {
		    return false;
		}
	    }
	}
	return false;
    }
}