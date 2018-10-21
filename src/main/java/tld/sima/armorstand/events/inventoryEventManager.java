package tld.sima.armorstand.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.conversations.MoveStandToPlayerConv;
import tld.sima.armorstand.conversations.MovementConv;
import tld.sima.armorstand.conversations.NameConv;
import tld.sima.armorstand.conversations.RadiusConv;
import tld.sima.armorstand.conversations.RotationConv;
import tld.sima.armorstand.inventories.mainMenuInventory;
import tld.sima.armorstand.inventories.optionsMenuInventory;

public class inventoryEventManager implements Listener {
	
	Main plugin = Main.getPlugin(Main.class);
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (player == null) {
			return;
		}
		Inventory open = event.getClickedInventory();
		if (open == null) {
			return;
		}
		ItemStack item = event.getCurrentItem();
		ArmorStand stand = plugin.getStandMap().get(player.getUniqueId());
		if (stand == null) {
			return;
		}
		String prefixMain = (ChatColor.DARK_BLUE + "Armorstand GUI");
		String prefixOptions = (ChatColor.DARK_BLUE + "Armorstand GUI Options");
		
		if (open.getName().equals(prefixMain)) {
			event.setCancelled(true);

			if (event.getAction().equals(InventoryAction.PICKUP_HALF)) {
				if (item == null) {
//					player.sendMessage(ChatColor.WHITE + "Clicked something null!");
					return;
				}else if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
//					player.sendMessage(ChatColor.WHITE + "Clicked something without meta!");
					return;
				}
				String itemName = item.getItemMeta().getDisplayName();
				if (itemName.contains("Head") && itemName.contains("rotation")) {
					StringBuilder string = new StringBuilder();
					string.append(ChatColor.GOLD + "Head ");
					if (stand.hasMetadata("HeadLock")) {
						stand.removeMetadata("HeadLock", plugin);
						string.append(ChatColor.GREEN + "unlocked");
					}else {
						stand.setMetadata("HeadLock", new FixedMetadataValue(plugin, true));
						string.append(ChatColor.RED + "locked");
					}
					player.sendMessage(string.toString());
					return;
				}else if (itemName.contains("Torso") && itemName.contains("rotation")) {
					StringBuilder string = new StringBuilder();
					string.append(ChatColor.GOLD + "Torso ");
					if (stand.hasMetadata("TorsoLock")) {
						stand.removeMetadata("TorsoLock", plugin);
						string.append(ChatColor.GREEN + "unlocked");
					}else {
						stand.setMetadata("TorsoLock", new FixedMetadataValue(plugin, true));
						string.append(ChatColor.RED + "locked");
					}
					player.sendMessage(string.toString());
					return;
				}else if (itemName.contains("Left Arm's") && itemName.contains("rotation")) {
					StringBuilder string = new StringBuilder();
					string.append(ChatColor.GOLD + "Left Arm ");
					if (stand.hasMetadata("LeftArmLock")) {
						stand.removeMetadata("LeftArmLock", plugin);
						string.append(ChatColor.GREEN + "unlocked");
					}else {
						stand.setMetadata("LeftArmLock", new FixedMetadataValue(plugin, true));
						string.append(ChatColor.RED + "locked");
					}
					player.sendMessage(string.toString());
					return;
				}else if (itemName.contains("Right Arm's") && itemName.contains("rotation")) {
					StringBuilder string = new StringBuilder();
					string.append(ChatColor.GOLD + "Right Arm ");
					if (stand.hasMetadata("RightArmLock")) {
						stand.removeMetadata("RightArmLock", plugin);
						string.append(ChatColor.GREEN + "unlocked");
					}else {
						stand.setMetadata("RightArmLock", new FixedMetadataValue(plugin, true));
						string.append(ChatColor.RED + "locked");
					}
					player.sendMessage(string.toString());
					return;
				}else if (itemName.contains("Left Leg's") && itemName.contains("rotation")) {
					StringBuilder string = new StringBuilder();
					string.append(ChatColor.GOLD + "Left Leg ");
					if (stand.hasMetadata("LeftLegLock")) {
						stand.removeMetadata("LeftLegLock", plugin);
						string.append(ChatColor.GREEN + "unlocked");
					}else {
						stand.setMetadata("LeftLegLock", new FixedMetadataValue(plugin, true));
						string.append(ChatColor.RED + "locked");
					}
					player.sendMessage(string.toString());
					return;
				}else if (itemName.contains("Right Leg's") && itemName.contains("rotation")) {
					StringBuilder string = new StringBuilder();
					string.append(ChatColor.GOLD + "Right Leg ");
					if (stand.hasMetadata("RightLegLock")) {
						stand.removeMetadata("RightLegLock", plugin);
						string.append(ChatColor.GREEN + "unlocked");
					}else {
						stand.setMetadata("RightLegLock", new FixedMetadataValue(plugin, true));
						string.append(ChatColor.RED + "locked");
					}
					player.sendMessage(string.toString());
					return;
				}
			}

