package pl.moskitek.idmanager.usecase.instantiate;

import org.junit.jupiter.api.Test;

import pl.moskitek.idmanager.*;

/**
 * Default setup is Organisation.longBits and Purpose.Minimum
 * Below are few instantiations variants for IdManager.
 * 
 * @author moskiek
 */
public class DefaultIdManager {
	@Test
	public void instantiateDefault() {
		IdManager idMgr = IdManager.builder().build();
	}
	@Test
	public void instantiateDefaultExplicit() {
		IdManager idMgr = IdManager.builder()
				.organization(Organisation.longBits)
				.purpose(Purpose.Minimum)
				.build();
	}
	@Test
	public void instantiate64BitsIdManagerForMax() {
		IdManager idMgr = IdManager.builder()
				.organization(Organisation.longBits)
				.purpose(Purpose.Maximum)
				.build();
	}
	@Test
	public void instantiate32BitsIdManagerForMinMax() {
		IdManager idMgr = IdManager.builder()
				.organization(Organisation.intBits)
				.purpose(Purpose.MinMax)
				.build();
	}
	
	@Test
	public void instantiateJavaBasedIdManager() {
		IdManager idMgr = IdManager.builder()
				.organization(Organisation.javaBased)
				.purpose(Purpose.MinMax)
				.build();
	}
}
