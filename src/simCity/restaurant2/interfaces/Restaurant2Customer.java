package simCity.restaurant2.interfaces;

import simCity.restaurant2.Restaurant2CashierRole.Check;
import simCity.restaurant2.Restaurant2CustomerRole.AgentPersonality;
import simCity.restaurant2.Restaurant2WaiterRole.Menu;
import simCity.restaurant2.gui.Restaurant2CustomerGui;

public interface Restaurant2Customer {

	/**
	 * hack to establish connection to Restaurant2Host agent.
	 */
	public abstract void setHost(Restaurant2Host restaurant2Host);

	/**
	 * hack to establish connection to Restaurant2Waiter agent.
	 */
	public abstract void setWaiter(Restaurant2Waiter restaurant2Waiter);

	/**
	 * hack to establish connection to Restaurant2Cashier agent.
	 */
	public abstract void setCashier(Restaurant2Cashier restaurant2Cashier);

	public abstract String getCustomerName();

	/**
	 * Messages
	 */

	// from GUI
	public abstract void GotHungry();

	// from timer
	public abstract void WaitTooLong();

	// from Restaurant2WaiterRole
	public abstract void FollowMe(Restaurant2Waiter w, Menu m);

	// from Restaurant2WaiterRole
	public abstract void WhatIsChoice();

	// from Restaurant2WaiterRole
	public abstract void HereIsFood(String choice);

	// from Restaurant2WaiterRole
	public abstract void PleaseReorder();

	// from Restaurant2WaiterRole
	public abstract void HereIsCheck(Check check);

	// from Restaurant2Cashier
	public abstract void HereIsChange(double cash);

	// from GUI
	public abstract void MsgAtSeat();

	// from GUI
	public abstract void MsgLeftRestaurant();

	// from GUI
	public abstract void MsgReadyToOrder();

	// from GUI
	public abstract void MsgFinishedEating();

	// from GUI
	public abstract void MsgActionComplete();

	public abstract String getName();

	public abstract int getHungerLevel();

	public abstract void setHungerLevel(int hungerLevel);

	public abstract String toString();

	public abstract void setGui(Restaurant2CustomerGui g);

	public abstract Restaurant2CustomerGui getGui();

	public abstract void depleteMoney();

	public abstract void addMoney(double cash);

	public abstract void setPersonality(AgentPersonality personality);

}