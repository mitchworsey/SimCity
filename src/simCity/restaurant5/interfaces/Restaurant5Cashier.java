package simCity.restaurant5.interfaces;

import java.util.List;

import simCity.market.MarketDeliveryTruck;
import simCity.restaurant5.Restaurant5CashierRole;
import simCity.restaurant5.Restaurant5CashierRole.MarketBill;
import simCity.restaurant5.Restaurant5CashierRole.Order;

public interface Restaurant5Cashier {

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getOrder()
	 */
	public abstract List<Order> getOrder();

	public abstract void addMarket(Restaurant5Market market);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgHereIsOrder(restaurant.WaiterAgent, restaurant.CustomerAgent, java.lang.String)
	 */
	public abstract void msgHereIsOrder(Restaurant5Waiter waiter,
			Restaurant5Customer customer, String order);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgCheckCalculated(restaurant.CashierAgent.Order)
	 */
	public abstract void msgCheckCalculated(Order o);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgHereIsOrderPayment(restaurant.CustomerAgent, int)
	 */
	public abstract void msgHereIsOrderPayment(
			Restaurant5Customer customer, int money);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgIDoNotHaveEnoughMoney(restaurant.CustomerAgent, int)
	 */
	public abstract void msgIDoNotHaveEnoughMoney(
			Restaurant5Customer customer, int debt);

	//public abstract void msgShippedOrder(Restaurant5Market market, int bill);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#findOrderWithCustomer(restaurant.CustomerAgent)
	 */
	public abstract Order findOrderWithCustomer(Restaurant5Customer customer);

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public abstract boolean pickAndExecuteAnAction();

	public abstract void PayMarket(MarketBill mbill);

	//////////////// UTILITIES ////////////////
	/* (non-Javadoc)
	 * @see restaurant.Cashier#callStateChange()
	 */
	public abstract void callStateChange();

	public abstract void msgShippedOrder(MarketDeliveryTruck market, int bill);

}