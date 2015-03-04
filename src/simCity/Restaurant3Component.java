package simCity;

public class Restaurant3Component extends CityComponent {
	public Restaurant3Component(String name, Location location, String type) {
		super(name, location, "Restaurant3Customer");
		this.type = type;
	}
	//Restaurant2Host host;
	//Menu m;
	public String type;
}