package simCity.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simCity.BankComponent;
import simCity.BusAgent;
import simCity.BusStopAgent;
import simCity.BusStopComponent;
import simCity.CityComponent;
import simCity.HousingComponent;
import simCity.Location;
import simCity.MarketComponent;
import simCity.OrdinaryPerson;
import simCity.Restaurant1Component;
import simCity.Restaurant2Component;
import simCity.Restaurant3Component;
import simCity.Restaurant4Component;
import simCity.Restaurant5Component;
import simCity.gui.BusGui;
import simCity.interfaces.BusGuiInterface;
import simCity.test.mock.*;
import simCity.BusStopAgent.MyPerson;
import junit.framework.*;	


public class PersonTest extends TestCase
{   
	//these are instantiated for each test separately via the setUp() method.
	OrdinaryPerson person;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		person = new OrdinaryPerson("Bob", 50.00);
		person.MsgActionComplete();
		person.MsgActionComplete();
		person.MsgActionComplete();
		for (int i=0; i<10; i++) {
			person.msgAtDestination();
		}
	}	
	
	/**
	 * This tests the basic 1 bus 1 busStop interaction with multiple people
	 */
	public void testPerson()
	{
		//setUp() runs first before this test!
		
		//check precondition
		assertEquals("OrdinaryPerson should have an empty event log"
				+ " Instead, the OrdinaryPerson's event log reads: "
						+ person.log.toString(), 0, person.log.size());
		
		MockBusStop bs1 = new MockBusStop("alp");
		MockBusStop bs2 = new MockBusStop("bs2");
		
		MockPersonGui mpg = new MockPersonGui("mpg");
		
		
		Map<String, CityComponent> buildingEntrances = new HashMap<String, CityComponent>();
		Location housingEntrance = new Location(325,525);
	    Location bankEntrance = new Location(780, 240);
	    Location restaurant1Entrance = new Location(130, 180);
	    Location restaurant2Entrance = new Location(350, 80);
	    Location restaurant3Entrance = new Location(460, 80);
	    Location restaurant4Entrance = new Location(560,80);
	    Location restaurant5Entrance = new Location(660, 80);
	    Location marketEntrance = new Location(140, 365);
	    Location busStop1Entrance = new Location(190, 390);
	    Location busStop2Entrance = new Location(730, 110);
	    CityComponent buildingComponent = new HousingComponent("House", housingEntrance, "house");
	        buildingEntrances.put("Housing", buildingComponent);
	        buildingComponent = new BankComponent("Bank", bankEntrance, "bank");
	        buildingEntrances.put("Bank", buildingComponent);
	        buildingComponent = new Restaurant1Component("Restaurant1", restaurant1Entrance, "restaurant1");
	        buildingEntrances.put("Restaurant1", buildingComponent);
	        buildingComponent = new Restaurant2Component("Restaurant2", restaurant2Entrance, "restaurant2");
	        buildingEntrances.put("Restaurant2", buildingComponent);
	        buildingComponent = new Restaurant3Component("Restaurant3", restaurant3Entrance, "restaurant3");
	        buildingEntrances.put("Restaurant3", buildingComponent);
	        buildingComponent = new Restaurant4Component("Restaurant4", restaurant4Entrance, "restaurant4");
	        buildingEntrances.put("Restaurant4", buildingComponent);
	        buildingComponent = new Restaurant5Component("Restaurant5", restaurant5Entrance, "restaurant5");
	        buildingEntrances.put("Restaurant5", buildingComponent);
	        buildingComponent = new MarketComponent("Restaurant2Market", marketEntrance, "restaurant2Market");
	        buildingEntrances.put("Restaurant2Market", buildingComponent);
	        buildingComponent = new BusStopComponent(bs1, "BusStop1", busStop1Entrance, "busstop1");
	        buildingEntrances.put("BusStop1", buildingComponent);
	        buildingComponent = new BusStopComponent(bs2, "BusStop2", busStop2Entrance, "busstop2");
	        buildingEntrances.put("BusStop2", buildingComponent);
	    
	        //City map now populated
	    person.addComponents(buildingEntrances);
	    person.setGui(mpg);
	    
		
		//Person testing (send message, check if message received call scheduler 2x, check action
	    // One call of scheduler should return false, one should return true
	    //BANK
	    
		person.ToBank();
		assertTrue("Person should have received ToBank() message. It didn't.", person.log.containsString("Received ToBank msg"));
		assertTrue("Person's scheduler should have returned true but didn't.", 
				person.pickAndExecuteAnAction());
		assertFalse("Person's scheduler should have returned false, nothing left to do, but didn't.", 
				person.pickAndExecuteAnAction());
	    assertTrue("Person should be going ToBank() now. It isn't.", person.log.containsString("Going To Bank"));
	    
	    person.ToRestaurant1();
		assertTrue("Person should have received ToRestaurant1() message. It didn't.", person.log.containsString("Received ToRestaurant1 msg"));
		assertTrue("Person's scheduler should have returned true but didn't.", 
				person.pickAndExecuteAnAction());
		assertFalse("Person's scheduler should have returned false, nothing left to do, but didn't.", 
				person.pickAndExecuteAnAction());
	    assertTrue("Person should be going ToRestaurant1() now. It isn't.", person.log.containsString("Going To Restaurant1"));
	    
	    person.ToHousing();
		assertTrue("Person should have received ToHousing() message. It didn't.", person.log.containsString("Received ToHousing msg"));
		assertTrue("Person's scheduler should have returned true but didn't.", 
				person.pickAndExecuteAnAction());
		assertFalse("Person's scheduler should have returned false, nothing left to do, but didn't.", 
				person.pickAndExecuteAnAction());
	    assertTrue("Person should be going ToHousing() now. It isn't.", person.log.containsString("Going To Housing"));
	    
	    person.ToMarket();
		assertTrue("Person should have received ToMarket() message. It didn't.", person.log.containsString("Received ToMarket msg"));
		assertTrue("Person's scheduler should have returned true but didn't.", 
				person.pickAndExecuteAnAction());
		assertFalse("Person's scheduler should have returned false, nothing left to do, but didn't.", 
				person.pickAndExecuteAnAction());
	    assertTrue("Person should be going ToMarket() now. It isn't.", person.log.containsString("Going To Market"));
	    
	    
	    // now check for the "at component" messages
	    // same format: send message, check message reception, call scheduler 2x (one true, one false), check action call
	    person.ArrivedAtBank();
		assertTrue("Person should have received ArrivedAtBank() message. It didn't.", person.log.containsString("Received ArrivedAtBank msg"));
		assertTrue("Person's scheduler should have returned true but didn't.", 
				person.pickAndExecuteAnAction());
		assertFalse("Person's scheduler should have returned false, nothing left to do, but didn't.", 
				person.pickAndExecuteAnAction());
	    assertTrue("Person should have ArrivedAtBank() now. It hasn't.", person.log.containsString("Entering Bank"));
	    person.insideComponent = false;
	    
	    person.ArrivedAtRestaurant1();
		assertTrue("Person should have received ArrivedAtRestaurant1() message. "
				+ "It didn't.", person.log.containsString("Received ArrivedAtRestaurant1 msg"));
		assertTrue("Person's scheduler should have returned true but didn't.", 
				person.pickAndExecuteAnAction());
		assertFalse("Person's scheduler should have returned false, nothing left to do, but didn't.", 
				person.pickAndExecuteAnAction());
	    assertTrue("Person should have ArrivedAtRestaurant1() now. It hasn't.", person.log.containsString("Entering Restaurant1"));
	    person.insideComponent = false;
	    
	    person.ArrivedAtMarket();
		assertTrue("Person should have received ArrivedAtMarket() message. It didn't.",
				person.log.containsString("Received ArrivedAtMarket msg"));
		assertTrue("Person's scheduler should have returned true but didn't.", 
				person.pickAndExecuteAnAction());
		assertFalse("Person's scheduler should have returned false, nothing left to do, but didn't.", 
				person.pickAndExecuteAnAction());
	    assertTrue("Person should have ArrivedAtMarket() now. It hasn't.", person.log.containsString("Entering Market"));
	    person.insideComponent = false;
	    
	}//end person testing
	
}








