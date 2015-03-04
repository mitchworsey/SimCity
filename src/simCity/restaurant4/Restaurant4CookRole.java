package simCity.restaurant4;

import simCity.Role;
import simCity.restaurant2.Restaurant2CookRole.Order;
import simCity.restaurant4.gui.Restaurant4CookGui;
import simCity.restaurant4.gui.Restaurant4WaiterGui;
import simCity.restaurant4.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant4 Cook Role
 */

public class Restaurant4CookRole extends Role implements Restaurant4Cook {
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	//private List<Restaurant4Market> markets = Collections.synchronizedList(new ArrayList<Restaurant4Market>());
	//private List<MyMarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MyMarketOrder>());
	private Map<String, Integer> cookingMap = new HashMap<String, Integer>(); //maps food to cooking time
	public static Map<String, Integer> inventoryMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Semaphore atLocation = new Semaphore(0, true);
	
	Timer timer = new Timer();
	
	private Semaphore allow = new Semaphore(0,true);
	
	private Restaurant4SharedDataWheel wheel;
	private boolean wheelEmpty = true;
	
	public enum cookState {nothing, checkingInventory, placedInitialOrder};
	cookState state;
	
	public enum orderState
	{pending, cooking, waitingForMarket, replenishing, ranOut, depleted, done};
	public enum marketOrderState
	{pending, orderPlaced, restocking, outOfStock, done};

	private String name;

	public Restaurant4CookGui cookGui = null;

	public Restaurant4CookRole(String name) {
		super();

		this.name = name;
		cookingMap.put("steak", 8);
		cookingMap.put("chicken", 6);
		cookingMap.put("salad", 2);
		cookingMap.put("pizza", 4);
		
		inventoryMap.put("steak", 0);
		inventoryMap.put("chicken", 2);
		inventoryMap.put("salad", 2);
		inventoryMap.put("pizza", 2);
	}

