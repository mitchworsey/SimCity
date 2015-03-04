package simCity.restaurant2.interfaces;

import java.util.List;

import simCity.restaurant2.Restaurant2CookRole.Order;
import simCity.restaurant2.gui.Restaurant2CookGui;

public interface Restaurant2Cook {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract void addMarket(Restaurant2Market m);

	/**
	 * Messages
	 */

	// from Restaurant2WaiterRole
	public abstract void HereIsOrder(Restaurant2Waiter w, Restaurant2Customer c,
			String foodChoice, int tableNum);

	// from Restaurant2MarketRole
	public abstract void NotEnoughFood(String foodName, int amount);

	// from Restaurant2MarketRole
	public abstract void HereIsFood(String foodName, int amount);

	// from makeOrder() timer timeout
	public abstract void FoodDone(Order o);

	// from plateOrder() timer timeout
	public abstract void PlateDone(Order o);

	public abstract void setGui(Restaurant2CookGui gui);

	public abstract Restaurant2CookGui getGui();

	public abstract void refillInventory();

	public abstract void depleteInventory();

	public abstract void depleteInventoryInitially();

	public abstract void resetMarketCounter();

	public abstract void HereIsOrder();

}