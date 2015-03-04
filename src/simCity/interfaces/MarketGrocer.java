package simCity.interfaces;
import java.util.ArrayList;

import simCity.market.MarketGrocerRole.MyCustomer;

public interface MarketGrocer {

	public abstract void setGui(MarketGrocerGuiInterface mg);
	
	public abstract void setCustomer(MarketCustomer customer);

	public abstract void setCashier(MarketCashier cashier);

	public abstract String getGrocerName();

	/*~~~~~~~~~~~~~~~~~~~~~ ACCESSORS ~~~~~~~~~~~~~~~~~~*/
	public abstract void findIt(MyCustomer c, String product);

	public abstract void msgIWantStuff(MarketCustomer c, ArrayList<String> needs);

	public abstract void FindProduct(MyCustomer c, String product);

	public abstract void BringProductToCust(MyCustomer c, String product);

	public abstract void ChargeCustomer(MyCustomer c);

	public abstract void msgAtHome();

	public abstract void msgAtDropOffArea();

	public abstract void msgAtCar();

	public abstract void msgAtBread();

	public abstract void msgAtMilk();

	public abstract void msgAtEggs();

	public abstract void msgAtDT();

}
