package simCity.Restaurant3;


public class Restaurant3NormalWaiter extends Restaurant3WaiterRole{

	public Restaurant3NormalWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	protected void deliverOrderToCook(MyCustomer mc){
		print("Called deliverOrderToCook()");
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
		cook.msgHereIsOrder(this, mc.choice, mc.table);
		mc.cs = CustomerState.waitingForFoodToCook;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}

}