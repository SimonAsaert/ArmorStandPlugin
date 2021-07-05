package tld.sima.armorstand;

import java.io.File;
import java.util.*;

import org.bukkit.*;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import tld.sima.armorstand.commands.ToolCommandManager;
import tld.sima.armorstand.commands.WorldInteractionCommandManger;
import tld.sima.armorstand.events.listeners.EventManager;
import tld.sima.armorstand.events.listeners.InventoryEventManager;
import tld.sima.armorstand.files.ProtectedStands;
import tld.sima.armorstand.files.SmartParentStorage;
import tld.sima.armorstand.files.StorageManager;
import tld.sima.armorstand.utils.ItemHub;
import tld.sima.armorstand.utils.Pair;
import tld.sima.armorstand.utils.PlayerData;
import tld.sima.armorstand.utils.ToolType;

public class Main extends JavaPlugin {
	// Player-based information
	private final Map<UUID, PlayerData> playerData = new HashMap<UUID, PlayerData>();
	private final Set<UUID> playersScanning = Collections.synchronizedSet(new LinkedHashSet<>());
	private Set<UUID> protectedStands;
	
	// ItemHub for used items
	private ItemHub itemHub;
	
	// Parent information
	private final Map<UUID, List<UUID>> smartParent = new HashMap<UUID, List<UUID>>();
	private Map<UUID,Integer> parentList;
	private StorageManager stmgr;

	// Runnables
	private BukkitTask scanStand;
	private Map<UUID, Pair<ArmorStand, Boolean>> previousScannedStand;

	// Extras
	private API api;
	public boolean AnimationActive = false;

	@Override
	public void onEnable() {
		// Setup Events
		getServer().getPluginManager().registerEvents(new EventManager(), this);
		getServer().getPluginManager().registerEvents(new InventoryEventManager(), this);

		// Setup storage manager and setup List of UUIDS for the parent list as well as setting up SmartParents
		stmgr = new StorageManager();
		stmgr.setup();
		parentList = stmgr.getList();
		protectedStands = ProtectedStands.getList();
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
			List<UUID> uuids = sps.loadList();
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
		
		// Initialize command managers
		ToolCommandManager tcm = new ToolCommandManager();
		this.getCommand(tcm.cmd1).setExecutor(tcm);
		this.getCommand(tcm.cmd1).setTabCompleter(tcm);

		WorldInteractionCommandManger wicm = new WorldInteractionCommandManger();
		this.getCommand(wicm.cmd1).setExecutor(wicm);

		for(Player player : this.getServer().getOnlinePlayers()) {
			playerData.put(player.getUniqueId(), new PlayerData());
		}

		// Start the armorstand scanning task
		previousScannedStand = new HashMap<>();
		scanStand = new BukkitRunnable(){
			@Override
			public void run() {
				Iterator<UUID> iter = playersScanning.iterator();
				Set<UUID> interactedStands = new HashSet<UUID>();
				while(iter.hasNext()){
					UUID pUUID = iter.next();
					Pair<ArmorStand, Boolean> pair = previousScannedStand.get(pUUID);
					Player p = (Player) Bukkit.getEntity(pUUID);
					if (p == null){
						if (pair != null && pair.getLeft() != null){
							pair.getLeft().setGlowing(pair.getRight());
						}
						previousScannedStand.remove(pUUID);
						iter.remove();
						continue;
					}
					RayTraceResult rayTraceResult = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getEyeLocation().getDirection(), 4, (entity) -> entity instanceof ArmorStand);

					if (rayTraceResult != null){
						final Entity e = rayTraceResult.getHitEntity();
						if (e instanceof ArmorStand){
							if(!interactedStands.contains(e.getUniqueId())) {
								interactedStands.add(e.getUniqueId());
								if (pair == null) {
									previousScannedStand.put(pUUID, new Pair<ArmorStand, Boolean>((ArmorStand) e, e.isGlowing()));
									e.setGlowing(true);
								} else if (!pair.getLeft().getUniqueId().equals(e.getUniqueId())) {
									if (pair.getLeft() != null) {
										pair.getLeft().setGlowing(pair.getRight());
									}
									previousScannedStand.put(pUUID, new Pair<ArmorStand, Boolean>((ArmorStand) e, e.isGlowing()));
									e.setGlowing(true);
								}
							}
							continue;
						}
					}
					if (pair != null && pair.getLeft() != null){
						pair.getLeft().setGlowing(pair.getRight());
					}
					previousScannedStand.remove(pUUID);
				}
			}
		}.runTaskTimer(this, 20, 5);

		// Initialize API for other plugins to hook onto!
		api = new API(); 
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Armorstand API Enabled.");
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

		scanStand.cancel();

		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Armorstand API Disabled.");
	}

	public Conversation getConv(UUID uuid){
		return playerData.get(uuid).getCurrentConversation();
	}
	
	public void replaceConversation(UUID uuid, Conversation conversation) {
		playerData.get(uuid).replaceCurrentConversation(conversation);
	}

	public boolean addRemoveToPlayerScanningSet(UUID pUUID){
		if (this.playersScanning.contains(pUUID)){
			this.playersScanning.remove(pUUID);
			Pair<ArmorStand, Boolean> pair = previousScannedStand.get(pUUID);
			if (pair != null && pair.getLeft() != null){
				pair.getLeft().setGlowing(pair.getRight());
			}
			previousScannedStand.remove(pUUID);
			return false;
		}else{
			this.playersScanning.add(pUUID);
			return true;
		}
	}

	public Collection<Pair<ArmorStand, Boolean>> getPairCollection(){
		return previousScannedStand.values();
	}

	public ArmorStand getPairedStand(UUID uuid){
		return playerData.get(uuid).getPairedStand();
	}
	
	public void setPairedStand(UUID uuid, ArmorStand stand) {
		playerData.get(uuid).setPairedStand(stand);
	}

	public Map<UUID, Integer> getParentMap(){
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

	public double getFuzzyToolRadius(UUID uuid){
		return playerData.get(uuid).getCustomArmorstandTool().getFuzzyRadius();
	}

	public void setFuzzyToolRadius(UUID uuid, double radius){
		this.playerData.get(uuid).setFuzzyRadius(radius);
	}
	
	public boolean isArmorstandCustomTool(UUID uuid, ItemStack item) {
		return playerData.get(uuid).getCustomArmorstandTool().isTool(item);
	}
	
	public ToolType getArmorstandToolType(UUID uuid) {
		return playerData.get(uuid).getToolType();
	}
	
	public void setArmorstandCustomTool(UUID uuid, ItemStack newTool) {
		playerData.get(uuid).getCustomArmorstandTool().setTool(newTool);
	}
	
	public void setArmorstandCustomToolType(UUID uuid, ToolType tooltype) {
		playerData.get(uuid).setToolType(tooltype);
	}
	
	public void setArmorstandTool(UUID uuid, ItemStack newTool, ToolType tooltype) {
		playerData.get(uuid).setArmorstandTool(newTool, tooltype);
	}
	
	public Map<UUID, List<UUID>> getSmartParent() {
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

	public ItemStack createItem(ItemStack item, String disName, List<String> loreList) {
		ItemMeta itemM = item.getItemMeta();
		if (itemM != null) {
			if (!disName.equals("")) {
				itemM.setDisplayName(disName);
			}
			if(!loreList.isEmpty()) {
				itemM.setLore(loreList);
			}
			itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			item.setItemMeta(itemM);
		}
		return item;
	}
	
	public Set<UUID> getProtectedStands(){
		return protectedStands;
	}
}
