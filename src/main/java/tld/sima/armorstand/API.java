package tld.sima.armorstand;

import java.util.*;

import org.bukkit.entity.ArmorStand;

public class API {
	private final Main plugin = Main.getPlugin(Main.class);
	
	public ArmorStand getStand(UUID uuid) {
		return plugin.getPlayerData(uuid).getPairedStand();
	}
	
	public Map<UUID, Integer> getParentMap() {
		return plugin.getParentMap();
	}
	
	public Map<UUID, List<UUID>> getSmartParentMap(){
		return plugin.getSmartParent();
	}
}
