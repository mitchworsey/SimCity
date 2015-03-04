package simCity.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import simCity.OrdinaryPerson;
import simCity.gui.MarketCustomerGui;
import simCity.gui.MarketGrocerGui;
import simCity.interfaces.MarketCustomerGuiInterface;
import simCity.interfaces.MarketGrocerGuiInterface;
import simCity.market.MarketDeliveryTruck;
import simCity.market.MarketGrocerRole;
import simCity.test.mock.MockMarketCashierRole;
import simCity.test.mock.MockMarketCustomerRole;
import simCity.test.mock.MockMarketGrocerGui;

public class MarketGrocerTest extends TestCase{
	MarketGrocerRole grocer;
	MockMarketCustomerRole customer;
	MockMarketCashierRole cashier;
	OrdinaryPerson p;
	MarketGrocerGuiInterface gui;
	MarketDeliveryTruck mdTruck;
	
	ArrayList<String> needs = new ArrayList<String>();
//	needs.add("Car");
//	needs.add("Bread");
	
	public void setUp() throws Exception{
		super.setUp();
		grocer = new MarketGrocerRole("grocer", mdTruck);
		p = new OrdinaryPerson("person", 300);
		gui = new MockMarketGrocerGui("mockgui");
		grocer.setPersonAgent(p);
		customer = new MockMarketCustomerRole("mockCustomer");
		customer.setGrocer(grocer);
		grocer.setGui(gui); 
		
	}
	
	/* make sure pre-msg is what you want it to be
	 * ____.msg_____();
	 * assertTrue and pickAndExecuteAnAction();
	 * assertFalse and pickAndExecuteAnAction(); is it??
	 * check data to see if data got updated
	 * check is msg was sent to the right person with logs 
	 */
	
	public void testOne() {
	/* PRE-MESSAGE CALLS */
		//List of myCustomers should be empty
		assertEquals("Grocer should have 0 customers in myCustomers list. It doesn't.", grocer.myCustomers.size(), 0);
		assertEquals("Grocer should have an empty event log before any of the Teller's messages are called. Instead, the Teller's event log reads: "
				+ grocer.log.toString(), 0, grocer.log.size());
		
		//add a customer to the list with full list of needs.
		//grocer.myCustomers.get(1).cState = CustomerState.None;
		
	/* MESSAGE CALL */
		grocer.msgIWantStuff(customer, needs);
		
		// List of myCustomers's needs should have things in it
		//call scheduler
	/* SCHEDULER */
		assertTrue("Grocer myCustomers has customer in it.", grocer.pickAndExecuteAnAction());//, customer, grocer.myCustomers.get(1));
		
	/* POST-MESSAGE & SCHEDULER */	
		assertEquals("Grocer has sent message to customer.", customer.log.size(), 0);
	}
	
//	public void testTwo() {
//	/* PRE-MESSAGE CALLS */
//		//List of myCustomers should be empty
//		assertEquals("Grocer should have 0 customers in myCustomers list. It doesn't.", grocer.myCustomers.size(), 0);
//		assertEquals("Grocer should have an empty event log before any of the Teller's messages are called. Instead, the Teller's event log reads: "
//				+ grocer.log.toString(), 0, grocer.log.size());
//		
//		//add a customer to the list with full list of needs.
//		//grocer.myCustomers.get(1).cState = CustomerState.None;
//		
//	/* MESSAGE CALL */
//		grocer.msgIWantStuff(customer, needs);
//		
//		// List of myCustomers's needs should have things in it
//		//call scheduler
//	/* SCHEDULER */
//		assertTrue("Grocer myCustomers has customer in it.", grocer.pickAndExecuteAnAction());//, customer, grocer.myCustomers.get(1));
//		
//	/* POST-MESSAGE & SCHEDULER */	
//		
//	}

}
