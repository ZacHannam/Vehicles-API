package nea.zachannam.vehicles.api.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.chunk.exceptions.ChunkIsLoadedException;
import nea.zachannam.vehicles.api.chunk.exceptions.VehicleHasNoLocationException;
import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.vehicles.Vehicle;

class ChunkID {
	
	/**
	 * Used to get the chunkID from a location
	 * @param paramChunk
	 * @return
	 */
	public static ChunkID getChunkID(Location paramLocation) {
		return new ChunkID(paramLocation.getWorld(), paramLocation.getBlockX() / 16, paramLocation.getBlockZ() / 16);
	}
	
	/**
	 * Used to get the chunkID from a chunk
	 * @param paramChunk
	 * @return
	 */
	public static ChunkID getChunkID(Chunk paramChunk) {
		return new ChunkID(paramChunk.getWorld(), paramChunk.getX(), paramChunk.getZ());
	}
	
	public boolean equals(ChunkID paramChunkID) {
		return (this.getX() == paramChunkID.getX()) && (this.getZ() == paramChunkID.getZ()) && (this.getWorld() == paramChunkID.getWorld());
	}
	
	public int hash(double paramMaxHashSize) {
		int concurrentHash = 1;
		
		for(char c : this.getWorld().getName().toCharArray()) {
			concurrentHash *= (int) c;
		}
		
		if(this.getX() < 0) {
			concurrentHash *= (Math.abs(this.getX()) + concurrentHash);
		} else if(this.getX() > 0) {
			concurrentHash *= Math.abs(this.getX());
		}
		
		if(this.getZ() < 0) {
			concurrentHash *= (Math.abs(this.getZ()) + concurrentHash);
		} else if(this.getX() > 0) {
			concurrentHash *= Math.abs(this.getZ());
		}
		
		return (int) (Math.abs(concurrentHash) % paramMaxHashSize);
	}
	
	@Getter
	@Setter
	private World world;
	
	@Getter
	@Setter
	private int x;
	
	@Getter
	@Setter
	private int z;
	
	public ChunkID (World paramWorld, int paramChunkX, int paramChunkZ) {
		this.setWorld(paramWorld);
		this.setX(paramChunkX);
		this.setZ(paramChunkZ);
	}
}

class BufferChunk {
	
	@Getter
	@Setter
	private ChunkID chunkID;
	
	@Getter
	@Setter
	private UUID[] UUIDS;
	
	public void addVehicleUUID(UUID paramUUID) {
		UUID[] newListOfUUIDS = new UUID[this.getUUIDS().length + 1]; // creates a new list of UUIDS that will be 1 larger than the listOfUUIDS
		
		newListOfUUIDS[0] = paramUUID; // adds the new uuid of the vehicle being added to the newListOfUUIDS
		for(int index = 0; index < this.getUUIDS().length; index++) { // runs from 0 to the length of the old list of UUIDS
			newListOfUUIDS[index+1] = this.getUUIDS()[index]; // adds the vehicle's uuid to the newListOfUUIDS from position 1.
		}
		
		this.setUUIDS(newListOfUUIDS);
	}
	
	public void removeVehicleUUID(UUID paramUUID) {
		if(this.getUUIDS().length == 1) this.setUUIDS(new UUID[0]);
		
		UUID[] newListOfUUIDS = new UUID[this.getUUIDS().length - 1];
		
		int index = 0; // index will be the current index of the newUUIDSInChunk
		for(int n = 0; n < this.getUUIDS().length; n++) { // n stores the current position in UUIDSInChunk
			if(this.getUUIDS()[n] != paramUUID) { // checks if not the current UUID in the iteration is the vehicle being removed
				newListOfUUIDS[index] = this.getUUIDS()[n]; // adds the UUID to the newUUIDSInChunk
				index+=1; // adds one to the index
			}
		}
		
		this.setUUIDS(newListOfUUIDS);
	}
	
	public BufferChunk(ChunkID paramChunkID, UUID[] paramUUIDS) {
		this.setChunkID(paramChunkID);
		this.setUUIDS(paramUUIDS);
	}
}

public class ChunkManager {
	
	private static final int MAX_HASH_SIZE = 10000;
	
	/*
	 * @Getter
	private HashMap<ChunkID, UUID[]> vehicleChunkBuffer = new HashMap<ChunkID, UUID[]>(); // CHUNK ID; LIST OF UUIDS OF VEHICLES IN THAT CHUNK
	 */
	
	@Getter
	private HashMap<UUID, ChunkID> vehiclesInChunkInBuffer = new HashMap<UUID, ChunkID>(); // UUID of vehicle, CHUNK ID of the vehicles location
	
	@Getter
	private BufferChunk[] chunkBuffer;
	
	/**
	 * Assume -1 = null;
	 * @param paramChunkID
	 * @return
	 */
	private int getBufferChunkIndex(ChunkID paramChunkID) {
			
		int hash = paramChunkID.hash(MAX_HASH_SIZE);
			
		while(this.getChunkBuffer()[hash] != null) {
			if(this.getChunkBuffer()[hash].getChunkID().equals(paramChunkID)) {
				return hash;
			} else {
				hash = (hash + 1) % MAX_HASH_SIZE;
			}
		}
		
		return -1;
	}
	
	private BufferChunk getBufferChunk(ChunkID paramChunkID) {
		int index = this.getBufferChunkIndex(paramChunkID);
		
		if(index == -1) {
			return null;
		}
		return this.getChunkBuffer()[index];
	}
	
	private BufferChunk getBufferChunk(int paramIndex) {
		if(this.getChunkBuffer()[paramIndex] == null) {
			return null;
		}
		return this.getChunkBuffer()[paramIndex];
	}
	
