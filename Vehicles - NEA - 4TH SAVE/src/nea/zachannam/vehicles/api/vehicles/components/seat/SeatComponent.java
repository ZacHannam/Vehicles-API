package nea.zachannam.vehicles.api.vehicles.components.seat;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.utils.Point4D;
import nea.zachannam.vehicles.api.utils.VehicleMath;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleLocation;
import nea.zachannam.vehicles.api.vehicles.components.Component;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.seat.entity.EntitySeat;
import nea.zachannam.vehicles.api.vehicles.components.seat.exceptions.SeatOccupiedException;

public abstract class SeatComponent extends Component {
	
	//-------------------------------------------------------------------- COMPONENT METHODS ------------------------------------------------------------------------
	
	public String getComponentType() {
		return ComponentName.SEAT.getName();
	}
	
	public String[] getRequiredComponents() {
		return new String[] {};
	}
	
	//-------------------------------------------------------------------- ABSTRACT METHODS ------------------------------------------------------------------------
	
	public abstract Vector OFFSET();

	//-------------------------------------------------------------------- RIDER ------------------------------------------------------------------------
	
	@Getter
	private Player rider;
	
	@SuppressWarnings("deprecation")
	public void setRider(Player paramPlayer) throws SeatOccupiedException {
		
		if(this.getArmorStand().getPassengers().size() >= 1) {
			throw new SeatOccupiedException();
		}
		
		if(paramPlayer == null) {
			return;
		}
		
		this.getArmorStand().setPassenger(paramPlayer);
		
		this.setRider(paramPlayer);
		this.getVehicle().onMount(paramPlayer);
	}
	
	public void ejectRider() {
		if(!this.hasRider()) {
			return;
		}
		
		OfflinePlayer player = this.getRider();
		
		this.getArmorStand().removePassenger(this.getRider());
		this.removeRider();
		this.getVehicle().onDismount(player);
		
		new BukkitRunnable() {
			public void run() {
				if(player.isOnline()) {
					Location location = getLocation().clone().add(0, 1, 0);
					Player p = player.getPlayer();
					location.setPitch(p.getLocation().getPitch());
					location.setYaw(p.getLocation().getYaw());
					location.setDirection(p.getLocation().getDirection());
					
					p.teleport(location);
				}
			}
		}.runTaskLater(VehiclesAPI.getPlugin(), 1);
	}
	
	public boolean hasRider() {
		return getRider() != null && this.getArmorStand().getPassengers().size() > 0;
	}
	
	public void removeRider() {
		this.rider = null;
	}
	
	//-------------------------------------------------------------------- METHODS ------------------------------------------------------------------------
	
	public void move() {
		Location newLocation = VehicleMath.getVectorYawLocation(this.getVehicle().getLocation(), this.getOffset(), this.getVehicle().getLocation().getYaw());
		this.getSeatEntity().setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), 0, 0);
	}

	public ArmorStand getArmorStand() {
		return this.getSeatEntity().getArmorStand();
	}
	
	public Location getLocation() {
		return this.getArmorStand().getLocation();
	}
	
	//-------------------------------------------------------------------- TICK ------------------------------------------------------------------------
	
	@Override
	public void tick() {
		move();
	}
	
	//-------------------------------------------------------------------- SPAWNING AND DESPAWNING ------------------------------------------------------------------------
	
	@Getter
	@Setter
	private Vector offset;
	
	@Getter
	@Setter
	private EntitySeat seatEntity;
	
	@Getter
	@Setter
	private Seat seat;
	
	@Override
	public void spawn(VehicleLocation paramLocation) {
		Location location = VehicleMath.getVectorYawLocation(paramLocation, this.getOffset(), paramLocation.getYaw());
		this.setSeatEntity(new EntitySeat(super.getVehicle(), Point4D.fromLocation(location)));
	}
	
	@Override
	public void despawn() {
		try {
			if(this.hasRider()) {
				VehiclesAPI.getUserManager().getUser(this.getRider().getUniqueId()).dismount();
			}
			this.ejectRider();
			if(this.seat != null) {
				this.getArmorStand().remove();
			}
		} catch(Exception e) {
			this.removeRider();
		}
	}
	
	//-------------------------------------------------------------------- CONSTRUCTOR ------------------------------------------------------------------------
	
	public SeatComponent(Vehicle paramVehicle) {
		super(paramVehicle);

		this.setOffset(OFFSET());
	}
}