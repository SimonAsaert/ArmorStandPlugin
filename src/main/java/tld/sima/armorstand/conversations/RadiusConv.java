package tld.sima.armorstand.conversations;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.inventories.OptionsMenuInventory;

public class RadiusConv extends StringPrompt {
	
	private Main plugin = Main.getPlugin(Main.class);
	private UUID uuid;
	private UUID standUUID;
	private boolean invType;

	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		ArmorStand stand = (ArmorStand) Bukkit.getEntity(standUUID);
		
		if(stand == null) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "The stand has disappeared!");
			return null;
		}
		
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
	
	public void setData(UUID uuid, UUID standUUID, boolean invType) {
		this.uuid = uuid;
		this.standUUID = standUUID;
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
