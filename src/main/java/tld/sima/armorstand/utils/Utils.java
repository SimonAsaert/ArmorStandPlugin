package tld.sima.armorstand.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import tld.sima.armorstand.Main;

public class Utils {
	
	private static Main plugin = Main.getPlugin(Main.class);
	
	public static List<UUID> collectAllChildStands(UUID parent, List<UUID> currentList){
		currentList.add(parent);
		
		if(plugin.getSmartParent().containsKey(parent)) {
			for(UUID child : plugin.getSmartParent().get(parent)) {
				if(!currentList.contains(child)) {
					collectAllChildStands(child, currentList);
				}
			}
		}else if (plugin.getParentMap().containsKey(parent)) {
			int radius = plugin.getParentMap().get(parent);
			ArrayList<Entity> entities = (ArrayList<Entity>)Bukkit.getEntity(parent).getNearbyEntities(radius, radius, radius);
			for (Entity child : entities) {
				if (child instanceof ArmorStand && !currentList.contains(child.getUniqueId())) {
					collectAllChildStands(child.getUniqueId(), currentList);
				}
			}
		}
		
		return currentList;
	}
	
	// Move without rotation. Assuming all stands were already collected previously
	public static void moveAllChildStands(List<UUID> entityList, Vector movement, UUID world) {
		ArrayList<Chunk> chunksToUnload = new ArrayList<Chunk>();
		for(UUID uuid : entityList) {
			Entity entity = Bukkit.getEntity(uuid);
			if(entity != null && entity instanceof ArmorStand) {
				Location loc = entity.getLocation();
				if(!loc.getChunk().isLoaded()) {
					loc.getChunk().load();
					chunksToUnload.add(loc.getChunk());
				}
				loc.setWorld(Bukkit.getWorld(world));
				loc.add(movement);
				entity.teleport(loc);
			}
		}
		for(Chunk chunk : chunksToUnload) {
			chunk.unload();
		}
	}
}
