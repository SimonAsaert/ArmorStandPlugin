package tld.sima.armorstand.events.listeners;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import tld.sima.armorstand.Main;
import tld.sima.armorstand.conversations.MovementConv;
import tld.sima.armorstand.conversations.NameConv;
import tld.sima.armorstand.conversations.RotationConv;
import tld.sima.armorstand.events.created.ArmorstandClonedEvent;
import tld.sima.armorstand.events.created.ArmorstandRemovedEvent;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.utils.CloneClass;
import tld.sima.armorstand.utils.ToolType;

public class EventManager implements Listener {
	
	private final Main plugin = Main.getPlugin(Main.class);

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		plugin.setupPlayerData(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void leftClicked(EntityDamageByEntityEvent event) {
		if(		event.getEntity() instanceof ArmorStand && 
				event.getDamager() instanceof Player && 
				(event.getDamager().hasPermission("Armorstand.atool") || event.getDamager().hasPermission("Armorstand.break") || 
				 event.getDamager().hasPermission("Armorstand.smartparenttool") ||
				 event.getDamager().isOp())) {
			Player player = (Player) event.getDamager();
			
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if (	itemInHand.isSimilar(plugin.getRemoveTool()) && 
					(player.hasPermission("Armorstand.break") || event.getDamager().isOp())) {
				event.setCancelled(true);
				ArmorStand stand = (ArmorStand)event.getEntity();
				UUID standUUID = stand.getUniqueId();
				ArmorstandRemovedEvent are = new ArmorstandRemovedEvent(standUUID);
				plugin.getServer().getPluginManager().callEvent(are);
				plugin.getParentMap().remove(standUUID);
				plugin.getSmartParent().remove(standUUID);
				
				for(UUID uuid : plugin.getSmartParent().keySet()) {
					plugin.getSmartParent().get(uuid).remove(standUUID);
				}
				
				stand.remove();
				return;
			}else if (plugin.isArmorstandCustomTool(player.getUniqueId(), itemInHand) &&
					(player.hasPermission("Armorstand.atool") || event.getDamager().isOp())) {
				event.setCancelled(true);
				ToolType type = plugin.getArmorstandToolType(player.getUniqueId());
				ArmorStand mainStand = (ArmorStand)event.getEntity();
				List<ArmorStand> affectedLists = new ArrayList<ArmorStand>();
				affectedLists.add(mainStand);
				double fuzzyRadius = plugin.getFuzzyToolRadius(player.getUniqueId());
				if (fuzzyRadius > 0.1){
					for (Entity entity : mainStand.getNearbyEntities(fuzzyRadius, fuzzyRadius*2, fuzzyRadius)){
						if (entity instanceof ArmorStand){
							affectedLists.add((ArmorStand) entity);
						}
					}
				}
				boolean flag;
				switch(type) {
					case GRAVITY:
						flag = !mainStand.hasGravity();
						for (ArmorStand stand : affectedLists) {
							stand.setGravity(flag);
						}
						break;
					case SIZE:
						flag = !mainStand.isSmall();
						for (ArmorStand stand : affectedLists){
							stand.setSmall(flag);
						}
						break;
					case INVISIBLE:
						flag = !mainStand.isVisible();
						for (ArmorStand stand : affectedLists){
							stand.setVisible(flag);
						}
						break;
					case FIRE:
						int fireTicks = mainStand.getFireTicks();
						flag = (fireTicks == -1 || fireTicks == 0);

						for (ArmorStand stand : affectedLists){
							if (flag){
								stand.setFireTicks(2147483645);
								stand.setInvulnerable(true);
							}else{
								stand.setFireTicks(-1);
							}
						}
						break;
					case BASE:
						flag = !mainStand.hasBasePlate();
						for (ArmorStand stand : affectedLists){
							stand.setBasePlate(flag);
						}
						break;
					case ARMS:
						flag = !mainStand.hasArms();
						for (ArmorStand stand : affectedLists){
							stand.setArms(flag);
						}
						break;
					case GLOW:
						flag = !mainStand.isGlowing();
						for (ArmorStand stand : affectedLists){
							stand.setGlowing(flag);
						}
						break;
					case NAME:
					{
						plugin.setPairedStand(player.getUniqueId(), mainStand);
						ConversationFactory cf = new ConversationFactory(plugin);
						NameConv conversation = new NameConv();
						conversation.setData(player.getUniqueId(), mainStand.getUniqueId());
						Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
						conv.begin();
						plugin.replaceConversation(player.getUniqueId(), conv);
					}
						break;
					case ROTATION:
					{
						plugin.setPairedStand(player.getUniqueId(), mainStand);
						ConversationFactory cf = new ConversationFactory(plugin);
						RotationConv conversation = new RotationConv();
						conversation.setData(player.getUniqueId(), mainStand.getUniqueId(), true, "BODY");
						Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
						conv.begin();
						plugin.replaceConversation(player.getUniqueId(), conv);
					}
						break;
					case MOVE:
					{
						plugin.setPairedStand(player.getUniqueId(), mainStand);
						ConversationFactory cf = new ConversationFactory(plugin);
						MovementConv conversation = new MovementConv();
						conversation.setData(player.getUniqueId(), mainStand.getUniqueId(), true);
						Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
						conv.begin();
						plugin.replaceConversation(player.getUniqueId(), conv);
					}
						break;
					default:
						break;
					}
				return;
			}else if (itemInHand.isSimilar(plugin.getSmartParentTool()) &&
					(player.hasPermission("Armorstand.smartparenttool") || event.getDamager().isOp())) {
				event.setCancelled(true);
				ArmorStand parentStand = plugin.getPairedStand(player.getUniqueId());
				if(parentStand == null) {
					return;
				}
				ArmorStand entity = (ArmorStand) event.getEntity();
				if(plugin.getSmartParent().containsKey(parentStand.getUniqueId())) {
					if(entity.getUniqueId().equals(parentStand.getUniqueId())) {
						return;
					}
					
					List<UUID> list = plugin.getSmartParent().get(parentStand.getUniqueId());
					if(list.contains(entity.getUniqueId())) {
						list.remove(entity.getUniqueId());
						plugin.getSmartParent().put(parentStand.getUniqueId(), list);
						player.sendMessage(ChatColor.WHITE + "Stand " + ChatColor.RED + "removed" + ChatColor.WHITE + " from parent map");
					}else {
						list.add(entity.getUniqueId());
						plugin.getSmartParent().put(parentStand.getUniqueId(), list);
						PotionEffectType type = PotionEffectType.GLOWING;
						PotionEffect potion = new PotionEffect(type, 60, 1);
						entity.addPotionEffect(potion);
						player.sendMessage(ChatColor.WHITE + "Stand " + ChatColor.GREEN + "added" + ChatColor.WHITE + " to parent map");
					}
				}
				return;
			}
		}
		
		if(plugin.getProtectedStands().contains(event.getEntity().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof ArmorStand && plugin.getProtectedStands().contains(event.getEntity().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onStandDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof ArmorStand) {
			ArmorstandRemovedEvent e = new ArmorstandRemovedEvent(event.getEntity().getUniqueId());
			plugin.getServer().getPluginManager().callEvent(e);
		}
	}
	
	@EventHandler
	public void onPlace(EntitySpawnEvent event) {
		if (event.getEntity() instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand) event.getEntity();
			stand.setHeadPose(stand.getHeadPose().setX(0).setY(0).setZ(0));
			stand.setBodyPose(stand.getBodyPose().setX(0).setY(0).setZ(0));
			stand.setCustomNameVisible(false);
			stand.setGravity(false);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onRightClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand() != null &&
				event.getHand().equals(EquipmentSlot.HAND) && player.hasPermission("armorstand.clone") &&
				player.getInventory().getItemInMainHand().isSimilar(plugin.getCloneTool())) {
			if (plugin.getClonedStand(player.getUniqueId()) != null) {
				ArmorStand oldStand = plugin.getClonedStand(player.getUniqueId());

				Location loc = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);
				
				Vector delta = loc.clone().toVector().subtract(oldStand.getLocation().clone().toVector());
				UUID worldUUID = event.getClickedBlock().getWorld().getUID();
				
				ArmorstandClonedEvent e = new ArmorstandClonedEvent(player, oldStand, delta, worldUUID);
				plugin.getServer().getPluginManager().callEvent(e);
				
				if(!e.isCancelled()) {
					CloneClass cc = new CloneClass();
					cc.CloneStand(oldStand, delta, worldUUID);
					player.sendMessage(ChatColor.GREEN + "Armorstand(s) cloned!");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onRightClickStand(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked() instanceof ArmorStand) {
			
			if(player.hasPermission("stand.interact") || player.hasPermission("armorstand.interact")) {
				event.setCancelled(true);
				ArmorStand stand = (ArmorStand) event.getRightClicked();
				
				ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, stand);
				plugin.getServer().getPluginManager().callEvent(e);
				
				if(!e.isCancelled()) {
					MainMenuInventory i = new MainMenuInventory();
					i.newInventory(player, stand);
				}
				plugin.setPairedStand(player.getUniqueId(), stand);
			}else if(plugin.getProtectedStands().contains(event.getRightClicked().getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
}
