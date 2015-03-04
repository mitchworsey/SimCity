package simCity.restaurant2;

import agent.Agent;



import simCity.Role;
import simCity.restaurant2.Restaurant2CashierRole.Check;
import simCity.restaurant2.gui.Restaurant2WaiterGui;
import simCity.restaurant2.interfaces.Restaurant2Cashier;
import simCity.restaurant2.interfaces.Restaurant2Cook;
import simCity.restaurant2.interfaces.Restaurant2Customer;
import simCity.restaurant2.interfaces.Restaurant2Host;
import simCity.restaurant2.interfaces.Restaurant2Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;


/**
 * Restaurant Restaurant2Host Agent
 */

public abstract class Restaurant2WaiterRole extends Role implements Restaurant2Waiter {
	
	protected String name;
	protected Restaurant2WaiterGui restaurant2WaiterGui = null;
	Timer timer = new Timer();
	
	protected Semaphore atDestination = new Semaphore(0, true);
	protected Semaphore actionComplete = new Semaphore(0, true);
	protected Semaphore atHome = new Semaphore(0, true); // atHome begins with 0 permits because waiter starts from "home" position so he will get 1 permit for free
	
	protected WaiterState state = WaiterState.Working;
	
	protected enum WaiterState {Working, WantBreak, BreakRequested, BreakApproved, BreakDenied, OnBreak};
	
	protected List<MyCheck> myChecks = new ArrayList<MyCheck>();
	
	protected class MyCheck {
		Check check;
		CheckState state;
		MyCheck( Check check, CheckState state ) {
			this.check = check;
			this.state = state;
		}
	}
	
	protected enum CheckState {Produced, Received, Given};
	
	
	public class Menu {
		class Item {
			String food;
			double price;
			Item (String food, double price) {
				this.food = food;
				this.price = price;
			}
		}
		List<Item> foods = new ArrayList<Item>();
		void addItem(String food, double price) {
			foods.add(new Item(food, price));
		}
	}
	
	public Menu menu = new Menu();
	
	protected Restaurant2Host restaurant2Host;
	protected Restaurant2Cook restaurant2Cook;
	protected Restaurant2Cashier restaurant2Cashier;
	protected List<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	
	class MyCustomer {
		Restaurant2Customer c;
		int tableNum;
		CustomerState state;
		String foodChoice;
		MyCustomer(Restaurant2Customer c, int tableNum, CustomerState state) {
			this.c = c;
			this.tableNum = tableNum;
			this.state = state;
		}
	}	

	
	public enum CustomerState
	{Waiting, Seated, AskedToOrder, Ordered, OrderDelivered, FoodReady, FoodPickedUp, 
		Eating, DoneEating, LeftRestaurant, ReorderRequest, ReorderRequested};
	
	
	public Restaurant2WaiterRole(String name) {
		super();

		this.name = name;
		// populate menu with items
		menu.addItem("Steak", 15.99);
		menu.addItem("Chicken", 10.99);
		menu.addItem("Salad", 5.99);
		menu.addItem("Pizza", 8.99);
	}
	
	/**
	 * hack to establish connection to Restaurant2Host agent.
	 */
	@Override
	public void setHost(Restaurant2Host restaurant2Host) {
		this.restaurant2Host = restaurant2Host;
	}
	
	/**
	 * hack to establish connection to Restaurant2Cook agent.
	 */
	@Override
	public void setCook(Restaurant2Cook restaurant2Cook) {
		this.restaurant2Cook = restaurant2Cook;
	}
	
