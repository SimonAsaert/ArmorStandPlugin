package tld.sima.armorstand.inventories.items;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;

public class ParentMenuItems {
	private Main plugin = Main.getPlugin(Main.class);
	
	// Default parent button, tools related to default parent
	ItemStack defaultParent;
	
	// Smart parent button, tools related to smart parent;
	ItemStack smartParent;
	ItemStack getTool;
	
	// Back button
	ItemStack back;
	
	public ParentMenuItems() {
		defaultParent = plugin.createItem(new ItemStack(Material.ARMOR_STAND), ChatColor.GREEN + "Set Parent", Arrays.asList(""));

		smartParent = plugin.createItem(new ItemStack(Material.ARMOR_STAND), ChatColor.GREEN + "Smart Parent", Arrays.asList(""));
		getTool = plugin.createItem(new ItemStack(Material.STICK), ChatColor.GREEN + "Smart Parent tool", Arrays.asList(""));
		back = plugin.createItem(new ItemStack(Material.BARRIER), ChatColor.RED + "Back", Arrays.asList(""));
	}
	
	public ItemStack getDefaultParent() {
		return defaultParent.clone();
	}
	
	public ItemStack getSmartParent() {
		return smartParent.clone();
	}
	
	public ItemStack getTool() {
		return getTool.clone();
	}
	
	public ItemStack getBack() {
		return back.clone();
	}
}
