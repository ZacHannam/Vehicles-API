package nea.zachannam.vehicles.api.vehicles.components.steeringwheel;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import nea.zachannam.vehicles.api.vehicles.components.steeringwheel.entity.EntitySteeringWheel;

public interface SteeringWheel {
	
	public EntitySteeringWheel getSteeringWheelEntity();
	public void setSteeringWheelEntity(EntitySteeringWheel paramSteeringWheelEntity);
	public ArmorStand getArmorStand();
	public void applyHelmetItem(ItemStack paramItem);
	public ItemStack getHelmetItem();
	public void setOffset(Vector paramOffset);
	public Vector getOffset();
	public void setSteeringWheelAngle(double paramAngle);
	public double getSteeringWheelAngle();
	public void setSteeringWheelLocks(int paramSteeringWheelLocks);
	public int getSteeringWheelLocks();
	public double getYawOffset();
	public void setYawOffset(double paramYawOffset);
	

}
