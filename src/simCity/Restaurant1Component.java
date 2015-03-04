package simCity;

public class Restaurant1Component extends CityComponent {
	public Restaurant1Component(String name, Location location, String type) {
		super(name, location, "Restaurant1Customer");
		this.type = type;
	}
	//Restaurant2Host host;
	//Menu m;
	public String type;
}