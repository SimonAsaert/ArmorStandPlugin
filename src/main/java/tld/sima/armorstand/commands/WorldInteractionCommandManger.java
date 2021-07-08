package tld.sima.armorstand.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tld.sima.armorstand.Main;

public class WorldInteractionCommandManger implements CommandExecutor {

    public final Main plugin = Main.getPlugin(Main.class);
    public final String cmd1 = "ScanStand";

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player){
            if(plugin.getVisualStand().addRemovePlayer(((Player) commandSender).getUniqueId())){
                commandSender.sendMessage(ChatColor.WHITE + "Scanning " + ChatColor.DARK_GREEN + "Enabled");
            }else{
                commandSender.sendMessage(ChatColor.WHITE + "Scanning " + ChatColor.DARK_RED + "Disabled");
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + "You need to be a player to use this commands!");
        }

        return true;
    }
}
