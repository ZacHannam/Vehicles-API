package nea.zachannam.vehicles.api.vehicles.components.seat.passangerseat;

import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.seat.SeatComponent;

public abstract class PassangerSeatComponent extends SeatComponent {
	
	//-------------------------------------------------------------------- COMPOONENT METHODS ------------------------------------------------------------------------
	
	
	@Override
	public String getComponentName() {
		return ComponentName.PASSANGER_SEAT.getName();
	}

	//-------------------------------------------------------------------- CONSTRUCTOR ------------------------------------------------------------------------
	
	public PassangerSeatComponent(Vehicle paramVehicle) {
		super(paramVehicle);
	}
}