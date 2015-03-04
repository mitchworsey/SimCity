package simCity.restaurant5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import simCity.Role;
import simCity.gui.*;
import simCity.restaurant5.*;
import simCity.restaurant5.interfaces.*;
import simCity.restaurant5.MarketOrder.OrderState;

public class Restaurant5MarketRole extends Role implements Restaurant5Market { // implements Restaurant5Market{
	// Notice that we implement waitingCustomers using ArrayList, but type it
		// with List semantics.
		private List<MarketOrder> orders = Collections.synchronizedList(new ArrayList<MarketOrder>());
		// note that tables is typed with Collection semantics.
		// Later we will see how it is implemented

		Map <String, Integer> MarketMenu = Collections.synchronizedMap(new HashMap<String, Integer>());
		
		int totalAccount = 0;
		private Restaurant5Cook cook;
		private int partialOrder = 0;
		private String name;

		Timer timer = new Timer();
		private int SteakTime = 2000;
		private int SaladTime = 2000;
		private int PizzaTime = 2000;
		private int ChickenTime = 2000;
		
		private int bill = 0;
		private Restaurant5Cashier cashier;

		Map <String, Food> CookMenu = Collections.synchronizedMap(new HashMap<String, Food>());
		
		List<String> menulist = Collections.synchronizedList(new ArrayList<String>());
		
		public Restaurant5MarketRole(String name, int in) {
			
			super();
			menulist.add("Steak");
			menulist.add("Salad");
			menulist.add("Pizza");
			menulist.add("Chicken");
			
			Food steak = new Food("Steak", SteakTime, in);
			Food salad = new Food("Salad", SaladTime, in);
			Food pizza = new Food("Pizza", PizzaTime, in);
			Food chicken = new Food("Chicken", ChickenTime, in);
			
			this.name = name;
			CookMenu.put("Steak", steak);
			CookMenu.put("Salad", salad);
			CookMenu.put("Pizza", pizza);
			CookMenu.put("Chicken", chicken);
			
			MarketMenu.put("Steak", 16);
			MarketMenu.put("Salad", 12);
			MarketMenu.put("Pizza", 11);
			MarketMenu.put("Chicken", 9);

		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#setCook(simCity.restaurant5.interfaces.Restaurant5Cook)
		 */
		@Override
		public void setCook(Restaurant5Cook cook) {
			this.cook = cook;
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#setCashier(simCity.restaurant5.interfaces.Restaurant5Cashier)
		 */
		@Override
		public void setCashier(Restaurant5Cashier cashier) {
			this.cashier = cashier;
		}
	
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#getMaitreDName()
		 */
		@Override
		public String getMaitreDName() {
			return name;
		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#getName()
		 */
		@Override
		public String getName() {
			return name;
		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#getOrder()
		 */
		@Override
		public List<MarketOrder> getOrder() {
			return orders;
		}

		// Messages
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#msgCookNeedsFood(simCity.restaurant5.interfaces.Restaurant5Cook, simCity.restaurant5.Restaurant5MarketRole.MarketOrder)
		 */
//		@Override
//		public void msgCookNeedsFood(Restaurant5Cook cook, MarketOrder morder) {
//			print("Received an order request.");
//			orders.add(new MarketOrder(morder.foodOrder, morder.quantOrder, OrderState.Pending));
//			stateChanged();
//		}
//		
		 
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#msgHereIsPayment(int)
		 */
		@Override
		public void msgHereIsPayment(int payment){
			print("Received money from Cashier");
			totalAccount += payment;
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#msgPayBackDebt(int)
		 */
		@Override
		public void msgPayBackDebt(int debt) {
			print("Received money that Cashier owed from before!");
			totalAccount += debt;
		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#findFoodFromMap(java.lang.String)
		 */
		@Override
		public Food findFoodFromMap(String food) {
			int foodnum = 0;
			
			Food f = null;
			for (int i=0; i<menulist.size(); i++) {
				if (food == menulist.get(i) ) {
					foodnum = i;
				}
			}
			
			for (int i = 0; i< menulist.size(); i++) {
				if (food == CookMenu.get(menulist.get(i)).choice) {
					String s = menulist.get(i);
					f = CookMenu.get(s);
					break;
				}
			}
			return f;
		}
		
		public MarketOrder findFoodFromOrder(String order) {
			MarketOrder found = null;
			for (int i=0; i< orders.size(); i++) {
				if (order == orders.get(i).foodOrder) {
					found = orders.get(i);
					break;
				}
			}	
			return found;
		}

		/**
		 * Scheduler. Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
			/*
			 * Think of this next rule as: Does there exist a table and customer, so
			 * that table is unoccupied and customer is waiting. If so seat him at
			 * the table.
			 */

			/* if there exists o in orders such that o.state = done, then
			 * PlateIt(o);
			 * if there exists o in orders such that o.state = pending, then
			 * CookIt(o);
			*/
			   
				if (!orders.isEmpty()) {
				
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == OrderState.Available) {
							ShipIt(orders.get(i), orders.get(i).quantOrder); //full
							return true;
						}
					}
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == OrderState.PartiallyAvail) {
							PartialShipIt(orders.get(i), partialOrder); //full
							return true;
						}
					}
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == OrderState.Pending) { // pending food state
							print("PENDING");
							CheckIt(orders.get(i));
							return true;
						}
					}
				}
				
