package nea.zachannam.vehicles.api.main;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import nea.zachannam.vehicles.api.commands.CommandManager;
import nea.zachannam.vehicles.api.database.VehiclesDatabase;
import nea.zachannam.vehicles.api.events.EventManager;
import nea.zachannam.vehicles.api.user.UserManager;
import nea.zachannam.vehicles.api.vehicles.VehicleManager;
import nea.zachannam.vehicles.api.vehicles.models.VehicleType;

public class VehiclesAPI extends JavaPlugin {
	
	private static Plugin plugin;
	private static VehicleManager vehicleManager;
	private static UserManager userManager;
	private static EventManager eventManager;
	private static CommandManager commandManager;
	private static VehiclesDatabase vehiclesDatabase;
	
	public static VehiclesDatabase getVehiclesDatabase() {
		return vehiclesDatabase;
	}
	
	public static EventManager getEventManager() {
		return eventManager;
	}
	
	public static UserManager getUserManager() {
		return userManager;
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static VehicleManager getVehicleManager() {
		return vehicleManager;
	}
	
	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public void onEnable() {
	
		plugin = this;
		eventManager = new EventManager();
		vehicleManager = new VehicleManager();
		userManager = new UserManager();
		commandManager = new CommandManager();
		vehiclesDatabase = new VehiclesDatabase();
		
		VehicleType.loadAllVehicles();
		
	}
	
	public void onDisable() {
		
		vehicleManager.halt();
		userManager.halt();
		commandManager.halt();
		eventManager.halt();
		vehiclesDatabase.halt();
		
	}
}
