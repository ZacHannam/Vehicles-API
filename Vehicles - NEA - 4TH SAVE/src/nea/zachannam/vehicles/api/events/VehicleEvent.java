package nea.zachannam.vehicles.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerEvent;

import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.user.User;

public class VehicleEvent {
	
	// transient variable used to store the event
	private transient Event event;
	
	/**
	 * returns the event that the player has performed
	 * @return
	 */
	public Event getEvent() {
		return this.event;
	}
	
	/**
	 * Used to set the event
	 * @param paramEvent
	 */
	private void setEvent(Event paramEvent) {
		this.event = paramEvent;
	}
	
	/**
	 * returns the user object of the user object that performed the action
	 * @return
	 */
	public User getUser() {
		if(this.getEvent() instanceof PlayerEvent) { // checks if the event is a player event, if so then it will involve a player
			return VehiclesAPI.getUserManager().getUser(((PlayerEvent) this.getEvent()).getPlayer().getUniqueId()); // if it is then it will return the user object of the player
		}
		if(this.getEvent() instanceof InventoryClickEvent) {
			return VehiclesAPI.getUserManager().getUser(((InventoryClickEvent) this.getEvent()).getWhoClicked().getUniqueId());
		}
		if(this.getEvent() instanceof InventoryCloseEvent) {
			return VehiclesAPI.getUserManager().getUser(((InventoryCloseEvent) this.getEvent()).getPlayer().getUniqueId());
		}
		return null; // if it is a normal event, then it will not have a player, therefore will return null
	}

	/**
	 * used to log the events and give methods used in each event
	 * @param paramEvent
	 */
	public VehicleEvent(Event paramEvent) {
		this.setEvent(paramEvent); // sets the event
	}
	
}
