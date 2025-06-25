package pl.moskitek.idmanager.usecase.random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.moskitek.idmanager.IdManager;
import pl.moskitek.idmanager.Organisation;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class LongMinimumIdManagerRandomTest extends AbstractRandomTest {
    @Override
    protected IdManager create() {
        return IdManager
                .builder()
                .build();
    }

}
