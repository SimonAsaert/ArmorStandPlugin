package tld.sima.armorstand.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import tld.sima.armorstand.Main;

public class CloneClass {
	
	private Main plugin = Main.getPlugin(Main.class);
	private HashSet<UUID> parentList = new HashSet<UUID>();
	
	public UUID CloneStand(ArmorStand stand, Vector delta, UUID worldUUID) {
		UUID uuid = stand.getUniqueId();
		Location newloc = stand.getLocation().clone().add(delta);
		newloc.setWorld(Bukkit.getWorld(worldUUID));
		ArmorStand newStand = (ArmorStand) newloc.getWorld().spawnEntity(newloc, EntityType.ARMOR_STAND);
		copyStandSettings(stand, newStand);
		
		if(plugin.getSmartParent().containsKey(uuid)) {
			parentList.add(uuid);
			Collection<UUID> uuids = plugin.getSmartParent().get(uuid);
			ArrayList<UUID> newList = new ArrayList<UUID>();
			for(UUID childuuid : uuids) {
				if(parentList.contains(childuuid)) {
					continue;
				}
				Entity entity = Bukkit.getEntity(childuuid);
				if(entity != null) {
					newList.add(CloneStand((ArmorStand) entity, delta, worldUUID));
				}
			}
			plugin.getSmartParent().put(newStand.getUniqueId(), newList);
		}else if (plugin.getParentMap().containsKey(uuid)) {
			parentList.add(uuid);
			double radius = plugin.getParentMap().get(uuid);
			Collection<Entity> entities = stand.getNearbyEntities(radius, radius, radius);
			for(Entity child : entities) {
				if(parentList.contains(child.getUniqueId())) {
					continue;
				}
				
				if(child.getType().equals(EntityType.ARMOR_STAND)) {
					CloneStand((ArmorStand)child, delta, worldUUID);
				}
			}
			plugin.getParentMap().put(newStand.getUniqueId(), (int) radius);
		}
		return newStand.getUniqueId();
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
