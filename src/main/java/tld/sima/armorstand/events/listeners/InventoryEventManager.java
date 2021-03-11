package tld.sima.armorstand.events.listeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;

public class InventoryEventManager implements Listener {
	
	private final Main plugin = Main.getPlugin(Main.class);
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		ArmorStand stand = plugin.getPairedStand(player.getUniqueId());
		if (stand == null) {
			return;
		}

		Inventory open = event.getClickedInventory();
		if (open == null) {
			return;
		}

		String openName = event.getView().getTitle();
		ItemStack item = event.getCurrentItem();

		String prefixMain = (ChatColor.DARK_BLUE + "Armorstand GUI");
		String prefixOptions = (ChatColor.DARK_BLUE + "Armorstand GUI Options");
		String prefixParent = (ChatColor.DARK_BLUE + "Armorstand Parent GUI Options");
		
		if(event.getClickedInventory().equals(event.getView().getTopInventory())) {
			if (openName.equals(prefixMain)) {
				plugin.getServer().getConsoleSender().sendMessage("Got to main menu");
				event.setCancelled(true);

				if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
					player.sendMessage(ChatColor.WHITE + "Clicked something strange!");
					return;
				}
				plugin.getServer().getConsoleSender().sendMessage("Item isn't Null!");
				
				String itemName = item.getItemMeta().getDisplayName();
				if (event.getAction().equals(InventoryAction.PICKUP_HALF)) {
					if(MainLockItemEvents.ItemParser(itemName, player, stand)) {
						return;
					}
				}
				plugin.getServer().getConsoleSender().sendMessage("Action is fine.");
				
				if(MainMenuItemEvents.parseItem(itemName, player, stand)) {
					return;
				}
				plugin.getServer().getConsoleSender().sendMessage("Was something outside of parseItem method");
				ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, stand);
				if (itemName.contains("Change Head")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getHelmet().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getHelmet());
					}
					stand.getEquipment().setHelmet(setter);
					player.sendMessage(ChatColor.GOLD + "Helmet set.");
					event.getCursor().setAmount(0);
					plugin.getServer().getPluginManager().callEvent(e);
					if (!e.isCancelled()) {
						MainMenuInventory i = new MainMenuInventory();
						i.newInventory(player, stand);
					}
				}else if (itemName.contains("Change Chest")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getChestplate().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getChestplate());
					}
					stand.getEquipment().setChestplate(setter);
					player.sendMessage(ChatColor.GOLD + "Chestplate set.");
					event.getCursor().setAmount(0);
					if (!e.isCancelled()) {
						MainMenuInventory i = new MainMenuInventory();
						i.newInventory(player, stand);
					}
				}else if (itemName.contains("Change Legs")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getLeggings().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getLeggings());
					}
					stand.getEquipment().setLeggings(setter);
					player.sendMessage(ChatColor.GOLD + "Leggings set.");
					event.getCursor().setAmount(0);
					if (!e.isCancelled()) {
						MainMenuInventory i = new MainMenuInventory();
						i.newInventory(player, stand);
					}
				}else if (itemName.contains("Change Feet")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getBoots().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getBoots());
					}
					stand.getEquipment().setBoots(setter);
					player.sendMessage(ChatColor.GOLD + "Boots set.");
					event.getCursor().setAmount(0);
					if (!e.isCancelled()) {
						MainMenuInventory i = new MainMenuInventory();
						i.newInventory(player, stand);
					}
				}else if (itemName.contains("Change Left Hand")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getItemInOffHand().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getItemInOffHand());
					}
					stand.getEquipment().setItemInOffHand(setter);
					player.sendMessage(ChatColor.GOLD + "Off-hand set.");
					event.getCursor().setAmount(0);
					if (!e.isCancelled()) {
						MainMenuInventory i = new MainMenuInventory();
						i.newInventory(player, stand);
					}
				}else if (itemName.contains("Change Right Hand")) {
					ItemStack setter = event.getCursor();
					if (!(stand.getEquipment().getItemInMainHand().equals(new ItemStack(Material.AIR)))) {
						player.getInventory().addItem(stand.getEquipment().getItemInMainHand());
					}
					stand.getEquipment().setItemInMainHand(setter);
					player.sendMessage(ChatColor.GOLD + "Main-hand set.");
					event.getCursor().setAmount(0);
					if (!e.isCancelled()) {
						MainMenuInventory i = new MainMenuInventory();
						i.newInventory(player, stand);
					}
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
					if (!e.isCancelled()) {
						MainMenuInventory i = new MainMenuInventory();
						i.newInventory(player, stand);
					}
				}
			}else if (openName.equals(prefixOptions)) {
				event.setCancelled(true);
	
				if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
					player.sendMessage(ChatColor.WHITE + "Clicked something strange!");
					return;
				}
				
				String itemName = item.getItemMeta().getDisplayName();
				OptionsMenuItemEvents.parseItem(itemName, player, stand);
			}else if (event.getView().getTitle().equals(prefixParent)) {
				event.setCancelled(true);
	
				if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
					player.sendMessage(ChatColor.WHITE + "Clicked something strange!");
					return;
				}
				
				ParentMenuItemEvents.parseItem(item, player, stand);
			}
		}else if (event.getView().getTitle().equals(prefixMain) || 
				event.getView().getTitle().equals(prefixOptions) || 
				event.getView().getTitle().equals(prefixParent)) {
			InventoryAction action = event.getAction();
			if(	action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
					|| action.equals(InventoryAction.SWAP_WITH_CURSOR)) {
				event.setCancelled(true);
			}
		}
	}
}
