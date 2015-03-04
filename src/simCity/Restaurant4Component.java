package simCity;

public class Restaurant4Component extends CityComponent {
	public Restaurant4Component(String name, Location location, String type) {
		super(name, location, "Restaurant4Customer");
		this.type = type;
	}
	//Restaurant2Host host;
	//Menu m;
	public String type;
}