package tld.sima.armorstand.inventories;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;

public class optionsMenuInventory {
	private Main plugin = Main.getPlugin(Main.class);
	
	public void openInventory(Player player, ArmorStand stand) {
		Inventory i = plugin.getServer().createInventory(null, 27, ChatColor.DARK_BLUE + "Armorstand GUI Options");
		
		boolean parent = plugin.getParentMap().containsKey(stand.getUniqueId());
		ItemStack setparent = new ItemStack(Material.ARMOR_STAND,1);
		setparent = plugin.createItem(setparent, ChatColor.WHITE + "Set Parent", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Parent status: " + parent));
		
		ItemStack position = new ItemStack(Material.STONE,1);
		position = plugin.createItem(position, ChatColor.WHITE + "Position", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current Position: " + ChatColor.WHITE + stand.getLocation().getX() + " " + stand.getLocation().getY() + " " + stand.getLocation().getZ() + " "));
		
		ItemStack clone = new ItemStack(Material.ARMOR_STAND,1);
		clone = plugin.createItem(clone, ChatColor.WHITE + "Clone Stand", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Get clone tool"));
		
		ItemStack delete = new ItemStack(Material.SKELETON_SKULL);
		delete = plugin.createItem(delete, ChatColor.WHITE + "Delete Stand", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Deletes Stand"));
		
		ItemStack back = new ItemStack(Material.BARRIER, 1);
		back = plugin.createItem(back, ChatColor.WHITE + "Back", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Return to main menu"));

				ItemStack toggleglow = new ItemStack(Material.GLOWSTONE,1);
		toggleglow = plugin.createItem(toggleglow, ChatColor.WHITE + "Toggle Glow", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current State: " + ChatColor.WHITE + stand.isGlowing()));

		if(!parent) {
			ItemStack togglesize = new ItemStack(Material.RED_MUSHROOM,1);
			ItemStack name = new ItemStack(Material.NAME_TAG,1);
			ItemStack invisible = new ItemStack(Material.POTION, 1);
			ItemStack togglearms = new ItemStack(Material.STICK, 1);
			ItemStack togglefire = new ItemStack(Material.FLINT_AND_STEEL,1);
			ItemStack rotation = new ItemStack(Material.OAK_WOOD,1);
			ItemStack togglebase = new ItemStack(Material.STONE_PRESSURE_PLATE,1);
			ItemStack togglegravity = new ItemStack(Material.APPLE,1);
			ItemStack movement = new ItemStack(Material.ARROW,1);

			movement = plugin.createItem(movement, ChatColor.WHITE + "Move Stand with Player", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Moves stand with respect to player."));

			togglesize = plugin.createItem(togglesize, ChatColor.WHITE + "Toggle Size", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current State: " + ChatColor.WHITE + stand.isSmall()));
			
			if (stand.isCustomNameVisible() == true){
				name = plugin.createItem(name, ChatColor.WHITE + "Name", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current name: " + stand.getName()));
			}else{
				name = plugin.createItem(name, ChatColor.WHITE + "Name", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current name: "));
			}

			invisible = plugin.createItem(invisible, ChatColor.WHITE + "Toggle Invisibility", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current visibility: " + ChatColor.WHITE +  stand.isVisible()));
			togglearms = plugin.createItem(togglearms, ChatColor.WHITE + "Toggle Arms", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current state: " + ChatColor.WHITE + stand.hasArms()));
			
			boolean onFire;
			if (stand.getFireTicks() == -1){ onFire = false;} else {onFire = true;}
			togglefire = plugin.createItem(togglefire, ChatColor.WHITE + "Toggle Fire",Arrays.asList( ChatColor.GRAY + "" + ChatColor.ITALIC + "Current State: "+ ChatColor.WHITE + onFire));
			
			rotation = plugin.createItem(rotation, ChatColor.WHITE + "Rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current Angle: " + ChatColor.WHITE + stand.getLocation().getYaw()));
			togglebase = plugin.createItem(togglebase, ChatColor.WHITE + "Toggle Base", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current State: " + ChatColor.WHITE + stand.hasBasePlate()));
			togglegravity = plugin.createItem(togglegravity, ChatColor.WHITE + "Toggle Gravity", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current State: " + ChatColor.WHITE + stand.hasGravity()));
			setparent = plugin.createItem(setparent, ChatColor.WHITE + "Set Parent", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Parent status: " + parent));
			ItemStack specialClone = new ItemStack(Material.ARMOR_STAND, 1);
			specialClone = plugin.createItem(specialClone, ChatColor.WHITE + "Special Clone", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Clones stand 2 blocks above this one."));
	
			i.setItem(0, togglegravity);
			i.setItem(1, togglesize);
			i.setItem(2, invisible);
			i.setItem(9, togglefire);
			i.setItem(10, togglebase);
			i.setItem(11, togglearms);
			i.setItem(18, toggleglow);
			i.setItem(19, name);

			i.setItem(5, rotation);
			i.setItem(14, position);
			i.setItem(23, movement);

			i.setItem(7, clone);
			i.setItem(8, specialClone);

			i.setItem(16, setparent);
			
			i.setItem(25, delete);
			i.setItem(26, back);
		} else {
			ItemStack disabled = new ItemStack(Material.BARRIER,1);
			disabled = plugin.createItem(disabled, ChatColor.RED + "Disabled due to parent status", Arrays.asList(ChatColor.RED + "Disabled due to parent status"));
			
			ItemStack setradius = new ItemStack(Material.STICK,1);
			setradius = plugin.createItem(setradius, ChatColor.WHITE + "Set Radius", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current Radius: " + ChatColor.WHITE + plugin.getParentMap().get(stand.getUniqueId())));


			i.setItem(0, disabled);
			i.setItem(1, disabled);
			i.setItem(2, disabled);
			i.setItem(9, disabled);
			i.setItem(10, disabled);
			i.setItem(11, disabled);
			i.setItem(18, toggleglow);
			i.setItem(19, disabled);

			i.setItem(5, disabled);
			i.setItem(14, position);
			i.setItem(23, disabled);

			i.setItem(7, clone);
			i.setItem(8, disabled);

			i.setItem(16, setparent);
			i.setItem(17, setradius);
			
			i.setItem(25, delete);
			i.setItem(26, back);
		}
		
		player.openInventory(i);
	}
}
