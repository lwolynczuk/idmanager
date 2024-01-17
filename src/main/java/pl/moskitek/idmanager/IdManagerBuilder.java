package pl.moskitek.idmanager;

import pl.moskitek.idmanager.managers.longmgr.LongIdManagerForMaximum;
import pl.moskitek.idmanager.managers.longmgr.LongIdManagerForMinimum;

public class IdManagerBuilder {
	public static enum Organisation{
		longBits, intBits, JavaBased
	}
	public static enum Purpose{
		Minimum, Maximum, MinMax, 
	}
	
	private Organisation organization = Organisation.longBits;
	private Purpose purpose = Purpose.Minimum;
	
	public IdManagerBuilder organization(Organisation organization) {
		this.organization = organization;
		return this;
	}
	public IdManagerBuilder purpose(Purpose purpose) {
		this.purpose = purpose;
		return this;
	}
	
	private IdManager buildLong() {
		return switch (purpose) {
			case Minimum -> new LongIdManagerForMinimum();
			case Maximum -> new LongIdManagerForMaximum();
			case MinMax -> throw new IdManagerException("Not yet implemented");
			default -> throw new IdManagerException("Unexpected value: " + purpose);
		};
	}
	public IdManager build() {
		return switch (organization) {
			case longBits -> buildLong();
			case intBits -> throw new IdManagerException("Not yet implemented");
			default -> throw new IdManagerException("Unexpected value: " + organization);
		};
	}
}
