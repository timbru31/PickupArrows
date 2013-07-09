package de.dustplanet.pickuparrows;

import java.util.List;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
// WorldGuard
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 * PickupArrows for CraftBukkit/Bukkit
 * Handles activities (ProjectileHit)!
 * Refer to the forum thread:
 * http://bit.ly/pathread
 * 
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/papagedev
 * 
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrowsListener implements Listener {
    private PickupArrows plugin;

    public PickupArrowsListener(PickupArrows instance) {
	plugin = instance;
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
	Projectile projectile = event.getEntity();
	// Arrow?
	if (projectile == null || !(projectile instanceof Arrow)) {
	    return;
	}
	// Get data
	Arrow arrow = (Arrow) projectile;
	Entity shooter = projectile.getShooter();
	boolean onFire = arrow.getFireTicks() > 0 ? true : false;
	
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
	// If it's a fire arrow and they should be disabled, return here
	if (onFire && !plugin.config.getBoolean("pickupFrom.fire")) {
	    return;
	}
	// If it's still on fire
	else if (onFire) {
	    // If we have no player in the near range and we use permissions
	    if (plugin.config.getBoolean("usePermission") && !rangeCheck(arrow, "fire")) {
		return;
	    }
	}
	// Shooter is a skeleton and they are enabled
	if (shooter instanceof Skeleton && plugin.config.getBoolean("pickupFrom.skeleton")) {
	    // If we have any player in the near range or we don't use permissions
	    if (!plugin.config.getBoolean("usePermission") || rangeCheck(arrow, "skeleton")) {
		setPickup(arrow, 1);
	    }
	}
	// Shooter is a player and they are enabled
	else if (shooter instanceof Player && plugin.config.getBoolean("pickupFrom.player")) {
	    // If we have any player in the near range or we don't use permissions
	    if (!plugin.config.getBoolean("usePermission") || rangeCheck(arrow, "player")) {
		setPickup(arrow, 1);
	    }
	}
	// Unknown shooter (like a dispenser) and they are enabled
	else if (shooter == null && plugin.config.getBoolean("pickupFrom.other")) {
	    // If we have any player in the near range or we don't use permissions
	    if (!plugin.config.getBoolean("usePermission") || rangeCheck(arrow, "other"))  {
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

    private boolean rangeCheck(Arrow arrow, String suffix) {
	// Get the range
	double r = plugin.config.getDouble("range");
	// Check for near entities
	List<Entity> nearbyEntities = arrow.getNearbyEntities(r, r, r);
	for (Entity nearbyEntity: nearbyEntities) {
	    // Player found
	    if (nearbyEntity instanceof Player) {
		Player player = (Player) nearbyEntity;
		// Check his permission
		if (player.hasPermission("pickuparrows.allow." + suffix)) {
		    return true;
		} else {
		    return false;
		}
	    }
	}
	return false;
    }
}