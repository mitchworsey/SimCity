package simCity.Restaurant3.interfaces;

import java.util.List;

import simCity.Restaurant3.Restaurant3Check;
import simCity.Restaurant3.Restaurant3WaiterRole.MyCustomer;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;

public interface Restaurant3Waiter {

	public abstract String getName();

	public abstract List getCustomers();

	public abstract boolean wantToGoOnBreak();

	public abstract boolean isOnBreak();

	public abstract boolean isOKToGoOnBreak();

	public abstract void setCook(Restaurant3Cook cook);

	public abstract void setHost(Restaurant3Host host);

	public abstract void setCashier(Restaurant3Cashier cashier);

	public abstract void msgBegInventoryFilled();

	public abstract void msgAddToMenu(String food);

	public abstract void msgRestockInventory();

	public abstract void msgCheckIsReady(Restaurant3Check c);

	public abstract void msgWantsToGoOnBreak();

	public abstract void msgBackToWork();

	public abstract void msgOKToGoOnBreak();

	public abstract void msgNOTOKToGoOnBreak();

	public abstract void msgSitAtTable(Restaurant3Customer c, int table);

	public abstract void msgReadyToOrder(Restaurant3Customer c);

	public abstract void msgHereIsMyChoice(Restaurant3Customer c, String choice);

	public abstract void msgOutOfFood(String choice, int table);

	public abstract void msgOrderIsReady(String choice, int table);

	public abstract void msgReadyForCheck(Restaurant3Customer c);

	public abstract void msgDonePayingAndLeaving(Restaurant3Customer c);

	public abstract void msgAtTable();

	public abstract void msgAtCook();

	public abstract void msgAtHost();

	public abstract void msgAtCashier();

	public abstract int getTableNumber(Restaurant3Customer c);

	public abstract MyCustomer find(Restaurant3Customer c);

	public abstract void setGui(Restaurant3WaiterGui gui);

	public abstract Restaurant3WaiterGui getGui();

}