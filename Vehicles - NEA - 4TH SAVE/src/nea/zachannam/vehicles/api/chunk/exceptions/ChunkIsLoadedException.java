package nea.zachannam.vehicles.api.chunk.exceptions;

public class ChunkIsLoadedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChunkIsLoadedException() {
		super("Can not add vehicle to buffer as chunk is already loaded!");
	}
}
