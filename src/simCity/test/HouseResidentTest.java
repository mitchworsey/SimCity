package simCity.test;

import simCity.OrdinaryPerson;
import simCity.house.HouseResidentRole;
import simCity.house.HouseResidentRole.AgentEvent;
import simCity.house.HouseResidentRole.AgentState;
import simCity.interfaces.Person;
import simCity.test.mock.MockHouseMaintenanceManagerRole;
import simCity.test.mock.MockHouseOwnerRole;
import junit.framework.*;

public class HouseResidentTest extends TestCase{
	HouseResidentRole resident;
	MockHouseOwnerRole owner;
	MockHouseMaintenanceManagerRole maintenance;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your role and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		resident = new HouseResidentRole("resident");	
		owner = new MockHouseOwnerRole("mockowner");
		maintenance = new MockHouseMaintenanceManagerRole("mockmaintenance");
		
	}	

	/**
	 * This tests the "in the house" resident scenarios
	 */
	public void testOne(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// runs first before this test!
		/*
		 * This wont work because the residentGui in HouseResidentRole isn't instantiated.
		 * This test can work if the residentGui actions are commented out in HouseResidentRole
		 */
		
		Person person = new OrdinaryPerson("Mitch", 10000);
		resident.setPersonAgent(person);
		
		assertTrue("Resident's state should == DoingNothing. It isn't.", resident.state == AgentState.DoingNothing);		
		
		resident.msgGotHungry();
				
		assertTrue("Resident's event should be hungry. It isn't.", resident.event == AgentEvent.hungry);
		
		assertTrue("Resident's scheduler should have returned true (needs to react to GotHungry), but didn't.", 
				resident.pickAndExecuteAnAction());
		
		//assertTrue("Resident's state should be aboutToMakeFood. It isn't.", resident.state == AgentState.aboutToMakeFood);
				
		//resident.msgMakeFood("Chicken");
		
		assertTrue("Resident's event should be makeFood. It isn't.", resident.event == AgentEvent.makeFood);
		
		assertTrue("Resident's scheduler should have returned true (needs to react MakeFood), but didn't.", 
				resident.pickAndExecuteAnAction());
		
		//assertTrue("Resident's state should be madeFood. It isn't.", resident.state == AgentState.madeFood);
		
		//resident.msgFoodDone();
		
		assertTrue("Resident's event should be eatFood. It isn't.", resident.event == AgentEvent.eatFood);
		
		assertTrue("Resident's scheduler should have returned true (needs to react to FoodDone), but didn't.", 
				resident.pickAndExecuteAnAction());
		
		//assertTrue("Resident's state should be DoingNothing. It isn't.", resident.state == AgentState.DoingNothing);
				
		//resident.msgLeaveHouse();
		
		////////assertTrue("Resident's event should be aboutToLeaveHouse. It isn't.", resident.event == AgentEvent.leaveHouse);
		
		assertTrue("Resident's scheduler should have returned true (needs to react to LeaveHouse), but didn't.", 
				resident.pickAndExecuteAnAction());
		
		//assertTrue("Resident's state should be DoingNothing. It isn't.", resident.state == AgentState.DoingNothing);
		
		resident.msgGotSleepy();
		
		assertTrue("Resident's event should be sleepy. It isn't.", resident.event == AgentEvent.sleepy);
		
		assertTrue("Resident's scheduler should have returned true (needs to react to GotSleepy), but didn't.", 
				resident.pickAndExecuteAnAction());
		
		assertTrue("Resident's state should be DoingNothing. It isn't.", resident.state == AgentState.DoingNothing);
		
		assertFalse("Resident's scheduler should have returned false (nothing to do), but didn't.", 
				resident.pickAndExecuteAnAction());
		
	}
	
}