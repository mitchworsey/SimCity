package simCity;

public class Restaurant5Component extends CityComponent {
	public Restaurant5Component(String name, Location location, String type) {
		super(name, location, "Restaurant5Customer");
		this.type = type;
	}
	//Restaurant2Host host;
	//Menu m;
	public String type;
}