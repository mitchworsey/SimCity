package simCity.Restaurant3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.Restaurant3.gui.Restaurant3CookGui;
import simCity.Restaurant3.gui.Restaurant3WaiterGui;
import simCity.Restaurant3.interfaces.Restaurant3Cashier;
import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.Restaurant3.interfaces.Restaurant3Market;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;
import simCity.interfaces.Restaurant1Cook;
import simCity.restaurant2.Restaurant2SharedDataWheel;
import simCity.restaurant2.Restaurant2CookRole.Order;

public class Restaurant3CookRole extends Role implements Restaurant3Cook{
	
	public enum OrderState
	{pending, cooking, done};
	
	private Restaurant3Cashier cashier;
	
	private Restaurant3CookGui cookGui;
	
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atCookingArea = new Semaphore(0, true);
	private Semaphore atPlatingArea = new Semaphore(0,true);
	
	private String waiterName;
	
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Order> ordersToRemove = new ArrayList<Order>();
	public List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	public List<MarketOrder> marketOrdersToRemove = Collections.synchronizedList(new ArrayList<MarketOrder>());
	public List<MarketOrder> marketOrdersFulfilled = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public List<Restaurant3Market> markets = Collections.synchronizedList(new ArrayList<Restaurant3Market>());
	
	Timer t = new Timer();
	
	public Map<String, Food> foods = new HashMap<String, Food>();
	private int marketIndex = 0;
	
	private String name;
	
	private boolean justOpened = false;
	private boolean begInventoryFilled;

	public Restaurant3WaiterGui waiterGui = null;
	
	
	Restaurant3ProducerConsumerMonitor sharedDataWheel;
	boolean sharedDataWheelEmpty = true;
	
