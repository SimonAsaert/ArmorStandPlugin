package tld.sima.armorstand.events.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ArmorStand;
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
import tld.sima.armorstand.utils.CloneClass;
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
						int fireTicks = stand.getFireTicks();
						if(fireTicks == -1 || fireTicks == 0) {
							stand.setFireTicks(2147483645);
							stand.setInvulnerable(true);
						}else {
							stand.setFireTicks(-1);
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
				
				Vector delta = loc.clone().toVector().subtract(oldStand.getLocation().clone().toVector());
				
				ArmorstandClonedEvent e = new ArmorstandClonedEvent(player, oldStand, delta);
				plugin.getServer().getPluginManager().callEvent(e);
				
				if(!e.isCancelled()) {
					CloneClass cc = new CloneClass();
					
					cc.CloneStand(oldStand, delta);
					player.sendMessage(ChatColor.GREEN + "Armorstand(s) cloned!");
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
}
