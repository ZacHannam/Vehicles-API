package nea.zachannam.vehicles.api.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import lombok.Getter;
import nea.zachannam.vehicles.api.chunk.exceptions.ChunkIsLoadedException;
import nea.zachannam.vehicles.api.chunk.exceptions.VehicleHasNoLocationException;
import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.vehicles.Vehicle;

public class ChunkManager {
	
	@Getter
	private HashMap<String, UUID[]> vehicleChunkBuffer = new HashMap<String, UUID[]>(); // CHUNK ID; LIST OF UUIDS OF VEHICLES
	
	@Getter
	private HashMap<UUID, String> vehiclesInChunkInBuffer = new HashMap<UUID, String>(); // UUID, CHUNK ID

	public Chunk getChunkFromChunkID(String paramChunkID) {
		try {
			String[] chunkIDSplit = paramChunkID.split(";");
			World world = Bukkit.getServer().getWorld(chunkIDSplit[0]);
			
			int x = Integer.valueOf(chunkIDSplit[1]);
			int z = Integer.valueOf(chunkIDSplit[2]);
			
			return world.getChunkAt(x, z);
			
		} catch(Exception e) {
			return null;
		}
	}
	
	private String getChunkID(Location paramLocation) {
		return paramLocation.getWorld().getName() + ";" + String.valueOf(paramLocation.getBlockX() / 16) + ";" + String.valueOf(paramLocation.getBlockZ() / 16);
	}
	
	private String getChunkID(Chunk paramChunk) {
		return paramChunk.getWorld().getName() + ";" + String.valueOf(paramChunk.getX()) + ";" + String.valueOf(paramChunk.getZ());
	}
	
	public void addVehicleToBuffer(Vehicle paramVehicle) {
		if(paramVehicle.getLocation() == null) {
			throw new RuntimeException(new VehicleHasNoLocationException());
		}
		if(this.isChunkLoaded(paramVehicle.getLocation().toLocation())) {
			throw new RuntimeException(new ChunkIsLoadedException());
		}
		
		String chunkID = getChunkID(paramVehicle.getLocation().toLocation());
		
		if(this.getVehicleChunkBuffer().containsKey(chunkID)) {
			UUID[] listOfUUIDS = this.getVehicleChunkBuffer().get(chunkID);
			UUID[] newListOfUUIDS = new UUID[listOfUUIDS.length + 1];
			newListOfUUIDS[0] = paramVehicle.getUuid();
			for(int index = 1; index <= listOfUUIDS.length; index++) {
				newListOfUUIDS[index] = listOfUUIDS[index - 1];
			}
			
			this.getVehicleChunkBuffer().put(chunkID, newListOfUUIDS);
		} else {
			this.getVehicleChunkBuffer().put(chunkID, new UUID[] {paramVehicle.getUuid()});
		}
		
		this.getVehiclesInChunkInBuffer().put(paramVehicle.getUuid(), chunkID);
	}
	
	public void loadChunk(Chunk paramChunk) {
		String chunkID = getChunkID(paramChunk);
		if(this.getVehicleChunkBuffer().containsKey(chunkID)) {
			for(UUID vehicleUUID : this.getVehicleChunkBuffer().get(chunkID)) {
				Vehicle vehicle = VehiclesAPI.getVehicleManager().getVehicleFromUUID(vehicleUUID);
				vehicle.show();
				
				this.getVehiclesInChunkInBuffer().remove(vehicle.getUuid());
			}
		}
		this.getVehicleChunkBuffer().remove(chunkID);
	}
	
	public void unloadChunk(Chunk paramChunk) {
		String chunkID = getChunkID(paramChunk);
		
		List<UUID> vehiclesInChunk = new ArrayList<UUID>();
		
		for(Vehicle vehicle : VehiclesAPI.getVehicleManager().getVehicles().values()) {
			if(vehicle.isSpawned()) {
				if(vehicle.getLocation() != null && getChunkID(vehicle.getLocation().toLocation()).equals(chunkID)) {
					vehicle.hide();
					vehiclesInChunk.add(vehicle.getUuid());
					this.getVehiclesInChunkInBuffer().put(vehicle.getUuid(), chunkID);
				}
			}
		}
		
		UUID[] uuids = new UUID[vehiclesInChunk.size()];
		for(int index = 0; index < vehiclesInChunk.size(); index++) {
			uuids[index] = vehiclesInChunk.get(index);
			
		}
		this.getVehicleChunkBuffer().put(chunkID, uuids);
	}

	public void halt() {
		// TODO Auto-generated method stub
		
	}

	public boolean isChunkLoaded(Location paramLocation) {
		String locationChunkID = getChunkID(paramLocation);
		Chunk[] chunks = paramLocation.getWorld().getLoadedChunks();
		for(Chunk chunk : chunks) {
			if(locationChunkID.equals(getChunkID(chunk))) {
				Bukkit.getLogger().info("A");
				return true;
			}
		}
		Bukkit.getLogger().info("F");
		return false;
	}
}
