package tld.sima.armorstand.events.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.conversations.RadiusConv;
import tld.sima.armorstand.inventories.MainMenuInventories;
import tld.sima.armorstand.inventories.ParentMenuInventory;
import tld.sima.armorstand.inventories.items.ItemHub;

public class ParentMenuItemEvents {
	public static Main plugin = Main.getPlugin(Main.class);
	
	public static void parseItem(ItemStack item, Player player, ArmorStand stand) {
		UUID standUUID = stand.getUniqueId();
		ItemHub hub = plugin.getItemHub();
		if(item.getItemMeta().getDisplayName().contains("Set Radius")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			RadiusConv conversation = new RadiusConv();
			conversation.setData(player.getUniqueId(), true);
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			if (plugin.getConv().containsKey(player.getUniqueId())) {
				plugin.getConv().get(player.getUniqueId()).abandon();
			}
			plugin.getConv().put(player.getUniqueId(), conv);
			return;
			
		}else if (item.isSimilar(hub.getParentMenuItems().getDefaultParent())) {
			if(plugin.getSmartParent().containsKey(standUUID)) {
				plugin.getSmartParent().remove(standUUID);
				plugin.getParentMap().put(standUUID, 1);
				ParentMenuInventory pmi = new ParentMenuInventory();
				pmi.openInventory(player, stand);
				return;
				
			}else if (plugin.getParentMap().containsKey(standUUID)) {
				PotionEffectType type = PotionEffectType.GLOWING;
				PotionEffect potion = new PotionEffect(type, 200, 1);
				int radius = plugin.getParentMap().get(standUUID);
				List<Entity> entities = stand.getNearbyEntities(radius, radius, radius);
				for(Entity entity: entities) {
					if(entity instanceof ArmorStand) {
						((ArmorStand) entity).addPotionEffect(potion);
					}
				}
				return;
				
			}else {
				plugin.getSmartParent().remove(standUUID);
				plugin.getParentMap().put(standUUID, 1);
				ParentMenuInventory pmi = new ParentMenuInventory();
				pmi.openInventory(player, stand);
				return;
				
			}
			
		}else if(item.isSimilar(hub.getParentMenuItems().getTool())){
			if(!player.getInventory().contains(plugin.getSmartParentTool())) {
				player.getInventory().addItem(plugin.getSmartParentTool());
			}
			player.sendMessage(ChatColor.GOLD + "Added tool to inventory");
		}else if (item.isSimilar(hub.getParentMenuItems().getStop())) {
			plugin.getSmartParent().remove(standUUID);
			plugin.getParentMap().remove(standUUID);
			ParentMenuInventory pmi = new ParentMenuInventory();
			pmi.openInventory(player, stand);
		}else if (item.isSimilar(hub.getParentMenuItems().getSmartParent())) {
			if(plugin.getSmartParent().containsKey(standUUID)) {
				PotionEffectType type = PotionEffectType.GLOWING;
				PotionEffect potion = new PotionEffect(type, 200, 1);
				ArrayList<UUID> uuids= plugin.getSmartParent().get(standUUID);
				for(UUID uuid : uuids) {
					Entity entity = Bukkit.getEntity(uuid);
					if(entity instanceof ArmorStand) {
						((ArmorStand) entity).addPotionEffect(potion);
					}
				}
			}else if(plugin.getParentMap().containsKey(standUUID)){
				int radius = plugin.getParentMap().get(standUUID);
				List<Entity> entities = stand.getNearbyEntities(radius, radius, radius);
				ArrayList<UUID> uuids = new ArrayList<UUID>();
				for(Entity entity: entities) {
					uuids.add(entity.getUniqueId());
				}
				plugin.getSmartParent().put(standUUID, uuids);
				ParentMenuInventory pmi = new ParentMenuInventory();
				pmi.openInventory(player, stand);
			}else {
				plugin.getSmartParent().put(standUUID, new ArrayList<UUID>());
				ParentMenuInventory pmi = new ParentMenuInventory();
				pmi.openInventory(player, stand);
			}
			
		}else if (item.isSimilar(hub.getParentMenuItems().getBack())) {
			MainMenuInventories mmi = new MainMenuInventories();
			mmi.newInventory(player, stand);
			
		}
	}
	
}
