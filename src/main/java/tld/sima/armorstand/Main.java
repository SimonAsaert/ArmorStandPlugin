package tld.sima.armorstand;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.api.API;
import tld.sima.armorstand.events.EventManager;
import tld.sima.armorstand.events.inventoryEventManager;

public class Main extends JavaPlugin {
	
	private HashMap<UUID, ArmorStand> standMap = new HashMap<UUID, ArmorStand>();
	private HashMap<UUID, Conversation> convList = new HashMap<UUID, Conversation>();
	private ItemStack removeTool = new ItemStack(Material.POISONOUS_POTATO);

	private HashMap<UUID,Integer> parentList;
	private HashMap<UUID, ArmorStand> cloneMap = new HashMap<UUID, ArmorStand>();
	private StorageManager stmgr;
	private API api;
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
		if (Bukkit.getServer().getPluginManager().getPlugin("ArmorstandAnimationPlugin") != null) {
			AnimationActive = true;
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Detected Armour stands animations.");
		}
		api = new API(); 
		
	}

	@Override
	public void onDisable() {
		standMap.clear();
		// Clear Conversation list to cancel out all the conversations with players
		for (UUID uuid : convList.keySet()) {
			if (convList.get(uuid) != null) {
				convList.get(uuid).abandon();
			}
		}
		stmgr.scanList(parentList);
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Armorstand API Disabled.");
	}

	public HashMap<UUID, Conversation> getConv(){
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

	public API getAPI() {
		return api;
	}
}
