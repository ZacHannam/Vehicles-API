package nea.zachannam.vehicles.api.vehicles.components.wheelbase;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.utils.Point3D;
import nea.zachannam.vehicles.api.utils.VehicleMath;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleLocation;
import nea.zachannam.vehicles.api.vehicles.components.Component;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import net.minecraft.server.v1_16_R1.BlockPosition;

enum Direction {
	
	PRESS_POWER,
	PRESS_BRAKE,
	NO_PRESS;
}

public abstract class WheelBaseComponent extends Component implements WheelBase {

	//-------------------------------------------------------------------- COMPONENT METHODS ------------------------------------------------------------------------

	
	@Override
	public String getComponentName() {
		return ComponentName.WHEELBASE.getName();
	}
	
	public String[] getRequiredComponents() {
		return new String[] {
		};
	}

	//-------------------------------------------------------------------- ABSTRACT METHODS ------------------------------------------------------------------------

	
	public abstract Vector[] WHEEL_OFFSETS();
	public abstract float MAX_STEERING_ROTATION();
	public abstract float STEERING_SPEED();
	public abstract double MAX_SPEED();
	public abstract float NORMALISE_STEERING_AMOUNT();
	public abstract double REVERSE_SPEED_MULTIPLIER();
	public abstract double POWER_ACCELERATION();
	public abstract double IDLE_ACCELERATION();
	public abstract double BRAKE_ACCELERATION();

	
	//-------------------------------------------------------------------- METHODS ------------------------------------------------------------------------
	
	public void addWheel(Wheel paramWheel) {
		this.getWheels().add(paramWheel);
	}
	
	
	public void crash() {
		this.setLinearAcceleration(0);
	}
	
	public Location[] getWheelLocations() {
		Location[] locations = new Location[this.getWheels().size()];
		for(int index = 0; index < this.getWheels().size(); index++) {
			locations[index] = this.getWheels().get(index).getLocation();
		}
		
		return locations;
	}

	
	//-------------------------------------------------------------------- SPAWNING AND DESPAWNING ------------------------------------------------------------------------
	
	@Getter
	@Setter
	private Vector[] wheelOffsets;
	
	
	// Spawn
	@Override
	public void spawn(VehicleLocation paramLocation) {
		
		for(Vector displacement : this.getWheelOffsets()) { // runs through the wheel displacements
			
			this.createWheel(displacement, VehicleMath.getVectorYawLocation(paramLocation, displacement, paramLocation.getYaw())); // creates a wheel at the displacement
		}
	}
	
	// Despawn
	@Override
	public void despawn() {
		this.getWheels().clear(); // clears all of the wheels
	}
	
	//-------------------------------------------------------------------- STEERING ------------------------------------------------------------------------

	
	@Getter
	@Setter
	private float steeringRotation;
	
	@Getter
	@Setter
	private float maxSteeringRotation;
	
	@Getter
	@Setter
	private float steeringSpeed;
	
	@Getter
	@Setter
	private float normaliseSteeringAmount;
	
	// A- Click
	@Override
	public void onButtonPressA() {
		
		// turns steering wheel to the right
		this.setSteeringRotation(Math.max(-this.getMaxSteeringRotation(), this.getSteeringRotation() - this.getSteeringSpeed()));
		
	}
	
	// D- Click
	@Override
	public void onButtonPressD() {
		
		// turns steering wheel to the left
		this.setSteeringRotation(Math.min(this.getMaxSteeringRotation(), this.getSteeringRotation() + this.getSteeringSpeed()));
		
	}
	
	// Space- Click
	@Override
	public void onButtonPressSpace() {
		this.setSteeringRotation(0); // centres the steering wheel
	}
	
	
	public void normaliseSteering() {
		if(this.getSteeringRotation() > 0) {
			this.setSteeringRotation((float) Math.max(0, this.getSteeringRotation() - this.getNormaliseSteeringAmount() * (this.getSpeed() / this.getMaxSpeed())));
		} else if(this.getSteeringRotation() < 0) {
			this.setSteeringRotation((float) Math.min(0,  this.getSteeringRotation() + this.getNormaliseSteeringAmount() * (this.getSpeed() / this.getMaxSpeed())));
		}
	}
	
	//-------------------------------------------------------------------- SPEED CONTROLS ------------------------------------------------------------------------
	
