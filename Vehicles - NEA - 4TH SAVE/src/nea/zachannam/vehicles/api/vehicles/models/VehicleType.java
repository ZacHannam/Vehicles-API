package nea.zachannam.vehicles.api.vehicles.models;

import java.util.HashMap;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.models.exceptions.NameOrIdAlreadyInUseException;

enum RegisteredVehicle{
	
	KART_1("Kart(1)", Kart_1.class, 1),
	RACECAR_1("Race Car(1)", RaceCar_1.class, 2);
	
	public static void loadAllVehicles() {
		for(RegisteredVehicle type : values()) {
			new VehicleType(type.getName(), type.getVehicleClass(), type.getID());
		}
	}
	
	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private int ID;
	
	@Getter
	@Setter
	private Class<? extends Vehicle> vehicleClass;
	
	RegisteredVehicle(String paramName, Class<? extends Vehicle> paramVehicleClass, int paramID){
		this.setName(paramName);
		this.setID(paramID);
		this.setVehicleClass(paramVehicleClass);
	}
}

public class VehicleType {
	
	@Getter
	private static HashMap<Integer, VehicleType> ID_MAP = new HashMap<Integer, VehicleType>();
	
	@Getter
	private static HashMap<String, VehicleType> NAME_MAP = new HashMap<String, VehicleType>();
	
	public static VehicleType getTypeFromID(int paramID) {
		if(ID_MAP.containsKey(paramID)) {
			return ID_MAP.get(paramID);
		}
		return null;
		
	}
	
	public static VehicleType getTypeFromName(String paramName) {
		if(NAME_MAP.containsKey(paramName)) {
			return NAME_MAP.get(paramName);
		}
		return null;
	}
	
	public static VehicleType getVehicleType(String paramArgument){
		try {
			
			int id = Integer.valueOf(paramArgument);
			VehicleType vehicleType = VehicleType.getTypeFromID(id);
			
			if(vehicleType == null) {
				throw new NullPointerException();
			}
			
			return vehicleType;
			
			
		} catch(NumberFormatException | NullPointerException e) {
			
			return VehicleType.getTypeFromName(paramArgument);
			
		}
	}
	
	@Getter
	@Setter
	String name;
	
	@Getter
	@Setter
	int ID;
	
	@Getter
	@Setter
	Class<? extends Vehicle> vehicleClass;
	
	public VehicleType(String paramName, Class<? extends Vehicle> paramVehicleClass, int paramID){
		Bukkit.getLogger().info("Loading vehicle");
		this.setName(paramName);
		this.setID(paramID);
		this.setVehicleClass(paramVehicleClass);
		
		if(getID_MAP().containsKey(this.getID()) || getNAME_MAP().containsKey(this.getName())) {
			throw new RuntimeException(new NameOrIdAlreadyInUseException());
		}
		
		if(this.getID() > 0) {
			getID_MAP().put(this.getID(), this);
		}
		if(this.getName() != null) {
			getNAME_MAP().put(this.getName(), this);
		}
	}

	public static void loadAllVehicles() {
		RegisteredVehicle.loadAllVehicles();
	}
}
