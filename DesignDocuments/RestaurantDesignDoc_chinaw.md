
# Restaurant_chinaw
## <<<<< HOST AGENT >>>>


```
### __DATA__:

List<MyCustomer> myCustomers;
List<WaiterAgent> myWaiters;
List<Table> tables;

class MyCustomer {
	Customer c;
	CustomerState state;
}

enum CustomerState state = {waiting, served, leftUnserved};

class MyWaiter {
	Waiter w;
	int customersHandled;
}

class Table {
	CustomerAgent occupiedBy;
	int tableNum;
}
```

### __MESSAGES__:

```
IWantFood(Customer c) {
	customers.add(new MyCustomer(c, waiting));
	stateChanged();
}

WantBreak(WaiterAgent w) {
	MyWaiter mw = findWaiter(w);
	mw.state = breakRequested;
	stateChanged();
}

IAmLeaving(CustomerAgent c) {
	mc = find (c);
	mc.state = leftUnserved;
	stateChanged();
}

TableAvailable(Waiter w, int tableNum) {
	Table table = tables.find(tableNum);
	mw = waiters.find(w);
	mw.customersHandled--;
	table.unOccupied();
	stateChanged();
}
```


### __SCHEDULER__:

```
pickAndExecuteAnAction() {
	// Safety check to make sure there is at least 1 waiter
		if (myWaiters.size() == 0) {
			return false;
		}
			
		// Check for waiters requesting for break
		for (all MyWaiter mw in myWaiters) {
			if (mw.state == WaiterState.BreakRequested) {
				for (MyWaiter mw2 : myWaiters) {
					if (mw2.state == WaiterState.Working) {
						approveBreak(mw);
						return true;
					}
				}
				// if it makes it here, that means no other waiters are Working, deny Break
				denyBreak(mw);
				return true;
			}
		}
		
		
		// Safety checks to make sure there is at least 1 table, 1 customer
		if (tables.size() == 0 || myCustomers.size() == 0) {
			// No tables, or no customers, or no waiters, nothing further to do
			return false;
		}
		
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		// Check for empty table
		for (Table table : tables) {
			if (!table.isOccupied()) {
				// Check for waiting customer
				for (MyCustomer mc : myCustomers) {
					if (mc.state == CustomerState.Waiting) {

						/**
						 *  WAITER SELECTING MECHANISM
						 */
						
						
						
						// Iterate through myWaiters list once to find first customer with state Working
						MyWaiter chosenWaiter = myWaiters.get(0);
						for (MyWaiter mw : myWaiters) {
							if (mw.state == WaiterState.Working) {
								// found first waiter who is Working, leave the loop
								chosenWaiter = mw;
								break;
							}
						}
						
						// After table and customer have been selected, select waiter
						// Waiter is selected based on whomever is handling the least
						// customers. If there is a tie, first waiter with lowest
						// # of cust handled on list gets picked
						
						// There must be at least one working Waiter picked initially before going through this
						if (chosenWaiter.state == WaiterState.Working) {
							for (MyWaiter mw : myWaiters) {
								if (mw.state == WaiterState.Working) {
									if (mw.customersHandled < chosenWaiter.customersHandled) {
										chosenWaiter = mw;
									}
								}
							}
							// Now, after iterating through all the waiters, we have the waiter
							// handling the fewest number of customers in our chosenWaiter variable
							// assign customer to waiter chosenWaiter.
							seatCustomer(chosenWaiter, mc, table);
							return true;
						}
					}
				}
			}
		}
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
}
```



### __ACTIONS__:

```
approveBreak(MyWaiter mw) {
	mw.state = onBreak;
	mw.w.BreakApproved();	
}
	
denyBreak(MyWaiter mw) {
	mw.state = working;
	mw.w.BreakDenied();
}
seatCustomer(MyWaiter mw, MyCustomer mc, Table t) {	
	mc.state = served;
	t.setOccupant(mc.c);
	mw.w.PleaseSeatCustomer(this, mc.c, t.getTable());
	mw.customersHandled++;
}
```







## <<<<< WAITER AGENT >>>>>


### __DATA__:

```
private List<MyCheck> myChecks;
	
	private class MyCheck {
		Check check;
		CheckState state;
	}
	
	private enum CheckState {Produced, Received, Given};

class Menu {
	class Item {
		String food;
		double price;
	}
	List<Item> foods;
	}
}

Menu menu;

class MyCustomer {
	Customer c;
	int tableNum;
	CustomerState state;
	String foodChoice;
}
List<MyCustomer> customers;
enum CustomerState = {waiting, seated, askedToOrder, ordered, orderDelivered,	 foodReady, foodPickedUp, eating, doneEating, leftRestaurant, reorderRequest, reorderRequested};

WaiterState state = Working;
enum WaiterState {Working, WantBreak, BreakRequested, BreakApproved, BreakDenied, OnBreak};

HostAgent host;
CookAgent cook;
CashierAgent cashier;
```



