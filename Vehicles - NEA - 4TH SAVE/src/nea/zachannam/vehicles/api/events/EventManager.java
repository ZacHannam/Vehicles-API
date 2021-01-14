package nea.zachannam.vehicles.api.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import nea.zachannam.vehicles.api.events.player.EntityDismount;
import nea.zachannam.vehicles.api.events.player.InventoryClick;
import nea.zachannam.vehicles.api.events.player.InventoryClose;
import nea.zachannam.vehicles.api.events.player.PlayerArmorStandManipulate;
import nea.zachannam.vehicles.api.events.player.PlayerInteract;
import nea.zachannam.vehicles.api.events.player.PlayerInteractAtEntity;
import nea.zachannam.vehicles.api.events.player.PlayerJoin;
import nea.zachannam.vehicles.api.events.player.PlayerQuit;
import nea.zachannam.vehicles.api.events.protocollib.PlayClientSteerVehicle;
import nea.zachannam.vehicles.api.main.VehiclesAPI;

class ProtocolLibEventManager {
	
	public ProtocolLibEventManager() {
		new PlayClientSteerVehicle();
	}
}

public class EventManager {

	private ProtocolLibEventManager protocolLibEventManager;
	
	public ProtocolLibEventManager getProtocolLibEventManager() {
		return protocolLibEventManager;
	}
	
	public EventManager() {
		
		this.protocolLibEventManager = new ProtocolLibEventManager();
		
		Plugin plugin = VehiclesAPI.getPlugin();
		PluginManager pluginManager = Bukkit.getPluginManager();
		
		pluginManager.registerEvents(new PlayerJoin(), plugin);
		pluginManager.registerEvents(new PlayerQuit(), plugin);
		pluginManager.registerEvents(new EntityDismount(), plugin);
		pluginManager.registerEvents(new PlayerInteract(), plugin);
		pluginManager.registerEvents(new PlayerArmorStandManipulate(), plugin);
		pluginManager.registerEvents(new InventoryClick(), plugin);
		pluginManager.registerEvents(new InventoryClose(), plugin);
		pluginManager.registerEvents(new PlayerInteractAtEntity(), plugin);
	}

	public void halt() {
		
	}
}
