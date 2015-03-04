package simCity.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import simCity.OrdinaryPerson;
import simCity.market.*;
import simCity.market.MarketCustomerRole.NeedState;
import simCity.interfaces.*;
import simCity.test.mock.*;

public class MarketCustomerTest extends TestCase {
	MockMarketGrocerRole grocer;
	MarketCustomerRole customer;
	MockMarketCashierRole cashier;
	OrdinaryPerson p;
	MarketCustomerGuiInterface gui;
	
	ArrayList<String> chosenNeeds = new ArrayList<String>();
	
	public void setUp() throws Exception{
		super.setUp();
		grocer = new MockMarketGrocerRole("mockGrocer");
		customer = new MarketCustomerRole("customer");
		cashier = new MockMarketCashierRole("mockCashier");
		p = new OrdinaryPerson("person", 300);
		gui = new MockMarketCustomerGui("mockgui");
		customer.setPersonAgent(p);
		grocer.setCustomer(customer);
		customer.setCashier(cashier);
		customer.setGrocer(grocer);
		customer.setGui(gui); 
		customer.atMarket.release();
		customer.atGrocer.release();
		customer.atWaitingArea.release();
		customer.atPickUpArea.release();
		customer.atCashier.release();
		customer.atExit.release();
		
	}
	
	/*
	 * TEST ONE:
	 * - Selected checkboxes from gui fill customer's needs list
	 * - Once needs list has things in it, customer should go/send message to Grocer
	 * - Once the list is send to Grocer, Grocer should add customer into its list of customers
	 * - When list of customers is not empty, it should also add the list of needs into the myCustomer list of needs
	 * - When Grocer has retrieved all products in the list of needs of myCustomer, it should send a message to Customer that its work is done
	 * - ------------------------------
	 * - Customer should pick up items and the products in Needs should all be nState.PickedUp
	 * - ------------------------------
	 * - Customer should send message to Cashier of bill and payment
	 * - Customer will receive message from Cashier of change
	 * - Change should be updated if change is >0
	 * */
	// make sure pre-msg is what you want it to be
	// ____.msg_____();
	// assertTrue and pickAndExecuteAnAction();
	// assertFalse and pickAndExecuteAnAction(); is it??
	// check data to see if data got updated
	// check is msg was sent to the right person with logs
	public void testOne() {
	/* PRE-MESSAGE CALL */
		//List of needs in customer should be empty
		assertEquals("There should an empty list in the list of needs.", 0, customer.myNeeds.size());
		
	/* MESSAGE CALL */
		customer.IAmInNeed(); // from animation
		// List of needs should have things in it
		assertEquals("Needs list is not empty.", 4, customer.myNeeds.size());
		customer.msgIRetrieved("Milk");
		// State of that product should be given/updated
		assertEquals("Product should have been updated to NeedState of GivenProduct", NeedState.GivenProduct, customer.findNeed("Milk").nState);
		customer.msgHereIsBill(45);
		// State of all products in list of needs should be updated as given
//		assertEquals("Product states in list should be Give");
		// Customer should have at least the same amount of 45 (the bill)
		customer.msgHereIsChange(5);
//		customer.msgWeHaveNomore("Car");
	
	/* SCHEDULER */
		assertTrue("Customer scheduler should return true, it doesn't.", customer.pickAndExecuteAnAction());
		
	/* POST-MESSAGE & SCHEDULER */	
		
		
	}
//	public void testTwo() {
//		
//		//List of needs in customer should be empty
//		assertEquals("There should an empty list in the list of needs.", 0, customer.myNeeds.size());
//		customer.IAmInNeed(); // from animation
//		// List of needs should have things in it
//		assertEquals("Needs list is not empty.", 4, customer.myNeeds.size());
//		customer.msgIRetrieved("Milk");
//		// State of that product should be given/updated
//		assertEquals("Product should have been updated to NeedState of GivenProduct", NeedState.GivenProduct, customer.findNeed("Milk").nState);
//		customer.msgHereIsBill(45);
//		// State of all products in list of needs should be updated as given
////		assertEquals("Product states in list should be Give");
//		// Customer should have at least the same amount of 45 (the bill)
//		customer.msgHereIsChange(5);
//		// 
////		customer.msgWeHaveNomore("Car");
//		
//		assertTrue("Customer scheduler should return true, it doesn't.", customer.pickAndExecuteAnAction());
//
//	}
//	public void testThree() {
//		
//		//List of needs in customer should be empty
//		assertEquals("There should an empty list in the list of needs.", 0, customer.myNeeds.size());
//		customer.IAmInNeed(); // from animation
//		// List of needs should have things in it
//		assertEquals("Needs list is not empty.", 4, customer.myNeeds.size());
//		customer.msgIRetrieved("Milk");
//		// State of that product should be given/updated
//		assertEquals("Product should have been updated to NeedState of GivenProduct", NeedState.GivenProduct, customer.findNeed("Milk").nState);
//		customer.msgHereIsBill(45);
//		// State of all products in list of needs should be updated as given
////		assertEquals("Product states in list should be Give");
//		// Customer should have at least the same amount of 45 (the bill)
//		customer.msgHereIsChange(5);
//		// 
////		customer.msgWeHaveNomore("Car");
//		
//		assertTrue("Customer scheduler should return true, it doesn't.", customer.pickAndExecuteAnAction());
//
//	}

	
}
