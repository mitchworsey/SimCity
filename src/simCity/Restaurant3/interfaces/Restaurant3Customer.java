package simCity.Restaurant3.interfaces;

import simCity.Restaurant3.Restaurant3Check;
import simCity.Restaurant3.Restaurant3WaiterRole.RestaurantMenu;
import simCity.Restaurant3.gui.Restaurant3CustomerGui;

public interface Restaurant3Customer {

	public abstract void setHost(Restaurant3Host host);

	public abstract double getCash();

	public abstract void setCash(double cash);

	public abstract String getChoice();

	public abstract void setWaiter(Restaurant3Waiter waiter);

	public abstract void setCashier(Restaurant3Cashier cashier);

	public abstract String getCustomerName();

	public abstract boolean isImpatient();

	// Messages

	public abstract void gotHungry();

	public abstract void msgRestaurantFull();

	public abstract void msgFollowMeToTable(RestaurantMenu m);

	public abstract void msgWhatDoYouWant();

	public abstract void msgPleaseReOrder(RestaurantMenu rm);

	public abstract void msgHereIsYourFood();

	public abstract void msgHereIsYourCheck(Restaurant3Check c);

	public abstract void msgHereIsYourChange(double change);

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgAtCashier();

	public abstract void msgAtWaitingArea();

	public abstract String getName();

	public abstract String toString();

	public abstract void setGui(Restaurant3CustomerGui g);

	public abstract Restaurant3CustomerGui getGui();

}