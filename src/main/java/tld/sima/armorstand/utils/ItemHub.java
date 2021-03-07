package tld.sima.armorstand.utils;

import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.files.DefaultSettings;

public class ItemHub {
	
	private final ItemStack smartParentTool;
	private final ItemStack cloneTool;
	private final ItemStack removeTool;
	
	public ItemHub() {
		DefaultSettings settings = new DefaultSettings();
		settings.setup();
		this.removeTool = settings.getRemoveTool();
		this.cloneTool = settings.getCloneTool();
		this.smartParentTool = settings.getParentTool();
	}
	
	public ItemStack getSmartParentTool() {
		return smartParentTool;
	}
	
	public ItemStack getCloneTool() {
		return cloneTool;
	}
	
	public ItemStack getRemoveTool() {
		return removeTool;
	}
}
