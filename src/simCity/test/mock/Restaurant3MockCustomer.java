package simCity.test.mock;

import simCity.Restaurant3.Restaurant3Check;
import simCity.Restaurant3.Restaurant3WaiterRole.RestaurantMenu;
import simCity.Restaurant3.gui.Restaurant3CustomerGui;
import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.Restaurant3.interfaces.Restaurant3Host;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;

public class Restaurant3MockCustomer extends Mock implements Restaurant3Customer{
	public EventLog log = new EventLog();
	public Restaurant3Cashier cashier;
	
	public Restaurant3MockCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setHost(Restaurant3Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getCash() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCash(double cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getChoice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWaiter(Restaurant3Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Restaurant3Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isImpatient() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(RestaurantMenu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoYouWant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleaseReOrder(RestaurantMenu rm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourCheck(Restaurant3Check c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsYourCheck from waiter. Total = "+ c.bill));
	}

	@Override
	public void msgHereIsYourChange(double change) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtWaitingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(Restaurant3CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Restaurant3CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

}
