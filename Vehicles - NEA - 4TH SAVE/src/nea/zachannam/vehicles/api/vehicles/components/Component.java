package nea.zachannam.vehicles.api.vehicles.components;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleLocation;
import nea.zachannam.vehicles.api.vehicles.components.frame.Frame;
import nea.zachannam.vehicles.api.vehicles.components.seat.driverseat.DriverSeat;
import nea.zachannam.vehicles.api.vehicles.components.steeringwheel.SteeringWheel;
import nea.zachannam.vehicles.api.vehicles.components.wheelbase.WheelBase;


public abstract class Component {

	//-------------------------------------------------------------------- VARIABLES ------------------------------------------------------------------------
	
	@Getter
	@Setter
	private Vehicle vehicle;
	
	//-------------------------------------------------------------------- ABSTRACT METHODS ------------------------------------------------------------------------

	
	public abstract String getComponentName();
	public abstract String[] getRequiredComponents();
	
	//-------------------------------------------------------------------- OVERRIDABLE METHODS ------------------------------------------------------------------------

	public JSONObject getMeta() {return null;}
	public void tick() {}
	public void spawn(VehicleLocation paramLocation) {}
	public void despawn() {}
	public void onButtonPressW() {}
	public void onButtonPressA() {}
	public void onButtonPressS() {}
	public void onButtonPressD() {}
	public void onButtonPressSpace() {}
	public void onButtonPressCtrl() {}
	public void noButtonPressForward() {}
	public void noButtonPressSideways() {}
	public void halt() {}
	public void onVehicleClick(Player paramPlayer) {}	
	public void onDriverDismount(Player paramRider) {}
	
	//-------------------------------------------------------------------- FINAL METHODS ------------------------------------------------------------------------

	public final boolean build() {
		
		for(String componentType : getRequiredComponents()) {
			if(!this.getVehicle().hasComponent(componentType)) {
				return false;
			}
		}
		return true;
	}
	
	//-------------------------------------------------------------------- EASE OF ACCESS METHODS ------------------------------------------------------------------------

	public SteeringWheel getSteeringWheel() {
		return ((SteeringWheel) this.getVehicle().getFirstComponentByType(ComponentName.STEERING_WHEEL.getName()));
	}
	
	public Frame getFrame() {
		return ((Frame) this.getVehicle().getFirstComponentByType(ComponentName.FRAME.getName()));
	}
	
	public WheelBase getWheelBase() {
		return ((WheelBase) this.getVehicle().getFirstComponentByType(ComponentName.WHEELBASE.getName()));
	}
	
	public DriverSeat getDriverSeat() {
		return ((DriverSeat) this.getVehicle().getFirstComponentByType(ComponentName.DRIVER_SEAT.getName()));
	}
	
	//-------------------------------------------------------------------- CONSTRUCTOR ------------------------------------------------------------------------

	//Constructor
	public Component(Vehicle paramVehicle) {
		setVehicle(paramVehicle);
	}
}
