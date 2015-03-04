package simCity.test.mock;

import java.util.List;

import simCity.Restaurant3.Restaurant3Check;
import simCity.Restaurant3.Restaurant3WaiterRole.MyCustomer;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;
import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.Restaurant3.interfaces.Restaurant3Host;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;

public class Restaurant3MockWaiter extends Mock implements Restaurant3Waiter{
	public EventLog log = new EventLog();
	public Restaurant3MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean wantToGoOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOKToGoOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCook(Restaurant3Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Restaurant3Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Restaurant3Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBegInventoryFilled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddToMenu(String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestockInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckIsReady(Restaurant3Check c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received CheckIsReady from cashier"));
	}

	@Override
	public void msgWantsToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBackToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOKToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNOTOKToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Restaurant3Customer c, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Restaurant3Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(Restaurant3Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForCheck(Restaurant3Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received ReadyForCheck from customer " + c));
	}

	@Override
	public void msgDonePayingAndLeaving(Restaurant3Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received DonePayingAndLeaving from customer " + c));
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtHost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTableNumber(Restaurant3Customer c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MyCustomer find(Restaurant3Customer c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGui(Restaurant3WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Restaurant3WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

}
