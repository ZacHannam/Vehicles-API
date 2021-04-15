package nea.zachannam.vehicles.api.main;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.chunk.ChunkManager;
import nea.zachannam.vehicles.api.commands.CommandManager;
import nea.zachannam.vehicles.api.database.VehiclesDatabase;
import nea.zachannam.vehicles.api.enums.Messages;
import nea.zachannam.vehicles.api.events.EventManager;
import nea.zachannam.vehicles.api.user.UserManager;
import nea.zachannam.vehicles.api.vehicles.VehicleManager;
import nea.zachannam.vehicles.api.vehicles.models.VehicleType;

public class VehiclesAPI extends JavaPlugin {
	
	@Getter
	public static Plugin plugin;
	
	@Getter
	public static VehicleManager vehicleManager;
	
	@Getter
	public static UserManager userManager;
	
	@Getter
	public static EventManager eventManager;
	
	@Getter
	public static CommandManager commandManager;
	
	@Getter
	public static VehiclesDatabase vehiclesDatabase;
	
	@Getter
	public static ChunkManager chunkManager;

	public void onEnable() {
	
		plugin = this;
		Messages.reload();
		eventManager = new EventManager();
		chunkManager = new ChunkManager();
		vehicleManager = new VehicleManager();
		userManager = new UserManager();
		commandManager = new CommandManager();	
		vehiclesDatabase = new VehiclesDatabase();
		
		VehicleType.loadAll();
		
	}
	
	public void onDisable() {
		
		vehicleManager.halt();
		vehiclesDatabase.halt();
		userManager.halt();
		commandManager.halt();	
		
	}

	public static void reload() {
		vehicleManager.halt();
		vehiclesDatabase.halt();
		userManager.halt();
		
		Messages.reload();
		chunkManager = new ChunkManager();
		vehicleManager = new VehicleManager();
		userManager = new UserManager();
		vehiclesDatabase = new VehiclesDatabase();
		
		VehicleType.reload();
	}
}
