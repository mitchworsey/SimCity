package simCity;

import simCity.house.House;

public class HousingOfficeComponent extends CityComponent{
	public HousingOfficeComponent(String name, Location location, String type) {
		super(name, location, "HousingCustomer");
		this.type = type;
	}
	//Owner owner; // may or may not need this
	public String type;
	public House h;
}
