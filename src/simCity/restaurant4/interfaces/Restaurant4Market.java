package simCity.restaurant4.interfaces;

import java.util.List;

import simCity.restaurant4.Restaurant4CookRole.MyMarketOrder;

/**
 * A Market interface built to unit test a MarketAgent.
 *
 * @author Jessica Wang
 *
 */
public interface Restaurant4Market {
	/**
	 * @param amount that is on the bill 
	 *
	 * Sent by the cashier if he has enough money to pay the bill to the market.
	 */
	public abstract void msgHereIsPayment(double amount);

	public abstract void msgCannotPayBill(double amount);

	public abstract void msgNeedMoreFood(List<MyMarketOrder> marketOrders);

}