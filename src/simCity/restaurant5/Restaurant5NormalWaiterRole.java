package simCity.restaurant5;

import simCity.restaurant5.interfaces.Restaurant5Waiter;

public class Restaurant5NormalWaiterRole extends Restaurant5WaiterRole implements Restaurant5Waiter {
	public Restaurant5NormalWaiterRole(String name) {
		super(name);
	}
	
	protected void GiveCookOrder(MyCustomer customer, String choice, int table) {
		print("Going to give the cook the order: " + choice);
		atHome.tryAcquire();
		waiterGui.setFoodString(choice); // TEXT
		waiterGui.DoGiveOrderToCook(choice); // animation call
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.msgHereIsOrder(this, choice, table);
		waiterGui.setFoodString(" "); // TEXT
		customer.orderState = FoodState.Pending;
		waiterGui.DoGoHome();
	}
}
	