	/**
	 * hack to establish connection to Restaurant2Cashier agent.
	 */
	@Override
	public void setCashier(Restaurant2Cashier restaurant2Cashier) {
		this.restaurant2Cashier = restaurant2Cashier;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	protected MyCustomer findMyCustomer(Restaurant2Customer c) {
		// iterate through list to find corresponding Restaurant2CustomerRole to c
		for (int i = 0; i < myCustomers.size(); i++) {
			if (c == myCustomers.get(i).c) {
				return (myCustomers.get(i));
			}
		}
		// this should never happen, but if no customer found return null for the MyCustomer pointer
		return null;
	}


	/* (non-Javadoc)
	 * @see restaurant.Waiter#BreakApproved()
	 */

	// from Restaurant2HostRole
	@Override
	public void BreakApproved() {
		print("Received BreakApproved msg");
		state = WaiterState.BreakApproved;
		stateChanged();
	}
	
	// from Restaurant2HostRole
	/* (non-Javadoc)
	 * @see restaurant.Waiter#BreakDenied()
	 */
	@Override
	public void BreakDenied() {
		print("Received BreakDenied msg");
		state = WaiterState.BreakDenied;
		stateChanged();
	}
	
	// from Restaurant2HostRole
	@Override
	public void PleaseSeatCustomer(Restaurant2Host h, Restaurant2Customer c, int tableNum) {
		print("Received PleaseSeatCustomer msg");
		setHost(h);
		myCustomers.add(new MyCustomer(c, tableNum, CustomerState.Waiting));
		stateChanged();
	}

	// from Restaurant2CustomerRole
	@Override
	public void ReadyToOrder(Restaurant2Customer c) {
		print("Received ReadyToOrder msg");
		MyCustomer mc = findMyCustomer(c);
		mc.state = CustomerState.AskedToOrder;
		stateChanged();
	}
	
	// from Restaurant2CustomerRole
	@Override
	public void UnableToOrder(Restaurant2Customer c) {
		print("Received UnableToOrder msg");
		MyCustomer mc = findMyCustomer(c);
		mc.state = CustomerState.DoneEating;
		stateChanged();
	}

	// from Restaurant2CustomerRole
	@Override
	public void HereIsMyChoice(Restaurant2Customer c, String foodChoice) {
		print("Received HereIsMyChoice msg");
		MyCustomer mc = findMyCustomer(c);
		mc.state = CustomerState.Ordered;
		mc.foodChoice = foodChoice;
		stateChanged();
	}

	// from Restaurant2CookRole
	@Override
	public void OutOfFood(Restaurant2Customer c, String foodChoice) {
		print("Received OutOfFood msg");
		MyCustomer mc = findMyCustomer(c);
		mc.state = CustomerState.ReorderRequest;
		stateChanged();
	}
	
	// from Restaurant2CookRole
	@Override
	public void OrderReady(Restaurant2Customer c) {
		print("Received OrderReady msg");
		MyCustomer mc = findMyCustomer(c);
		mc.state = CustomerState.FoodReady;
		stateChanged();
	}

	// from Restaurant2CustomerRole
	@Override
	public void DoneEating(Restaurant2Customer c) {
		print("Received DoneEating msg");
		MyCustomer mc = findMyCustomer(c);
		mc.state = CustomerState.DoneEating;
		stateChanged();
	}
	
	// from Restaurant2CashierRole
	/* (non-Javadoc)
	 * @see restaurant.Waiter#HereIsCheck(restaurant.CashierAgent.Check)
	 */
	@Override
	public void HereIsCheck(Check check) {
		print("Received HereIsCheck msg");
		myChecks.add(new MyCheck(check, CheckState.Produced));
		stateChanged();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Waiter#MsgAtDestination()
	 */
	@Override
	public void MsgAtDestination() {
		// print("Received MsgAtDestination msg");
		atDestination.release();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Waiter#MsgAtHome()
	 */
	@Override
	public void MsgAtHome() {
		// print("Received MsgAtHome msg");
		atHome.release();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Waiter#MsgActionComplete()
	 */
	@Override
	public void MsgActionComplete() {
		// print("Received MsgActionComplete msg");
		actionComplete.release();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Waiter#MsgWantBreak()
	 */
	@Override
	public void MsgWantBreak() {
		// print("Received MsgWantBreak msg");
		state = WaiterState.WantBreak;
		stateChanged();
	}
	
	// from GUI
	/* (non-Javadoc)
	 * @see restaurant.Waiter#MsgOffBreak()
	 */
	@Override
	public void MsgOffBreak() {
		// print("Received MsgOffBreak msg");
		state = WaiterState.Working;
		stateChanged();
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		try {
		
			// Safety check to make sure there at least is one MyCustomer on the list
			if (myCustomers.size() == 0) {
				// Check if waiter wants break
				if (state == WaiterState.WantBreak) {
					requestBreak();
					return true;
				}
				
				// Check if waiter was denied break
				if (state == WaiterState.BreakDenied) {
					state = WaiterState.Working;
					restaurant2WaiterGui.enableBreak();
					return true;
				}
				
				// Check if waiter is on break
				if (state == WaiterState.BreakApproved) {
					print("Going on break");
					state = WaiterState.OnBreak;
					restaurant2WaiterGui.toggleBreak();
					restaurant2WaiterGui.enableBreak();
					atHome.tryAcquire();
					restaurant2WaiterGui.DoGoOnBreak();
					return false;
				}	
						
			}
			
				
			else {
				
				// Check if waiter wants break
				if (state == WaiterState.WantBreak) {
					requestBreak();
					return true;
				}
							
				// Check if waiter was denied break
				if (state == WaiterState.BreakDenied) {
					state = WaiterState.Working;
					restaurant2WaiterGui.enableBreak();
					return true;
				}
				
				// Check if waiter was allowed break (only logic for checkbox)
				if (state == WaiterState.BreakApproved) {
					state = WaiterState.OnBreak;
					restaurant2WaiterGui.toggleBreak();
					restaurant2WaiterGui.enableBreak();
					return true;
				}
				
				
				// Check to see if there are any checks that need picking up first
				for (MyCheck myCheck : myChecks) {
					if (myCheck.state == CheckState.Produced){
						pickUpCheck(myCheck);
						return true;
					}
				}
				
				/*
				if (there exists mc in myCustomers such that mc.state = FoodPickedUp) {
					then deliverFood(mc);
				}
				**/
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.FoodPickedUp) {
						deliverFood(mc);
						return true;
					}
				}
				
				/*
				if (there exists mc in myCustomers such that mc.state = FoodReady) {
					then pickUpFood(mc);
				}
				**/   // Later implement to pick up multiple food at once maybe
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.FoodReady) {
						pickUpFood(mc);
						return true;
					}
				}
				
				/*
				if (there exists mc in myCustomers such that mc.state = ReorderRequest) {
					then requestReorder(mc);
				}
				**/
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.ReorderRequest) {
						requestReorder(mc);
						return true;
					}
				}
				
				/*
				if (there exists mc in myCustomers such that mc.state = AskedToOrder) {
					then takeOrder(mc);
				}
				**/
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.AskedToOrder) {
						takeOrder(mc);
						return true;
					}
				}
				
