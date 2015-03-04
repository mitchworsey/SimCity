package simCity.test;

import java.util.ArrayList;

import java.util.List;

import simCity.BusAgent;
import simCity.BusStopAgent;
import simCity.gui.BusGui;
import simCity.interfaces.Bus;
import simCity.interfaces.BusGuiInterface;
import simCity.interfaces.BusStop;
import simCity.test.mock.*;
import simCity.BusStopAgent.MyPerson;
import junit.framework.*;	


public class BusStopTest extends TestCase
{   
	//these are instantiated for each test separately via the setUp() method.
	MockBus bus;
	BusStopAgent busStop;
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
		bus = new MockBus("Bus");	
		person1 = new MockPerson("p1");
		person2 = new MockPerson("p2");
		person3 = new MockPerson("p3");
		gui = new MockBusGui("mock");
		//release 3 semaphores to make sure agent doesn't lock up during unit testing
	}	
	
	/**
	 * This tests the basic 1 bus 1 busStop interaction with multiple people
	 */
	public void testOneBusOneBusStopInteraction()
	{
		//setUp() runs first before this test!
		
		
		
		//add the single busRoute to the map
		List<Bus> buses = new ArrayList<Bus>();
		buses.add(bus);
		
		List<BusStop> busRoute = new ArrayList<BusStop>();
		busRoute.add(busStop);
		
		busStop = new BusStopAgent("busStop", buses);
		
		busStop.addBusRoute(bus, busRoute);
		
		//check preconditions
		assertEquals("BusStop's waitingPeople should have 0 people in it. It doesn't.", busStop.waitingPeople.size(), 0);		
		assertEquals("BusStop's buses list should have 1 bus in it. It doesn't.", busStop.buses.size(), 1);
		assertEquals("BusStopAgent should have an empty event log."
						+ " Instead, the BusStop's event log reads: "
								+ busStop.log.toString(), 0, busStop.log.size());
		
		
		busStop.WantToRide(person1, busStop);
		busStop.WantToRide(person2, busStop);
		busStop.WantToRide(person3, busStop);
		
		//check to see it received the messages
		assertTrue("BusStop should have received message. It didn't.", busStop.log.containsString("Received WantToRide msg"));
	
		//check to see that BusAgent's onBoard list should now have 3 people in it
		assertEquals("BusStops's waitingPeople list should have 3 people in it. It doesn't.", busStop.waitingPeople.size(), 3);
	
		
		//step 2 invoke the BusStop scheduler, check to make sure it returns false
		assertFalse("Bus's scheduler should have returned false, nothing left to do, but didn't.", 
				busStop.pickAndExecuteAnAction());
		
		
		//step 4 send BusIsHere to busStop
		busStop.BusIsHere(bus);
		
		//check to see it received the message
		assertTrue("BusStop should have received message. It didn't.", busStop.log.containsString("Received BusIsHere msg"));
		
		
		//step 5 reinvoke the BusStop scheduler, check to make sure it returns true then false
		assertTrue("BusStop's scheduler should have returned true (start loading passengers) but didn't.", 
				busStop.pickAndExecuteAnAction());
		assertFalse("BusStop's scheduler should have returned false (start loading passengers) but didn't.", 
				busStop.pickAndExecuteAnAction());
		
		//Check to see that the loadPassengers() action was called by the scheduler
		assertTrue("BusStop should have unloaded passengers to load onto BusStop. It didn't.",
				busStop.log.containsString("Loading passengers to bus"));
		
		// send final message
		busStop.BusIsLeaving(bus);
		
		//Check to see it was received
		assertTrue("BusStop should have received the message. It didn't.",
				busStop.log.containsString("Received BusIsLeaving msg"));
		
	}//end one bus one bus stop interaction with multiple people
	
	
	public void testManyBusOneBusStopInteraction()
	{
		//setUp() runs first before this test!
		
		
		MockBus bus2 = new MockBus("bus2");
		
		//add the single busRoute to the map
		List<Bus> buses = new ArrayList<Bus>();
		buses.add(bus);
		buses.add(bus2);
		
		List<BusStop> busRoute = new ArrayList<BusStop>();
		busRoute.add(busStop);
		
		busStop = new BusStopAgent("busStop", buses);
		
		busStop.addBusRoute(bus, busRoute);
		busStop.addBusRoute(bus2, busRoute);
		
		//check preconditions
		assertEquals("BusStop's waitingPeople should have 0 people in it. It doesn't.", busStop.waitingPeople.size(), 0);		
		assertEquals("BusStop's buses list should have 2 buses in it. It doesn't.", busStop.buses.size(), 2);
		assertEquals("BusStopAgent should have an empty event log."
						+ " Instead, the BusStop's event log reads: "
								+ busStop.log.toString(), 0, busStop.log.size());
		
		
		busStop.WantToRide(person1, busStop);
		busStop.WantToRide(person2, busStop);
		busStop.WantToRide(person3, busStop);
		
		//check to see it received the messages
		assertTrue("BusStop should have received message. It didn't.", busStop.log.containsString("Received WantToRide msg"));
	
		//check to see that BusAgent's onBoard list should now have 3 people in it
		assertEquals("BusStops's waitingPeople list should have 3 people in it. It doesn't.", busStop.waitingPeople.size(), 3);
	
		
		//step 2 invoke the BusStop scheduler, check to make sure it returns false
		assertFalse("Bus's scheduler should have returned false, nothing left to do, but didn't.", 
				busStop.pickAndExecuteAnAction());
		
		
		//step 4 send BusIsHere to busStop
		busStop.BusIsHere(bus);
		
		//check to see it received the message
		assertTrue("BusStop should have received message. It didn't.", busStop.log.containsString("Received BusIsHere msg"));
		
		
		//step 5 reinvoke the BusStop scheduler, check to make sure it returns true then false
		assertTrue("BusStop's scheduler should have returned true (start loading passengers) but didn't.", 
				busStop.pickAndExecuteAnAction());
		assertFalse("BusStop's scheduler should have returned false (start loading passengers) but didn't.", 
				busStop.pickAndExecuteAnAction());
		
		//Check to see that the loadPassengers() action was called by the scheduler
		assertTrue("BusStop should have unloaded passengers to load onto BusStop. It didn't.",
				busStop.log.containsString("Loading passengers to bus"));
		
		// send final message
		busStop.BusIsLeaving(bus);
		
		//Check to see it was received
		assertTrue("BusStop should have received the message. It didn't.",
				busStop.log.containsString("Received BusIsLeaving msg"));
		
	}//end one bus one bus stop interaction with multiple people
	
}








