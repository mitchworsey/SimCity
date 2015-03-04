package simCity.house;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.Clock;
import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.gui.HouseCustomerGui;
import simCity.gui.SimCityGui;
import simCity.interfaces.HouseCustomer;
import simCity.interfaces.HouseOwner;
import simCity.interfaces.Person;

public class HouseCustomerRole extends Role implements HouseCustomer{
	String name;
	double money = 1000.00;
	House house;
	public HouseCustomerGui customerGui;
	public HouseOwner owner;
	private Semaphore leftOffice = new Semaphore(0,true);
	private Semaphore atRealEstate = new Semaphore(0,true);
	
	Timer timer = new Timer();
	
	public enum AgentEvent {none, wantToBuyProperty, cantBuyProperty, askedToPaySecurityDeposit, toldToMoveIn, askedToPayRent, askedToPayMortgage,
		paidRent, movedIn, leftOffice, almostdone, done}
	public AgentEvent event = AgentEvent.none;
	
	public enum AgentState {DoingNothing, waitingForResponse, paidSecurityDeposit, paidMaintenance, paidMortgage, Done}
	public AgentState state = AgentState.DoingNothing;
	
	
	public HouseCustomerRole(String name){
		super();
		this.name = name;
	}
	
	@Override
	public void setGui(HouseCustomerGui g) {
		customerGui = g;
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#setHouse(simCity.house.House)
	 */
	@Override
	public void setHouse(House h){
		this.house = h;
	}
	
	@Override
	public void setOwner(HouseOwner o){
		owner = o;
	}

	
	
///Messages
	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#msgAskToBuyProperty()
	 */
	@Override
	public void msgAskToBuyProperty(){
		print("Received msgAskToBuyProperty");
		state = AgentState.DoingNothing;
		event = AgentEvent.wantToBuyProperty;
		stateChanged();
	}
	
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#msgHouseUnavailable()
	 */
	@Override
	public void msgHouseUnavailable(){
		print("Received msgHouseUnavailable");
		house = null;
		event = AgentEvent.cantBuyProperty;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#msgPaySecurityDeposit()
	 */
	@Override
	public void msgPaySecurityDeposit(House h){
		print("Received msgPaySecurityDeposit");
		house = h;
		getPersonAgent().setHousingLocation(h.type);
		getPersonAgent().setTimeHouseBought(Clock.globalClock.getTime());
		event = AgentEvent.askedToPaySecurityDeposit;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#msgMoveIn(simCity.house.House)
	 */
	@Override
	public void msgMoveIn(House h){
		print("Received msgMoveIn");
		//if the house is of type "Home", then the Resident will take over the current owner and //become the new owner.
		//therefore, the Resident will pay the Bank mortgage and for maintenance
		
		//if(h.type.equals("Home"))
			//h.owner = null;
		
		//if the house is NOT of type "Home", then the Resident will NOT be the owner
		//therefore, the Resident will pay the Owner rent and for maintenance
		house = h;
		event = AgentEvent.toldToMoveIn;
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#msgPayRent()
	 */
	@Override
	public void msgPayRent(){
		print("Received msgPayRent");
		state = AgentState.DoingNothing;
		event = AgentEvent.askedToPayRent;
		stateChanged();
	}
	
	
	//Semaphore Messages
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#msgLeftOffice()
	 */
	@Override
	public void msgLeftOffice(){
		print("Received msgLeftOffice");
		leftOffice.release();
		event = AgentEvent.done;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#msgAtRealEstate()
	 */
	@Override
	public void msgAtRealEstate(){
		atRealEstate.release();
		stateChanged();
	}

	
	
///Scheduler
	/* (non-Javadoc)
	 * @see simCity.house.HouseCustomer#pickAndExecuteAnAction()
	 */
	@Override
	public boolean pickAndExecuteAnAction(){
		if(state == AgentState.DoingNothing && event == AgentEvent.wantToBuyProperty){
			state = AgentState.waitingForResponse;
			askToBuyProperty();
			return true;
		}
		
		if(state == AgentState.waitingForResponse && event == AgentEvent.askedToPaySecurityDeposit){
			state = AgentState.paidSecurityDeposit;
			paySecurityDeposit();
			return true;
		}
		
		if(state == AgentState.paidSecurityDeposit && event == AgentEvent.toldToMoveIn){
			state = AgentState.DoingNothing;
			moveIn();
			return true;
		}
		
		if(state == AgentState.DoingNothing && event == AgentEvent.askedToPayRent){
			payRent();
			return true;
		}
		
		if(state == AgentState.Done && event == AgentEvent.done){
			leaveOffice();
			return true;
		}
		
		return false;
		
	}

	
	
	
///Actions
	private void askToBuyProperty(){
		customerGui.DoGoToRealEstate();
		try {
			atRealEstate.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		owner.msgIWantToLiveHere(this);
	}
	
	private void paySecurityDeposit(){
		money -= house.securityDeposit;
		house.owner.msgHereIsSecurityDeposit(this, house.securityDeposit);
	}	
	
	private void moveIn(){
		//Gui animation to show resident has moved in
		customerGui.DoLeaveOffice();
		try {
			leftOffice.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = AgentState.Done;
		SimCityGui.housingOfficePanel.removeGui(customerGui);
		getPersonAgent().OutOfComponent(this);
		
		/////////////HACK/////////////////
		//GO TO HOUSE NOW
		
		
		//event = AgentEvent.movedIn;
	}
	
	private void payRent(){
		//getPersonAgent().OutOfComponent(this);
		
		
		///////////HACK////////////////
		//GO To housing office
		
		
		customerGui.DoGoToRealEstate();
		try {
			atRealEstate.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		money -= house.monthlyPayment;
		/*if(house.owner == null){
			//bankTeller.msgHereIsMortgage(this, house.monthlyPayment);
		}
		else{*/
			
		house.owner.msgHereIsRent(this, house.monthlyPayment);
		
		//}
		event = AgentEvent.paidRent;
		customerGui.DoLeaveOffice();
		try {
			leftOffice.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		state = AgentState.Done;
		SimCityGui.housingOfficePanel.removeGui(customerGui);
		getPersonAgent().OutOfComponent(this);
		
		
	}
	
	private void leaveOffice() {
		print("Nothing to do in office, preparing to leave office");
		event = AgentEvent.almostdone;
		timer.schedule(new TimerTask() {
			public void run() {
				actuallyLeave();
			}
		},
		2000);
	}
	
	private void actuallyLeave() {
		print("Leaving office");
		event = AgentEvent.done;
		SimCityGui.housingOfficePanel.removeGui(customerGui);
		getPersonAgent().OutOfComponent(this);
	}

}
