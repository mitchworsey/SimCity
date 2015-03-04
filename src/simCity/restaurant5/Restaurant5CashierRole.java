package simCity.restaurant5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import simCity.Restaurant5Component;
import simCity.restaurant5.Restaurant5CookRole.MarketState;
import simCity.restaurant5.interfaces.Restaurant5Cashier;
import simCity.restaurant5.interfaces.Restaurant5Customer;
import simCity.restaurant5.interfaces.Restaurant5Market;
import simCity.restaurant5.interfaces.Restaurant5Waiter;
import simCity.test.mock.EventLog;
import simCity.interfaces.*;
import simCity.market.MarketDeliveryTruck;
import simCity.gui.*;
import simCity.Role;

public class Restaurant5CashierRole extends Role implements Restaurant5Cashier {
//refactor them 
	//guis and interfaces
		 
		public EventLog log = new EventLog();
		public int debt = 0;
		// Notice that we implement waitingCustomers using ArrayList, but type it
		// with List semantics.
		public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
		public List<MarketBill> bills = Collections.synchronizedList(new ArrayList<MarketBill>());
		// note that tables is typed with Collection semantics.
		// Later we will see how it is implemented
		public List<String> listLowFoods = Collections.synchronizedList(new ArrayList<String>());
		public List<MyMarket> myMarkets = Collections.synchronizedList(new ArrayList<MyMarket>());

		
		private String name;
		
		public enum CookState {
			DoingNothing, MakingFood, PlatingFood
		};

		Timer timer = new Timer();
		private int SteakTime = 2000;
		private int SaladTime = 2000;
		private int PizzaTime = 2000;
		private int ChickenTime = 2000;
		
		private int orderQuant = 10;
		private int totalAccount = 100;

		public enum OrderState {
			Pending, Calculating, ReadyForPayment, WaitingOnCustomer, ReceivedMoney, Paid
		};
		
		public enum BillState {
			Pending, ReadyForPayment,  Paid
		};
		
		
		Map <String, Integer> CashierMenu = Collections.synchronizedMap(new HashMap<String, Integer>());
		
		public List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		
		List<String> menulist = Collections.synchronizedList(new ArrayList<String>());
		
		public Restaurant5CashierRole(String name) {
			super();
			menulist.add("Steak");
			menulist.add("Salad");
			menulist.add("Pizza");
			menulist.add("Chicken");
			
			int inven = 3;
			
			Food steak = new Food("Steak", SteakTime, inven);
			Food salad = new Food("Salad", SaladTime, inven);
			Food pizza = new Food("Pizza", PizzaTime, inven);
			Food chicken = new Food("Chicken", ChickenTime, inven);
			
			this.name = name;
		
			CashierMenu.put("Steak", 16);
			CashierMenu.put("Salad", 12);
			CashierMenu.put("Pizza", 11);
			CashierMenu.put("Chicken", 9);
			

		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#getName()
		 */
		@Override
		public String getName() {
			return name;
		}

		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#getOrder()
		 */
		@Override
		public List<Order> getOrder() {
			return orders;
		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#addMarket(simCity.restaurant5.Restaurant5MarketRole)
		 */
		@Override
		public void addMarket(Restaurant5Market market) {
			myMarkets.add(new MyMarket(market));

		}
		

		
		// Messages

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#msgHereIsOrder(simCity.restaurant5.Restaurant5WaiterRole, simCity.restaurant5.Restaurant5CustomerRole, java.lang.String)
		 */
		@Override
		public void msgHereIsOrder(Restaurant5Waiter waiter, Restaurant5Customer customer, String order) {
			print("Cashier received order for a customer.");
			Order norder = new Order(waiter, customer, order, OrderState.Pending);
			orders.add(norder);
			stateChanged();
		}

		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#msgCheckCalculated(simCity.restaurant5.Restaurant5CashierRole.Order)
		 */
		@Override
		public void msgCheckCalculated(Order o) {
			print(" Order is done calculating and ready for payment");
			o.s = OrderState.ReadyForPayment;
			stateChanged();
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#msgHereIsOrderPayment(simCity.restaurant5.Restaurant5CustomerRole, int)
		 */
		@Override
		public void msgHereIsOrderPayment(Restaurant5Customer customer, int money) {
			print("Recieved " + money + " dollars from " + customer);
			//find customer's order
			Order ord = findOrderWithCustomer(customer);
			ord.s = OrderState.ReceivedMoney;
			ord.payment = money;
			stateChanged();
			
		}

//		/* FROM THE DELIVER TRUCK! */
//		public void msgBillFromMarket(MarketDeliveryTruck truck ,int bill, ArrayList<String> products) {
//			print("Received a bill of " + bill + " from " + truck.getName() +  " for " + products);
//			MarketBill nbill = new MarketBill();
//		}
//		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#msgIDoNotHaveEnoughMoney(simCity.restaurant5.Restaurant5CustomerRole, int)
		 */
		@Override
		public void msgIDoNotHaveEnoughMoney(Restaurant5Customer customer, int debt) {
			/* When customer does not have enough money...
			 * Debt goes up.
			 * Must pay next time...
			 * */
			print("... You owe us " + debt);
			myCustomers.add(new MyCustomer(customer, debt));
			stateChanged();
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#msgShippedOrder(simCity.restaurant5.Restaurant5MarketRole, int)
		 */
		@Override
		public void msgShippedOrder(MarketDeliveryTruck market, int bill) {
			print("Cashier received a bill from a restaurant.");
			MarketBill nbill = new MarketBill(market, bill, BillState.Pending);
			bills.add(nbill);
			stateChanged(); 
		}
		
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#findOrderWithCustomer(simCity.restaurant5.Restaurant5CustomerRole)
		 */
		@Override
		public Order findOrderWithCustomer(Restaurant5Customer customer) {
			Order found = null;
			for (int i=0; i< orders.size(); i++) {
				if (customer == orders.get(i).c) {
					found = orders.get(i);
					break;
				}
			}	
			return found;
		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#pickAndExecuteAnAction()
		 */
		@Override
		public boolean pickAndExecuteAnAction() {
			/*
			 * Think of this next rule as: Does there exist a table and customer, so
			 * that table is unoccupied and customer is waiting. If so seat him at
			 * the table.
			 */

			// if there exists o in orders such that o.state = done, then
			// PlateIt(o);
			// if there exists o in orders such that o.state = pending, then
			// CookIt(o);
			
			//Pending, Calculating, ReadyForPayment, ReceivedMoney, Paid
				if (!orders.isEmpty()) {
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == OrderState.Paid) {
							RemoveIt(orders.get(i));
							return true;
						}
					}
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == OrderState.ReadyForPayment) {
							CheckReady(orders.get(i));// wait for customer to come pay
							return true;
						}
					}
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == OrderState.ReceivedMoney) {
							ReceivedMoney(orders.get(i));//calculate change for customer
							return true;
						}
					}
					
				
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == OrderState.Pending) { // pending food state
							print("PENDING!");
							CalculateIt(orders.get(i));
							return true;
						}
					}
				//return true;
				}
				
				if (!bills.isEmpty()) {
					for(int i=0; i< bills.size(); i++) {
						if(bills.get(i).bs == BillState.Pending) {
							CheckIfCashierCanPay(bills.get(i));
							return true;
						}
					}
					for(int i=0; i< bills.size(); i++) {
						if(bills.get(i).bs == BillState.ReadyForPayment) {
							PayMarket(bills.get(i));
							return true;
						}
					}
				}
				// we have tried all our rules and found
				// nothing to do. So return false to main loop of abstract agent
				// and wait.
			
