package de.dustplanet.pickuparrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.defaultConstructorIn;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles the test cases for disabling. Thanks to Pandarr for the awesome tutorial.
 *
 * @author timbru31
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnitPlatform.class)
@PrepareForTest(JavaPlugin.class)
@SuppressWarnings({ "PMD.AtLeastOneConstructor", "checkstyle:MissingCtor", "PMD.TooManyStaticImports" })
public class PickupArrowsTest {
    private PickupArrows plugin;

    /**
     * Initializes the test.
     */
    @BeforeEach
    public void initialize() {
        suppress(defaultConstructorIn(PickupArrows.class));
        plugin = new PickupArrows();
    }

    /**
     * Test method for {@link de.dustplanet.pickuparrows.PickupArrows#onDisable()}.
     *
     * @throws SecurityException in case of an error
     * @throws IllegalAccessException in case of an error
     */
    @Test
    @SuppressFBWarnings("RFI_SET_ACCESSIBLE")
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    public void testOnDisable() throws NoSuchFieldException, IllegalAccessException {
        final List<String> worldList = spy(new ArrayList<String>());
        final List<UUID> disabledPlayersList = spy(new ArrayList<UUID>());
        worldList.add("I'm not empty!");
        disabledPlayersList.add(UUID.randomUUID());
        plugin.setRegions(worldList);
        final Field disabledPlayersField = PickupArrows.class.getDeclaredField("disabledPlayers");
        disabledPlayersField.setAccessible(true);
        disabledPlayersField.set(plugin, disabledPlayersList);
        plugin.onDisable();
        verify(worldList).clear();
        verify(disabledPlayersList).clear();
        assertEquals(0, worldList.size(), "worldList should be empty");
        assertEquals(0, disabledPlayersList.size(), "disabledPlayersList should be empty");
    }
}
