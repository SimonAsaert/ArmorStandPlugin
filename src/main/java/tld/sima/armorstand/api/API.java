package tld.sima.armorstand.api;

import java.util.UUID;

import org.bukkit.entity.ArmorStand;

import tld.sima.armorstand.Main;

public class API {
	private Main plugin = Main.getPlugin(Main.class);
	
	public ArmorStand getStand(UUID uuid) {
		return plugin.getStandMap().get(uuid);
	}
	
}
