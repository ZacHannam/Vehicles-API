package nea.zachannam.vehicles.api.vehicles.components.frame;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.utils.Point2D;
import nea.zachannam.vehicles.api.utils.Point4D;
import nea.zachannam.vehicles.api.utils.VehicleMath;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleLocation;
import nea.zachannam.vehicles.api.vehicles.components.Component;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.frame.entity.EntityFrame;

public abstract class FrameComponent extends Component implements Frame {
	
	// COMPONENT METHODS +
	
	public String getComponentName() {
		return ComponentName.FRAME.getName();
	}
	
	public String[] getRequiredComponents() {
		return new String[] {
				ComponentName.WHEELBASE.getName()
		};
	}
	
	// COMPONENT METHODS -
	
	// ABSTRACT METHODS +
	
	public abstract ItemStack HELMET_ITEM();
	public abstract Vector OFFSET();
	public abstract double HEIGHT();
	public abstract double WIDTH();
	public abstract double LENGTH();
	public abstract double YAW_OFFSET();
	
	// ABSTRACT METHODS -
	
	// VARIABLES +
	
	@Getter
	@Setter
	private EntityFrame frameEntity;
	
	@Getter
	@Setter
	private Vector offset;
	
	@Getter
	@Setter
	private double width;
	
	@Getter
	@Setter
	private double height;
	
	@Getter
	@Setter
	private double length;
	
	@Getter
	private ItemStack helmetItem;
	
	@Getter
	@Setter
	private double yawOffset;
	// VARIABLES -
	
	// COMPONENT +
	
	public ArmorStand getArmorStand() {
		return this.frameEntity.getArmorStand();
	}
	
	@SuppressWarnings("deprecation")
	public void applyHelmetItem(ItemStack paramItem) {
		this.setHelmetItem(paramItem);
		if(this.getFrameEntity() != null) {
			this.getArmorStand().setHelmet(this.getHelmetItem());
		}
	}
	
	public double getLongestBodyLength() {
		return Collections.max(Arrays.asList(this.getWidth(), this.getLength(), this.getHeight()));
	}
	
	public double getShortestBodyLength() {
		return Collections.min(Arrays.asList(this.getWidth(), this.getLength(), this.getHeight()));
	}
	
	public Point2D[] getFrameCorners() {
		Point2D[] points = new Point2D[4];
		
		VehicleLocation location = super.getVehicle().getLocation();
		double yaw = this.getYawOffset() + location.getYaw();
		
		points[0] = Point2D.fromLocation(VehicleMath.getVectorYawLocation(location, new Vector(getWidth() / 2, 0, getLength() / 2), yaw));
		points[1] = Point2D.fromLocation(VehicleMath.getVectorYawLocation(location, new Vector(-getWidth() / 2, 0, getLength() / 2), yaw));
		points[2] = Point2D.fromLocation(VehicleMath.getVectorYawLocation(location, new Vector(-getWidth() / 2, 0, -getLength() / 2), yaw));
		points[3] = Point2D.fromLocation(VehicleMath.getVectorYawLocation(location, new Vector(getWidth() / 2, 0, -getLength() / 2), yaw));
	
		return points;
	}

	// COMPONENT -
	
	// METHODS +

	
	private void setHelmetItem(ItemStack paramItem) {
		this.helmetItem = paramItem;
	}
	
	public void updatePosition() {
		
		Location newLocation = VehicleMath.getVectorYawLocation(getVehicle().getLocation(), this.getOffset(), this.getVehicle().getLocation().getYaw());
		
		this.getFrameEntity().setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), 0, 0);
		
		this.getArmorStand().setHeadPose(new EulerAngle(0, super.getVehicle().getLocation().getYaw() + this.getYawOffset(), 0));
	}
	
	// METHODS -
	
	// CONTROL +
	
	// Tick
	@Override
	public void tick() {
		updatePosition();
	}
	
	// Spawn
	@Override
	public void spawn(VehicleLocation paramLocation) {

		Location location = VehicleMath.getVectorYawLocation(paramLocation, this.getOffset(), paramLocation.getYaw());
		
		this.setFrameEntity(new EntityFrame(super.getVehicle(), Point4D.fromLocation(location)));
		this.applyHelmetItem(this.getHelmetItem());
	}
	
	// Despawn
	@Override
	public void despawn() {
		if(this.getFrameEntity() != null) {
			this.getArmorStand().remove();
		}
		this.setFrameEntity(null);
	}
	
	// CONTROL -
	
	// MAIN +
	
	public FrameComponent(Vehicle paramVehicle) {
		super(paramVehicle);
		
		this.setYawOffset(this.YAW_OFFSET());
		this.setOffset(this.OFFSET());
		this.setHelmetItem(this.HELMET_ITEM());
		this.setHeight(HEIGHT());
		this.setLength(LENGTH());
		this.setWidth(WIDTH());
	}
	
	// MAIN -
}
