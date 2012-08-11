package de.dustplanet.pickuparrows;

import java.util.List;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class PickupArrowsListener implements Listener {
	
	private PickupArrows plugin;
	public PickupArrowsListener(PickupArrows plugin) {
		this.plugin = plugin;
	}

    @EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		// Arrow?
		if (projectile == null || !(projectile instanceof Arrow)) return;
		Arrow arrow = (Arrow) projectile;
		Entity shooter = projectile.getShooter();
		// If only skeletons is true, return if shooter isn't a skeleton
		if (plugin.config.getBoolean("skeletonsOnly")) {
            if (shooter == null || !(shooter instanceof Skeleton)) {
            	plugin.log("Only skeltons arrows are allowed, this arrow is not from a skeleton");
                return;
            }
        }

        // Anyone can pickup
        if (!plugin.config.getBoolean("usePermissions")) {
            plugin.log("Allowed by configuration");
            setAllowPickup(arrow);
            return;
        }
        
        // Check now for the shooter -> if null not able to get the entities around
        if (shooter == null) return;

        // Otherwise, check if anyone nearby is allowed
        double r = plugin.config.getDouble("range");
        List<Entity> nearbyEntities = shooter.getNearbyEntities(r, r, r);
        for (Entity nearbyEntity: nearbyEntities) {
            if (nearbyEntity instanceof Player) {
            	// Player nearby?
                Player player = (Player) nearbyEntity;
                if (player.hasPermission("pickuparrows.allow")) {
                	// Allow
                    plugin.log(player.getName() + " has got the permission to pickup this arrow");
                    setAllowPickup(arrow);
                    return;
                }
                else plugin.log(player.getName() + " hasn't got the permission to pickup this arrow");
            }
        }
        plugin.log("Nothing changed");
	}

    private void setAllowPickup(Arrow arrow) {
        ((CraftArrow)arrow).getHandle().fromPlayer = 1;
    }
}