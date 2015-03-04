package simCity.interfaces;

import simCity.Restaurant1.Restaurant1CustomerRole;
import simCity.Restaurant1.Restaurant1WaiterRole;

/**
 * A Host interface built to unit test a CashierAgent.
 *
 * @author Marina Hierl 
 *
 */
public interface Restaurant1Host {
	/**
	 * Sent by the customer to indicate it has arrived in the Restaurant 
	 * and wants to be served
	 * 
	 * @param restaurant1Customer The customer that wants food
	 */
	void msgIWantFood(Restaurant1Customer restaurant1Customer);

	/**
	 * Sent by the customer to pay an outstanding bill
	 * 
	 * @param restaurant1Customer The customer that gave money
	 */
	void msgHereIsMoney(Restaurant1Customer restaurant1Customer);

	/**
	 * Sent by the customer to let the host know his table is clear
	 * 
	 * @param restaurant1Customer The customer that has left
	 */
	void msgLeavingTable(Restaurant1Customer restaurant1Customer);

	/**
	 * Sent by waiter to let Host know he is at the start position
	 * 
	 * @param restaurant1Waiter
	 */
	void msgAtStart(Restaurant1Waiter restaurant1Waiter);

	/**
	 * Sent by waiter to let Host know a customer could not pay for his meal
	 * 
	 * @param customerName Name of customer
	 * @param order Thing he ate 
	 */
	void msgCustomerWithDebt(String customerName, String order);

	/**
	 * Sent by waiter to request a break 
	 * 
	 * @param restaurant1WaiterRole Waiter 
	 */
	void msgAskForBreak(Restaurant1Waiter waiter);

	/**
	 * Sent by waiter to let host know he's back from break 
	 * 
	 * @param restaurant1WaiterRole Waiter 
	 */
	void msgBackAtWork(Restaurant1Waiter waiter);

	/**
	 * Sent by anyone to get the host's name
	 * 
	 * @return string name 
	 */
	String getName();
	
}