package nea.zachannam.vehicles.api.vehicles.components.seat.entity;

import nea.zachannam.vehicles.api.utils.Point4D;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleEntity;

public class EntitySeat extends VehicleEntity {

	public EntitySeat(Vehicle paramVehicle, Point4D paramLocation) {
		super(paramVehicle, paramLocation);
		
		this.setMarker(true);
	}
}
