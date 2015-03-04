package simCity.market;

import java.util.ArrayList;

import simCity.market.MarketGrocerRole.MyRestaurant;
import simCity.restaurant5.Restaurant5CashierRole;
import simCity.restaurant5.Restaurant5CookRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import agent.Agent;
import simCity.CityComponent;
import simCity.Role;
import simCity.gui.DeliveryTruckGui;
import simCity.interfaces.DeliveryTruck;

import simCity.restaurant5.MarketOrder;

public class MarketDeliveryTruck extends Agent implements DeliveryTruck{
	
	/* ~~~~~~ DATA ~~~~~~ */
	
	String name;
	DeliveryTruckGui gui;
	public Map<Integer, CityComponent> deliveryRoute = new HashMap<Integer, CityComponent>();
	public CityComponent thisComponent;
	public Semaphore atMarket = new Semaphore(0,true);
	public Semaphore atRestaurant = new Semaphore(0, true);
	public Semaphore atRestaurant1 = new Semaphore(0,true);
	public Semaphore atRestaurant2 = new Semaphore(0,true);
	public Semaphore atRestaurant3 = new Semaphore(0,true);
	public Semaphore atRestaurant4 = new Semaphore(0,true);
	public Semaphore atRestaurant5 = new Semaphore(0,true);
	public Semaphore unloadComplete = new Semaphore(0, true);
	public Semaphore loadingComplete = new Semaphore(0, true);
	DeliveryTruckState dtState = DeliveryTruckState.AtMarket;
	enum DeliveryTruckState { None, AtMarket, DoneLoading, Driving, AtRestaurant1, AtRestaurant2,
			AtRestaurant3, AtRestaurant4, AtRestaurant5, UnloadingProducts, DoneUnloading, 
			GivingInvoice, RecievedPayment, AtRestaurant}
	private int deliveryPayments = 0;
	private int bulk = 30;
	
	public enum OrderState{Loaded, ReadyToDeliver, Delivered, Paid};
//	List<Order> orders = new ArrayList<Order>();
//	class Order {
//		int restaurantNum;
//		String product;
//		int amount;
//		OrderState oState = OrderState.Loaded;
//		
//		public Order(int loc, String prod){
//			restaurantNum = loc;
//			product = prod;
//		//	amount = amnt;
//		}
//	}
	MarketGrocerRole mgrocer;
	ArrayList<MyRestaurant> myRests = new ArrayList<MyRestaurant>();
	ArrayList<RestaurantOrder> rOrders = new ArrayList<RestaurantOrder>();
	class RestaurantOrder {
		int restaurantNum;
		List<String> orderedProducts = new ArrayList<String>();
		OrderState oState = OrderState.Loaded;
		int bill;
		
		public RestaurantOrder(int restNum, String prod) {
			restaurantNum = restNum;
			orderedProducts.add(prod);
		}
	}
	
	public RestaurantOrder findRestOrder(int restnum) {
		for(int i=0; i<rOrders.size(); i++) {
			if (rOrders.get(i).restaurantNum == restnum){
				return rOrders.get(i);
			}
		}
		return null;
	}
	
	public boolean atMarket(){
		return atMarket.tryAcquire(); //returns false if no permits
	}
	
//	public void RoleToNum(Role r) {
//		switch(r) {
//			case
//		}
//	}
	
	
	/* ~~~~~~ CONSTRUCTORS ~~~~~~ */
	public MarketDeliveryTruck() {
		super();
	}
	
	public MarketDeliveryTruck(String MarketDTname) {
		super();
		name = MarketDTname;
	}
	
	public void setGui(DeliveryTruckGui gui) {
		this.gui = gui;
	}
	
	public void setGrocerRole(MarketGrocerRole grocer) {
		this.mgrocer = grocer;
	}
	
	public void populateMap(int index, CityComponent cc) {
		deliveryRoute.put(index, cc);
	}
	
