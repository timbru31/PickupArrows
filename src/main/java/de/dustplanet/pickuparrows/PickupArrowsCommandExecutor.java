package de.dustplanet.pickuparrows;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Command handler of PickupArrows to disable or enable the picking up of arrows.
 *
 * @author timbru31
 */
@SuppressFBWarnings("IMC_IMMATURE_CLASS_NO_TOSTRING")
public class PickupArrowsCommandExecutor implements CommandExecutor {
    private final PickupArrows plugin;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public PickupArrowsCommandExecutor(final PickupArrows instance) {
        plugin = instance;
    }

    @Override
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] arguments) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console can't change the pickup status");
            return true;
        }
        final Player player = (Player) sender;
        final UUID uniqueId = player.getUniqueId();
        if (this.plugin.getDisabledPlayers().contains(uniqueId)) {
            this.plugin.getDisabledPlayers().remove(uniqueId);
            sender.sendMessage(ChatColor.YELLOW + "[PickupArrows] " + ChatColor.DARK_GREEN + "Successfully enabled picking up arrows!");
        } else {
            this.plugin.getDisabledPlayers().add(uniqueId);
            sender.sendMessage(ChatColor.YELLOW + "[PickupArrows] " + ChatColor.DARK_GREEN + "Successfully disabled picking up arrows!");
        }
        return true;
    }

}
