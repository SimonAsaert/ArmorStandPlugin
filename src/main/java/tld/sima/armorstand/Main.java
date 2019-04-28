package tld.sima.armorstand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import tld.sima.armorstand.files.ProtectedStands;
import tld.sima.armorstand.files.SmartParentStorage;
import tld.sima.armorstand.files.StorageManager;
import tld.sima.armorstand.utils.ItemHub;
import tld.sima.armorstand.utils.PlayerData;
import tld.sima.armorstand.utils.ToolType;

public class Main extends JavaPlugin {
	// Player-based information
	private HashMap<UUID, PlayerData> playerData = new HashMap<UUID, PlayerData>();
	private HashSet<UUID> protectedStands = new HashSet<UUID>();
	
	// ItemHub for used items
	private ItemHub itemHub;
	
	// Parent information
	private HashMap<UUID, ArrayList<UUID>> smartParent = new HashMap<UUID, ArrayList<UUID>>();
	private HashMap<UUID,Integer> parentList;
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
		protectedStands = ProtectedStands.getList();
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
		itemHub = new ItemHub();
		
		// Initialize command manager
		ToolCommandManager tcm = new ToolCommandManager();
		this.getCommand(tcm.cmd1).setExecutor(tcm);
		
		for(Player player : this.getServer().getOnlinePlayers()) {
			playerData.put(player.getUniqueId(), new PlayerData());
		}
		
		// Initialize API for other plugins to hook onto!
		api = new API(); 
	}

	@Override
	public void onDisable() {
		// Clear Conversation list to cancel out all the conversations with players
		for (Player player : Bukkit.getOnlinePlayers()) {
			playerData.get(player.getUniqueId()).abandonConversation();
		}
		for(UUID uuid : smartParent.keySet()) {
			SmartParentStorage sps = new SmartParentStorage();
			sps.setup(uuid);
			sps.saveUUID(uuid);
			sps.saveList(smartParent.get(uuid));
		}
		ProtectedStands.saveList(protectedStands);
		stmgr.scanList(parentList);
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Armorstand API Disabled.");
	}

	public Conversation getConv(UUID uuid){
		return playerData.get(uuid).getCurrentConversation();
	}
	
	public void replaceConversation(UUID uuid, Conversation conversation) {
		playerData.get(uuid).replaceCurrentConversation(conversation);
	}

	public ArmorStand getPairedStand(UUID uuid){
		return playerData.get(uuid).getPairedStand();
	}
	
	public void setPairedStand(UUID uuid, ArmorStand stand) {
		playerData.get(uuid).setPairedStand(stand);
	}

	public HashMap<UUID, Integer> getParentMap(){
		return parentList;
	}

	public ArmorStand getClonedStand(UUID uuid){
		return playerData.get(uuid).getClonedStand();
	}
	
	public void setClonedStand(UUID uuid, ArmorStand cloned) {
		playerData.get(uuid).setClonedStand(cloned);
	}

	public ItemStack getRemoveTool() {
		return itemHub.getRemoveTool();
	}
	
	public ItemStack getCloneTool() {
		return itemHub.getCloneTool();
	}
	
	public ItemStack getSmartParentTool() {
		return itemHub.getSmartParentTool();
	}

	public API getAPI() {
		return api;
	}
	
	public Material getArmorstandToolMaterial(UUID uuid) {
		return playerData.get(uuid).getToolMaterialType();
	}
	
	public ToolType getArmorstandToolType(UUID uuid) {
		return playerData.get(uuid).getToolType();
	}
	
	public void setArmorstandToolMaterial(UUID uuid, Material material) {
		playerData.get(uuid).setToolMaterialType(material);
	}
	
	public void setArmorstandToolType(UUID uuid, ToolType tooltype) {
		playerData.get(uuid).setToolType(tooltype);
	}
	
	public void setArmorstandTool(UUID uuid, ToolType tooltype, Material material) {
		playerData.get(uuid).setArmorstandTool(material, tooltype);
	}
	
	public HashMap<UUID, ArrayList<UUID>> getSmartParent() {
		return smartParent;
	}
	
	public PlayerData getPlayerData(UUID uuid){
		return playerData.get(uuid);
	}
	
	public void setupPlayerData(UUID uuid) {
		if(!playerData.containsKey(uuid)) {
			playerData.put(uuid, new PlayerData());
		}
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
	
	public HashSet<UUID> getProtectedStands(){
		return protectedStands;
	}
}
