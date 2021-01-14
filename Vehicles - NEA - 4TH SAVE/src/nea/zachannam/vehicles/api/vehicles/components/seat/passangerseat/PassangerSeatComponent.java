package nea.zachannam.vehicles.api.vehicles.components.seat.passangerseat;

import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.seat.SeatComponent;

public abstract class PassangerSeatComponent extends SeatComponent {
	
	// COMPONENT METHODS +
	
	@Override
	public String getComponentName() {
		return ComponentName.PASSANGER_SEAT.getName();
	}
	
	// COMPONENT METHODS -
	
	// ABSTRACT METHODS + 
	// ABSTRACT METHODS -
	
	// VARIABLES +
	// VARIABLES -
	
	// COMPONENT +
	// COMPONENT -
	
	// METHODS +
	// METHODS -
	
	// CONTROL +
	// CONTROL -
	
	// MAIN +
	
	public PassangerSeatComponent(Vehicle paramVehicle) {
		super(paramVehicle);
	}
	
	// MAIN -

}
