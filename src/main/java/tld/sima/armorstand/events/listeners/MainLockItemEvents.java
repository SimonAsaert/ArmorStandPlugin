package tld.sima.armorstand.events.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import tld.sima.armorstand.Main;

public class MainLockItemEvents {
	
	private static final Main plugin = Main.getPlugin(Main.class);
	
	public static boolean ItemParser(String itemName, Player player, ArmorStand stand) {
		if (itemName.contains("Head") && itemName.contains("rotation")) {
			StringBuilder string = new StringBuilder();
			string.append(ChatColor.GOLD).append("Head ");
			if (stand.hasMetadata("HeadLock")) {
				stand.removeMetadata("HeadLock", plugin);
				string.append(ChatColor.GREEN).append("unlocked");
			}else {
				stand.setMetadata("HeadLock", new FixedMetadataValue(plugin, true));
				string.append(ChatColor.RED).append("locked");
			}
			player.sendMessage(string.toString());
			return true;
		}else if (itemName.contains("Torso") && itemName.contains("rotation")) {
			StringBuilder string = new StringBuilder();
			string.append(ChatColor.GOLD).append("Torso ");
			if (stand.hasMetadata("TorsoLock")) {
				stand.removeMetadata("TorsoLock", plugin);
				string.append(ChatColor.GREEN).append("unlocked");
			}else {
				stand.setMetadata("TorsoLock", new FixedMetadataValue(plugin, true));
				string.append(ChatColor.RED).append("locked");
			}
			player.sendMessage(string.toString());
			return true;
		}else if (itemName.contains("Left Arm's") && itemName.contains("rotation")) {
			StringBuilder string = new StringBuilder();
			string.append(ChatColor.GOLD).append("Left Arm ");
			if (stand.hasMetadata("LeftArmLock")) {
				stand.removeMetadata("LeftArmLock", plugin);
				string.append(ChatColor.GREEN).append("unlocked");
			}else {
				stand.setMetadata("LeftArmLock", new FixedMetadataValue(plugin, true));
				string.append(ChatColor.RED).append("locked");
			}
			player.sendMessage(string.toString());
			return true;
		}else if (itemName.contains("Right Arm's") && itemName.contains("rotation")) {
			StringBuilder string = new StringBuilder();
			string.append(ChatColor.GOLD).append("Right Arm ");
			if (stand.hasMetadata("RightArmLock")) {
				stand.removeMetadata("RightArmLock", plugin);
				string.append(ChatColor.GREEN).append("unlocked");
			}else {
				stand.setMetadata("RightArmLock", new FixedMetadataValue(plugin, true));
				string.append(ChatColor.RED).append("locked");
			}
			player.sendMessage(string.toString());
			return true;
		}else if (itemName.contains("Left Leg's") && itemName.contains("rotation")) {
			StringBuilder string = new StringBuilder();
			string.append(ChatColor.GOLD).append("Left Leg ");
			if (stand.hasMetadata("LeftLegLock")) {
				stand.removeMetadata("LeftLegLock", plugin);
				string.append(ChatColor.GREEN).append("unlocked");
			}else {
				stand.setMetadata("LeftLegLock", new FixedMetadataValue(plugin, true));
				string.append(ChatColor.RED).append("locked");
			}
			player.sendMessage(string.toString());
			return true;
		}else if (itemName.contains("Right Leg's") && itemName.contains("rotation")) {
			StringBuilder string = new StringBuilder();
			string.append(ChatColor.GOLD).append("Right Leg ");
			if (stand.hasMetadata("RightLegLock")) {
				stand.removeMetadata("RightLegLock", plugin);
				string.append(ChatColor.GREEN).append("unlocked");
			}else {
				stand.setMetadata("RightLegLock", new FixedMetadataValue(plugin, true));
				string.append(ChatColor.RED).append("locked");
			}
			player.sendMessage(string.toString());
			return true;
		}
		return false;
	}
}
