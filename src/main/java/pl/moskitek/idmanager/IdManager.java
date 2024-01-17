package pl.moskitek.idmanager;

public interface IdManager {	
	void free(int id);
	void free(int[] ids);
	int allocate();
	int[] allocate(int length);
	boolean isAllocated( int id );
	void reset();
	int allocatedSize();
	
	int nextSetBit(int id);
	int prevSetBit(int id);
	void allocateConrete(int id);
	
	public IdManager cloneTree( ) throws Exception;
	
	public static IdManagerBuilder builder() {
		return new IdManagerBuilder();
	}
}
