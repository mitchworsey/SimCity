package simCity.test;

import simCity.OrdinaryPerson;
import simCity.bank.BankCustomerRole;
import simCity.bank.BankCustomerRole.Transaction;
import simCity.bank.BankCustomerRole.state;
import simCity.bank.BankTellerRole;
import simCity.bank.BankTellerRole.Account;
import simCity.bank.BankTellerRole.MyCustomer;
import simCity.bank.BankTellerRole.transactionState;
import simCity.gui.BankGuardGui;
import simCity.gui.BankTellerGui;
import simCity.gui.SimCityGui;
import simCity.interfaces.Person;
import simCity.test.mock.MockBankCustomerGui;
import simCity.test.mock.MockBankCustomerRole;
import simCity.test.mock.MockBankTellerRole;
import simCity.test.mock.MockPerson;
import junit.framework.TestCase;

/**
 * 
 * This class is a JUnit test class to unit test the BankCustomer
 *
 * @author Jessica Wang
 */
public class BankCustomerTest extends TestCase {
	//these are instantiated for each test separately via the setUp() method.
		BankCustomerRole c;
		MockBankTellerRole teller;
		OrdinaryPerson p;
		MockBankCustomerGui gui;
		
		/**
		 * This method is run before each test. You can use it to instantiate the class variables
		 * for your agent and mocks, etc.
		 */
		public void setUp() throws Exception{
			super.setUp();		
			c = new BankCustomerRole("Jane");
			p = new OrdinaryPerson("Jane", 300);
			gui = new MockBankCustomerGui("Jane");
			c.setGui(gui);
			c.setPersonAgent(p);
			teller = new MockBankTellerRole("Phil");
			c.setTeller(teller);
		}	
		/**
		 * This tests the cashier in the scenario where a waiter messages the cashier to create a check for a customer
		 */
		public void testOne() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Customer should have 0 transactions in it. It doesn't.",c.transactions.size(), 0);	
			assertEquals("Customer should have an empty event log before any of the Customer's messages are called. Instead, the Customer's event log reads: "
							+ c.log.toString(), 0, teller.log.size());
			
			c.transactions.add(c.new Transaction(40, state.waiting, "OpenAccount"));
			c.msgHereIsYourAcctNumber(4, "OpenAccount"); //send message from the teller
			c.msgAtLocation();
			
			//call the scheduler
			assertTrue("Customer's scheduler should have returned true, but didn't.", c.pickAndExecuteAnAction());		
		}
		
		public void testTwo() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Customer should have 0 transactions in it. It doesn't.",c.transactions.size(), 0);	
			assertEquals("Customer should have an empty event log before any of the Customer's messages are called. Instead, the Customer's event log reads: "
							+ c.log.toString(), 0, teller.log.size());
			
			c.msgCreateBankAccount(); //send message from GUI
			c.msgAtLocation();
			
			//call the scheduler
			assertTrue("Customer's scheduler should have returned true, but didn't.", c.pickAndExecuteAnAction());		
		}
		
		public void testThree() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Customer should have 0 transactions in it. It doesn't.",c.transactions.size(), 0);	
			assertEquals("Customer should have an empty event log before any of the Customer's messages are called. Instead, the Customer's event log reads: "
							+ c.log.toString(), 0, teller.log.size());
			
			c.msgWithdrawFromBank(50); //send message from GUI
			c.msgAtLocation();
			
			//call the scheduler
			assertTrue("Customer's scheduler should have returned true, but didn't.", c.pickAndExecuteAnAction());		
		}
		
		public void testFour() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Customer should have 0 transactions in it. It doesn't.",c.transactions.size(), 0);	
			assertEquals("Customer should have an empty event log before any of the Customer's messages are called. Instead, the Customer's event log reads: "
							+ c.log.toString(), 0, teller.log.size());
			
			c.msgMakeDepositFromBank(100); //send message from GUI
			c.msgAtLocation();
			 
			//call the scheduler
			assertTrue("Customer's scheduler should have returned true, but didn't.", c.pickAndExecuteAnAction());		
		}
		
		public void testFive() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Customer should have 0 transactions in it. It doesn't.",c.transactions.size(), 0);	
			assertEquals("Customer should have an empty event log before any of the Customer's messages are called. Instead, the Customer's event log reads: "
							+ c.log.toString(), 0, teller.log.size());
			
			c.msgMakeLoanFromBank(80); //send message from GUI
			c.msgAtLocation();
			
			//call the scheduler
			assertTrue("Customer's scheduler should have returned true, but didn't.", c.pickAndExecuteAnAction());		
		}
}
