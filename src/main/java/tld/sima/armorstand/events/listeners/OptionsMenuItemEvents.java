package tld.sima.armorstand.events.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.conversations.MoveStandToPlayerConv;
import tld.sima.armorstand.conversations.MovementConv;
import tld.sima.armorstand.conversations.NameConv;
import tld.sima.armorstand.conversations.RadiusConv;
import tld.sima.armorstand.conversations.RotationConv;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.inventories.OptionsMenuInventory;
import tld.sima.armorstand.utils.Pair;

public class OptionsMenuItemEvents {
	public final static Main plugin = Main.getPlugin(Main.class);
	
	public static void parseItem(String itemName, Player player, ArmorStand stand) {
		if (itemName.contains("Back")) {
			ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, stand);
			plugin.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				MainMenuInventory i = new MainMenuInventory();
				i.newInventory(player, stand);
			}
			return;
		}else if (itemName.contains("Delete Stand")) {
			if (plugin.getParentMap().containsKey(stand.getUniqueId())){
				plugin.getParentMap().remove(stand.getUniqueId());
			}
			player.closeInventory();
			stand.remove();
			player.sendMessage(ChatColor.GOLD + "Stand removed");
		}else if (itemName.contains("Position")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			MovementConv conversation = new MovementConv();
			conversation.setData(player.getUniqueId(), stand.getUniqueId(), false);
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
		}else if (itemName.contains("Rotation")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			RotationConv converstaion = new RotationConv();
			converstaion.setData(player.getUniqueId(), stand.getUniqueId(), false, "BODY");
			Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
		}else if (itemName.contains("Name")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			NameConv conversation = new NameConv();
			conversation.setData(player.getUniqueId(), stand.getUniqueId());
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
		}else if (itemName.contains("Set Radius")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			RadiusConv conversation = new RadiusConv();
			conversation.setData(player.getUniqueId(), stand.getUniqueId(), false);
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
		}else if (itemName.contains("Move Stand with Player")) {
			player.closeInventory();
			ConversationFactory cf = new ConversationFactory(plugin);
			MoveStandToPlayerConv conversation = new MoveStandToPlayerConv();
			conversation.setData(player.getUniqueId(), stand.getUniqueId(), false);
			Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
			conv.begin();
			plugin.replaceConversation(player.getUniqueId(), conv);
			
		}else if (itemName.contains("Clone Stand")) {
			ItemStack tool = new ItemStack(Material.STICK);
			ItemMeta toolMeta = tool.getItemMeta();
			toolMeta.setDisplayName(ChatColor.GREEN + "Clone tool");
			tool.setItemMeta(toolMeta);
			if (!player.getInventory().contains(tool)) {
				player.getInventory().addItem(tool);
			}
			plugin.setClonedStand(player.getUniqueId(), stand);
			player.closeInventory();
		}else if (itemName.contains("Special Clone")) {
			Location loc = stand.getLocation().clone();
			loc.add(0, 1, 0);
			ArmorStand newStand = (ArmorStand) Bukkit.getWorld(stand.getWorld().getUID()).spawnEntity(loc, EntityType.ARMOR_STAND);
			copyStandSettings(stand, newStand);
			player.sendMessage(ChatColor.GOLD + "Stand cloned");
		}

		OptionsMenuInventory i = new OptionsMenuInventory();
		if (itemName.contains("Toggle Glow")) {
			boolean flag = false;
			for (Pair<ArmorStand, Boolean> pair : plugin.getVisualStand().getPairs()){
				if (pair.getLeft() != null && pair.getLeft().getUniqueId().equals(stand.getUniqueId())){
					flag = true;
					pair.setRight(!pair.getRight());
					break;
				}
			}

			if (!flag) {
				stand.setGlowing(!stand.isGlowing());
			}
			i.openInventory(player, stand);
		}else if (itemName.contains("Toggle Size")){
			stand.setSmall(!stand.isSmall());
			i.openInventory(player, stand);
		}else if (itemName.contains("Toggle Invisibility")) {
			stand.setVisible(!stand.isVisible());
			i.openInventory(player, stand);
		}else if (itemName.contains("Toggle Fire")) {
			int fireTicks = stand.getFireTicks();
			if (fireTicks == -1 || fireTicks == 0) {
				stand.setFireTicks(2147483645);
				stand.setInvulnerable(true);
			}else {
				stand.setFireTicks(-1);
			}
			i.openInventory(player, stand);
		}else if (itemName.contains("Toggle Base")) {
			stand.setBasePlate(!stand.hasBasePlate());
			i.openInventory(player, stand);
		}else if (itemName.contains("Toggle Gravity")) {
			stand.setGravity(!stand.hasGravity());
			i.openInventory(player, stand);
		}else if (itemName.contains("Toggle Arms")) {
			stand.setArms(!stand.hasArms());
			i.openInventory(player, stand);
		}else if (itemName.contains("Set Parent")) {
			UUID uuid = stand.getUniqueId();
			if (!plugin.getParentMap().containsKey(uuid)) {
				int radius = 1;
				plugin.getParentMap().put(uuid, radius);
				
				stand.setVisible(false);
				stand.setCustomNameVisible(false);
				stand.setCustomName("N/A");

				player.getInventory().addItem(stand.getEquipment().getHelmet());
				player.getInventory().addItem(stand.getEquipment().getChestplate());
				player.getInventory().addItem(stand.getEquipment().getLeggings());
				player.getInventory().addItem(stand.getEquipment().getBoots());
				player.getInventory().addItem(stand.getEquipment().getItemInMainHand());
				player.getInventory().addItem(stand.getEquipment().getItemInOffHand());

				stand.getEquipment().setHelmet(new ItemStack(Material.AIR));
				stand.getEquipment().setChestplate(new ItemStack(Material.AIR));
				stand.getEquipment().setLeggings(new ItemStack(Material.AIR));
				stand.getEquipment().setBoots(new ItemStack(Material.AIR));
				stand.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
				stand.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
				
				stand.setGravity(false);
				stand.setBasePlate(false);
				stand.setArms(false);
				stand.setSmall(false);
			}else {
				stand.setVisible(true);
				plugin.getParentMap().remove(uuid);
			}
			i.openInventory(player, stand);
		}
	}	
	
	private static void copyStandSettings(ArmorStand oldStand, ArmorStand newStand) {
		newStand.setBasePlate(oldStand.hasBasePlate());
		
		newStand.setLeftLegPose(oldStand.getLeftLegPose());
		newStand.setRightLegPose(oldStand.getRightLegPose());
		newStand.setLeftArmPose(oldStand.getLeftArmPose());
		newStand.setRightArmPose(oldStand.getRightArmPose());
		newStand.setBodyPose(oldStand.getBodyPose());
		newStand.setHeadPose(oldStand.getHeadPose());
		
		newStand.setCustomName(oldStand.getCustomName());
		if (!newStand.getCustomName().equals("N/A")) {
			newStand.setCustomNameVisible(true);
		}else {
			newStand.setCustomNameVisible(false);
		}
		newStand.getEquipment().setItemInOffHand(oldStand.getEquipment().getItemInOffHand());
		newStand.getEquipment().setItemInMainHand(oldStand.getEquipment().getItemInMainHand());
		newStand.getEquipment().setBoots(oldStand.getEquipment().getBoots());
		newStand.getEquipment().setLeggings(oldStand.getEquipment().getLeggings());
		newStand.getEquipment().setChestplate(oldStand.getEquipment().getChestplate());
		newStand.getEquipment().setHelmet(oldStand.getEquipment().getHelmet());
		
		newStand.setFireTicks(oldStand.getFireTicks());
		newStand.setGlowing(oldStand.isGlowing());
		newStand.setGravity(oldStand.hasGravity());
		newStand.setSmall(oldStand.isSmall());
		newStand.setVisible(oldStand.isVisible());
	}
}
