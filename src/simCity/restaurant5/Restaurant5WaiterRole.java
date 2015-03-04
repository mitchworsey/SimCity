package simCity.restaurant5;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.restaurant5.gui.*;
import simCity.restaurant5.interfaces.Restaurant5Cashier;
import simCity.restaurant5.interfaces.Restaurant5Cook;
import simCity.restaurant5.interfaces.Restaurant5Customer;
import simCity.restaurant5.interfaces.Restaurant5Host;
import simCity.restaurant5.interfaces.Restaurant5Waiter;

public class Restaurant5WaiterRole extends Role implements Restaurant5Waiter { // implements Restaurant5Waiter{
	static final int NTABLES = 4;// a global for the number of tables.
	// Notice that we implement waitingCustomers using ArrayList, but type it
	// with List semantics.
	public List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	// note that tables is typed with Collection semantics.
	// Later we will see how it is implemented
	public List<String> Menu = Collections.synchronizedList(new ArrayList<String>());

	protected boolean onBreak = false;

	protected String name;
	protected Semaphore atTable = new Semaphore(0, true);
	protected Semaphore atKitchen = new Semaphore(0, true);
	protected Semaphore atWaitingArea = new Semaphore(0, true);
	protected Semaphore atHome = new Semaphore(0,true);
	protected Semaphore atCashier = new Semaphore(0, true);
	
	protected Semaphore waiting = new Semaphore(0,true);

	public Restaurant5WaiterGui waiterGui = null;

	Timer timer = new Timer();
	protected int BreakTime = 5000;

	public enum CustomerState {
		WaitingToBeSeated, BeingSeated, Seated, ReadyToOrder, WaitingForFood, FoodIsComing, ReceivedFood, Eating, WaitingForCheck,
		DoneAndLeaving, OutOfFood};

		protected CustomerState cstate = CustomerState.WaitingToBeSeated;

		public enum BreakState {
			None, Pending, RequestedBreak, BreakDenied, BreakAccepted, currentlyOnBreak
		}
		BreakState myBreakState = BreakState.None;


		public enum FoodState {
			None, Chosen, Ordered, Pending, Cooking, ReadyToPlate, Plated, OnItsWay, Served, OutofFood, OrderedCheck, NotPaid, 
			GaveCheck, Paid};

			protected Restaurant5Host host;// = new HostAgent;
			protected Restaurant5Cook cook;
			protected Restaurant5Cashier cashier;// = new CashierAgent("Cashier"); 

			public Restaurant5WaiterRole(String name) {
				super();
				Menu.add("Steak");
				Menu.add( "Chicken");
				Menu.add("Salad");
				Menu.add( "Pizza");
			//	print("Menu size is " + Menu.size());

				this.name = name;
				waiterGui = new Restaurant5WaiterGui(this);
				this.setHost(host);
				this.setCook(cook);
				this.setCashier(cashier);
			}

