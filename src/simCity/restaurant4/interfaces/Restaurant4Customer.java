package simCity.restaurant4.interfaces;

import simCity.restaurant4.Restaurant4CashierRole;
import simCity.restaurant4.Restaurant4HostRole;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Jessica Wang
 *
 */
public interface Restaurant4Customer {

	/**
	 * @param amount The cost that was not paid and is added to the amount owed by the customer
	 *
	 * Sent by the cashier if the customer does not have enough money to pay.
	 */
	public abstract void msgPayNextTime(double amount);

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedGoToWaitingArea();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void gotHungry();

	public abstract Object getGui();

	public abstract void setWaiter(Restaurant4Waiter waiter);

	public abstract void msgRestaurantIsFull(int seat);

	public abstract void msgFollowMe(int table);

	public abstract void msgWhatDoYouWant();

	public abstract void msgChooseNewOrder();

	public abstract void msgHereIsYourFood();

	public abstract void msgHereIsYourCheck(double checkAmt);

	public abstract void setHost(Restaurant4Host h1);

	public abstract void setCashier(Restaurant4Cashier ca);

}