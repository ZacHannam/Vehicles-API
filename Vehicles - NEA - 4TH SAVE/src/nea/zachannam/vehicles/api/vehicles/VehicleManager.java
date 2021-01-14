package nea.zachannam.vehicles.api.vehicles;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import nea.zachannam.vehicles.api.utils.VehicleMath;
import nea.zachannam.vehicles.api.vehicles.exceptions.CouldNotCreateVehicleException;
import nea.zachannam.vehicles.api.vehicles.models.VehicleType;

public class VehicleManager {

	private LinkedHashMap<UUID, Vehicle> vehicles = new LinkedHashMap<UUID, Vehicle>();
	
	public VehicleManager() {
		
	}
	
	public Vehicle createVehicle(VehicleType vehicleType) {
		
		try {
			UUID uuid = UUID.randomUUID();
			Vehicle vehicle;
			try {
				vehicle = vehicleType.getVehicleClass().getDeclaredConstructor(UUID.class).newInstance(uuid);
				vehicle.build();
				vehicles.put(uuid, vehicle);
				return vehicle;
			} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				e.printStackTrace();
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		throw new RuntimeException(new CouldNotCreateVehicleException(vehicleType.getName()));
	}
	
	public void halt() {
		for(Vehicle vehicle : vehicles.values()) {
			if(vehicle.isSpawned()) {
				vehicle.despawn();
			}
		}
	}

	public HashMap<UUID, Vehicle> getVehicles() {
		return vehicles;
	}

	public void destroyVehicle(Vehicle paramVehicle) {
		if(paramVehicle.isSpawned()) {
			paramVehicle.despawn();
			paramVehicle.haltComponents();
			paramVehicle.halt();
		}
				
		this.vehicles.remove(paramVehicle.getUuid());
	}
	
	public void destroyVehicle(UUID paramUUID) {
		if(this.getVehicles().containsKey(paramUUID)) {
			
			Vehicle vehicle = this.getVehicles().get(paramUUID);
			
			if(vehicle.isSpawned()) {
				vehicle.despawn();
				vehicle.haltComponents();
				vehicle.halt();
			}
			
			this.vehicles.remove(paramUUID);
		}
	}

	public boolean isVehicleAtLocation(VehicleLocation paramLocation) {
		for(Vehicle vehicle : this.getVehicles().values()) {
			if(VehicleMath.locationInsideVehicle(vehicle, paramLocation.toLocation())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isVehicle(UUID paramUUID) {
		return this.getVehicles().containsKey(paramUUID);
	}
	
	public Vehicle getVehicleFromUUID(UUID paramUUID) {
		if(this.isVehicle(paramUUID)) {
			return this.getVehicles().get(paramUUID);
		}
		return null;
	}

	public int getNumberOfVehicles() {
		return this.getVehicles().size();
	}
}
