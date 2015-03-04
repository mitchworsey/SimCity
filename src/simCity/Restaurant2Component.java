package simCity;

public class Restaurant2Component extends CityComponent {
	public Restaurant2Component(String name, Location location, String type) {
		super(name, location, "Restaurant2Customer");
		this.type = type;
	}
	//Restaurant2Host host;
	//Menu m;
	public String type;
}