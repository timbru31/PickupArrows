package de.dustplanet.pickuparrows;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles the test cases for the listener. Thanks to Pandarr for the awesome tutorial.
 *
 * @author timbru31
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnitPlatform.class)
@PrepareForTest({ ProjectileHitEvent.class, PickupArrowsListener.class })
@SuppressWarnings({ "PMD.AtLeastOneConstructor", "checkstyle:MissingCtor", "PMD.TooManyStaticImports", "PMD.ExcessiveImports",
        "checkstyle:MultipleStringLiterals" })
public class PickupArrowsListenerTest {
    private final ProjectileHitEvent mockEvent = PowerMockito.mock(ProjectileHitEvent.class);
    private PickupArrowsListener listener;
    private PickupArrows plugin;
    private final Projectile arrow = mock(Arrow.class);
    private final String[] cazes = { "player", "dispenser", "skeleton", "unknown" };

    /**
     * Initializes the test.
     */
    @BeforeEach
    @SuppressWarnings({ "checkstyle:IllegalCatch", "PMD.AvoidDuplicateLiterals", "PMD.AvoidCatchingGenericException",
            "PMD.AvoidPrintStackTrace" })
    @SuppressFBWarnings({ "PRMC_POSSIBLY_REDUNDANT_METHOD_CALLS", "IMC_IMMATURE_CLASS_PRINTSTACKTRACE",
            "INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE" })
    public void initialize() {
        plugin = mock(PickupArrows.class);
        listener = PowerMockito.spy(new PickupArrowsListener(plugin));
        // We never use WorldGuard
        when(plugin.isUsingWorldGuard()).thenReturn(Boolean.FALSE);

        // Default mocks
        when(mockEvent.getEntity()).thenReturn(arrow);
        when(plugin.getConfig()).thenReturn(mock(FileConfiguration.class));

        // Always ignore setPickup private method
        try {
            PowerMockito.doNothing().when(listener, "setPickup", arrow, PickupStatus.ALLOWED);
            PowerMockito.doNothing().when(listener, "setPickup", arrow, PickupStatus.DISALLOWED);
            PowerMockito.doReturn(PickupStatus.DISALLOWED).when(listener, "getPickup", arrow);
            PowerMockito.doReturn(Boolean.FALSE).when(listener, "isTipped", arrow);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for
     * {@link de.dustplanet.pickuparrows.PickupArrowsListener#onProjectileHitEvent(org.bukkit.event.entity.ProjectileHitEvent)}.
     */
    @Test
    @SuppressWarnings({ "PMD.AvoidCatchingNPE", "PMD.AvoidCatchingGenericException", "PMD.JUnitTestContainsTooManyAsserts" })
    public void testWrongEntity() {
        // a null entity should be filtered out
        when(mockEvent.getEntity()).thenReturn(null);
        try {
            listener.onProjectileHitEvent(mockEvent);
        } catch (final NullPointerException e) {
            assertNull(e, "A wrong entity should throw a NPE");
        }

        // A Fish is a Projectile, too, but shouldn't pass the entity check
        final Projectile fish = mock(FishHook.class);
        when(mockEvent.getEntity()).thenReturn(fish);
        try {
            listener.onProjectileHitEvent(mockEvent);
        } catch (final ClassCastException e) {
            assertNull(e, "An invalid entity should throw a ClassCastException");
        }
    }

    /**
     * Tests the different pickup cases with various config settings.
     *
     * @throws Exception in case of an error
     */
    @Test
    @SuppressWarnings({ "checkstyle:MagicNumber", "checkstyle:IllegalCatch", "checkstyle:ExecutableStatementCount",
            "PMD.AvoidCatchingGenericException", "PMD.AvoidPrintStackTrace", "PMD.AvoidInstantiatingObjectsInLoops",
            "PMD.AvoidDuplicateLiterals" })
    @SuppressFBWarnings({ "IMC_IMMATURE_CLASS_PRINTSTACKTRACE", "INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE" })
    public void testCases() throws Exception {
        for (final String caze : cazes) {
            final ProjectileSource shooter;
            if ("player".equalsIgnoreCase(caze)) {
                shooter = mock(Player.class);
                final PlayerInventory inv = mock(PlayerInventory.class);
                PowerMockito.doReturn(new ItemStack(Material.BOW)).when(inv, "getItemInMainHand");
                PowerMockito.doReturn(inv).when(shooter, "getInventory");
            } else if ("skeleton".equalsIgnoreCase(caze)) {
                shooter = mock(Skeleton.class);
                final EntityEquipment eqipment = mock(EntityEquipment.class);
                PowerMockito.doReturn(new ItemStack(Material.BOW)).when(eqipment, "getItemInMainHand");
                PowerMockito.doReturn(eqipment).when(shooter, "getEquipment");
                when(((LivingEntity) shooter).getType()).thenReturn(EntityType.SKELETON);
            } else if ("dispenser".equalsIgnoreCase(caze)) {
                shooter = mock(BlockProjectileSource.class);
                final Block stubbedBlock = mock(Block.class);
                when(((BlockProjectileSource) shooter).getBlock()).thenReturn(stubbedBlock);
                when(stubbedBlock.getType()).thenReturn(Material.DISPENSER);
            } else {
                shooter = null;
            }

            when(mockEvent.getEntity().getShooter()).thenReturn(shooter);

            // Iterate through permission values
            final boolean[] booleanValues = { false, true };
            for (final boolean value : booleanValues) {
                usePermissions(value);
                // Defaults
                when(plugin.getConfig().contains("pickupFrom." + caze)).thenReturn(Boolean.TRUE);
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".normal")).thenReturn(Boolean.TRUE);
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".fire")).thenReturn(Boolean.TRUE);

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
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".fire")).thenReturn(Boolean.FALSE);
                listener.onProjectileHitEvent(mockEvent);

                /*
                 * Case 4 is no fire and disallowed
                 */
                onFire(0);
                when(plugin.getConfig().getBoolean("pickupFrom." + caze + ".normal")).thenReturn(Boolean.FALSE);
                listener.onProjectileHitEvent(mockEvent);
            }
        }

        // we need to verify private invocations after the for loop because it's not reset
        try {
            PowerMockito.verifyPrivate(listener, times(32)).invoke("setPickup", arrow, PickupStatus.DISALLOWED);
            PowerMockito.verifyPrivate(listener, times(16)).invoke("setPickup", arrow, PickupStatus.ALLOWED);
        } catch (final Exception e) {
            e.printStackTrace();
            fail("Test need to fail in case of an excetpion");
        }
    }

    private void onFire(final int ticks) {
        when(arrow.getFireTicks()).thenReturn(ticks);
    }

    private void usePermissions(final boolean perm) {
        when(plugin.getConfig().getBoolean("usePermissions")).thenReturn(perm);
    }
}
