package simCity.interfaces;

import simCity.Restaurant1.Restaurant1HostRole;

/**
 * A Customer interface built to unit test a CashierAgent.
 *
 * @author Marina Hierl 
 *
 */
public interface Restaurant1Customer {
	/**
	 * Sent from waiter to retrieve customer's name
	 */
	public abstract String getCustomerName();
	
	/**
	 * @param table The table number for the customer to sit at
	 * 
	 * Sent by the waiter to direct the customer to move to the given table
	 */
	public abstract void msgSitAtTable(int table);
	
	/**
	 * Sent by the waiter to instruct the customer to decide
	 */
	public abstract void msgWhatWouldYouLike();
	
	/**
	 * Sent by the waiter to receive a new order from the customer when their original order was not available 
	 */
	public abstract void msgReOrder();
	
	/**
	 * Sent by the waiter to instruct the customer to begin eating
	 */
	public void msgHereIsYourFood();
	
	/**
	 * @param amt The amount of money that the customer owes
	 * 
	 * Sent by the waiter to give the customer the bill
	 */
	public void msgHereIsYourCheck(double amt);

	/**
	 * Sent by host to let customer know where to wait
	 * 
	 * @param i Position to stand
	 */
	public abstract void msgStandAt(int i);

	/**
	 * Sent by host to get waiter assigned to customer
	 * 
	 * @return Waiter
	 */
	public abstract Restaurant1Waiter getWaiter();

	/**
	 * Sent by host to assign waiter to customer
	 * 
	 * @param waiter The waiter 
	 */
	public abstract void setWaiter(Restaurant1Waiter waiter);

	/**
	 * Sent by host to request money for outstanding bill
	 * 
	 * @param amount Amount of money owed 
	 */
	public abstract void msgRequestMoney(double amount);

	/**
	 * Sent by gui signaling that they have exited the restaurant
	 */
	public abstract void msgAnimationFinishedLeaveRestaurant();

	/**
	 * Sent by gui signaling that they have reached their seat 
	 */
	public abstract void msgAnimationFinishedGoToSeat();

	/**
	 * Sent by gui signaling that customer has entered restaurant and wants food
	 */
	public abstract void gotHungry();

	/**
	 * Sent by gui to set the host when customer enters restaurant
	 * 
	 * @param host Host role 
	 */
	public abstract void setHost(Restaurant1Host host);


}