	public void setSharedDataWheel(Restaurant4SharedDataWheel wheel) {
		this.wheel = wheel;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getOrders() {
		return orders;
	}

	// Messages
	public void msgAtLocation() { //from animation
		atLocation.release();
		stateChanged();
	}
	
	/*public void addMarket(Restaurant4Market market) {
		markets.add(market);
	}*/
	
	public void msgCheckInventory() {
		state = cookState.checkingInventory;
		stateChanged();
	}
	
	public void msgCookOrder() {
		print("Received msgCookOrder - wheel has something on it");
		wheelEmpty = false;
		stateChanged();
	}

	public void msgCookOrder(Restaurant4Waiter waiter, String choice, int table) {
		print("Received msgCookOrder");
		orders.add(new Order(waiter, choice, table, orderState.pending));
		stateChanged();
	}
	
	public void msgCanFulfillOrder(String choice) {
		/*synchronized (marketOrders) {
			for (MyMarketOrder marketOrder : marketOrders) {
				if(marketOrder.choice == choice) {
					marketOrder.state = marketOrderState.restocking;
					stateChanged();
					break;
				}
			}
		}*/
	}

	public void msgCannotFulfillOrder(String choice) {
		/*synchronized (marketOrders) {
			for (MyMarketOrder marketOrder : marketOrders) {
				if(marketOrder.choice == choice) {
					marketOrder.state = marketOrderState.outOfStock;
					stateChanged();
					break;
				}
			}
		}*/
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/*if(state == cookState.checkingInventory) {
			placeInitialOrders();
			return true;
		}*/
		/*for (MyMarketOrder marketOrder : marketOrders) {
			if (marketOrder.state == marketOrderState.restocking) {
				restockKitchen(marketOrder);
				return true;
			}
		}
		for (MyMarketOrder marketOrder : marketOrders) {
			if (marketOrder.state == marketOrderState.outOfStock) {
				updateMarketState(marketOrder);
				return true;
			}
		}*/
		for (Order order : orders) { 
			if (order.state == orderState.pending) {
				cookFood(order);
				return true;
			}
		}
		if(!wheelEmpty) {
			clearWheel();
			return true;
		}
 		return false;
	}

	// Actions
	private void placeInitialOrders() {
		/*Random market = new Random(); 
		int i = market.nextInt(markets.size()); //random market 
		Random orderAmt = new Random(); 
		int amt = orderAmt.nextInt(5-1)+1; //random order amount
		synchronized (inventoryMap) {
			for(Map.Entry<String, Integer> entry : inventoryMap.entrySet()) {
				// if there are fewer than 2 units of the food left and the market has the food in stock
				if(entry.getValue() <= 2) { 
					//print("Placing initial orders for: "+entry.getKey());
					marketOrders.add(new MyMarketOrder(markets.get(i), entry.getKey(), amt, marketOrderState.pending));
				}
			}
		}
		markets.get(i).msgNeedMoreFood(marketOrders);
		state = cookState.placedInitialOrder;
		stateChanged();*/
	}
	
	private void cookFood(Order o) {
		print("Units left of " + o.choice + ": "+inventoryMap.get(o.choice));
		if(inventoryMap.get(o.choice) == 0) { 
			tellWaiterOutOfStock(o.choice);
			
			state = cookState.checkingInventory;
			stateChanged();
			return;
		}
		
		inventoryMap.put(o.choice,inventoryMap.get(o.choice)-1);

		print("Cooking order of "+o.choice);
		cookGui.DoCookFood(o.choice);
		o.state = orderState.cooking;
		//cannot pass instance of Order into TimerTask, so change its state 
		//and find the matching instance in the TimerTask
		//wait for cook to finish cooking before messaging waiter
		timer.schedule(new TimerTask() {
			public void run() { 
				synchronized (orders) {
					for (Order order : orders) {
						if(order.state == orderState.cooking) {
							order.waiter.msgOrderReady(order.table);
							cookGui.DoPlating(order.choice);
							orders.remove(order);
							stateChanged();
							break;
						}
					}
				}
			}
		},
		cookingMap.get(o.getChoice())*1000); //wait time depends on order
	}
	
	private void restockKitchen(MyMarketOrder mOrder) {
		/*print("Restocking kitchen with "+mOrder.amount+" units of "+mOrder.choice);
		inventoryMap.put(mOrder.choice,inventoryMap.get(mOrder.choice)+mOrder.amount);
		marketOrders.remove(mOrder);

		stateChanged();*/
	}
	
	private void updateMarketState(MyMarketOrder mOrder) {
		/*print("updating market with out of stock status");
		marketOrders.remove(mOrder);

		stateChanged();*/
	}
	
	private void tellWaiterOutOfStock(String choice) {
		if(inventoryMap.get(choice) == 0) {
			synchronized (orders) {
				for(Order order : orders) {
					if(order.choice == choice) {
						print("Telling waiter out of stock");
						order.waiter.msgOutOfFood(order.choice);
						orders.remove(order);
						stateChanged();
						break;
					}
				}
			}
		}
	}
	
	private void clearWheel() {
		cookGui.DoGetOrdersFromWheel();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.DoReturnToGrill();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		wheelEmpty = true;
		List<Order> wheelOrders = wheel.removeAll();
		for(Order o : wheelOrders) {
			orders.add(o);
		}
		stateChanged();
	}

	//utilities

	public void setGui(Restaurant4CookGui gui) {
		cookGui = gui;
	}

	public Restaurant4CookGui getGui() {
		return cookGui;
	}
	
	public class MyMarketOrder {
		Restaurant4Market market;
		String choice;
		int amount;
		marketOrderState state;
		
		MyMarketOrder(Restaurant4Market market, String choice, int amount, marketOrderState state) {
			this.market = market;
			this.choice = choice;
			this.amount = amount;
			this.state = state;
		}
	}

	public class Order {
		Restaurant4Waiter waiter;
		String choice;
		int table;
		int grill;
		orderState state;

		Order(Restaurant4Waiter w, String choice, int table, orderState state) {
			this.waiter = w;
			this.choice = choice;
			this.table = table;
			this.state = state;
		}
		
		public int getTableNum() {
			return table;
		}
		
		public String getChoice() {
			return choice;
		}
	}
}

