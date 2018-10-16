package tld.sima.armorstand.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.inventories.mainMenuInventory;

public class EventManager implements Listener {
	
	Main main = Main.getPlugin(Main.class);
	ItemStack removetool = main.getRemoveTool();

	@EventHandler
	public void leftClicked(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof ArmorStand) {
			if(event.getDamager() instanceof Player) {
				Player player = (Player) event.getDamager();
				if (!(player.getInventory().getItemInMainHand().equals(removetool))) {
					event.setCancelled(true);
				}
			}
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
	
	Set<UUID> delay = new HashSet<UUID>();
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

			ItemStack tool = new ItemStack(Material.STICK);
			ItemMeta toolMeta = tool.getItemMeta();
			toolMeta.setDisplayName(ChatColor.GREEN + "Clone tool");
			tool.setItemMeta(toolMeta);
			if(player.getInventory().getItemInMainHand().isSimilar(tool) && !delay.contains(player.getUniqueId())) {
				delay.add(player.getUniqueId());
				new BukkitRunnable() {
					public void run() {
						delay.remove(player.getUniqueId());
					}
				}.runTaskLater(this.main, 5);
				if (main.getCloneMap().containsKey(player.getUniqueId())) {
					ArmorStand oldStand = main.getCloneMap().get(player.getUniqueId());
					Location loc = event.getClickedBlock().getLocation().clone();
					loc.add(0.5, 1, 0.5);
					
					if (main.getParentMap().containsKey(oldStand.getUniqueId())) {
						player.sendMessage("Is parent");
						int radius = main.getParentMap().get(oldStand.getUniqueId());
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

								if (main.getParentMap().containsKey(stands.getUniqueId())) {main.getParentMap().put(newStand.getUniqueId(), radius);}
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
	
	@EventHandler
	public void onRightClickStand(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		if (player == null) {
			return;
		}
		
		if (event.getRightClicked() instanceof ArmorStand) {
			event.setCancelled(true);
			if (player.hasPermission("stand.interact")) {
				mainMenuInventory i = new mainMenuInventory();
				ArmorStand stand = (ArmorStand) event.getRightClicked();
				i.newInventory(player, stand);
				main.getStandMap().put(player.getUniqueId(), stand);
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
