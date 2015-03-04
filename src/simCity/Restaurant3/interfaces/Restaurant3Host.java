package simCity.Restaurant3.interfaces;

import java.util.Collection;
import java.util.List;

import simCity.Restaurant3.gui.Restaurant3WaiterGui;

public interface Restaurant3Host {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getCustomers();

	public abstract Collection getTables();

	public abstract void setWaiter(Restaurant3Waiter w);

	// Messages

	public abstract void msgIWantFood(Restaurant3Customer cust);

	public abstract void msgLeavingImpatiently(Restaurant3Customer cust);

	public abstract void msgTableIsFree(Restaurant3Customer cust);

	public abstract void msgAskToGoOnBreak(Restaurant3Waiter w);

	public abstract void msgGoingOnBreak(Restaurant3Waiter w);

	public abstract void msgBackToWork(Restaurant3Waiter w);

	public abstract void msgAtTable();

	public abstract void readyToBeSeated();

	public abstract void setGui(Restaurant3WaiterGui gui);

	public abstract Restaurant3WaiterGui getGui();

}