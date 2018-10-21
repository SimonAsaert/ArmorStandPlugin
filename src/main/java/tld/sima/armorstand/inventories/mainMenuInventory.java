package tld.sima.armorstand.inventories;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tld.sima.armorstand.Main;

public class mainMenuInventory {
	private Main plugin = Main.getPlugin(Main.class);
	
	private ItemStack createItem(ItemStack item, String disName, String loreName) {
		
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(disName);
		ArrayList<String> itemL = new ArrayList<String>();
		itemL.add(loreName);
		itemM.setLore(itemL);
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(itemM);
		
		return item;
	}
	
	public void newInventory(Player player, ArmorStand stand){
		Inventory i = plugin.getServer().createInventory(null, 54, ChatColor.DARK_BLUE + "Armorstand GUI");
		
		// Parent Options come first as a check to ensure it isn't in place for anything else.
		boolean parent = plugin.getParentMap().containsKey(stand.getUniqueId());
		ItemStack setparent = new ItemStack(Material.ARMOR_STAND,1);
		setparent = createItem(setparent, ChatColor.WHITE + "Set Parent", ChatColor.GRAY + "" + ChatColor.ITALIC + "Parent status: " + parent);

		ItemStack delete = new ItemStack(Material.SKELETON_SKULL, 1);
		delete = createItem(delete, ChatColor.WHITE + "Delete Stand", ChatColor.GRAY + "" + ChatColor.ITALIC + "Deletes Stand");
		
		ItemStack options = new ItemStack(Material.ANVIL, 1);
		options = createItem(options, ChatColor.WHITE + "Options", ChatColor.GRAY + "" + ChatColor.ITALIC + "More Armorstand Options");

		ItemStack animations = new ItemStack(Material.REDSTONE, 1);
		animations = createItem(animations, ChatColor.WHITE + "Animations", ChatColor.GRAY + "" + ChatColor.ITALIC + "Armorstand animations");

		ItemStack position = new ItemStack(Material.STONE,1);
		position = createItem(position, ChatColor.WHITE + "Position", ChatColor.GRAY + "" + ChatColor.ITALIC + "Move armorstands in XYZ position");


		ItemStack clone = new ItemStack(Material.ARMOR_STAND,1);
		clone = createItem(clone, ChatColor.WHITE + "Clone Stand", ChatColor.GRAY + "" + ChatColor.ITALIC + "Get clone tool");
		
		
		if (parent) {
			// Further Options
			ItemStack disabled = new ItemStack(Material.BARRIER,1);		
			disabled = createItem(disabled, ChatColor.RED + "Disabled due to parent status", ChatColor.RED + "Disabled due to parent status");

			ItemStack setradius = new ItemStack(Material.STICK,1);
			setradius = createItem(setradius, ChatColor.WHITE + "Set Radius", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current Radius: " + ChatColor.WHITE + plugin.getParentMap().get(stand.getUniqueId()));

			i.setItem(0, disabled);
			
			i.setItem(3, disabled);
			i.setItem(4, disabled);
			i.setItem(5, disabled);
			i.setItem(6, disabled);
			i.setItem(7, disabled);
			i.setItem(8, disabled);
			
			i.setItem(9, disabled);

			i.setItem(12, disabled);
			i.setItem(13, disabled);
			i.setItem(14, disabled);
			i.setItem(15, disabled);
			i.setItem(16, disabled);
			i.setItem(17, disabled);
			
			i.setItem(18, disabled);

			i.setItem(21, disabled);
			i.setItem(22, disabled);
			i.setItem(23, disabled);
			i.setItem(24, disabled);
			i.setItem(25, disabled);
			i.setItem(26, disabled);
			
			i.setItem(27, disabled);
			
			i.setItem(36, disabled);
			
			i.setItem(39, options);
			i.setItem(40, animations);
			i.setItem(41, setparent);
			i.setItem(42, setradius);
			i.setItem(43, disabled);
			
			i.setItem(45, disabled);
			
			i.setItem(48, clone);
			i.setItem(50, disabled);
			i.setItem(52, position);
			i.setItem(53, delete);
			
		}else {
		
			// Further Options
			ItemStack rotation = new ItemStack(Material.OAK_PLANKS,1);
			ItemStack movement = new ItemStack(Material.ARROW,1);
			movement = createItem(movement, ChatColor.WHITE + "Move Stand with Player", ChatColor.GRAY + "" + ChatColor.ITALIC + "Moves stand with respect to player.");
			rotation = createItem(rotation, ChatColor.WHITE + "Rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current Angle: " + ChatColor.WHITE + stand.getLocation().getYaw());
			
			// Segment rotation
			ItemStack headx = new ItemStack(Material.LEATHER_HELMET, 1);
			ItemStack heady = new ItemStack(Material.LEATHER_HELMET, 1);
			ItemStack headz = new ItemStack(Material.LEATHER_HELMET, 1);

			ItemStack torsox = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			ItemStack torsoy = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			ItemStack torsoz = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
	
			ItemStack leftarmx = new ItemStack(Material.STICK, 1);
			ItemStack leftarmy = new ItemStack(Material.STICK, 1);
			ItemStack leftarmz = new ItemStack(Material.STICK, 1);
	
			ItemStack rightarmx = new ItemStack(Material.STICK, 1);
			ItemStack rightarmy = new ItemStack(Material.STICK, 1);
			ItemStack rightarmz = new ItemStack(Material.STICK, 1);
	
			ItemStack leftlegx = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			ItemStack leftlegy = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			ItemStack leftlegz = new ItemStack(Material.LEATHER_LEGGINGS, 1);
	
			ItemStack rightlegx = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			ItemStack rightlegy = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			ItemStack rightlegz = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			
			headx = createItem(headx, ChatColor.WHITE + "Change Head x-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Xhead: " + ChatColor.WHITE + (double)(stand.getHeadPose().getX())*180/Math.PI);
			heady = createItem(heady, ChatColor.WHITE + "Change Head y-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Yhead: " + ChatColor.WHITE + (double)stand.getHeadPose().getY()*180/Math.PI);
			headz = createItem(headz, ChatColor.WHITE + "Change Head z-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Zhead: " + ChatColor.WHITE + (double)stand.getHeadPose().getZ()*180/Math.PI);
			
			torsox = createItem(torsox, ChatColor.WHITE + "Change Torso x-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Xtorso: " + ChatColor.WHITE + (double)stand.getBodyPose().getX()*180/Math.PI);
			torsoy = createItem(torsoy, ChatColor.WHITE + "Change Torso y-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Ytorso: " + ChatColor.WHITE + (double)stand.getBodyPose().getY()*180/Math.PI);
			torsoz = createItem(torsoz, ChatColor.WHITE + "Change Torso z-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Ztorso: " + ChatColor.WHITE + (double)stand.getBodyPose().getZ()*180/Math.PI);
			
			leftarmx = createItem(leftarmx, ChatColor.WHITE + "Change Left Arm's x-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XLeft Arm: " + ChatColor.WHITE + (double)stand.getLeftArmPose().getX()*180/Math.PI);
			leftarmy = createItem(leftarmy, ChatColor.WHITE + "Change Left Arm's y-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YLeft Arm: " + ChatColor.WHITE + (double)stand.getLeftArmPose().getY()*180/Math.PI);
			leftarmz = createItem(leftarmz, ChatColor.WHITE + "Change Left Arm's z-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZLeft Arm: " + ChatColor.WHITE + (double)stand.getLeftArmPose().getZ()*180/Math.PI);
	
			rightarmx = createItem(rightarmx, ChatColor.WHITE + "Change Right Arm's x-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XRight Arm: " + ChatColor.WHITE + (double)stand.getRightArmPose().getX()*180/Math.PI);
			rightarmy = createItem(rightarmy, ChatColor.WHITE + "Change Right Arm's y-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YRight Arm: " + ChatColor.WHITE + (double)stand.getRightArmPose().getY()*180/Math.PI);
			rightarmz = createItem(rightarmz, ChatColor.WHITE + "Change Right Arm's z-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZRight Arm: " + ChatColor.WHITE + (double)stand.getRightArmPose().getZ()*180/Math.PI);
	
			leftlegx = createItem(leftlegx, ChatColor.WHITE + "Change Left Leg's x-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XLeft Leg: " + ChatColor.WHITE + (double)stand.getLeftLegPose().getX()*180/Math.PI);
			leftlegy = createItem(leftlegy, ChatColor.WHITE + "Change Left Leg's y-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YLeft Leg: " + ChatColor.WHITE + (double)stand.getLeftLegPose().getY()*180/Math.PI);
			leftlegz = createItem(leftlegz, ChatColor.WHITE + "Change Left Leg's z-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZLeft Leg: " + ChatColor.WHITE + (double)stand.getLeftLegPose().getZ()*180/Math.PI);
	
			rightlegx = createItem(rightlegx, ChatColor.WHITE + "Change Right Leg's x-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XRight Leg: " + ChatColor.WHITE + (double)stand.getRightLegPose().getX()*180/Math.PI);
			rightlegy = createItem(rightlegy, ChatColor.WHITE + "Change Right Leg's y-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YRight Leg: " + ChatColor.WHITE + (double)stand.getRightLegPose().getY()*180/Math.PI);
			rightlegz = createItem(rightlegz, ChatColor.WHITE + "Change Right Leg's z-rotation", ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZRight Leg: " + ChatColor.WHITE + (double)stand.getRightLegPose().getZ()*180/Math.PI);
	
			// Set Items in Positions
			ItemStack head = new ItemStack(Material.LEATHER_HELMET, 1);
			ItemStack torso = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			ItemStack leftHand = new ItemStack(Material.STICK, 1);
			ItemStack rightHand = new ItemStack(Material.STICK, 1);
			
			head = createItem(head, ChatColor.WHITE + "Change Head", ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the head");
			torso = createItem(torso, ChatColor.WHITE + "Change Chest", ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the chest");
			legs = createItem(legs, ChatColor.WHITE + "Change Legs", ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the legs");
			boots = createItem(boots, ChatColor.WHITE + "Change Feet", ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the feet");
			leftHand = createItem(leftHand, ChatColor.WHITE + "Change Left Hand", ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is in the left hand");
			rightHand = createItem(rightHand, ChatColor.WHITE+ "Change Right Hand", ChatColor.GRAY + "" + ChatColor.ITALIC  + "Use to change what item is in the right hand");
			
			ItemStack curhead;
			if(stand.getHelmet() == (null) || stand.getHelmet().equals(new ItemStack(Material.AIR))) {
				curhead = createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), ChatColor.WHITE + "Change Head", "");
			}else {
				curhead = createItem(stand.getHelmet(), ChatColor.WHITE + "Change Head", "");
			}
			ItemStack curtorso;
			if(stand.getChestplate() == (null) || stand.getChestplate().equals(new ItemStack(Material.AIR))) {
				curtorso = createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), ChatColor.WHITE + "Change Chest", "");
			}else {
				curtorso = createItem(stand.getChestplate(), ChatColor.WHITE + "Change Chest", "");
			}
			ItemStack curlegs;
			if(stand.getLeggings() == (null) || stand.getLeggings().equals(new ItemStack(Material.AIR))) {
				curlegs = createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), ChatColor.WHITE + "Change Legs", "");
			}else {
				curlegs = createItem(stand.getLeggings(), ChatColor.WHITE + "Change Legs", "");
			}
			ItemStack curboots;
			if(stand.getBoots() == (null) || stand.getBoots().equals(new ItemStack(Material.AIR))) {
				curboots = createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), ChatColor.WHITE + "Change Feet", "");
			}else {
				curboots = createItem(stand.getBoots(), ChatColor.WHITE + "Change Feet", "");
			}
			ItemStack curleftHand;
			if(stand.getEquipment().getItemInOffHand() == (null) || stand.getEquipment().getItemInOffHand().equals(new ItemStack(Material.AIR))) {
				curleftHand = createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), ChatColor.WHITE + "Change Left Hand", "");
			}else {
				curleftHand = createItem(stand.getEquipment().getItemInOffHand(), ChatColor.WHITE + "Change Left Hand", "");
			}
			ItemStack currightHand = createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Right Hand", "");
			if(stand.getEquipment().getItemInMainHand() == (null) || stand.getEquipment().getItemInMainHand().equals(new ItemStack(Material.AIR))) {
				currightHand = createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), ChatColor.WHITE + "Change Right Hand", "");
			}else {
				currightHand = createItem(stand.getEquipment().getItemInMainHand(), ChatColor.WHITE + "Change Right Hand", "");
			}
			// Items in locations
			i.setItem(0, head);
			i.setItem(1, curhead);
				
			i.setItem(3, headx);
			i.setItem(4, heady);
			i.setItem(5, headz);
			i.setItem(6, torsox);
			i.setItem(7, torsoy);
			i.setItem(8, torsoz);
			
			i.setItem(9, torso);
			i.setItem(10, curtorso);

			i.setItem(12, leftarmx);
			i.setItem(13, leftarmy);
			i.setItem(14, leftarmz);
			i.setItem(15, rightarmx);
			i.setItem(16, rightarmy);
			i.setItem(17, rightarmz);
			
			i.setItem(18, legs);
			i.setItem(19, curlegs);

			i.setItem(21, leftlegx);
			i.setItem(22, leftlegy);
			i.setItem(23, leftlegz);
			i.setItem(24, rightlegx);
			i.setItem(25, rightlegy);
			i.setItem(26, rightlegz);
			
			i.setItem(27, boots);
			i.setItem(28, curboots);
			
			i.setItem(36, leftHand);
			i.setItem(37, curleftHand);
			
			i.setItem(39, options);
			i.setItem(40, animations);
			i.setItem(41, setparent);
			i.setItem(43, rotation);
			
			i.setItem(45, rightHand);
			i.setItem(46, currightHand);
			i.setItem(48, clone);
			i.setItem(50, movement);
			i.setItem(52, position);
			i.setItem(53, delete);
		}
		
		player.openInventory(i);
	}
}
