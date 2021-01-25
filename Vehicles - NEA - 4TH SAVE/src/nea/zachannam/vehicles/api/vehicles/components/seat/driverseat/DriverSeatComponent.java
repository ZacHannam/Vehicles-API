package nea.zachannam.vehicles.api.vehicles.components.seat.driverseat;

import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.VehicleLocation;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.seat.SeatComponent;
import net.minecraft.server.v1_16_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R1.PacketPlayOutTitle.EnumTitleAction;

public abstract class DriverSeatComponent extends SeatComponent implements DriverSeat {

	// COMPONENT METHODS +
	
	@Override
	public String getComponentName() {
		return ComponentName.DRIVER_SEAT.getName();
	}
	
	//-------------------------------------------------------------------- ABSTRACT METHODS ------------------------------------------------------------------------
	
	public abstract boolean SHOW_STEERING_DISPLAY();
	public abstract int NUMBER_OF_CHARACTERS_ON_DISPLAY();
	
	//-------------------------------------------------------------------- SETUP ------------------------------------------------------------------------
	
	@Getter
	@Setter
	private boolean showSteeringDisplay;
	
	@Getter
	@Setter
	private int numberOfCharactersOnDisplay;
	
	@Getter
	@Setter
	private float maxSteeringAngle;
	
	@Getter
	@Setter
	private float steeringAngleRange;
	
	private void refreshMaxSteeringAngle() {
		
		setMaxSteeringAngle(super.getWheelBase().getMaxSteeringRotation());
		setSteeringAngleRange(getMaxSteeringAngle() * 2);
	}
	
	//-------------------------------------------------------------------- TICK ------------------------------------------------------------------------
	
	@Override
	public void tick() {
		
		super.tick();
		
		if(this.isShowSteeringDisplay() && this.hasRider()) {

			double steeringAngle = super.getWheelBase().getSteeringRotation();

			double percentageIn = (getMaxSteeringAngle() + steeringAngle) / getSteeringAngleRange();

			int switchCharInt = (int) Math.floor(percentageIn * getNumberOfCharactersOnDisplay());

			String start = "";
			String end = "";

			for(int index = 0; index < getNumberOfCharactersOnDisplay(); index++) {
				if(index < switchCharInt) {
					start += "<";
				} else if (index > switchCharInt) {
					end += ">";
				}
			}
			
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR,
					ChatSerializer.a(("[\"\",{\"text\":\"%start%\",\"color\":\"yellow\"},{\"text\":\"|\",\"bold\":true,\"color\":\"dark_red\"},{\"text\":\"%end%\",\"color\":\"yellow\"}]").replace("%start%", start).replace("%end%", end)));
			((CraftPlayer) this.getRider()).getHandle().playerConnection.sendPacket(title);
			
		}
		
	}
	
	//-------------------------------------------------------------------- SPAWNING AND DESPAWNING ------------------------------------------------------------------------
	
	@Override
	public void spawn(VehicleLocation paramLocation) {
		super.spawn(paramLocation);
		
		refreshMaxSteeringAngle();
	}
	
	
	//-------------------------------------------------------------------- CONSTRUCTOR ------------------------------------------------------------------------
	
	public DriverSeatComponent(Vehicle paramVehicle) {
		super(paramVehicle);
	
		
		
		this.setNumberOfCharactersOnDisplay(NUMBER_OF_CHARACTERS_ON_DISPLAY());
		this.setShowSteeringDisplay(SHOW_STEERING_DISPLAY());
	}
}