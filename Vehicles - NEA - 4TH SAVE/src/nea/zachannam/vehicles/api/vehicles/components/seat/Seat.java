package nea.zachannam.vehicles.api.vehicles.components.seat;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import nea.zachannam.vehicles.api.vehicles.components.seat.entity.EntitySeat;
import nea.zachannam.vehicles.api.vehicles.components.seat.exceptions.SeatOccupiedException;

public interface Seat {
	
	public Location getLocation();
	public void setRider(Player paramPlayer) throws SeatOccupiedException;
	public boolean hasRider();
	public Player getRider();
	public void ejectRider();
	public EntitySeat getSeatEntity();
	public void setSeatEntity(EntitySeat paramSeatEntity);
	public ArmorStand getArmorStand();
	public Vector getOffset();
	public void setOffset(Vector paramOffset);
	
	
}
