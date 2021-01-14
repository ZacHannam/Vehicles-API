package nea.zachannam.vehicles.api.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nea.zachannam.vehicles.api.events.VehicleEvent;
import nea.zachannam.vehicles.api.main.VehiclesAPI;

public class PlayerJoin implements Listener {
	
	/**
	 * Called whenever a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		@SuppressWarnings("unused")
		VehicleEvent vehicleEvent = new VehicleEvent((Event) event); // logs event
		
		if(event.getPlayer() == null) return; // Player Join can sometimes be fake, when another plugin add new Player Entities into the game, so its worth checking that they are actually a player
		
		VehiclesAPI.getUserManager().createUser(event.getPlayer()); // creates a User instance for the player.
	}
}
