package de.dustplanet.pickuparrows;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import net.minecraft.server.v1_10_R1.EntityArrow.PickupStatus;

/**
 * PickupArrows for CraftBukkit/Spigot
 * Handles the test cases
 *
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/pickuparrows/
 *
 * @author xGhOsTkiLLeRx
 * thanks to Pandarr for the awesome tutorial
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProjectileHitEvent.class, PickupArrowsListener.class})
public class PickupArrowsListenerTest {
    private ProjectileHitEvent mockEvent = PowerMockito.mock(ProjectileHitEvent.class);
    private PickupArrowsListener listener;
    private PickupArrows plugin;
    private Projectile arrow = mock(Arrow.class);
    private String[] cazes = {"player", "dispenser", "skeleton", "unknown"};

    @Before
    public void initialize() {
        plugin = mock(PickupArrows.class);
        listener = PowerMockito.spy(new PickupArrowsListener(plugin));
        // We never use WorldGuard
        when(plugin.isUsingWorldGuard()).thenReturn(false);

        // Default mocks
        when(mockEvent.getEntity()).thenReturn(arrow);
        when(plugin.getConfig()).thenReturn(mock(FileConfiguration.class));

        // Always ignore setPickup private method
        try {
            PowerMockito.doNothing().when(listener, "setPickup", arrow, PickupStatus.ALLOWED);
            PowerMockito.doNothing().when(listener, "setPickup", arrow, PickupStatus.DISALLOWED);
            PowerMockito.doReturn(PickupStatus.DISALLOWED).when(listener, "getPickup", arrow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for
     * {@link de.dustplanet.pickuparrows.PickupArrowsListener#onProjectileHitEvent(org.bukkit.event.entity.ProjectileHitEvent)}
     *
     */
    @Test
    public void testWrongEntity() {
        // a null entity should be filtered out
        when(mockEvent.getEntity()).thenReturn(null);
        listener.onProjectileHitEvent(mockEvent);

        // A Fish is a Projectile, too, but shouldn't pass the entity check
        Projectile fish = mock(FishHook.class);
        when(mockEvent.getEntity()).thenReturn(fish);
        listener.onProjectileHitEvent(mockEvent);
    }

    @Test
    public void testCases() {
        for (String caze : cazes) {
            ProjectileSource shooter;
            if (caze.equalsIgnoreCase("player")) {
                shooter = mock(Player.class);
            } else if (caze.equalsIgnoreCase("skeleton")) {
                shooter = mock(Skeleton.class);
                stub(((LivingEntity) shooter).getType()).toReturn(EntityType.SKELETON);
            } else if (caze.equalsIgnoreCase("dispenser")) {
                shooter = mock(BlockProjectileSource.class);
                Block stubbedBlock = mock(Block.class);
                stub(((BlockProjectileSource) shooter).getBlock()).toReturn(stubbedBlock);
                when(stubbedBlock.getType()).thenReturn(Material.DISPENSER);
            } else {
                shooter = null;
            }

            when(mockEvent.getEntity().getShooter()).thenReturn(shooter);

            // Iterate through permission values
            boolean v[] = {false, true};
            for (boolean value: v) {
                usePermissions(value);
                // Defaults
                when(plugin.getConfig().contains("pickupFrom." + caze)).thenReturn(true);
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".normal")).thenReturn(true);
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".fire")).thenReturn(true);

                /*
                 * Case 1 is no fire and allowed
                 */
                onFire(0);
                listener.onProjectileHitEvent(mockEvent);

                /*
                 * Case 2 is fire and allowed
                 */
                onFire(1);
                listener.onProjectileHitEvent(mockEvent);

                /*
                 * Case 3 is fire and disallowed
                 */
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".fire")).thenReturn(false);
                listener.onProjectileHitEvent(mockEvent);

                /*
                 * Case 4 is no fire and disallowed
                 */
                onFire(0);
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".normal")).thenReturn(false);
                listener.onProjectileHitEvent(mockEvent);
            }
        }

        // we need to verify private invocations after the for loop because it's not reset
        try {
            PowerMockito.verifyPrivate(listener, times(32)).invoke("setPickup", arrow, PickupStatus.DISALLOWED);
            PowerMockito.verifyPrivate(listener, times(16)).invoke("setPickup", arrow, PickupStatus.ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void onFire(int ticks) {
        when(arrow.getFireTicks()).thenReturn(ticks);
    }

    private void usePermissions(boolean perm) {
        when(plugin.getConfig().getBoolean("usePermissions")).thenReturn(perm);
    }
}
