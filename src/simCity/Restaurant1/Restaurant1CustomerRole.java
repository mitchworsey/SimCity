package simCity.Restaurant1;

//import simCity.gui.CustomerGui;
import simCity.Role;
import simCity.gui.Restaurant1Panel;
import simCity.gui.Restaurant1.Restaurant1CustomerGui;
import simCity.interfaces.MarketCustomer;
import simCity.interfaces.Restaurant1Customer;
import simCity.interfaces.Restaurant1Host;
import simCity.interfaces.Restaurant1Waiter;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SimCity Restaurant1 customer agent.
 */
public class Restaurant1CustomerRole extends Role implements Restaurant1Customer {
	private String name;
	private int hungerLevel = 5;
	private double money = 16.00;
	private double amount = 0;
	Timer timer = new Timer();
	public Restaurant1CustomerGui customerGui;
	int tableNumber = 0;
	int position = 0;
	Order myOrder = new Order(this);

	// agent correspondents
	private Restaurant1Host host;
	private Restaurant1Waiter waiter;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Deciding, TalkingToWaiter, WaitingForFood, Eating, DoneEating, WaitingForReceipt, Leaving, ReadyForCheck};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, reOrderFood, followHost, seated, ready, orderFood, getFood, doneEating, getCheck, getReceipt, doneLeaving, requestMoney};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public Restaurant1CustomerRole(String name){
		super();
		this.name = name;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Restaurant1Host host) {
		this.host = host;
	}
	
	public void setWaiter(Restaurant1Waiter waiter) {
		this.waiter = waiter;
	}
	
	public Restaurant1Waiter getWaiter() {
		return waiter;
	}

	public String getCustomerName() {
		return name;
	}
	
	public double getAmount() {
		return money;
	}
	
	// Messages
	
	public void msgAddMoney(String mon) {
		double m = Double.parseDouble(mon);
		money += m;
	}
	
	public void gotHungry() {//from animation
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgSitAtTable(int table) {
		event = AgentEvent.followHost;
		state = AgentState.WaitingInRestaurant;
		tableNumber = table;
		stateChanged();
	}
	
	public void msgStandAt(int pos) {
		position = pos;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		event = AgentEvent.orderFood;
		stateChanged();
	}
	
	public void msgReOrder() {
		event = AgentEvent.reOrderFood;
		stateChanged();
	}
	
	public void msgHereIsYourFood() {
		event = AgentEvent.getFood;
		stateChanged();
	}

	public void msgHereIsYourCheck(double amt) {
		amount = amt;
		event = AgentEvent.getCheck;
		stateChanged();
	}
	
	public void msgRequestMoney(double amt) {
		event = AgentEvent.requestMoney;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry && position == 0){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry && position!= 0){
			//standAt(position);
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.requestMoney) {
			state = AgentState.DoingNothing;
			requestMoney();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown(tableNumber);
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Deciding;
			Decide();
			return true;
		}
		if (state == AgentState.Deciding && event == AgentEvent.ready){
			state = AgentState.TalkingToWaiter;
			CallWaiter();
			return true;
		}
		if (state == AgentState.TalkingToWaiter && event == AgentEvent.orderFood) {
			state = AgentState.WaitingForFood;
			OrderFood();
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.reOrderFood){
			ReOrderFood();
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.getFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.ReadyForCheck;
			getCheck();
			return true;
		}
		if (state == AgentState.ReadyForCheck && event == AgentEvent.getCheck){
			state = AgentState.Leaving;
			PayWaiter();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			EnterCity();
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}
	
	private void requestMoney() {
		if(money >= amount) {
			host.msgHereIsMoney(this);
			money -= amount;
			//customerGui.updateMoney(money);
			amount = 0;
			event = AgentEvent.followHost;
		}
		else {
			customerGui.DoExitRestaurant();
			event = AgentEvent.doneLeaving;
		}
	}
	
	private void SitDown(int table) {
		customerGui.DoGoToSeat(table);
	}
	
	private void Decide() {
		Random random = new Random();
		int randomInteger = random.nextInt(4);
		if(randomInteger == 0){
			timer.schedule(new TimerTask() {
				public void run() {
					myOrder.setPreperationTime("salad");
					event = AgentEvent.ready;
					stateChanged();
				}
			},
			5000);
		}
		else if(randomInteger == 1) {
			timer.schedule(new TimerTask() {
				public void run() {
					myOrder.setPreperationTime("chicken");
					event = AgentEvent.ready;
					stateChanged();
				}
			},
			5000);
		}
			
		else if (randomInteger == 2) {
			timer.schedule(new TimerTask() {
				public void run() {
					myOrder.setPreperationTime("pizza");
					event = AgentEvent.ready;
					stateChanged();
				}
			},
			5000);
		}
			
		else {
			timer.schedule(new TimerTask() {
				public void run() {
					myOrder.setPreperationTime("steak");
					event = AgentEvent.ready;
					stateChanged();
				}
			},
			5000);
		}
	}
	
	private void CallWaiter() {
		waiter.msgReadyToOrder(getCustomerName());
	}
	
	private void OrderFood() {
		//Decide();
		customerGui.DoShowFood(myOrder.getFood());
		customerGui.DoShowQuestion();
		waiter.msgHereIsMyChoice(getCustomerName(), myOrder.getFood());
	}

	private void ReOrderFood() {
		customerGui.DoShowFood(myOrder.getFood());
		customerGui.DoShowQuestion();
		waiter.msgHereIsMyChoice(getCustomerName(), myOrder.getFood());
	}
	
	private void EatFood() {
		customerGui.DoRemoveQuestion();
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		10000);
	}
	
	private void getCheck() {
		customerGui.DoRemoveFood();
		waiter.msgReadyForCheck(getCustomerName(), myOrder.getFood());
	}
	
	private void PayWaiter() {
		if(money >= amount){
			money -= amount;
			waiter.msgCustomerPaid(getCustomerName());
		}
		else {
			waiter.msgCustomerHasDebt(getCustomerName(), myOrder.getFood());
		}
		leaveTable();
	}

	private void leaveTable() {
		host.msgLeavingTable(this);
		waiter.msgLeavingTable(getCustomerName());
		customerGui.DoExitRestaurant();
	}
	
	private void EnterCity() {
		getPersonAgent().OutOfComponent(this);
		customerGui.gui.removeGui(customerGui);
	}
	

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(Restaurant1CustomerGui g) {
		customerGui = g;
	}

	public Restaurant1CustomerGui getGui() {
		return customerGui;
	}
	
	public class Order {
		Restaurant1Customer sendTo;
		String food = new String();
		int preperationTime;
		
		Order(Restaurant1CustomerRole cust) {
			sendTo = cust;
		}
		
		void setPreperationTime(String order) {
			food = order;
			if(order.equals("pizza")) {
				preperationTime = 3;
			}
			if(order.equals("steak")) {
				preperationTime = 10;
			}
			if(order.equals("salad")) {
				preperationTime = 7;
			}
			if(order.equals("chicken")) {
				preperationTime = 8;
			}
		}
		
		int getPreperationTime() {
			return preperationTime;
		}
		
		String getFood() {
			return food;
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
	}

}
