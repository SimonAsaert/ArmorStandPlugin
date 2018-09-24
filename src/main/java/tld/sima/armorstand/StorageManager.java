package tld.sima.armorstand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StorageManager {
	
	private Main plugin = Main.getPlugin(Main.class);

	private FileConfiguration storagecfg;
	private File storagefile;
	
	public void setup() {
		// Create plugin folder if doesn't exist.
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		// Create main file
		String FileLocation = plugin.getDataFolder().toString() + File.separator + "Storage" + ".yml";
		File tmp = new File(FileLocation);
		storagefile = tmp;
		// Check if file exists
		if (!storagefile.exists()) {
			try {
				storagefile.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
				Bukkit.getServer().getConsoleSender().sendMessage(net.md_5.bungee.api.ChatColor.RED + "Storage file unable to be created!");
			}
		}
		storagecfg = YamlConfiguration.loadConfiguration(storagefile);
		createStorageValues();
	}
	
	private void createStorageValues() {
		storagecfg.addDefault("Stands.parent.uuids", new ArrayList<String>());
		storagecfg.options().copyDefaults(true);
		savecfg();
	}

	private boolean savecfg() {
		try {
			storagecfg.save(storagefile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to save storage file!" );
			return false;
		}
		return true;
	}
	
	public void scanList(HashMap<UUID, Integer> list) {
		// Get List of UUIDS from HashMap and convert to String
		List<String> strings = new ArrayList<String>();
		for (UUID uuid : list.keySet()) {
			if (!(Bukkit.getServer().getEntity(uuid) == null)) {
				strings.add(uuid.toString());
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Radius of " + strings.get(strings.size()-1) + ": " + ChatColor.WHITE + list.get(uuid));
			}else {
				Bukkit.getServer().getConsoleSender().sendMessage("Ping");
			}
		}
		
		// Get list from file and duplicate to ensure I don't remove current item by accident
		List<String> oldStrings = new ArrayList<String>(storagecfg.getStringList("Stands.parent.uuids"));

		// Update initial list with the updated list of uuids that actually exist
		storagecfg.set("Stands.parent.uuids", new ArrayList<String>(strings));
		
		// Ensure that whatever is on the file actually exists on the server. Remove if it isn't.
		for (String string : oldStrings) {
			if (!strings.contains(string)) {
				storagecfg.set("Stands." + string, null);
			}else {
				storagecfg.set("Stands."+string+".radius", list.get(UUID.fromString(string)));
				strings.remove(string);
			}
		}
		
		for (String string : strings) {
			if(!oldStrings.contains(string)) {
				storagecfg.set("Stands." + string + ".radius", list.get(UUID.fromString(string)));
			}
		}
		savecfg();
	}
	
	public void checkParentUUID() {
		List<String> check = storagecfg.getStringList("Stands.parent.uuids");
		for (String string : check) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + string);
		}
	}
	
	public void removeUUID(UUID uuid) {
		
		List<String> list = storagecfg.getStringList("Stands.parent.uuids");
		if (list.contains(uuid.toString())) {
			list.remove(uuid.toString());
			storagecfg.set("Stands.parent.uuids", list);
			storagecfg.set("Stands." + uuid.toString(), null);
			savecfg();
		}
	}
	
	public void inputUUID(UUID uuid, int radius) {
		List<String> list = storagecfg.getStringList("Stands.parent.uuids");
		list.add(uuid.toString());
		storagecfg.set("Stands.parent.uuids", list);
		storagecfg.set("Stands."+uuid.toString()+".radius", radius);
		
		savecfg();
	}

	public HashMap<UUID, Integer> getList(){
		List<String> list = storagecfg.getStringList("Stands.parent.uuids");
		HashMap<UUID, Integer> returnList = new HashMap<UUID, Integer>();
		int radius;
		for (String string : list) {
			radius = storagecfg.getInt("Stands." + string + ".radius");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Radius of " + string + ": " + ChatColor.WHITE + radius);
			returnList.put(UUID.fromString(string), radius);
		}
		for (UUID uuid : returnList.keySet()) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + uuid.toString() + " | " + Bukkit.getServer().getEntity(uuid).getType().toString());
		}
		return returnList;
	}
}
