package simCity;

public class MarketComponent extends CityComponent {
	public MarketComponent(String name, Location location, String type) {
		super(name, location, "MarketCustomer");
		this.type = type;
	}
	//MarketEmployee employee; // may or may not need this
	public String type;
}