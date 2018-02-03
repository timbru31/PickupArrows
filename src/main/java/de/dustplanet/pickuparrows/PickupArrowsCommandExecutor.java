package de.dustplanet.pickuparrows;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PickupArrowsCommandExecutor implements CommandExecutor {
    private PickupArrows plugin;

    public PickupArrowsCommandExecutor(PickupArrows instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] arguments) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "The console can't change the pickup status");
            return true;
        }
        Player player = (Player) sender;
        UUID uniqueId = player.getUniqueId();
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
