package nea.zachannam.vehicles.api.events.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import nea.zachannam.vehicles.api.main.VehiclesAPI;

public class ChunkUnload implements Listener {
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		VehiclesAPI.getChunkManager().unloadChunk(event.getChunk());
	}

}