			if (item == null) {
//				player.sendMessage(ChatColor.WHITE + "Clicked something null!");
				return;
			}else if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
//				player.sendMessage(ChatColor.WHITE + "Clicked something without meta!");
				return;
			}
			
			String itemName = item.getItemMeta().getDisplayName();
			
			if (itemName.contains("Move Stand with Player")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				MoveStandToPlayerConv conversation = new MoveStandToPlayerConv();
				conversation.setData(player.getUniqueId());
				Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
			}else if (itemName.contains("Animations") && !plugin.AnimationActive) {
				player.sendMessage(ChatColor.RED + "No animations plugin active!");
			}else if (itemName.contains("Options")) {
				optionsMenuInventory i = new optionsMenuInventory();
				i.openInventory(player, stand);
			}else if (itemName.contains("Delete Stand")) {
				if (plugin.getParentMap().containsKey(stand.getUniqueId())){
					plugin.getParentMap().remove(stand.getUniqueId());
				}
				stand.remove();
				player.closeInventory();
				player.sendMessage(ChatColor.GOLD + "Removed stand");
			}else if (itemName.contains("Clone Stand")) {
				if(player.hasPermission("armorstand.clone")) {
					ItemStack tool = new ItemStack(Material.STICK);
					ItemMeta toolMeta = tool.getItemMeta();
					toolMeta.setDisplayName(ChatColor.GREEN + "Clone tool");
					tool.setItemMeta(toolMeta);
					if (!player.getInventory().contains(tool)) {
						player.getInventory().addItem(tool);
					}
					plugin.getCloneMap().put(player.getUniqueId(), stand);
					player.closeInventory();
				}
			}else if (itemName.contains("Position")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				MovementConv conversation = new MovementConv();
				conversation.setData(player.getUniqueId(), true);
				Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Rotation")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "BODY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Head x-rotation")) {
				if (stand.hasMetadata("HeadLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "HX");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);

			}else if (itemName.contains("Change Head y-rotation")) {
				if (stand.hasMetadata("HeadLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "HY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Head z-rotation")) {
				if (stand.hasMetadata("HeadLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "HZ");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);

			}else if (itemName.contains("Change Torso x-rotation")) {
				if (stand.hasMetadata("TorsoLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "TX");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);

			}else if (itemName.contains("Change Torso y-rotation")) {
				if (stand.hasMetadata("TorsoLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "TY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);

			}else if (itemName.contains("Change Torso z-rotation")) {
				if (stand.hasMetadata("TorsoLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "TZ");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);

			}else if (itemName.contains("Change Left Arm's x-rotation")) {
				if (stand.hasMetadata("LeftArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "LAX");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Left Arm's y-rotation")) {
				if (stand.hasMetadata("LeftArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "LAY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Left Arm's z-rotation")) {
				if (stand.hasMetadata("LeftArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "LAZ");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);

			}else if (itemName.contains("Change Right Arm's x-rotation")) {
				if (stand.hasMetadata("RightArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "RAX");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Right Arm's y-rotation")) {
				if (stand.hasMetadata("RightArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "RAY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Right Arm's z-rotation")) {
				if (stand.hasMetadata("RightArmLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "RAZ");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);

			}else if (itemName.contains("Change Left Leg's x-rotation")) {
				if (stand.hasMetadata("LeftLegLock")) {
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "LLX");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Left Leg's y-rotation")) {
				if (stand.hasMetadata("LeftLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "LLY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Left Leg's z-rotation")) {
				if (stand.hasMetadata("LeftLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "LLZ");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Right Leg's x-rotation")) {
				if (stand.hasMetadata("RightLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "RLX");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Right Leg's y-rotation")) {
				if (stand.hasMetadata("RightLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "RLY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Change Right Leg's z-rotation")) {
				if (stand.hasMetadata("RightLegLock")) {
					player.sendMessage(ChatColor.RED + "Limb Locked");
					return;
				}
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), true, "RLZ");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Set Radius")) {
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
				
			}else {
			
				mainMenuInventory i = new mainMenuInventory();
				if (itemName.contains("Change Head")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getHelmet().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getHelmet());
					}
					stand.getEquipment().setHelmet(setter);
					player.sendMessage(ChatColor.GOLD + "Helmet set.");
					event.getCursor().setAmount(0);
					i.newInventory(player, stand);
				}else if (itemName.contains("Change Chest")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getChestplate().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getChestplate());
					}
					stand.getEquipment().setChestplate(setter);
					player.sendMessage(ChatColor.GOLD + "Chestplate set.");
					event.getCursor().setAmount(0);
					i.newInventory(player, stand);
				}else if (itemName.contains("Change Legs")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getLeggings().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getLeggings());
					}
					stand.getEquipment().setLeggings(setter);
					player.sendMessage(ChatColor.GOLD + "Chestplate set.");
					event.getCursor().setAmount(0);
					i.newInventory(player, stand);
				}else if (itemName.contains("Change Feet")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getBoots().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getBoots());
					}
					stand.getEquipment().setBoots(setter);
					player.sendMessage(ChatColor.GOLD + "Boots set.");
					event.getCursor().setAmount(0);
					i.newInventory(player, stand);
				}else if (itemName.contains("Change Left Hand")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getItemInOffHand().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getItemInOffHand());
					}
					stand.getEquipment().setItemInOffHand(setter);
					player.sendMessage(ChatColor.GOLD + "Off-hand set.");
					event.getCursor().setAmount(0);
					i.newInventory(player, stand);
				}else if (itemName.contains("Change Right Hand")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getItemInMainHand().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getItemInMainHand());
					}
					stand.getEquipment().setItemInMainHand(setter);
					player.sendMessage(ChatColor.GOLD + "Main-hand set.");
					event.getCursor().setAmount(0);
					i.newInventory(player, stand);
				}else if (itemName.contains("Set Parent")) {
					UUID uuid = stand.getUniqueId();
					if (!plugin.getParentMap().containsKey(uuid)) {
						int radius = 1;
						plugin.getParentMap().put(uuid, radius);
						
						stand.setVisible(false);
						stand.setCustomNameVisible(false);
						stand.setCustomName("N/A");
	
						player.getInventory().addItem(stand.getEquipment().getHelmet());
						player.getInventory().addItem(stand.getEquipment().getChestplate());
						player.getInventory().addItem(stand.getEquipment().getLeggings());
						player.getInventory().addItem(stand.getEquipment().getBoots());
						player.getInventory().addItem(stand.getEquipment().getItemInMainHand());
						player.getInventory().addItem(stand.getEquipment().getItemInOffHand());
	
						stand.getEquipment().setHelmet(new ItemStack(Material.AIR));
						stand.getEquipment().setChestplate(new ItemStack(Material.AIR));
						stand.getEquipment().setLeggings(new ItemStack(Material.AIR));
						stand.getEquipment().setBoots(new ItemStack(Material.AIR));
						stand.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
						stand.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
						
						stand.setGravity(false);
						stand.setBasePlate(false);
						stand.setArms(false);
						stand.setSmall(false);
					}else {
						plugin.getParentMap().remove(uuid);
						stand.setVisible(true);
					}
					i.newInventory(player, stand);
				}
			}
		}else if (open.getName().equals(prefixOptions)) {
			event.setCancelled(true);

			if (item == null) {
//				player.sendMessage(ChatColor.WHITE + "Clicked something null!");
				return;
			}else if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
//				player.sendMessage(ChatColor.WHITE + "Clicked something without meta!");
				return;
			}
			
			String itemName = item.getItemMeta().getDisplayName();
			
			if (itemName.contains("Back")) {
				mainMenuInventory i = new mainMenuInventory();
				i.newInventory(player, stand);
				return;
			}else if (itemName.contains("Delete Stand")) {
				if (plugin.getParentMap().containsKey(stand.getUniqueId())){
					plugin.getParentMap().remove(stand.getUniqueId());
				}
				player.closeInventory();
				stand.remove();
				player.sendMessage(ChatColor.GOLD + "Stand removed");
			}else if (itemName.contains("Position")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				MovementConv conversation = new MovementConv();
				conversation.setData(player.getUniqueId(), false);
				Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
			}else if (itemName.contains("Rotation")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RotationConv converstaion = new RotationConv();
				converstaion.setData(player.getUniqueId(), false, "BODY");
				Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
			}else if (itemName.contains("Name")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				NameConv conversation = new NameConv();
				conversation.setData(player.getUniqueId());
				Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
			}else if (itemName.contains("Set Radius")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				RadiusConv conversation = new RadiusConv();
				conversation.setData(player.getUniqueId(), false);
				Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
			}else if (itemName.contains("Move Stand with Player")) {
				player.closeInventory();
				ConversationFactory cf = new ConversationFactory(plugin);
				MoveStandToPlayerConv conversation = new MoveStandToPlayerConv();
				conversation.setData(player.getUniqueId());
				Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
				conv.begin();
				if (plugin.getConv().containsKey(player.getUniqueId())) {
					plugin.getConv().get(player.getUniqueId()).abandon();
				}
				plugin.getConv().put(player.getUniqueId(), conv);
				
			}else if (itemName.contains("Clone Stand")) {
				ItemStack tool = new ItemStack(Material.STICK);
				ItemMeta toolMeta = tool.getItemMeta();
				toolMeta.setDisplayName(ChatColor.GREEN + "Clone tool");
				tool.setItemMeta(toolMeta);
				if (!player.getInventory().contains(tool)) {
					player.getInventory().addItem(tool);
				}
				plugin.getCloneMap().put(player.getUniqueId(), stand);
				player.closeInventory();
			}else if (itemName.contains("Special Clone")) {
				Location loc = stand.getLocation().clone();
				loc.add(0, 1, 0);
				ArmorStand newStand = (ArmorStand) Bukkit.getWorld(stand.getWorld().getUID()).spawnEntity(loc, EntityType.ARMOR_STAND);
				copyStandSettings(stand, newStand);
				player.sendMessage(ChatColor.GOLD + "Stand cloned");
			}

			optionsMenuInventory i = new optionsMenuInventory();
			if (itemName.contains("Toggle Glow")) {
				stand.setGlowing(!stand.isGlowing());
				i.openInventory(player, stand);
			}else if (itemName.contains("Toggle Size")){
				stand.setSmall(!stand.isSmall());
				i.openInventory(player, stand);
			}else if (itemName.contains("Toggle Invisibility")) {
				stand.setVisible(!stand.isVisible());
				i.openInventory(player, stand);
			}else if (itemName.contains("Toggle Fire")) {
				int fireTicks = stand.getFireTicks();
				if (fireTicks == -1) {
					stand.setFireTicks(2147483645);
					stand.setInvulnerable(true);
				}else {
					stand.setFireTicks(-1);
				}
				i.openInventory(player, stand);
			}else if (itemName.contains("Toggle Base")) {
				stand.setBasePlate(!stand.hasBasePlate());
				i.openInventory(player, stand);
			}else if (itemName.contains("Toggle Gravity")) {
				stand.setGravity(!stand.hasGravity());
				i.openInventory(player, stand);
			}else if (itemName.contains("Toggle Arms")) {
				stand.setArms(!stand.hasArms());
				i.openInventory(player, stand);
			}else if (itemName.contains("Set Parent")) {
				UUID uuid = stand.getUniqueId();
				if (!plugin.getParentMap().containsKey(uuid)) {
					int radius = 1;
					plugin.getParentMap().put(uuid, radius);
					
					stand.setVisible(false);
					stand.setCustomNameVisible(false);
					stand.setCustomName("N/A");

					player.getInventory().addItem(stand.getEquipment().getHelmet());
					player.getInventory().addItem(stand.getEquipment().getChestplate());
					player.getInventory().addItem(stand.getEquipment().getLeggings());
					player.getInventory().addItem(stand.getEquipment().getBoots());
					player.getInventory().addItem(stand.getEquipment().getItemInMainHand());
					player.getInventory().addItem(stand.getEquipment().getItemInOffHand());

					stand.getEquipment().setHelmet(new ItemStack(Material.AIR));
					stand.getEquipment().setChestplate(new ItemStack(Material.AIR));
					stand.getEquipment().setLeggings(new ItemStack(Material.AIR));
					stand.getEquipment().setBoots(new ItemStack(Material.AIR));
					stand.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
					stand.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
					
					stand.setGravity(false);
					stand.setBasePlate(false);
					stand.setArms(false);
					stand.setSmall(false);
				}else {
					stand.setVisible(true);
					plugin.getParentMap().remove(uuid);
				}
				i.openInventory(player, stand);
			}
		}
	}
	
	private void copyStandSettings(ArmorStand oldStand, ArmorStand newStand) {
		newStand.setBasePlate(oldStand.hasBasePlate());
		
		newStand.setLeftLegPose(oldStand.getLeftLegPose());
		newStand.setRightLegPose(oldStand.getRightLegPose());
		newStand.setLeftArmPose(oldStand.getLeftArmPose());
		newStand.setRightArmPose(oldStand.getRightArmPose());
		newStand.setBodyPose(oldStand.getBodyPose());
		newStand.setHeadPose(oldStand.getHeadPose());
		
		newStand.setCustomName(oldStand.getCustomName());
		if (!newStand.getCustomName().equals("N/A")) {
			newStand.setCustomNameVisible(true);
		}else {
			newStand.setCustomNameVisible(false);
		}
		newStand.getEquipment().setItemInOffHand(oldStand.getEquipment().getItemInOffHand());
		newStand.getEquipment().setItemInMainHand(oldStand.getEquipment().getItemInMainHand());
		newStand.getEquipment().setBoots(oldStand.getEquipment().getBoots());
		newStand.getEquipment().setLeggings(oldStand.getEquipment().getLeggings());
		newStand.getEquipment().setChestplate(oldStand.getEquipment().getChestplate());
		newStand.getEquipment().setHelmet(oldStand.getEquipment().getHelmet());
		
		newStand.setFireTicks(oldStand.getFireTicks());
		newStand.setGlowing(oldStand.isGlowing());
		newStand.setGravity(oldStand.hasGravity());
		newStand.setSmall(oldStand.isSmall());
		newStand.setVisible(oldStand.isVisible());
		
	}
}
