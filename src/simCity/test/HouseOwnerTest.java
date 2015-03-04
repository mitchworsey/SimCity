package simCity.test;

import junit.framework.*;
import simCity.house.House;
import simCity.house.HouseOwnerRole;
import simCity.house.House.HouseState;
import simCity.house.HouseOwnerRole.CustomerState;
import simCity.test.mock.MockHouseCustomerRole;
import simCity.test.mock.MockHouseMaintenanceManagerRole;

public class HouseOwnerTest extends TestCase{
	HouseOwnerRole owner;
	MockHouseCustomerRole customer;
	MockHouseMaintenanceManagerRole maintenance;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your role and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		owner = new HouseOwnerRole("owner");		
		customer = new MockHouseCustomerRole("mockcustomer");		
		maintenance = new MockHouseMaintenanceManagerRole("mockmaintenance");
		
	}	
	
	/**
	 * This tests the owner under very simple terms: one customer is asked to pay rent.
	 */
	public void testOne(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// runs first before this test!
		/*
		 * This wont work because the ownerGui in HouseOwnerRole isn't instantiated.
		 * This test can work if the ownerGui actions are commented out in HouseOwnerRole
		 */

		assertEquals("Owner should have 0 customers. It doesn't.", owner.customers.size(), 0);		
		
		owner.setMaintenanceManager(maintenance);
		owner.createHouses();
		
		
		//House house = new House(owner, customer, maintenance, "Rented Home", 1111, 500.00, 100.00, 50.00, HouseState.maintained);
		House house = owner.findAvailableHouse();
		owner.msgIWantToLiveHere(customer);
		
		
		assertEquals("MockCustomer should have an empty event log before the Owner's scheduler is called. Instead, the Mockcustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
				
		assertTrue("OwnerCustomers should contain a customer with state == wantsToBuyProperty. It doesn't.",
				owner.customers.get(0).cs == CustomerState.wantsToBuyProperty);
		
		assertTrue("OwnerCustomers should contain a customer with the right house. It doesn't.", 
				owner.customers.get(0).house == house);
		
		assertTrue("Owner's scheduler should have returned true (needs to react to customer's wantsToBuyProperty), but didn't.", 
				owner.pickAndExecuteAnAction());
		
		assertEquals(
				"MockCustomer should not have an empty event log after the Owner's scheduler is called for the first time. "
				+ "Instead, the Mockcustomers's event log reads: "
						+ customer.log.toString(), 1, customer.log.size());
		
		assertTrue("Ownercustomers should contain a customer with state == wantsToBuyProperty. It doesn't.",
				owner.customers.get(0).cs == CustomerState.askedToPayDeposit);
		
		customer.msgPaySecurityDeposit(house);//Not necessary, but helps with understanding the steps of the test
		
		owner.msgHereIsSecurityDeposit(customer, 100.00);
				
		assertTrue("Owner's scheduler should have returned true (needs to react to customer's paidDeposit), but didn't.", 
				owner.pickAndExecuteAnAction());
		
		assertTrue("OwnerCustomers should contain a customer with state == movedIn. It doesn't.",
				owner.customers.get(0).cs == CustomerState.movedIn);
		
		customer.msgMoveIn(house);//Not necessary, but helps with understanding the steps of the test
		
		owner.msgResidentNeedsToPayRent(customer);
		
		assertTrue("Owner's scheduler should have returned true (needs to react to customer's needsToPayRent), but didn't.", 
				owner.pickAndExecuteAnAction());
		
		assertTrue("OwnerCustomers should contain a customer with state == wantsToBuyProperty. It doesn't.",
				owner.customers.get(0).cs == CustomerState.askedToPayRent);
		
		customer.msgPayRent();//Not necessary, but helps with understanding the steps of the test
		
		owner.msgHereIsRent(customer, 500.00);
		
		assertFalse("Owner's scheduler should have returned false (nothing to do), but didn't.", 
				owner.pickAndExecuteAnAction());
		
		owner.msgHouseNeedsMaintenance(customer);
		
		assertTrue("Owner's scheduler should have returned true (needs to react to house's needsMaintenance), but didn't.", 
				owner.pickAndExecuteAnAction());
		
		assertTrue("OwnerHouses should contain a house with state == waitingForMaintenance. It doesn't.",
				owner.houses.get(0).hs == HouseState.waitingForMaintenance);
		
		maintenance.msgINeedMaintenance(house);//Not necessary, but helps with understanding the steps of the test
		
		owner.msgPayMaintenanceFee(house);
		
		assertTrue("Owner's scheduler should have returned true (needs to react to house's justMaintained), but didn't.", 
				owner.pickAndExecuteAnAction());
		
		assertTrue("OwnerHouses should contain a house with state == maintained. It doesn't.",
				owner.houses.get(0).hs == HouseState.maintained);
		
		maintenance.msgHereIsMaintenanceFee(50.00);//Not necessary, but helps with understanding the steps of the test
		
		assertFalse("Owner's scheduler should have returned false (nothing to do), but didn't.", 
				owner.pickAndExecuteAnAction());
		
	}
}

