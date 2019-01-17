package tld.sima.armorstand.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import tld.sima.armorstand.Main;

public class SmartParentStorage {
	private Main plugin = Main.getPlugin(Main.class);
	
	// File and File Configurations here
	private FileConfiguration maincfg;
	private File mainfile;
	/*-------------------------------*/
	
	// Main Config setup
	public boolean setup(UUID uuid) {
		//Create plugin folder if doesn't exist.
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		//Main Config setup
		String fileName = plugin.getDataFolder().toString() + File.separator + "Storage" + File.separator + "SmartParent";
		File tmp = new File(fileName);
		if(!tmp.exists()) {
			tmp.mkdirs();
		}
		mainfile = new File(fileName, uuid.toString() + ".yml");
		
		if(!mainfile.exists()) {
			try {
				mainfile.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Config file created");
			}catch(IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "An error has occured creating the main config filefile");
				return false;
			}
		}
		maincfg = YamlConfiguration.loadConfiguration(mainfile);

		createMainConfigValues();
		
		return true;
	}

	private void createMainConfigValues() {
		maincfg.addDefault("Parent.UUID", "");
		maincfg.addDefault("Parent.List", Arrays.asList(""));
		maincfg.options().copyDefaults(true);
		save();
	}
	
	// Attempt to load from file
	public boolean load(File file) {
		mainfile = file;
		maincfg = YamlConfiguration.loadConfiguration(mainfile);
		return true;
	}
	
	// Save file
	private boolean save() {
		try {
			maincfg.save(mainfile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "unable to add default values to config.");
			return false;
		}
		return true;
	}
	
	// Setter, save array list
	public boolean saveList(ArrayList<UUID> list) {
		ArrayList<String> listAsString = new ArrayList<String>();
		for(UUID uuid : list) {
			listAsString.add(uuid.toString());
		}
		maincfg.set("Parent.List", listAsString);
		return save();
	}
	
	//Setter, save UUID of stand owning this file
	public boolean saveUUID(UUID uuid) {
		maincfg.set("Parent.UUID", uuid.toString());
		return save();
	}
	
	public boolean saveUUID(Entity entity) {
		return saveUUID(entity.getUniqueId());
	}
	
	// Getter, load array list
	public ArrayList<UUID> loadList(){
		List<String> listAsString = maincfg.getStringList("Parent.List");
		ArrayList<UUID> list = new ArrayList<UUID>();
		for(String string : listAsString) {
			list.add(UUID.fromString(string));
		}
		return list;
	}
	
	// Getter, load UUID from stand owning file
	public UUID getUUID() {
		return UUID.fromString(maincfg.getString("Parent.UUID"));
	}
}
