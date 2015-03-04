package simCity.restaurant5.interfaces;

import java.util.Collection;
import java.util.List;

import simCity.restaurant5.Restaurant5HostRole;
import simCity.restaurant5.Restaurant5HostRole.MyWaiter;
import simCity.restaurant5.gui.Restaurant5HostGui;

public interface Restaurant5Host {

	/* (non-Javadoc)
	 * @see restaurant.Host#getMaitreDName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see restaurant.Host#getCustomers()
	 */
	public abstract List getCustomers();

	/* (non-Javadoc)
	 * @see restaurant.Host#getTables()
	 */
	public abstract Collection getTables();

	/* (non-Javadoc)
	 * @see restaurant.Host#getMyWaiters()
	 */
	public abstract List getMyWaiters();

	/* (non-Javadoc)
	 * @see restaurant.Host#msgIWantBreak(restaurant.WaiterAgent)
	 */
	public abstract void msgIWantBreak(Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Host#msgBackFromBreak(restaurant.WaiterAgent)
	 */
	public abstract void msgBackFromBreak(Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Host#msgIWantFood(restaurant.Customer)
	 */
	public abstract void msgIWantFood(Restaurant5Customer cust);

	/* (non-Javadoc)
	 * @see restaurant.Host#msgIAmHereToWork(restaurant.WaiterAgent)
	 */
	public abstract void msgIAmHereToWork(Restaurant5Waiter wait);

	/* (non-Javadoc)
	 * @see restaurant.Host#findWaiter(restaurant.WaiterAgent)
	 */
	public abstract MyWaiter findWaiter(Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Host#msgLeavingTable(restaurant.Customer, restaurant.WaiterAgent)
	 */
	public abstract void msgLeavingTable(Restaurant5Customer cust,
			Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Host#howManyCustomers()
	 */
	public abstract int howManyCustomers();

	/* (non-Javadoc)
	 * @see restaurant.Host#numWaitersWorking()
	 */
	public abstract int numWaitersWorking();

	/* (non-Javadoc)
	 * @see restaurant.Host#AddWaiter(restaurant.WaiterAgent)
	 */
	public abstract void AddWaiter(Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Host#AcceptWaiterBreak(restaurant.WaiterAgent)
	 */
	public abstract void AcceptWaiterBreak(Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Host#DenyWaiterBreak(restaurant.WaiterAgent)
	 */
	public abstract void DenyWaiterBreak(Restaurant5Waiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Host#WaitBackFromBreak(restaurant.WaiterAgent)
	 */
	public abstract void WaitBackFromBreak(Restaurant5Waiter waiter);

	// //////////////UTILITIES /////////////////
	/* (non-Javadoc)
	 * @see restaurant.Host#callStateChange()
	 */
	public abstract void callStateChange();

	/* (non-Javadoc)
	 * @see restaurant.Host#setGui(restaurant.gui.HostGui)
	 */
	public abstract void setGui(Restaurant5HostGui gui);

	/* (non-Javadoc)
	 * @see restaurant.Host#getGui()
	 */
	public abstract Restaurant5HostGui getGui();

}