package simCity;

import simCity.Restaurant1.Restaurant1CashierRole;
import simCity.Restaurant1.Restaurant1CookRole;
import simCity.Restaurant1.Restaurant1HostRole;
import simCity.Restaurant1.Restaurant1NormalWaiterRole;
import simCity.Restaurant1.Restaurant1WaiterRole;
import simCity.Restaurant3.Restaurant3CashierRole;
import simCity.Restaurant3.Restaurant3CookRole;
import simCity.Restaurant3.Restaurant3HostRole;
import simCity.Restaurant3.Restaurant3WaiterRole;
import simCity.bank.BankGuardRole;
import simCity.bank.BankTellerRole;
import simCity.gui.BankTellerGui;
import simCity.gui.PersonGui;
import simCity.gui.Restaurant1Panel;
import simCity.gui.SimCityGui;
import simCity.gui.Restaurant1.Restaurant1CookGui;
import simCity.gui.Restaurant1.Restaurant1WaiterGui;
import simCity.house.HouseCustomerRole;
import simCity.house.HouseMaintenanceManagerRole;
import simCity.house.HouseOwnerRole;
import simCity.market.MarketCashierRole;
import simCity.market.MarketDeliveryTruck;
import simCity.market.MarketGrocerRole;
import simCity.restaurant2.Restaurant2CashierRole;
import simCity.restaurant2.Restaurant2CookRole;
import simCity.restaurant2.Restaurant2HostRole;
import simCity.restaurant2.Restaurant2WaiterRole;
import simCity.restaurant4.Restaurant4CashierRole;
import simCity.restaurant4.Restaurant4CookRole;
import simCity.restaurant4.Restaurant4HostRole;
import simCity.restaurant4.Restaurant4WaiterRole;

public class CitySetUpBaseline{
	
	boolean active = false;
    
