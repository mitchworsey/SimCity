package simCity.interfaces;

import simCity.Restaurant1.Restaurant1WaiterRole;
import simCity.gui.Restaurant1.Restaurant1CookGui;

/**
 * A Cook interface built to unit test a CashierAgent.
 *
 * @author Marina Hierl 
 *
 */
public interface Restaurant1Cook {

	/**
	 * Sent by waiter to give an order to cook to prepare
	 * 
	 * @param sendTo Customer that order is for
	 * @param food Food that customer wants

	 * @param restaurant1Waiter Waiter serving the customer
	 */

	void hereIsAnOrder(Restaurant1Customer sendTo, String food,
			Restaurant1Waiter restaurant1Waiter);

	/**
	 * Sent by waiter to let the cook know he picked up an order
	 * 
	 * @param food Food he picked up 
	 */
	void msgGotOrder(String food);

	/**
	 * Sent  by gui to establish relationship 
	 * 
	 * @param restaurant1CookGui The gui
	 */
	void setGui(Restaurant1CookGui restaurant1CookGui);
	
}
