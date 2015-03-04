package simCity.Restaurant3.interfaces;

import java.util.List;

import simCity.Restaurant3.Restaurant3CookRole.Order;
import simCity.Restaurant3.gui.Restaurant3CookGui;

public interface Restaurant3Cook {

	public abstract void setWaiterName(String name);

	public abstract void setGui(Restaurant3CookGui cg);

	public abstract String getName();

	public abstract List getOrders();

	public abstract void setCashier(Restaurant3Cashier cashier);

	public abstract Restaurant3Cashier getCashier();

	public abstract void msgRestockInventory(Restaurant3Waiter w);

	public abstract void msgHereIsOrder(Restaurant3Waiter w, String choice,
			int table);

	public abstract void msgFoodDone(Order o);

	public abstract void msgOutOfStock(String choice);

	public abstract void msgOrderBeingProcessed(String choice);

	public abstract void msgOrderDelivered(String choice, int numberFulfilled);

	public abstract void msgAtCookingArea();

	public abstract void msgAtPlatingArea();

	public abstract void msgAtFridge();

	public abstract void updateMenu();

	public abstract void fillBegInventory();

	public abstract void restockInventory();

	public abstract void plateIt(Order o);

	public abstract void cookIt(Order o);

	public abstract void orderFood(Restaurant3Market m, String choice);

	public abstract void outOfFood(Order o);

	public abstract void addMarket(Restaurant3Market m);

}