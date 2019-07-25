package tld.sima.armorstand.conversations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.events.created.ArmorstandMovedEvent;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.inventories.OptionsMenuInventory;

public class MovementConv extends StringPrompt{
	
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
		
		if(plugin.getSmartParent().containsKey(stand.getUniqueId())) {
			ArrayList<UUID> stands = plugin.getSmartParent().get(stand.getUniqueId());
			for(UUID uuid : stands) {
				Entity entity = Bukkit.getEntity(uuid);
				Location oldLoc = entity.getLocation().clone();
				Location newLoc = oldLoc.clone().add(movement[0], movement[1], movement[2]);
				ArmorstandMovedEvent ame = new ArmorstandMovedEvent(entity, newLoc, false);
				Bukkit.getServer().getPluginManager().callEvent(ame);
				if(!ame.isCancelled()) {
					entity.teleport(newLoc);
				}
			}
			
		}else if (plugin.getParentMap().containsKey(stand.getUniqueId())) {
			int radius = plugin.getParentMap().get(stand.getUniqueId());
			List<Entity> entities = stand.getNearbyEntities(radius, radius, radius);
			for (Entity entity : entities) {
				if (entity instanceof ArmorStand) {
					Location loc = entity.getLocation().clone();
					Location newLoc = loc.clone().add(movement[0], movement[1], movement[2]);
					ArmorstandMovedEvent ame = new ArmorstandMovedEvent(entity, newLoc, false);
					plugin.getServer().getPluginManager().callEvent(ame);
					if(!ame.isCancelled()) {
						entity.teleport(newLoc);
					}
				}
			}
		}
		Location oldLoc = stand.getLocation().clone();
		Location newLoc = oldLoc.clone().add(movement[0], movement[1], movement[2]);
		
		ArmorstandMovedEvent ame = new ArmorstandMovedEvent(stand, newLoc, false);
		plugin.getServer().getPluginManager().callEvent(ame);
		
		if(!ame.isCancelled()) {
			stand.teleport(newLoc);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Position set!");
		}
		openInventory(player, stand);
		
		return null;
	}
	
	public void setData(UUID uuid, UUID standUUID, boolean invType) {
		this.uuid = uuid;
		this.standUUID = standUUID;
		this.invType = invType;
	}
	
	private void openInventory(Player player, ArmorStand stand) {
		if (invType) {
			ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, stand);
			plugin.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				MainMenuInventory i = new MainMenuInventory();
				i.newInventory(player, stand);
			}
		}else {
			OptionsMenuInventory i = new OptionsMenuInventory();
			i.openInventory(player, stand);
		}
	}

	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Type relative input in format: " + ChatColor.WHITE + "0 1 0" + ChatColor.GOLD + ".");
		return output;
	}

}
