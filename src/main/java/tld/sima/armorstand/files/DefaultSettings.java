package tld.sima.armorstand.files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tld.sima.armorstand.Main;

public class DefaultSettings {
	private Main plugin = Main.getPlugin(Main.class);
	
	// File and File Configurations here
	private FileConfiguration maincfg;
	private File mainfile;
	/*-------------------------------*/
	
	// Main Config setup
	public boolean setup() {
		//Create plugin folder if doesn't exist.
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		//Main Config setup
		mainfile = new File(plugin.getDataFolder(), "config.yml");
		
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
		maincfg.addDefault("Settings.removetool.type", Material.POISONOUS_POTATO.toString());
		maincfg.addDefault("Settings.clonetool.type", Material.STICK.toString());
		maincfg.addDefault("Settings.clonetool.name", "&AClone Tool");
		maincfg.addDefault("Settings.clonetool.item-description", "");
		
		maincfg.addDefault("Settings.parenttool.type", Material.SLIME_BALL.toString());
		maincfg.addDefault("Settings.parenttool.name", "&AParent Tool");
		maincfg.addDefault("Settings.parenttool.item-description", "");
		maincfg.options().copyDefaults(true);
		save();
	}
	
	private boolean save() {
		try {
			maincfg.save(mainfile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "unable to add default values to config.");
			return false;
		}
		return true;
	}
	
	public ItemStack getRemoveTool() {
		Material material = Material.getMaterial(maincfg.getString("Settings.removetool.type"));
		if(material == null) {
			material = Material.POISONOUS_POTATO;
		}
		return new ItemStack(material);
	}
	
	public ItemStack getParentTool() {
		Material material = Material.getMaterial(maincfg.getString("Settings.parenttool.type"));
		if(material == null) {
			material = Material.SLIME_BALL;
		}
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', maincfg.getString("Settings.parenttool.name")));
		meta.setLore(Arrays.asList(maincfg.getString("Settings.parenttool.item-description").split("[|]")));
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCloneTool() {
		Material material = Material.getMaterial(maincfg.getString("Settings.clonetool.type"));
		if(material == null) {
			material = Material.STICK;
		}
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', maincfg.getString("Settings.clonetool.name")));
		meta.setLore(Arrays.asList(maincfg.getString("Settings.clonetool.item-description")));
		item.setItemMeta(meta);
		return item;
	}
}