				// we have tried all our rules and found
				// nothing to do. So return false to main loop of abstract agent
				// and wait.
			
			return false;
		}

		// Actions
		
		private void CheckIt(MarketOrder ord) {
			print("Checking if " + ord.foodOrder + " is available.");
			/*if f.amount == 0 then tell Cook to set out of order */
			if (findFoodFromMap(ord.foodOrder).Inventory == 0) {
				print(ord.foodOrder + " is not available.");
				ord.s = OrderState.Unavailable;
				cook.msgMarketOutOfFood(this, ord.foodOrder);
			}
			if (findFoodFromMap(ord.foodOrder).Inventory < ord.quantOrder) {
				print(ord.foodOrder + " is low but will still supply...");
				ord.s = OrderState.PartiallyAvail;
				partialOrder = findFoodFromMap(ord.foodOrder).Inventory;
				findFoodFromMap(ord.foodOrder).Inventory = 0;

				//orders.remove(ord);
				
			}
			/*if (findFoodFromMap(ord.choice).Inventory = )
			 * if threshold is passed then OrderFoodFromMarket();
			 *else {
			 */
			else {
				findFoodFromMap(ord.foodOrder).Inventory = findFoodFromMap(ord.foodOrder).Inventory - ord.quantOrder;
				print(ord + " is available and is getting ready to ship.");
				ord.s = OrderState.Available;
			}
			stateChanged();
			
		}

		private void ShipIt(final MarketOrder order, int quant) {
			print(order.foodOrder + " is being shipped.");
			order.s = OrderState.Shipping;
			//calculate bill
			
			bill = order.quantOrder * MarketMenu.get(order.foodOrder);
			
			timer.schedule(new TimerTask() { 
				public void run() { 
					 /* go ship order to Cook*/
					cook.msgOrderIsShipped(order); // Message to cook: Shipping order
					//cashier.msgBillOrder(this, bill);
					order.s = OrderState.Shipped;
				//	orders.remove(order);
				} 
			}, (CookMenu.get(order.foodOrder).WaitingTime));
			//cashier.msgBillOrder(this, bill);
		//	cashier.msgShippedOrder(this, bill); 
			stateChanged();
			
		} 
		
		private void PartialShipIt(final MarketOrder order, final int quant) {
			print(order.foodOrder + " is being shipped, only partial quantity. Order from another restaurant.");
			order.s = OrderState.Shipping;
			//calculate bill
			
			bill = order.quantOrder * MarketMenu.get(order.foodOrder);
			
			timer.schedule(new TimerTask() { 
				public void run() { 
					 /* go ship order to Cook*/
					cook.msgOrderIsShipped(order, quant); // Message to cook: Shipping order
					//cashier.msgBillOrder(this, bill); 
					order.s = OrderState.Shipped;
				//	orders.remove(order);
				} 
			}, (CookMenu.get(order.foodOrder).WaitingTime));
			//cashier.msgBillOrder(this, bill);
		//	cashier.msgShippedOrder(this, bill); 
			stateChanged();
			
		} 
		
		//////// The animation DoXYZ() routines ////////
		/*There are none because Markets are off screen.*/
		
		//////////////// UTILITIES ////////////////
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Market#callStateChange()
		 */
		@Override
		public void callStateChange() {
			stateChanged();
		}

		public class Food {
			String choice;
			int WaitingTime;
			int Inventory;
			
			Food(String c, int ct, int inv) {
				choice = c;
				WaitingTime = ct;
				Inventory = inv;
			}
		}

		@Override
		public void msgCookNeedsFood(Restaurant5Cook cook, MarketOrder morder) {
			// TODO Auto-generated method stub
			
		}
	
}
