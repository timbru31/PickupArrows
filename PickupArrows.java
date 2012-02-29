package me.exphc.PickupArrows;

import java.util.logging.Logger;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.*;

import org.bukkit.craftbukkit.entity.CraftArrow;

class PickupArrowsListener implements Listener {
	PickupArrows plugin;

	public PickupArrowsListener(PickupArrows plugin) {
		this.plugin = plugin;

		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

    @EventHandler(priority = EventPriority.NORMAL)
	public void onEntityShootBow(EntityShootBowEvent event) {
		Entity projectile = event.getProjectile();
		if (projectile == null || !(projectile instanceof Arrow)) {
			return;
		}
		Arrow arrow = (Arrow)projectile;

		Entity shooter = event.getEntity();
		if (plugin.getConfig().getBoolean("skeletonsOnly", true)) {
            if (shooter == null || !(shooter instanceof Skeleton)) {
                return;
            }
        }

        // anyone can pickup
        if (!plugin.getConfig().getBoolean("usePermissions", false)) {
            plugin.log("allowed by configuration");
            setAllowPickup(arrow);
            return;
        }

        // otherwise, check if anyone nearby is allowed
        double r = plugin.getConfig().getDouble("range", 10.0);
        List<Entity> nearbyEntities = shooter.getNearbyEntities(r, r, r);
        for (Entity nearbyEntity: nearbyEntities) {
            if (nearbyEntity instanceof Player) {
                Player player = (Player)nearbyEntity;

                if (player.hasPermission("pickuparrows.allow")) {
                    plugin.log("allowed by permissions of "+player.getName());
                    setAllowPickup(arrow);
                    return;
                } else {
                    plugin.log("no permission from "+player.getName());
                }
            }
        }
        plugin.log("not changed");
	}

    public void setAllowPickup(Arrow arrow) {
        ((CraftArrow)arrow).getHandle().fromPlayer = true;
    }
}

public class PickupArrows extends JavaPlugin {
    Logger log = Logger.getLogger("Minecraft");

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

		new PickupArrowsListener(this);
    }

    public void onDisable() {
    }

    public void log(String message) {
        if (getConfig().getBoolean("debug", false)) {
            log.info(message);
        }
    }
}
