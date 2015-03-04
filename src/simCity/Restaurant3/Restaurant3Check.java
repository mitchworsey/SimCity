package simCity.Restaurant3;

import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.Restaurant3.interfaces.Restaurant3Waiter;



public class Restaurant3Check {
	public enum CheckState
	{produced, distributed, paymentFinished};
	
	public Restaurant3Waiter w;
	public Restaurant3Customer c;
	public String choice;
	public double bill;
	public double change = 0;
	public CheckState cs;
	
	public Restaurant3Check(Restaurant3Waiter w, Restaurant3Customer c, String choice, double bill, CheckState cs){
		this.w = w;
		this.c = c;
		this.choice = choice;
		this.bill = bill;
		this.cs = cs;
	}
}
