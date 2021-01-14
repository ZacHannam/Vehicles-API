package nea.zachannam.vehicles.api.vehicles.components;

import lombok.Getter;
import lombok.Setter;

public enum ComponentName {
	
	FRAME("FRAME"),
	INVENTORY("INVENTORY"),
	SEAT("SEAT"),
	PASSANGER_SEAT("PASSANGER_SEAT"),
	DRIVER_SEAT("DRIVER_SEAT"),
	STEERING_WHEEL("STEERING_WHEEL"),
	WHEELBASE("WHEELBASE");
	
	@Getter
	@Setter
	String name;
	
	ComponentName(String paramName){
		this.setName(paramName);
	}
}
