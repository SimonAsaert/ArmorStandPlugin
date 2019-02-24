package tld.sima.armorstand.inventories.items;

import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;
@Deprecated
public class ParentMenuItems {
	private Main plugin = Main.getPlugin(Main.class);
	
	// Default parent button, tools related to default parent
	ItemStack defaultParent;
	
	// Smart parent button, tools related to smart parent;
	ItemStack smartParent;
	ItemStack getTool;
	
	// Disable parent stand entirely
	ItemStack stopParent;
	
	// Back button
	ItemStack back;
	
	public ParentMenuItems() {
		
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
	
	public ItemStack getStop() {
		return stopParent.clone();
	}
}
