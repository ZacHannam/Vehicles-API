package nea.zachannam.vehicles.api.vehicles.components.seat.driverseat;

import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;

import nea.zachannam.vehicles.api.vehicles.Vehicle;
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
	
	// COMPONENT METHODS -
	
	// ABSTRACT METHODS + 
	
	public abstract boolean SHOW_STEERING_DISPLAY();
	public abstract int NUMBER_OF_CHARACTERS_ON_DISPLAY();
	
	// ABSTRACT METHODS -
	
	// VARIABLES +
	
	private boolean showSteeringDisplay;
	private int numberOfCharacters;
	private float maxSteeringAngle;
	private float steeringAngleRange;
	
	// VARIABLES -
	
	// COMPONENT +
	
	public void setShowSteeringDisplay(boolean paramShowSteeringDisplay) {
		this.showSteeringDisplay = paramShowSteeringDisplay;
	}
	
	public boolean getShowSteeringDisplay() {
		return this.showSteeringDisplay;
	}
	
	public void setNumberOfCharactersOnDisplay(int paramNumberOfCharacters) {
		this.numberOfCharacters = paramNumberOfCharacters;
	}
	
	public int getNumberOfCharactersOnDisplay() {
		return this.numberOfCharacters;
	}
	
	public void setMaxSteeringAngle(float paramMaxSteeringAngle) {
		this.maxSteeringAngle = paramMaxSteeringAngle;
	}
	
	public float getMaxSteeringAngle() {
		return this.maxSteeringAngle;
	}
	
	public void setSteeringAngleRange(float paramSteeringAngleRange) {
		this.steeringAngleRange = paramSteeringAngleRange;
	}
	
	public float getSteeringAngleRange() {
		return this.steeringAngleRange;
	}
	
	// COMPONENT -
	
	// METHODS +
	// METHODS -
	
	// CONTROL +
	
	// Tick
	@Override
	public void tick() {
		
		super.tick();
		
		if(this.getShowSteeringDisplay() && this.hasRider()) {

			if(getMaxSteeringAngle() == -1) {
				setMaxSteeringAngle(super.getWheelBase().getMaxSteeringRotation());
				setSteeringAngleRange(getMaxSteeringAngle() * 2);
			}

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
	
	// CONTROL -
	
	// MAIN +
	
	public DriverSeatComponent(Vehicle paramVehicle) {
		super(paramVehicle);
	
		this.setMaxSteeringAngle(-1);
		this.setNumberOfCharactersOnDisplay(NUMBER_OF_CHARACTERS_ON_DISPLAY());
		this.setShowSteeringDisplay(SHOW_STEERING_DISPLAY());
	}
	
	// MAIN -
	
}