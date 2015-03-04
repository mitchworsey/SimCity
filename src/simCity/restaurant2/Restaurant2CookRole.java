package simCity.restaurant2;

import agent.Agent;
import simCity.Role;
import simCity.restaurant2.gui.Restaurant2CookGui;
import simCity.restaurant2.interfaces.Restaurant2Cook;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Market;
import simCity.restaurant2.interfaces.Restaurant2Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Restaurant2Cook Agent
 */

public class Restaurant2CookRole extends Role implements Restaurant2Cook {
	
	// DATA
	private String name;
	private List<Order> orders = new ArrayList<Order>();
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	Restaurant2SharedDataWheel sharedDataWheel;
	boolean sharedDataWheelEmpty = true;
	
	public class Order {
		Restaurant2Waiter w;
		Restaurant2Customer c;
		public String foodChoice;
		int tableNum;
		OrderState state = OrderState.None;
		public Order(Restaurant2Waiter w, Restaurant2Customer c, String foodChoice, int tableNum, OrderState state) {
			this.w = w;
			this.c = c;
			this.foodChoice = foodChoice;
			this.tableNum = tableNum;
			this.state = state;
		}
		public Order(Restaurant2Waiter w, Restaurant2Customer c, String foodChoice, int tableNum) {
			this.w = w;
			this.c = c;
			this.foodChoice = foodChoice;
			this.tableNum = tableNum;
			state = OrderState.Pending;
		}
	}

	enum OrderState 
	{None, Pending, Cooking, DoneCooking, Plating, ReadyToServe, Served, Ignored}
	
	
	private List<Restaurant2Market> restaurant2Markets = new ArrayList<Restaurant2Market>();
	private int marketCounter = 0;
	
	Timer timer = new Timer();

	private Map<String, Food> foodMap = new HashMap<String, Food>();
	

	class Food {
		String foodName;
		int cookTime;
		int plateTime;
		int inventory;
		int low;
		int capacity;
		int reorder = 0;
		Food(String foodName, int cookTime, int plateTime, int inventory, int low, int capacity) {
			this.foodName = foodName;
			this.cookTime = cookTime;
			this.plateTime = plateTime;
			this.inventory = inventory;
			this.low = low;
			this.capacity = capacity;
		}
		FoodState state = FoodState.Initial;
	}
	
	enum FoodState {Initial, Low, Ordered, Reorder, Enough};
	
	public Restaurant2CookGui restaurant2CookGui = null;

	
	
	
	
	public Restaurant2CookRole(String name) {
		super();
		this.name = name;
		
		// populate foodMap with some food items for now
		foodMap.put("Steak", new Food("Steak", 7500, 4000, 500, 2, 5));
		foodMap.put("Chicken", new Food("Chicken", 11000, 3000, 500, 2, 7));
		foodMap.put("Salad", new Food("Salad", 5000, 5000, 500, 1, 4));
		foodMap.put("Pizza", new Food("Pizza", 8500, 8000, 500, 2, 6));
	}
	
	public Restaurant2CookRole(String name, Restaurant2SharedDataWheel sharedDataWheel) {
		super();
		this.name = name;
		this.sharedDataWheel = sharedDataWheel;
		// populate foodMap with some food items for now
		foodMap.put("Steak", new Food("Steak", 7500, 4000, 500, 2, 5));
		foodMap.put("Chicken", new Food("Chicken", 11000, 3000, 500, 2, 7));
		foodMap.put("Salad", new Food("Salad", 5000, 5000, 500, 1, 4));
		foodMap.put("Pizza", new Food("Pizza", 8500, 8000, 500, 2, 6));
	}
	
