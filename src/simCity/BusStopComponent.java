package simCity;

import simCity.interfaces.BusStop;

public class BusStopComponent extends CityComponent {
	public BusStopComponent(BusStop b, String name, Location location, String type) {
		super(name, location, null);
		this.b = b;
		this.type = type;
	}
	public BusStop b;
	public String type;
}