//				try {
//					 //->sleep // second time -> run
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			return false;
		}

		// Actions
		
		private void CalculateIt(final Order ord) {
			print("Calculating order.");
			ord.s = OrderState.Calculating;
			ord.total = CashierMenu.get(ord.choice);
			for (int i=0; i< myCustomers.size(); i++) {
				if(ord.c == myCustomers.get(i).c) {
					ord.total += myCustomers.get(i).debt;
				}
			}
			ord.s = OrderState.ReadyForPayment;
			
		}
		
		private void CheckReady(Order order) {
			print("Check is ready.");
			order.w.msgHereIsCheck(order.c, order.total);
			order.s = OrderState.WaitingOnCustomer;
			
		}
		
		private void ReceivedMoney(Order order) { // Check if money given is the same as total!
			print(order.choice + " is being paid for.");
			if(order.payment == order.total) {
				//totalAccount += order.payment;
				order.s = OrderState.Paid;
				totalAccount += order.payment;
				myCustomers.remove(order.c);
			}
			if(totalAccount > debt) {
			//	myMarkets.get(1).m.msgPayBackDebt(debt); 
			}

		}
		
		private void RemoveIt(Order order) {
			orders.remove(order);
		}

		private void CheckIfCashierCanPay(MarketBill mbill) {
			print("Checking if cashier has enough money.");
			if(totalAccount < mbill.bill){
				print("Cashier does not have enough funds!");
				// IF THE CASHIER DOES NOT HAVE ENOUGH MONEY*** EXTRA CREDIT.
				debt += mbill.bill;
				bills.remove(mbill);
			}
			else {
				mbill.bs = BillState.ReadyForPayment;
			} 
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#PayMarket(simCity.restaurant5.Restaurant5CashierRole.MarketBill)
		 */
		@Override
		public void PayMarket(MarketBill mbill) {
			print("Paying restaurant");
			//mbill.m.msgHereIsPayment(mbill.bill);
			mbill.m.msgPayment(this, mbill.bill);
			totalAccount -= mbill.bill;
			print("Restaurant total account is now: " + totalAccount);
			mbill.bs = BillState.Paid;
			stateChanged();
		} 

		//////// The animation DoXYZ() routines ////////

		
		//////////////// UTILITIES ////////////////
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cashier#callStateChange()
		 */
		@Override
		public void callStateChange() {
			stateChanged();
		}

		public class MyMarket {
			public Restaurant5Market m;
			MarketState ms;

			MyMarket(Restaurant5Market mc) {//, MarketState mss) {
				m = mc;
				//ms = mss;
			}
		}
		
		public class MyCustomer {
			public Restaurant5Customer c;
			int debt;

			MyCustomer(Restaurant5Customer cc, int debtt) {
				c = cc;
				debt = debtt;
			}

		}
		
		public class MarketBill {
			public MarketDeliveryTruck m;
			public int bill;
			public BillState bs;
			
			public MarketBill(MarketDeliveryTruck mm, int billb, BillState bss) {
				m = mm;
				bill = billb;
				bs = bss;
			}
		}
		
		public class Order {
			Restaurant5Waiter w;
			public Restaurant5Customer c;
			String choice;
			public OrderState s;
			public int total;
			public int payment;

			public Order(Restaurant5Waiter ww, Restaurant5Customer cc, String choicec, OrderState sc) {
				w = ww;
				c = cc;
				choice = choicec;
				s = sc;
			}
		}

		public class Food {
			String choice;
			int CookingTime;
			int Inventory;
			
			Food(String c, int ct, int inv) {
				choice = c;
				CookingTime = ct;
				Inventory = inv;
			}
		}

	
}
