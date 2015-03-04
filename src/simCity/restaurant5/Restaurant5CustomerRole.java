package simCity.restaurant5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.gui.*;
import simCity.interfaces.*;
import simCity.restaurant5.*;
import simCity.restaurant5.gui.*;
import simCity.restaurant5.interfaces.*;
import simCity.restaurant5.MarketOrder.OrderState;

public class Restaurant5CustomerRole extends Role implements Restaurant5Customer { 
	private String name;
	private int hungerLevel = 5; // determines length of meal

	private int moneyLevel = 0;
	private int debtLevel = 0;
	
	protected Semaphore atExit = new Semaphore(0, true);
	
	Timer timer = new Timer();
	private Restaurant5CustomerGui customerGui;
	private int tableNum;
	// agent correspondents
	private Restaurant5Host host;
	private Restaurant5Waiter waiter;
	private Restaurant5Cashier cashier;
	
	private String choice;
	private int bill;

	private List<String> CustomerMenu = Collections.synchronizedList(new ArrayList<String>());
	
	private int ChooseOrderTime = 5000;
	private int EatFoodTime = 4000;

	private boolean EnoughMoney = true; // should the customer pay or not?
	
	public enum AgentState {
		DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, GivingOrder, OrderedAndWaiting, Eating, DoneEating, 
			Paying, PayingForFood, ReadyToLeave, Leaving, ReadyForCheck
	};
	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {
		none, gotHungry, followWaiter, seated, WaitingToOrder, waiterToTakeOrder, waiterTakingOrder, WaiterBroughtFood, 
			doneEating, waitingForBill, Paying, doneLeaving, ReceivedCheck
	};

	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 * 
	 * @param name
	 *            name of the customer
	 * @param gui
	 *            reference to the customergui so the customer can send it
	 *            messages
	 */
	public Restaurant5CustomerRole(String name) {
		super();
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#setHost(simCity.restaurant5.Restaurant5HostRole)
	 */
	public void setHost(Restaurant5Host host) {
		this.host = host;
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#setWaiter(simCity.restaurant5.Restaurant5WaiterRole)
	 */
	@Override
	public void setWaiter(Restaurant5Waiter waiter) {
		this.waiter = waiter;
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#setCashier(simCity.restaurant5.interfaces.Restaurant5Cashier)
	 */
	@Override
	public void setCashier(Restaurant5Cashier cashier) {
		this.cashier = cashier;
	}	
	
	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#getCustomerName()
	 */
	@Override
	public String getCustomerName() {
		return name;
	}

	// Messages

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#gotHungry()
	 */
	@Override
	public void gotHungry() {// from animation
		print("Restaurant5:: I'm hungry.");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgNoFoodLeaveOrReorder()
	 */
	@Override
	public void msgNoFoodLeaveOrReorder() {
		print("Restaurant5:: No food. Reorder?");
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgFollowMe(int, java.util.List)
	 */
	@Override
	public void msgFollowMe(int table, List<String> menu) {
		print("Restaurant5:: Received msgSitAtTable");
		CustomerMenu = menu;
		print("Restaurant5:: menu size: " + Integer.toString(menu.size()));
		print("Restaurant5:: Customer Menu size: " + Integer.toString(CustomerMenu.size()));
		tableNum = table;
		event = AgentEvent.followWaiter;
		stateChanged();
	}


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgSitAtTable(java.util.List)
	 */
	@Override
	public void msgSitAtTable(List<String> menu) {
		CustomerMenu = menu;
		print("Restaurant5:: menu size: " + Integer.toString(menu.size()));
		print("Restaurant5:: Customer Menu size: " + Integer.toString(CustomerMenu.size()));
		event = AgentEvent.seated; // ***?
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgWhatDoYouWant()
	 */
	@Override
	public void msgWhatDoYouWant() {
		event = AgentEvent.waiterToTakeOrder;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgHereIsYourFood(java.lang.String)
	 */
	@Override
	public void msgHereIsYourFood(String order) {
		event = AgentEvent.WaiterBroughtFood;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgHereIsYourCheck(int)
	 */
	@Override
	public void msgHereIsYourCheck(int check) {
		bill = check;
		event = AgentEvent.ReceivedCheck;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgAnimationFinishedGoToSeat()
	 */
	@Override
	public void msgAnimationFinishedGoToSeat() {
		// from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#msgAnimationFinishedLeaveRestaurant()
	 */
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// from animation
		event = AgentEvent.doneLeaving;
		atExit.release();
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#randMoneyLevel()
	 */
	@Override
	public void randMoneyLevel() {
		Random generator = new Random();
		int randMoney = generator.nextInt(30);
		//moneyLevel = randMoney; ****************
		moneyLevel = 20;
	}
	

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#DoIHaveEnoughMoney()
	 */
	public boolean DoIHaveEnoughMoney() {
		randMoneyLevel();
		print("Restaurant5:: My money level: " + moneyLevel);
		Random generator = new Random();
		int randStay = generator.nextInt(2); // Randomization
		if (randStay == 0) {
			if (getMoneyLevel() < 9) {
				EnoughMoney = false;
			}
		} 
		return EnoughMoney;
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		// CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant
				&& event == AgentEvent.followWaiter) {
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			state = AgentState.Seated;
			if (DoIHaveEnoughMoney() == true) {
			// wait until you have chosen order
				ChooseOrder();
			}
			else {
				//leave restaurant
				state = AgentState.ReadyToLeave;
				event = AgentEvent.Paying;
			}
			return true;
		}

		if (state == AgentState.Seated && event == AgentEvent.WaitingToOrder) {
			state = AgentState.ReadyToOrder;
			IAmReadyToOrder(); // calling waiter
			// wait()
			return true;
		}

		if (state == AgentState.ReadyToOrder
				&& event == AgentEvent.waiterToTakeOrder) {
			state = AgentState.GivingOrder;
			HereIsMyOrder();
			return true;
		}

		if (state == AgentState.GivingOrder
				&& event == AgentEvent.waiterTakingOrder) {
			state = AgentState.OrderedAndWaiting;
			return true;
		}

		if (state == AgentState.OrderedAndWaiting
				&& event == AgentEvent.WaiterBroughtFood) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			print("I have eaten and I need to pay.");
			state = AgentState.ReadyForCheck;
			WhereIsMyCheck();
			//whereismycheck;
			return true;
		}
		//msgWhereIsMyCheck
		if (state == AgentState.ReadyForCheck && event == AgentEvent.ReceivedCheck) {
			print("I have recieved my check and now I will pay/leave.");
			state = AgentState.ReadyToLeave;
			PayForFood();
			return true;
		}
		
		if (state == AgentState.ReadyToLeave && event == AgentEvent.Paying) {
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			// no action
			return true;
		}
		
		return false;
	}

	// Actions

	private void goToRestaurant() {
		print("Restaurant5:: Going to restaurant");
		host.msgIWantFood(this);// send our instance, so he can respond to us
		print("After going to restaurant");
	}

	private void SitDown() {
		print("Restaurant5:: Being seated. Going to table");
		customerGui.DoGoToSeat(tableNum);// hack; only one table
	}

	private void ChooseOrder() {
		print("Restaurant5:: ~Choosing Order~");
		//print("menu size (before) timer task: " + Integer.toString(CustomerMenu.size())); // PRINT
		String name = this.name;
		switch (name) {
		case "Steak":
			choice = name;
			break;

		case "Salad":
			choice = name;
			break;

		case "Chicken":
			choice = name;
			break;

		case "Pizza":
			choice = name;
			break;

		default:
			Random generator = new Random();
			int randorder = generator.nextInt(4);
			choice = CustomerMenu.get(randorder); //String
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.WaitingToOrder;
				stateChanged();
			}
		}, ChooseOrderTime);

		//print("menu size (after) timer task: " + Integer.toString(CustomerMenu.size())); // PRINT
		//customerGui.cDrawFood(choice);
	}

	private void IAmReadyToOrder() {
		print("Restaurant5:: I know what I want to eat.");
		waiter.msgReadyToOrder(this);
	}

	private void HereIsMyOrder() {
		print("Restaurant5:: I would like to order: " + choice);
		waiter.msgHereIsMyOrder(choice, this);
		event = AgentEvent.waiterTakingOrder;
	}
	
	private void EatFood() {
		print("Restaurant5:: Eating Food");
		customerGui.setFoodString(choice); // TEXT
		// This next complicated line creates and starts a timer thread.
		// We schedule a deadline of getHungerLevel()*1000 milliseconds.
		// When that time elapses, it will call back to the run routine
		// located in the anonymous class created right there inline:
		// TimerTask is an interface that we implement right there inline.
		// Since Java does not all us to pass functions, only objects.
		// So, we use Java syntactic mechanism to create an
		// anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating.");
				//event = AgentEvent.waitingForBill;
				event = AgentEvent.doneEating;
				// isHungry = false;
				customerGui.setFoodString(" "); // TEXT
				stateChanged();
			}
		
		}, EatFoodTime);// getHungerLevel() * 1000);
		//waiter.msgWhereIsMyCheck(this);
	}
	
	private void WhereIsMyCheck() {
		print("Can I have my check please?");
		waiter.msgWhereIsMyCheck(this);
	}

	private void PayForFood() {
		print("Going to pay my check for the food.");
		if(getMoneyLevel() >= (bill + debtLevel)) {
			customerGui.DoGoToCashier();
			cashier.msgHereIsOrderPayment(this, bill);
			moneyLevel -= bill;
			print("Money level is now: " + moneyLevel);
		}
		else {
			cashier.msgIDoNotHaveEnoughMoney(this, bill);
			print("Sorry! I'll pay next time........");
			debtLevel = bill;
		}
		event = AgentEvent.Paying;
		
	}
	
	private void leaveTable() {
		print("Leaving.");
		waiter.msgLeavingTable(this); // this.waiter.msgLeavingTable(this);
		//host.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
		try { atExit.acquire(); } catch(InterruptedException e){ e.printStackTrace(); }

		getPersonAgent().OutOfComponent(this);
		customerGui.getPanel().removeGui(customerGui);

	}

	// Accessors, etc.
	
	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#getMoneyLevel()
	 */
	@Override
	public int getMoneyLevel() {
		return moneyLevel;
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#getHungerLevel()
	 */
	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#setHungerLevel(int)
	 */
	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		// could be a state change. Maybe you don't
		// need to eat until hunger lever is > 5?
	}


	/* (non-Javadoc)
	 * @see simCity.restaurant5.Restaurant5Customer#toString()
	 */
	@Override
	public String toString() {
		return "Customer " + getName();
	}


	@Override
	public void setGui(Restaurant5CustomerGui g) {
		customerGui = g;
	}

	@Override
	public Restaurant5CustomerGui getGui() {
		return customerGui;
	}

}
