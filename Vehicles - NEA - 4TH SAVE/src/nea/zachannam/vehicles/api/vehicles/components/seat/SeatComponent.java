package nea.zachannam.vehicles.api.vehicles.components.seat;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import nea.zachannam.vehicles.api.utils.Point4D;
import nea.zachannam.vehicles.api.utils.VehicleMath;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleLocation;
import nea.zachannam.vehicles.api.vehicles.components.Component;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.seat.entity.EntitySeat;
import nea.zachannam.vehicles.api.vehicles.components.seat.exceptions.SeatOccupiedException;

public abstract class SeatComponent extends Component {
	
	// COMPONENT METHODS +
	
	public String getComponentType() {
		return ComponentName.SEAT.getName();
	}
	
	public String[] getRequiredComponents() {
		return new String[] {};
	}
	
	// COMPONENT METHODS -
	
	// ABSTRACT METHODS +
	
	public abstract Vector OFFSET();
	
	// ABSTRACT METHODS -
	
	// VARIABLES +
	
	private EntitySeat seatEntity;
	private Vector offset;
	private Player rider;
	private Seat seat;
	
	// VARIABLES -
	
	// COMPONENT + 
	
	public Location getLocation() {
		return this.getArmorStand().getLocation();
	}
	
	@SuppressWarnings("deprecation")
	public void setRider(Player paramPlayer) throws SeatOccupiedException {
		
		if(this.getArmorStand().getPassengers().size() >= 1) {
			throw new SeatOccupiedException();
		}
		
		if(paramPlayer == null) {
			return;
		}
		
		this.getArmorStand().setPassenger(paramPlayer);
		
		this.rider = paramPlayer;
	}
	
	public boolean hasRider() {
		return rider != null && this.getArmorStand().getPassengers().size() > 0;
	}
	
	public Player getRider() {
		return this.rider;
	}
	
	public void ejectRider() {
		if(!this.hasRider()) {
			return;
		}
		this.getVehicle().onDriverDismount(this.getRider());
		this.getArmorStand().removePassenger(this.getRider());
		this.rider = null;
	}
	
	public EntitySeat getSeatEntity() {
		return this.seatEntity;
	}
	
	public void setSeatEntity(EntitySeat paramSeatEntity) {
		this.seatEntity = paramSeatEntity;
	}
	
	public ArmorStand getArmorStand() {
		return this.getSeatEntity().getArmorStand();
	}
	
	public Vector getOffset() {
		return this.offset;
	}
	
	public void setOffset(Vector paramOffset) {
		this.offset = paramOffset;
	}
	
	// COMPONENT -
	
	// METHODS +
	
	public void move() {
		
		Location newLocation = VehicleMath.getVectorYawLocation(this.getVehicle().getLocation(), this.getOffset(), this.getVehicle().getLocation().getYaw());
		this.getSeatEntity().setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), 0, 0);
		
	}
	
	// METHODS -
	
	// CONTROL +
	
	// Tick
	@Override
	public void tick() {
		move();
	}	
	
	// Spawn
	@Override
	public void spawn(VehicleLocation paramLocation) {
		Location location = VehicleMath.getVectorYawLocation(paramLocation, this.getOffset(), paramLocation.getYaw());
		this.setSeatEntity(new EntitySeat(super.getVehicle(), Point4D.fromLocation(location)));
	}
	
	// Despawn
	@Override
	public void despawn() {
		if(this.seat != null) {
			this.getArmorStand().remove();
		}
		this.rider = null;
	}
	
	// CONTROL -
	
	// MAIN +
	
	public SeatComponent(Vehicle paramVehicle) {
		super(paramVehicle);

		this.setOffset(OFFSET());
	}
	
	// MAIN -
}
