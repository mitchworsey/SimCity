package simCity;

public class CityComponent {
	public CityComponent(String name, Location location, String customerRole) {
		this.name = name;
		this.location = location;
		this.customerRole = customerRole;
		this.isOpen = true;
	}
	public boolean isOpen;
	
	public void setOpen(boolean open) {
		isOpen = open;
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public String name;
	public Location location;
	public String customerRole;
}