package simCity.restaurant4.interfaces;

import java.util.HashMap;
import java.util.Map;

/**
 * A sample Waiter interface built to unit test a WaiterAgent.
 *
 * @author Jessica Wang
 *
 */
public interface Restaurant4Waiter {
	/**
	 * @param amount that is on the check
	 * @param table number
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	public abstract void msgCheckComputed(double amount, int table);

	public abstract void msgAtDestination();

	public abstract void msgLeftTable();

	public abstract Object getGui();

	public abstract void msgOrderReady(int table);

	public abstract void msgOutOfFood(String choice);

	public abstract void msgReadyToOrder(Restaurant4Customer customer);

	public abstract void msgDoneEatingAndLeaving(Restaurant4Customer customer);

	public abstract void msgHereIsMyOrder(Restaurant4Customer customer, String order);

	public abstract void msgBreakPermitted();

	public abstract void msgKeepWorking();

	public abstract void msgSeatCustomer(Restaurant4Customer customer, int tableNum, int seatNum);

}