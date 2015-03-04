package simCity.restaurant4;

import simCity.restaurant4.Restaurant4WaiterRole.MyCustomer;
import simCity.restaurant4.Restaurant4WaiterRole.myCustomerState;
import simCity.restaurant4.interfaces.Restaurant4Waiter;

public class Restaurant4NormalWaiterRole extends Restaurant4WaiterRole implements Restaurant4Waiter {

	public Restaurant4NormalWaiterRole(String name) {
		super(name);
	}

	protected void giveCookOrder(MyCustomer c) {
		cook.msgCookOrder(this, c.getOrder(), c.table);
		c.state = myCustomerState.waitingForFoodToBeCooked;
		waiterGui.DoLeaveCustomer();
	}
	
}
