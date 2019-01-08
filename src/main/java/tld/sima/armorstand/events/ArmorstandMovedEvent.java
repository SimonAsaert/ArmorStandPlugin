package tld.sima.armorstand.events;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmorstandMovedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private UUID standUUID;
	private Location oldLoc;
	private Location newLoc;
	private boolean cancelled = false;
	private boolean isRotation;
	
	public ArmorstandMovedEvent(UUID standUUID, Location oldLoc, Location newLoc, boolean flag){
		this.standUUID = standUUID;
		this.oldLoc = oldLoc;
		this.newLoc = newLoc;
		this.isRotation = flag;
	}
	
	public ArmorstandMovedEvent(Location oldLoc, Entity entity, boolean flag) {
		this.standUUID = entity.getUniqueId();
		this.oldLoc = oldLoc;
		this.newLoc = entity.getLocation();
		this.isRotation = flag;
	}
	
	public ArmorstandMovedEvent(Entity entity, Location newLoc, boolean flag) {
		this.standUUID = entity.getUniqueId();
		this.oldLoc = entity.getLocation();
		this.newLoc = newLoc;
		this.isRotation = flag;
	}
	
	public UUID getStand() {
		return standUUID;
	}
	
	public Location getOldLocation() {
		return oldLoc;
	}
	
	public Location getNewLocation() {
		return newLoc;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public boolean getType() {
		return this.isRotation;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
