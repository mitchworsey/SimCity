#Markets

##MarketCustomer

###Data
	int change
	int wallet
	ArrayList<Need> myNeeds = new ArrayList<>();
	Need{
		String type;
		enum needState( InNeed, GivenProduct, PaidForProduct ) 	
	}


###Scheduler 
	for every need in myNeeds {
	if state is InNeed then INeed()
	}
	for every need in myNeeds {
	if state is GivenProduct then PayForProducts()
	}
	for every need in myNeeds {
	if state is PaidForProduct then LeaveMarket()
	}
	if (change =/= 0) 
		then AddChange(int change);
	
###Messages
	msgIRetreived(String type) {
		for (n: myNeeds){
			if(type == n){
				n.needState = GivenProduct;
				stateChanged();
			}
		}
	}

	msgHereIsTotal(int total) {
		charge = total;
		stateChanged();
	}

	msgHereIsChange(int cashierChange) {
		change = Cashierchange;
		stateChanged();
	}
 
###Actions

	INeed() {
		for (n: myNeeds) {
			msgIWant(this, n.type);
		}
	}

	PayForProducts() {
		msgMyTotal&Payment(int total, int pay); 
		guiGoToCashier(); // animation
	}
 
	LeaveMarket() {
		guiLeaveMarket(); // animation
	}

	AddChange(int change) {
		wallet += change;
	}

##Grocer

###Data
	Map<String type, int price, int inventory> Products; // like menu, 		has prices and inventory

	ArrayList<MyCustomer> myCustomers = new ArrayList<>();
	MyCustomer{
		MarketCustomer c;
		enum( InNeed, BeingHelped, GivenProducts, PaidForProducts ) cState;
		ArrayList<String> customerNeeds = new ArrayList<>();

		MyCustomer(MarketCustomer cc, String type){
			c = cc;
			customerNeeds.add(type);
			int charge;
		}
	}

###Scheduler
	if there exists product in customerNeeds within myCustomers then FindProduct(product) for customer


###Messages
	msgIWant(MarketCustomer c, String type) {
		myCustomers.add(new MyCustomer(c, type));
		stateChanged();
	} 

###Actions
	FindProduct(MyCustomer c, String product) {
		guiDoFindProduct(); // animation
		//Check if inventory is not 0
		if(!Products.get(product).inventory == 0) {
			c.c.msgIRetreived(product);
			Products.get(product).inventory -= 1;
			c.charge += Product.get(product).price;
			BringProductToCust(product);
		}
		else{
			c.c.msgWeHaveNoMore(product);
			c.customerNeeds.remove(product);
			stateChanged();
		}
	}

	BringProductToCust(String product) {
		guiDoBringProductToCust(); // animation
		stateChanged();
	}

	CalculateTotal() {
		c.c.msgHereIsCharge(c.charge);
		c.customerNeeds.remove(product);
		stateChanged();
	}

##Cashier 
###Data
	ArrayList<MyCustomer> myCustomers = new ArrayList<>();
	MyCustomer {
		MarketCustomer c;
		int total
		int payment
		int change
		int debt

		MyCustomer(MarketCustomer cc, int totall, int pay) {
			total = totall;
			c = cc;
			payment = pay;
		}
	}
	int register

###Scheduler
	if there exists a customer in myCustomers, then CompareTotal&Payment(customer);

###Messages
	MyTotal&Payment(MarketCustomer c,int total, int pay) {
		myCustomers.add(new MyCustomer(c, total, pay);
		stateChanged();
	}

###Actions
	CompareTotal&Payment(MyCustomer c) {
		ifc.(payment >=c. total) {
			c.change = c.payment - c.total;
			msgHereIsChange(c.change);
			register += c.total;
		}
		else {
			print(Your payment does not cover the total charge.);
			c.debt = c.total - c.payment;
			msgYouOweUs(c.debt);
			register += c.payment;
		}
	}
