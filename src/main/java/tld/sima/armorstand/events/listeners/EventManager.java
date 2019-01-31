package tld.sima.armorstand.events.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.conversations.MovementConv;
import tld.sima.armorstand.conversations.NameConv;
import tld.sima.armorstand.conversations.RotationConv;
import tld.sima.armorstand.events.created.ArmorstandClonedEvent;
import tld.sima.armorstand.events.created.ArmorstandRemovedEvent;
import tld.sima.armorstand.events.created.ArmorstandSelectedEvent;
import tld.sima.armorstand.inventories.MainMenuInventory;
import tld.sima.armorstand.utils.Pair;
import tld.sima.armorstand.utils.ToolType;

public class EventManager implements Listener {
	
	Main plugin = Main.getPlugin(Main.class);

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Pair<Material, ToolType> pair = new Pair<Material, ToolType>(Material.AIR, ToolType.NONE);
		plugin.getPlayerCustomTool().put(event.getPlayer().getUniqueId(), pair);
	}
	
	@EventHandler
	public void leftClicked(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof ArmorStand) {
			event.setCancelled(true);
			if(event.getDamager() instanceof Player) {
				Player player = (Player) event.getDamager();
				ItemStack itemInHand = player.getInventory().getItemInMainHand();
				if(itemInHand == null || itemInHand.getType().equals(Material.AIR)) {
					return;
				}else if (itemInHand.isSimilar(plugin.getRemoveTool())) {
					ArmorStand stand = (ArmorStand)event.getEntity();
					UUID standUUID = stand.getUniqueId();
					ArmorstandRemovedEvent are = new ArmorstandRemovedEvent(standUUID);
					plugin.getServer().getPluginManager().callEvent(are);
					plugin.getParentMap().remove(standUUID);
					plugin.getSmartParent().remove(standUUID);
					
					for(UUID uuid : plugin.getSmartParent().keySet()) {
						if(plugin.getSmartParent().get(uuid).contains(standUUID)) {
							plugin.getSmartParent().get(uuid).remove(standUUID);
						}
					}
					
					stand.remove();
				}else if (itemInHand.getType().equals(plugin.getPlayerCustomTool().get(player.getUniqueId()).getLeft())) {
					ToolType type = plugin.getPlayerCustomTool().get(player.getUniqueId()).getRight();
					ArmorStand stand = (ArmorStand)event.getEntity();
					switch(type) {
					case NONE:
						break;
					case GRAVITY:
						stand.setGravity(!stand.hasGravity());
						break;
					case SIZE:
						stand.setSmall(!stand.isSmall());
						break;
					case INVISIBLE:
						stand.setVisible(!stand.isVisible());
						break;
					case FIRE:
						int i = stand.getFireTicks();
						if(i > 5) {
							stand.setFireTicks(Integer.MAX_VALUE);
						}else {
							stand.setFireTicks(0);
						}
						break;
					case BASE:
						stand.setBasePlate(!stand.hasBasePlate());
						break;
					case ARMS:
						stand.setArms(!stand.hasArms());
						break;
					case GLOW:
						stand.setGlowing(!stand.isGlowing());
						break;
					case NAME:
					{
						plugin.getStandMap().put(player.getUniqueId(), stand);
						ConversationFactory cf = new ConversationFactory(plugin);
						NameConv conversation = new NameConv();
						conversation.setData(player.getUniqueId());
						Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
						conv.begin();
						if (plugin.getConv().containsKey(player.getUniqueId())) {
							plugin.getConv().get(player.getUniqueId()).abandon();
						}
						plugin.getConv().put(player.getUniqueId(), conv);
					}
						break;
					case ROTATION:
					{
						plugin.getStandMap().put(player.getUniqueId(), stand);
						ConversationFactory cf = new ConversationFactory(plugin);
						RotationConv converstaion = new RotationConv();
						converstaion.setData(player.getUniqueId(), true, "BODY");
						Conversation conv = cf.withFirstPrompt(converstaion).withLocalEcho(true).buildConversation(player);
						conv.begin();
						if (plugin.getConv().containsKey(player.getUniqueId())) {
							plugin.getConv().get(player.getUniqueId()).abandon();
						}
						plugin.getConv().put(player.getUniqueId(), conv);
					}
						break;
					case MOVE:
					{
						plugin.getStandMap().put(player.getUniqueId(), stand);
						ConversationFactory cf = new ConversationFactory(plugin);
						MovementConv conversation = new MovementConv();
						conversation.setData(player.getUniqueId(), true);
						Conversation conv = cf.withFirstPrompt(conversation).withLocalEcho(true).buildConversation(player);
						conv.begin();
						if (plugin.getConv().containsKey(player.getUniqueId())) {
							plugin.getConv().get(player.getUniqueId()).abandon();
						}
						plugin.getConv().put(player.getUniqueId(), conv);
					}
					default:
						break;
					}
				}else if (player.getEquipment().getItemInMainHand().isSimilar(plugin.getSmartParentTool())) {
					ArmorStand parentStand = plugin.getStandMap().get(player.getUniqueId());
					if(parentStand == null) {
						return;
					}
					ArmorStand entity = (ArmorStand) event.getEntity();
					if(plugin.getSmartParent().containsKey(parentStand.getUniqueId())) {
						if(entity.getUniqueId().equals(parentStand.getUniqueId())) {
							return;
						}
						
						ArrayList<UUID> list = plugin.getSmartParent().get(parentStand.getUniqueId());
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
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof ArmorStand) {
			event.setCancelled(true);;
		}
	}
	
	@EventHandler
	public void onPlace(EntitySpawnEvent event) {
		if (event.getEntity() instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand) event.getEntity();
			stand.setHeadPose(stand.getHeadPose().setX(0).setY(0).setZ(0));
			stand.setBodyPose(stand.getBodyPose().setX(0).setY(0).setZ(0));
			stand.setCustomName("N/A");
			stand.setCustomNameVisible(false);
			stand.setGravity(false); 
		}
	}
	
	@EventHandler
	public void onStandDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof ArmorStand) {
			ArmorstandRemovedEvent e = new ArmorstandRemovedEvent(event.getEntity().getUniqueId());
			plugin.getServer().getPluginManager().callEvent(e);
		}
	}
	
	Set<UUID> delay = new HashSet<UUID>();
	
	@EventHandler(priority = EventPriority.LOW)
	public void onRightClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.hasPermission("armorstand.clone") && player.getInventory().getItemInMainHand().isSimilar(plugin.getCloneTool()) && !delay.contains(player.getUniqueId())) {
			delay.add(player.getUniqueId());
			new BukkitRunnable() {
				public void run() {
					delay.remove(player.getUniqueId());
				}
			}.runTaskLater(this.plugin, 10);
			if (plugin.getCloneMap().containsKey(player.getUniqueId())) {
				ArmorStand oldStand = plugin.getCloneMap().get(player.getUniqueId());

				Location loc = event.getClickedBlock().getLocation().clone();
				loc.add(0.5, 1, 0.5);
				
				ArmorstandClonedEvent e = new ArmorstandClonedEvent(player, oldStand, loc);
				plugin.getServer().getPluginManager().callEvent(e);
				
				if(!e.isCancelled()) {
					if (plugin.getParentMap().containsKey(oldStand.getUniqueId())) {
						int radius = plugin.getParentMap().get(oldStand.getUniqueId());
						Collection<Entity> entities = oldStand.getLocation().getWorld().getNearbyEntities(oldStand.getLocation(), 2.0, 2.0, 2.0);
						for (Entity entity : entities) {
							if (entity instanceof ArmorStand) {
								ArmorStand stands = (ArmorStand) entity;
								Vector offset = stands.getLocation().toVector().subtract(oldStand.getLocation().toVector());
								Location newloc = loc.clone();
								newloc.add(offset);
								newloc.setYaw(stands.getLocation().getYaw());
								newloc.setPitch(stands.getLocation().getPitch());
								ArmorStand newStand = (ArmorStand) newloc.getWorld().spawnEntity(newloc, EntityType.ARMOR_STAND);
								copyStandSettings(stands, newStand);

								if (plugin.getParentMap().containsKey(stands.getUniqueId())) {plugin.getParentMap().put(newStand.getUniqueId(), radius);}
							}
						}
					}else {
						loc.setYaw(oldStand.getLocation().getYaw());
						loc.setPitch(oldStand.getLocation().getPitch());
						ArmorStand newOStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
						copyStandSettings(oldStand, newOStand);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onRightClickStand(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		if (player == null) {
			return;
		}
		
		if (event.getRightClicked() instanceof ArmorStand) {
			event.setCancelled(true);
			
			if(player.hasPermission("stand.interact")) {
				ArmorStand stand = (ArmorStand) event.getRightClicked();
				
				ArmorstandSelectedEvent e = new ArmorstandSelectedEvent(player, stand);
				plugin.getServer().getPluginManager().callEvent(e);
				
				if(!e.isCancelled()) {
					MainMenuInventory i = new MainMenuInventory();
					i.newInventory(player, stand);
				}
				plugin.getStandMap().put(player.getUniqueId(), stand);
			}
		}
	}
	
	private void copyStandSettings(ArmorStand oldStand, ArmorStand newStand) {
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
		newStand.setArms(oldStand.hasArms());
	}
}
