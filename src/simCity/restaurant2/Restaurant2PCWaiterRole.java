package simCity.restaurant2;

import java.util.*;

import simCity.restaurant2.Restaurant2CookRole.Order;
import simCity.restaurant2.Restaurant2CookRole;
import simCity.restaurant2.Restaurant2CookRole.OrderState;
import simCity.restaurant2.Restaurant2WaiterRole.CustomerState;
import simCity.restaurant2.interfaces.Restaurant2Waiter;


public class Restaurant2PCWaiterRole extends Restaurant2WaiterRole implements Restaurant2Waiter {

	Restaurant2SharedDataWheel sharedDataWheel;
	
	Restaurant2CookRole dummyCook = new Restaurant2CookRole("dummy");
	
	public Restaurant2PCWaiterRole(String name) {
		super(name); 
	}

	public Restaurant2PCWaiterRole(String name, Restaurant2SharedDataWheel sharedDataWheel) {
		super(name); 
		this.sharedDataWheel = sharedDataWheel;
	}
	
	public void setSharedDataWheel(Restaurant2SharedDataWheel sharedDataWheel) {
		this.sharedDataWheel = sharedDataWheel;
	}
	
	@Override
	protected void deliverOrder(MyCustomer mc) {
		print("Adding orders to shared data");
		
		atHome.tryAcquire(); // in case waiter was sitting idly at home, take permit as you leave "home" so waiter cannot seat customer from afar
		
		restaurant2WaiterGui.DoDeliverOrderToWheel();
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Order o = dummyCook.new Order(this, mc.c, mc.foodChoice, mc.tableNum, OrderState.Pending);
		sharedDataWheel.insert(o);
		restaurant2Cook.HereIsOrder();
		mc.state = CustomerState.OrderDelivered;
		restaurant2WaiterGui.DoGoHome();
	}

}
