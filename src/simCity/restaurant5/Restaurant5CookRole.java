package simCity.restaurant5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.gui.*;
import simCity.interfaces.*;
import simCity.market.MarketGrocerRole;
import simCity.restaurant5.*;
import simCity.restaurant5.MarketOrder.OrderState;
import simCity.restaurant5.gui.Restaurant5CookGui;
import simCity.restaurant5.interfaces.Restaurant5Cook;
import simCity.restaurant5.interfaces.Restaurant5Market;
import simCity.restaurant5.interfaces.Restaurant5Waiter;
import simCity.restaurant5.MarketOrder;

public class Restaurant5CookRole extends Role implements Restaurant5Cook { 
	// Notice that we implement waitingCustomers using ArrayList, but type it
		// with List semantics.
		public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
		public List<MarketOrder> ImportedOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	//	private List<Order> sharedOrderList = new ArrayList<Order>();
		
		Restaurant5SharedDataWheel sharedDataWheel;
		
		private ArrayList<String> orderMarket = new ArrayList<String>();
		
		public Restaurant5CookGui cookGui = null;
		
		// note that tables is typed with Collection semantics.
		// Later we will see how it is implemented
		public List<String> listLowFoods = Collections.synchronizedList(new ArrayList<String>());
		public List<MyMarket> myMarkets = Collections.synchronizedList(new ArrayList<MyMarket>());

		
		/* ACCESS TO MARKET GROCER */
		private Restaurant5Market market;// = new MarketAgent("Market");
		MarketGrocerRole marketGrocer;// = new MarketGrocerRole("The Market");
		//private simCity.market.MarketGrocerRole grocer;
		
		private String name;

		public enum MarketState {
			OutOfFood, NotVisited
		}
		
		private Semaphore atCook = new Semaphore(0, true);
		private Semaphore atPlate = new Semaphore(0, true);
		private Semaphore atHome = new Semaphore(0, true);
		public enum CookState {
			DoingNothing, MakingFood, PlatingFood, OrderingFood
		};
		CookState cstate = CookState.DoingNothing;
		
		Timer timer = new Timer();
		private int SteakTime = 2000;
		private int SaladTime = 2000;
		private int PizzaTime = 2000;
		private int ChickenTime = 2000;
		
		
		private int orderQuant = 10;
		
		private int MarketType = 0;

		public enum FoodState {
			Pending, Cooking, ReadyToPlate, Plated
		};
		
		private boolean wheelEmpty = true;
		
		
		Map <String, Food> CookMenu = Collections.synchronizedMap(new HashMap<String, Food>());
		
		List<String> menulist = Collections.synchronizedList(new ArrayList<String>());
		
		
		//public Restaurant
		
		public Restaurant5CookRole(String name, Restaurant5SharedDataWheel sharedWheel, MarketGrocerRole mg) {
			super();
			this.name = name;
			this.sharedDataWheel = sharedWheel;
			this.marketGrocer = mg;
			menulist.add("Steak");
			menulist.add("Salad");
			menulist.add("Pizza");
			menulist.add("Chicken");
			
			int inven = 500;
			
			Food steak = new Food("Steak", SteakTime, inven);
			Food salad = new Food("Salad", SaladTime, inven);
			Food pizza = new Food("Pizza", PizzaTime, inven);
			Food chicken = new Food("Chicken", ChickenTime, inven);
			
			this.name = name;
			//cookGui = new CookGui(this);
			CookMenu.put("Steak", steak);
			CookMenu.put("Salad", salad);
			CookMenu.put("Pizza", pizza);
			CookMenu.put("Chicken", chicken);
			

		}
		public Restaurant5CookRole(String name2) {
			// TODO Auto-generated constructor stub
		}
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#getMaitreDName()
		 */
		@Override
		public String getMaitreDName() {
			return name;
		}


		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#setGui(simCity.restaurant5.gui.Restaurant5CookGui)
		 */
		@Override
		public void setGui(Restaurant5CookGui gui) {
			cookGui = gui;
		}
		
		@Override
		public void setGrocer(MarketGrocerRole mgrocer) {
			marketGrocer = mgrocer;
		}
		
