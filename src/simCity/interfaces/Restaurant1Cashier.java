package simCity.interfaces;

import simCity.interfaces.Restaurant1Customer;
import simCity.interfaces.Restaurant1Waiter;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Marina Hierl 
 *
 */
public interface Restaurant1Cashier {
        /**
         * @param price The amount of money that the restaurant owes the market
         * 
         * Sent from a market to let the cashier know it owes money
         */
		public void msgFoodBill(double price);
		
		/**
		 * @param customerName The customer that needs a bill
		 * @param amount The amount of money on the bill
		 * @param waiter The waiter completing the transaction between the cashier and customer
		 * 
		 * Sent from the waiter to let the cashier know a customer has eaten and owes money
		 */
		public void msgHereIsAnOrder(String customerName, double amount, Restaurant1Waiter waiter);
		
		/**
		 * @param customer The customer that has given money for their bill
		 * @param order The amount of money the customer gave
		 * @param waiter The waiter completing the transaction between the cashier and customer
		 * 
		 * Sent from the waiter to give the cashier money from a paticular bill
		 */
		public void msgHereIsPayment(Restaurant1Customer customer, String order, Restaurant1Waiter waiter);
		
		/**
		 * @param customer The customer that has a bill but cannot pay it
		 * @param order The amount of money that the bill was
		 * @param waiter The waiter completing the transaction between the cashier and customer
		 * 
		 * Sent from the waiter to let the cashier know that a bill could not be paid 
		 */
		public void msgCustomerCannotPay(Restaurant1Customer customer, String order, Restaurant1Waiter waiter);
}