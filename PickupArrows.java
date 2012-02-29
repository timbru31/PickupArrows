package me.exphc.PickupArrows;

import java.util.logging.Logger;

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
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Arrow)) {
            return;
        }

        Arrow arrow = (Arrow)entity;

        ((CraftArrow)arrow).getHandle().fromPlayer = true;
	}
}

public class PickupArrows extends JavaPlugin {
    Logger log = Logger.getLogger("Minecraft");

    public void onEnable() {
		new PickupArrowsListener(this);
    }

    public void onDisable() {
    }
}
