package simCity.restaurant2.interfaces;

import simCity.restaurant2.Restaurant2CashierRole.Check;
import simCity.restaurant2.gui.Restaurant2WaiterGui;

public interface Restaurant2Waiter {

	/**
	 * hack to establish connection to Restaurant2Host agent.
	 */
	public abstract void setHost(Restaurant2Host restaurant2Host);

	/**
	 * hack to establish connection to Restaurant2Cook agent.
	 */
	public abstract void setCook(Restaurant2Cook restaurant2Cook);

	/**
	 * hack to establish connection to Restaurant2Cashier agent.
	 */
	public abstract void setCashier(Restaurant2Cashier restaurant2Cashier);

	public abstract String getMaitreDName();

	public abstract String getName();

	/** 
	 * Messages 
	 */

	// from Restaurant2HostRole
	public abstract void BreakApproved();

	// from Restaurant2HostRole
	public abstract void BreakDenied();

	// from Restaurant2HostRole
	public abstract void PleaseSeatCustomer(Restaurant2Host h, Restaurant2Customer c, int tableNum);

	// from Restaurant2CustomerRole
	public abstract void ReadyToOrder(Restaurant2Customer c);

	// from Restaurant2CustomerRole
	public abstract void UnableToOrder(Restaurant2Customer c);

	// from Restaurant2CustomerRole
	public abstract void HereIsMyChoice(Restaurant2Customer c, String foodChoice);

	// from Restaurant2CookRole
	public abstract void OutOfFood(Restaurant2Customer c, String foodChoice);

	// from Restaurant2CookRole
	public abstract void OrderReady(Restaurant2Customer c);

	// from Restaurant2CustomerRole
	public abstract void DoneEating(Restaurant2Customer c);

	// from Restaurant2CashierRole
	public abstract void HereIsCheck(Check check);

	// from GUI
	public abstract void MsgAtDestination();

	// from GUI
	public abstract void MsgAtHome();

	// from GUI
	public abstract void MsgActionComplete();

	// from GUI
	public abstract void MsgWantBreak();

	// from GUI
	public abstract void MsgOffBreak();

	public abstract void setGui(Restaurant2WaiterGui gui);

	public abstract Restaurant2WaiterGui getGui();

}