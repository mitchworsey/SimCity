package simCity.restaurant4;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.restaurant4.Restaurant4WaiterRole.Menu;
import simCity.restaurant4.gui.Restaurant4CustomerGui;
import simCity.restaurant4.interfaces.*;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class Restaurant4CustomerRole extends Role implements Restaurant4Customer {
	private String name;
	private String order;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private Restaurant4CustomerGui customerGui;

	// agent correspondents
	private Restaurant4Host host;
	private Restaurant4Waiter waiter;
	private Restaurant4Cashier cashier;
	
	private double checkAmt = 0;
	//private double moneyInWallet = 0;
	private int waitingLocation = 0;
	private int table;
	
	private Semaphore allow = new Semaphore(0,true);

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, ReadingMenu, Ordering, Eating, DoneEating, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, debating, gotHungry, followHost, seated, ordered, reordered, served, readyToPay, doneEating, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public Restaurant4CustomerRole(String name){
		super();
		this.name = name;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Restaurant4Host host) {
		this.host = host;
	}
	
	public void setWaiter(Restaurant4Waiter waiter) {
		this.waiter = waiter;
	}
	
	public void setCashier(Restaurant4Cashier cashier) {
		this.cashier = cashier;
	}
	
	public Restaurant4Host getHost() {
		return host;
	}

	public String getCustomerName() {
		return name;
	}
	
	/*public void setWalletMoney(float amt) {
		this.moneyInWallet = amt;
	}*/
	
	public void setTable(int table) {
		this.table = table;
	}
	
	public int getTable() {
		return this.table;
	}
	// Messages

	public void gotHungry() {//from animation
		allow.release();
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgRestaurantIsFull(int location) {
		waitingLocation = location;
		event = AgentEvent.debating;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToWaitingArea() {
		
	}

	public void msgFollowMe(int table) {
		print("Received msgFollowMe");
		setTable(table);
		event = AgentEvent.followHost;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgWhatDoYouWant() {
		print("Received msgWhatDoYouWant");
		event = AgentEvent.ordered;
		stateChanged();
	}
	
	public void msgChooseNewOrder() {
		state = AgentState.ReadingMenu;
		event = AgentEvent.reordered;
		stateChanged();
	}
	
	public void msgHereIsYourFood() {
		event = AgentEvent.served;
		stateChanged();
	}
	
	public void msgHereIsYourCheck(double checkAmt) {
		print("Received msgHereIsYourCheck");
		allow.release();
		this.checkAmt += checkAmt;
	}
	
	public void msgPayNextTime(double amount) {
		this.checkAmt += amount;
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		allow.release();
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try {
			//	CustomerAgent is a finite state machine
			if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
				state = AgentState.WaitingInRestaurant;
				goToRestaurant();
				return true;
			}
			if(state == AgentState.WaitingInRestaurant && event == AgentEvent.debating) {
				makeADecision();
				return true;
			}
			if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
				state = AgentState.BeingSeated;
				SitDown(table);
				return true;
			}
			if (state == AgentState.BeingSeated && event == AgentEvent.seated){
				state = AgentState.ReadingMenu;
				ReadMenu();
				return true;
			}
			if (state == AgentState.ReadingMenu && (event == AgentEvent.ordered || event == AgentEvent.reordered)){
				state = AgentState.Ordering;
				GiveOrder(waiter);
			}
			if (state == AgentState.Ordering && event == AgentEvent.served){
				state = AgentState.Eating;
				EatFood();
				return true;
			}
			if (state == AgentState.Eating && event == AgentEvent.doneEating) {
				state = AgentState.Paying;
				leaveTable();
				return true;
			}
			if (state == AgentState.Paying && event == AgentEvent.readyToPay){
				state = AgentState.Leaving;
				PayForFood();
				return true;
			}
			if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
				state = AgentState.DoingNothing;
				//no action
				return true;
			}
		}
		catch (ConcurrentModificationException cce) {
			return true;
		}
		return false;
	}

	// Actions

	private void makeADecision() {
		Random random = new Random();
		boolean stay = random.nextBoolean();
		if(stay) {
			print("The restaurant is full, but I'm staying.");
			customerGui.DoGoToWaitingArea(waitingLocation);
		}
		else {
			print("The restaurant is too full. I'm leaving.");
			personAgent.Do("Leaving.");
			host.msgLeavingBeforeEating(this, waitingLocation);
			customerGui.DoExitRestaurant();
			state = AgentState.DoingNothing;
			
			customerGui.removeGui(customerGui);
			customerGui = null;
			personAgent.OutOfComponent(this);
			
			state = AgentState.DoingNothing;
		}
		event = AgentEvent.none;
	}
	
	private void goToRestaurant() {
		/*
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*if(getName().equalsIgnoreCase("Andy")) {
			moneyInWallet = 5;
		}
		
		else if(getName().equalsIgnoreCase("Cindy")) {
			moneyInWallet = 5.99;
		}
		
		else {
			Random money = new Random();
			double i = money.nextInt(25-2)+2;
			moneyInWallet = i;
		}*/
		
		if(personAgent.getMoney() < 5.99) {
			Random random = new Random();
			boolean stay = random.nextBoolean();
			if(stay) {
				print("I don't have enough money, but I'm staying.");
				personAgent.Do("Going to restaurant");
				host.msgIWantFood(this);//send our instance, so he can respond to us
			}
			else {
				print("I don't have enough money. I'm leaving.");
				personAgent.Do("Leaving.");
				customerGui.DoExitRestaurant();
				state = AgentState.DoingNothing;
				event = AgentEvent.none;
				
				customerGui.removeGui(customerGui);
				customerGui = null;
				personAgent.OutOfComponent(this);
				
				state = AgentState.DoingNothing;
				event = AgentEvent.none;
			}
		}
		else {
			host.msgIWantFood(this);
		}
	}

	private void SitDown(int table) {
		personAgent.Do("Being seated. Going to table");
		customerGui.DoGoToSeat(table);
		
		if(waitingLocation != 0) {
			host.msgWaitingSeatIsFree(waitingLocation);
		}
	}
	
	private void ReadMenu() {
		waiter.msgReadyToOrder(this);
	}

	private void GiveOrder(Restaurant4Waiter waiter) {
		// if customer is reordering (item is out of stock) and only has enough money for the cheapest item, leave 
		if(event == AgentEvent.reordered && personAgent.getMoney() >= 5.99 && personAgent.getMoney() < 8.99) {
			print("Leaving. The food I want is not available");
			customerGui.DoDisplayOrder("");
			waiter.msgDoneEatingAndLeaving(this);
			customerGui.DoExitRestaurant();
			
			try {
				allow.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			customerGui.removeGui(customerGui);
			customerGui = null;
			personAgent.OutOfComponent(this);
			
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
		}
		else {
			if(getName().equals("steak")) {
				order = "steak";
				customerGui.DoDisplayOrder("steak?");
				waiter.msgHereIsMyOrder(this, order);
			}
			else if(personAgent.getMoney() >= 5.99 && personAgent.getMoney() < 8.99) {
				order = "salad";
				customerGui.DoDisplayOrder("salad?");
				waiter.msgHereIsMyOrder(this, order);
			}
			else {
				Random orderNum = new Random();
				int i = orderNum.nextInt(Menu.menuMap.size())+1; 
				order = Menu.menuMap.get(i); //menu is intentionally made public
				customerGui.DoDisplayOrder(order+"?");
				waiter.msgHereIsMyOrder(this, order); //choose a random selection from the menu
			}
		}
	}
	
	private void EatFood() {
		personAgent.Do("Eating Food");
		customerGui.DoDisplayOrder(order);
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		10000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void leaveTable() {
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personAgent.Do("Leaving.");
		customerGui.DoDisplayOrder("");
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
		event = AgentEvent.readyToPay;
	}

	private void PayForFood() {
		try {
			allow.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(personAgent.getMoney() - checkAmt >= 0) {
			((OrdinaryPerson)personAgent).money -= checkAmt;
			print("Money left in wallet: "+personAgent.getMoney());
			cashier.msgCanAffordFood(this);
		}
		else {
			cashier.msgCannotAffordFood(this);
		}
		customerGui.removeGui(customerGui);
		customerGui = null;
		personAgent.OutOfComponent(this);
		
		state = AgentState.DoingNothing;
		event = AgentEvent.none;
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

	public void setGui(Restaurant4CustomerGui g) {
		customerGui = g;
	}

	public Restaurant4CustomerGui getGui() {
		return customerGui;
	}
}

