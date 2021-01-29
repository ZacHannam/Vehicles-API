package nea.zachannam.vehicles.api.events.player;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleEntity;

public class PlayerInteractAtEntity implements Listener {
	
	/**
	 * Called whenever a player clicks on an entity
	 * 
	 * Checking for a player clicking on an entity that is used as part of the vehicle
	 * @param event
	 */
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		
		if(!(event.getRightClicked() instanceof ArmorStand)) return; // checks if clicked
				
		if(VehicleEntity.isVehicle(event.getRightClicked())) { // checks if the entity is part of a vehicle
			
			Vehicle vehicle = VehicleEntity.getVehicleFromVehicleEntity(event.getRightClicked()); // sets vehicle to the vehicle in which the armorstand originates from
			
			if(vehicle!= null) { // checks if the vehicle is not null
				vehicle.onVehicleClick(event.getPlayer()); // calls the onVehicleClick procedure
			}
		}
	}

}
