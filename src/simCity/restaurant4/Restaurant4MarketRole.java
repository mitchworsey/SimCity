package simCity.restaurant4;

import simCity.Role;
import simCity.restaurant4.Restaurant4CookRole.MyMarketOrder;
import simCity.restaurant4.gui.Restaurant4WaiterGui;
import simCity.restaurant4.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cook Agent
 */

public class Restaurant4MarketRole extends Role implements Restaurant4Market {
	public List<Order> orders = new ArrayList<Order>();
	public Map<String, Integer> inventoryMap = new HashMap<String, Integer>();
	public enum orderState
	{pending, waitingForPayment};
	
	Timer timer = new Timer();
	
	private Semaphore allow = new Semaphore(0,true);
	
	private Map<String, Double> priceMap = new HashMap<String, Double>();

	private String name;
	
	// agent correspondents
	private Restaurant4Cook cook;
	private Restaurant4Cashier cashier;
	
	public Restaurant4MarketRole(String name) {
		super();

		this.name = name;
		
		inventoryMap.put("steak", 5);
		inventoryMap.put("chicken", 5);
		inventoryMap.put("salad", 5);
		inventoryMap.put("pizza", 5);
		
		priceMap.put("steak", 9.99);
		priceMap.put("chicken", 7.99);
		priceMap.put("salad", 2.99);
		priceMap.put("pizza", 4.99);
	}
	
	public void setCook(Restaurant4Cook cook) {
		this.cook = cook;
	}
	
	public void setCashier(Restaurant4Cashier cashier) {
		this.cashier = cashier;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages

	public void msgNeedMoreFood(List<MyMarketOrder>foods) {
		//print("Received msgNeedMoreFood");
		for(MyMarketOrder food : foods) {
			orders.add(new Order(food.choice, food.amount, orderState.pending));
		}
		stateChanged();
	}
	
	public void msgHereIsPayment(double amount) {
		print(amount + " received from cashier");
	}
	
	public void msgCannotPayBill(double amount) {
		print("Cashier could not pay bill of amount " + amount);
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try {
			for (Order order : orders) { 
				if (order.state == orderState.pending) {
					checkInventoryStatus(order);
					return true;
				}
			}
			for (Order order : orders) { 
				if (order.state == orderState.waitingForPayment) {
					sendBillToCashier();
					return true;
				}
			}
		}
		catch (ConcurrentModificationException cce) {
			return true;
		}
		return false;
	}

	// Actions
	private void checkInventoryStatus(Order o) {
		if(inventoryMap.get(o.choice) >= o.amount) {
			//print("Can fulfill order for "+o.choice);
			cook.msgCanFulfillOrder(o.choice);
			inventoryMap.put(o.choice,inventoryMap.get(o.choice)-o.amount);
			o.state = orderState.waitingForPayment;
			stateChanged();
		}
		else {
			print("Out of stock for "+o.choice);
			cook.msgCannotFulfillOrder(o.choice);
			orders.remove(o);
		}
	}
	
	private void sendBillToCashier() {
		double amount = 0;
		for (Order order : orders) {
			if(order.state == orderState.waitingForPayment) {
				amount += (priceMap.get(order.choice)*order.amount);
			}
		}
		orders.clear();
		cashier.msgHereIsYourBill(this, amount);
	}
	
	private static class Order {
		String choice;
		int amount;
		orderState state;

		Order(String choice, int amount, orderState state) {
			this.choice = choice;
			this.amount = amount;
			this.state = state;
		}
	}
	
}

