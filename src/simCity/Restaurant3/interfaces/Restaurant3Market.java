package simCity.Restaurant3.interfaces;

import java.util.List;

import simCity.Restaurant3.Restaurant3MarketBill;
import simCity.Restaurant3.Restaurant3MarketRole.Order;

public interface Restaurant3Market {

	public abstract void setLowInventory();

	public abstract String getName();

	public abstract List getOrders();

	public abstract void msgHereIsOrder(Restaurant3Cook c, String choice,
			int numberOrdered);

	public abstract void msgOrderProcessed(Order o);

	public abstract void msgHereIsPayment(Restaurant3MarketBill mb, double cash);

	public abstract void deliverOrder(Order o);

	public abstract void deliverMarketBill(Restaurant3MarketBill mb);

	public abstract void giveChangeToCashier(Restaurant3MarketBill mb);

	public abstract void processOrder(Order o);

	public abstract void outOfFood(Order o);

}