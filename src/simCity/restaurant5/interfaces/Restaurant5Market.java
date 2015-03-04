package simCity.restaurant5.interfaces;

import java.util.List;


import simCity.restaurant5.Restaurant5MarketRole;
import simCity.restaurant5.Restaurant5MarketRole.Food;
import simCity.restaurant5.MarketOrder;

public interface Restaurant5Market {

	/* (non-Javadoc)
	 * @see restaurant.Market#setCook(restaurant.Cook)
	 */
	public abstract void setCook(Restaurant5Cook cook);

	public abstract void setCashier(Restaurant5Cashier cashier);

	/* (non-Javadoc)
	 * @see restaurant.Market#getMaitreDName()
	 */
	public abstract String getMaitreDName();

	/* (non-Javadoc)
	 * @see restaurant.Market#getName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see restaurant.Market#getOrder()
	 */
	public abstract List<MarketOrder> getOrder();

	/* (non-Javadoc)
	 * @see restaurant.Market#msgCookNeedsFood(restaurant.Cook, restaurant.MarketOrder)
	 */
	public abstract void msgCookNeedsFood(Restaurant5Cook cook,
			MarketOrder morder);

	public abstract void msgHereIsPayment(int payment);

	public abstract void msgPayBackDebt(int debt);

	/* (non-Javadoc)
	 * @see restaurant.Market#findFoodFromMap(java.lang.String)
	 */
	public abstract Food findFoodFromMap(String food);

	/* (non-Javadoc)
	 * @see restaurant.Market#findFoodFromOrder(java.lang.String)
	 */
	public abstract MarketOrder findFoodFromOrder(String order);

	//////////////// UTILITIES ////////////////
	/* (non-Javadoc)
	 * @see restaurant.Market#callStateChange()
	 */
	public abstract void callStateChange();

}