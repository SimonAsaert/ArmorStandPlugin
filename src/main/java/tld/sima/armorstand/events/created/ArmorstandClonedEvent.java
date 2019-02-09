package tld.sima.armorstand.events.created;

import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class ArmorstandClonedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private UUID playerUUID;
	private UUID standUUID;
	private Vector loc;
	private boolean cancelled = false;

	public ArmorstandClonedEvent(Player player, ArmorStand stand, Vector loc) {
		this.playerUUID = player.getUniqueId();
		this.standUUID = stand.getUniqueId();
		this.loc = loc;
	}
	
	public ArmorstandClonedEvent(UUID player, UUID stand, Vector loc) {
		this.playerUUID = player;
		this.standUUID = stand;
		this.loc = loc;
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

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
