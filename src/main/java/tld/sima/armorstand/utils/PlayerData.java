package tld.sima.armorstand.utils;

import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

	private final CustomArmorstandTool customArmorstandTool;
	
	private ArmorStand pairedStand;
	private Conversation currentConversation;
	
	private ArmorStand clonedStand;
	
	public PlayerData() {
		this.customArmorstandTool = new CustomArmorstandTool();
	}

	public void replaceCurrentConversation(Conversation currentConversation) {
		if(this.currentConversation != null) {
			this.currentConversation.abandon();
		}
		this.currentConversation = currentConversation;
	}
	
	public void abandonConversation() {
		if(this.currentConversation != null) {
			this.currentConversation.abandon();
		}
	}

	public CustomArmorstandTool getCustomArmorstandTool(){
		return this.customArmorstandTool;
	}
	
	public ArmorStand getPairedStand() {
		return pairedStand;
	}
	
	public Conversation getCurrentConversation() {
		return currentConversation;
	}
	
	public boolean isArmorstandTool(ItemStack item){
		return this.customArmorstandTool.isTool(item);
	}
	
	public ToolType getToolType() {
		return this.customArmorstandTool.getToolType();
	}
	
	public ArmorStand getClonedStand() {
		return clonedStand;
	}
	
	public void setPairedStand(ArmorStand pairedStand) {
		this.pairedStand = pairedStand;
	}
	
	public void setCurrentConversation(Conversation currentConversation) {
		this.currentConversation = currentConversation;
	}
	
	public void setToolItemType(ItemStack newTool) {
		this.customArmorstandTool.setTool(newTool);
	}
	
	public void setToolType(ToolType type) {
		this.customArmorstandTool.setToolType(type);
	}
	
	public void setArmorstandTool(ItemStack newTool, ToolType type) {
		this.customArmorstandTool.setTool(newTool);
		this.customArmorstandTool.setToolType(type);
	}

	public void setFuzzyRadius(double radius){
		this.customArmorstandTool.setFuzzyRadius(radius);
	}
	
	public void setClonedStand(ArmorStand cloned) {
		this.clonedStand = cloned;
	}
}