			/* (non-Javadoc)
			 * @see restaurant.Waiter#setHost(restaurant.Host)
			 */
			@Override
			public void setHost(Restaurant5Host host) {
				this.host = (Restaurant5Host) host;
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#setCook(simCity.restaurant5.interfaces.Restaurant5Cook)
			 */
			@Override
			public void setCook(Restaurant5Cook cook) {
				this.cook = (Restaurant5Cook) cook;
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#setCashier(simCity.restaurant5.interfaces.Restaurant5Cashier)
			 */
			@Override
			public void setCashier(Restaurant5Cashier cashier) {
				this.cashier = (Restaurant5Cashier) cashier;
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#getMaitreDName()
			 */
			@Override
			public String getMaitreDName() {
				return name;
			}

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#getName()
			 */
			@Override
			public String getName() {
				return name;
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#toString()
			 */
			@Override
			public String toString() {
				return "Waiter " + getName();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#getMyCustomers()
			 */
			@Override
			public List<MyCustomer> getMyCustomers() {
				return myCustomers;
			}

			// //////////// MESSAGES ///////////////



			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgReadyToServe()
			 */
			@Override
			public void msgReadyToServe() {
				stateChanged();
			}



			public void msgSeatCustomer(Restaurant5Customer c, Restaurant5Waiter w, int table) {
				print(w + " has been assigned to serve " + c.getName());
				print("MSG SEAT CUSTOMER");
				myCustomers.add(new MyCustomer(c, table, CustomerState.WaitingToBeSeated));
				stateChanged();
			}




			//~~~~~~~~~~~~~~~~~~~~~~~~~GUI MSGs~~~~~~~~~~~~~~~~~~~~~~//


			public void msgLeavingTable(Restaurant5Customer cust) {
				for (MyCustomer c : myCustomers) {
					if (c.c == cust) {
						print(cust + " leaving " + c.table);
						c.s = CustomerState.DoneAndLeaving;
						stateChanged();// calls scheduleee
					}
				}
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgAtTable()
			 */
			@Override
			public void msgAtTable() {// from animation
				print("msgAtTable() called");
				atTable.release();// = true;
				//wstate = WaiterState.AtTable;
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgAtKitchen()
			 */
			@Override
			public void msgAtKitchen() {// from animation
				print("msgAtKitchen called");
				atKitchen.release();
				stateChanged();
			}

			public void msgAtCashier() {// from animation
				print("msgAtCashier called");
				atCashier.release();
				stateChanged();
			}

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgAtWaitingArea()
			 */

			@Override
			public void msgAtWaitingArea() { // from animation
				print("msgAtWaitingArea() called");
				atWaitingArea.release();
				stateChanged();

			}

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgAtHome()
			 */
			@Override
			public void msgAtHome() { // from animation
				print("msgAtHome() called");
				atHome.release();
				stateChanged();

			}
			
			public void msgWaitComplete() {
				print("msgAtHome() called");
				waiting.release();
				stateChanged();
			}


			//~~~~~~~~~~~~~~~~~~~~~~~~~CUSTOMER MSGs~~~~~~~~~~~~~~~~~~~~~~//
			
			public void msgReadyToOrder(Restaurant5Customer c) { //CUSTOMER - I am ready to order.
				print("msgReadyToOrder() called");
				findCustomer(c).s = CustomerState.ReadyToOrder;
				stateChanged();
			}

			public void msgHereIsMyOrder(String choice, Restaurant5Customer customer) { //CUSTOMER - I want to order this.
				print(customer.getName() + ", you have ordered: " + choice);
				findCustomer(customer).orderState = FoodState.Ordered;
				findCustomer(customer).order = choice;
				findCustomer(customer).s = CustomerState.WaitingForFood;
				stateChanged();
			}

			public void msgWhereIsMyCheck(Restaurant5Customer cust) {
				for (MyCustomer c : myCustomers) {
					if (c.c == cust) {
						print(cust + " wants check.");
						c.s = CustomerState.WaitingForCheck;
						stateChanged();// calls scheduleee
					}
				}
			}
			//~~~~~~~~~~~~~~~~~~~~~~~~~COOK MSGs~~~~~~~~~~~~~~~~~~~~~~//

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgGaveCookOrder()
			 */
			@Override
			public void msgGaveCookOrder() {
				print("msgGaveCookOrder called. Go home, Ben.");
				//wstate = WaiterState.GaveCookOrder;
				//waiterGui.DoGoHome();
				stateChanged();
			}

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgOrderIsReady(java.lang.String, int)
			 */
			@Override
			public void msgOrderIsReady(String order, int table) {
				print("msgOrderIsReady was called from Cook."); // go pick up the ready
				// plate and serve
				// findCustomer().orderState = FoodState.Plated;
				findCustomerTable(table).orderState = FoodState.Plated;
				//findCustomerOrder(order).s = CustomerState.FoodIsComing;
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgOutOfFood(java.lang.String, int)
			 */
			@Override
			public void msgOutOfFood(String outofFood, int table) { // from cook
				print("Cook says we're out of :" + outofFood);
				findCustomerTable(table).s = CustomerState.OutOfFood;
				stateChanged();
			}

			//~~~~~~~~~~~~~~~~~~~~~~~~~CASHIER MSGs~~~~~~~~~~~~~~~~~~~~~~//

			public void msgHereIsCheck(Restaurant5Customer customer, int totalCheck) {
				print("Received check from cashier.");
				//go to customer and deliver check
				findCustomer(customer).orderState = FoodState.NotPaid; 
				findCustomer(customer).check = totalCheck;
				stateChanged();
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~BREAK MSGs~~~~~~~~~~~~~~~~~~~~~~//


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#gotTired()
			 */
			@Override
			public void gotTired() {
				//				print("I'm hungry");
				//		event = AgentEvent.gotHungry;
				//		stateChanged();

				print("I'm tired");
				myBreakState = BreakState.Pending;
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgDenyBreak()
			 */
			@Override
			public void msgDenyBreak() {
				print("Host just denied my break :(");
				myBreakState = BreakState.BreakDenied;
				//continue working
				//change setEnabled for checkbox
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#msgAcceptBreak()
			 */
			@Override
			public void msgAcceptBreak() {
				print("Host just accepted my break :)");
				myBreakState = BreakState.BreakAccepted;
				//go on break
				//start timer for checkbox
				stateChanged();

			}

			//~~~~~~~~~~~~~~~~~~~~~~~~~Find Accessors~~~~~~~~~~~~~~~~~~~~~~//

			public MyCustomer findCustomer(Restaurant5Customer c) {
				for (int n = 0; n < myCustomers.size(); n++) {
					if (c == myCustomers.get(n).c) {
						return myCustomers.get(n);
					}
				}
				return null; // print out stating couldn't find customer.
			}

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#findCustomerTable(int)
			 */
			@Override
			public MyCustomer findCustomerTable(int table) {
				for (int n = 0; n < myCustomers.size(); n++) {
					if (table == myCustomers.get(n).table) {
						return myCustomers.get(n);
					}
				}
				return null; // print out stating couldn't find customer.
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#findCustomerOrder(java.lang.String)
			 */
			@Override
			public MyCustomer findCustomerOrder(String order) {
				for (int n = 0; n < myCustomers.size(); n++) {
					if (order == myCustomers.get(n).order) {
						return myCustomers.get(n);
					}
				}
				return null; // print out stating couldn't find customer.
			}

			/**
			 * SCHEDULER: Determine what action is called for, and do it.
			 */

			// if there exists c in customers such that c.s = Waiting, then
			// SeatCustomer(c);
			// if there exists c in customers such that c.s = ReadyToORder, then
			// GoToCustomerAndTakeOrder(c);

			protected boolean pickAndExecuteAnAction() {
				/*
				 * Think of this next rule as: Does there exist a table and customer, so
				 * that table is unoccupied and customer is waiting. If so seat him at
				 * the table.
				 */
				// if (wstate == WaiterState.DoingNothing) { //semaphore***
				// WaitingToBeSeated, Seated, ReadyToOrder, WaitingForFood,
				// ReceivedFood, DoneAndLeaving
				if(! (myBreakState == BreakState.currentlyOnBreak))
				{
					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).s == CustomerState.WaitingToBeSeated) {
							//print("SCHEDULE WALKING CUSTOMER TO TABLE");
							WalkingCustomerToTable(myCustomers.get(i),
									myCustomers.get(i).table);// the action
							return true;// return true to the abstract agent to reinvoke
							// the scheduler.
						}
					}
//					for (int i = 0; i < myCustomers.size(); i++) {
//						if (myCustomers.get(i).s == CustomerState.BeingSeated) {
//							seatCustomer(myCustomers.get(i));
//							return true;
//						}
//					}
					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).s == CustomerState.ReadyToOrder) {
							print("Customer is ReadyToOrder -> TakeCustomerOrder");
							TakeCustomerOrder(myCustomers.get(i));
							return true;
						}
					}
					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).orderState == FoodState.Ordered) { //findCustomer(customer).orderState = FoodState.Ordered;
							print("Customer has given an order -> GiveCookOrder");
							GiveCookOrder(myCustomers.get(i), myCustomers.get(i).order,
									myCustomers.get(i).table);
							return true;
						}
					}

					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).orderState == FoodState.Plated) { //(order).orderState = FoodState.Plated;
							PickUpOrder(myCustomers.get(i), myCustomers.get(i).order);
							return true;
						}
					}
					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).orderState == FoodState.OnItsWay) {
							TakeOrderToCustomer(myCustomers.get(i),
									myCustomers.get(i).order);
							return true;
						}
					}

					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).s == CustomerState.DoneAndLeaving ) {
							CustomerIsLeaving(myCustomers.get(i)); // telling host customer is leaving, removing cust
							return true;
						}
					}
					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).s == CustomerState.OutOfFood) {
							OutOfFood(myCustomers.get(i));
							//tell customer their food is unavail
							return true;
						}
					}
					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).orderState == FoodState.Served) {
							//take order to cashier to save and make check
							GiveCashierOrder( myCustomers.get(i), myCustomers.get(i).order);
							return true;
						}
					}
					for (int i = 0; i < myCustomers.size(); i++) {
						if (myCustomers.get(i).orderState == FoodState.NotPaid && myCustomers.get(i).s == CustomerState.WaitingForCheck) {
							//take check to customer 
							GiveCustomerCheck( myCustomers.get(i));
							return true;
						}
					}


					if (myBreakState == BreakState.Pending) { // tired -> ask to go on break
						askToBreak();

						return true;
					}

					if (myBreakState == BreakState.RequestedBreak) {
						return true;
					}
					if (myBreakState == BreakState.BreakAccepted) {
						onBreak();
						return true;
					}
					if (myBreakState == BreakState.BreakDenied) {
						BreakDenied();
						return true;
					}
				}


				return false;
				// we have tried all our rules and found
				// nothing to do. So return false to main loop of abstract agent
				// and wait.
			}

			// ///////////// ACTIONS ////////////////
			// acquire after sending msg to gui
			// gui releases when destination is = to position

			protected void WalkingCustomerToTable(MyCustomer customer, int table) {
				atHome.tryAcquire();
				print("Seating " + customer.c + " at table #" + table);
				customer.s = CustomerState.BeingSeated;
				/* 1. Pick up the customer from the waiting area */
				waiterGui.DoGoToWaitArea();
				try {
					atWaitingArea.acquire(); //->sleep // second time -> run
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/* 2. Tell the customer to follow you to their table */
				customer.c.msgFollowMe(table, Menu); // Menu is given!
				waiterGui.DoBringToTable(customer.c, table); // animation call
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/* 3. Once you have reached the table, give the customer their menu and go to your home position */
				print(customer.c + ", here is your table and your menu.");
				
				//print("AT WaitingArea RELEASED. DO SEAT CUSTOMER - ACTION");
				waiterGui.DoGoHome(); // animation call
				customer.s = CustomerState.Seated;
				//stateChanged();
			}

			protected void TakeCustomerOrder(MyCustomer customer) {
				atHome.tryAcquire();
				print("Taking " + customer.c + "'s order at table " + customer.table);
				waiterGui.TakeOrder(customer.c, customer.table); // GUI go to customer
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				customer.c.msgWhatDoYouWant();
				
				waiterGui.waiting();
				try{ waiting.acquire(); } catch(InterruptedException e) {e.printStackTrace();}
				
				waiterGui.DoGoHome();
				//customer.s = CustomerState.;
			}

			protected void GiveCookOrder(MyCustomer customer, String choice, int table) {
				atHome.tryAcquire();
				print("Going to give the cook the order: " + choice);
				waiterGui.setFoodString(choice); // TEXT
				waiterGui.DoGiveOrderToCook(choice); // animation call
				try {
					atKitchen.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cook.msgHereIsOrder(this, choice, table);
				waiterGui.setFoodString(" "); // TEXT
				//customer.s = CustomerState.WaitingForFood;
				customer.orderState = FoodState.Pending;
				waiterGui.DoGoHome();

			}

			protected void PickUpOrder(MyCustomer customer, String choice) {
				atHome.tryAcquire();
				print("On my way to pick up " + choice);
				waiterGui.DoPickUpOrder(choice); //gui
				try {
					atKitchen.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				waiterGui.setFoodString(choice); // TEXT
				customer.orderState = FoodState.OnItsWay;
				
			}

			protected void TakeOrderToCustomer(MyCustomer customer, String choice) {
				atHome.tryAcquire();
				print("Food is on it's way.");
				//waiterGui.DoBringToTable(customer.c, customer.table); // animation
				waiterGui.TakeOrder(customer.c, customer.table);
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				print(customer.c + ", here is your order: " + choice);
				waiterGui.setFoodString(" "); // TEXT
				customer.c.msgHereIsYourFood(choice);
				customer.s = CustomerState.ReceivedFood;
				customer.orderState = FoodState.Served;
				waiterGui.DoGoHome();
				//stateChanged();
			}

			protected void OutOfFood(MyCustomer customer) {
				atHome.tryAcquire();
				print("We're out of this food..." );
				//waiterGui.DoBringToTable(customer.c, customer.table);
				waiterGui.TakeOrder(customer.c, customer.table);
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				print(customer.c + ", order something else or leave.");
				customer.c.msgNoFoodLeaveOrReorder();
				waiterGui.DoGoHome();
				customer.orderState = FoodState.Pending;
				customer.s = CustomerState.Seated;
				//waiterGui.DoGoHome();
				//customer.s = CustomerState.DoneAndLeaving;
			}
			
			protected void CustomerIsLeaving(MyCustomer customer) {
				// tell host -> unoccupy table
				// this.host.msgLeavingTable(customer);
				// take customer off mycustomer list
				host.msgLeavingTable(customer.c, this);// tell Host that customer is leaving
				myCustomers.remove(customer);
				waiterGui.DoGoHome();
				//print(customer.getName() + " was removed!");

			}

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#guiSetBreak()
			 */
			@Override
			public void guiSetBreak() {
				print("Man! I'm tired... ~wink wink~");
				//ask host if waiter can go on break
				myBreakState = BreakState.RequestedBreak;
				//host.msgIWantBreak(this);

			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#askToBreak()
			 */
			@Override
			public void askToBreak() {
				print("Host, can I go on break?");
				host.msgIWantBreak(this);
				myBreakState = BreakState.RequestedBreak;
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#onBreak()
			 */
			@Override
			public void onBreak() {//from animation
				print("I'm on break!");
				//onBreak = true;
				waiterGui.DoGoOnBreak();
				myBreakState = BreakState.currentlyOnBreak;
				BreakTime();
				//DoGoHome();
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#BreakDenied()
			 */
			@Override
			public void BreakDenied() { // enable check box 
				print("I will continue working...");
				//onBreak = false;
				waiterGui.DoStayWorking(); // changes checkbox to unchecked
				myBreakState = BreakState.None;
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#BreakTime()
			 */
			@Override
			public void BreakTime() {
				timer.schedule(new TimerTask() { 
					public void run() { 
						backFromBreak();
					} 
				}, BreakTime);
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#backFromBreak()
			 */
			@Override
			public void backFromBreak() { // enable check box
				print("I'm back!");
				host.msgBackFromBreak(this);
				//onBreak = false;
				waiterGui.DoStayWorking();// changes checkbox to unchecked
				myBreakState = BreakState.None;
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#GiveCashierOrder(simCity.restaurant5.Restaurant5WaiterRole.MyCustomer, java.lang.String)
			 */
			@Override
			public void GiveCashierOrder(MyCustomer mcust, String order) {
				atHome.tryAcquire();
				print("Going to cashier to get customer's check.");
				waiterGui.DoGoToCashier();
				try {
					atCashier.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				print("Cashier, here is what the customer ordered.");
				cashier.msgHereIsOrder(this, mcust.c, order);
				waiterGui.DoGoHome();
				mcust.orderState = FoodState.OrderedCheck;
				stateChanged();

			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#GiveCustomerCheck(simCity.restaurant5.Restaurant5WaiterRole.MyCustomer)
			 */
			@Override
			public void GiveCustomerCheck(MyCustomer customer) {
				atHome.tryAcquire();
				print("Giving " + customer.c + " check.");
				waiterGui.DoBringToTable(customer.c, customer.table); // animation
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				print(customer.c + ", here is your check of " + customer.check);
				customer.c.msgHereIsYourCheck(customer.check);
				waiterGui.DoGoHome();//***
				customer.orderState = FoodState.GaveCheck;
				stateChanged();
			}


			// ///////////// ANIMATION ////////////// //The animation DoXYZ() routines
//			protected void DoSeatCustomer(Restaurant5Customer customer, int table) {
//				// Notice how we print "customer" directly. It's toString method will do
//				// it.
//				// Same with "table"
//				print("Seating " + customer + " at " + table);
//				//print("DO SEAT CUSTOMER CALLED");
//				waiterGui.DoGoToWaitArea();
//				try {
//					atWaitingArea.acquire();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				waiterGui.DoBringToTable(customer, table);
//				try {
//					atTable.acquire();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				//customer.getGui().DoGoToSeat(table);
//
//			}
//
//			protected void DoTakeCustomerOrder(Restaurant5Customer customer, int table) {
//				waiterGui.TakeOrder(customer, table);
//			}
//
//			protected void TakeOrderToCook(String order) {
//				waiterGui.DoGiveOrderToCook(order);
//			}
//
//			protected void DoGoHome() {
//				print("Going home.");
//				waiterGui.DoGoHome();
//			}

			///////////////// UTILITIES /////////////////

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#callStateChange()
			 */
			@Override
			public void callStateChange() {
				stateChanged();
			}


			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#setGui(simCity.restaurant5.gui.Restaurant5WaiterGui)
			 */
			@Override
			public void setGui(Restaurant5WaiterGui gui) {
				waiterGui = gui;
			}

			/* (non-Javadoc)
			 * @see simCity.restaurant5.Restaurant5Waiter#getGui()
			 */
			@Override
			public Restaurant5WaiterGui getGui() {
				return waiterGui;
			}

			public class MyCustomer {
				public Restaurant5Customer c;
				int table;
				String order;
				CustomerState s;
				FoodState orderState = FoodState.None;

				int check;

				MyCustomer(Restaurant5Customer cc, int tablec, CustomerState sc) {
					c = cc;
					table = tablec;
					s = sc;
				}

			}

}
