package simCity.restaurant2;

import agent.Agent;


import simCity.Role;
import simCity.restaurant2.Restaurant2CashierRole.Check;
import simCity.restaurant2.gui.Restaurant2WaiterGui;
import simCity.restaurant2.interfaces.Restaurant2Cashier;
import simCity.restaurant2.interfaces.Restaurant2Cook;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Host;
import simCity.restaurant2.interfaces.Restaurant2Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Restaurant2NormalWaiterRole extends Restaurant2WaiterRole implements Restaurant2Waiter {
	

	
	
	public Restaurant2NormalWaiterRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void deliverOrder(MyCustomer mc) {
		print("Delivering orders");
		
		atHome.tryAcquire(); // in case waiter was sitting idly at home, take permit as you leave "home" so waiter cannot seat customer from afar
		
		// Later drop off all current orders at once
		restaurant2WaiterGui.DoDeliverOrder();
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		restaurant2Cook.HereIsOrder(this, mc.c, mc.foodChoice, mc.tableNum);
		mc.state = CustomerState.OrderDelivered;
		restaurant2WaiterGui.DoGoHome();
	}
	
	
}
