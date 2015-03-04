package simCity.restaurant2.interfaces;

import java.util.Collection;
import java.util.List;

import simCity.restaurant2.gui.Restaurant2HostGui;

public interface Restaurant2Host {

	public abstract void addWaiter(Restaurant2Waiter w);

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getMyCustomers();

	public abstract List getWaiters();

	public abstract Collection getTables();

	/**
	 *  Messages
	 */

	// from Restaurant2WaiterRole
	public abstract void WantBreak(Restaurant2Waiter w);

	// from Restaurant2CustomerRole
	public abstract void IWantFood(Restaurant2Customer c);

	// from Restaurant2CustomerRole
	public abstract void IAmLeaving(Restaurant2Customer c);

	// from Restaurant2WaiterRole
	public abstract void TableAvailable(Restaurant2Waiter w, int tableNum);

	// from Restaurant GUI
	public abstract void WaiterOffBreak(Restaurant2Waiter w);

	public abstract void setGui(Restaurant2HostGui gui);

	public abstract Restaurant2HostGui getGui();

}