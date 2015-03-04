package simCity.Restaurant3;

import simCity.Restaurant3.Restaurant3CookRole.Order;
import simCity.Restaurant3.Restaurant3CookRole.OrderState;


public class Restaurant3PCWaiter extends Restaurant3WaiterRole{

	Restaurant3ProducerConsumerMonitor sharedDataWheel;
	
	Restaurant3CookRole dummyCook = new Restaurant3CookRole("dummy");
	
	public Restaurant3PCWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public Restaurant3PCWaiter(String name, Restaurant3ProducerConsumerMonitor sharedDataWheel) {
		super(name);
		// TODO Auto-generated constructor stub
		this.sharedDataWheel = sharedDataWheel;
	}
	
	public void setSharedDataWheel(Restaurant3ProducerConsumerMonitor sharedDataWheel){
		this.sharedDataWheel = sharedDataWheel;
	}
	
	

	@Override
	protected void deliverOrderToCook(MyCustomer mc) {
		// TODO Auto-generated method stub
		print("Adding orders to shared data");
		waiterGui.createFoodGui(mc.choice, mc.table);
		waiterGui.DoGoToCookingArea();
		
		waiterGui.foodOrdered(mc.choice, mc.table);
		waiterGui.FoodGuiGoToCookingArea(mc.choice, mc.table);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Order o = ((Restaurant3CookRole) cook).new Order(this, mc.choice, mc.table, OrderState.pending);
		sharedDataWheel.insert(o);
		cook.msgHereIsOrder(this, mc.choice, mc.table);
		mc.cs = CustomerState.waitingForFoodToCook;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);

		
		
	}

}
