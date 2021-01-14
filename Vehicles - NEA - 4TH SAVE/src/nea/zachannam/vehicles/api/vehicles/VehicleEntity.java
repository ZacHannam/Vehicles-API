package nea.zachannam.vehicles.api.vehicles;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.utils.Point4D;
import net.minecraft.server.v1_16_R1.EntityArmorStand;

public class VehicleEntity extends EntityArmorStand {
	
	//-------------------------------------------------------------------- VARIABLES ------------------------------------------------------------------------

	@Getter
	@Setter
	private Vehicle parentVehicle;
	
	@Getter
	private static HashMap<UUID, UUID> vehicleEntities = new HashMap<UUID, UUID>();
	
	//-------------------------------------------------------------------- Constructor ------------------------------------------------------------------------

	// Constructor
	public VehicleEntity(Vehicle paramVehicle, Point4D paramLocation) {
		super(((CraftWorld) paramLocation.getWorld()).getHandle(), paramLocation.getX(), paramLocation.getY(), paramLocation.getZ());
		((CraftWorld) paramLocation.getWorld()).getHandle().addEntity(this); // spawns in the custom entity
		
		setParentVehicle(paramVehicle); // sets the parent vehicle to the vehicle which it is attached to
		
		super.setMarker(false); // sets the armorstand to not be a marker
		super.setInvisible(true); // makes the armorstand invisible
		
		addVehicleEntity(this); // adds the entity to the vehicleEntities
	}
	
	//-------------------------------------------------------------------- METHODS ------------------------------------------------------------------------

	/**
	 * Gets the armorstand entity
	 * @return
	 */
	public ArmorStand getArmorStand() {
		return (ArmorStand) this.getBukkitEntity(); // returns the armorstand bukkit entity
	}
	
	/**
	 * Attaches the armorstand to the vehicle using UUIDs
	 * @param paramVehicleEntity
	 */
	private static void addVehicleEntity(VehicleEntity paramVehicleEntity) {
		getVehicleEntities().put(paramVehicleEntity.getUniqueID(), paramVehicleEntity.getParentVehicle().getUuid()); // key: ArmorStand UUID, value: vehicle UUID
	}
	
	/**
	 * Removes the armorstand from the vehicle
	 * @param paramUUID
	 */
	private static void removeVehicleEntity(UUID paramUUID) {
		getVehicleEntities().remove(paramUUID); // removes key: ArmorStand UUID
	}
	
	/**
	 * Checks if the armorstand is attached to a vehicle
	 * @param paramEntity
	 * @return
	 */
	public static boolean isVehicle(Entity paramEntity) {
		return getVehicleEntities().containsKey(paramEntity.getUniqueId());
	}
	
	/**
	 * Checks if the UUID (Unknown Entity) is a vehicle armor stand
	 * @param paramUUID
	 * @return
	 */
	public static boolean isVehicle(UUID paramUUID) {
		return getVehicleEntities().containsKey(paramUUID);
	}

	/**
	 * Returns the Vehicle from the clicked ArmorStand
	 * @param paramEntity
	 * @return
	 */
	public static Vehicle getVehicleFromVehicleEntity(Entity paramEntity) {
		return VehiclesAPI.getVehicleManager().getVehicleFromUUID(getVehicleEntities().get(paramEntity.getUniqueId()));
	}
	
	//-------------------------------------------------------------------- OVERRIDE METHODS ------------------------------------------------------------------------

	/**
	 * Overrides death procedure of an entity, this is called whenever the entity dies
	 */
	@Override
	public void die() {
		removeVehicleEntity(this.getUniqueID()); // removes the vehicle entity
		super.die(); // calls the parent's die procedure
	}
}