	public String getName() {
		return name;
	}
	
/* ~~~~~ ANIMATION ~~~~~ */	
/* ~~~~~~ MESSAGES ~~~~~~ */	
	public void msgAtRestaurant() {
		print("AtRestaurant #1 called.");
		atRestaurant.release();
		dtState = DeliveryTruckState.AtRestaurant;
		stateChanged();
	}
	public void msgAtRestaurant1() {
		print("AtRestaurant #1 called.");
		atRestaurant1.release();
		dtState = DeliveryTruckState.AtRestaurant1;
		stateChanged();
	}
	public void msgAtRestaurant2() {
		print("AtRestaurant #2 called.");
		atRestaurant2.release();
		dtState = DeliveryTruckState.AtRestaurant2;
		stateChanged();
	}
	public void msgAtRestaurant3() {
		print("AtRestaurant #3 called.");
		atRestaurant3.release();
		dtState = DeliveryTruckState.AtRestaurant3;
		stateChanged();
	}
	public void msgAtRestaurant4() {
		print("AtRestaurant #4 called.");
		atRestaurant4.release();
		dtState = DeliveryTruckState.AtRestaurant4;
		stateChanged();
	}
	public void msgAtRestaurant5() {
		print("AtRestaurant #5 called.");
		atRestaurant5.release();
		dtState = DeliveryTruckState.AtRestaurant5;
		stateChanged();
	}
	public void msgAtMarket() {
		print("AtMarket called.");
		atMarket.release();
		dtState = DeliveryTruckState.AtMarket;
		stateChanged();
	}
	public void msgLoadingComplete() {
		print("LoadingComplete called.");
		loadingComplete.release();
		dtState = DeliveryTruckState.DoneLoading;
		stateChanged();
	}
	public void msgUnloadComplete() {
		print("UnloadComplete called.");
		unloadComplete.release();
		dtState = DeliveryTruckState.DoneUnloading;
		stateChanged();
	}
	
	
	

	
	
/* ~~~~~~  "REAL" ~~~~~~ */
/* ~~~~~~ MESSAGES ~~~~~~ */
	/* Grocer will "load" the delivery truck with products 
	 * and give other important information to the DT */
			/* -- MarketGrocerRole -- */
//	@Override
//	public void msgLoadProduct(int restnum, String product) {
//		rOrders.add(new RestaurantOrder(restnum, product));
//		stateChanged();
//	}
//	@Override
	public void msgLoadProducts(MarketGrocerRole mg, MyRestaurant r) {
		this.mgrocer = mg;
		myRests.add(r);
		dtState = DeliveryTruckState.DoneLoading;
		stateChanged();
	}
	
	
	/* Restaurant Cashier will give the delivery truck a payment 
	 * for the delivery of products */
		/* -- RestaurantCashierRole -- */
	@Override
	public void msgPayment(Role r, int payment) {
		deliveryPayments += payment;
		dtState = DeliveryTruckState.RecievedPayment;
		stateChanged();
	}
	


/* ~~~~~~~~~~~~~~~~~~~~~~~ */	
/* ~~~~~~ SCHEDULER ~~~~~~ */
/* ~~~~~~~~~~~~~~~~~~~~~~~ */
	/* for every order in orders, if  */
	public boolean pickAndExecuteAnAction() {
		
		/*           STEPS FOR MARKET DELIVERY TRUCK
		 * 1. Deliver list of products to specific Restaurants
		 * 2. Once unloaded all products, bill the Restaurant Cashier
		 * 3. After receiving payment, go to the next Restaurant,
		 * 		or go back to Market
		 * 
		 *  You will only be doing the above tasks, if and only if :
		 *  - The time of day is 8:00AM, or whenever the Market opens.
		 *  - Your list, myRests, of MyRestaurant's is != 0
		 * */
		//if ( TIME OF DAY IS 8:00AM/WHENEVER THE MARKET REOPENS ) {
			
			for (MyRestaurant rest : myRests) {
				if ((dtState == DeliveryTruckState.AtRestaurant)) { // && ()) {
					Deliver(rest);
				}
			}
			for (MyRestaurant rest : myRests) {
				if ((dtState == DeliveryTruckState.DoneUnloading)) { // && ()) {
					Bill(rest);
				}
			}
			for (MyRestaurant rest : myRests) {
				if ((dtState == DeliveryTruckState.RecievedPayment)) { // && ()) {
					SendProfit(rest);
				}
			}

		return false;
	}
	
	
	
/* ~~~~~~~~~~~~~~~~~~~~~~~ */	
/* ~~~~~~~ ACTIONS ~~~~~~~ */
/* ~~~~~~~~~~~~~~~~~~~~~~~ */
	
	/* ~~~ DELIVERING TO RESTAURANTS ~~~ */
	public void Deliver(MyRestaurant rest) {
		print("Delivering order for Restaurant: #" + rest.restaurant + " of " + rest.restOrder);
		//gui.DeliverToRest(rest.restaurantNum); (Restaurant5CashierRole)
		for (String order: rest.restOrder) {
			
			MarketOrder mOrd = new MarketOrder(order, bulk);
			((Restaurant5CookRole)rest.restaurant).msgOrderIsShipped( mOrd, bulk);
			
		}
		//((Restaurant5CookRole)rest.restaurant).msgOrderIsShipped( rest.restOrder, bulk);
		dtState = DeliveryTruckState.DoneUnloading;
		stateChanged();
	}
	
	
	/* ~~~ BILLING RESTAURANTS ~~~ */
	public void Bill(MyRestaurant rest) {
		print("Cashier, here is the bill for your order");
		((Restaurant5CashierRole)rest.restaurant).msgShippedOrder(this, rest.bill);
		//try{ actionComplete(); } catch(InterruptedExpection e) {e.printStackTrace()};
		
		dtState = DeliveryTruckState.RecievedPayment;
		stateChanged();
	}
	
	
	/* ~~~ SENDING PAYMENTS TO MARKET ~~~ */
	public void SendProfit(MyRestaurant rest) {
		print("Sending delivery profits to the Market.");
		mgrocer.msgUpdateRegister(rest.bill);
		dtState = DeliveryTruckState.None;
		stateChanged();
	}

	





	
	
	
	
	
	
	
}
