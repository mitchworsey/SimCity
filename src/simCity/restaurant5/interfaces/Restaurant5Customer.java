package simCity.restaurant5.interfaces;

import java.util.List;

import simCity.restaurant5.gui.Restaurant5CustomerGui;

public interface Restaurant5Customer {

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHost(restaurant.HostAgent)
	 */
	public abstract void setHost(Restaurant5Host host);

	/* (non-Javadoc)
	 * @see restaurant.Customer#setWaiter(restaurant.WaiterAgent)
	 */
	public abstract void setWaiter(Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Customer#setCashier(restaurant.Cashier)
	 */
	public abstract void setCashier(Restaurant5Cashier cashier);

	/* (non-Javadoc)
	 * @see restaurant.Customer#getCustomerName()
	 */
	public abstract String getCustomerName();

	/* (non-Javadoc)
	 * @see restaurant.Customer#gotHungry()
	 */
	public abstract void gotHungry();

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgNoFoodLeaveOrReorder()
	 */
	public abstract void msgNoFoodLeaveOrReorder();

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMe(int, java.util.List)
	 */
	public abstract void msgFollowMe(int table, List<String> menu);

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgSitAtTable(java.util.List)
	 */
	public abstract void msgSitAtTable(List<String> menu);

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgWhatDoYouWant()
	 */
	public abstract void msgWhatDoYouWant();

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourFood(java.lang.String)
	 */
	public abstract void msgHereIsYourFood(String order);

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourCheck(int)
	 */
	public abstract void msgHereIsYourCheck(int check);

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToSeat()
	 */
	public abstract void msgAnimationFinishedGoToSeat();

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedLeaveRestaurant()
	 */
	public abstract void msgAnimationFinishedLeaveRestaurant();

	/* (non-Javadoc)
	 * @see restaurant.Customer#randMoneyLevel()
	 */
	public abstract void randMoneyLevel();

	/* (non-Javadoc)
	 * @see restaurant.Customer#DoIHaveEnoughMoney()
	 */
	public abstract boolean DoIHaveEnoughMoney();

	/* (non-Javadoc)
	 * @see restaurant.Customer#getMoneyLevel()
	 */
	public abstract int getMoneyLevel();

	/* (non-Javadoc)
	 * @see restaurant.Customer#getName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see restaurant.Customer#getHungerLevel()
	 */
	public abstract int getHungerLevel();

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHungerLevel(int)
	 */
	public abstract void setHungerLevel(int hungerLevel);

	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	public abstract String toString();

	/* (non-Javadoc)
	 * @see restaurant.Customer#setGui(restaurant.gui.CustomerGui)
	 */
	public abstract void setGui(Restaurant5CustomerGui g);

	/* (non-Javadoc)
	 * @see restaurant.Customer#getGui()
	 */
	public abstract Restaurant5CustomerGui getGui();


}