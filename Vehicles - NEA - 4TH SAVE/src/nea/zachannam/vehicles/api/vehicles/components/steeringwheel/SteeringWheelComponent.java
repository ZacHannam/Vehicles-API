package nea.zachannam.vehicles.api.vehicles.components.steeringwheel;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import nea.zachannam.vehicles.api.utils.Point4D;
import nea.zachannam.vehicles.api.utils.VehicleMath;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleLocation;
import nea.zachannam.vehicles.api.vehicles.components.Component;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.steeringwheel.entity.EntitySteeringWheel;
import nea.zachannam.vehicles.api.vehicles.components.wheelbase.WheelBase;

public abstract class SteeringWheelComponent extends Component implements SteeringWheel {
	
	// COMPONENT METHODS +
	
	public String getComponentName() {
		return ComponentName.STEERING_WHEEL.getName();
	}
		
	public String[] getRequiredComponents() {
		return new String[] {
				ComponentName.FRAME.getName(), ComponentName.DRIVER_SEAT.getName(),
		};
	}
		
	// COMPONENT METHODS -
	
	// ABSTRACT METHODS +
	
	public abstract ItemStack HELMET_ITEM();
	public abstract int STEERINGWHEEL_LOCKS();
	public abstract double STEERINGWHEEL_ANGLE();
	public abstract Vector OFFSET();
	public abstract double YAW_OFFSET();
	
	// ABSTRACT METHODS -
	
	// VARIABLES +
	
	private Vector offset;
	private EntitySteeringWheel steeringWheelEntity;
	private ItemStack helmetItem;
	
	private double steeringWheelAngle;
	private int steeringWheelLocks = 1;
	private double yawOffset;
	
	// VARIABLES -
	
	// COMPONENT +
	
	public EntitySteeringWheel getSteeringWheelEntity() {
		return this.steeringWheelEntity;
	}
	
	public void setSteeringWheelEntity(EntitySteeringWheel paramSteeringWheelEntity) {
		this.steeringWheelEntity = paramSteeringWheelEntity;
	}
	
	public ArmorStand getArmorStand() {
		return this.steeringWheelEntity.getArmorStand();
	}
	
	@SuppressWarnings("deprecation")
	public void applyHelmetItem(ItemStack paramItem) {
		this.setHelmetItem(paramItem);
		if(this.getSteeringWheelEntity() != null) {
			this.getArmorStand().setHelmet(this.getHelmetItem());
		}
	}
	
	public ItemStack getHelmetItem() {
		return helmetItem;
	}
	
	public void setOffset(Vector paramOffset) {
		this.offset = paramOffset;
	}
	public Vector getOffset() {
		return this.offset;
	}
	
	public void setSteeringWheelAngle(double paramAngle) {
		this.steeringWheelAngle = paramAngle;
	}
	
	public double getSteeringWheelAngle() {
		return this.steeringWheelAngle;
	}
	
	public void setSteeringWheelLocks(int paramSteeringWheelLocks) {
		this.steeringWheelLocks = paramSteeringWheelLocks;
	}
	
	public int getSteeringWheelLocks() {
		return this.steeringWheelLocks;
	}
	
	public double getYawOffset() {
		return this.yawOffset;
	}
	public void setYawOffset(double paramYawOffset) {
		this.yawOffset = paramYawOffset;
	}
	
	// COMPONENT -
	
	// METHODS +
	
	private void setHelmetItem(ItemStack paramItem) {
		this.helmetItem = paramItem;
	}
	
	private void move() {
		
		Location newLocation = VehicleMath.getVectorYawLocation(this.getVehicle().getLocation(), this.getOffset(), this.getVehicle().getLocation().getYaw());
		
		double height = super.getFrame().getLocation().getY();
		
		newLocation.setY(height + getOffset().getY());
		
		Location seatLocation = super.getDriverSeat().getLocation();
		
		double deltaX = seatLocation.getX() - newLocation.getX();
		double deltaZ = seatLocation.getZ() - newLocation.getZ();
		
		double theta = Math.atan2(deltaZ, deltaX);
		
		newLocation.setYaw((float) Math.toDegrees(theta));
		
		
		this.getArmorStand().teleport(newLocation);
		this.getSteeringWheel().getArmorStand().setHeadPose(new EulerAngle(0, getMappedSteeringAngle() + this.getYawOffset(), steeringWheelAngle));
	}
	
	private double getMappedSteeringAngle() {
		
		WheelBase wheelBase = (WheelBase) getVehicle().getFirstComponentByType(ComponentName.WHEELBASE.getName());
		
		double wheelAngle = (wheelBase.getSteeringRotation() / wheelBase.getMaxSteeringRotation()) * (steeringWheelLocks * 180);
		return Math.toRadians(wheelAngle);
		
	}
	
	// METHODS -
	
	// CONTROL +
	
	// Spawn
	@Override
	public void spawn(VehicleLocation paramLocation) {
		
		Location location = VehicleMath.getVectorYawLocation(paramLocation, this.getOffset(), paramLocation.getYaw());
		double height = super.getFrame().getLocation().getY();
		location.setY(height + getOffset().getY());
		
		this.setSteeringWheelEntity(new EntitySteeringWheel(this.getVehicle(), Point4D.fromLocation(location)));
		this.applyHelmetItem(this.getHelmetItem());
	}
	
	// Despawn
	@Override
	public void despawn() {
		
		if(this.getSteeringWheelEntity() != null) {
			this.getArmorStand().remove();
		}
		this.setSteeringWheelEntity(null);
		
	}
	
	// Tick
	@Override
	public void tick() {
		this.move();
	}
	
	// CONTROL -
	
	// MAIN +
	
	public SteeringWheelComponent(Vehicle paramVehicle) {
		super(paramVehicle);
		
		this.setYawOffset(YAW_OFFSET());
		this.setHelmetItem(HELMET_ITEM());
		this.setOffset(OFFSET());
		this.setSteeringWheelAngle(STEERINGWHEEL_ANGLE());
		this.setSteeringWheelLocks(STEERINGWHEEL_LOCKS());
	}
	
	// MAIN -
}
