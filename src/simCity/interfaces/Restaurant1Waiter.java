package simCity.interfaces;

import simCity.Restaurant1.Restaurant1CookRole;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.gui.Restaurant1.Restaurant1WaiterGui;
import simCity.interfaces.Restaurant1Cashier;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Marina Hierl 
 *
 */
public interface Restaurant1Waiter {
        /**
         * @param amount The amount of money the customer owes
         * @param customerName The name of the customer that owes the money
         *
         * Sent by the cashier telling the waiter that a specific customer owes the given amount of money
         */
		public abstract void msgCustomerOwesMoney(double amount, String customerName);

        /**
         * @param customerName The name of the Customer that was unable to pay the full price of the bill
         * @param order The food that the customer was unable to pay for
         * 
         * Sent by the cashier to tell the waiter that the customer failed to pay the bill in full
         */
        public abstract void msgCustomerHasDebt(String customerName, String order);
        
        /**
         * @param cash The cashier that the waiter is set to
         * 
         * Sent by the cashier to let the waiter know which cashier to interact with 
         */
        public abstract void msgSetCashier(Restaurant1Cashier cash);
        
        /**
         * Sent by cashier to get the name of the waiter
         */
        public abstract String getName();
        
        /**
         * @param customerName The customer that the cashier received money from
         * 
         * Sent by the cashier to let the waiter know which custmer has paid their bill
         */
    	public void msgCustomerPaid(String customerName);

    	/**
    	 * Sent by the customer to alert the waiter that he has made a decision
    	 * 
    	 * @param customerName Name of customer thats ready to order
    	 */
		public abstract void msgReadyToOrder(String customerName);

		/**
		 * Sent by the customer to let the waiter know his table is clear
		 * 
		 * @param customerName The name of the customer that left
		 */
		public abstract void msgLeavingTable(String customerName);

		/** 
		 * Sent by the customer to let the waiter know what food he/she wants
		 * 
		 * @param customerName Name of the Customer
		 * @param food Name of the food 
		 */
		public abstract void msgHereIsMyChoice(String customerName, String food);

		/**
		 * Sent by the customer to let the waiter know he is done eating
		 * 
		 * @param customerName Name of customer
		 * @param food Name of food they just ate 
		 */
		public abstract void msgReadyForCheck(String customerName, String food);

		/**
		 * Set by cook to establish relationship, set cook as given cook
		 * 
		 * @param restaurant1Cook The restaurant cook 
		 */
		public abstract void msgSetCook(Restaurant1Cook restaurant1Cook);

		/**
		 * Sent by the Cook to let the waiter know an order is ready
		 * 
		 * @param sendTo The person receiving the food
		 * @param food The food 
		 */
		public abstract void msgOrderIsReady(Restaurant1Customer sendTo,
				String food);

		/**
		 * Sent by the cook to let the waiter know an order cannot be fufilled
		 * 
		 * @param sendTo The customer who ordered the food
		 */
		public abstract void msgNotEnoughFood(Restaurant1Customer sendTo);

		/**
		 * Sent by the Host to establish relationship 
		 * 
		 * @param restaurant1Host
		 */
		public abstract void msgSetHost(Restaurant1Host restaurant1Host);

		/**
		 * Sent by Host to let waiter know the table is free
		 * 
		 * @param number Table number 
		 */
		public abstract void msgUpdateTablesNull(int number);

		/**
		 * sent by host to let waiter know who is occupying the table
		 * 
		 * @param occupant Person at table
		 * @param number Table number 
		 */
		public abstract void msgUpdateTables(Restaurant1Customer occupant,
				int number);

		/**
		 * Sent by Host to let waiter know he can go on break
		 */
		public abstract void msgGoOnBreak();

		/**
		 * Sent by Host to let waiter know he may not go on break
		 */
		public abstract void msgNoBreak();

		/**
		 * Sent by Host to instruct waiter to seat customer 
		 * 
		 * @param customer
		 * @param number
		 */
		public abstract void msgSeatCustomer(Restaurant1Customer customer,
				int number);

		/**
		 * Sent by gui to indicate which table it's at 
		 * 
		 * @param i Tabel number 
		 */
		public void msgAtTable(int i);

		/**
		 * Sent by gui signaling that it reached the start position
		 */
		public abstract void msgAtStart();

		/**
		 * Sent by gui signaling that it reached the cook
		 */
		public abstract void msgAtCook();

		/**
		 * Sent by gui signaling that waiter wants a break
		 */
		public abstract void wantsBreak();

		/**
		 * Sent by gui to set the agent 
		 * 
		 * @param restaurant1WaiterGui The gui
		 */
		public abstract void setGui(Restaurant1WaiterGui restaurant1WaiterGui);
	

}