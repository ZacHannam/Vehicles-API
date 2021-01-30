package nea.zachannam.vehicles.api.chunk.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import nea.zachannam.vehicles.api.main.VehiclesAPI;

public class ChunkLoad implements Listener {

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		VehiclesAPI.getChunkManager().loadChunk(event.getChunk());
	}
	
}