	public void setSharedDataWheel(Restaurant2SharedDataWheel sharedDataWheel) {
		this.sharedDataWheel = sharedDataWheel;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cook#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cook#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#addMarket(restaurant.MarketAgent)
	 */
	@Override
	public void addMarket(Restaurant2Market m) {
		restaurant2Markets.add(m);
	}

	
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#HereIsOrder(restaurant.WaiterAgent, restaurant.Customer, java.lang.String, int)
	 */
	
	// from Restaurant2WaiterRole
	public void HereIsOrder() {
		print("Received msg that sharedDataWheel is not empty");
		sharedDataWheelEmpty = false;
		stateChanged();
	}
	
	
	
	// from Restaurant2WaiterRole
	@Override
	public void HereIsOrder(Restaurant2Waiter w, Restaurant2Customer c, String foodChoice, int tableNum) {
		print("Received HereIsOrder msg from normal waiter");
		orders.add(new Order(w, c, foodChoice, tableNum, OrderState.Pending));
		stateChanged();
	}
	
	// from Restaurant2MarketRole
	/* (non-Javadoc)
	 * @see restaurant.Cook#NotEnoughFood(java.lang.String, int)
	 */
	@Override
	public void NotEnoughFood(String foodName, int amount) {
		print("Received NotEnoughFood msg");
		Food f = foodMap.get(foodName);
		f.reorder = amount;
		f.state = FoodState.Reorder;
		stateChanged();
	}
	
	// from Restaurant2MarketRole
	/* (non-Javadoc)
	 * @see restaurant.Cook#HereIsFood(java.lang.String, int)
	 */
	@Override
	public void HereIsFood(String foodName, int amount) {
		print("Received HereIsFood msg");
		Food f = foodMap.get(foodName);
		f.inventory = f.inventory + amount;
		if (f.inventory >= f.capacity) {
			f.state = FoodState.Enough;
		}
		else {
			f.state = FoodState.Low;
		}
	}

	// from makeOrder() timer timeout
	/* (non-Javadoc)
	 * @see restaurant.Cook#FoodDone(restaurant.CookAgent.Order)
	 */
	@Override
	public void FoodDone(Order o) {
		print("Received FoodDone msg");
		o.state = OrderState.DoneCooking;
		stateChanged();
	}
	
	
	// from plateOrder() timer timeout
	/* (non-Javadoc)
	 * @see restaurant.Cook#PlateDone(restaurant.CookAgent.Order)
	 */
	@Override
	public void PlateDone(Order o) {
		print("Received PlateDone msg");
		o.state = OrderState.ReadyToServe;
		stateChanged();
	}
	



	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try {
		
			// Safety check first to avoid null pointer exception that there is some order in orders
			if (orders.size() == 0) {
				// No orders in orders, nothing to do
				return false;
			}
			
			// initial check on all food inventory
			for( Map.Entry<String, Food> entry : foodMap.entrySet() ) {
				if (entry.getValue().state == FoodState.Initial) {
					if (entry.getValue().inventory <= entry.getValue().low) {
						requestFood(entry.getValue());
					}
					entry.getValue().state = FoodState.Enough;
				}
			}
						
		
			// check if any foods need ordering or reordering
			for( Map.Entry<String, Food> entry : foodMap.entrySet() ) {
				if ( (entry.getValue().state == FoodState.Low) || (entry.getValue().state == FoodState.Reorder) ) {
					requestFood(entry.getValue());
					return true;
				}
			}
			
			/*
			if (there exists o in orders such that o.state = readyToServe) {
				orderIsDone(o);
			}
			*/		
			for(Order o : orders) {
				if (o.state == OrderState.ReadyToServe) {
					orderIsDone(o);
					return true;
				}
			}
		
			/*
			if (there exists o in orders such that o.state = done) {
				plateOrder(o);
			}
			*/
			for (Order o : orders) {
				if (o.state == OrderState.DoneCooking) {
					plateOrder(o);
					return true;
				}
			}
		
			/*
			if (there exists o in orders such that o.state = pending) {
				makeOrder(o);
			}
			 */
			for (Order o : orders) {
				if (o.state == OrderState.Pending) {
					makeOrder(o);
					return true;
				}
			}
			
			if(!sharedDataWheelEmpty) {
				gatherOrders();
				return true;
			}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
		}
		catch (ConcurrentModificationException e) {
			return false;
		}
	}

	
	/**
	 *  Actions
	 */
	private void makeOrder(final Order o) {
		
		
		Food currentFood = foodMap.get(o.foodChoice);
		if (currentFood.inventory <= currentFood.low) {
			if (currentFood.state != FoodState.Ordered) {
				currentFood.state = FoodState.Low;
			}
		}
		
		if (currentFood.inventory == 0) {
			o.state = OrderState.Ignored;
			o.w.OutOfFood(o.c, o.foodChoice);
			return;
		}
		
		print("Started cooking " + o.foodChoice);
		currentFood.inventory--;

			
		restaurant2CookGui.DoCooking(o); 
		
		o.state = OrderState.Cooking;
		timer.schedule(new TimerTask() {
			public void run() {
				print("Finished cooking " + o.foodChoice);
				FoodDone(o);
			}
		},
		(foodMap.get(o.foodChoice).cookTime) );
	}

