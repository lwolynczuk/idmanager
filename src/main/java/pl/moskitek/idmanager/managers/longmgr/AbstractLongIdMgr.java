package pl.moskitek.idmanager.managers.longmgr;

import java.util.Arrays;

import pl.moskitek.idmanager.IdManager;

public abstract class AbstractLongIdMgr implements IdManager{
	protected static final long REST_MASK = Long.parseLong("111111", 2);
	
	protected static final long powers[];

	protected long[][] longTree;
	
	protected int currentTreeLevel;
	
	protected int currentSize;
	protected long maxCapacity;
	
	protected boolean initialLoad;
	protected int initialIdx;
	
	
	static{
		powers = new long[16];
		for( int i = 0; i <= 15; i++ ){
			long p = pow( 64, i);
			powers[ i ] = Long.numberOfTrailingZeros(p);
		}
	}
	public static long pow( int base, int pow ){
		long ret = 1;
		for( int i = 0; i < pow; i++ ){
			ret *= base;
		}
		return ret;
	}
	
	public AbstractLongIdMgr(){
		initTree();
	}
	private void initTree(){
		currentTreeLevel = 1;
		currentSize = 0;
		maxCapacity = 64;
		initialLoad = true;
		initialIdx = 0;
		longTree = new long[2][];
		longTree[0] = new long[1];
		longTree[1] = new long[1];
	}
	
	protected void expand( long[][] oldTree, boolean force ){
		expandTree( force );
		int oldNewOffset = oldTree.length == longTree.length ? 0 : -1;
		long newArraySize = (long)oldTree[currentTreeLevel + oldNewOffset].length * (long)2;
		if( newArraySize > Integer.MAX_VALUE - 5 ){
			longTree[currentTreeLevel] = Arrays.copyOf(oldTree[currentTreeLevel + oldNewOffset], Integer.MAX_VALUE - 5);
		}else{
			longTree[currentTreeLevel] = 
					Arrays.copyOf(oldTree[currentTreeLevel + oldNewOffset], oldTree[currentTreeLevel + oldNewOffset].length * 2 );
		}
		
		for(int i = currentTreeLevel - 1; i > 0; i--){
			int lowerLevelLength = oldTree[ i + 1 + oldNewOffset].length;
			long currentILevelLength = ( oldTree[ i + oldNewOffset ].length << 6);
			if( lowerLevelLength >= currentILevelLength ){
				longTree[i] = Arrays.copyOf(oldTree[i + oldNewOffset], oldTree[i + oldNewOffset].length * 2 );
			}else{
				longTree[i] = Arrays.copyOf(oldTree[i + oldNewOffset], oldTree[i + oldNewOffset].length);
			}
		}
		
		maxCapacity *= 2;
	}

	public int getHight(){
		return longTree.length;
	}
	protected void expandTree(boolean force){
		if( ~longTree[ 0 ][ 0 ] == 0 || force){
			currentTreeLevel++;
			longTree = new long[ currentTreeLevel + 1][];
			
			longTree[ 0 ] = new long[ 1 ];
			longTree[ 0 ][ 0 ] = 1;
		}
	}


	@Override
	public boolean isAllocated( int id ){
		final int currentBucket = id >> 6;
		final int idx = (int)(id & REST_MASK);
		if( currentBucket >= longTree[currentTreeLevel].length ){
			return false;
		}
		long currentIdx = (long)1 << idx;
		boolean allocated = ( longTree[ currentTreeLevel ][ currentBucket ] & currentIdx ) == currentIdx;
		return allocated;
	}
	
	@Override
	public void free(int[] ids){
		for( int id : ids ){
			free(id);
		}
	}
	
	
	@Override
	public void reset(){
		initTree();
	}
	
	@Override
	public int allocatedSize(){
		return currentSize;
	}
	
	public int[] getNodeSizeOnEachLevel(){
		int[] nodesSizes = new int[ longTree.length ];
		for( int i = 0; i < longTree.length; i++){
			nodesSizes[i] = longTree[i].length;
		}
		return nodesSizes;
	}
	
	@Override
	public void allocateConrete(int id) {
		if( id < 0 ){
			throw new RuntimeException("Id must be above 0, you try to set concreteId: " + id );
		}
		if( id > ((Integer.MAX_VALUE - 5 ) * 64L ) ){
			throw new RuntimeException(String.format( "You try to allocate value above limit: %d, where max is: %d ", id, ( Integer.MAX_VALUE - 5 ) * 64 ) );
		}
		int maxHeight = 1;
		for( long i = 64; i <= id; i *= 64, maxHeight++ );
		while( maxHeight > longTree.length ){
			expand( longTree, true );
		}
			
		while( maxCapacity <= id ){
			expand( longTree, false );
		}
		initialLoad = false;
		currentSize++;
		allocateConrete( 0, id );
	}
	
	protected abstract void allocateConrete(final int height, int id);
	
	@Override
	public IdManager cloneTree( ) throws Exception {
		AbstractLongIdMgr toReturn = getClass().newInstance();
		toReturn.longTree = new long[ longTree.length ][];
		for( int i = 0; i < longTree.length; i++ ){
			toReturn.longTree[ i ] = new long[ longTree[i].length ];
			for( int k = 0; k < longTree[i].length; k++ ){
				toReturn.longTree[ i ][ k ] = longTree[i][k];
			}
			
		}
		toReturn.currentTreeLevel = currentTreeLevel;
		toReturn.maxCapacity = maxCapacity;
		toReturn.currentSize = currentSize;
		toReturn.initialLoad = initialLoad;
		toReturn.initialIdx = initialIdx;
		return toReturn;
	}

}
