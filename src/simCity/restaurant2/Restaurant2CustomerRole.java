package simCity.restaurant2;

import simCity.Role;
import simCity.restaurant2.Restaurant2CashierRole.Check;
import simCity.restaurant2.Restaurant2WaiterRole.Menu;
import simCity.restaurant2.Restaurant2WaiterRole.Menu.Item;
import simCity.restaurant2.gui.Restaurant2CustomerGui;
import simCity.restaurant2.interfaces.Restaurant2Cashier;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Host;
import simCity.restaurant2.interfaces.Restaurant2Waiter;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class Restaurant2CustomerRole extends Role implements Restaurant2Customer {
	
	private String name;
	private String foodChoice = "NONE";
	private Restaurant2CustomerGui restaurant2CustomerGui;	
	int hungerLevel = 5;
	private double money = 20.00;
	
	private Semaphore actionComplete = new Semaphore(0, true);
	private Menu menu;
	private Check check;
	private AgentPersonality personality = AgentPersonality.Normal;
	
	public enum AgentPersonality {Normal, Impatient, Scumbag, Picky, Poor};
	
	Random generator = new Random();
	
	Timer timer = new Timer();
		
	// agent correspondents
	private Restaurant2Host restaurant2Host;
	private Restaurant2Waiter restaurant2Waiter;
	private Restaurant2Cashier restaurant2Cashier;
	
	public enum AgentState 
	{DoingNothing, WaitingInRestaurant, BeingSeated, CheckingMenu,
		ReadyToOrder, Ordered, Eating, DoneEating, PaidCashier, Leaving};
	
	private AgentState state = AgentState.DoingNothing; //The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, pickedItem, ordering, foodArrived, doneEating, checkReceived, changeReceived, doneLeaving, cannotOrder, noFood, gotImpatient};
	
	private AgentEvent event = AgentEvent.none;

	
	/**
	 * Constructor for Restaurant2CustomerRole class
	 *
	 * @param name name of the customer
	 * @param gui reference to the customergui so the customer can send it messages
	 */
	
	public Restaurant2CustomerRole(String name){
		super();
		this.name = name;
	}

	
	/**
	 * hack to establish connection to Restaurant2Host agent.
	 */
	@Override
	public void setHost(Restaurant2Host restaurant2Host) {
		this.restaurant2Host = restaurant2Host;
	}
	
	/**
	 * hack to establish connection to Restaurant2Waiter agent.
	 */
	@Override
	public void setWaiter(Restaurant2Waiter restaurant2Waiter) {
		this.restaurant2Waiter = restaurant2Waiter;
	}
	
	/**
	 * hack to establish connection to Restaurant2Cashier agent.
	 */
	@Override
	public void setCashier(Restaurant2Cashier restaurant2Cashier) {
		this.restaurant2Cashier = restaurant2Cashier;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#getCustomerName()
	 */
	@Override
	public String getCustomerName() {
		return name;
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#GotHungry()
	 */

	// from GUI
	@Override
	public void GotHungry() { 
		print("Received gotHungry msg");
		event = AgentEvent.gotHungry;
		money = getPersonAgent().getMoney();
		stateChanged();
	}
	
	// from timer
	/* (non-Javadoc)
	 * @see restaurant.Customer#WaitTooLong()
	 */
	@Override
	public void WaitTooLong() {
		if (state == AgentState.WaitingInRestaurant) {
			print("This is taking too long!!!");
			event = AgentEvent.gotImpatient;
			stateChanged();
		}
	}
	
	// from Restaurant2WaiterRole
	@Override
	public void FollowMe(Restaurant2Waiter w, Menu m) {
		print("Received followMe msg");
		setWaiter(w);
		menu = m;
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	// from Restaurant2WaiterRole
	/* (non-Javadoc)
	 * @see restaurant.Customer#WhatIsChoice()
	 */
	@Override
	public void WhatIsChoice() {
		print("Received WhatIsChoice msg");
		event = AgentEvent.ordering;
		stateChanged();
	}

	// from Restaurant2WaiterRole
	/* (non-Javadoc)
	 * @see restaurant.Customer#HereIsFood(java.lang.String)
	 */
	@Override
	public void HereIsFood(String choice) {
		print("Received HereIsFood msg");
		event = AgentEvent.foodArrived;
		stateChanged();
	}
	
	// from Restaurant2WaiterRole
	/* (non-Javadoc)
	 * @see restaurant.Customer#PleaseReorder()
	 */
	@Override
	public void PleaseReorder() {
		print("Received PleaseReorder msg");
		
		if (personality == AgentPersonality.Picky) {
			state = AgentState.BeingSeated;
			event = AgentEvent.noFood;
		}
		
		else {
			state = AgentState.BeingSeated;
			event = AgentEvent.seated;
		}
		stateChanged();
	}
	
	// from Restaurant2WaiterRole
	/* (non-Javadoc)
	 * @see restaurant.Customer#HereIsCheck(restaurant.CashierAgent.Check)
	 */
	@Override
	public void HereIsCheck(Check check) {
		print("Received HereIsCheck msg");
		this.check = check;
		event = AgentEvent.checkReceived;
		stateChanged();
	}
	
	// from Restaurant2Cashier
	/* (non-Javadoc)
	 * @see restaurant.Customer#HereIsChange(double)
	 */
	@Override
	public void HereIsChange(double cash) {
		print("Received HereIsChange msg");
		money = money + cash;
		event = AgentEvent.changeReceived;
		stateChanged();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Customer#MsgAtSeat()
	 */
	@Override
	public void MsgAtSeat() {
		// print("Received MsgAtSeat msg");
		event = AgentEvent.seated;
		stateChanged();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Customer#MsgLeftRestaurant()
	 */
	@Override
	public void MsgLeftRestaurant() {
		// print("Received MsgLeftRestaurant msg");
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Customer#MsgReadyToOrder()
	 */
	@Override
	public void MsgReadyToOrder() {
		print ("Decided on choice");
		
		if (personality == AgentPersonality.Poor) {
			String oldFoodChoice = foodChoice;
			for ( Item item : menu.foods ) {
				if (item.price <= money) {
					foodChoice = item.food;
					event = AgentEvent.pickedItem;
				}
			}
			if (foodChoice.equals(oldFoodChoice)) { // no items are cheap enough to order, leave
				event = AgentEvent.cannotOrder;
			}
			stateChanged();
		}
		
		else {
			/* ordering hacks */
			if (name.equals("Steak")) {
				foodChoice = "Steak";
			}
			else if (name.equals("Chicken")) {
				foodChoice = "Chicken";
			}
			else if (name.equals("Salad")) {
				foodChoice = "Salad";
			}
			else if (name.equals("Pizza")) {
				foodChoice = "Pizza";
			}
			
			else {
			// SIMULATE RANDOM CHOICE OF FOOD ITEM
				String oldFoodChoice = foodChoice;
				while (oldFoodChoice.equals(foodChoice)) {
					int choice = generator.nextInt(4);
					switch (choice) {
						case 0: foodChoice = menu.foods.get(0).food; break;
						case 1: foodChoice = menu.foods.get(1).food; break;
						case 2: foodChoice = menu.foods.get(2).food; break;
						case 3: foodChoice = menu.foods.get(3).food; break;
					}
				}
			}
			
			event = AgentEvent.pickedItem;
			stateChanged();
		}
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Customer#MsgFinishedEating()
	 */
	@Override
	public void MsgFinishedEating() {
		print ("Done eating");
		event = AgentEvent.doneEating;
		stateChanged();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Customer#MsgActionComplete()
	 */
	@Override
	public void MsgActionComplete() {
		// print ("Received MsgActionComplete msg");
		actionComplete.release();
	}
	
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	Restaurant2CustomerRole is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ) {
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ) {
			sitDown();
			return true;
		}
		// NON-NORMATIVE SCENARIO, TABLES SATURATED, CUSTOMER IMPATIENT
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.gotImpatient ) {
			leaveNoTime();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			reviewMenu();
			return true;
		}
		// NON-NORMATIVE SCENARIO, OUT OF DESIRED FOOD CHOICE
		if (state == AgentState.BeingSeated && event == AgentEvent.noFood) {
			leaveNoFood();
			return true;
		}
		if (state == AgentState.CheckingMenu && event == AgentEvent.pickedItem) {
			callWaiter();
			return true;
		}
		// NON-NORMATIVE SCENARIO, UNABLE TO ORDER ANY FOOD
		if (state == AgentState.CheckingMenu && event == AgentEvent.cannotOrder) {
			leaveNoMoney();
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.ordering) {
			placeOrder();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.foodArrived) {
			eatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			restaurant2Waiter.DoneEating(this);
			state = AgentState.DoneEating;
			// no action
			return true;
		}
		if (state == AgentState.DoneEating && event == AgentEvent.checkReceived) {
			payCashier();
			return true;
		}
		if (state == AgentState.PaidCashier && event == AgentEvent.changeReceived) {
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			randomMoneyGenerator();
			getPersonAgent().OutOfComponent(this);
			restaurant2CustomerGui.restPanel.removeGui(restaurant2CustomerGui);
			return true;
		}
		
		return false;
	}

	
	/**
	 * Actions
	 */

	private void goToRestaurant() {
		print("Going to restaurant");
		
		restaurant2CustomerGui.DoGoToRestaurant();
		
		try {
			actionComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("Got in restaurant");
		
		state = AgentState.WaitingInRestaurant;
		restaurant2Host.IWantFood(this);//send our instance, so he can respond to us
		if (personality == AgentPersonality.Impatient) {
			timer.schedule(new TimerTask() {
				public void run() {
					WaitTooLong();
				}
			},
			10000);
		}
	}
	
	// NON-NORMATIVE, TAKING TOO LONG TO GET SEATED
	private void leaveNoTime() {
		restaurant2Host.IAmLeaving(this);
		print("This is taking too long!");
		state = AgentState.Leaving;
		restaurant2CustomerGui.DoNoTimeLeave();
	}

	private void sitDown() {
		print("Being seated. Going to table");
		state = AgentState.BeingSeated;
		restaurant2CustomerGui.DoGoToSeat();
	}

	private void reviewMenu() {
		print("Reviewing Menu");
		state = AgentState.CheckingMenu;
		restaurant2CustomerGui.DoReviewMenu();
		try {
			actionComplete.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void callWaiter() {
		print("Calling restaurant2Waiter");
		state = AgentState.ReadyToOrder;
		restaurant2Waiter.ReadyToOrder(this);
	}
	
	// NON-NORMATIVE UNABLE TO ORDER
	private void leaveNoMoney() {
		print("Unable to order, telling restaurant2Waiter");
		restaurant2Waiter.UnableToOrder(this);
		print("Leaving restaurant - No money");
		state = AgentState.Leaving;
		restaurant2CustomerGui.DoNoMoneyLeave();
	}
	
	// NON-NORMATIVE, OUT OF DESIRED FOOD
	private void leaveNoFood() {
		print("This place sucks! I'm outta here");
		restaurant2Waiter.UnableToOrder(this);
		print("Leaving restaurant - No food here");
		state = AgentState.Leaving;
		restaurant2CustomerGui.DoNoFoodLeave();
	}
	
	private void placeOrder() {
		print("Placing order");
		restaurant2Waiter.HereIsMyChoice(this, foodChoice);
		state = AgentState.Ordered;
		restaurant2CustomerGui.DoPlaceOrder(foodChoice);
		try {
			actionComplete.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void eatFood() {
		print("Eating food");
		state = AgentState.Eating;
		restaurant2CustomerGui.DoEatFood();
		try {
			actionComplete.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void payCashier() {
		print("Paying restaurant2Cashier");
		
		restaurant2CustomerGui.DoGoToCashier();
		
		try {
			actionComplete.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (check.price <= money) {
			print("Paid check in full");
			restaurant2Cashier.HereIsPayment(check, check.price);
			money = money - check.price;
			getPersonAgent().subtractMoney(check.price);
		}
		
		else { // check.price > money	
			print("Could not pay check in full, in debt");
			restaurant2Cashier.HereIsPayment(check, money);
			money = 0;
			getPersonAgent().setMoney(0);
		}
		
		state = AgentState.PaidCashier;
	}
	

	private void leaveRestaurant() {
		print("Leaving Restaurant");
		state = AgentState.Leaving;
		restaurant2CustomerGui.DoExitRestaurant();
	}
	
	private void randomMoneyGenerator() {
		print("Getting money");
		money = money + generator.nextInt(9) + 3;
	}
	
	

	// Accessors, utilities, etc.

	/* (non-Javadoc)
	 * @see restaurant.Customer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#getHungerLevel()
	 */
	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHungerLevel(int)
	 */
	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	@Override
	public String toString() {
		return "customer " + getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setGui(restaurant.gui.CustomerGui)
	 */
	@Override
	public void setGui(Restaurant2CustomerGui g) {
		restaurant2CustomerGui = g;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getGui()
	 */
	@Override
	public Restaurant2CustomerGui getGui() {
		return restaurant2CustomerGui;
	}
	
	
	// Non-normative testing utilities
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#depleteMoney()
	 */
	@Override
	public void depleteMoney() {
		money = 0;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#addMoney(double)
	 */
	@Override
	public void addMoney(double cash) {
		money = money + cash;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#setPersonality(restaurant.CustomerAgent.AgentPersonality)
	 */
	@Override
	public void setPersonality(AgentPersonality personality) {
		this.personality = personality;
	}
}

