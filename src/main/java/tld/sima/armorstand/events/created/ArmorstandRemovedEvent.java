package tld.sima.armorstand.events.created;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmorstandRemovedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private UUID standUUID;

	public ArmorstandRemovedEvent(UUID standUUID) {
		this.standUUID = standUUID;
	}
	
	public UUID getStand() {
		return standUUID;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
