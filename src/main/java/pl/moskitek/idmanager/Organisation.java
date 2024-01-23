package pl.moskitek.idmanager;

/**
 * Organization describes what data structure is used to store data.
 * 
 * @author moskitek
 */
public enum Organisation{
	/**
	 * BitSet tree is built using java long (64 bits per one bucket)
	 */
	longBits, 
	/**
	 * same as above but used ints (32 bits per bucket)
	 */
	intBits, 
	/**
	 * implementation uses java.util.BitSet for all operation.
	 * This version is used mainly for validation and performance tests
	 */
	javaBased
}
