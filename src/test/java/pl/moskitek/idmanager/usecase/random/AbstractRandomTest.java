package pl.moskitek.idmanager.usecase.random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.moskitek.idmanager.IdManager;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractRandomTest {
    public static final int INITIAL_SIZE = 1_000_000;
    public static final int RANDOM_OPERATIONS = 100_000;
    protected IdManager idMgr;

    protected abstract IdManager create();

    @BeforeEach
    public void setup() {
        idMgr = create();
        for( int i = 0; i < INITIAL_SIZE; i++ ) {
            int al = idMgr.allocate();
        }
    }

    @Test
    public void randomRemoveTest(){
        long seed = System.currentTimeMillis();
        System.out.println("Seed: " + seed);

        Random r = new Random( seed );

        Set<Integer> randoms = new TreeSet<>();
        for(int i = 0; i < RANDOM_OPERATIONS; i++ ){
            int random = r.nextInt(INITIAL_SIZE);
            if( !randoms.contains(random)) {
                randoms.add(random);
                idMgr.free(random);
            }
        }

        randoms.forEach( R -> assertThat(idMgr.allocate()).isEqualTo(R) );
    }
}
