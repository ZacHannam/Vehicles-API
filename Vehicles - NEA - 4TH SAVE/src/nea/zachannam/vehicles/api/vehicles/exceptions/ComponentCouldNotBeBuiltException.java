package nea.zachannam.vehicles.api.vehicles.exceptions;

public class ComponentCouldNotBeBuiltException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7732504598309131539L;
	
	public ComponentCouldNotBeBuiltException(String paramComponentName) {
		super(paramComponentName);
	}

}
