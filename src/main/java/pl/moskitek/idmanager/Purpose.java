package pl.moskitek.idmanager;

/**
 * What operation are available in IdManager.
 * Simple implementation of IdManager offers marking gap. Using 0 and 1 values,
 * there is no posibility to store more information. Because of that, when 
 * only one BitSet is used - only one informatino is available: 
 * - minimum available value (first gap)
 * - maximum available value (last gap)
 * 
 * To be possible to return both values, minimum and maximum, two IdManagers
 * have to be created - one for minimum, one for maximum and they works as delegates.
 * 
 * @author moskitek
 */
public enum Purpose{
	Minimum, 
	Maximum, 
	MinMax, 
}