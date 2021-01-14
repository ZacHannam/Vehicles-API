package nea.zachannam.vehicles.api.main;

import org.bukkit.entity.Player;

public enum PermissionsEnum {

	COMMAND_SUMMON_VEHICLE("vehiclesapi.summonvehicle");
	
	private String permission;
	
	PermissionsEnum(String paramPermission){
		this.permission = paramPermission;
	}
	
	public String getPermission() {
		return this.permission;

	}

	public boolean hasPermission(Player paramPlayer) {
		return paramPlayer.hasPermission(this.getPermission());
	}
	
}
