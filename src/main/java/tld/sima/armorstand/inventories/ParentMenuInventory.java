package tld.sima.armorstand.inventories;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;

public class ParentMenuInventory {
	private Main plugin = Main.getPlugin(Main.class);
	
	public void openInventory(Player player, ArmorStand stand) {
		Inventory i = plugin.getServer().createInventory(null, 9, ChatColor.DARK_BLUE + "Armorstand Parent GUI Options");
		boolean smartFlag = plugin.getSmartParent().containsKey(stand.getUniqueId());
		boolean defaultFlag = plugin.getParentMap().containsKey(stand.getUniqueId());
		ItemStack disabled = plugin.createItem(new ItemStack(Material.BARRIER), ChatColor.RED + "Disabled", Arrays.asList(""));
		
		ItemStack defaultParent = plugin.createItem(new ItemStack(Material.ARMOR_STAND), ChatColor.GREEN + "Radius Parent", Arrays.asList(""));		
		ItemStack smartParent = plugin.createItem(new ItemStack(Material.ARMOR_STAND), ChatColor.GREEN + "Smart Parent", Arrays.asList(""));

		ItemStack getTool = plugin.createItem(new ItemStack(Material.STICK), ChatColor.GREEN + "Smart Parent tool", Arrays.asList(""));
		ItemStack stop = plugin.createItem(new ItemStack(Material.REDSTONE_BLOCK), ChatColor.RED + "Remove this stand from being parent", Arrays.asList(""));

		ItemStack back = plugin.createItem(new ItemStack(Material.BARRIER), ChatColor.RED + "Back", Arrays.asList(""));
		
		if(smartFlag) {
			i.setItem(0, defaultParent);
			i.setItem(1, disabled);
			
			i.setItem(3, smartParent);
			i.setItem(4, getTool);
			
			i.setItem(6, stop);
		}else if(defaultFlag) {
			ItemStack radius = plugin.createItem(new ItemStack(Material.STICK), 
					ChatColor.WHITE + "Set Radius", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + 
					"Current Radius: " + ChatColor.WHITE + plugin.getParentMap().get(stand.getUniqueId())));
			
			i.setItem(0, defaultParent);
			i.setItem(1, radius);
			
			i.setItem(3, smartParent);
			i.setItem(4, disabled);

			i.setItem(6, stop);
			
		}else {

			i.setItem(0, defaultParent);
			i.setItem(1, disabled);
			
			i.setItem(3, smartParent);
			i.setItem(4, disabled);
		}
		
		i.setItem(8, back);
		
		player.openInventory(i);
	}
}
