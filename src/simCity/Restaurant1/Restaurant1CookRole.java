package simCity.Restaurant1;

import simCity.Role;
import java.util.*;
import java.util.concurrent.Semaphore;

import simCity.gui.Restaurant1.Restaurant1CookGui;
import simCity.interfaces.Restaurant1Cook;
import simCity.interfaces.Restaurant1Customer;
import simCity.interfaces.Restaurant1Waiter;
/**
 * Restaurant Cook Agent
 **/

public class Restaurant1CookRole extends Role implements Restaurant1Cook {

	private String name;
	private int salads = 30;
	private int steaks = 30;
	private int chickens = 30;
	private int pizzas = 30;
	private int clearPizza = 0;
	private int clearSteak = 0;
	private int clearChicken = 0;
	private int clearSalad = 0;
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Order> ordersMade = Collections.synchronizedList(new ArrayList<Order>());
	//public List<MarketAgent> markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
	Timer timer = new Timer();
	boolean busy = false;
	private Semaphore makingOrder = new Semaphore(0,true);

	public Restaurant1CookGui cookGui = null;
	
	// agent correspondents
	
	public Restaurant1CookRole(String name) {
		super();

		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addWaiters(Restaurant1Waiter wait) {
		wait.msgSetCook(this);
	}
	
	/*public void addMarkets(MarketAgent market) {
		market.msgSetCook(this);
		markets.add(market);
	}*/

	// Messages

	// From waiter
	public void hereIsAnOrder( Restaurant1Customer customer, String order, Restaurant1Waiter waiter) {
		Order nextOrder = new Order();
		nextOrder.setCustomer(customer);
		nextOrder.setWaiter(waiter);
		nextOrder.setPreperationTime(order);
		orders.add(nextOrder);
		ordersMade.add(nextOrder);
		stateChanged();
	}
	
	public void msgGotOrder( String order ){
		if (order.equals("pizza"))
			clearPizza += 1;
		else if (order.equals("steak"))
			clearSteak += 1;
		else if (order.equals("chicken"))
			clearChicken +=1;
		else
			clearSalad +=1;
		stateChanged();
	}
	//From market
	public void msgOrderFilled() {
		steaks += 5;
		salads += 5;
		pizzas += 5;
		chickens += 5;
		stateChanged();
	}
	
	public void msgOrderNotFilled(int sal, int piz, int stk, int chk) {
		pizzas += 5 - piz;
		salads += 5 - sal;
		pizzas += 5 - piz;
		chickens += 5 - chk;
		/*if (markets.get(0).getName() == "Primary Market"){
			markets.remove(markets.get(0));
		}
		else if (markets.get(0).getName() == "Small Market 1") {
			markets.remove(markets.get(0));
		}*/
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if (clearPizza != 0 || clearChicken != 0 || clearSteak != 0 || clearSalad != 0) {
			clearFood();
			return true;
		}
		if (!orders.isEmpty() && !busy) {
			MakeFood(orders.get(0));
			return true;
		}
		return false;
	}

	// Actions
	
	private void clearFood() {
		if (clearPizza > 0){
			clearPizza -= 1;
			cookGui.clearPizza();
		}
		else if (clearSteak > 0){
			clearSteak -= 1;
			cookGui.clearSteak();
		}
		else if (clearChicken > 0){
			clearChicken -= 1;
			cookGui.clearChicken();
		}
		else {
			clearSalad -= 1;
			cookGui.clearSalad();
		}
	}

	private void MakeFood(Order order) {
		busy = true;
		orders.remove(order);
		if(order.getFood().equals("pizza")) {
			pizzas -=1;
			cookGui.getPizza();
		}
		if(order.getFood().equals("steak")) {
			steaks -= 1;
			cookGui.getSteak();
		}
		if(order.getFood().equals("salad")) {
			salads -= 1;
			cookGui.getSalad();
		}
		if(order.getFood().equals("chicken")) {
			chickens -= 1;
			cookGui.getChicken();
		}
		if(pizzas <=1 || steaks <= 1 || salads <= 1 || chickens <= 1) {
			int piz = 0, sal = 0, stk = 0, chk = 0;
			if(pizzas < 1)
				piz = 5;
			if(salads < 1)
				sal = 5;
			if (steaks < 1)
				stk = 5;
			if(chickens < 1)
				chk = 5;
			//markets.get(0).msgRunningLow(piz, sal, stk, chk, this);
		}
		if(pizzas >=0 && steaks >= 0 && salads >= 0 && chickens >= 0) {
			timer.schedule(new TimerTask() {
				public void run() {
					OrderReady();
				}
			},
			order.getPreperationTime()*1000);
		}
		else {
			(order.getWaiter()).msgNotEnoughFood(order.getSendTo());
		}
	}
	
	private void OrderReady(){
		(ordersMade.get(0).getWaiter()).msgOrderIsReady(ordersMade.get(0).getSendTo(), ordersMade.get(0).getFood());
		busy = false;
		ordersMade.remove(ordersMade.get(0));
		stateChanged();
	}

	//utilities

	public void setGui(Restaurant1CookGui gui) {
		cookGui = gui;
	}

	public Restaurant1CookGui getGui() {
		return cookGui;
	}

	public class Order {
		Restaurant1Customer sendTo;
		Restaurant1Waiter waiter;
		String food= new String();
		int preperationTime;
		
		Order() {
		}
		
		void setPreperationTime(String order) {
			food = order;
			if(order.equals("pizza")) {
				preperationTime = 7;
			}
			if(order.equals("steak")) {
				preperationTime = 10;
			}
			if(order.equals("salad")) {
				preperationTime = 4;
			}
			if(order.equals("chicken")) {
				preperationTime = 8;
			}
		}
		
		int getPreperationTime() {
			return preperationTime;
		}
		
		Restaurant1Customer getSendTo() {
			return sendTo;
		}
		
		Restaurant1Waiter getWaiter() {
			return waiter;
		}
		
		void setWaiter(Restaurant1Waiter wait) {
			waiter = wait;
		}
		
		void setCustomer(Restaurant1Customer customer) {
			sendTo = customer;
		}
		
		void setFood(String foo) {
			food = foo;
		}
		
		String getFood() {
			return food;
		}
	}
}