		public void setSharedDataWheel(Restaurant5SharedDataWheel sharedWheel) {
			this.sharedDataWheel = sharedWheel;
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#getName()
		 */
		@Override
		public String getName() {
			return name;
		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#getOrder()
		 */
		@Override
		public List<Order> getOrder() {
			return orders;
		}

		// Messages

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgAtCook()
		 */
		@Override
		public void msgAtCook() {
			//cstate = CookState.MakingFood;
			atCook.release();
			stateChanged();
		}
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgAtPlate()
		 */
		@Override
		public void msgAtPlate() {
			//cstate = CookState.PlatingFood;
			atPlate.release();
			stateChanged();
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgAtHome()
		 */
		@Override
		public void msgAtHome() {
			//cstate = CookState.DoingNothing;
			atHome.release();
			stateChanged();
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgHereIsOrder(simCity.restaurant5.Restaurant5WaiterRole, java.lang.String, int)
		 */
		@Override
		public void msgHereIsOrder(Restaurant5Waiter w, String choice, int table) {
			print("Cook received order.");
			orders.add(new Order(w, choice, table, FoodState.Pending));
			stateChanged();
		}
		
		@Override
		public void msgHereIsOrder() {
			print("Cook received order. - Wheel");
			wheelEmpty = false;
			stateChanged();
			
		}
		
		@Override
		public void msgHereIsOrder(List<Order> sharedOrderWheel) {
			// TODO Auto-generated method stub
			
		}
		
		// from Restaurant5WaiterRole
//		public void msgHereIsOrder(List<Order> sharedDataWheel) {
//			sharedOrderList = sharedDataWheel;
//			stateChanged();
//		}

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgFoodDone(simCity.restaurant5.Restaurant5CookRole.Order)
		 */
		@Override
		public void msgFoodDone(Order o) {
			print(o.choice + " is done cooking.");
			o.s = FoodState.ReadyToPlate;
			stateChanged();
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgOrderIsShipped(simCity.restaurant5.Restaurant5CookRole.MarketOrder, int)
		 */
		@Override
		public void msgOrderIsShipped(MarketOrder order, int quantity) {
			print("Recieved shipment of " + quantity + " " + order.foodOrder);
			ImportedOrders.add(order);
			order.s = OrderState.Shipped;
			print(order.foodOrder + " before update is " + CookMenu.get(order.foodOrder).Inventory);
			findFoodFromMap(order.foodOrder).Inventory += quantity;
			print(order.foodOrder + " is updated to " + CookMenu.get(order.foodOrder).Inventory);
			if (quantity < order.quantOrder) {
				order.s = OrderState.PartiallyAvail;
				order.shipped = quantity;
				order.quantOrder -= quantity;
			}
			else{
			order.s = OrderState.Complete;}
			
			stateChanged();
			
		}
		
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgMarketOutOfFood(simCity.restaurant5.Restaurant5MarketRole, java.lang.String)
		 */
		@Override
		public void msgMarketOutOfFood(Restaurant5Market market, String outof) {
			print("TEST");
			print(market.getName() + " is out of " + outof); // go order from another restaurant
			MarketType += 1;
			cstate = CookState.OrderingFood;
			stateChanged();
		}
		
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#addMarket(simCity.restaurant5.Restaurant5MarketRole)
		 */
		@Override
		public void addMarket(Restaurant5Market market) {
			myMarkets.add(new MyMarket(market));

		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#findMarket(simCity.restaurant5.Restaurant5MarketRole)
		 */
		@Override
		public MyMarket findMarket(Restaurant5Market market) {
			for (int i=0; i< myMarkets.size(); i++) {
				if(market == myMarkets.get(i).m) {
					return myMarkets.get(i);
				}
			}
			return null;
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#findFoodFromMap(java.lang.String)
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
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#findFoodFromOrder(java.lang.String)
		 */
		@Override
		public Order findFoodFromOrder(String order) {
			Order found = null;
			for (int i=0; i< orders.size(); i++) {
				if (order == orders.get(i).choice) {
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

			// if there exists o in orders such that o.state = done, then
			// PlateIt(o);
			// if there exists o in orders such that o.state = pending, then
			// CookIt(o);
			
			
				if (!orders.isEmpty()) {
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == FoodState.Plated) {
							RemoveIt(orders.get(i));
							return true;
						}
					}
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == FoodState.ReadyToPlate) {
							PlateIt(orders.get(i));
							return true;
						}
					}
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == FoodState.ReadyToPlate) {
							PlateIt(orders.get(i));
							return true;
						}
					}
					for (int i = 0; i< menulist.size(); i++) { // checks CookMenu inventory to check if out of food -> tell waiter
						if (menulist.get(i) == CookMenu.get(menulist.get(i)).choice) {
							if (CookMenu.get(menulist.get(i)).Inventory <= 0) {
								OutOfFood(CookMenu.get(menulist.get(i)));
								return true;
							}
						}
					}
					for (int i = 0; i < getOrder().size(); i++) {
						if (orders.get(i).s == FoodState.Pending) { // pending food state
							CookIt(orders.get(i));
							return true;
						}
					}
					for (int i = 0; i < ImportedOrders.size(); i++) {
						if (ImportedOrders.get(i).s == OrderState.PartiallyAvail) { // pending food state
							OrderFoodFromMarket(MarketType++);
							return true;
						}
					}
					if (cstate == CookState.OrderingFood) {
						if (MarketType <=2) {
							cstate = CookState.DoingNothing;
							OrderFoodFromMarket(MarketType);
							return true;
						}
					}
					if (cstate == CookState.OrderingFood) {
						if (MarketType <=2) {
							cstate = CookState.DoingNothing;
							OrderFoodFromMarket(MarketType);
							return true;
						}
					}
				return true;
				}
				
				if(!wheelEmpty) {
					clearWheel();
					return true;
				}
			
				//for ()
//				if (CookMenu.get(ord.choice).Inventory <=0) {
//					OutOfFood(CookMenu.get(ord.choice));
//					cstate = CookState.OrderingFood;
//				}
//				
				
				
				
//				Food f;
//				for( Map.Entry<String, Food> entry : CookMenu.entrySet() ) {
//					f = entry.getValue();
//					if (f.Inventory < 5) {
//						OutOfFood(f);
//						return true;
//					}
//				}
           
				// we have tried all our rules and found
				// nothing to do. So return false to main loop of abstract agent
				// and wait.
			
			return false;
		}

		// Actions

		
		private void CookIt(final Order ord) {
			if (CookMenu.get(ord.choice).Inventory <=0) {
				OutOfFood(CookMenu.get(ord.choice));
				cstate = CookState.OrderingFood;
			}
			else {
				print("Cooking " + ord.choice);
				ord.s = FoodState.Cooking; 
				

				print(Integer.toString(CookMenu.get(ord.choice).Inventory));


				if (CookMenu.get(ord.choice).Inventory <= 3) {
					print(ord.choice + " is running low!");
					listLowFoods.add(ord.choice);
					cstate = CookState.OrderingFood;
					//OrderFoodFromMarket(MarketType);

				}


				//if f.amount == 0 then tell Waiter to set out of order
				//if threshold is passed then OrderFoodFromMarket();
				//else {
				ord.w.msgGaveCookOrder();
				findFoodFromMap(ord.choice).Inventory --;
			//	DoCook();
				/*cookGui.goSomewhere();
				try{
					atCook.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				timer.schedule(new TimerTask() { // Object cookie = 1; 
					public void run() { 
						msgFoodDone(ord); // message to itself - timer goes off
						ord.s = FoodState.ReadyToPlate; 
						//DoGoHome();
						//cookGui.GoHome();
					} 
				}, (CookMenu.get(ord.choice).CookingTime));
			}
			stateChanged();
		}
	 
		private void PlateIt(Order order) {
			print(order.choice + " is being plated.");
			//DoPlate();
			/*cookGui.DoGoPlate();
			try{
				atPlate.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			order.w.msgOrderIsReady(order.choice, order.table); // Message to waiter: Come pick up your order
			order.s = FoodState.Plated;
			//orders.remove(order);
			// DoPlating(o); // animation
			stateChanged();

		}
		
		private void RemoveIt(Order order) {
			orders.remove(order);
			stateChanged();
		}

		private void OutOfFood(Food food) { // tell waiter that order cannot be cooked
			print(food.choice + "is out!");
			Order outof; 
			outof =	findFoodFromOrder(food.choice);
			outof.w.msgOutOfFood(food.choice, outof.table); // to waiter
			orders.remove(outof);
			cstate = CookState.OrderingFood;
			stateChanged();
			//food.choice;
			
			//add to inventory low list
		}
		 

		
		private void OrderFoodFromMarket(int MarketNum) {
			print("Ordering " + orderQuant + " quantities of food from Market.");// + food + "from Market.");
			//if there exists Steak
				//then steakOrder = new MarketOrder();
			//if there exists Salad
				//then saladOrder = new MarketOrder();
			//if there exists Chicken
				//then chickenOrder = new MarketOrder();
			//if there exists Pizza	
				//then pizzaOrder = new MarketOrder();
			
			
			
			
		
			listLowFoods.add("Steak");
			listLowFoods.add("Chicken");
			listLowFoods.add("Pizza");
			listLowFoods.add("Steak");
			listLowFoods.add("Steak");
			
			
			
			for (int i=0; i < listLowFoods.size(); i++) {
				switch (listLowFoods.get(i)) {
				case "Steak":
					MarketOrder steakOrder = new MarketOrder("Steak", orderQuant);
					//print(restaurant);
				//	myMarkets.get(MarketNum).m.msgCookNeedsFood(this, steakOrder);
					orderMarket.add("Steak");
					print("I need " + orderQuant + " STEAKs.");
					break;
				case "Salad":
					MarketOrder saladOrder = new MarketOrder("Salad", orderQuant);//, OrderState.Pending);
				//	myMarkets.get(MarketNum).m.msgCookNeedsFood(this, saladOrder);
					orderMarket.add("Salad");
					print("I need " + orderQuant + " SALADs.");
					break;
				case "Chicken":
					MarketOrder chickenOrder = new MarketOrder("Chicken", orderQuant);//, OrderState.Pending);
					//myMarkets.get(MarketNum).m.msgCookNeedsFood(this, chickenOrder);
					orderMarket.add("Chicken");
					print("I need " + orderQuant + " CHICKENs.");
					break;
				case "Pizza":
					MarketOrder pizzaOrder = new MarketOrder("Pizza", orderQuant);//, OrderState.Pending);
				//	myMarkets.get(MarketNum).m.msgCookNeedsFood(this, pizzaOrder);
					orderMarket.add("Pizza");
					print("I need " + orderQuant + " PIZZAs.");
					break;
				default:
					print("List of Low Foods is empty. There are no foods that are low.");
				}
				
			}
			print("MARKET IS " + marketGrocer.name);
			//marketGrocer.msgListOfOrder(5, orderMarket);
			marketGrocer.msgListOfOrder(this, orderMarket);
			
			stateChanged();
			
			
		}
		
		//////// The animation DoXYZ() routines ////////
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#DoCook()
		 */
		@Override
		public void DoCook() {
			cookGui.DoGoCook();
			try{
				atCook.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#DoPlate()
		 */
		@Override
		public void DoPlate() {
			cookGui.DoGoPlate();
			try{
				atPlate.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#DoGoHome()
		 */
		@Override
		public void DoGoHome() {
			cookGui.GoHome();
			try{
				atHome.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			}
		}
		
		
		public void clearWheel() {
			wheelEmpty = true;
			List<Order> wheelOrders = sharedDataWheel.removeAll();
			for (Order o : wheelOrders) {
				orders.add(o);
			}
			stateChanged();
		}
		
		//////////////// UTILITIES ////////////////
		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#callStateChange()
		 */
		@Override
		public void callStateChange() {
			stateChanged();
		}

		public class MyMarket {
			public Restaurant5Market m;
			MarketState ms = MarketState.NotVisited;

			MyMarket(Restaurant5Market mc){//, MarketState mss) {
				m = mc;
				//ms = mss;
			}
		}
		
		public class Order {
			Restaurant5Waiter w;
			String choice;
			int table;
			FoodState s;

			Order(Restaurant5Waiter wc, String choicec, int tablec, FoodState sc) {
				w = wc;
				choice = choicec;
				table = tablec;
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

		/* (non-Javadoc)
		 * @see simCity.restaurant5.Restaurant5Cook#msgOrderIsShipped(simCity.restaurant5.Restaurant5CookRole.MarketOrder)
		 */
		@Override
		public void msgOrderIsShipped(MarketOrder order) {
			// TODO Auto-generated method stub
			
		}
	
	
	
}
