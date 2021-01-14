package nea.zachannam.vehicles.api.vehicles.components.wheelbase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Force {
	
	private double theta;
	private double magnitude;
	
	public Force(double paramTheta, double paramMagnitude) {
		this.theta = paramTheta;
		this.magnitude = paramMagnitude;
	}
	
	public double getTheta() {
		return this.theta;
	}
	
	public double getMagnitude() {
		return this.magnitude;
	}
}

public class ForceManager extends HashMap<Double, Double> {

	private static final long serialVersionUID = 3376711873046792627L;
	
	public void addEntry(double paramTheta, double d) {
		paramTheta %= 2*Math.PI;
		if(!this.containsKey(paramTheta) || d > this.get(paramTheta)) {
			this.put(paramTheta, d);
		}
	}
	
	public void degrade(double paramDegradeSize, List<Double> paramIgnore) {
		HashMap<Double, Double> newMap = new HashMap<Double, Double>();
		for(Map.Entry<Double, Double> entry : this.entrySet()) {
			if(paramIgnore.contains(entry.getKey()%2)) continue;
			Double newValue = (entry.getValue() * paramDegradeSize);
			if(Math.round(newValue) != 0.0) {
				newMap.put(entry.getKey(), newValue);
			}
		}
		this.clear();
		for(Map.Entry<Double, Double> entry : newMap.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}
	
	public Force calculatePositions() {
		double positionX = 0;
		double positionZ = 0;
		for(Map.Entry<Double, Double> entry : this.entrySet()) {
			double theta = (Math.PI / 2) - entry.getKey();
			positionX += (Math.cos(theta) * entry.getValue());
			positionZ += (Math.sin(theta) * entry.getValue());
		}
		positionX /= this.size();
		positionZ /= this.size();
		double magnitude = Math.sqrt(Math.pow(positionX, 2) + Math.pow(positionZ, 2));
		double theta = positionX == 0.0 ? 0.0 : (Math.PI / 2) - Math.atan(positionZ/positionX);
		return (new Force(theta, magnitude));
	}
}
