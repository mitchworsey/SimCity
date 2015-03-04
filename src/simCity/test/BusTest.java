package simCity.test;

import java.util.ArrayList;
import java.util.List;

import simCity.BusAgent;
import simCity.BusStopAgent;
import simCity.gui.BusGui;
import simCity.interfaces.BusGuiInterface;
import simCity.test.mock.*;
import simCity.BusStopAgent.MyPerson;
import junit.framework.*;	


public class BusTest extends TestCase
{   
	//these are instantiated for each test separately via the setUp() method.
	BusAgent bus;
	MockBusStop busStop;
	MockPerson person1;
	MockPerson person2;
	MockPerson person3;
	BusGuiInterface gui;

	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		bus = new BusAgent("Bus");	
		busStop = new MockBusStop("bs1");
		person1 = new MockPerson("p1");
		person2 = new MockPerson("p2");
		person3 = new MockPerson("p3");
		gui = new MockBusGui("mock");
		bus.actionComplete.release();
		bus.actionComplete.release();
		bus.actionComplete.release();
		bus.setGui(gui);
		//release 3 semaphores to make sure agent doesn't lock up during unit testing
	}	
	
	/**
	 * This tests the basic 1 bus 1 busStop interaction with multiple people
	 */
	public void testOneBusOneBusStopInteraction()
	{
		//setUp() runs first before this test!
		
		//check preconditions
		assertEquals("Bus's stopCounter should be at 0 (indexed for the first busStop in its list. It isn't.", bus.stopCounter, 0);		
		assertEquals("BusAgent should have an empty event log before the Bus's LoadPassengers() is called."
				+ " Instead, the Bus's event log reads: "
						+ bus.log.toString(), 0, bus.log.size());
		
		//add the single busRoute to the map
		//bus.busRoute.put(0, busStop);
		
		//step 1 of the test, send LoadPassengers() msg to Bus
		BusStopAgent bs = new BusStopAgent("bs");
		
		//first create a List of <MyPerson> to act as the passenger
		List<MyPerson> passengers = new ArrayList<MyPerson>();
		MyPerson mp1 = bs.new MyPerson(person1, busStop);
		MyPerson mp2 = bs.new MyPerson(person2, busStop);
		MyPerson mp3 = bs.new MyPerson(person3, busStop);
		passengers.add( mp1 );
		passengers.add( mp2 );
		passengers.add( mp3 );
		
		bus.populateMap(0, busStop);
	
		bus.pickAndExecuteAnAction();
		
		bus.LoadPassengers(passengers); //send the list to the Bus
	
		//check to see that BusAgent's onBoard list should now have 3 people in it
		assertEquals("Bus's onBoard list should have 3 people in it. It doesn't.", bus.onBoard.size(), 3);
	
		
		//step 2 invoke the Bus scheduler, check to make sure it returns true once then false
		assertTrue("Bus's scheduler should have returned true (done loading passengers, start driving) but didn't.", 
				bus.pickAndExecuteAnAction());
		assertFalse("Bus's scheduler should have returned false (already driving), nothing left to do, but didn't.", 
				bus.pickAndExecuteAnAction());
		
		
		
		
		//step 4 send MsgAtBusStop to bus
		bus.MsgAtBusStop();
		
		//check to see it received the message
		assertTrue("Bus should have received message. It didn't.", bus.log.containsString("Arrived at BusStop."));
		
		
		//step 5 reinvoke the Bus scheduler, check to make sure it returns true
		assertTrue("Bus's scheduler should have returned true (start loading passengers) but didn't.", 
				bus.pickAndExecuteAnAction());
	
		
		//Check to see that the unloadPassengers() action was called by the scheduelr
		assertTrue("Bus should have unloaded passengers. It didn't.",
				bus.log.containsString("Unloading passengers."));
		
		//Now send LeavingBus message to Bus
		bus.LeavingBus(mp1.p); 
		
		//Check to see that onBoard size is now 2
		assertEquals("Bus's onBoard list should have 2 people in it. It doesn't.", bus.onBoard.size(), 2);
		
		bus.LeavingBus(mp2.p); 
		bus.LeavingBus(mp3.p); 
		
		//Check to see onBoard is now size 0 (empty)
		assertEquals("Bus's onBoard list should have 0 people in it. It doesn't.", bus.onBoard.size(), 0);
		
		
	}//end one bus one bus stop interaction with multiple people
	
	public void testOneBusManyBusStopInteraction()
	{
		//setUp() runs first before this test!
		
		MockBusStop busStop2 = new MockBusStop("bs2");
		
		//check preconditions
		assertEquals("Bus's stopCounter should be at 0 (indexed for the first busStop in its list. It isn't.", bus.stopCounter, 0);		
		
		//add the single busRoute to the map
		//bus.busRoute.put(0, busStop);
		
		
		
		//step 1 of the test, send LoadPassengers() msg to Bus
		BusStopAgent bs = new BusStopAgent("bs");
		
		//first create a List of <MyPerson> to act as the passenger
		List<MyPerson> passengers = new ArrayList<MyPerson>();
		MyPerson mp1 = bs.new MyPerson(person1, busStop);
		MyPerson mp2 = bs.new MyPerson(person2, busStop);
		MyPerson mp3 = bs.new MyPerson(person3, busStop);
		passengers.add( mp1 );
		passengers.add( mp2 );
		passengers.add( mp3 );
		
		bus.populateMap(0, busStop);
		bus.populateMap(1, busStop2);
		
		bus.pickAndExecuteAnAction();
	
		bus.LoadPassengers(passengers); //send the list to the Bus
	
		//check to see that BusAgent's onBoard list should now have 3 people in it
		assertEquals("Bus's onBoard list should have 3 people in it. It doesn't.", bus.onBoard.size(), 3);
		
		
		//step 2 invoke the Bus scheduler, check to make sure it returns true once then false
		assertTrue("Bus's scheduler should have returned true (done loading passengers, start driving) but didn't.", 
				bus.pickAndExecuteAnAction());
		
		
		//step 3 check to see that Bus is now moving (startBusRoute() has been called)
		assertTrue("Bus should have started along the route and logged the event. It didn't.",
				bus.log.containsString("Continuing on bus route."));
		
		
		//step 4 send MsgAtBusStop to bus
		bus.MsgAtBusStop();
		
		//check to see it received the message
		assertTrue("Bus should have received message. It didn't.", bus.log.containsString("Arrived at BusStop."));
		
		
		//step 5 reinvoke the Bus scheduler, check to make sure it returns true
		assertTrue("Bus's scheduler should have returned true (start loading passengers) but didn't.", 
				bus.pickAndExecuteAnAction());
	
		
		//Check to see that the unloadPassengers() action was called by the scheduelr
		assertTrue("Bus should have unloaded passengers. It didn't.",
				bus.log.containsString("Unloading passengers."));
		
		//Now send LeavingBus message to Bus
		bus.LeavingBus(mp1.p); 
		
		//Check to see that onBoard size is now 2
		assertEquals("Bus's onBoard list should have 2 people in it. It doesn't.", bus.onBoard.size(), 2);
		
		bus.LeavingBus(mp2.p); 
		bus.LeavingBus(mp3.p); 
		
		//Check to see onBoard is now size 0 (empty)
		assertEquals("Bus's onBoard list should have 0 people in it. It doesn't.", bus.onBoard.size(), 0);
		
		
	}//end one bus one bus stop interaction with multiple people
	
}








