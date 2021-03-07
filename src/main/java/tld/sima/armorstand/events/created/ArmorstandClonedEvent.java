package tld.sima.armorstand.events.created;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class ArmorstandClonedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final UUID playerUUID;
	private final UUID standUUID;
	private final UUID worldUUID;
	private final Vector loc;
	private boolean cancelled = false;

	public ArmorstandClonedEvent(Player player, ArmorStand stand, Vector loc, UUID worldUUID) {
		this.playerUUID = player.getUniqueId();
		this.standUUID = stand.getUniqueId();
		this.loc = loc;
		this.worldUUID = worldUUID;
	}
	
	public ArmorstandClonedEvent(UUID player, UUID stand, Vector loc, UUID worldUUID) {
		this.playerUUID = player;
		this.standUUID = stand;
		this.loc = loc;
		this.worldUUID = worldUUID;
	}
	
	public UUID getPlayer() {
		return playerUUID;
	}
	
	public UUID getStand() {
		return standUUID;
	}
	
	public Vector getVector() {
		return loc;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public UUID getWorldUUID() {
		return worldUUID;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(worldUUID);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