				/*
				if (there exists mc in myCustomers such that mc.state = Ordered) {
					then deliverOrder(mc);
				}
				**/    // Later implement to drop off multiple order at once maybe
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.Ordered){
						deliverOrder(mc);
						return true;
					}
				}
				
				
				
				
				/*
				if (there exists mc in myCustomers such that mc.state = DoneEating) {
					then cleanTable(mc);
				}
				**/
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.DoneEating) {
						cleanTable(mc);
						return true;
					}
				}
				
				/*
				if (there exists mc in myCustomers such that mc.state = Waiting) {
					then seatCustomer(mc);
				}
				**/
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.Waiting) {
						seatCustomer(mc);
						return true;
					}
				}
				
				// LAST PRIORITY
				if (state == WaiterState.OnBreak)
				{
					for (MyCustomer mc : myCustomers) {
						if (mc.state != CustomerState.LeftRestaurant) {
							// no actual work to do, just call GUI code and return false
							restaurant2WaiterGui.DoGoHome();
							return false;
						}
					}
					print("Going on break");
					atHome.tryAcquire();
					restaurant2WaiterGui.DoGoOnBreak();
					return false;
				}
			}	
	
			if (state == WaiterState.Working) {
				restaurant2WaiterGui.DoGoHome();
			}
			
			return false;
			//we have tried all our rules and found
			//nothing to do. So return false to main loop of abstract agent
			//and wait.
			
		}
		
		catch (ConcurrentModificationException e) { // lists modified while in scheduler, rerun scheduler
			return false;
		}
	}

	
	
	/**
	 * Actions
	 */

	protected void requestBreak() {
		print("Requesting Break");
		state = WaiterState.BreakRequested;
		restaurant2Host.WantBreak(this);
	}
	
	protected void seatCustomer(MyCustomer mc) {
		print("Seating customer");
		
		mc.c.getGui().SendWaiterCoordinates(restaurant2WaiterGui);
		
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		restaurant2WaiterGui.DoGoToCustomer();
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		mc.c.FollowMe(this, menu);
		restaurant2WaiterGui.DoSeatCustomer(mc.c, mc.tableNum);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.state = CustomerState.Seated;
		restaurant2WaiterGui.DoGoHome();
	}

	
	protected void takeOrder(MyCustomer mc) {
		print("Taking order");
		
		atHome.tryAcquire(); // in case waiter was sitting idly at home, take permit as you leave "home" so waiter cannot seat customer from afar
		
		restaurant2WaiterGui.DoGoToTable(mc.tableNum);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.c.WhatIsChoice(); // do some other gui stuff
		restaurant2WaiterGui.DoTakeOrder(mc.tableNum);
		
		try {
			actionComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("Order taken");
		restaurant2WaiterGui.DoGoHome();
	}

	
	protected abstract void deliverOrder(MyCustomer mc);
	
	
	protected void pickUpFood(MyCustomer mc) {
		print("Picking up food");
		
		atHome.tryAcquire(); // in case waiter was sitting idly at home, take permit as you leave "home" so waiter cannot seat customer from afar
		
		restaurant2WaiterGui.DoPickUpOrder(mc.foodChoice);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// May or may not add additional gui function for gui stuff to update waiter with food
		mc.state = CustomerState.FoodPickedUp;
		stateChanged();
	}

	
	protected void deliverFood(MyCustomer mc) {
		print("Delivering food");
		
		atHome.tryAcquire(); // in case waiter was sitting idly at home, take permit as you leave "home" so waiter cannot seat customer from afar
		
		restaurant2WaiterGui.DoGoToTable(mc.tableNum);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		restaurant2WaiterGui.DoDeliverFood(mc.tableNum); // will do some other gui stuff
		
		try {
			actionComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.c.HereIsFood(mc.foodChoice);
		print("Delivered " + mc.foodChoice + " to table " + mc.tableNum);
		mc.state = CustomerState.Eating;
		
		print("Dropping off check");
		restaurant2WaiterGui.DoGoToCashier();
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		restaurant2Cashier.ProduceCheck(this, mc.c, mc.foodChoice);
		
		restaurant2WaiterGui.DoGoHome();
	}

	
	protected void requestReorder(MyCustomer mc) {
		print("Requesting Reorder");
		
		restaurant2WaiterGui.DoGoToTable(mc.tableNum);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.c.PleaseReorder();
		mc.state = CustomerState.ReorderRequested;
		restaurant2WaiterGui.DoGoHome();
	}
	
	
	protected void cleanTable(MyCustomer mc) {
		print("Cleaning table");
		
		atHome.tryAcquire(); // in case waiter was sitting idly at home, take permit as you leave "home" so waiter cannot seat customer from afar
		
		restaurant2WaiterGui.DoGoToTable(mc.tableNum);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// give customer the check
		for (MyCheck myCheck : myChecks) {
			if (myCheck.check.c == mc.c) {
				mc.c.HereIsCheck(myCheck.check);
				myCheck.state = CheckState.Given;
			}
		}
		restaurant2WaiterGui.DoCleanTable(mc.tableNum); // will do some other gui stuff
		
		try {
			actionComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("Table cleaned");
		mc.state = CustomerState.LeftRestaurant;
		restaurant2Host.TableAvailable(this, mc.tableNum); // pass back waiter pointer so restaurant2Host can decrement the waiter's customers managed
		restaurant2WaiterGui.DoGoHome();
	}	
	
	protected void pickUpCheck(MyCheck myCheck) {
		print("Picking up check");
		
		atHome.tryAcquire();
		
		restaurant2WaiterGui.DoGoToCashier();
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		myCheck.state = CheckState.Received;
		restaurant2WaiterGui.DoGoHome();
	}
	

	
	//utilities

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setGui(restaurant.gui.WaiterGui)
	 */
	@Override
	public void setGui(Restaurant2WaiterGui gui) {
		restaurant2WaiterGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getGui()
	 */
	@Override
	public Restaurant2WaiterGui getGui() {
		return restaurant2WaiterGui;
	}

	
}

