package nea.zachannam.vehicles.api.vehicles.components.seat.driverseat;

import nea.zachannam.vehicles.api.vehicles.components.seat.Seat;

public interface DriverSeat extends Seat {

	public void setShowSteeringDisplay(boolean paramShowSteeringDisplay);
	public boolean isShowSteeringDisplay();
	public void setNumberOfCharactersOnDisplay(int paramNumberOfCharacters);
	public int getNumberOfCharactersOnDisplay();
	public void setMaxSteeringAngle(float paramMaxSteeringAngle);
	public float getMaxSteeringAngle();
	public void setSteeringAngleRange(float paramSteeringAngleRange);
	public float getSteeringAngleRange();
	
	
}