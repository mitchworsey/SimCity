package simCity.interfaces;

import simCity.Role;
import simCity.market.MarketGrocerRole;
import simCity.market.MarketGrocerRole.MyRestaurant;
import simCity.market.MarketDeliveryTruck;

public interface DeliveryTruck {
	
	//public abstract void msgLoadProduct(int restnum, String product);
	//public abstract void msgLoadProducts(MyRestaurant r);
	public abstract void msgPayment(Role r, int payment);
	public abstract void msgLoadProducts(MarketGrocerRole mg, MyRestaurant r);

	
}
