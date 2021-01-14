package nea.zachannam.vehicles.api.vehicles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.utils.VehicleMath;
import nea.zachannam.vehicles.api.vehicles.components.Component;
import nea.zachannam.vehicles.api.vehicles.components.exceptions.ComponentDoesNotExistException;
import nea.zachannam.vehicles.api.vehicles.components.seat.driverseat.DriverSeat;
import nea.zachannam.vehicles.api.vehicles.exceptions.ComponentCouldNotBeBuiltException;
import nea.zachannam.vehicles.api.vehicles.exceptions.TooManyDriverSeatsException;
import nea.zachannam.vehicles.api.vehicles.exceptions.VehicleHasNotBeenBuiltException;

public abstract class Vehicle implements ComponentBasedVehicle {
	
	//-------------------------------------------------------------------- VARIABLES ------------------------------------------------------------------------

	@Getter
	@Setter
	private int taskID; // stores the taskid (repeating task every tick)
	
	@Getter
	@Setter
	private boolean spawned; // stores the value of if the vehicle is spawned or not
	
	@Getter
	@Setter
	private VehicleLocation location; // stores the location of the vehicle

	@Getter
	private ArrayList<Component> components = new ArrayList<Component>(); //  stores an arraylist of components that are in the vehicle
	
	@Getter
	@Setter
	private boolean built;
	
	@Getter
	@Setter
	private UUID uuid; // stores the UUID of the vehicle
	
	//-------------------------------------------------------------------- COMPONENTS IN VEHICLE ------------------------------------------------------------------------
	/**
	 * 
	 * Returns a list of components that the vehicle has that are of a certain component type
	 * 
	 * @param paramComponentType
	 * @return
	 */
	public List<Component> getComponentByType(String paramComponentType) {
		
		List<Component>foundComponents = new ArrayList<Component>(); // creates the found list arraylist
		
		for(Component component : components) { // runs through each component in the vehicle
			if(component.getComponentName().equals(paramComponentType)) { // checks if it has the right component type
				foundComponents.add(component); // adds the component to foundComponents
			}
		}
		return foundComponents; // returns the found components
	}
	
	/**
	 * Used to get the first component in the componets arraylist. This is for when we are certain that there is only one of the components.
	 * 
	 * @param paramComponentType
	 * @return
	 */
	public Component getFirstComponentByType(String paramComponentName) {
		
		for(Component component : components) { // runs through each component
			if(component.getComponentName().equals(paramComponentName)) { // checks if the component type is the same as the paramComponentType
				return component; // returns that component
			}
		}
		throw new RuntimeException(new ComponentDoesNotExistException(paramComponentName)); //no component was found with that type, so it throws an exception
	}
	
	/**
	 * Used to add a component to the vehicle
	 * @param paramComponent
	 */
	public void addComponent(Component paramComponent) {
		components.add(paramComponent); // adds the component
		
		if(this.isSpawned()) { // checks if the vehicle is spawned
			this.despawn(); // despawns the vehicle if the vehicle is spawned
		}
		this.setBuilt(false); // sets built to flase
	}
	
	/**
	 * Used to check if the vehicle has a component
	 * @param componentType
	 * @return
	 */
	public boolean hasComponent(String paramComponentName) {
		for(Component component : this.components) { // iterates through each component in the vehicle
			if(component.getComponentName().equals(paramComponentName)) { // checks if the componentType is the same as the component's type
				return true; // returns true if it is the same
			}
		}
		return false; // after iteration, no components found to match the componentType then return false
	}
	
	//-------------------------------------------------------------------- SPAWNING, DESPAWNING AND, BUILDING ------------------------------------------------------------------------
	
	/** 
	 * Used to spawn in the vehicle at the location paramLocation
	 * @param paramLocation
	 */
	public void spawn(VehicleLocation paramLocation) {
		
		if(!this.isBuilt()) { // checks if the vehicle is built
			throw new RuntimeException(new VehicleHasNotBeenBuiltException()); // throws a VehicleHasNotBeenBuiltException if the vehicle has not been built
		}
		
		VehicleLocation spawnLocation = VehicleMath.fixSpawnY(paramLocation); // fixes the y spawning position, so it doesnt spawn in the air or in a block
		
		setLocation(spawnLocation); // sets the location of the vehicle
		
		for(Component component : components) { // runs through each of the components in the vehicle
			component.spawn(spawnLocation); // spawns the component
		}
		
		setSpawned(true); // sets spawned to true
		
		setTaskID(new BukkitRunnable() { // creates a new Bukkit Runnable and sets the taskID to the taskID of the Runnable

			@Override
			public void run() {
				
				if(!isSpawned()) { // checks if the vehicle is spawned
					this.cancel();// if the vehicle is not spawned then it end the runnable
					return; // returns from the function
				}
				
				for(Component component : components) { // runs through each of the components
					
					if(!isDriver()) { // if there is no driver then it will call the noButtonPressForward and noButtonPressSideways
						noButtonPressForward();
						noButtonPressSideways();
					}
					
					
					component.tick(); // calls the tick function for each component
				}
				
			}
			
		}.runTaskTimer(VehiclesAPI.getPlugin(), 1, 1).getTaskId()); // runs the task every tick
		
	}
	
