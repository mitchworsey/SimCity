package simCity.market;

import simCity.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.interfaces.DeliveryTruck;
import simCity.interfaces.MarketGrocer;
import simCity.interfaces.MarketCashier;
import simCity.interfaces.MarketCustomer;
import simCity.interfaces.MarketGrocerGuiInterface;
import simCity.interfaces.Restaurant1Cook;
import simCity.restaurant2.interfaces.Restaurant2Cook;
import simCity.restaurant4.interfaces.Restaurant4Cook;
import simCity.restaurant5.interfaces.Restaurant5Cook;
import simCity.test.mock.EventLog;


public class MarketGrocerRole extends Role implements MarketGrocer {
//	public static List<MarketCustomerRole> customerLine = new ArrayList<MarketCustomerRole>();
	public String name;
	public class Stuff {
		int price =0 ;
		int inventory = 0;
		
		Stuff(int pricee, int inven) {
			price = pricee;
			inventory = inven;
		}
	}
	public class MyCustomer {
		MarketCustomer c;
		public CustomerState cState = CustomerState.InNeed;
		public ArrayList<String> customerNeeds = new ArrayList<>();
		int bill;
		
		MyCustomer(MarketCustomer cc, ArrayList<String> needs){
			c = cc;
			customerNeeds = needs;
		}
	}
	
	public class MyRestaurant {
		Role restaurant;
		int restaurantNum = 0;
		public RestaurantState rState = RestaurantState.InNeed;
		public ArrayList<String> restOrder = new ArrayList<>();
		int bill = 0;
		int payment = 0;
	
		public MyRestaurant(Role r, ArrayList<String> needs){
			restaurant = r;
			restOrder = needs;
		}
		public MyRestaurant(int rolenumber, ArrayList<String> needs){
			restaurantNum = rolenumber;
			restOrder = needs;
		}
	}

	
	
	private Semaphore atCar = new Semaphore(0, true);
	private Semaphore atBread = new Semaphore(0, true);
	private Semaphore atMilk = new Semaphore(0, true);
	private Semaphore atEggs = new Semaphore(0, true);
	private Semaphore atHome = new Semaphore(0, true);
	private Semaphore atDropOffArea = new Semaphore(0, true);
	private Semaphore atDeliveryTruck = new Semaphore(0, true);

	public EventLog log = new EventLog();
	
	private MarketGrocerGuiInterface mGrocer;

	public enum CustomerState{ None, InNeed, GivenProducts, GivenBill};
	//CustomerState cState = CustomerState.InNeed;
	public enum RestaurantState{ InNeed, GettingPickedUp, Loaded, CalculatedBill};
	
	Map<String, Stuff> Products = new HashMap<String, Stuff>(); // like menu, has prices and inventory
	public ArrayList<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	public ArrayList<MyRestaurant> myRests = new ArrayList<MyRestaurant>();
	
	MarketCustomer customer;
	MarketCashier cashier;
	
	/* ACCESS TO DELIVERY TRUCK */
	MarketDeliveryTruck deliverytruck; //simcity - agent citypanel - gui
	
	
	
