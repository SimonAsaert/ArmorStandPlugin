package tld.sima.armorstand.inventories;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tld.sima.armorstand.Main;

public class MainMenuInventory {
	private Main plugin = Main.getPlugin(Main.class);

	public void newInventory(Player player, ArmorStand stand){
		Inventory i = plugin.getServer().createInventory(null, 54, ChatColor.DARK_BLUE + "Armorstand GUI");
		
		// Parent Options come first as a check to ensure it isn't in place for anything else.
		UUID standuuid = stand.getUniqueId();
		boolean parent = plugin.getParentMap().containsKey(standuuid) || plugin.getSmartParent().containsKey(standuuid);
		ItemStack setparent = new ItemStack(Material.ARMOR_STAND,1);
		setparent = plugin.createItem(setparent, ChatColor.WHITE + "Parent", Arrays.asList(""));

		ItemStack delete = new ItemStack(Material.SKELETON_SKULL);
		delete = plugin.createItem(delete, ChatColor.WHITE + "Delete Stand", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Deletes Stand"));
		
		ItemStack options = new ItemStack(Material.ANVIL);
		options = plugin.createItem(options, ChatColor.WHITE + "Options", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "More Armorstand Options"));

		ItemStack animations = new ItemStack(Material.REDSTONE);
		animations = plugin.createItem(animations, ChatColor.WHITE + "Animations", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Armorstand animations"));

		ItemStack position = new ItemStack(Material.STONE);
		position = plugin.createItem(position, ChatColor.WHITE + "Position", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Move armorstands in XYZ position"));

		ItemStack clone = new ItemStack(Material.ARMOR_STAND);
		clone = plugin.createItem(clone, ChatColor.WHITE + "Clone Stand", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Get clone tool"));
		
		ItemStack rotation = new ItemStack(Material.OAK_WOOD);
		rotation = plugin.createItem(rotation, ChatColor.WHITE + "Rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current Angle: " + ChatColor.WHITE + stand.getLocation().getYaw()));
		
		ItemStack protect = new ItemStack(Material.STONE_BUTTON);
		protect = plugin.createItem(protect, ChatColor.WHITE + "Protect stand", Arrays.asList(ChatColor.WHITE + "Protection status: " + ChatColor.GRAY + plugin.getProtectedStands().contains(standuuid)));
		
		if (parent) {
			// Further Options
			ItemStack disabled = new ItemStack(Material.BARRIER);		
			disabled = plugin.createItem(disabled, ChatColor.RED + "Disabled due to parent status", Arrays.asList(ChatColor.RED + "Disabled due to parent status"));

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
			i.setItem(42, protect);
			i.setItem(43, rotation);
			
			i.setItem(45, disabled);
			
			i.setItem(48, clone);
			i.setItem(50, disabled);
			i.setItem(52, position);
			i.setItem(53, delete);
			
		}else {
		
			// Further Options
			ItemStack movement = new ItemStack(Material.ARROW);
			movement = plugin.createItem(movement, ChatColor.WHITE + "Move Stand with Player", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Moves stand with respect to player."));
			
			// Segment rotation
			ItemStack headx = new ItemStack(Material.LEATHER_HELMET);
			ItemStack heady = new ItemStack(Material.LEATHER_HELMET);
			ItemStack headz = new ItemStack(Material.LEATHER_HELMET);

			ItemStack torsox = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack torsoy = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack torsoz = new ItemStack(Material.LEATHER_CHESTPLATE);
	
			ItemStack leftarmx = new ItemStack(Material.STICK);
			ItemStack leftarmy = new ItemStack(Material.STICK);
			ItemStack leftarmz = new ItemStack(Material.STICK);
	
			ItemStack rightarmx = new ItemStack(Material.STICK);
			ItemStack rightarmy = new ItemStack(Material.STICK);
			ItemStack rightarmz = new ItemStack(Material.STICK);
	
			ItemStack leftlegx = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack leftlegy = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack leftlegz = new ItemStack(Material.LEATHER_LEGGINGS);
	
			ItemStack rightlegx = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack rightlegy = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack rightlegz = new ItemStack(Material.LEATHER_LEGGINGS);
			
			headx = plugin.createItem(headx, ChatColor.WHITE + "Change Head x-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Xhead: " + ChatColor.WHITE + (double)(stand.getHeadPose().getX())*180/Math.PI));
			heady = plugin.createItem(heady, ChatColor.WHITE + "Change Head y-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Yhead: " + ChatColor.WHITE + (double)stand.getHeadPose().getY()*180/Math.PI));
			headz = plugin.createItem(headz, ChatColor.WHITE + "Change Head z-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Zhead: " + ChatColor.WHITE + (double)stand.getHeadPose().getZ()*180/Math.PI));
			
			torsox = plugin.createItem(torsox, ChatColor.WHITE + "Change Torso x-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Xtorso: " + ChatColor.WHITE + (double)stand.getBodyPose().getX()*180/Math.PI));
			torsoy = plugin.createItem(torsoy, ChatColor.WHITE + "Change Torso y-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Ytorso: " + ChatColor.WHITE + (double)stand.getBodyPose().getY()*180/Math.PI));
			torsoz = plugin.createItem(torsoz, ChatColor.WHITE + "Change Torso z-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of Ztorso: " + ChatColor.WHITE + (double)stand.getBodyPose().getZ()*180/Math.PI));
			
			leftarmx = plugin.createItem(leftarmx, ChatColor.WHITE + "Change Left Arm's x-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XLeft Arm: " + ChatColor.WHITE + (double)stand.getLeftArmPose().getX()*180/Math.PI));
			leftarmy = plugin.createItem(leftarmy, ChatColor.WHITE + "Change Left Arm's y-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YLeft Arm: " + ChatColor.WHITE + (double)stand.getLeftArmPose().getY()*180/Math.PI));
			leftarmz = plugin.createItem(leftarmz, ChatColor.WHITE + "Change Left Arm's z-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZLeft Arm: " + ChatColor.WHITE + (double)stand.getLeftArmPose().getZ()*180/Math.PI));
	
			rightarmx = plugin.createItem(rightarmx, ChatColor.WHITE + "Change Right Arm's x-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XRight Arm: " + ChatColor.WHITE + (double)stand.getRightArmPose().getX()*180/Math.PI));
			rightarmy = plugin.createItem(rightarmy, ChatColor.WHITE + "Change Right Arm's y-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YRight Arm: " + ChatColor.WHITE + (double)stand.getRightArmPose().getY()*180/Math.PI));
			rightarmz = plugin.createItem(rightarmz, ChatColor.WHITE + "Change Right Arm's z-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZRight Arm: " + ChatColor.WHITE + (double)stand.getRightArmPose().getZ()*180/Math.PI));
	
			leftlegx = plugin.createItem(leftlegx, ChatColor.WHITE + "Change Left Leg's x-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XLeft Leg: " + ChatColor.WHITE + (double)stand.getLeftLegPose().getX()*180/Math.PI));
			leftlegy = plugin.createItem(leftlegy, ChatColor.WHITE + "Change Left Leg's y-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YLeft Leg: " + ChatColor.WHITE + (double)stand.getLeftLegPose().getY()*180/Math.PI));
			leftlegz = plugin.createItem(leftlegz, ChatColor.WHITE + "Change Left Leg's z-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZLeft Leg: " + ChatColor.WHITE + (double)stand.getLeftLegPose().getZ()*180/Math.PI));
	
			rightlegx = plugin.createItem(rightlegx, ChatColor.WHITE + "Change Right Leg's x-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of XRight Leg: " + ChatColor.WHITE + (double)stand.getRightLegPose().getX()*180/Math.PI));
			rightlegy = plugin.createItem(rightlegy, ChatColor.WHITE + "Change Right Leg's y-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of YRight Leg: " + ChatColor.WHITE + (double)stand.getRightLegPose().getY()*180/Math.PI));
			rightlegz = plugin.createItem(rightlegz, ChatColor.WHITE + "Change Right Leg's z-rotation", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current angle of ZRight Leg: " + ChatColor.WHITE + (double)stand.getRightLegPose().getZ()*180/Math.PI));
	
			// Set Items in Positions
			ItemStack head = new ItemStack(Material.LEATHER_HELMET);
			ItemStack torso = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			ItemStack leftHand = new ItemStack(Material.STICK);
			ItemStack rightHand = new ItemStack(Material.STICK);
			
			head = plugin.createItem(head, ChatColor.WHITE + "Change Head", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the head"));
			torso = plugin.createItem(torso, ChatColor.WHITE + "Change Chest", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the chest"));
			legs = plugin.createItem(legs, ChatColor.WHITE + "Change Legs", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the legs"));
			boots = plugin.createItem(boots, ChatColor.WHITE + "Change Feet", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is worn on the feet"));
			leftHand = plugin.createItem(leftHand, ChatColor.WHITE + "Change Left Hand", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Use to change what item is in the left hand"));
			rightHand = plugin.createItem(rightHand, ChatColor.WHITE+ "Change Right Hand", Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC  + "Use to change what item is in the right hand"));
			
			ItemStack curhead;
			if(stand.getHelmet() == (null) || stand.getHelmet().getType().equals(Material.AIR)) {
				curhead = plugin.createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Head", Arrays.asList(""));
			}else {
				curhead = plugin.createItem(stand.getHelmet(), ChatColor.WHITE + "Change Head", Arrays.asList(""));
			}
			ItemStack curtorso;
			if(stand.getChestplate() == (null) || stand.getChestplate().getType().equals(Material.AIR)) {
				curtorso = plugin.createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Chest", Arrays.asList(""));
			}else {
				curtorso = plugin.createItem(stand.getChestplate(), ChatColor.WHITE + "Change Chest", Arrays.asList(""));
			}
			ItemStack curlegs;
			if(stand.getLeggings() == (null) || stand.getLeggings().getType().equals(Material.AIR)) {
				curlegs = plugin.createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Legs", Arrays.asList(""));
			}else {
				curlegs = plugin.createItem(stand.getLeggings(), ChatColor.WHITE + "Change Legs", Arrays.asList(""));
			}
			ItemStack curboots;
			if(stand.getBoots() == (null) || stand.getBoots().getType().equals(Material.AIR)) {
				curboots = plugin.createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Feet", Arrays.asList(""));
			}else {
				curboots = plugin.createItem(stand.getBoots(), ChatColor.WHITE + "Change Feet", Arrays.asList(""));
			}
			
			ItemStack curleftHand;
			if(stand.getEquipment().getItemInOffHand() == (null) || stand.getEquipment().getItemInOffHand().getType().equals(Material.AIR)) {
				curleftHand = plugin.createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Left Hand", Arrays.asList(""));
			}else {
				curleftHand = plugin.createItem(stand.getEquipment().getItemInOffHand(), ChatColor.WHITE + "Change Left Hand", Arrays.asList(""));
			}
			
			ItemStack currightHand = plugin.createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Right Hand", Arrays.asList(""));
			if(stand.getEquipment().getItemInMainHand() == (null) || stand.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {
				currightHand = plugin.createItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), ChatColor.WHITE + "Change Right Hand", Arrays.asList(""));
			}else {
				currightHand = plugin.createItem(stand.getEquipment().getItemInMainHand(), ChatColor.WHITE + "Change Right Hand", Arrays.asList(""));
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
			i.setItem(42, protect);
			i.setItem(43, rotation);
			// Go to parent item
			for(Entity entity : stand.getNearbyEntities(8, 8, 8)){
				UUID parentUUID = entity.getUniqueId();
				
				if(plugin.getSmartParent().containsKey(parentUUID) && plugin.getSmartParent().get(parentUUID).contains(standuuid)) {
					i.setItem(44, plugin.createItem(new ItemStack(Material.ITEM_FRAME), ChatColor.GREEN + "Go to parent stand", Arrays.asList("")));
				}
				
				if(plugin.getParentMap().containsKey(parentUUID)) {
					int distance = plugin.getParentMap().get(entity.getUniqueId());
					if(Math.max(Math.abs(stand.getLocation().getX() - entity.getLocation().getX()), 
						Math.abs(stand.getLocation().getZ() - entity.getLocation().getZ())) <= distance) {
						i.setItem(44, plugin.createItem(new ItemStack(Material.ITEM_FRAME), ChatColor.GREEN + "Go to parent stand", Arrays.asList("")));
					break;
					}
				}
			}
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
