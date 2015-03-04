package simCity.test;

import junit.framework.*;
import simCity.OrdinaryPerson;
import simCity.market.*;
import simCity.gui.MarketCashierGui;
import simCity.interfaces.*;
import simCity.test.mock.*;

public class MarketCashierTest extends TestCase{
	MarketCashierRole cashier;
	MockMarketCustomerRole customer;
	MockMarketGrocerRole grocer;
	OrdinaryPerson p;// = new OrdinaryPerson("Person", 50);
	MarketCashierGuiInterface gui;
	
	public void setUp() throws Exception{
		super.setUp();
		grocer = new MockMarketGrocerRole("mockGrocer");
		customer = new MockMarketCustomerRole("mockCustomer");
		cashier = new MarketCashierRole("cashier");
		p = new OrdinaryPerson("person", 300);
		cashier.setPersonAgent(p);
		//grocer.setCustomer(customer);
		customer.setCashier(cashier);
		//customer.setGrocer(grocer);
		cashier.setCustomer(customer);
		cashier.setGui((MarketCashierGui) gui);
	}
	
	// make sure pre-msg is what you want it to be
	// ____.msg_____();
	// assertTrue and pickAndExecuteAnAction();
	// assertFalse and pickAndExecuteAnAction(); is it??
	// check data to see if data got updated
	// check is msg was sent to the right person with logs
	
	public void testOne() {
	/* PRE-MESSAGE CALL */
		//list of myCustomers should be 0
		assertEquals("Cashier's list of myCustomers should be empty (equal 0).", 0, cashier.log.size() );
		assertEquals("Cashier should have an empty event log before any of the Cashier's messages are called. Instead, the Cashier's event log reads: "
				+ customer.log.toString(), 0, cashier.log.size());
		// register int should be > 0
		assertEquals("Cashier register should not be zero. It is", 0, cashier.register);
		
	/* MESSAGE CALL */
		cashier.msgMyTotalPayment(customer, 45, 50);
		// list of myCustomers should have 1
		assertEquals("Cashier should have a list of 1 customer", cashier.myCustomers.size(),1);
		
	/* SCHEDULER */
		assertTrue("Cashier scheduler should return true, it doesn't.", cashier.pickAndExecuteAnAction());
		
	/* POST-MESSAGE & SCHEDULER */
		assertEquals("Cashier's list of myCustomers should be back to zero.", cashier.myCustomers.size(), 0);
		assertEquals("Cashier should have sent message to Customer about change.", customer.log.size(), 1);
	}


	
	
	
}
