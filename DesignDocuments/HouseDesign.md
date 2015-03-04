#House



##Owner Role

###Data
	String name;
	double money;
	List<MyCustomer> customers;
	List<House> houses;
	HouseMaintenanceManager maintenance;
	class MyCustomer{
		Customer c;
		House house;
		CustomerState cs;
	}
	enum CustomerState {wantsToBuyProperty, toldCantBuyProperty, askedToPayDeposit, paidDeposit, movedIn, needsToPayRent, askedToPayRent, paidRent};
	class House{
		Owner owner;
		Customer occupiedBy;
		MaintenanceManager maintenance;
		String type;
		int address, xCoordinate, yCoordinate;
		double monthlyPayment, securityDeposit, maintenanceFee;
		HouseState hs;
	}
	enum HouseState {maintained, needsMaintenance, waitingForMaintenance, justMaintained};

###Messages
	msgIWantToLiveHere(Customer c){
		House h = findAvailableHouse();
		customers.add(new MyCustomer(c, h, wantsToBuyProperty));
	}
	
	msgHereIsSecurityDeposit(Customer c, double payment){
		money += payment;
		MyCustomer mc = customers.find(c);
		mc.cs = paidDeposit;
	}

	msgResidentNeedsToPayRent(Customer c){
		MyCustomer mc = customers.find(c);
		mc.cs = needsToPayRent;
	}
	
	msgHereIsRent(Customer c, double payment){
		money += payment;
		MyCustomer mc = customers.find(c);
		mc.cs = paidRent;
	}
	
	msgHouseNeedsMaintenance(Customer c){
		//Owner will receiver this after a timer is finished or on a certain day every month or if something in the house breaks
		House h = houses.findHouseWithCustomer(c);	
		h.hs = needsMaintenance;
	}
	
	msgPayMaintenanceFee(House house){
		House h = houses.find(house);
		h.hs = justMaintained;
	}

###Scheduler
	if ∃ c in customers ∋ c.cs = wantsToBuyProperty,
		if c.house != null,
			then tellCustomerToPaySecurityDeposit(c)
		else,
			then tellCustomerNoHousesAvailable(c)
 
	if ∃ c in customers ∋ c.cs = paidDeposit,
		then tellCustomerToMoveIn(c)
	
	if ∃ c in customers ∋ c.cs = needsToPayRent,
		then tellCustomerToPayRent(c)
	
	if ∃ h in houses ∋ h.needsMaintenance,
		then askForMaintenance(h);
	
	if ∃ h in houses ∋ h.justMaintained,
		then payMaintenanceFee(h);
	
###Actions
	tellCustomerToPaySecurityDeposit(MyCustomer mc){
		mc.c.msgPaySecurityDeposit(mc.house);
		mc.cs = askedToPayDeposit;
	}
	
	tellCustomerHouseAlreadyTaken(MyCustomer mc){
		mc.c.msgHouseUnavailable();
		mc.cs = toldCantBuyProperty;
		customers.remove(mc);
	}
	
	tellCustomerToMoveIn(MyCustomer mc){
		mc.house.occupiedBy = mc.c;
		mc.c.msgMoveIn(mc.house);
		mc.cs = movedIn;
	}
	
	tellCustomerToPayRent(MyCustomer mc){
		mc.c.msgPayRent();
		mc.cs = askedToPayRent;
	}
	
	askForMaintenance(House h){
		h.maintenance.msgINeedMaintenance(h);
		h.hs = waitingForMaintenance;
	}
	
	payMaintenanceFee(House h){
		money -= h.maintenanceFee;
		h.maintenance.msgHereIsMaintenanceFee(h.maintenanceFee);
		h.hs = maintained;
	}



##Resident Role

###Data
	String name;
	double money;
	House house;
	Timer timeCook, timeEat;
	enum AgentEvent {none, hungry, makeFood, eatFood, cleanDishes, sleepy, doneCleaning, leftHouse}
	AgentEvent event = none;
	enum AgentState {DoingNothing, aboutToMakeFood, cleaningDishes, madeFood}
	AgentState state = DoingNothing;

###Messages

	public void msgGotSleepy() {//from animation
		event = sleepy;
	}
	
	public void msgGotHungry() {//from animation
		event = hungry;
	}
	
	public void msgMakeFood(String type){
		event = makeFood;
		food = type;
	}

	public void msgFoodDone(){
		event = eatFood;
	}
	
	public void msgCleanDishes(){
		event = cleanDishes;
	}

