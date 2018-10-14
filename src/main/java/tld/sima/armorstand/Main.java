package tld.sima.armorstand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.events.EventManager;
import tld.sima.armorstand.events.inventoryEventManager;

public class Main extends JavaPlugin {

	private HashMap<UUID, ArmorStand> standMap = new HashMap<UUID, ArmorStand>();
	private List<Conversation> convList = new ArrayList<Conversation>();
	private ItemStack removeTool = new ItemStack(Material.POISONOUS_POTATO);
	
	private HashMap<UUID,Integer> parentList;
	private HashMap<UUID, ArmorStand> cloneMap = new HashMap<UUID, ArmorStand>();
	private StorageManager stmgr;
	public boolean AnimationActive = false;
	
	@Override
	public void onEnable() {
		// Setup Events
		getServer().getPluginManager().registerEvents(new EventManager(), this);
		getServer().getPluginManager().registerEvents(new inventoryEventManager(), this);

		// Setup storage manager and setup List of UUIDS for the parent list
		stmgr = new StorageManager();
		stmgr.setup();
		parentList = stmgr.getList();
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Armorstand API Enabled.");
		if (this.getServer().getPluginManager().getPlugin("ArmorstandAnimation") != null) {
			AnimationActive = true;
		}
	}

	@Override
	public void onDisable() {
		standMap.clear();
		// Clear Conversation list to cancel out all the conversations with players
		for (Conversation conv : convList) {
			if (conv != null) {
				conv.abandon();
			}
		}
		stmgr.scanList(parentList);
		
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Armorstand API Disabled.");
	}
	
	public List<Conversation> getConv(){
		return convList;
	}
	
	public HashMap<UUID, ArmorStand> getStandMap(){
		return standMap;
	}
	
	public HashMap<UUID, Integer> getParentMap(){
		return parentList;
	}
	
	public HashMap<UUID, ArmorStand> getCloneMap(){
		return cloneMap;
	}
	
	public ItemStack getRemoveTool() {
		return removeTool;
	}
}
