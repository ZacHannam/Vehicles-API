package nea.zachannam.vehicles.api.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import nea.zachannam.vehicles.api.events.VehicleEvent;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;

public class InventoryClick implements Listener {
	
	/**
	 * Called whenever an entity clicks within an inventory
	 * Used to get when a player clicks within a vehicle's inventory
	 * @param event
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		VehicleEvent vehicleEvent = new VehicleEvent((Event) event); // logs event
		
		if(!(event.getWhoClicked() instanceof Player)) return; // checks that it was  a player that clicked returns if not a player
		
		if(vehicleEvent.getUser().inVehicleInventory()) { // checks if the user is in a vehicle inventory
			Vehicle vehicle = vehicleEvent.getUser().getVehicleWithOpenedInventory(); // gets the current vehicle inventory
			if(vehicle == null) return; // checks the vehicle is not null
			
			if(vehicle.hasComponent(ComponentName.INVENTORY.getName())) {
				((nea.zachannam.vehicles.api.vehicles.components.inventory.Inventory) vehicle.getFirstComponentByType(ComponentName.INVENTORY.getName())).onInventoryClick(event); // performs the inventory click event inside of the vehicle
			}
		}
	}
}
