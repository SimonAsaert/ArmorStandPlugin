package tld.sima.armorstand.conversations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.events.created.ArmorstandMovedEvent;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.inventories.OptionsMenuInventory;
import tld.sima.armorstand.utils.VectorEuler;

public class RotationConv extends StringPrompt {
	
	private Main plugin = Main.getPlugin(Main.class);
	private UUID uuid;
	private boolean invType;
	private enum rotationType {
		BODY,
		HX, HY, HZ,
		TX, TY, TZ,
		LAX, LAY, LAZ,
		RAX, RAY, RAZ,
		LLX, LLY, LLZ,
		RLX, RLY, RLZ
	};
	private rotationType typeUsed;
	
	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		
		// If somehow input to function is incorrect, fallback to a main menu.
		if (typeUsed == null) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "Something somewhere went wrong");
			if(invType) {
				MainMenuInventory i = new MainMenuInventory();
				i.newInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}else {
				OptionsMenuInventory i = new OptionsMenuInventory();
				i.openInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}
			return null;
		}
		
		// Get angle
		double degrees;
		try {
			degrees = Double.parseDouble(message);
		}catch(Exception e) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "You need to put an number value here");
			if(invType) {
				MainMenuInventory i = new MainMenuInventory();
				i.newInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}else {
				OptionsMenuInventory i = new OptionsMenuInventory();
				i.openInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}
			return null;
			
		}
		
		ArmorStand stand = plugin.getStandMap().get(player.getUniqueId());
		
		// Setting armorstand rotations depending on type.
		if (typeUsed.equals(rotationType.BODY)) {
			
			if(plugin.getSmartParent().containsKey(stand.getUniqueId())) {
				ArrayList<UUID> UUIDS = plugin.getSmartParent().get(stand.getUniqueId());
				double angle = degrees - stand.getLocation().getYaw();
				
				if(!(angle == 0) && !(angle % 360 == 0)) {
					for(UUID uuid : UUIDS) {
						Entity entity = Bukkit.getEntity(uuid);
						VectorEuler euler = new VectorEuler(entity.getLocation().clone().subtract(stand.getLocation().clone()));
						euler.addRadian(angle * (Math.PI/180));
						Location newloc = stand.getLocation().clone().add(euler.getX(), 0, euler.getZ());
						newloc.setYaw(stand.getLocation().getYaw() + (float) angle);
						ArmorstandMovedEvent ame = new ArmorstandMovedEvent(entity, newloc, true);
						plugin.getServer().getPluginManager().callEvent(ame);
						if(!ame.isCancelled()) {
							entity.teleport(newloc);
				}	}	}
				
			}else if(plugin.getParentMap().containsKey(stand.getUniqueId())) {
				int radius = plugin.getParentMap().get(stand.getUniqueId());
				List<Entity> entities = stand.getNearbyEntities(radius, radius, radius);
				double angle = degrees - stand.getLocation().getYaw();
				
				if(!(angle == 0) && !(angle % 360 == 0)) {
					for(Entity entity : entities) {
						if(entity instanceof ArmorStand) {
							Location vector = entity.getLocation().clone().subtract(stand.getLocation().clone());
							VectorEuler euler = new VectorEuler(vector);
							euler.addRadian(angle*(Math.PI/180));
							vector.setX(euler.getX());
							vector.setZ(euler.getZ());
							Location newloc = stand.getLocation().clone().add(vector);
							newloc.setYaw(entity.getLocation().getYaw() + (float)angle);
							ArmorstandMovedEvent ame = new ArmorstandMovedEvent(entity, newloc, true);
							plugin.getServer().getPluginManager().callEvent(ame);
							if(!ame.isCancelled()) {
								entity.teleport(newloc);
			}	}	}	}	}
			
			Location loc = stand.getLocation().clone();
			Location newLoc = loc.clone();
			newLoc.setYaw((float) degrees);
			
			ArmorstandMovedEvent ame = new ArmorstandMovedEvent(stand, newLoc, true);
			plugin.getServer().getPluginManager().callEvent(ame);
			
			if(!ame.isCancelled()) {
				stand.teleport(newLoc);
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Body angle changed to: " + ChatColor.WHITE + degrees);
			}
			
		}else {
			int degreesD = (int)degrees;
			degrees = degrees*Math.PI/180;
			
			switch(typeUsed) {
				case HX:
					stand.setHeadPose(stand.getHeadPose().setX(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Head X angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case HY:
					stand.setHeadPose(stand.getHeadPose().setX(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Head X angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case HZ:
					stand.setHeadPose(stand.getHeadPose().setZ(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Head Z angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case TX:
					stand.setBodyPose(stand.getBodyPose().setX(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Chest X angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case TY:
					stand.setBodyPose(stand.getBodyPose().setY(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Chest Y angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case TZ:
					stand.setBodyPose(stand.getBodyPose().setZ(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Chest Z angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case LAX:
					stand.setLeftArmPose(stand.getLeftArmPose().setX(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Arm X angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case LAY:
					stand.setLeftArmPose(stand.getLeftArmPose().setY(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Arm Y angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case LAZ:
					stand.setLeftArmPose(stand.getLeftArmPose().setZ(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Arm Z angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case RAX:
					stand.setRightArmPose(stand.getRightArmPose().setX(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Arm X angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case RAY:
					stand.setRightArmPose(stand.getRightArmPose().setY(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Arm Y angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case RAZ:
					stand.setRightArmPose(stand.getRightArmPose().setZ(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Arm Z angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case LLX:
					stand.setLeftLegPose(stand.getLeftLegPose().setX(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Leg X angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case LLY:
					stand.setLeftLegPose(stand.getLeftLegPose().setY(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Leg Y angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case LLZ:
					stand.setLeftLegPose(stand.getLeftLegPose().setZ(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Leg Z angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case RLX:
					stand.setRightLegPose(stand.getRightLegPose().setX(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Leg X angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case RLY:
					stand.setRightLegPose(stand.getRightLegPose().setY(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Leg Y angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				case RLZ:
					stand.setRightLegPose(stand.getRightLegPose().setZ(degrees));
					con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Leg Z angle changed to: " + ChatColor.WHITE + degreesD);
					break;
				default:
					con.getForWhom().sendRawMessage(ChatColor.RED + "Unable to find rotation type... Something went wrong");
					break;
		}	}

		if(invType) {
			ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, stand);
			plugin.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				MainMenuInventory i = new MainMenuInventory();
				i.newInventory(player, stand);
			}
		}else {
			OptionsMenuInventory i = new OptionsMenuInventory();
			i.openInventory(player, plugin.getStandMap().get(player.getUniqueId()));
		}
		return null;
	}

	public void setData(UUID uuid, boolean invType, String type) {
		this.uuid = uuid;
		this.invType = invType;
		typeUsed = rotationType.valueOf(type);
	}
	
	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Put in an angle in degrees, like: " + ChatColor.WHITE + "90");
		return output;
	}
}
