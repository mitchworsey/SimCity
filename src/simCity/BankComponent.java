package simCity;

public class BankComponent extends CityComponent {
	public BankComponent(String name, Location location, String type) {
		super(name, location, "BankCustomer");
		this.type = type;
	}
	//BankTeller bankTeller; // may or may not need this
	public String type;
}