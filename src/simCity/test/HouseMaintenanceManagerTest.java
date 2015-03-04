package simCity.test;

import simCity.house.HouseMaintenanceManagerRole;
import simCity.test.mock.MockHouseOwnerRole;
import simCity.test.mock.MockHouseResidentRole;
import junit.framework.*;

public class HouseMaintenanceManagerTest extends TestCase{
	HouseMaintenanceManagerRole maintenance;
	MockHouseOwnerRole owner;
	MockHouseResidentRole resident;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		maintenance = new HouseMaintenanceManagerRole("maintenance");
		owner = new MockHouseOwnerRole("mockowner");		
		resident = new MockHouseResidentRole("mockresident");		
		
	}	
}