package pl.moskitek.idmanager.managers.java;

import java.util.BitSet;

import pl.moskitek.idmanager.IdManager;

/**
 * Simple implementation with Java BitSet implementation.
 * This class is used for checking correctness of IdManager and to compare 
 * performance
 * 
 * @author Lukasz Wolynczuk
 */
public class JavaBitSetIdManager implements IdManager{
	private final BitSet bSet;
	
	private int allocatedSize;
	
	public JavaBitSetIdManager() {
		this(new BitSet());
	}
	public JavaBitSetIdManager(BitSet bSet) {
		this.bSet = bSet;
	}
	@Override
	public void allocateConrete(int id){
		allocatedSize++;
		bSet.set(id);
	}
	@Override
	public int nextSetBit(int id){
		return bSet.nextSetBit(id);
	}
	
	@Override
	public int prevSetBit(int id) {
		return bSet.previousSetBit(id);
	}
	
	@Override
	public void free(int[] ids) {
		for( int id : ids ){
			free(id);
		}		
	}
	@Override
	public int[] allocate(int length) {
		int[] ids = new int[length];
		for( int i = 0; i < length; i++ ){
			ids[ i ] = allocate();
		}
		return ids;
	}
	
	@Override
	public int allocatedSize(){
		return allocatedSize;
	}
	@Override
	public int allocate(){
		allocatedSize++;
		int id = bSet.nextClearBit(0);
		bSet.set(id);
		return id;
	}
	
	@Override
	public boolean isAllocated(int id){
		return bSet.get(id);
	}
	@Override
	public void free(int id){
		if( !bSet.get(id)){
			throw new RuntimeException("Attempt to free position that is not set: " + id);
		}
		allocatedSize--;
		bSet.clear(id);
	}
	
	@Override
	public void reset(){
		allocatedSize = 0;
		bSet.clear();
	}
	
	@Override
	public JavaBitSetIdManager cloneTree() {
		JavaBitSetIdManager idmgr = new JavaBitSetIdManager( (BitSet)bSet.clone() );
		idmgr.allocatedSize = allocatedSize;
		return idmgr;
	}
}

