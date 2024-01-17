package pl.moskitek.idmanager.managers.longmgr;

public class LongIdManagerForMinimum extends AbstractLongIdMgr{

	@Override
	public int nextSetBit(int id) {
		throw new RuntimeException("This manager is for minimum, not allowed call.");
	}
	
	@Override
	public int prevSetBit(int id) {
		throw new RuntimeException("This manager is for minimum, not allowed call.");
	}
	
	@Override
	public int[] allocate(int length){
		int[] newIds = new int[length];
		for(int i = 0; i < length; i++ ){
			newIds[ i ] = allocate(); 
		}
		return newIds;
	}	
	
	private boolean traverseTree(final int height, final long[] toReturn, final int bucketIdx ){
		long currentValue = longTree[height][bucketIdx];
		currentValue = Long.numberOfTrailingZeros( ~currentValue );
		if( height < currentTreeLevel ){
			final long nextBucketIdx = ( bucketIdx << 6 ) + currentValue;
			toReturn[0] += currentValue << powers[ currentTreeLevel - height ];
			final boolean lowerLevelFull = traverseTree(height + 1, toReturn, (int)nextBucketIdx );
			if( lowerLevelFull ){
				longTree[ height ][ bucketIdx ] |= (1L << currentValue);
				return ~longTree[height][bucketIdx] == 0;
			}
			return false;
		}else{
			toReturn[0] += currentValue;
			if( (longTree[height][bucketIdx] & (1L << currentValue)) != 0 ){
				throw new RuntimeException("Trying to set bit that already set, bug in implementation. ");
			}
			longTree[height][bucketIdx] |= (1L << currentValue);
			final boolean full = ~longTree[height][bucketIdx] == 0;
			if( full ){
				initialIdx = bucketIdx + 1;
			}
			return full;
		}
	}
	
	@Override
	public void allocateConrete(final int height, int id) {
		final int currentBucket = id >> 6;
		final int idx = (int)(id & REST_MASK);
		final long currentIdx = 1L << (idx);
		
		longTree[ currentTreeLevel - height ][ currentBucket ] |= currentIdx;
		final boolean fullAtEnd = ~(longTree[currentTreeLevel - height][ currentBucket]) == 0;
		if( fullAtEnd && height < currentTreeLevel ){
			allocateConrete( height + 1, currentBucket );
		}
	}
	
	@Override
	public void free( int id ){
		if( id < 0 ){
			throw new RuntimeException("Id must be above 0, you try to free: " + id );
		}
		initialLoad = false;
		currentSize--;
		free( 0, id );
	}
	
	public void free( final int height, final long id ){
		final int currentBucket = (int)(id >> 6);
		final int idx = (int)(id & REST_MASK);
		final long currentIdx = 1L << (idx);
		final boolean fullAtStart = ~( longTree[currentTreeLevel - height][ currentBucket] ) == 0;
		
		if( (longTree[currentTreeLevel - height][currentBucket] & currentIdx ) == 0 ){
			throw new RuntimeException("Trying to free bit that is not set: " + id + ", on level: " + ( currentTreeLevel - height) + ", with maxTreeLEvel: " + currentTreeLevel);
		}
		longTree[ currentTreeLevel - height ][ currentBucket ] &= ~currentIdx;
		if( fullAtStart && height < currentTreeLevel ){
			free( height + 1, currentBucket );
		}
	}
	
	@Override
	public int allocate(){
		if( currentSize == maxCapacity ){
			expand(longTree, false);
		}
		currentSize++;
		if( initialLoad ){
			long initialValue = longTree[ currentTreeLevel ][initialIdx];
			long currentValue = Long.numberOfTrailingZeros( ~initialValue );
			if( !( currentValue == 63 ) ){
				longTree[ currentTreeLevel ][ initialIdx ] |= ((long)1 << currentValue );
				return (int)(currentValue + ( initialIdx << 6));
			}
		}
		long[] newId = new long[1];
		traverseTree( 0, newId, 0);
		return (int)newId[0];
	}
}
