package simCity.Restaurant1;

import agent.Agent;


import simCity.Role;
import simCity.interfaces.Restaurant1Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Restaurant1NormalWaiterRole extends Restaurant1WaiterRole implements Restaurant1Waiter {
	
	public Restaurant1NormalWaiterRole(String name) {
		super(name);
	}
	
	@Override
	protected void orderCook(Order order) {
		atCook = false;
		cook.hereIsAnOrder(order.getSendTo(), order.getFood(), this);
		orderToCook.remove(order);
		gotOrder = false;
	}
	
}