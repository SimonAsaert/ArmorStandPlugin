package tld.sima.armorstand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.ArmorStand;

public class API {
	private Main plugin = Main.getPlugin(Main.class);
	
	public ArmorStand getStand(UUID uuid) {
		return plugin.getPlayerData(uuid).getPairedStand();
	}
	
	public HashMap<UUID, Integer> getParentMap() {
		return plugin.getParentMap();
	}
	
	public HashMap<UUID, ArrayList<UUID>> getSmartParentMap(){
		return plugin.getSmartParent();
	}
}
