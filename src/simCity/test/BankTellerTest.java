package simCity.test;

import simCity.OrdinaryPerson;
import simCity.bank.BankCustomerRole;
import simCity.bank.BankTellerRole;
import simCity.bank.BankTellerRole.Account;
import simCity.bank.BankTellerRole.MyCustomer;
import simCity.bank.BankTellerRole.transactionState;
import simCity.gui.BankTellerGui;
import simCity.interfaces.Person;
import simCity.test.mock.MockBankCustomerRole;
import simCity.test.mock.MockBankTellerRole;
import simCity.test.mock.MockPerson;
import junit.framework.TestCase;

/**
 * 
 * This class is a JUnit test class to unit test the BankTeller
 *
 * @author Jessica Wang
 */
public class BankTellerTest extends TestCase {
	//these are instantiated for each test separately via the setUp() method.
		BankTellerRole teller;
		MockBankCustomerRole c;
		OrdinaryPerson p;
		BankTellerGui gui;
		
		/**
		 * This method is run before each test. You can use it to instantiate the class variables
		 * for your agent and mocks, etc.
		 */
		public void setUp() throws Exception{ 
			super.setUp();		
			teller = new BankTellerRole("Jane", 300);
			p = new OrdinaryPerson("Jane", 300);
			gui = new BankTellerGui(teller, 500, 700);
			teller.setGui(gui);
			teller.setPersonAgent(p);
			c = new MockBankCustomerRole("Phil");
			c.setTeller(teller);
		}	
		/**
		 * This tests the cashier in the scenario where a waiter messages the cashier to create a check for a customer
		 */
		public void testOne() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Teller should have 0 customers in it. It doesn't.",teller.customers.size(), 0);	
			assertEquals("Teller should have 0 accounts in it. It doesn't.",teller.accounts.size(), 0);
			assertEquals("Teller should have an empty event log before any of the Teller's messages are called. Instead, the Teller's event log reads: "
							+ teller.log.toString(), 0, teller.log.size());
			
			//add a customer to the list who wants to open an account
			int size = teller.accounts.size()+1;
			Account account = teller.new Account(c, size, 0);
			teller.accounts.add(account);
			MyCustomer customer = teller.new MyCustomer(account, 0, transactionState.openingAcct, "OpenAccount");
			teller.customers.add(customer);
			
			//call the scheduler
			assertTrue("Teller's scheduler should have returned true, but didn't.", teller.pickAndExecuteAnAction());			
		}
		
		public void testTwo() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Teller should have 0 customers in it. It doesn't.",teller.customers.size(), 0);	
			assertEquals("Teller should have 0 accounts in it. It doesn't.",teller.accounts.size(), 0);
			assertEquals("Teller should have an empty event log before any of the Teller's messages are called. Instead, the Teller's event log reads: "
							+ teller.log.toString(), 0, teller.log.size());
			
			//add a customer to the list who wants to withdraw money
			int size = teller.accounts.size()+1;
			Account account = teller.new Account(c, size, 0);
			teller.accounts.add(account);
			MyCustomer customer = teller.new MyCustomer(account, 0, transactionState.withdrawing, "Withdraw");
			teller.customers.add(customer);
			
			//call the scheduler
			assertTrue("Teller's scheduler should have returned true, but didn't.", teller.pickAndExecuteAnAction());
		}
		
		public void testThree() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Teller should have 0 customers in it. It doesn't.",teller.customers.size(), 0);	
			assertEquals("Teller should have 0 accounts in it. It doesn't.",teller.accounts.size(), 0);
			assertEquals("Teller should have an empty event log before any of the Teller's messages are called. Instead, the Teller's event log reads: "
							+ teller.log.toString(), 0, teller.log.size());
			
			//add a customer to the list who wants to deposit money
			int size = teller.accounts.size()+1;
			Account account = teller.new Account(c, size, 0);
			teller.accounts.add(account);
			MyCustomer customer = teller.new MyCustomer(account, 0, transactionState.depositing, "Deposit");
			teller.customers.add(customer);
			
			//call the scheduler
			assertTrue("Teller's scheduler should have returned true, but didn't.", teller.pickAndExecuteAnAction());
		}
		
		public void testFour() {
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Teller should have 0 customers in it. It doesn't.",teller.customers.size(), 0);	
			assertEquals("Teller should have 0 accounts in it. It doesn't.",teller.accounts.size(), 0);
			assertEquals("Teller should have an empty event log before any of the Teller's messages are called. Instead, the Teller's event log reads: "
							+ teller.log.toString(), 0, teller.log.size());
			
			//add a customer to the list who wants to receive a loan
			int size = teller.accounts.size()+1;
			Account account = teller.new Account(c, size, 0);
			teller.accounts.add(account);
			MyCustomer customer = teller.new MyCustomer(account, 0, transactionState.loaning, "Loan");
			teller.customers.add(customer);
			
			//call the scheduler
			assertTrue("Teller's scheduler should have returned true, but didn't.", teller.pickAndExecuteAnAction());
		}
		/*public void testOne()
		{
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.checks.size(), 0);		
			assertEquals("CashierAgent should have an empty event log before any of the Cashier's messages are called. Instead, the Cashier's event log reads: "
							+ cashier.log.toString(), 0, cashier.log.size());
			
			//step 1 of the test
			cashier.msgCreateCheck(waiter, customer, "steak", 1); //send the message from a waiter
			
			//check postconditions for step 1
			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
					+ waiter.log.toString(), 0, waiter.log.size());
			
			assertTrue("Cashier should have logged \"Received msgCreateCheck\" but didn't. His log reads instead: " 
					+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgCreateCheck"));
			
			//step 2
			//call the scheduler
			assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
			
			assertEquals("Cashier should have 1 check in it. It doesn't.",cashier.checks.size(), 1);
			
			assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called. Instead, the MockCustomer's event log reads: "
					+ customer.log.toString(), 0, customer.log.size());
			
			assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
					+ market.log.toString(), 0, market.log.size());
			
			//check postconditions for ComputeCheck(Check check) method 
			assertTrue("checks list should contain a check with state == computed. It doesn't.",
					cashier.checks.get(0).state == checkState.computed);

			assertTrue("checks list should contain a check of amount = 15.99. It contains something else instead: $" 
					+ cashier.checks.get(0).amount, cashier.checks.get(0).amount == 15.99);
			
			assertTrue("checks list should contain a check with the right customer in it. It doesn't.", 
						cashier.checks.get(0).customer == customer);
			
			assertTrue("checks list should contain a check with the right waiter in it. It doesn't.", 
					cashier.checks.get(0).waiter == waiter);
			
			assertTrue("Waiter should have logged \"Received msgCheckComputed\" but didn't. His log reads instead: " 
					+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgCheckComputed"));
			
			//the scheduler should return false because there is no action left to be executed
			assertFalse("Cashier's scheduler should have returned false, but didn't.", cashier.pickAndExecuteAnAction());
			
		}//end one normal cashier scenario*/
}
