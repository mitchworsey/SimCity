package simCity.restaurant4;

import simCity.restaurant4.Restaurant4CookRole.Order;
import simCity.restaurant4.Restaurant4CookRole.orderState;
import simCity.restaurant4.Restaurant4WaiterRole.MyCustomer;
import simCity.restaurant4.interfaces.Restaurant4Customer;
import simCity.restaurant4.interfaces.Restaurant4Waiter;

public class Restaurant4PCWaiterRole extends Restaurant4NormalWaiterRole implements Restaurant4Waiter {

	private Restaurant4SharedDataWheel wheel;
	
	public Restaurant4PCWaiterRole(String name) {
		super(name);
	}
	
	public void setSharedDataWheel(Restaurant4SharedDataWheel wheel) {
		this.wheel = wheel;
	}
	
	protected void giveCookOrder(MyCustomer c) {
		waiterGui.DoPlaceOrderOnWheel();
		
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Order o = ((Restaurant4CookRole) cook).new Order(this, c.choice, c.table, orderState.pending);
		wheel.insert(o);
		
		cook.msgCookOrder();
		c.state = myCustomerState.waitingForFoodToBeCooked;
		waiterGui.DoLeaveCustomer();
	}
	
}
