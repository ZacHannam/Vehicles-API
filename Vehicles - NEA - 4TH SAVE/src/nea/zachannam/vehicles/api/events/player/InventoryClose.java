package nea.zachannam.vehicles.api.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import nea.zachannam.vehicles.api.events.VehicleEvent;

public class InventoryClose implements Listener {
	
	/**
	 * called whenever a player's inventory is closed
	 * we want to check when the player's vehicle inventory is closed.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		VehicleEvent vehicleEvent = new VehicleEvent((Event) event); // logs event
		
		if(vehicleEvent.getUser().inVehicleInventory()) { // checks if the player is in a vehicles inventory
			vehicleEvent.getUser().closeInventory(); // remove the player from the vehicles inventory
		}
	}

}
