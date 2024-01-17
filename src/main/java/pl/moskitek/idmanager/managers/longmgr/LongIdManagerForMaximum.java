package pl.moskitek.idmanager.managers.longmgr;

public class LongIdManagerForMaximum extends AbstractLongIdMgr{
	@Override
	public int allocate() {
		throw new RuntimeException("This manager is for next, not allowed call.");
	}
	@Override
	public int[] allocate(int length) {
		throw new RuntimeException("This manager is for next, not allowed call.");
	}

	private long searchMinSetBit( final int height, final int bucketIdx ){
		long currentValue = longTree[height][bucketIdx];
		currentValue = Long.numberOfTrailingZeros( currentValue );
		if( height < currentTreeLevel ){
			final long nextBucketIdx = ( bucketIdx << 6 ) + currentValue;
			return searchMinSetBit(height + 1, (int)nextBucketIdx  );
		}else{
			final long nextBucketIdx = ( bucketIdx << 6 ) + currentValue;
			return nextBucketIdx;
		}
	}
	public int nextSetBit( int height, int id){
		int currentBucket = id >> 6;
			int idx = (int)(id & REST_MASK);
			long currentIdx = 1L << (idx);

			long currentValue = longTree[currentTreeLevel - height][ currentBucket];

			long newIdx = ~( currentIdx | ( currentIdx - 1L ) );
			newIdx &= currentValue;

			if( newIdx == 0 ){
				//go up
				if( height == currentTreeLevel ){
					return -1;
				}else{
					return nextSetBit( height + 1, currentBucket);
				}
			}else{
				//go down
				currentIdx = Long.numberOfTrailingZeros(newIdx);
				if( height == 0 ){
					return (int)((currentBucket << 6) + currentIdx);
				}
				//w dol pojdzie tylko numer bucketa, pozycja w tym buckecie sama sie zlilczy -> currentBucket
				long value = searchMinSetBit(currentTreeLevel - height + 1, (int)((currentBucket << 6) + currentIdx));
				//TODO check range
				return (int)value;
			}		
	}

	@Override
	public int nextSetBit(int id) {
		if( isAllocated(id)){
			return id;
		}
		if( id >= maxCapacity ){
			return -1;
		}
		return nextSetBit(0, id);
	}

	private long searchMaxSetBit( final int height, final int bucketIdx ){
		long currentValue = longTree[height][bucketIdx];
		currentValue = 64 - Long.numberOfLeadingZeros( currentValue ) - 1;
		if( height < currentTreeLevel ){
			final long nextBucketIdx = ( bucketIdx << 6 ) + currentValue;
			return searchMaxSetBit(height + 1, (int)nextBucketIdx  );
		}else{
			final long nextBucketIdx = ( bucketIdx << 6 ) + currentValue;
			return nextBucketIdx;
		}
	}
	public int prevSetBit( int height, int id){
		int currentBucket = id >> 6;
			int idx = (int)(id & REST_MASK);
			long currentIdx = 1L << (idx);

			long currentValue = longTree[currentTreeLevel - height][ currentBucket];

			long newIdx = ( currentIdx - 1L );
			newIdx &= currentValue;

			if( newIdx == 0 ){
				//go up
				if( height == currentTreeLevel ){
					return -1;
				}else{
					return prevSetBit( height + 1, currentBucket);
				}
			}else{
				//go down
				currentIdx = 64 - Long.numberOfLeadingZeros(newIdx) - 1;
				if( height == 0 ){
					return (int)((currentBucket << 6) + currentIdx);
				}
				//w dol pojdzie tylko numer bucketa, pozycja w tym buckecie sama sie zlilczy -> currentBucket
				long value = searchMaxSetBit(currentTreeLevel - height + 1, (int)((currentBucket << 6) + currentIdx));
				//TODO check range
				return (int)value;
			}		
	}

	@Override
	public int prevSetBit(int id) {
		if( isAllocated(id)){
			return id;
		}
		if( id < 0 ){
			return -1;
		}
		return prevSetBit( 0, id);
	}

	@Override
	public void allocateConrete(final int height, int id) {
		final int currentBucket = id >> 6;
		final int idx = (int)(id & REST_MASK);
		final long currentIdx = 1L << (idx);
		final boolean emptyAtStart = longTree[currentTreeLevel - height][ currentBucket] == 0;

		longTree[ currentTreeLevel - height ][ currentBucket ] |= currentIdx;
		if( emptyAtStart && height < currentTreeLevel ){
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


		if( (longTree[currentTreeLevel - height][currentBucket] & currentIdx ) == 0 ){
			throw new RuntimeException("Trying to free bit that is not set: " + id + ", on level: " + ( currentTreeLevel - height) + ", with maxTreeLEvel: " + currentTreeLevel);
		}
		longTree[ currentTreeLevel - height ][ currentBucket ] &= ~currentIdx;
		final boolean emptyAfterOperation = longTree[currentTreeLevel - height][ currentBucket] == 0;
		if( emptyAfterOperation && height < currentTreeLevel ){
			free( height + 1, currentBucket );
		}
	}
}
