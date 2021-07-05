package tld.sima.armorstand.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;

import java.util.List;
import java.util.function.Predicate;

public class WorldInteractionCommandManger implements CommandExecutor {

    public final Main plugin = Main.getPlugin(Main.class);
    public final String cmd1 = "ScanStand";

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player){
            if(plugin.addRemoveToPlayerScanningSet(((Player) commandSender).getUniqueId())){
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