	public Restaurant3CookRole(String name) {
		super();
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#setWaiterName(java.lang.String)
	 */
	@Override
	public void setWaiterName(String name){
		waiterName = name;
		foods.put("Chicken", new Food("Chicken"));
		foods.put("Steak", new Food("Steak"));
		foods.put("Salad", new Food("Salad"));
		foods.put("Pizza", new Food("Pizza"));
	}
	
	@Override
	public void setGui(Restaurant3CookGui cg){
		cookGui = cg;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#getOrders()
	 */
	@Override
	public List getOrders() {
		return orders;
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#setCashier(simCity.Restaurant3.Restaurant3Cashier)
	 */
	@Override
	public void setCashier(Restaurant3Cashier cashier){
		this.cashier = cashier;
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#getCashier()
	 */
	@Override
	public Restaurant3Cashier getCashier(){
		return cashier;
	}
	

	//STUB get foods??
	// Messages
	

	@Override
	public void msgRestockInventory(Restaurant3Waiter w){
		print("Received msgRestockInventory");
		marketOrders.add(new MarketOrder(w, "Chicken"));
		marketOrders.add(new MarketOrder(w, "Steak"));
		marketOrders.add(new MarketOrder(w, "Salad"));
		marketOrders.add(new MarketOrder(w, "Pizza"));
		justOpened = true;
		stateChanged();
	}


	@Override
	public void msgHereIsOrder(Restaurant3Waiter w, String choice, int table){
		print("Received msgHereIsOrder");
		orders.add(new Order(w, choice, table, OrderState.pending));
		cookGui.createFoodGui(choice + " ingredients", table);
		//foods.put(choice, new Food(choice));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#msgFoodDone(simCity.Restaurant3.Restaurant3CookRole.Order)
	 */
	@Override
	public void msgFoodDone(Order o){
		print("Received msgFoodDone");
		o.os = OrderState.done;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#msgOutOfStock(java.lang.String)
	 */
	@Override
	public void msgOutOfStock(String choice){
		print("Received msgOutOfStock from market");
		stateChanged();
		//markets.remove(m);//HACK THIS
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#msgOrderBeingProcessed(java.lang.String)
	 */
	@Override
	public void msgOrderBeingProcessed(String choice){
		print("Received msgOrderBeingProcessed");
		for(MarketOrder mo: marketOrders){
			if(mo.choice.equals(choice))
				mo.beingProcessed = true;
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#msgOrderDelivered(java.lang.String, int)
	 */
	@Override
	public void msgOrderDelivered(String choice, int numberFulfilled){
		print("Received msgOrderDelivered from market");
		foods.get(choice).stock += numberFulfilled;
		synchronized(marketOrders){
			for(MarketOrder mo: marketOrders){
				if(mo.choice.equals(choice)){
					mo.beingProcessed = false;
					marketOrdersFulfilled.add(mo);
					marketOrdersToRemove.add(mo);
				}
			}
		}
		
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#msgAtCookingArea()
	 */
	@Override
	public void msgAtCookingArea(){
		atCookingArea.release();
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#msgAtPlatingArea()
	 */
	@Override
	public void msgAtPlatingArea(){
		atPlatingArea.release();
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#msgAtFridge()
	 */
	@Override
	public void msgAtFridge(){
		atFridge.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(justOpened && !markets.isEmpty()){
			restockInventory();
			return true;
		}
		if(!marketOrdersFulfilled.isEmpty()){
			if(!begInventoryFilled)
				fillBegInventory();
			else
				updateMenu();
			return true;
		}
			
		if(!ordersToRemove.isEmpty()){
			for(Order o: ordersToRemove){
				orders.remove(o);
			}
			ordersToRemove.clear();
		}
		if(!marketOrdersToRemove.isEmpty()){
			synchronized(marketOrdersToRemove){
				for(MarketOrder mo: marketOrdersToRemove){
					marketOrders.remove(mo);
				}
			}
			marketOrdersToRemove.clear();
		}
		synchronized(orders){
			for(Order ord: orders){
				if(ord.os == OrderState.done){
					plateIt(ord);
					return true;
				}
			}
		}
		synchronized(orders){
			for(Order ord: orders){
				if(ord.os == OrderState.pending){
					if(foods.get(ord.choice).stock <= foods.get(ord.choice).low && foods.get(ord.choice).stock > 0){
						marketOrders.add(new MarketOrder(ord.w, ord.choice));
						for(int x = 0; x < markets.size(); x++){
							if(x == (marketIndex % markets.size())){
								cookIt(ord);
								for(MarketOrder mo: marketOrders){
									if(mo.choice == ord.choice && !mo.beingProcessed){
										orderFood(markets.get(x), mo.choice);
										marketIndex++;
									}
								}
								return true;
							}
						}
					}
					if(foods.get(ord.choice).stock == 0){
						outOfFood(ord);
						for(MarketOrder mo: marketOrders){
							if(mo.choice == ord.choice && !mo.beingProcessed){
								orderFood(markets.get(marketIndex % markets.size()), mo.choice);
								marketIndex++;
							}
						}
						return true;
					}
				}
			}
		}
		synchronized(orders){
			for(Order ord: orders){
				if(ord.os == OrderState.pending){
					cookIt(ord);
					return true;
				}
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

	// Actions
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#updateMenu()
	 */
	@Override
	public void updateMenu(){
		print("updating menu");
		for(MarketOrder mo: marketOrdersFulfilled){
			mo.w.msgAddToMenu(mo.choice);
			
		}
		marketOrdersFulfilled.clear();
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#fillBegInventory()
	 */
	@Override
	public void fillBegInventory(){
		print("filling beginning inventory");
		synchronized(marketOrdersFulfilled){
			for(MarketOrder mo: marketOrdersFulfilled){
				if(mo.choice.equals("Pizza")){
					mo.w.msgBegInventoryFilled();
					begInventoryFilled = true;
				}
			}
		}
		marketOrdersFulfilled.clear();
	}

	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#restockInventory()
	 */
	@Override
	public void restockInventory(){
		print("called restockInventory");
		int amountToOrderChicken = foods.get("Chicken").capacity - foods.get("Chicken").stock;
		int amountToOrderSteak = foods.get("Steak").capacity - foods.get("Steak").stock;
		int amountToOrderSalad = foods.get("Salad").capacity - foods.get("Salad").stock;
		int amountToOrderPizza = foods.get("Pizza").capacity - foods.get("Pizza").stock;
		
		markets.get(marketIndex % markets.size()).msgHereIsOrder(this, "Salad", amountToOrderSalad);
		marketIndex++;
		
		markets.get(marketIndex % markets.size()).msgHereIsOrder(this, "Chicken", amountToOrderChicken);
		marketIndex++;

		markets.get(marketIndex % markets.size()).msgHereIsOrder(this, "Steak", amountToOrderSteak);
		marketIndex++;

		markets.get(marketIndex % markets.size()).msgHereIsOrder(this, "Pizza", amountToOrderPizza);
		marketIndex++;

		justOpened = false;
		begInventoryFilled = false;
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#plateIt(simCity.Restaurant3.Restaurant3CookRole.Order)
	 */
	@Override
	public void plateIt(Order o){
		print("Called plateIt");
		cookGui.DoGoToCookingArea();
		try {
			atCookingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.DoWatch();
		/*cookGui.DoGoToPlatingArea();
		try {
			atPlatingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		o.w.msgOrderIsReady(o.choice, o.table);
		
		ordersToRemove.add(o);
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#cookIt(simCity.Restaurant3.Restaurant3CookRole.Order)
	 */
	@Override
	public void cookIt(final Order o){
		print("Called cookIt");
		cookGui.DoGoToFridge();
		cookGui.FoodGuiGoToFridge(o.choice + " ingredients", o.table);
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.foodAtFridge(o.choice + " ingredients", o.table);
		cookGui.DoGoToCookingArea();
		cookGui.FoodGuiGoToCookingArea(o.choice + " ingredients", o.table);
		try {
			atCookingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.DoWatch();
		o.os = OrderState.cooking;
		foods.get(o.choice).stock--;
		print(o.choice + " stock = " + foods.get(o.choice).stock);
		t.schedule(new TimerTask() {
			public void run() {
				msgFoodDone(o);
				print("Done cooking "+ o.choice);
				cookGui.removeFoodGui(o.choice + " ingredients", o.table);
			}
		},foods.get(o.choice).cookTime);
		
		//stateChanged();
	}
	
	@Override
	public void orderFood(Restaurant3Market m, String choice){
		print("called orderFood from Market");
		int amountToOrder = foods.get(choice).capacity - foods.get(choice).stock;
		List<String> nameOfOrders = new ArrayList<String>();
		for(MarketOrder mo: marketOrders){
			nameOfOrders.add(mo.choice);
		}
		//grocer.msgHereIsOrder(3, nameOfOrders);
		
		m.msgHereIsOrder(this, choice, amountToOrder);
		//outOfFood(o);
	}
	
	/* (non-Javadoc)
	 * @see simCity.Restaurant3.Restaurant3Cook#outOfFood(simCity.Restaurant3.Restaurant3CookRole.Order)
	 */
	@Override
	public void outOfFood(Order o){
		print("called outOfOrder");
		o.w.msgOutOfFood(o.choice, o.table);
		ordersToRemove.add(o);
	}
	
	private void gatherOrders() {
		sharedDataWheelEmpty = true;
		List<Order> sharedData = sharedDataWheel.removeAll();
		for (Order o : sharedData) {
			orders.add(o);
		}
		stateChanged();
	}
	
	/*
	public void doneCooking(Order o){
		o.os = OrderState.done;
		stateChanged();
	}*/

	@Override
	public void addMarket(Restaurant3Market m){
		markets.add(m);
	}

	private class MarketOrder{
		public Restaurant3Waiter w;
		public String choice;
		public boolean beingProcessed;
		MarketOrder(Restaurant3Waiter w, String choice){
			this.w = w;
			this.choice = choice;
			beingProcessed = false;
		}
	}
	
	public class Order{
		public Restaurant3Waiter w;
		public String choice;
		public int table;
		public OrderState os;
		
		Order(Restaurant3Waiter w, String choice, int table, OrderState os){
			this.w = w;
			this.choice = choice;
			this.table = table;
			this.os = os;
		}
		
	}
	private class Food{
		String type;
		int cookTime;
		int stock = 100;
		int low = 15;
		int capacity = 100;
		
		Food(String type){
			if(waiterName.equals("low beg inventory"))
				stock = 5;
			else if(waiterName.equals("poor cashier"))
				stock = 8;
			else if(waiterName.equals("high beg inventory")){
				stock = 1000;
				capacity = 1000;
			}
			else if(waiterName.equals("one low market")){
				stock = 3;
				capacity  =3;
			}
			this.type = type;
			if(type == "Chicken"){
				cookTime = 7000;
				if(waiterName.equals("no chicken")){
					stock = 1;
					low =0;
					capacity = 1;
				}
				else if (waiterName.equals("one order one market")){
					stock = 7;
				}
				else if(waiterName.equals("one order two markets")){
					stock = 7;
				}
			}
			else if(type == "Steak"){
				cookTime = 10000;
				if(waiterName.equals("one order two markets")){
					stock = 7;
				}
			}
			else if(type == "Salad"){
				cookTime = 3000;
				if(waiterName.equals("no salad")){
					stock = 0;
					capacity = 0;
				}
				else if(waiterName.equals("poor cashier")){
					stock = 0;
				}
			}
			else if(type == "Pizza"){
				cookTime = 12000;
			}
		}
	}
}