### __MESSAGES__:

```
HereIsCheck(Check check) {
	myChecks.add(new MyCheck(check, produced));
	stateChanged();
}
	
BreakApproved() {
	state = breakApproved;
	stateChanged();
}

BreakDenied() {
	state = breakDenied;
	stateChanged();
}

PleaseSeatCustomer(Host h, Customer c, int tableNum) {
	setHost(h);
	customers.add(new MyCustomer(c, tableNum, waiting));
	stateChanged();
}

ReadyToOrder(Customer c) {
	MyCustomer mc = customers.find(c);
	mc.state = askedToOrder;
	stateChanged();
}

HereIsMyChoice(Customer c, string choice) {
	MyCustomer mc = customers.find(c);
	mc.state = ordered;
	mc.foodChoice = choice;
	stateChanged();
}

OrderReady(Customer c) {
	MyCustomer mc = customers.find(c);
	mc.state = foodReady;
	stateChanged();
}

DoneEating(Customer c) {
	MyCustomer mc = customers.find(c);
	mc.state = doneEating;
	stateChanged();
}

public void UnableToOrder(CustomerAgent c) {
	MyCustomer mc = findMyCustomer(c);
	mc.state = doneEating;
	stateChanged();
}
```


### __SCHEDULER__:

```
pickAndExecuteAnAction() {
	if (myCustomers.size() == 0) {
			// Check if waiter wants break
			if (state == wantBreak) {
				requestBreak();
				return true;
			}
			
			// Check if waiter was denied break
			if (state == breakDenied) {
				state = working;
				waiterGui.enableBreak();
				return true;
			}
			
			// Check if waiter is on break
			if (state == breakApproved) {
				state = onBreak;
				waiterGui.toggleBreak();
				waiterGui.enableBreak();
				waiterGui.DoGoOnBreak();
				return false;
			}	
					
		}
	else {
		// Check if waiter wants break
		if (state == wantBreak) {
			requestBreak();
			return true;
		}
		
		// Check if waiter was denied break
		if (state == breakDenied) {
			state = working;
			waiterGui.enableBreak();
			return true;
		}
		
		// Check if waiter is on break
		if (state == breakApproved) {
			state = onBreak;
			return false;
		}	
		
		// Check to see if there are any checks that need picking up first
		for (all MyCheck myCheck in myChecks) {
			if (myCheck.state == CheckState.Produced){
				pickUpCheck(myCheck);
				return true;
			}
		}
			
		for (all MyCustomer mc in myCustomers) {
			if (there exists mc in customers such that mc.state = foodPickedUp) {
				then deliverFood(mc);
				return true;
			}
		}

		for (all MyCustomer mc in myCustomers) {
			if (there exists mc in customers such that mc.state = foodReady) {
				then pickUpFood(mc);
				return true;
			}
		}

		for (all MyCustomer mc in myCustomers) {
			if (there exists mc in customers such that mc.state = askedToOrder) {
				then takeOrder(mc);
				return true;
			}
		}

		for (all MyCustomer mc in myCustomers) {
			if (there exists mc in customers such that mc.state = ordered) {
				then deliverOrder(mc);
				return true;
			}
		}

		for (all MyCustomer mc in myCustomers) {
			if (there exists mc in customers such that mc.state = doneEating) {
				then cleanTable(mc);
				return true;
			}
		}

		for (all MyCustomer mc in myCustomers) {
			if (there exists mc in customers such that mc.state = waiting) {
				then seatCustomer(mc);
				return true;
			}
		}

	waiterGui.DoGoHome();
	return false;
}
```





### __ACTIONS__:

