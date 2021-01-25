package nea.zachannam.vehicles.api.vehicles.models;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.models.exceptions.NameOrIdAlreadyInUseException;

enum RegisteredVehicle{
	
	KART_1("Kart(1)", Kart_1.class, 1),
	RACECAR_1("RaceCar(1)", RaceCar_1.class, 2);
	
	/**
	 * Loads all of the registered vehicles (these are the vehicles provided with the API!)
	 */
	public static void loadAllVehicles() {
		for(RegisteredVehicle type : values()) { // iterates through each vehicle model
			new VehicleType(type.getName(), type.getVehicleClass(), type.getID()); // creates a new vehicle type to register it into the actual API
		}
	}
	
	@Getter
	@Setter
	private String name; // name of the vehicle (MUST BE UNIQUE)
	
	@Getter
	@Setter
	private int ID; // ID of the vehicle (MOST COMMONLY USED) (MUST BE UNIQUE)
	
	@Getter
	@Setter
	private Class<? extends Vehicle> vehicleClass; // Vehicle model class (Must have the Vehicle class as a parent)
	
	/**
	 * Constructor for enum item
	 * @param paramName
	 * @param paramVehicleClass
	 * @param paramID
	 */
	RegisteredVehicle(String paramName, Class<? extends Vehicle> paramVehicleClass, int paramID){
		this.setName(paramName); 
		this.setID(paramID);
		this.setVehicleClass(paramVehicleClass);
	}
}

public class VehicleType {
	
	@Getter
	private static HashMap<Integer, VehicleType> ID_MAP = new HashMap<Integer, VehicleType>(); // stores a list of ids and their VehicleType
	
	@Getter
	private static HashMap<String, VehicleType> NAME_MAP = new HashMap<String, VehicleType>(); // stores a list of names and their VehicleType
	
	/**
	 * Used to get the VehicleType from its ID
	 * @param paramID
	 * @return
	 */
	public static VehicleType getTypeFromID(int paramID) {
		if(ID_MAP.containsKey(paramID)) { // checks if the ID is valid
			return ID_MAP.get(paramID); // returns the corresponding VehicleType
		}
		return null; // if the ID is not valid null will be returned instead
		
	}
	
	/**
	 * Used to get the VehicleType from its name
	 * @param paramName
	 * @return
	 */
	public static VehicleType getTypeFromName(String paramName) {
		if(NAME_MAP.containsKey(paramName)) { // checks if the name is valid
			return NAME_MAP.get(paramName); // returns the corresponding VehicleType
		}
		return null; // if the name is not valid null will be returned instead
	}
	
	/**
	 * Used to get the VehicleType from its id or name / first check id
	 * @param paramArgument
	 * @return
	 */
	public static VehicleType getVehicleType(String paramArgument){
		try {
			
			int id = Integer.valueOf(paramArgument); // attempts to convert the argument to an int, if it is not an int then it will try to get it by its name
			VehicleType vehicleType = VehicleType.getTypeFromID(id); // successfully get the int
			
			if(vehicleType == null) { // checks if the vehicletype is null
				throw new NullPointerException(); // raises a new NullPointerException which is caught later in the method, to attempt to convert it to an int
			} else {
				return vehicleType; // if the vehicle type is not null then it has successfully retrieved a valid vehicle type
			}

		} catch(NumberFormatException | NullPointerException e) { // called when the argument is not an ID to check if it is a name
			
			return VehicleType.getTypeFromName(paramArgument); // either returns null (Name is not valid) or the VehicleType (Name is valid)
			
		}
	}
	
	@Getter
	@Setter
	private String name; // name of the vehicle (MUST BE UNIQUE)
	
	@Getter
	@Setter
	private int ID; // ID of the vehicle (MOST COMMONLY USED) (MUST BE UNIQUE)
	
	@Getter
	@Setter
	private Class<? extends Vehicle> vehicleClass; // Vehicle model class (Must have the Vehicle class as a parent)
	
	/**
	 * Constructor which setups the object, and puts it in the static ID_MAP and NAME_MAP
	 * @param paramName
	 * @param paramVehicleClass
	 * @param paramID
	 */
	public VehicleType(String paramName, Class<? extends Vehicle> paramVehicleClass, int paramID) {
		this.setName(paramName);
		this.setID(paramID);
		this.setVehicleClass(paramVehicleClass);
		
		if(getID_MAP().containsKey(this.getID()) || getNAME_MAP().containsKey(this.getName())) { // checks if the key and name are already in use
			throw new RuntimeException(new NameOrIdAlreadyInUseException()); // raises a new exception as atleast one is already used
		}
		
		if(this.getID() > 0) { // checks if the ID is greater than 0, IDS must be bigger than 0. Otherwise the VehicleType cannot be found using its ID
			getID_MAP().put(this.getID(), this); // adds the ID and VehicleType into ID_MAP
		}
		if(this.getName() != null) { // checks if the name is null, if it is null then the VehicleType cannot be found by its name
			getNAME_MAP().put(this.getName(), this); // adds the name and VehicleType into the NAME_MAP
		}
		
		VehiclesAPI.getVehiclesDatabase().loadAllWithID(paramID); // Now that the VehicleType has been 'discovered' it is safe to load in all vehicles that use the id.
	}

	/**
	 * Called to load in all API defined vehicles. 
	 */
	public static void loadAllVehicles() {
		RegisteredVehicle.loadAllVehicles(); // loads all of the vehicles.
	}
}
