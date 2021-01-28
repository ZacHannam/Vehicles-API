package nea.zachannam.vehicles.api.vehicles.components.seat.driverseat;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;
import nea.zachannam.vehicles.api.vehicles.components.seat.SeatComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle.EnumTitleAction;

public abstract class DriverSeatComponent extends SeatComponent implements DriverSeat {

	//-------------------------------------------------------------------- COMPONENT METHODS ------------------------------------------------------------------------
	
	@Override
	public String getComponentName() {
		return ComponentName.DRIVER_SEAT.getName();
	}

	//-------------------------------------------------------------------- ABSTRACT METHODS ------------------------------------------------------------------------

	public abstract boolean SHOW_STEERING_DISPLAY();
	public abstract int NUMBER_OF_CHARACTERS_ON_DISPLAY();

	//-------------------------------------------------------------------- VARIABLES ------------------------------------------------------------------------


	@Getter
	@Setter
	private boolean showSteeringDisplay;
	
	@Getter
	@Setter
	private int numberOfCharacters;
	
	@Getter
	@Setter
	private float maxSteeringAngle;
	
	@Getter
	@Setter
	private float steeringAngleRange;

	//-------------------------------------------------------------------- TICK ------------------------------------------------------------------------

	@Override
	public void tick() {

		super.tick();

		if(this.isShowSteeringDisplay() && this.hasRider()) {

			double steeringAngle = super.getWheelBase().getSteeringRotation();

			double percentageIn = (getMaxSteeringAngle() + steeringAngle) / getSteeringAngleRange();

			int switchCharInt = (int) Math.floor(percentageIn * this.getNumberOfCharacters());

			String start = "";
			String end = "";

			for(int index = 0; index < this.getNumberOfCharacters(); index++) {
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
	
	//-------------------------------------------------------------------- SETUP ------------------------------------------------------------------------

	private void setup() {
		setMaxSteeringAngle(super.getWheelBase().getMaxSteeringRotation());
		setSteeringAngleRange(getMaxSteeringAngle() * 2);
	}
	

	//-------------------------------------------------------------------- CONSTRUCTOR ------------------------------------------------------------------------

	public DriverSeatComponent(Vehicle paramVehicle) {
		super(paramVehicle);
		
		this.setNumberOfCharacters(NUMBER_OF_CHARACTERS_ON_DISPLAY());
		this.setShowSteeringDisplay(SHOW_STEERING_DISPLAY());
		
		setup();
	}


}