```
requestBreak() {
	state = breakRequested;
	host.WantBreak(this);
}
	
seatCustomer(MyCustomer mc) {
	mc.c.followMe(this, menu);
	waiterGui.DoSeatCustomer(mc);
	mc.state = seated;
	waiterGui.DoGoHome();
}

takeOrder(MyCustomer mc) {
	waiterGui.DoGoToTable(mc.tableNum);
	mc.state.WhatIsChoice();
	waiterGui.DoTakeOrder();
}

deliverOrder(MyCustomer mc) {
	waiterGui.DoDeliverOrder();
	cook.HereIsOrder(this, mc.c, mc.foodChoice, mc.tableNum);
	mc.state = orderDelivered;
}

pickUpFood(MyCustomer mc) {
	waiterGui.DoPickUpOrder();
	mc.state = foodPickedUp;
	stateChanged();
}

deliverFood(MyCustomer mc) {
	waiterGui.DoGoToTable(mc.tableNum);
	waiterGui.DoDeliverFood(mc.tableNum); 
	mc.c.HereIsFood(mc.foodChoice);
	mc.state = eating;
}

cleanTable(MyCustomer mc) {
	waiterGui.DoGoToTable(mc.tableNum);
	waiterGui.DoCleanTable(mc.tableNum); // will do some other gui stuff
	mc.state = leftRestaurant;
	host.TableAvailable(this, mc.tableNum);
}
```







## <<<<< CUSTOMER AGENT >>>>>

### __DATA__:

```
HostAgent host;
WaiterAgent waiter;
CashierAgent cashier;

String foodChoice;
Menu menu;
Check check;
double money;

AgentPersonality personality = Normal

enum AgentPersonality {Normal, Impatient, Scumbag, Poor, Picky};

AgentState state = doingNothing;
AgentEvent event = none;
enum AgentState {doingNothing, waitingInRestaurant, beingSeated, checkingMenu, readyToOrder, ordered, eating, doneEating, paidCashier, leaving};
enum AgentEvent {none, gotHungry, followWaiter, seated, pickedItem, ordering, foodArrived, doneEating, doneLeaving, checkReceived, changeReceived, cannotOrder, noFood, gotImpatient};
```


### __MESSAGES__:

```
GotHungry() { 
	event = gotHungry;
	stateChanged();
}

WaitTooLong() {
	if (state == WaitingInRestaurant) {
		event = gotImpatient;
		stateChanged();
	}
}

FollowMe(WaiterAgent waiter, Menu m) {
	setWaiter(waiter);
	menu = m;
	event = followWaiter;
	stateChanged();
}

WhatIsChoice() {
	event = ordering;
	stateChanged();
}

HereIsFood(string choice) {
	event = foodArrived;
	stateChanged();
}

HereIsCheck(Check check) {
	this.check = check;
	event = checkReceived;
	stateChanged();
}

HereIsChange(double cash) {
	money = money + cash;
	event = changeReceived;
	stateChanged();
)
```


### __SCHEDULER__:

```
pickAndExecuteAnAction() {
	if (state == doingNothing and event == gotHungry) {
		goToRestaurant();
		return true;
	}

	if (state == waitingInRestaurant and event == followWaiter) {
		sitDown();
		return true;
	}
	// NON-NORMATIVE SCENARIO, TABLES SATURATED, CUSTOMER IMPATIENT
	if (state ==waitingInRestaurant && event == gotImpatient ) {
		leaveNoTime();
		return true;
	}
	
	if (state == beingSeated and event == seated) {
		reviewMenu();
		return true;
	}
	// NON-NORMATIVE SCENARIO, OUT OF DESIRED FOOD CHOICE
	if (state == beingSeated && event == noFood) {
		leaveNoFood();
		return true;
	}

	if (state == checkingMenu and event == pickedItem) {
		callWaiter();
		return true;
	}
	// NON-NORMATIVE SCENARIO, UNABLE TO ORDER ANY FOOD
	if (state == checkingMenu && event == cannotOrder) {
		leaveNoMoney();
		return true;
	}
	
	if (state == readyToOrder and event == ordering) {
		placeOrder();
		return true;
	}

	if (state == ordered and event == foodArrived) {
		eatFood();
		return true;
	}

	if (state == eating && event == doneEating) {
		waiter.DoneEating(this);
		state = doneEating;
		return true;
	}
	
	if (state == doneEating && event == checkReceived) {
		payCashier();
		return true;
	}
	
	if (state == paidCashier && event == changeReceived) {
		leaveRestaurant();
		return true;
	}

	if (state == leaving && event == doneLeaving) {
		state = doingNothing;
		return true;
	}

	return false;
}
```


### __ACTIONS__:

