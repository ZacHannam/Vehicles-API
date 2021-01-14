package nea.zachannam.vehicles.api.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum MessagesEnum {

	SEAT_OCCUPIED(ChatColor.GREEN + "That seat is already occupied!"),
	USER_IN_SEAT(ChatColor.GREEN + "You are already in a seat!"),
	INVALID_PERMISSION(ChatColor.RED + "Invalid Permission!"),
	COMMAND_SUMMON_VEHICLE_USAGE(ChatColor.RED + "/summonvehicle <vehicletype> [<world> <x> <y> <z>]"),
	INVALID_VEHICLE(ChatColor.RED + "Invalid Vehicle Model"),
	SUCCESSFULLY_SPAWNED_VEHICLE(ChatColor.GREEN + "Successfully summoned vehicle!");
	
	private String message;
	
	MessagesEnum(String paramMessage){
		this.message = paramMessage;
	}
	
	public String getMessage() {
		return this.message;

	}

	public void sendMessage(Player paramPlayer) {
		paramPlayer.sendMessage(this.getMessage());
	}

	public void sendMessageToConsole() {
		Bukkit.getLogger().info(this.getMessage());
	}

	public void sendMessageToCommandSender(CommandSender paramSender)  {
		paramSender.sendMessage(this.getMessage());
	}
	
}

