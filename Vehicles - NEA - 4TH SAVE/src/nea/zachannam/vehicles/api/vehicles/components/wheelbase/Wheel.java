package nea.zachannam.vehicles.api.vehicles.components.wheelbase;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.utils.Point3D;
import nea.zachannam.vehicles.api.utils.Point4D;

public class Wheel {
	
	//-------------------------------------------------------------------- CONSTANTS ------------------------------------------------------------------------

	public static final double JUMP_HEIGHT = 1;
	
	//-------------------------------------------------------------------- VARIABLES ------------------------------------------------------------------------

	@Getter
	@Setter
	private Vector offset;
	
	@Getter
	@Setter
	private Point4D lastLocation;
	
	@Getter
	@Setter
	World world;
	
	@Getter
	@Setter
	double x;
	
	@Getter
	@Setter
	double y;
	
	@Getter
	@Setter
	double z;
	
	//-------------------------------------------------------------------- LOCATION METHODS ------------------------------------------------------------------------

	/**
	 * Sets the new location using a Point4D
	 * @param paramLocation
	 */
	public void setLocation(Point4D paramLocation) {
		this.setLocation(paramLocation.getWorld(), paramLocation.getX(), paramLocation.getY(), paramLocation.getZ());
	}

	/**
	 * Sets the new location using a Point3D
	 * @param paramLocation
	 */
	public void setLocation(Point3D paramLocation) {
		this.setLocation(paramLocation.getX(), paramLocation.getY(), paramLocation.getZ());
	}
	
	/**
	 * Sets the new location of the wheel
	 * @param paramWorld
	 * @param paramX
	 * @param paramY
	 * @param paramZ
	 */
	public void setLocation(World paramWorld, double paramX, double paramY, double paramZ) {
		
		this.setLastLocation(new Point4D(this.getWorld(), this.getX(), this.getY(), this.getZ())); // stores the last location into a new Point4D variable.
		
		this.setWorld(paramWorld);
		this.setX(paramX);
		this.setY(paramY);
		this.setZ(paramZ);
	}
	
	/**
	 * Used to set the location from a point 3D
	 * @param paramX
	 * @param paramY
	 * @param paramZ
	 */
	public void setLocation(double paramX, double paramY, double paramZ) {
		
		this.setLastLocation(new Point4D(this.getWorld(), this.getX(), this.getY(), this.getZ()));
		
		this.setX(paramX);
		this.setY(paramY);
		this.setZ(paramZ);
	}
	
	/**
	 * Returns the location of the wheel
	 * @return
	 */
	public Location getLocation() {
		return new Location(this.getWorld(), this.getX(), this.getY(), this.getZ());
	}
	
	/**
	 * Gets the next location of the wheel.
	 * @param paramLocation
	 * @return
	 */
	public Point3D nextLocation(Location paramLocation) {
		
		Location location = paramLocation.clone();
		
		location.setY(Math.round(this.getY()));
		
		while(location.getBlock().getType().isSolid() && location.getY() <= 255) {
			location.setY(location.getY() + 1);
		}
		
		location.setY(location.getY() - 1);
		while(!location.getBlock().getType().isSolid() && location.getY() >= 0) {
			location.setY(location.getY() - 1);
		}
		
		if((location.getY() - this.y) > (JUMP_HEIGHT-1)) return null;
		
		double y = location.getY() + (location.getBlock().isPassable() ? 0 : location.getBlock().getBoundingBox().getHeight());
		
		return new Point3D(location.getX(), y, location.getZ());
	}

	/**
	 * Sets the location to the last location
	 */
	public void teleportToLastLocation() {
		this.setLocation(this.lastLocation);
	}
	
	//-------------------------------------------------------------------- CONSTRUCTOR ------------------------------------------------------------------------

	//Constructor
	public Wheel(Vector paramOffset, Location paramLocation) {
		this.setLocation(paramLocation.getWorld(), paramLocation.getX(), paramLocation.getY(), paramLocation.getZ()); // Sets the location
		this.setOffset(paramOffset);
	}

}