	@Getter
	@Setter
	private double powerAcceleration;
	
	@Getter
	@Setter
	private double idleAcceleration;
	
	@Getter
	@Setter
	private double brakeAcceleration;
	
	@Getter
	@Setter
	private double linearAcceleration;
	
	@Getter
	@Setter
	private double maxSpeed;
	
	@Getter
	@Setter
	private Direction direction;
	
	@Getter
	@Setter
	private boolean canSwitchReverse;
	
	@Getter
	@Setter
	private boolean inReverse;
	
	@Override
	public void onButtonPressW() {
		
		if(this.isCanSwitchReverse() && this.getLinearAcceleration() <= 0) {
			this.setInReverse(false);
		}
		
		if(this.isInReverse()) {
			this.setDirection(Direction.PRESS_BRAKE);
			this.setLinearAcceleration(Math.max(this.getLinearAcceleration() - this.getBrakeAcceleration(), 0));
		} else {
			this.setDirection(Direction.PRESS_POWER);
			this.setLinearAcceleration(Math.min(this.getLinearAcceleration() + this.getPowerAcceleration(), maxSpeed));
		}
		this.setCanSwitchReverse(false);
	}
	
	@Override
	public void onButtonPressS() {
		
		if(this.isCanSwitchReverse() && this.getLinearAcceleration() <= 0) {
			this.setInReverse(true);
		}
		
		if(this.isInReverse()) {
			this.setDirection(Direction.PRESS_POWER);
			this.setLinearAcceleration(Math.min(this.getLinearAcceleration() + this.getPowerAcceleration(), maxSpeed));
		} else {
			this.setDirection(Direction.PRESS_BRAKE);
			this.setLinearAcceleration(Math.max(this.getLinearAcceleration() - this.getBrakeAcceleration(), 0)); // used when breaking
		}
		this.setCanSwitchReverse(false);
	}
	
	@Override
	public void noButtonPressForward() {
		if(this.getDirection() == Direction.PRESS_BRAKE) {
			
		}
		this.setDirection(Direction.NO_PRESS);
		this.setLinearAcceleration(Math.max(this.getLinearAcceleration() - this.getIdleAcceleration(), 0)); // used to get certain number of dp.
		
		this.setCanSwitchReverse(true);
	}
	
	public double getSpeed() {
		switch(this.getDirection())	{
		case PRESS_POWER:
			return Math.sqrt(Math.pow(this.getMaxSpeed(), 2) - Math.pow((this.getLinearAcceleration()-this.getMaxSpeed()), 2));
		case PRESS_BRAKE:
			return this.getLinearAcceleration();
		case NO_PRESS:
			return Math.sqrt(Math.pow(this.getMaxSpeed(), 2) - Math.pow((this.getLinearAcceleration()-this.getMaxSpeed()), 2));
		default:
			return 0;
		}
	}
	
	//-------------------------------------------------------------------- TICK ------------------------------------------------------------------------
	
	@Override
	public void tick() {
		
		updatePosition();
		
		normaliseSteering();
	}
	
	//-------------------------------------------------------------------- POSITION CONTROL ------------------------------------------------------------------------
	
	
	@Getter
	@Setter
	private ForceManager forceManager;
	
	
	@Getter
	@Setter
	private VehicleLocation lastLocation;
	
	@Getter
	@Setter
	private double reversePower;
	
