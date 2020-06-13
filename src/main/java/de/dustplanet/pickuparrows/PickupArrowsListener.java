package de.dustplanet.pickuparrows;

import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles projectile hit and pickup events.
 *
 * @author timbru31
 * @author mushroomhostage
 */

@SuppressWarnings({ "PMD.GodClass", "checkstyle:ClassFanOutComplexity", "checkstyle:MultipleStringLiterals" })
@SuppressFBWarnings({ "IMC_IMMATURE_CLASS_NO_TOSTRING", "CD_CIRCULAR_DEPENDENCY", "FCCD_FIND_CLASS_CIRCULAR_DEPENDENCY" })
public class PickupArrowsListener implements Listener {
    private final PickupArrows plugin;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public PickupArrowsListener(final PickupArrows instance) {
        plugin = instance;
    }

    /**
     * Event called when a projectile lands.
     *
     * @param event a ProjectileHitEvent
     */
    @EventHandler
    @SuppressWarnings({ "checkstyle:ReturnCount", "PMD.AvoidDuplicateLiterals", "PMD.DataflowAnomalyAnalysis", "PMD.CyclomaticComplexity",
            "checkstyle:CyclomaticComplexity" })
    public void onProjectileHitEvent(final ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();
        if (!(projectile instanceof AbstractArrow)) {
            return;
        }

        final AbstractArrow arrow = (AbstractArrow) projectile;

        // Return if arrow is creative
        if (plugin.getConfig().getBoolean("ignoreCreativeArrows", false) && getPickup(arrow) == PickupStatus.CREATIVE_ONLY) {
            return;
        }

        // Make WorldGuard check
        if (plugin.isUsingWorldGuard()) {
            final RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
            final RegionQuery query = regionContainer.createQuery();
            final ApplicableRegionSet regionList = query.getApplicableRegions(BukkitAdapter.adapt(arrow.getLocation()));
            // If we use a whitelist and no regions are here, cancel
            if (regionList.size() == 0 && !plugin.isBlacklist()) {
                return;
            }
            // Iterate through the regions
            for (final ProtectedRegion region : regionList) {
                final String regionName = region.getId();
                // Either it's on the blacklist or not on the whitelist --> cancel
                if (plugin.isBlacklist() && plugin.getRegions().contains(regionName) || !plugin.getRegions().contains(regionName)) {
                    return;
                }
            }
        }

        final ProjectileSource shooter = projectile.getShooter();
        String shooterName = getShooterName(shooter);
        final boolean onFire = onFire(arrow);
        final boolean isSpectral = isSpectral(arrow);
        final boolean isTipped = isTipped(arrow);
        final boolean isTrident = isTrident(arrow);
        // First deny it & then check if we can allow it again
        setPickup(arrow, PickupStatus.DISALLOWED);

        // Check if shooterName is in config, otherwise fallback again
        if (!plugin.getConfig().contains("pickupFrom." + shooterName)) {
            shooterName = "unknown";
        }
        setPickupStatus(arrow, shooterName, onFire, isSpectral, isTipped, isTrident);
    }

