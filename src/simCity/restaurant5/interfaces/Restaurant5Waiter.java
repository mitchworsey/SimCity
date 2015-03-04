package simCity.restaurant5.interfaces;

import java.util.List;

import simCity.restaurant5.Restaurant5WaiterRole;
import simCity.restaurant5.Restaurant5WaiterRole.MyCustomer;
import simCity.restaurant5.gui.Restaurant5WaiterGui;

public interface Restaurant5Waiter {

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setHost(restaurant.Host)
	 */
	public abstract void setHost(Restaurant5Host host);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCook(restaurant.Cook)
	 */
	//@Override
	public abstract void setCook(Restaurant5Cook cook);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCashier(restaurant.Cashier)
	 */
	//@Override
	public abstract void setCashier(Restaurant5Cashier cashier);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getMaitreDName()
	 */
	//@Override
	public abstract String getMaitreDName();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#toString()
	 */
	public abstract String toString();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getMyCustomers()
	 */
	//@Override
	public abstract List<MyCustomer> getMyCustomers();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyToServe()
	 */
	//@Override
	public abstract void msgReadyToServe();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgSeatCustomer(restaurant.Customer, restaurant.Waiter, int)
	 */
	//@Override
	public abstract void msgSeatCustomer(Restaurant5Customer c,
			Restaurant5Waiter w, int table);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgLeavingTable(restaurant.Customer)
	 */
	public abstract void msgLeavingTable(Restaurant5Customer cust);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtTable()
	 */
	public abstract void msgAtTable();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtKitchen()
	 */
	public abstract void msgAtKitchen();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtFront()
	 */
	public abstract void msgAtWaitingArea();
	
	public abstract void msgAtCashier();
	
	public abstract void msgWaitComplete();

	public abstract void msgAtHome();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyToOrder(restaurant.Customer)
	 */
	//@Override
	public abstract void msgReadyToOrder(Restaurant5Customer c);

	//~~~~~~~~~~~~~~~~~~~~~~~~~COOK MSGs~~~~~~~~~~~~~~~~~~~~~~//
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgGaveCookOrder()
	 */
	//@Override
	public abstract void msgGaveCookOrder();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsMyOrder(java.lang.String, restaurant.Customer)
	 */
	//@Override
	public abstract void msgHereIsMyOrder(String choice,
			Restaurant5Customer customer);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOrderIsReady(java.lang.String, int)
	 */
	//@Override
	public abstract void msgOrderIsReady(String order, int table);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOutOfFood(java.lang.String, int)
	 */
	//@Override
	public abstract void msgOutOfFood(String outofFood, int table);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsCheck(restaurant.Customer, int)
	 */
	//@Override
	public abstract void msgHereIsCheck(Restaurant5Customer customer,
			int totalCheck);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgWhereIsMyCheck(restaurant.Customer)
	 */
	//@Override
	public abstract void msgWhereIsMyCheck(Restaurant5Customer cust);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#gotTired()
	 */
	//@Override
	public abstract void gotTired();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgDenyBreak()
	 */
	//@Override
	public abstract void msgDenyBreak();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAcceptBreak()
	 */
	public abstract void msgAcceptBreak();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#findCustomer(restaurant.Customer)
	 */
	public abstract MyCustomer findCustomer(Restaurant5Customer c);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#findCustomerTable(int)
	 */
	public abstract MyCustomer findCustomerTable(int table);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#findCustomerOrder(java.lang.String)
	 */
	public abstract MyCustomer findCustomerOrder(String order);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#guiSetBreak()
	 */
	public abstract void guiSetBreak();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#askToBreak()
	 */
	public abstract void askToBreak();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#onBreak()
	 */
	public abstract void onBreak();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#BreakDenied()
	 */
	public abstract void BreakDenied();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#BreakTime()
	 */
	public abstract void BreakTime();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#backFromBreak()
	 */
	public abstract void backFromBreak();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#GiveCashierOrder(restaurant.WaiterAgent.MyCustomer, java.lang.String)
	 */
	public abstract void GiveCashierOrder(MyCustomer mcust, String order);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#GiveCustomerCheck(restaurant.WaiterAgent.MyCustomer)
	 */
	public abstract void GiveCustomerCheck(MyCustomer customer);

	///////////////// UTILITIES /////////////////
	/* (non-Javadoc)
	 * @see restaurant.Waiter#callStateChange()
	 */
	public abstract void callStateChange();

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setGui(restaurant.gui.WaiterGui)
	 */
	public abstract void setGui(Restaurant5WaiterGui gui);

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getGui()
	 */
	public abstract Restaurant5WaiterGui getGui();




}