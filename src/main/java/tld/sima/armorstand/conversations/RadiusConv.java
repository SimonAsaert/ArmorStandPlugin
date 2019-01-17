package tld.sima.armorstand.conversations;

import java.util.UUID;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.inventories.OptionsMenuInventory;

public class RadiusConv extends StringPrompt {
	
	private Main plugin = Main.getPlugin(Main.class);
	private UUID uuid;
	private boolean invType;

	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		ArmorStand stand = plugin.getStandMap().get(uuid);
		
		int radius;
		try {
			radius = Integer.parseInt(message);
			plugin.getParentMap().put(stand.getUniqueId(), radius);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Radius set: " + ChatColor.WHITE + message);
		}catch (Exception e) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "Message not a number");
		}
		
		openInventory(player, stand);
		con.getForWhom().sendRawMessage(ChatColor.GOLD + "Position set!");
		
		return null;
	}
	
	public void setData(UUID uuid, boolean invType) {
		this.uuid = uuid;
		this.invType = invType;
	}
	
	private void openInventory(Player player, ArmorStand stand) {
		if (invType) {
			MainMenuInventory i = new MainMenuInventory();
			i.newInventory(player, stand);
		}else {
			OptionsMenuInventory i = new OptionsMenuInventory();
			i.openInventory(player, stand);
		}
	}

	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Type radius. eg: " + ChatColor.WHITE + "1" + ChatColor.GOLD + ".");
		return output;
	}

}