	/**
	 * Used to despawn the vehicle
	 */
	public void despawn() {
		
		setSpawned(false); // sets spawned to false
		
		Bukkit.getScheduler().cancelTask(taskID); // cancels the repeating task
		
		for(Component component : components) {	// runs through each component in the vehicle
			component.despawn(); // despawns the component
		}
	}
	
	/**
	 * Used to build the vehicle
	 * 
	 * Building does not spawn the vehicle, it just checks if the vehicle is spawnable.
	 */
	public void build() {
		int numberOfDriverSeats = 0; // stores a count for number of driver seats / only one is allowed
		for(Component component : this.components) { // runs through each component in the vehicle
			if(!component.build()) { // attempts to build the component, if it cannot be built then throws an exception
				throw new RuntimeException(new ComponentCouldNotBeBuiltException(component.getComponentName()));
			}
			if(component.getComponentName().equals("DRIVER_SEAT")) { // checks if the component is a driver's seat
				++numberOfDriverSeats; // adds 1 to the number of driver seats
				if(numberOfDriverSeats >= 2) { // checks if the number of driver seats > 1
					throw new RuntimeException(new TooManyDriverSeatsException()); // throws the too many driver seats execption
				}
			}
		}
		
		setBuilt(true); // sets built to true.
	}
	
	//-------------------------------------------------------------------- VEHICLE METHODS ------------------------------------------------------------------------

	/**
	 * Checks if there is a driver in the vehicle
	 * @return
	 */
	public boolean isDriver() {
		for(Component seat :  this.getComponentByType("DRIVER_SEAT")) { // runs throughe each seat in the vehicle
			if(((DriverSeat) seat).hasRider()) return true; // returns true if there is a rider in the driver seat
		}
		return false; // otherwise returns false
	}
	
	/**
	 * Checks if there is another vehicle at the location
	 * 
	 * @param paramLocation
	 * @return
	 */
	public boolean isOtherVehicle(VehicleLocation paramLocation) {
		for(Vehicle vehicle : VehiclesAPI.getVehicleManager().getVehicles().values()) { // runs through each vehicle in the VehicleManager
			if(vehicle != this) { // checks vehicle is the not this vehicle
				if(VehicleMath.locationInsideVehicle(vehicle, paramLocation.toLocation())) { // checks if they are inside of eachother
					return true; // returns true if there is a vehicle at that location
				}
			}
		}
		return false; // after completed iteration, no vehicles found, return false
	}

	//-------------------------------------------------------------------- HANDLES VEHICLE META ------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	private JSONObject getVehicleMeta() {
		
		JSONObject meta = new JSONObject(); // creates a new meta jsonobject
		meta.put("spawned", isSpawned()); // spawned to isspawned
		meta.put("location", getLocation()); // sets location to getLocation
		
		return meta; // returns the meta object
	}
	
	/**
	 * Gets the meta for the vehicle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getMeta() {
		JSONObject meta = new JSONObject();
		JSONObject componentsJSON = new JSONObject();
		for(Component component : this.getComponents()) {
			if(componentsJSON.containsKey(component.getComponentName())) continue;
			List<Component> components = this.getComponentByType(component.getComponentName());
			JSONObject[] componentsMeta = new JSONObject[components.size()];
			for(int componentIndex = 0; componentIndex < components.size(); componentIndex++) {
				componentsMeta[componentIndex]=components.get(componentIndex).getMeta();
			}
			
			componentsJSON.put(component.getComponentName(), componentsMeta);
		}
		meta.put("components", componentsJSON);
		meta.put("vehicle", getVehicleMeta());
		
		return meta;
	}
	
	//-------------------------------------------------------------------- CONSTRUCTOR AND HALT ------------------------------------------------------------------------
	
	// CONSTRUCTOR
	public Vehicle(UUID paramUUID) {
		this.setUuid(paramUUID); // sets UUID to paramUUID
		
		setBuilt(false); // sets built to false
		setSpawned(false); // sets spawned to false
	}
	
	/**
	 * Halts the vehicle
	 */
	public void halt() {
		VehiclesAPI.getVehiclesDatabase().saveVehicle(this); // saves the vehicle's data
	}
}