	public void setGui(MarketGrocerGuiInterface mg) {
		mGrocer = mg;
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#setCustomer(restaurant2Market.MarketCustomer)
	 */
	@Override
	public void setCustomer(MarketCustomer customer) {
		this.customer = customer;
	}
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#setCashier(restaurant2Market.MarketCashier)
	 */
	@Override
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#getGrocerName()
	 */
	@Override
	public String getGrocerName() {
		return name;
	}
	
	/*  ACCESS TO DELIVERY TRUCK */
	public void setDeliveryTruck(MarketDeliveryTruck dtruck) {
		deliverytruck = dtruck;
	}
	
	public MarketGrocerRole(String name) {
		super();
		this.name = name;
		System.out.println("DeliveryTruck made");
		
		Stuff car = new Stuff( 2, 10);
		Stuff bread = new Stuff( 1, 10);
		Stuff milk = new Stuff(1, 10);
		Stuff eggs = new Stuff(1, 10);
		Stuff steak = new Stuff(1, 10);
		Stuff salad = new Stuff(1, 10);
		Stuff pizza = new Stuff(1, 10);
		Stuff chicken = new Stuff(1, 10);
		
		Products.put("Car", car);
		Products.put("Bread", bread);
		Products.put("Milk", milk);
		Products.put("Eggs", eggs);
		Products.put("Steak", steak);
		Products.put("Salad", salad);
		Products.put("Pizza", pizza);
		Products.put("Chicken", chicken);
	}
	
	public MarketGrocerRole(String name, MarketDeliveryTruck mdt) {
		super();
		this.name = name;
		this.deliverytruck = mdt;
		System.out.println("DeliveryTruck made");
		
		Stuff car = new Stuff( 2, 10);
		Stuff bread = new Stuff( 1, 10);
		Stuff milk = new Stuff(1, 10);
		Stuff eggs = new Stuff(1, 10);
		Stuff steak = new Stuff(1, 10);
		Stuff salad = new Stuff(1, 10);
		Stuff pizza = new Stuff(1, 10);
		Stuff chicken = new Stuff(1, 10);
		
		Products.put("Car", car);
		Products.put("Bread", bread);
		Products.put("Milk", milk);
		Products.put("Eggs", eggs);
		Products.put("Steak", steak);
		Products.put("Salad", salad);
		Products.put("Pizza", pizza);
		Products.put("Chicken", chicken);
	}
	
	/*~~~~~~~~~~~~~~~~~~~~~ ACCESSORS ~~~~~~~~~~~~~~~~~~*/
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#findIt(restaurant2Market.MarketGrocerRole.MyCustomer, java.lang.String)
	 */
	@Override
	public void findIt(MyCustomer c ,String product){
		for(int i=0; i<c.customerNeeds.size(); i++){
			if(product == c.customerNeeds.get(i)){
				
			}
		}
	}
	
	public void removeProduct(MyCustomer c, String product) {
		for(int i=0; i<c.customerNeeds.size(); i++){
			if(product == c.customerNeeds.get(i)){
				c.customerNeeds.remove(i);
			}
		}
	}
	
	public void selectProduct(String type) {
		switch(type) {
			case "Car": 
				mGrocer.DoGetCar();
				break;
			case "Bread":
				mGrocer.DoGetBread();
				break;
			case "Milk":
				mGrocer.DoGetMilk();
				break;
			case "Eggs":
				mGrocer.DoGetEggs();
				break;
			case "Steak":
				mGrocer.DoGetSteak();
				break;
			case "Pizza":
				mGrocer.DoGetPizza();
				break;
			case "Salad":
				mGrocer.DoGetSalad();
				break;
			case "Chicken":
				mGrocer.DoGetChicken();
				break;
			default:
				mGrocer.DoGetCar();
				break;
		}
	}
	
	
	/*~~~~~~~~~~~~~~~~~~~~~ MESSAGES ~~~~~~~~~~~~~~~~~~*/
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#msgIWantStuff(restaurant2Market.MarketCustomer, java.util.ArrayList)
	 */
	@Override
	public void msgIWantStuff(MarketCustomer c, ArrayList<String> needs) {
		print("Recieved order from Customer: " + c.getCustomerName());
		myCustomers.add(new MyCustomer(c, needs));
		print("Size of myCustomer list: " + myCustomers.size());
		stateChanged();
	}
//	
//	public void msgRestaurantNeedsStuff(Role r, String product, int quant) {
//		print("Recieved order from Restaurant: #" + r);
//		myRests.add(new MyRestaurant(r, product));
//		stateChanged();
//	}
//	
	
	public void msgListOfOrder(Role r, ArrayList<String> products) {
		print("Recieved an order from Restaurant: " + r);
		myRests.add(new MyRestaurant(r, products));
		print("List of restaurant orders is : " + myRests.size());
		stateChanged();
	}
	
//	
//	public void msgListOfOrder(int restNum, ArrayList<String> products) {
//		print("What");
//		print("Recieved an order from Restaurant: #" + restNum);
//		myRests.add(new MyRestaurant(restNum, products));
//		print("List of restaurant orders is : " + myRests.size());
//		stateChanged();
//	}
	
	public void msgUpdateRegister(int bill) {
		print("Delivery Truck sent money: " + bill);
		
		
	}
	
	/*~~~~~~~~~~~~~~~~~~~~~ GUI MESSAGES ~~~~~~~~~~~~~~~~~~~~~*/
	public void msgAtHome() {
		atHome.release();
		stateChanged();
	}
	public void msgAtCar() {
		atCar.release();
		stateChanged();
	}
	public void msgAtBread() {
		atBread.release();
		stateChanged();
	}
	public void msgAtMilk() {
		atMilk.release();
		stateChanged();
	}
	public void msgAtEggs() {
		atEggs.release();
		stateChanged();
	}
	public void msgAtDropOffArea() {
		atDropOffArea.release();
		stateChanged();
	}
	public void msgAtDT() {
		atDeliveryTruck.release();
		stateChanged();
	}	

	
	/*~~~~~~~~~~~~~~~~~~~~~ SCHEDULER ~~~~~~~~~~~~~~~~~~*/
	public boolean pickAndExecuteAnAction() {
		print("Hellooooo");
		for(int n=0; n<myCustomers.size(); n++) {
			print("hello...?");
			if(myCustomers.get(n).cState == CustomerState.InNeed){
				for(int i=0; i<myCustomers.get(n).customerNeeds.size(); i++){
					print("Scheduler: InNeed");
					FindProduct(myCustomers.get(n), myCustomers.get(n).customerNeeds.get(i));
				}
			return true;
			}
		}
		for(int n=0; n<myCustomers.size(); n++) {
			if(myCustomers.get(n).cState == CustomerState.GivenProducts){
				ChargeCustomer(myCustomers.get(n));
				return true;
			}
		}
		
		/* MarketGrocerRole ONLY loads DeliveryTruck IFF DT is atMarket */
	//	if (this.deliverytruck.atMarket()) { 
			for(int n=0; n<myRests.size(); n++) {
				if(myRests.get(n).rState == RestaurantState.InNeed){
					for(int i=0; i<myRests.get(n).restOrder.size(); i++){
						print("Scheduler: InNeed");
						FindProductr(myRests.get(n), myRests.get(n).restOrder.get(i));
					}
					return true;
				}
			}
			for(int n=0; n<myRests.size(); n++) {
				if(myRests.get(n).rState == RestaurantState.Loaded){
					ClearRestaurantOrders(myRests.get(n));
					
					// Tell DeliveryTruck to check 
					//ChargeRestaurant(myRests.get(n));
					return true;
				}
			}
	//	}
		
		return false;
	}
	
	private void ClearRestaurantOrders(MyRestaurant myRestaurant) {
		
		myRestaurant.restOrder.clear();
		
	}

	/*~~~~~~~~~~~~~~~~~~~~~ ACTIONS ~~~~~~~~~~~~~~~~~~*/
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#FindProduct(restaurant2Market.MarketGrocerRole.MyCustomer, java.lang.String)
	 */
	@Override
	public void FindProduct(MyCustomer c, String product) {
	//	guiDoFindProduct(); // animation with try-catch sempahore thing
		//Check if inventory is 0
		atHome.tryAcquire();
		print("Going to find product:" + product);
		if(!(Products.get(product).inventory == 0)) {
			atHome.tryAcquire();
			print("Inventory for " + product + " is: " + Products.get(product).inventory );
			mGrocer.DoGetCar();
//			selectProduct(product);
			try{
				atCar.acquire();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
			Products.get(product).inventory -= 1;
			print("Bill: " + c.bill);
			c.bill += Products.get(product).price;
			c.customerNeeds.remove(product);
			BringProductToCust(c, product); // animation
		}
		else {
			print("Inventory for " + product + " is zero." );
			c.c.msgWeHaveNoMore(product);
			c.customerNeeds.remove(product);
			stateChanged();
		}
		if(c.customerNeeds.size() == 0){
			c.cState = CustomerState.GivenProducts;
			stateChanged();
		}

	}
	
	public void FindProductr(MyRestaurant r, String product) {
	//	guiDoFindProduct(); // animation with try-catch sempahore thing
		//Check if inventory is 0
		atHome.tryAcquire();
		print("Sending Delivery Truck an invoice of what is about to be loaded.");
		deliverytruck.msgLoadProducts(this, r);
		print("Going to find product:" + product);
		if(!(Products.get(product).inventory == 0)) {
			print("Inventory for " + product + " is: " + Products.get(product).inventory );
			mGrocer.DoGetCar();
			try{
				atCar.acquire();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
			Products.get(product).inventory -= 1;
			print("Bill: " + r.bill);
			r.bill += Products.get(product).price;
			r.restOrder.remove(product);
			//r.restOrder.remove(product); //???????????????????????
			//BringProductToCust(r, product); // animation
			mGrocer.DoLoadProducts();
			try{ atDeliveryTruck.acquire(); } catch(InterruptedException e) {
				e.printStackTrace();
			}
			mGrocer.DoGoHome();
			
			//deliverytruck.msgLoadProduct(r.restaurantNum, product);
			//deliverytruck.msgLoadProducts(r);
			
			
		}
		
		/* Should never reach this 'else' part! 
		 * Markets do not need to worry about running out of inventory*/
		else { 
			print("Inventory for " + product + " is zero." );
			//r.c.msgWeHaveNoMore(product);
			r.restOrder.remove(product);
			stateChanged();
		}
		if(r.restOrder.size() == 0){
			r.rState = RestaurantState.Loaded;
			stateChanged();
		}

	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#BringProductToCust(restaurant2Market.MarketGrocerRole.MyCustomer, java.lang.String)
	 */
	@Override
	public void BringProductToCust(MyCustomer c, String product) {
		atHome.tryAcquire();
		print("Dropping off " + product + " for customer: " + c.c + ".");
		mGrocer.DoDropOffProducts();
		try{
			atDropOffArea.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		print("Dropped off: " + product);
		if (c.customerNeeds.size() == 0) {
			c.cState = CustomerState.GivenProducts;
			mGrocer.DoGoHome();
			try{
				atHome.acquire();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
			stateChanged();
		}
		c.c.msgIRetrieved(product);
		removeProduct(c, product);
		mGrocer.DoGoHome();
	}
	
	/* (non-Javadoc)
	 * @see restaurant2Market.MarketGrocer#ChargeCustomer(restaurant2Market.MarketGrocerRole.MyCustomer)
	 */
	@Override
	public void ChargeCustomer(MyCustomer c) {
		c.bill -= 1;
		print( c.c + ", here is your bill of " + c.bill + " dollars. Please, go to the Restaurant2Cashier to pay for your products.");
		c.c.msgHereIsBill(c.bill);
		c.cState = CustomerState.GivenBill;
		stateChanged();
	}
	
//	public void ChargeRestaurant(MyRestaurant r) {
//		r.bill -= 1;
//		deliverytruck.msgBillForRestaurant(r.restaurantNum, r.bill);
//		r.rState = RestaurantState.CalculatedBill;
//		stateChanged();
//	}


	
}
