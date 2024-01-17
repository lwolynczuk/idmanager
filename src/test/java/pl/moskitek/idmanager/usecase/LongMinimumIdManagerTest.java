package pl.moskitek.idmanager.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.moskitek.idmanager.IdManager;

public class LongMinimumIdManagerTest {
	private IdManager idMgr;
	
	@BeforeEach
	public void setup() {
		idMgr = IdManager
					.builder()
					.build();
		
		for( int i = 0; i < 10000; i++ ) {
			idMgr.allocate();
		}
	}

	@Test
	public void isAllocatedTest() {
		for( int i = 0; i < 10000; i++ ) {
			assertThat(idMgr.isAllocated(i)).isEqualTo(true);
		}	
	}
	
	@Test
	public void freeingSthAndGettingItBack() {
		Stream.of(4543, 8543, 2543, 1111, 15, 1000).forEach( idMgr::free );
		
		Stream.of(15, 1000, 1111, 2543, 4543, 8543).forEach( id -> 
				assertThat(idMgr.allocate()).isEqualTo(id )
		);
	}
	
	@Test
	public void allocateMany() {
		idMgr.free(new int[] { 4543, 8543, 2543, 1111, 15, 1000 } );
		
		assertThat( idMgr.allocate(6) )
			.isEqualTo(new int[] { 15, 1000, 1111, 2543, 4543, 8543 });		
	}
	
	@Test
	public void allocateConcrete() {
		idMgr.free(7777);
		idMgr.free(7778);
		idMgr.free(7779);
		idMgr.allocateConrete(7778);
		
		assertThat(idMgr.allocate()).isEqualTo(7777);
		assertThat(idMgr.allocate()).isEqualTo(7779);
	}
	
	@Test
	public void testSize() {
		assertThat(idMgr.allocatedSize()).isEqualTo(10000);
		idMgr.free(1111);
		idMgr.free(2222);
		assertThat(idMgr.allocatedSize()).isEqualTo(10000 - 2);
	}
	
}
