package nea.zachannam.vehicles.api.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import nea.zachannam.vehicles.api.vehicles.VehicleEntity;

public class PlayerArmorStandManipulate implements Listener {

	/**
	 * Called whenever a player clicks an armorstand and changes their armor
	 * 
	 * Used to stop players removing stuff from VehicleEntities.
	 * @param event
	 */
	@EventHandler
	public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		
		if(VehicleEntity.isVehicle(event.getRightClicked().getUniqueId())) { // checks if armorstand is a VehiclesArmorStand
			event.setCancelled(true); // sets cancelled to true
		}
		
	}
}