	private void removeBufferChunk(ChunkID paramChunkID) {
		int index = this.getBufferChunkIndex(paramChunkID);
		
		if(index != -1) {
			this.getChunkBuffer()[index] = null;
		}
	}
	
	private void addBufferChunk(BufferChunk paramBufferChunk) {
		int hash = paramBufferChunk.getChunkID().hash(MAX_HASH_SIZE);
		
		while(this.getChunkBuffer()[hash] != null) {
			hash = (hash + 1) % MAX_HASH_SIZE;
		}
		
		this.getChunkBuffer()[hash] = paramBufferChunk;
	}
	
	public void addVehicleToBuffer(Vehicle paramVehicle) {
		if(paramVehicle.getLocation() == null) { // checks that the vehicle has a location
			throw new RuntimeException(new VehicleHasNoLocationException()); // no location present throws an error
		}
		if(this.isChunkLoaded(paramVehicle.getLocation().toLocation())) { // checks if the chunk is loaded that the vehicle is in
			throw new RuntimeException(new ChunkIsLoadedException()); // throws a new exception as the chunk is loaded, and therefore will spawn without a problem
		}
		
		ChunkID chunkID = ChunkID.getChunkID(paramVehicle.getLocation().toLocation());
		
		int index = this.getBufferChunkIndex(chunkID);
		
		if(index == -1) {
			this.addBufferChunk(new BufferChunk(chunkID, new UUID[] {paramVehicle.getUuid()}));
		} else {
			BufferChunk bufferChunk = this.getBufferChunk(index);
			bufferChunk.addVehicleUUID(paramVehicle.getUuid());
			chunkID = bufferChunk.getChunkID();
		}
		
		this.getVehiclesInChunkInBuffer().put(paramVehicle.getUuid(), chunkID);
	}
	
	public void removeVehicleFromBuffer(Vehicle paramVehicle) {
		if(!this.getVehiclesInChunkInBuffer().containsKey(paramVehicle.getUuid())) return;
		
		ChunkID chunkID = this.getVehiclesInChunkInBuffer().get(paramVehicle.getUuid());
		
		BufferChunk bufferChunk = this.getBufferChunk(chunkID);
		
		if(bufferChunk.getUUIDS() != null && bufferChunk.getUUIDS().length == 1){
			this.removeBufferChunk(chunkID);
		} else {
			bufferChunk.removeVehicleUUID(paramVehicle.getUuid());
		}
		
		this.getVehiclesInChunkInBuffer().remove(paramVehicle.getUuid());
	}
	
	public void loadChunk(Chunk paramChunk) {
	
		ChunkID chunkID = ChunkID.getChunkID(paramChunk);
		int index = this.getBufferChunkIndex(chunkID);
		
		if(index == -1) return;
		
		for(UUID uuid : this.getChunkBuffer()[index].getUUIDS()) {
			Vehicle vehicle = VehiclesAPI.getVehicleManager().getVehicleFromUUID(uuid);
			if(vehicle.isSpawned()) {
				vehicle.show();
			}
			this.getVehiclesInChunkInBuffer().remove(uuid);
		}
		this.removeBufferChunk(chunkID);
	}
	
	public void unloadChunk(Chunk paramChunk) {
		
		ChunkID chunkID = ChunkID.getChunkID(paramChunk); // gets the chunk id for the chunk that is being unloaded
		
		List<UUID> vehiclesInChunk = new ArrayList<UUID>(); // dynamic arraylist to store all of the vehicles uuids that are inside of the chunk that is being unloaded
		
		for(Vehicle vehicle : VehiclesAPI.getVehicleManager().getVehicles().values()) { // iterates through all of the vehicles on the server
			if(vehicle.isSpawned()) { // checks if the vehicle is spawned / if it is not spawned then it does not matter if the chunk is active or not
				if(vehicle.getLocation() != null && ChunkID.getChunkID(vehicle.getLocation().toLocation()).equals(chunkID)) { // cannot check chunk directly as it loads the chunk, then unloads the chunk causing a stackoverflow error
					vehicle.hide(); // as the vehicle is in the chunk, the vehicle is hidden
					vehiclesInChunk.add(vehicle.getUuid()); // adds vehicle to the list of vehicles in the chunk
					this.getVehiclesInChunkInBuffer().put(vehicle.getUuid(), chunkID); // adds the vehicle to the chunk buffer
				}
			}
		}
		
		UUID[] uuids = new UUID[vehiclesInChunk.size()]; // creates a new list of UUIDs which is to contain all of the vehicles inside of the vehicles in chunk
		for(int index = 0; index < vehiclesInChunk.size(); index++) { // 0 -> len(vehiclesInChunk)
			uuids[index] = vehiclesInChunk.get(index); // puts the uuid of vehicle in vehiclesInChunk into the uuids
			
		}
		
		this.addBufferChunk(new BufferChunk(chunkID, uuids));
		
	}
	
	public boolean isChunkLoaded(Location paramLocation) {
		ChunkID locationChunkID = ChunkID.getChunkID(paramLocation); // gets the chunk id at paramLocation
		Chunk[] chunks = paramLocation.getWorld().getLoadedChunks(); // cannot check if the chunk is loaded directly as it will always return true as the world::getChunkAt and Chunk::isLoaded loads the chunk
		for(Chunk chunk : chunks) { // runs through each chunk in the loaded chunks
			if(locationChunkID.equals(ChunkID.getChunkID(chunk))) { // checks if the location we are checking is inside of an active chunk
				return true;
			}
		}
		return false; // chunk is not loaded
	}
	
	
	
	public ChunkManager() {
		chunkBuffer = new BufferChunk[MAX_HASH_SIZE];
	}
	
	public void halt() {
	}
}
