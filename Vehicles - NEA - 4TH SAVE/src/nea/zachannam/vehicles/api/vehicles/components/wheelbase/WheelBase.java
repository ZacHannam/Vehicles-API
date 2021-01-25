package nea.zachannam.vehicles.api.vehicles.components.wheelbase;

import org.bukkit.util.Vector;

import nea.zachannam.vehicles.api.vehicles.VehicleLocation;

public interface WheelBase {
	public Vector[] getWheelOffsets();
	public double getReversePower();
	public VehicleLocation getLastLocation();
	public float getSteeringRotation();
	public float getMaxSteeringRotation();
	public float getSteeringSpeed();
	public float getNormaliseSteeringAmount();
	public double getPowerAcceleration();
	public double getBrakeAcceleration();
	public double getIdleAcceleration();
	public double getLinearAcceleration();
	public double getMaxSpeed();
	public Direction getDirection();
	public boolean isInReverse();
	public ForceManager getForceManager();
	public double getSurfaceFriction();
	public double getWidth();
	public double getLength();
}
