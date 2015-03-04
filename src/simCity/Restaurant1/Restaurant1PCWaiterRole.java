package simCity.Restaurant1;

import simCity.interfaces.Restaurant1Waiter;

public class Restaurant1PCWaiterRole extends Restaurant1WaiterRole implements Restaurant1Waiter {

	private Restaurant1SharedDataWheel sharedDataWheel;
	
	Restaurant1CookRole dummyCook = new Restaurant1CookRole("dummy");
	
	public Restaurant1PCWaiterRole(String name) {
		super(name); 
	}

	public Restaurant1PCWaiterRole(String name, Restaurant1SharedDataWheel sharedDataWheel) {
		super(name); 
		this.sharedDataWheel = sharedDataWheel;
	}
	
	public void setSharedDataWheel(Restaurant1SharedDataWheel sharedDataWheel) {
		this.sharedDataWheel = sharedDataWheel;
	}
	
	protected void orderCook(Order order) {
		atCook = false;
		cook.hereIsAnOrder(order.getSendTo(), order.getFood(), this);
		orderToCook.remove(order);
		gotOrder = false;
	}

}
