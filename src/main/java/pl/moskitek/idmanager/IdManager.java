package pl.moskitek.idmanager;

public interface IdManager {	
	/**
	 * Frees previously allocated number
	 * @param id - previously allocated number
	 */
	void free(int id);
	/**
	 * Version of bulk free operation. 
	 * All items in array has to be previously allocated.
	 * 
	 * @param ids - array of allocated ids
	 */
	void free(int[] ids);
	/**
	 * Allocate and returns one number. When there is no free in "operation
	 * history" for that idManager, it has similar meaning as i++ (returns 
	 * next number). 
	 * This operation may result with extending the tree.
	 * 
 	 * @return next number (when no free has already been called), first gap 
 	 * otherwise. 
	 */
	int allocate();
	/**
	 * Bulk operation of allocate.
	 * 
	 * @param length - number of elements to allocate
	 * @return array of allocated numbers. Array is sorted in ascending order.
	 * 		   Obviusly - as allocate always returns first gap, or next number.
	 */
	int[] allocate(int length);
	/**
	 * Checks id given number has been already  allocated.
	 * 
	 * @param id number to check
	 * @return true - if allocated, false if free
	 */
	boolean isAllocated( int id );
	/**
	 * Clear the idManager.
	 */
	void reset();
	/**
	 * Returns number of allocated elements.
	 * @return Number of allocation - number of free operations
	 */
	int allocatedSize();
	
	
	int nextSetBit(int id);
	int prevSetBit(int id);
	
	/**
	 * Marks given id as allocated. 
	 * For example allocateConrete(1) and allocateConrete(100) creates a set with 
	 * positions 2..99 set to zero, and it can be considered as big gap. Next allocate()
	 * returns 2
	 * 
	 * @param id - number to mark
	 */
	void allocateConrete(int id);
	
	/**
	 * Cloning the instance. There is no connection between newly created clone
	 */
	public IdManager cloneTree( ) throws Exception;
	
	/**
	 * @return IdManager builder
	 */
	public static IdManagerBuilder builder() {
		return new IdManagerBuilder();
	}
}