```
goToRestaurant() {
	state = waitingInRestaurant;
	host.msgIWantFood(this);
}

// NON-NORMATIVE, TAKING TOO LONG TO GET SEATED
private void leaveNoTime() {
	host.IAmLeaving(this);
	state = leaving;
	leaveRestaurant();
}

// NON-NORMATIVE, OUT OF DESIRED FOOD
leaveNoFood() {
	waiter.UnableToOrder(this);
	state = leaving;
	customerGui.DoNoFoodLeave();
}

// NON-NORMATIVE UNABLE TO ORDER
leaveNoMoney() {
	waiter.UnableToOrder(this);
	state = leaving;
	customerGui.DoNoMoneyLeave();
}

sitDown() {
	state = beingSeated;
	customerGui.DoGoToSeat();
}

reviewMenu() {
	state = checkingMenu;
	customerGui.DoReviewMenu();
}

callWaiter() {
	state = readyToOrder;
	waiter.ReadyToOrder(this);
}

placeOrder() {
	waiter.HereIsChoice(this, foodChoice);
	state = ordered;
	customerGui.DoPlaceOrder(foodChoice);
}

eatFood() {
	state = eating;
	customerGui.DoEatFood();
}

leaveRestaurant() {
	state = leaving;
	waiter.DoneEating(this);	
	customerGui.DoExitRestaurant();
}

payCashier() {
	customerGui.DoGoToCashier();
	if (check.price <= money) {
		cashier.HereIsPayment(check, check.price);
		money = money - check.price;
	}
		
	else { // check.price > money	
		cashier.HereIsPayment(check, money);
		money = 0;
	}
	state = paidCashier;
}
```









## <<<<< COOK AGENT >>>>>


### __DATA__:

```
List<Order> orders

class Order {
	Customer c;
	Waiter w;
	string foodChoice;
	int tableNum;
	OrderState state = none;
}

enum OrderState = {none, pending, cooking, doneCooking, plating, readyToServe, served, ignored}

Timer timer;

Map<String, Food> foodMap;

class Food {
	String foodName;
	int cookTime;
	int plateTime;
	int inventory;
	int low;
	int capacity;
	int reorder = 0;
}
	FoodState state = initial;
}
	
enum FoodState {Initial, Low, Ordered, Reorder, Enough};
```

### __MESSAGES__:

```
HereIsOrder(Waiter w, Customer c, String foodChoice, int tableNum) {
	orders.add(new Order(w, c, foodChoice, tableNum, pending));
	stateChanged();
}

foodDone(Order o) {
	o.state = done;
	stateChanged();
}

plateDone(Order o) {
	o.state = readyToServe;
	stateChanged();
}

NotEnoughFood(String foodName, int amount) {
	Food f = foodMap.get(foodName);
	f.reorder = amount;
	f.state = reorder;
	stateChanged();
}

HereIsFood(String foodName, int amount) {
	Food f = foodMap.get(foodName);
	f.inventory = f.inventory + amount;
	if (f.inventory >= f.capacity) {
		f.state = enough;
	}
	else {
		f.state = low;
	}
}
```


### __SCHEDULER__:

```
pickAndExecuteAnAction() {
	// safety check to make sure orders not empty
	if (orders.size() == 0) {
		return false;
	}

	// initial check on all food inventory
	for( all Foods in inventory) {
			if (food.inventory <= food.low) {
				requestFood(food);
			}
			food.state = enough;
		}
	}
		
				
	// check if any foods need ordering or reordering
	for( all Foods in inventory ) {
		if ( food.state = low) || food.state = reorder) ) {
			requestFood(food);
			return true;
		}
	}
	
	for (all Orders o in orders) {
		if (there exists o in orders such that o.state = readyToServe) {
			orderIsDone(o);
			return true;
		}
	}
	
	for (all Orders o in orders) {
		if (there exists o in orders such that o.state = doneCooking) {
			plateOrder(o);
			return true;
		}
	}
	
	for (all Orders o in orders) {
		if (there exists o in orders such that o.state = pending) {
			makeOrder(o);
			return true;
		}
	}
	
	return false;
}
```



### __ACTIONS__:

```
makeOrder(Order o) {
	cookGui.DoCooking(o); // no actual gui stuff for v2
	o.state = cooking;
	timer.schedule(new TimerTask() {
			public void run() {
				FoodDone(o);
			}
		},
	(foodMap.get(o.foodChoice).cookTime) );
}

plateOrder(Order o) {
	cookGui.DoPlating(o); // no actual gui stuff for v2
	o.state = plating;
	timer.schedule(new TimerTask() {
			public void run() {
				PlateDone(o);
			}
		},
	(foodMap.get(o.foodChoice).plateTime) );
}

orderIsDone(Order o) {
	o.w.OrderReady(o.customer, o.choice=);
	o.state = served;
}

requestFood(Food f) {
	if (f.reorder != 0) {
		if (marketCounter == 2*markets.size()) {
			return;
		}
		marketCounter++;
		markets.get( marketCounter % markets.size() ).INeedFood(this, f.foodName, f.reorder);
		f.reorder = 0;
		f.state = ordered;
	}
	else { // reorder == 0
		markets.get( marketCounter % markets.size() ).INeedFood(this, f.foodName, f.capacity - f.inventory);
		f.state = ordered;
	}
}
```







