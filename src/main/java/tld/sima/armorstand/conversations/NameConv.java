package tld.sima.armorstand.conversations;

import java.util.UUID;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.inventories.optionsMenuInventory;

public class NameConv extends StringPrompt {
	private UUID uuid;
	private Main plugin = Main.getPlugin(Main.class);
	
	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		ArmorStand stand = plugin.getStandMap().get(uuid);
		
		if (message.equalsIgnoreCase("N/A")) {
			stand.setCustomName(message);
			stand.setCustomNameVisible(false);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Name disabled.");
		}else {
			stand.setCustomName(ChatColor.translateAlternateColorCodes('&', message));
			stand.setCustomNameVisible(true);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Name changed to: " + ChatColor.translateAlternateColorCodes('&', message) + ChatColor.GOLD + ".");
		}
		optionsMenuInventory i = new optionsMenuInventory();
		i.openInventory(player, stand);
		return null;
	}
	
	public void setData(UUID uuid) {
		this.uuid = uuid;
	}
	
	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Input name. Type " + ChatColor.WHITE + "N/A" + ChatColor.GOLD + " if you want to clear the name.");
		return output;
	}

}
