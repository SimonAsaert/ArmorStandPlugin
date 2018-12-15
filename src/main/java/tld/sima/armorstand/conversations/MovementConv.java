package tld.sima.armorstand.conversations;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.events.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.mainMenuInventory;
import tld.sima.armorstand.inventories.optionsMenuInventory;

public class MovementConv extends StringPrompt{
	
	private Main plugin = Main.getPlugin(Main.class);
	private UUID uuid;
	private boolean invType;

	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		ArmorStand stand = plugin.getStandMap().get(uuid);
		String delims = " ";
		String[] tokens = message.split(delims);
		if (tokens.length != 3) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "Input incorrect number of variables.");
			openInventory(player, stand);
			return null;
		}
		Double[] movement = new Double[3];
		for (int i = 0 ; i < tokens.length ; i++) {
			try {
				movement[i] = Double.parseDouble(tokens[i]);
			}catch(Exception e) {
				con.getForWhom().sendRawMessage(ChatColor.RED + "Variable " + ChatColor.WHITE + (i+1) + ChatColor.GOLD + " is not a number!");
				openInventory(player, stand);
				return null;
			}
		}
		if (plugin.getParentMap().containsKey(stand.getUniqueId())) {
			int radius = plugin.getParentMap().get(stand.getUniqueId());
			List<Entity> entities = stand.getNearbyEntities(radius, radius, radius);
			for (Entity entity : entities) {
				if (entity instanceof ArmorStand) {
					Location loc = entity.getLocation().clone();
					loc.add(movement[0], movement[1], movement[2]);
					entity.teleport(loc);
				}
			}
		}
		Location loc = stand.getLocation().clone();
		loc.add(movement[0], movement[1], movement[2]);
		stand.teleport(loc);
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
			ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, stand);
			plugin.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				mainMenuInventory i = new mainMenuInventory();
				i.newInventory(player, stand);
			}
		}else {
			optionsMenuInventory i = new optionsMenuInventory();
			i.openInventory(player, stand);
		}
	}

	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Type relative input in format: " + ChatColor.WHITE + "0 1 0" + ChatColor.GOLD + ".");
		return output;
	}

}
