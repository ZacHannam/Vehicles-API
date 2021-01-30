package nea.zachannam.vehicles.api.chunk.exceptions;

public class ChunkBufferFullException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChunkBufferFullException() {
		super("The chunk buffer is full.");
	}

}