	private void plateOrder(final Order o) {
		print("Started plating " + o.foodChoice);
		
		restaurant2CookGui.DoPlating(o.foodChoice); 
		
		o.state = OrderState.Plating;
		timer.schedule(new TimerTask() {
			public void run() {
				print("Finished cooking " + o.foodChoice);
				PlateDone(o);
				restaurant2CookGui.DoServing(o);
			}
		},
		(foodMap.get(o.foodChoice).plateTime) );
	}

	private void orderIsDone(Order o) {
		print("Order Up");
		o.w.OrderReady(o.c);
		o.state = OrderState.Served;
	}
	
	private void requestFood(Food f) {
		print("Requesting " + f.foodName + " from restaurant2Market");
		if (f.reorder != 0) {
			if (marketCounter == 2*restaurant2Markets.size()) {
				return;
			}
			marketCounter++;
			restaurant2Markets.get( marketCounter % restaurant2Markets.size() ).INeedFood(this, f.foodName, f.reorder);
			f.reorder = 0;
			f.state = FoodState.Ordered;
		}
		else { // reorder == 0
			restaurant2Markets.get( marketCounter % restaurant2Markets.size() ).INeedFood(this, f.foodName, f.capacity - f.inventory);
			f.state = FoodState.Ordered;
		}
	}
	
	private void gatherOrders() {
		sharedDataWheelEmpty = true;
		List<Order> sharedData = sharedDataWheel.removeAll();
		for (Order o : sharedData) {
			orders.add(o);
		}
		stateChanged();
	}
	
	
	
	// utilities

	/* (non-Javadoc)
	 * @see restaurant.Cook#setGui(restaurant.gui.CookGui)
	 */
	@Override
	public void setGui(Restaurant2CookGui gui) {
		restaurant2CookGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cook#getGui()
	 */
	@Override
	public Restaurant2CookGui getGui() {
		return restaurant2CookGui;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#refillInventory()
	 */
	@Override
	public void refillInventory() {
		Food f;
			for( Map.Entry<String, Food> entry : foodMap.entrySet() ) {
				f = entry.getValue();
				f.inventory = f.inventory + 1;
			}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#depleteInventory()
	 */
	@Override
	public void depleteInventory() {
		Food f;
			for( Map.Entry<String, Food> entry : foodMap.entrySet() ) {
				f = entry.getValue();
				f.inventory = 0;
			}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#depleteInventoryInitially()
	 */
	@Override
	public void depleteInventoryInitially() {
		Food f;
			for( Map.Entry<String, Food> entry : foodMap.entrySet() ) {
				f = entry.getValue();
				f.inventory = f.low;
				f.state = FoodState.Initial;
			}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#resetMarketCounter()
	 */
	@Override
	public void resetMarketCounter() {
		marketCounter = 0;
	}

}

