package tld.sima.armorstand.conversations;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;

public class MoveStandToPlayerConv extends StringPrompt {
	private UUID uuid;
	private Main plugin = Main.getPlugin(Main.class);

	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		if (!message.equalsIgnoreCase("cancel")) {
			ArmorStand stand = plugin.getStandMap().get(uuid);
			
			Location loc = player.getLocation();
			stand.teleport(loc);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Stand moved");
		}else {
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Stand left in place");
		}
		return null;
	}
	
	public void setData(UUID uuid) {
		this.uuid = uuid;
	}
	
	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Once you are in the correct position, type anything into chat. \nTo abandon, type " + ChatColor.WHITE + "cancel" + ChatColor.GOLD + " instead");
		return output;
	}

}