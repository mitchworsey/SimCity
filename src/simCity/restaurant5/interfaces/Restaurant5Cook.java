package simCity.restaurant5.interfaces;

import java.util.List;

import simCity.interfaces.MarketGrocer;
import simCity.market.MarketGrocerRole;
import simCity.restaurant5.Restaurant5CookRole;
import simCity.restaurant5.Restaurant5CookRole.Food;
import simCity.restaurant5.MarketOrder;
import simCity.restaurant5.Restaurant5CookRole.MyMarket;
import simCity.restaurant5.Restaurant5CookRole.Order;
import simCity.restaurant5.gui.Restaurant5CookGui;

public interface Restaurant5Cook {

	/* (non-Javadoc)
	 * @see restaurant.Cook#getMaitreDName()
	 */
	public abstract String getMaitreDName();

	public abstract void setGui(Restaurant5CookGui cookgui);

	public abstract void setGrocer(MarketGrocerRole mgrocer);
	/* (non-Javadoc)
	 * @see restaurant.Cook#getName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see restaurant.Cook#getOrder()
	 */
	public abstract List<Order> getOrder();

	public abstract void msgAtCook();

	public abstract void msgAtPlate();

	public abstract void msgAtHome();

	/* (non-Javadoc)
	 * @see restaurant.Cook#msgHereIsOrder(restaurant.WaiterAgent, java.lang.String, int)
	 */
	public abstract void msgHereIsOrder(Restaurant5Waiter w, String choice,
			int table);

	public abstract void msgHereIsOrder(List<Order> sharedOrderWheel);
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#msgFoodDone(restaurant.CookAgent.Order)
	 */
	public abstract void msgFoodDone(Order o);

	/* (non-Javadoc)
	 * @see restaurant.Cook#msgOrderIsShipped(restaurant.MarketOrder)
	 */
	public abstract void msgOrderIsShipped(MarketOrder order, int quantity);

	/* (non-Javadoc)
	 * @see restaurant.Cook#msgMarketOutOfFood(restaurant.MarketAgent, java.lang.String)
	 */
	public abstract void msgMarketOutOfFood(Restaurant5Market market,
			String outof);

	/* (non-Javadoc)
	 * @see restaurant.Cook#addMarket(restaurant.MarketAgent)
	 */
	public abstract void addMarket(Restaurant5Market market);

	/* (non-Javadoc)
	 * @see restaurant.Cook#findMarket(restaurant.MarketAgent)
	 */
	public abstract MyMarket findMarket(Restaurant5Market market);

	/* (non-Javadoc)
	 * @see restaurant.Cook#findFoodFromMap(java.lang.String)
	 */
	public abstract Food findFoodFromMap(String food);

	/* (non-Javadoc)
	 * @see restaurant.Cook#findFoodFromOrder(java.lang.String)
	 */
	public abstract Order findFoodFromOrder(String order);

	//////// The animation DoXYZ() routines ////////
	public abstract void DoCook();

	public abstract void DoPlate();

	public abstract void DoGoHome();

	//////////////// UTILITIES ////////////////
	/* (non-Javadoc)
	 * @see restaurant.Cook#callStateChange()
	 */
	public abstract void callStateChange();

	public abstract void msgOrderIsShipped(MarketOrder order);

	public abstract void msgHereIsOrder();


}