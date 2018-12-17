package tld.sima.armorstand.events;

import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmorstandClonedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private UUID playerUUID;
	private UUID standUUID;
	private boolean cancelled = false;

	public ArmorstandClonedEvent(Player player, ArmorStand stand) {
		this.playerUUID = player.getUniqueId();
		this.standUUID = stand.getUniqueId();
	}
	
	public ArmorstandClonedEvent(UUID player, UUID stand) {
		this.playerUUID = player;
		this.standUUID = stand;
	}
	
	public UUID getPlayer() {
		return playerUUID;
	}
	
	public UUID getStand() {
		return standUUID;
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
