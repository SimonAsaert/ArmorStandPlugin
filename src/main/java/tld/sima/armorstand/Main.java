package tld.sima.armorstand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.commands.ToolCommandManager;
import tld.sima.armorstand.events.listeners.EventManager;
import tld.sima.armorstand.events.listeners.inventoryEventManager;
import tld.sima.armorstand.files.DefaultSettings;
import tld.sima.armorstand.files.SmartParentStorage;
import tld.sima.armorstand.files.StorageManager;
import tld.sima.armorstand.inventories.items.ItemHub;
import tld.sima.armorstand.utils.Pair;
import tld.sima.armorstand.utils.ToolType;

public class Main extends JavaPlugin {
	
	private HashMap<UUID, ArmorStand> standMap = new HashMap<UUID, ArmorStand>();
	private HashMap<UUID, Conversation> convList = new HashMap<UUID, Conversation>();
	private HashMap<UUID, Pair<Material, ToolType>> playerTool = new HashMap<UUID, Pair<Material, ToolType>>();
	private HashMap<UUID, ArrayList<UUID>> smartParent = new HashMap<UUID, ArrayList<UUID>>();
	
	private ItemStack removeTool;
	private ItemStack cloneTool;
	private ItemStack addToParentTool;
	
	private ItemHub inventoryItems;

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

		// Setup storage manager and setup List of UUIDS for the parent list as well as setting up SmartParents
		stmgr = new StorageManager();
		stmgr.setup();
		parentList = stmgr.getList();
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Armorstand API Enabled.");
		if (Bukkit.getServer().getPluginManager().getPlugin("ArmorstandAnimationPlugin") != null) {
			AnimationActive = true;
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Detected Armour stands animations.");
		}

		String fileName = this.getDataFolder().toString() + File.separator + "Storage" + File.separator + "SmartParent";
		File dir = new File(fileName);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		File[] files = dir.listFiles();
		for(File file : files) {
			SmartParentStorage sps = new SmartParentStorage();
			if(!sps.load(file)) {
				continue;
			}
			smartParent.put(sps.getUUID(), sps.loadList());
			StringBuilder builder = new StringBuilder();
			ArrayList<UUID> uuids = sps.loadList();
			if(uuids.size() > 0) {
				builder.append(ChatColor.GREEN).append("Child parents: ").append(ChatColor.WHITE).append(uuids.get(0));
				for(int i = 1 ; i < uuids.size() ; i++) {
					builder.append(", ").append(uuids.get(i));
				}
			}
			Bukkit.getServer().getConsoleSender().sendMessage(builder.toString());
		}
		
		// Initilize settings for plugin
		DefaultSettings settings = new DefaultSettings();
		settings.setup();
		this.removeTool = settings.getRemoveTool();
		this.cloneTool = settings.getCloneTool();
		this.addToParentTool = settings.getParentTool();
		
		// Initilize inventory items
		this.inventoryItems = new ItemHub();
		
		// Initialize command manager
		ToolCommandManager tcm = new ToolCommandManager();
		this.getCommand(tcm.cmd1).setExecutor(tcm);
		
		for(Player player : this.getServer().getOnlinePlayers()) {
			Pair<Material, ToolType> pair = new Pair<Material, ToolType>(Material.AIR, ToolType.NONE);
			playerTool.put(player.getUniqueId(), pair);
		}
		
		// Initialize API for other plugins to hook onto!
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
		for(UUID uuid : smartParent.keySet()) {
			SmartParentStorage sps = new SmartParentStorage();
			sps.setup(uuid);
			sps.saveUUID(uuid);
			sps.saveList(smartParent.get(uuid));
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
	
	public ItemStack getCloneTool() {
		return cloneTool;
	}
	
	public ItemStack getSmartParentTool() {
		return addToParentTool;
	}
	
	public ItemHub getItemHub() {
		return inventoryItems;
	}

	public API getAPI() {
		return api;
	}
	
	public HashMap<UUID, Pair<Material, ToolType>> getPlayerCustomTool() {
		return playerTool;
	}
	
	public HashMap<UUID, ArrayList<UUID>> getSmartParent() {
		return smartParent;
	}

	public ItemStack createItem(ItemStack item, String disName, List<String> list) {
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(disName);
		itemM.setLore(list);
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(itemM);
		
		return item;
	}
}
