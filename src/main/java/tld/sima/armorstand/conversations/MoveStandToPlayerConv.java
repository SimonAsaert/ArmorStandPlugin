package tld.sima.armorstand.conversations;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.inventories.OptionsMenuInventory;
import tld.sima.armorstand.utils.RotationClass;
import tld.sima.armorstand.utils.Utils;

public class MoveStandToPlayerConv extends StringPrompt {
	private UUID uuid;
	private UUID standUUID;
	private boolean mainInventory;
	private final Main plugin = Main.getPlugin(Main.class);

	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		ArmorStand stand = (ArmorStand) Bukkit.getEntity(standUUID);
		if(stand == null) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "Unable to find stand!");
			return null;
		}
		
		if (!message.equalsIgnoreCase("cancel") && player != null) {
			Location finalLocation = player.getLocation();
			Location currentStandLocation = stand.getLocation();
			Vector movement = new Vector();
			movement.setX(finalLocation.getX()-currentStandLocation.getX());
			movement.setY(finalLocation.getY()-currentStandLocation.getY());
			movement.setZ(finalLocation.getZ()-currentStandLocation.getZ());
			
			ArrayList<UUID> childList = (ArrayList<UUID>)Utils.collectAllChildStands(stand.getUniqueId(), new ArrayList<UUID>());
			Utils.moveAllChildStands(childList, movement, finalLocation.getWorld().getUID());
			
			RotationClass rotation = new RotationClass();
			rotation.InsertionDegrees(stand.getUniqueId(), (double)player.getLocation().getYaw() - (double)currentStandLocation.getYaw());
		}else {
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Stand left in place");
		}
		
		openInventory(player, stand);
		return null;
	}
	
	private void openInventory(Player player, ArmorStand stand) {
		if (mainInventory) {
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
	
	public void setData(UUID uuid, UUID standUUID, boolean isMainInventory) {
		this.mainInventory = isMainInventory;
		this.uuid = uuid;
		this.standUUID = standUUID;
	}
	
	public String getPromptText(ConversationContext arg0) {
		return (ChatColor.GOLD + "Once you are in the correct position, type anything into chat. \nTo abandon, type " + ChatColor.WHITE + "cancel" + ChatColor.GOLD + " instead");
	}
}
