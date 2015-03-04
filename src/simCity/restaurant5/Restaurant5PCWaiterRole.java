package simCity.restaurant5;

import java.util.*;

import simCity.market.MarketGrocerRole;
import simCity.restaurant5.Restaurant5CookRole.*;
import simCity.restaurant5.interfaces.Restaurant5Waiter;
import simCity.restaurant5.Restaurant5CookRole.FoodState;
import simCity.restaurant5.Restaurant5WaiterRole.MyCustomer;
import simCity.restaurant5.Restaurant5CookRole.Order;

public class Restaurant5PCWaiterRole extends Restaurant5WaiterRole implements Restaurant5Waiter{
//	public static List<Order> orders;
	
	Restaurant5SharedDataWheel sharedDataWheel;
	MarketGrocerRole mg;
	
	Restaurant5CookRole tempCook = new Restaurant5CookRole("tempCook", sharedDataWheel, mg);
	
	public Restaurant5PCWaiterRole(String name) {
		super(name);
//		if (orders == null) {
//			orders = Collections.synchronizedList(new ArrayList<Order>());
//		}
	}
	
	public Restaurant5PCWaiterRole(String name, Restaurant5SharedDataWheel sdwheel) {
		super(name);
		this.sharedDataWheel = sdwheel;
	}
	
	public void setSharedDataWheel(Restaurant5SharedDataWheel wheel) {
		this.sharedDataWheel = wheel;
	}
	
	protected void GiveCookOrder(MyCustomer customer, String choice, int table) {
		print("Adding order to shared data with Cook");
		
		waiterGui.setFoodString(choice); // TEXT
		//waiterGui.DoGiveOrderToCook(choice); // animation call
		
		waiterGui.DoDeliverOrderToWheel();
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Order o = tempCook.new Order(this, choice, table, Restaurant5CookRole.FoodState.Pending);
		
		//orders.add(tempCook.new Order(this, choice, table, Restaurant5CookRole.FoodState.Pending));
		sharedDataWheel.insert(o);
		cook.msgHereIsOrder();
		waiterGui.setFoodString(" "); // TEXT
		customer.orderState = FoodState.Pending;
		waiterGui.DoGoHome();

	}
	
}