## <<<<< CASHIER AGENT >>>>>

### __DATA__:

```
List<Check> checks

class Check {
		Customer c;
		Waiter w;
		String foodChoice;
		double price;
		CheckState state = CheckState.Requested;
		double payment = 0;
	}

enum CheckState {Requested, Produced, Paid, Debt, Done}
Map<String,Double> priceMap


__MESSAGES__:

ProduceCheck(Waiter w, Customer c, String choice) {
	checks.add(new Check(c, w, choice));
	stateChanged();
}

HereIsPayment(Check check, double cash) {
	check.state = Paid;
	check.payment = cash;
	stateChanged();
}
```

### __SCHEDULER__:

```
pickAndExecuteAnAction() {
	// Safety check first to avoid null pointer exception that there is some check in checks
	if (checks.size() == 0) {
		// no checks to handle, nothing to do
		return false;
	}
		
	if (there exists Check check in checks such that check.state = Paid) {
			giveCustomerChange(check);
			return true;
		}
	}
		
	if (there exists Check check in checks such chat check.state = Requested) {
			giveWaiterCheck(check);
			return true;
		}
	}
	return false;
}
```



### __ACTIONS__:

```
giveWaiterCheck(Check check) {
	// iterate through all checks, find indebted checks, if customer owes any money, append to price
	if (there exists Check currentcheck in checks such that currentcheck.state = Debt) {
			if (check.c == currentcheck.c) {
				check.price = check.price + currentcheck.price; // append the debted value from previous meal to this meals price
				currentcheck.state = Done;
			}
		}
	}
	check.state = Produced;
	check.w.HereIsCheck(check);
}
	
	
giveCustomerChange(Check check) {
	if ( check.payment >= check.price ) {
		check.state = Done;
		check.c.HereIsChange( check.payment - check.price );
	}
	else { //check.payment < check.price (Customer was not able to pay full amount for meal)
		check.state = Debt; // mark check as in debt for later use
		check.price = check.price - check.payment; // produce the debt owed for this meal as the price on the check
		check.c.HereIsChange( 0 ); // no change
	}
}
```






## <<<<< MARKET AGENT >>>>>

### __DATA__:

```
List<MyCook> myCooks
	
class MyCook {
	CookAgent c;
	CookState state = None;
	List<Food> foodRequests;
	MyCook(CookAgent c, CookState state) {
		this.c = c;
		this.state = state;
	}
}
	
enum CookState {None, OrderRequested, OrderProcessed};
	
Timer timer = new Timer();

private Map<String, Food> marketInventory;
	
class Food {
	String foodName;
	int amount;
	Food(String foodName, int amount) {
		this.foodName = foodName;
		this.amount = amount;
	}
}
```

### __MESSAGES__:

```
INeedFood(CookAgent c, String foodChoice, int amount) {
	MyCook mc = new MyCook(c, OrderRequested);
	mc.foodRequests.add(new Food(foodChoice, amount));
	myCooks.add(mc);
	stateChanged();
}
```



### __SCHEDULER__:

```
pickAndExecuteAnAction() {
	// Safety check first to avoid null pointer exception that there is some cooks in myCooks
	
	if (myCooks.size() == 0) {
		// No cooks in myCooks, nothing to do
		return false;
	}
		
	if (there exists MyCook mc in myCooks such that
		mc.state = OrderRequested) {
			processRequest(mc);
			return true;
		}
	}

	return false;
}
```



### __ACTIONS__:

```
processRequest(MyCook mc) {
	for (Food f : mc.foodRequests) {
		Food marketFood = marketInventory.get(f.foodName);
		if (f.amount <= marketFood.amount) {
			marketFood.amount = marketFood.amount - f.amount;
			deliverFood(mc, f.foodName, f.amount);
		}
		else { // (f.amount > marketFood.amount)
			if (marketFood.amount <= 0) {
				mc.c.NotEnoughFood(f.foodName, f.amount);
			}
			else { // marketFood.amount > 0
				mc.c.NotEnoughFood(f.foodName, (f.amount - marketFood.amount));
				deliverFood(mc, f.foodName, marketFood.amount);
				marketFood.amount = 0;
			}
		}	
	}
	mc.state = OrderProcessed;
}
	
private void deliverFood(final MyCook mc, final String foodName, final int amount) {
	timer.schedule(new TimerTask() {
		public void run() {
			print("Food Delivered to " + mc.c.getName());
			mc.c.HereIsFood(foodName, amount);
		}
	},
25000 );
}
```













