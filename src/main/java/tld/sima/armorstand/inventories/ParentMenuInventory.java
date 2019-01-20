package tld.sima.armorstand.inventories;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.inventories.items.ItemHub;
import tld.sima.armorstand.inventories.items.ParentMenuItems;

public class ParentMenuInventory {
	private Main plugin = Main.getPlugin(Main.class);
	
	public void openInventory(Player player, ArmorStand stand) {
		Inventory i = plugin.getServer().createInventory(null, 9, ChatColor.DARK_BLUE + "Armorstand Parent GUI Options");
		boolean smartFlag = plugin.getSmartParent().containsKey(stand.getUniqueId());
		boolean defaultFlag = plugin.getParentMap().containsKey(stand.getUniqueId());
		ItemStack disabled = plugin.createItem(new ItemStack(Material.BARRIER), ChatColor.RED + "Disabled", Arrays.asList(""));
		ItemHub ihub = plugin.getItemHub();
		ParentMenuItems hub = ihub.getParentMenuItems();
		if(smartFlag) {
			ItemStack defaultParent = hub.getDefaultParent();
			
			ItemStack smartParent = hub.getSmartParent();
			ItemStack getTool = hub.getTool();
			
			i.setItem(0, defaultParent);
			i.setItem(1, disabled);
			
			i.setItem(3, smartParent);
			i.setItem(4, getTool);
			
			i.setItem(6, hub.getStop());
		}else if(defaultFlag) {
			ItemStack defaultParent = hub.getDefaultParent();
			
			ItemStack smartParent = hub.getSmartParent();
			ItemStack radius = plugin.createItem(new ItemStack(Material.STICK), ChatColor.WHITE + "Set Radius", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current Radius: " + ChatColor.WHITE + plugin.getParentMap().get(stand.getUniqueId())));
			
			i.setItem(0, defaultParent);
			i.setItem(1, radius);
			
			i.setItem(3, smartParent);
			i.setItem(4, disabled);

			i.setItem(6, hub.getStop());
			
		}else {
			ItemStack defaultParent = hub.getDefaultParent();
			ItemStack smartParent = hub.getSmartParent();

			i.setItem(0, defaultParent);
			i.setItem(1, disabled);
			
			i.setItem(3, smartParent);
			i.setItem(4, disabled);
		}
		
		ItemStack back = hub.getBack();
		i.setItem(8, back);
		
		player.openInventory(i);
	}
}