	public CitySetUpBaseline(SimCityGui gui) {
		active = true;
		
    	/** House Employees */
        OrdinaryPerson hor = new OrdinaryPerson("Steven", 15, "bus");
        hor.noPersonState();
        hor.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui horg = new PersonGui(hor, gui, 340, 500);
        SimCityGui.cityPanel.addGui(horg);
        hor.setGui(horg);
        SimCityGui.controlPanel.people.add(hor);
        Role horr = hor.RoleFactory("HousingOwner");
        hor.roles.add(horr);
        hor.startThread();
        hor.ToHousingOffice(horr);
        ((HouseOwnerRole) horr).createHouses();
        SimCityGui.housingOfficePanel.setOwner((HouseOwnerRole) horr);
        
        
        OrdinaryPerson hmm = new OrdinaryPerson("Lucas", 100, "walk");
        hmm.noPersonState();
        hmm.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui hmmg = new PersonGui(hmm, gui, 320, 500);
        SimCityGui.cityPanel.addGui(hmmg);
        hmm.setGui(hmmg);
        SimCityGui.controlPanel.people.add(hmm);
        Role hmmr = hmm.RoleFactory("HousingMaintenanceManager");
        hmm.roles.add(hmmr);
        hmm.startThread();
        hmm.ToHousingOffice(hmmr);
        ((HouseOwnerRole) horr).setMaintenanceManager((HouseMaintenanceManagerRole) hmmr);
        SimCityGui.housingOfficePanel.setMaitenanceManager((HouseMaintenanceManagerRole) hmmr);
        //((HouseCustomerRole) r).msgAskToBuyProperty();

		/** Restaurant1 Employees */
        OrdinaryPerson r1w = new OrdinaryPerson("Kyle", 15, "bus");
        r1w.noPersonState();
        r1w.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui r1wg = new PersonGui(r1w, gui, 360, 500);
        SimCityGui.cityPanel.addGui(r1wg);
        r1w.setGui(r1wg);
        SimCityGui.controlPanel.people.add(r1w);
        Role r1wr = r1w.RoleFactory("Restaurant1Waiter");
        r1w.roles.add(r1wr);
        r1w.startThread();
        r1w.ToRestaurant1(r1wr);
        
        OrdinaryPerson r1w2 = new OrdinaryPerson("Kyle1", 15);
        r1w2.noPersonState();
        r1w2.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui r1w2g = new PersonGui(r1w2, gui, 360, 500);
        SimCityGui.cityPanel.addGui(r1w2g);
        r1w2.setGui(r1w2g);
        SimCityGui.controlPanel.people.add(r1w2);
        Role r1w2r = r1w2.RoleFactory("Restaurant1Waiter");
        r1w2.roles.add(r1w2r);
        r1w2.startThread();
        r1w2.ToRestaurant1(r1wr);
        
        OrdinaryPerson r1w3 = new OrdinaryPerson("Kyle2", 15, "bus");
        r1w3.noPersonState();
        r1w3.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui r1w3g = new PersonGui(r1w3, gui, 360, 500);
        SimCityGui.cityPanel.addGui(r1w3g);
        r1w3.setGui(r1w3g);
        SimCityGui.controlPanel.people.add(r1w3);
        Role r1w3r = r1w3.RoleFactory("Restaurant1Waiter");
        r1w3r.setPersonAgent(r1w3);
        r1w3.roles.add(r1w3r);
        r1w3.startThread();
        r1w3.ToRestaurant1(r1wr);

        OrdinaryPerson r1w4 = new OrdinaryPerson("Kyle3", 15);
        r1w4.noPersonState();
        r1w4.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui r1w4g = new PersonGui(r1w4, gui, 320, 500);
        SimCityGui.cityPanel.addGui(r1w4g);
        r1w4.setGui(r1w4g);
        SimCityGui.controlPanel.people.add(r1w4);
        Role r1w4r = r1w4.RoleFactory("Restaurant1Waiter");
        r1w4.roles.add(r1w4r);
        r1w4.startThread();
        r1w4.ToRestaurant1(r1wr);
        
        OrdinaryPerson r1h = new OrdinaryPerson("Stacy", 15, "bus");
        r1h.noPersonState();
        r1h.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui r1hg = new PersonGui(r1h, gui, 470, 400);
        SimCityGui.cityPanel.addGui(r1hg);
        r1h.setGui(r1hg);
        SimCityGui.controlPanel.people.add(r1h);
        Role r1hr = r1h.RoleFactory("Restaurant1Host");
        r1h.roles.add(r1hr);
        r1h.startThread();
        r1h.ToRestaurant1(r1hr);
        Restaurant1Panel.setHost((Restaurant1HostRole)r1hr);

        OrdinaryPerson r1ck = new OrdinaryPerson("Ben", 15);
        r1ck.noPersonState();
        r1ck.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui r1ckg = new PersonGui(r1ck, gui, 520, 400);
        SimCityGui.cityPanel.addGui(r1ckg);
        r1ck.setGui(r1ckg);
        SimCityGui.controlPanel.people.add(r1ck);
        Role r1ckr = r1ck.RoleFactory("Restaurant1Cook");
        r1ck.roles.add(r1ckr);
        r1ck.startThread();
        r1ck.ToRestaurant1(r1ckr);

        OrdinaryPerson r1cs = new OrdinaryPerson("Jen", 15, "bus");
        r1cs.noPersonState();
        r1cs.addComponents(SimCityGui.controlPanel.buildingEntrances);
        PersonGui r1csg = new PersonGui(r1cs, gui, 550, 400);
        SimCityGui.cityPanel.addGui(r1csg);
        r1cs.setGui(r1csg);
        SimCityGui.controlPanel.people.add(r1cs);
        Role r1csr = r1cs.RoleFactory("Restaurant1Cashier");
        r1cs.roles.add(r1csr);
        r1cs.startThread();
        r1cs.ToRestaurant1(r1csr);
        
        ((Restaurant1HostRole) r1hr).addWaiters((Restaurant1WaiterRole) r1wr);
        ((Restaurant1HostRole) r1hr).addWaiters((Restaurant1WaiterRole) r1w2r);
        ((Restaurant1HostRole) r1hr).addWaiters((Restaurant1WaiterRole) r1w3r);
        ((Restaurant1HostRole) r1hr).addWaiters((Restaurant1WaiterRole) r1w4r);
    	
    	((Restaurant1CashierRole) r1csr).addWaiters((Restaurant1WaiterRole) r1wr);
    	((Restaurant1CashierRole) r1csr).addWaiters((Restaurant1WaiterRole) r1w2r);
    	((Restaurant1CashierRole) r1csr).addWaiters((Restaurant1WaiterRole) r1w3r);
    	((Restaurant1CashierRole) r1csr).addWaiters((Restaurant1WaiterRole) r1w4r);
    	
    	((Restaurant1CookRole) r1ckr).addWaiters((Restaurant1WaiterRole) r1wr);
    	((Restaurant1CookRole) r1ckr).addWaiters((Restaurant1WaiterRole) r1w2r);
    	((Restaurant1CookRole) r1ckr).addWaiters((Restaurant1WaiterRole) r1w3r);
    	((Restaurant1CookRole) r1ckr).addWaiters((Restaurant1WaiterRole) r1w4r);
    	
    	/** Bank Employees */
    	OrdinaryPerson b1t = new OrdinaryPerson("Betsy", 500); 
    	b1t.noPersonState();
    	b1t.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui b1tg = new PersonGui(b1t, gui, 550, 450);
    	SimCityGui.cityPanel.addGui(b1tg);
    	b1t.setGui(b1tg);
    	SimCityGui.controlPanel.people.add(b1t);
    	Role b1tr = b1t.RoleFactory("BankTeller");
		b1t.roles.add(b1tr);
    	b1t.startThread();
    	SimCityGui.bankPanel.setTeller((BankTellerRole) b1tr);
    	b1t.ToBank(b1tr);
    	
    	OrdinaryPerson b1g = new OrdinaryPerson("Vincent", 500, "bus"); 
    	b1g.noPersonState();
    	b1g.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui b1gg = new PersonGui(b1g, gui, 600, 500);
    	SimCityGui.cityPanel.addGui(b1gg);
    	b1g.setGui(b1gg);
    	SimCityGui.controlPanel.people.add(b1g);
    	Role b1gr = b1g.RoleFactory("BankGuard");
    	((BankGuardRole) b1gr).setTeller((BankTellerRole) b1tr);
		b1g.roles.add(b1gr);
    	b1g.startThread();
    	SimCityGui.bankPanel.setGuard((BankGuardRole) b1gr);
    	b1g.ToBank(b1gr);
    	
    	/** Bank1 Employees */
    	OrdinaryPerson b1t2 = new OrdinaryPerson("Gina", 500, "bus"); 
    	b1t2.noPersonState();
    	b1t2.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui b1tg2 = new PersonGui(b1t2, gui, 600, 400);
    	SimCityGui.cityPanel.addGui(b1tg2);
    	b1t2.setGui(b1tg2);
    	SimCityGui.controlPanel.people.add(b1t2);
    	Role b1tr2 = b1t2.RoleFactory("Bank1Teller");
		b1t2.roles.add(b1tr2);
    	b1t2.startThread();
    	SimCityGui.bank1Panel.setTeller((BankTellerRole) b1tr2);
    	b1t2.ToBank(b1tr2);
    	
    	/*OrdinaryPerson b1g2 = new OrdinaryPerson("Carmen", 500, "bus"); 
    	b1g2.noPersonState();
    	b1g2.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui b1gg2 = new PersonGui(b1g2, gui, 700, 550);
    	SimCityGui.cityPanel.addGui(b1gg2);
    	b1g2.setGui(b1gg2);
    	SimCityGui.controlPanel.people.add(b1g2);
    	Role b1gr2 = b1g2.RoleFactory("Bank1Guard");
    	((BankGuardRole) b1gr2).setTeller((BankTellerRole) b1tr2);
		b1g2.roles.add(b1gr2);
    	b1g2.startThread();
    	SimCityGui.bank1Panel.setGuard((BankGuardRole) b1gr2);
    	b1g2.ToBank(b1gr2);*/
    	
    	/** Market Employees */ 
    	OrdinaryPerson m1g = new OrdinaryPerson("Kevin", 500, "bus");
    	m1g.noPersonState();
    	m1g.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui m1gg = new PersonGui(m1g, gui, 500, 450);
    	SimCityGui.cityPanel.addGui(m1gg);
    	m1g.setGui(m1gg);
    	SimCityGui.controlPanel.people.add(m1g);
    	
    	MarketDeliveryTruck mdTruck = new MarketDeliveryTruck("DeliveryTruck");
    	mdTruck.startThread();
    	SimCityGui.marketPanel.setDeliveryTruck(mdTruck);
    	Role m1gr = m1g.RoleFactory("MarketGrocer");
    	m1g.roles.add(m1gr);
    	m1g.startThread();
    	SimCityGui.marketPanel.setGrocer((MarketGrocerRole) m1gr);
    	m1g.ToMarket(m1gr);
    	
    	OrdinaryPerson m1c = new OrdinaryPerson("Laura", 500, "bus");
    	m1c.noPersonState();
    	m1c.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui m1cg = new PersonGui(m1g, gui, 500, 450);
    	SimCityGui.cityPanel.addGui(m1cg);
    	m1c.setGui(m1cg);
    	SimCityGui.controlPanel.people.add(m1c);
    	Role m1cr = m1c.RoleFactory("MarketCashier");
    	m1c.roles.add(m1cr);
    	m1c.startThread();
    	SimCityGui.marketPanel.setCashier((MarketCashierRole) m1cr);
    	m1c.ToMarket(m1cr);
    	
    	
    	((MarketGrocerRole) m1gr).setCashier((MarketCashierRole) m1cr);
        ((MarketCashierRole) m1cr).setGrocer((MarketGrocerRole) m1gr);
        ((MarketGrocerRole) m1gr).setDeliveryTruck(SimCityGui.marketPanel.mdTruck);
        ((MarketCashierRole) m1cr).setDeliveryTruck(SimCityGui.marketPanel.mdTruck);
    	
    	/** Restaurant2 Employees */ 
    	
    	OrdinaryPerson r2h = new OrdinaryPerson("Sarah", 100);
    	r2h.noPersonState();
    	r2h.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r2hg = new PersonGui(r2h, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r2hg);
    	r2h.setGui(r2hg);
    	SimCityGui.controlPanel.people.add(r2h);
    	Role r2hr = r2h.RoleFactory("Restaurant2Host");
    	r2h.roles.add(r2hr);
    	r2h.startThread();
    	r2h.ToRestaurant2(r2hr);
    	SimCityGui.restaurant2Panel.setHost((Restaurant2HostRole) r2hr);
    	
    	OrdinaryPerson r2ck = new OrdinaryPerson("Jake", 100, "bus");
    	r2ck.noPersonState();
    	r2ck.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r2ckg = new PersonGui(r2ck, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r2ckg);
    	r2ck.setGui(r2ckg);
    	SimCityGui.controlPanel.people.add(r2ck);
    	Role r2ckr = r2ck.RoleFactory("Restaurant2Cook");
    	r2ck.roles.add(r2ckr);
    	r2ck.startThread();
    	r2ck.ToRestaurant2(r2ckr);

    	OrdinaryPerson r2cs = new OrdinaryPerson("Will", 100);
    	r2cs.noPersonState();
    	r2cs.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r2csg = new PersonGui(r2cs, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r2csg);
    	r2cs.setGui(r2csg);
    	SimCityGui.controlPanel.people.add(r2cs);
    	Role r2csr = r2cs.RoleFactory("Restaurant2Cashier");
    	r2cs.roles.add(r2csr);
    	r2cs.startThread();
    	r2cs.ToRestaurant2(r2csr);
    	SimCityGui.restaurant2Panel.setCashier((Restaurant2CashierRole) r2csr);

    	OrdinaryPerson r2w1 = new OrdinaryPerson("Joshua", 100, "bus");
    	r2w1.noPersonState();
    	r2w1.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r2w1g = new PersonGui(r2w1, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r2w1g);
    	r2w1.setGui(r2w1g);
    	SimCityGui.controlPanel.people.add(r2w1);
    	Role r2w1r = r2w1.RoleFactory("Restaurant2Waiter");
    	r2w1.roles.add(r2w1r);
    	r2w1.startThread();
    	r2w1.ToRestaurant2(r2w1r);

    	OrdinaryPerson r2w2 = new OrdinaryPerson("Poe", 100);
    	r2w2.noPersonState();
    	r2w2.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r2w2g = new PersonGui(r2w2, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r2w2g);
    	r2w2.setGui(r2w2g);
    	SimCityGui.controlPanel.people.add(r2w2);
    	Role r2w2r = r2w2.RoleFactory("Restaurant2Waiter");
    	r2w2.roles.add(r2w2r);
    	r2w2.startThread();
    	r2w2.ToRestaurant2(r2w2r);

    	OrdinaryPerson r2w3 = new OrdinaryPerson("Leory", 100);
    	r2w3.noPersonState();
    	r2w3.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r2w3g = new PersonGui(r2w3, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r2w3g);
    	r2w3.setGui(r2w3g);
    	SimCityGui.controlPanel.people.add(r2w3);
    	Role r2w3r = r2w3.RoleFactory("Restaurant2Waiter");
    	r2w3.roles.add(r2w3r);
    	r2w3.startThread();
    	r2w3.ToRestaurant2(r2w3r);

    	OrdinaryPerson r2w4 = new OrdinaryPerson("Benjamin", 100, "walk");
    	r2w4.noPersonState();
    	r2w4.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r2w4g = new PersonGui(r2w4, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r2w4g);
    	r2w4.setGui(r2w4g);
    	SimCityGui.controlPanel.people.add(r2w4);
    	Role r2w4r = r2w4.RoleFactory("Restaurant2Waiter");
    	r2w4.roles.add(r2w4r);
    	r2w4.startThread();
    	r2w4.ToRestaurant2(r2w4r);
    	
    	((Restaurant2HostRole) r2hr).addWaiter((Restaurant2WaiterRole)r2w1r);
    	((Restaurant2HostRole) r2hr).addWaiter((Restaurant2WaiterRole)r2w2r);
    	((Restaurant2HostRole) r2hr).addWaiter((Restaurant2WaiterRole)r2w3r);
    	((Restaurant2HostRole) r2hr).addWaiter((Restaurant2WaiterRole)r2w4r);
    	
    	((Restaurant2WaiterRole) r2w1r).setCook((Restaurant2CookRole) r2ckr);
    	((Restaurant2WaiterRole) r2w1r).setHost((Restaurant2HostRole) r2hr);
    	((Restaurant2WaiterRole) r2w1r).setCashier((Restaurant2CashierRole) r2csr);
    	
    	((Restaurant2WaiterRole) r2w2r).setCook((Restaurant2CookRole) r2ckr);
    	((Restaurant2WaiterRole) r2w2r).setHost((Restaurant2HostRole) r2hr);
    	((Restaurant2WaiterRole) r2w2r).setCashier((Restaurant2CashierRole) r2csr);
    	
    	((Restaurant2WaiterRole) r2w3r).setCook((Restaurant2CookRole) r2ckr);
    	((Restaurant2WaiterRole) r2w3r).setHost((Restaurant2HostRole) r2hr);
    	((Restaurant2WaiterRole) r2w3r).setCashier((Restaurant2CashierRole) r2csr);
    	
    	((Restaurant2WaiterRole) r2w4r).setCook((Restaurant2CookRole) r2ckr);
    	((Restaurant2WaiterRole) r2w4r).setHost((Restaurant2HostRole) r2hr);
    	((Restaurant2WaiterRole) r2w4r).setCashier((Restaurant2CashierRole) r2csr);
    	
    	/** Restaurant 3 Employees */
    	
    	OrdinaryPerson r3h = new OrdinaryPerson("Sarah", 100);
    	r3h.noPersonState();
    	r3h.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r3hg = new PersonGui(r3h, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r3hg);
    	r3h.setGui(r3hg);
    	SimCityGui.controlPanel.people.add(r3h);
    	Role r3hr = r3h.RoleFactory("Restaurant3Host");
    	r3h.roles.add(r3hr);
    	r3h.startThread();
    	r3h.ToRestaurant3(r3hr);
    	
    	OrdinaryPerson r3ck = new OrdinaryPerson("Ken", 100, "walk");
    	r3ck.noPersonState();
    	r3ck.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r3ckg = new PersonGui(r3ck, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r3ckg);
    	r3ck.setGui(r3ckg);
    	SimCityGui.controlPanel.people.add(r3ck);
    	Role r3ckr = r3ck.RoleFactory("Restaurant3Cook");
    	r3ck.roles.add(r3ckr);
    	r3ck.startThread();
    	r3ck.ToRestaurant3(r3ckr);

    	OrdinaryPerson r3cs = new OrdinaryPerson("Will", 100);
    	r3cs.noPersonState();
    	r3cs.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r3csg = new PersonGui(r3cs, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r3csg);
    	r3cs.setGui(r3csg);
    	SimCityGui.controlPanel.people.add(r3cs);
    	Role r3csr = r3cs.RoleFactory("Restaurant3Cashier");
    	r3cs.roles.add(r3csr);
    	r3cs.startThread();
    	r3cs.ToRestaurant3(r3csr);

    	OrdinaryPerson r3w1 = new OrdinaryPerson("Joshua", 100, "walk");
    	r3w1.noPersonState();
    	r3w1.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r3w1g = new PersonGui(r3w1, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r3w1g);
    	r3w1.setGui(r3w1g);
    	SimCityGui.controlPanel.people.add(r3w1);
    	Role r3w1r = r3w1.RoleFactory("Restaurant3Waiter");
    	r3w1.roles.add(r3w1r);
    	r3w1.startThread();
    	r3w1.ToRestaurant3(r3w1r);

    	OrdinaryPerson r3w2 = new OrdinaryPerson("Kendall", 100);
    	r3w2.noPersonState();
    	r3w2.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r3w2g = new PersonGui(r3w2, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r3w2g);
    	r3w2.setGui(r3w2g);
    	SimCityGui.controlPanel.people.add(r3w2);
    	Role r3w2r = r3w2.RoleFactory("Restaurant3Waiter");
    	r3w2.roles.add(r3w2r);
    	r3w2.startThread();
    	r3w2.ToRestaurant3(r3w2r);

    	OrdinaryPerson r3w3 = new OrdinaryPerson("Leory", 100, "walk");
    	r3w3.noPersonState();
    	r3w3.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r3w3g = new PersonGui(r3w3, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r3w3g);
    	r3w3.setGui(r3w3g);
    	SimCityGui.controlPanel.people.add(r3w3);
    	Role r3w3r = r3w3.RoleFactory("Restaurant3Waiter");
    	r3w3.roles.add(r3w3r);
    	r3w3.startThread();
    	r3w3.ToRestaurant3(r3w3r);

    	OrdinaryPerson r3w4 = new OrdinaryPerson("Benjamin", 100, "walk");
    	r3w4.noPersonState();
    	r3w4.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r3w4g = new PersonGui(r3w4, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r3w4g);
    	r3w4.setGui(r3w4g);
    	SimCityGui.controlPanel.people.add(r3w4);
    	Role r3w4r = r3w4.RoleFactory("Restaurant3Waiter");
    	r3w4.roles.add(r3w4r);
    	r3w4.startThread();
    	r3w4.ToRestaurant3(r3w4r);
    	
    	((Restaurant3CookRole) r3ckr).setCashier((Restaurant3CashierRole) r3csr);
    	((Restaurant3WaiterRole) r3w1r).setCook((Restaurant3CookRole) r3ckr);
    	((Restaurant3WaiterRole) r3w1r).setHost((Restaurant3HostRole) r3hr);
    	((Restaurant3WaiterRole) r3w1r).setCashier((Restaurant3CashierRole) r3csr);
    	
    	((Restaurant3WaiterRole) r3w2r).setCook((Restaurant3CookRole) r3ckr);
    	((Restaurant3WaiterRole) r3w2r).setHost((Restaurant3HostRole) r3hr);
    	((Restaurant3WaiterRole) r3w2r).setCashier((Restaurant3CashierRole) r3csr);
    	
    	((Restaurant3WaiterRole) r3w3r).setCook((Restaurant3CookRole) r3ckr);
    	((Restaurant3WaiterRole) r3w3r).setHost((Restaurant3HostRole) r3hr);
    	((Restaurant3WaiterRole) r3w3r).setCashier((Restaurant3CashierRole) r3csr);
    	
    	((Restaurant3WaiterRole) r3w4r).setCook((Restaurant3CookRole) r3ckr);
    	((Restaurant3WaiterRole) r3w4r).setHost((Restaurant3HostRole) r3hr);
    	((Restaurant3WaiterRole) r3w4r).setCashier((Restaurant3CashierRole) r3csr);
    			
		((Restaurant3CookRole) r3ckr).setCashier((Restaurant3CashierRole) r3csr);
		
		((Restaurant3CookRole) r3ckr).setWaiterName("Joshua");
		
		((Restaurant3HostRole) r3hr).addWaiters((Restaurant3WaiterRole) r3w1r);
    	((Restaurant3HostRole) r3hr).addWaiters((Restaurant3WaiterRole) r3w2r);
    	((Restaurant3HostRole) r3hr).addWaiters((Restaurant3WaiterRole) r3w3r);
    	((Restaurant3HostRole) r3hr).addWaiters((Restaurant3WaiterRole) r3w4r);
    	
    	SimCityGui.restaurant3Panel.setHost((Restaurant3HostRole) r3hr);
    	SimCityGui.restaurant3Panel.setCashier((Restaurant3CashierRole) r3csr);
    	
    	/** Restaurant 4 employees */
    	
    	OrdinaryPerson r4h = new OrdinaryPerson("Sarah", 100, "bus");
    	r4h.noPersonState();
    	r4h.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r4hg = new PersonGui(r4h, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r4hg);
    	r4h.setGui(r4hg);
    	SimCityGui.controlPanel.people.add(r4h);
    	Role r4hr = r4h.RoleFactory("Restaurant4Host");
    	r4h.roles.add(r4hr);
    	r4h.startThread();
    	r4h.ToRestaurant4(r4hr);
    	SimCityGui.restaurant4Panel.h1 = (Restaurant4HostRole) r4hr;
    	
    	OrdinaryPerson r4ck = new OrdinaryPerson("Ken", 100, "walk");
    	r4ck.noPersonState();
    	r4ck.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r4ckg = new PersonGui(r4ck, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r4ckg);
    	r4ck.setGui(r4ckg);
    	SimCityGui.controlPanel.people.add(r4ck);
    	Role r4ckr = r4ck.RoleFactory("Restaurant4Cook");
    	r4ck.roles.add(r4ckr);
    	r4ck.startThread();
    	r4ck.ToRestaurant4(r4ckr);
    	((Restaurant4HostRole) r4hr).setCook((Restaurant4CookRole) r4ckr);
    	SimCityGui.restaurant4Panel.ck = (Restaurant4CookRole) r4ckr;

    	OrdinaryPerson r4cs = new OrdinaryPerson("Will", 100, "bus");
    	r4cs.noPersonState();
    	r4cs.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r4csg = new PersonGui(r4cs, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r4csg);
    	r4cs.setGui(r4csg);
    	SimCityGui.controlPanel.people.add(r4cs);
    	Role r4csr = r4cs.RoleFactory("Restaurant4Cashier");
    	r4cs.roles.add(r4csr);
    	r4cs.startThread();
    	r4cs.ToRestaurant4(r4csr);
    	SimCityGui.restaurant4Panel.ca = (Restaurant4CashierRole) r4csr;

    	OrdinaryPerson r4w1 = new OrdinaryPerson("Joshua", 100, "walk");
    	r4w1.noPersonState();
    	r4w1.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r4w1g = new PersonGui(r4w1, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r4w1g);
    	r4w1.setGui(r4w1g);
    	SimCityGui.controlPanel.people.add(r4w1);
    	Role r4w1r = r4w1.RoleFactory("Restaurant4Waiter");
    	r4w1.roles.add(r4w1r);
    	r4w1.startThread();
    	r4w1.ToRestaurant4(r4w1r);
    	((Restaurant4WaiterRole) r4w1r).setHost((Restaurant4HostRole) r4hr);
    	((Restaurant4WaiterRole) r4w1r).setCashier((Restaurant4CashierRole) r4csr);
    	((Restaurant4WaiterRole) r4w1r).setCook((Restaurant4CookRole) r4ckr);

    	OrdinaryPerson r4w2 = new OrdinaryPerson("Kendall", 100);
    	r4w2.noPersonState();
    	r4w2.addComponents(SimCityGui.controlPanel.buildingEntrances);
    	PersonGui r4w2g = new PersonGui(r4w2, gui, 400, 450);
    	SimCityGui.cityPanel.addGui(r4w2g);
    	r4w2.setGui(r4w2g);
    	SimCityGui.controlPanel.people.add(r4w2);
    	Role r4w2r = r4w2.RoleFactory("Restaurant4Waiter");
    	r4w2.roles.add(r4w2r);
    	r4w2.startThread();
    	r4w2.ToRestaurant4(r4w2r);
    	((Restaurant4WaiterRole) r4w2r).setHost((Restaurant4HostRole) r4hr);
    	((Restaurant4WaiterRole) r4w2r).setCashier((Restaurant4CashierRole) r4csr);
    	((Restaurant4WaiterRole) r4w2r).setCook((Restaurant4CookRole) r4ckr);
    	 	
	}
	
	public boolean getActive() {
		return active;
	}
}