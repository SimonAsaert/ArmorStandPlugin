package tld.sima.armorstand.events.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.conversations.MoveStandToPlayerConv;
import tld.sima.armorstand.conversations.MovementConv;
import tld.sima.armorstand.conversations.RadiusConv;
import tld.sima.armorstand.conversations.RotationConv;
import tld.sima.armorstand.events.created.ArmorstandRemovedEvent;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.inventories.OptionsMenuInventory;
import tld.sima.armorstand.inventories.ParentMenuInventory;

public class MainMenuItemEvents {
	
	public static Main plugin = Main.getPlugin(Main.class);
	
	public static boolean parseItem(String itemName, Player player, ArmorStand stand) {
		if (itemName.contains("Move Stand with Player")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			MoveStandToPlayerConv conversation = new MoveStandToPlayerConv();
			conversation.setData(player.getUniqueId());
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
			return true;
		}else if (itemName.contains("Animations") && !plugin.AnimationActive) {
			if (!plugin.AnimationActive) {
				player.sendMessage(ChatColor.RED + "No animations plugin active!");
			}
			if(Bukkit.getServer().getPluginManager().getPlugin("ArmorstandAnimationPlugin") != null) {
				plugin.AnimationActive = true;
				return true;
			}
			return true;
		}else if (itemName.contains("Go to parent stand")) {
			ArmorStand parent = null;
			for(UUID uuid : plugin.getSmartParent().keySet()) {
				if(plugin.getSmartParent().get(uuid).contains(stand.getUniqueId())){
					parent = (ArmorStand)Bukkit.getEntity(uuid);
				}
			}
			if(parent == null) {
				for(Entity entity: stand.getNearbyEntities(8, 8, 8)) {
					if(plugin.getParentMap().containsKey(entity.getUniqueId())) {
						int distance = plugin.getParentMap().get(entity.getUniqueId());
						if(Math.max(Math.abs(stand.getLocation().getX() - entity.getLocation().getX()), 
								Math.abs(stand.getLocation().getZ() - entity.getLocation().getZ())) <= distance) {
							parent = (ArmorStand) entity;
							break;
						}
					}
				}
			}
			if(parent == null) {
				player.sendMessage(ChatColor.RED + "Unable to find parent stand!");
				return true;
			}
			ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, parent);
			plugin.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				MainMenuInventory j = new MainMenuInventory();
				j.newInventory(player, parent);
			}
			plugin.setPairedStand(player.getUniqueId(), parent);
			return true;
		}else if (itemName.contains("Options")) {
			OptionsMenuInventory i = new OptionsMenuInventory();
			i.openInventory(player, stand);
			return true;
		}else if (itemName.contains("Delete Stand")) {
			if (plugin.getParentMap().containsKey(stand.getUniqueId())){
				plugin.getParentMap().remove(stand.getUniqueId());
			}
			UUID standUUID = stand.getUniqueId();
			ArmorstandRemovedEvent are = new ArmorstandRemovedEvent(standUUID);
			plugin.getServer().getPluginManager().callEvent(are);
			stand.remove();
			player.closeInventory();
			player.sendMessage(ChatColor.GOLD + "Removed stand");
			return true;
		}else if (itemName.contains("Clone Stand")) {
			if(player.hasPermission("armorstand.clone")) {
				if (!player.getInventory().contains(plugin.getCloneTool())) {
					player.getInventory().addItem(plugin.getCloneTool());
				}
				plugin.setClonedStand(player.getUniqueId(), stand);
				player.closeInventory();
			}else {
				player.sendMessage(ChatColor.WHITE + "You do not have armorstand.clone permision");
			}
			return true;
		}else if (itemName.contains("Position")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			MovementConv conversation = new MovementConv();
			conversation.setData(player.getUniqueId(), true);
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
			return true;
		}else if(itemName.contains("otation")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			RotationConv converstaion = new RotationConv();
			if (itemName.contains("Rotation")) {
				converstaion.setData(player.getUniqueId(), true, "BODY");
			}else if (itemName.contains("Change Head x-rotation")) {
				if (stand.hasMetadata("HeadLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "HX");
			}else if (itemName.contains("Change Head y-rotation")) {
				if (stand.hasMetadata("HeadLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "HY");
			}else if (itemName.contains("Change Head z-rotation")) {
				if (stand.hasMetadata("HeadLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "HZ");
			}else if (itemName.contains("Change Torso x-rotation")) {
				if (stand.hasMetadata("TorsoLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "TX");
			}else if (itemName.contains("Change Torso y-rotation")) {
				if (stand.hasMetadata("TorsoLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "TY");
			}else if (itemName.contains("Change Torso z-rotation")) {
				if (stand.hasMetadata("TorsoLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "TZ");
			}else if (itemName.contains("Change Left Arm's x-rotation")) {
				if (stand.hasMetadata("LeftArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "LAX");
				return true;
			}else if (itemName.contains("Change Left Arm's y-rotation")) {
				if (stand.hasMetadata("LeftArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "LAY");
			}else if (itemName.contains("Change Left Arm's z-rotation")) {
				if (stand.hasMetadata("LeftArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "LAZ");
			}else if (itemName.contains("Change Right Arm's x-rotation")) {
				if (stand.hasMetadata("RightArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "RAX");
			}else if (itemName.contains("Change Right Arm's y-rotation")) {
				if (stand.hasMetadata("RightArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "RAY");
			}else if (itemName.contains("Change Right Arm's z-rotation")) {
				if (stand.hasMetadata("RightArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "RAZ");
			}else if (itemName.contains("Change Left Leg's x-rotation")) {
				if (stand.hasMetadata("LeftLegLock")) {
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "LLX");
			}else if (itemName.contains("Change Left Leg's y-rotation")) {
				if (stand.hasMetadata("LeftLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "LLY");
			}else if (itemName.contains("Change Left Leg's z-rotation")) {
				if (stand.hasMetadata("LeftLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "LLZ");
			}else if (itemName.contains("Change Right Leg's x-rotation")) {
				if (stand.hasMetadata("RightLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "RLX");
			}else if (itemName.contains("Change Right Leg's y-rotation")) {
				if (stand.hasMetadata("RightLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				converstaion.setData(player.getUniqueId(), true, "RLY");
			}else if (itemName.contains("Change Right Leg's z-rotation")) {
				if (stand.hasMetadata("RightLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return true;
				}
				player.closeInventory();
				converstaion.setData(player.getUniqueId(), true, "RLZ");
			}else {
				player.sendMessage("What did you click?");
				return true;
			}
			Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
			return true;
		}else if (itemName.contains("Set Radius")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			RadiusConv conversation = new RadiusConv();
			conversation.setData(player.getUniqueId(), true);
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
			return true;
		}else if (itemName.contains("Parent")) {
			ParentMenuInventory pmi = new ParentMenuInventory();
			pmi.openInventory(player, stand);
		}
		return false;
	}
}
