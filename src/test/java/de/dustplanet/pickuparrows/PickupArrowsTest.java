package de.dustplanet.pickuparrows;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.defaultConstructorIn;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * PickupArrows for CraftBukkit/Spigot
 * Handles the test cases
 *
 * Refer to the dev.bukkit.org page:
 * https://dev.bukkit.org/projects/pickuparrows
 *
 * @author xGhOsTkiLLeRx
 * thanks to Pandarr for the awesome tutorial
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( {JavaPlugin.class})
public class PickupArrowsTest {
    private PickupArrows plugin;

    @Before
    public void initialize() {
        suppress(defaultConstructorIn(PickupArrows.class));
        plugin = new PickupArrows();
    }

    /**
     * Test method for
     * {@link de.dustplanet.pickuparrows.PickupArrows#onDisable()}
     *
     */
    @Test
    public void testOnDisable() {
        List<String> worldList = spy(new ArrayList<String>());
        worldList.add("I'm not empty!");
        plugin.setRegions(worldList);
        plugin.onDisable();
        verify(worldList).clear();
        assertEquals("worldList should be empty", worldList.size(), 0);
    }
}