	private void updatePosition() {
		
		this.getForceManager().addEntry(this.getVehicle().getLocation().getYaw() + this.getSteeringRotation(), this.getSpeed());
		
		Force force = this.getForceManager().calculatePositions(); // calculates the next position the vehicle will go to
		
		double nextSpeed = this.isInReverse() ? force.getMagnitude() * this.getReversePower(): force.getMagnitude();
	
		VehicleLocation location = VehicleMath.getNextLocation(super.getVehicle().getLocation(), force.getTheta() - this.getVehicle().getLocation().getYaw(), nextSpeed, this.getLength(), this.isInReverse());
	
		if(super.getVehicle().isOtherVehicle(location)) {
			this.crash();
			return;
		}
		
		Point3D[] nextWheelLocations = new Point3D[4];
		int completedWheels = 0;
		for(int index = 0; index < wheels.size(); index++) {
			Wheel wheel = this.getWheels().get(index);
			Location wheelLocation = VehicleMath.getVectorYawLocation(location, wheel.getOffset(), location.getYaw());
			Point3D nextLocation = wheel.nextLocation(wheelLocation);
			if(nextLocation == null) {
				break;
			}
			nextWheelLocations[index] = nextLocation;
			completedWheels++;
		}
		
		if(completedWheels == this.getWheels().size()) {
			
			double y = 0;
			for(Wheel wheel : this.getWheels()) {
				y += wheel.getLocation().getY();
			}
			y /= wheels.size();
			
			location.setY(y);
			
			this.getVehicle().setLocation(location);
			
			this.setLastLocation(location);
			for(int index = 0; index < wheels.size(); index++) {
				Wheel wheel = this.getWheels().get(index);
				wheel.setLocation(nextWheelLocations[index]);
			}
		} else {
			this.crash();
			
		}
		
		this.getForceManager().degrade(this.getSurfaceFriction(), Arrays.asList(this.getVehicle().getLocation().getYaw()));
	}
	
	public double getSurfaceFriction() {
		
		double totalFriction = 0;
		
		for(Wheel wheel : wheels) {
			Block block = (Block) wheel.getLocation().clone().add(0, -1, 0).getBlock();
			CraftBlock craftBlock = CraftBlock.at(((CraftWorld)block.getWorld()).getHandle(),
					new BlockPosition(block.getX(), block.getY(), block.getZ()));
			
			totalFriction += craftBlock.getNMS().getBlock().getFrictionFactor();
		}
		
		
		return totalFriction/(wheels.size());
	}


	//-------------------------------------------------------------------- VEHICLE METHODS ------------------------------------------------------------------------
	
	public void playEffectAtWheels(Effect paramEffect) {
		
		Location[] wheelLocations = this.getWheelLocations();
		
		if(wheelLocations.length <= 0) {
			return;
		}
		
		World world = wheelLocations[0].getWorld();
		
		for(Location location : wheelLocations) {
			world.playEffect(location.clone().add(0, -0.5, 0), paramEffect, 1);
		}
	}
	

	//-------------------------------------------------------------------- SETUP ------------------------------------------------------------------------
	
	@Getter
	@Setter
	private double length;
	
	@Getter
	@Setter
	private double width;
	
	@Getter
	@Setter
	private ArrayList<Wheel> wheels = new ArrayList<Wheel>();
	
	private Wheel createWheel(Vector paramOffset, Location paramLocation) {
		
		Wheel wheel = new Wheel(paramOffset, paramLocation);
	
		this.addWheel(wheel);
		return wheel;
		
	}
	
	private void calculateWidthAndLength() {
		
		double max_width = 0;
		double min_width = 0;
		
		double max_length = 0;
		double min_length = 0;
		
		for(Vector displacement : wheelOffsets) {
			if(displacement.getX() < min_width) {
				min_width = displacement.getX();
			}
			if(displacement.getX() > max_width) {
				max_width = displacement.getX();
			}
			if(displacement.getZ() > max_length) {
				max_length = displacement.getZ();
			}
			if(displacement.getZ() < min_length) {
				min_length = displacement.getZ();
			}
		}
		
		
		this.setWidth(max_length - min_length);
		this.setLength(max_width - min_width);
		
	}
	
	public WheelBaseComponent(Vehicle paramVehicle) {	
		super(paramVehicle);
		
		this.setWheelOffsets(this.WHEEL_OFFSETS());
		this.setMaxSteeringRotation(this.MAX_STEERING_ROTATION());
		this.setSteeringSpeed(this.STEERING_SPEED());
		this.setMaxSpeed(this.MAX_SPEED());
		this.setNormaliseSteeringAmount(this.NORMALISE_STEERING_AMOUNT());
		this.setReversePower(this.REVERSE_SPEED_MULTIPLIER());
		this.setBrakeAcceleration(this.BRAKE_ACCELERATION());
		this.setPowerAcceleration(this.POWER_ACCELERATION());
		this.setIdleAcceleration(this.IDLE_ACCELERATION());
		
		this.setForceManager(new ForceManager());
		this.setDirection(Direction.NO_PRESS);
		this.setLinearAcceleration(0);
		
		this.calculateWidthAndLength();
	}
}