###Scheduler

	if state = DoingNothing & event = sleepy,
		then goToBed();

	if state = DoingNothing & event = hungry,
		then goToFridge();

	if state = aboutToMakeFood & event = makeFood,
		makeFood();

	if state = madeFood & event = eatFood,
		eatFood();

	if state = cleaningDishes & event = cleanDishes,
		cleanDishes();

###Actions

	goToBed(){
		DoGoToBed();
		DoLeaveHouse();
		event = leftHouse;
	}
	
	goToFridge(){
		DoGoToFridge();
		msgMakeFood(food);
	}
	
	makeFood(){
		DoGoToStove();
		state = madeFood;
		timeCook.schedule(new TimerTask() {
			public void run() {
				msgFoodDone();
			}
		},5000);
		
	}
	
	eatFood(){
		DoGoToTable();
		state = cleaningDishes;
		timeEat.schedule(new TimerTask() {
			public void run() {
				msgCleanDishes();
			}
		},5000);
		
	}
	
	cleanDishes(){
		DoGoToSink();
		DoGoToDishWasher();
		DoLeaveHouse();
		event = leftHouse;
	}


	
##Customer Role

###Data
	String name;
	double money;
	House house;
	HouseOwner owner;
	enum AgentEvent {none, wantToBuyProperty, cantBuyProperty, askedToPaySecurityDeposit, toldToMoveIn, askedToPayRent, askedToPayMortgage, paidRent, movedIn, leftOffice}
	AgentEvent event = none;
	enum AgentState {DoingNothing, waitingForResponse, paidSecurityDeposit, paidMaintenance, paidMortgage}
	AgentState state = DoingNothing;
	class House{
		Owner owner;
		Customer occupiedBy;
		MaintenanceManager maintenance;
		String type;
		int address, xCoordinate, yCoordinate;
		double monthlyPayment, securityDeposit, maintenanceFee;
		HouseState hs;
	}
	enum HouseState {maintained, needsMaintenance, waitingForMaintenance, justMaintained};


###Messages
	msgAskToBuyProperty(){
		event = wantToBuyProperty;
	}
	
	public void msgHouseUnavailable(){
		house = null;
		event = cantBuyProperty;
	}

	public void msgPaySecurityDeposit(House h){
		house = h;
		event = askedToPaySecurityDeposit;
	}
	
	public void msgMoveIn(House h){
		//if the house is of type "Home", then the Resident will take over the current owner and //become the new owner.
		//therefore, the Resident will pay the Bank mortgage and for maintenance
		if(h.type.equals("Home"))
			h.owner = null;
		//if the house is NOT of type "Home", then the Resident will NOT be the owner
		//therefore, the Resident will pay the Owner rent and for maintenance
		house = h;
		event = toldToMoveIn;
		stateChanged();
	}
	
	public void msgPayRent(){
		event = askedToPayRent;
	}

	
###Scheduler
	if state = DoingNothing & event = wantToBuyProperty,
		then askToBuyProperty();
	 
	if state = waitingForResponse & event = askedToPaySecurityDeposit,
		then paySecurityDeposity();
	 
	if state = paidSecurityDeposit & event = toldToMoveIn,
		then moveIn();
	 
	if state = DoingNothing & event = askedToPayRent,
		then payRent();

###Actions
	askToBuyProperty(){
		house.o.msgIWantToLiveHere(this);
	}
	
	paySecurityDeposit(){
		money -= house.securityDeposit;
		house.o.msgHereIsSecurityDeposit(this, house.securityDeposit;);
	}	
	
	moveIn(){
		DoMoveIn();
		event = movedIn;
	}
	
	payRent(){
		money -= house.monthlyPayment;
		house.owner.msgHereIsRent(this, house.monthlyPayment);
		event = paidRent;
	}	
	


##Maintenance Manager Role

###Data
	String name;
	double money;
	Timer t;
	List<House> houses;
	class House{
		Owner owner;
		Customer occupiedBy;
		MaintenanceManager maintenance;
		String type;
		int address, xCoordinate, yCoordinate;
		double monthlyPayment, securityDeposit, maintenanceFee;
		HouseState hs;
	}
	enum HouseState {maintained, needsMaintenance, waitingForMaintenance, justMaintained};


###Messages
	msgINeedMaintenance(House h){
		houses.add(h);
	}
	
	msgHereIsMaintenanceFee(double payment){
		money += payment;
	}
	 

###Scheduler
	if ∃ h in houses ∋ h.needsMaintenance,
		then maintainHouse(h);


###Actions
	maintainHouse(House h){
		placeWorkOrder();
		t.schedule(new TimerTask() {
			public void run() {
				h.owner.msgPayMaintenanceFee(h);
			}
		},288000);
		houses.remove(h);
	}