    @SuppressWarnings({ "checkstyle:CyclomaticComplexity", "PMD.CyclomaticComplexity" })
    private void setPickupStatus(final AbstractArrow arrow, final String shooterName, final boolean onFire, final boolean isSpectral,
            final boolean isTipped, final boolean isTrident) {
        if (isTrident) {
            if (plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".trident")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            }
        } else {
            if (onFire && plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".fire")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            } else if (!onFire && !isSpectral && !isTipped && plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".normal")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            } else if (isSpectral && !isTipped && plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".spectral")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            } else if (!isSpectral && isTipped && plugin.getConfig().getBoolean("pickupFrom." + shooterName + ".tipped")) {
                setPickup(arrow, PickupStatus.ALLOWED);
            }
        }
    }

    @SuppressWarnings({ "static-method", "PMD.AvoidDuplicateLiterals", "PMD.DataflowAnomalyAnalysis",
            "PMD.UseStringBufferForStringAppends" })
    @SuppressFBWarnings("ITC_INHERITANCE_TYPE_CHECKING")
    private String getShooterName(final ProjectileSource shooter) {
        String shooterName = "unknown";
        if (shooter instanceof Player) {
            shooterName = "player";
            final ItemStack crossbow = ((Player) shooter).getInventory().getItemInMainHand();
            final boolean isCrossbow = crossbow.getType() == Material.CROSSBOW;
            if (isCrossbow) {
                shooterName += ".crossbow";
            }
            if (isCrossbow && crossbow.containsEnchantment(Enchantment.MULTISHOT)) {
                shooterName += ".multishot";
            }
        } else if (shooter instanceof BlockProjectileSource) {
            shooterName = ((BlockProjectileSource) shooter).getBlock().getType().name().toLowerCase(Locale.ENGLISH);
        } else if (shooter instanceof LivingEntity) {
            shooterName = ((LivingEntity) shooter).getType().toString().toLowerCase(Locale.ENGLISH);
        }
        return shooterName;
    }

    /**
     * Handles the picking up of arrows.
     *
     * @param event the PickupArrowsEvent
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerPickupArrow(final PlayerPickupArrowEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getDisabledPlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        if (!plugin.getConfig().getBoolean("usePermissions", false)) {
            return;
        }

        checkArrowStates(event, player);
    }

    @SuppressWarnings({ "deprecation", "PMD.DataflowAnomalyAnalysis", "PMD.CyclomaticComplexity", "PMD.ConfusingTernary",
            "checkstyle:CyclomaticComplexity" })
    private void checkArrowStates(final PlayerPickupArrowEvent event, final Player player) {
        final AbstractArrow arrow = event.getArrow();
        final boolean onFire = onFire(arrow);
        final boolean isSpectral = isSpectral(arrow);
        final boolean isTipped = isTipped(arrow);
        final boolean isTrident = isTrident(arrow);

        if (onFire && !player.hasPermission("pickuparrows.allow.fire")) {
            event.setCancelled(true);
        } else if (!onFire && !isSpectral && !isTipped && !player.hasPermission("pickuparrows.allow.normal")) {
            event.setCancelled(true);
        } else if (isSpectral && !isTipped && !player.hasPermission("pickuparrows.allow.spectral")) {
            event.setCancelled(true);
        } else if (!isSpectral && isTipped && !player.hasPermission("pickuparrows.allow.tipped")) {
            event.setCancelled(true);
        } else if (isTrident && !player.hasPermission("pickuparrows.allow.trident")) {
            event.setCancelled(true);
        }
    }

    /**
     * Sets whether the arrow is from a player or not.
     *
     * @param arrow to change
     * @param status PickupStatus (allowed, disallowed, creative only)
     */
    @SuppressWarnings({ "static-method", "PMD.AvoidDuplicateLiterals" })
    private void setPickup(final AbstractArrow arrow, final PickupStatus status) {
        arrow.setPickupStatus(status);
    }

    /**
     * Returns the current pickup state of an arrow.
     *
     * @param arrow the arrow
     * @return PickupStatus (allowed, disallowed, creative only)
     */
    @SuppressWarnings("static-method")
    private PickupStatus getPickup(final AbstractArrow arrow) {
        return arrow.getPickupStatus();
    }

    @SuppressWarnings({ "static-method", "PMD.DataflowAnomalyAnalysis" })
    private boolean onFire(final AbstractArrow arrow) {
        boolean onFire = false;
        if (arrow.getFireTicks() > 0) {
            onFire = true;
        }
        return onFire;
    }

    @SuppressWarnings("static-method")
    private boolean isTipped(final AbstractArrow arrow) {
        return arrow instanceof Arrow && ((Arrow) arrow).getBasePotionData().getType() != PotionType.UNCRAFTABLE;

    }

    @SuppressWarnings("static-method")
    private boolean isSpectral(final AbstractArrow arrow) {
        return arrow instanceof SpectralArrow;
    }

    @SuppressWarnings("static-method")
    private boolean isTrident(final AbstractArrow arrow) {
        return arrow instanceof Trident;
    }
}
