package simCity.house;

import simCity.interfaces.HouseCustomer;
import simCity.interfaces.HouseMaintenanceManager;
import simCity.interfaces.HouseOwner;
import simCity.interfaces.HouseResident;

public class House {
	HouseOwner owner;
	HouseCustomer occupiedBy;
	HouseMaintenanceManager maintenance;
	String type;
	int address, xCoordinate, yCoordinate;
	double monthlyPayment, securityDeposit, maintenanceFee;
	public enum HouseState {maintained, needsMaintenance, waitingForMaintenance, justMaintained};
	public HouseState hs;
	
	public House(HouseOwner owner, HouseCustomer occupiedBy, HouseMaintenanceManager maintenance, String type, int address,
			int xCoordinate, int yCoordinate, double monthlyPayment, double securityDeposit, double maintenanceFee, HouseState hs){
		this.owner = owner;
		this.occupiedBy = occupiedBy;
		this.maintenance = maintenance;
		this.type = type;
		this.address = address;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.monthlyPayment = monthlyPayment;
		this.securityDeposit = securityDeposit;
		this.maintenanceFee = maintenanceFee;
		this.hs = hs;
	}

	
}
