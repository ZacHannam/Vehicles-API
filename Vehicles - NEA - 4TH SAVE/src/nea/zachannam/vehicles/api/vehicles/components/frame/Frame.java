package nea.zachannam.vehicles.api.vehicles.components.frame;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import nea.zachannam.vehicles.api.utils.Point2D;

public interface Frame {

	public double getLongestBodyLength();
	public double getShortestBodyLength();
	public Point2D[] getFrameCorners();
	public void setHeight(double paramHeight);
	public double getHeight();
	public void setLength(double paramLength);
	public double getLength();
	public void setWidth(double paramWidth);
	public double getWidth();
	public void setOffset(Vector paramOffset);
	public Vector getOffset();
	public void setYawOffset(double paramYaw);
	public double getYawOffset();
	public Location getLocation();
}
