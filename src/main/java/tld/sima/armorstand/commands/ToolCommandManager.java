package tld.sima.armorstand.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.utils.ToolType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ToolCommandManager implements CommandExecutor, TabExecutor {

	private final Main plugin = Main.getPlugin(Main.class);
	
	public final String cmd1 = "atool";
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(command.getName().equalsIgnoreCase(cmd1)) {
				if(args.length == 0) {
					player.sendMessage(ChatColor.GOLD + "Use " + ChatColor.WHITE + "/atool <TYPE> " + ChatColor.GOLD + "to have the item in your hand set to a mass-altering tool.");
					player.sendMessage(ChatColor.GOLD + "Then left-click a stand to toggle the stand using this method.");
					StringBuilder builder = getToolTypesBuilder();
					player.sendMessage(builder.toString());
				}else {
					ToolType type;
					try {
						type = ToolType.valueOf(args[0].toUpperCase());
					}catch(IllegalArgumentException e) {
						player.sendMessage(ChatColor.RED + "Unable to find that tool!");
						StringBuilder builder = getToolTypesBuilder();
						player.sendMessage(builder.toString());
						return true;
					}
					ItemStack item = player.getInventory().getItemInMainHand();
					if(item.getType().equals(Material.AIR) || item.isSimilar(plugin.getCloneTool()) || item.isSimilar(plugin.getRemoveTool())) {
						player.sendMessage(ChatColor.RED + "Unable to bind tool to hand or use currently used tools.");
						return true;
					}
					plugin.setArmorstandTool(player.getUniqueId(), type, item.getType());
					player.sendMessage(ChatColor.GOLD + "Changed tool type to: " + ChatColor.WHITE + type.toString());
				}
				return true;
			}
		}else {
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "You have to be a Player to use this command!");
			}
		return false;
	}
	
	public StringBuilder getToolTypesBuilder() {
		ToolType[] types = ToolType.values();
		StringBuilder builder = new StringBuilder();
		builder.append(ChatColor.GOLD).append("Current types: ").append(ChatColor.WHITE).append(types[0]);
		for(int i = 1 ; i < types.length -1 ; i++) {
			builder.append(", ").append(types[i]);
		}
		return builder;
	}

	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
		List<String> arguments = new ArrayList<String>();
		if (command.getName().equalsIgnoreCase(cmd1)){

			if (strings.length == 0){
				arguments = tabCompleteATool("");
			}else if (strings.length == 1){
				arguments = tabCompleteATool(strings[0]);
			}
		}
		return arguments;
	}

	private List<String> tabCompleteATool(String inputString){
		inputString = "^" + inputString;
		List<String> output = new ArrayList<String>();
		Pattern pattern = Pattern.compile(inputString, Pattern.CASE_INSENSITIVE);
		for (ToolType argument: ToolType.values()){
			if (pattern.matcher(argument.toString()).find()){
				output.add(argument.name().toLowerCase());
			}
		}
		return output;
	}

}
