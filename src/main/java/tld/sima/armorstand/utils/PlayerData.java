package tld.sima.armorstand.utils;

import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.ArmorStand;

public class PlayerData {
	
	private ArmorStand pairedStand;
	private Conversation currentConversation;
	
	private Material materialType;
	private ToolType toolType;
	
	private ArmorStand clonedStand;
	
	public PlayerData() {
		toolType = ToolType.NONE;
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
	
	public ArmorStand getPairedStand() {
		return pairedStand;
	}
	
	public Conversation getCurrentConversation() {
		return currentConversation;
	}
	
	public Material getToolMaterialType() {
		return materialType;
	}
	
	public ToolType getToolType() {
		return toolType;
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
	
	public void setToolMaterialType(Material material) {
		this.materialType = material;
	}
	
	public void setToolType(ToolType type) {
		this.toolType = type;
	}
	
	public void setArmorstandTool(Material material, ToolType type) {
		this.materialType = material;
		this.toolType = type;
	}
	
	public void setClonedStand(ArmorStand cloned) {
		this.clonedStand = cloned;
	}
}
