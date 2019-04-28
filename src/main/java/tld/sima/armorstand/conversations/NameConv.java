package tld.sima.armorstand.conversations;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.inventories.OptionsMenuInventory;

public class NameConv extends StringPrompt {
	private UUID uuid;
	private UUID standUUID;
	private Main plugin = Main.getPlugin(Main.class);
	
	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		ArmorStand stand = (ArmorStand) Bukkit.getEntity(standUUID);
		
		if(stand == null) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "Your stand has been removed in the time.");
			return null;
		}
		
		if (message.equalsIgnoreCase("N/A")) {
			stand.setCustomName(message);
			stand.setCustomNameVisible(false);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Name disabled.");
		}else {
			stand.setCustomName(ChatColor.translateAlternateColorCodes('&', message));
			stand.setCustomNameVisible(true);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Name changed to: " + ChatColor.translateAlternateColorCodes('&', message) + ChatColor.GOLD + ".");
		}
		OptionsMenuInventory i = new OptionsMenuInventory();
		i.openInventory(player, stand);
		return null;
	}
	
	public void setData(UUID uuid, UUID standUUID) {
		this.uuid = uuid;
		this.standUUID = standUUID;
	}
	
	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Input name. Type " + ChatColor.WHITE + "N/A" + ChatColor.GOLD + " if you want to clear the name.");
		return output;
	}

}
