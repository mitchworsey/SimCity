package simCity.house;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.gui.HouseMaintenanceManagerGui;
import simCity.house.House.HouseState;
import simCity.interfaces.HouseMaintenanceManager;
import simCity.interfaces.Person;

public class HouseMaintenanceManagerRole extends Role implements HouseMaintenanceManager {
	HouseMaintenanceManagerGui maintenanceGui;
	String name;
	Timer t = new Timer();
	double money = 500.00;
	List<House> houses = new ArrayList<House>();
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atStove = new Semaphore(0,true);
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atBed = new Semaphore(0,true);
	private Semaphore atShower = new Semaphore(0,true);
	private Semaphore atWasher = new Semaphore(0,true);
	private Semaphore atDishWasher = new Semaphore(0,true);
	private Semaphore atSink = new Semaphore(0,true);
	private Semaphore leftHouse = new Semaphore(0,true);
	private Semaphore leftOffice = new Semaphore(0,true);
	private Semaphore atMaintenance = new Semaphore(0,true);
	private Semaphore placedWorkOrder = new Semaphore(0,true);
	
	public HouseMaintenanceManagerRole(String name){
		super();
		this.name = name;
	}
	
	public void setGui(HouseMaintenanceManagerGui gui){
		maintenanceGui = gui;
	}
	
	

	
///Messages
	/* (non-Javadoc)
	 * @see simCity.house.HouseMaintenanceManager#msgINeedMaintenance(simCity.house.House)
	 */
	@Override
	public void msgINeedMaintenance(House h){
		print("Received msgINeedMaintenance");
		houses.add(h);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseMaintenanceManager#msgHereIsMaintenanceFee(double)
	 */
	@Override
	public void msgHereIsMaintenanceFee(double payment){
		print("Received msgHereIsMaintenanceFee");
		money += payment;
	}
	
	 
	//Semaphore Messages
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseMaintenanceManager#msgAtFridge()
	 */
	@Override
	public void msgAtFridge(){
		atFridge.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseMaintenanceManager#msgAtStove()
	 */
	@Override
	public void msgAtStove(){
		atStove.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseMaintenanceManager#msgAtTable()
	 */
	@Override
	public void msgAtTable(){
		atTable.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseMaintenanceManager#msgAtBed()
	 */
	@Override
	public void msgAtBed(){
		atBed.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseMaintenanceManager#msgAtShower()
	 */
	@Override
	public void msgAtShower(){
		atShower.release();
		stateChanged();
	}
	
	
	@Override
	public void msgAtSink() {
		// TODO Auto-generated method stub
		atSink.release();
		stateChanged();
	}

	@Override
	public void msgAtDishWasher() {
		// TODO Auto-generated method stub
		atDishWasher.release();
		stateChanged();
	}

	@Override
	public void msgAtWasher() {
		// TODO Auto-generated method stub
		atWasher.release();
		stateChanged();
	}
	
	@Override
	public void msgLeftHouse() {
		// TODO Auto-generated method stub
		leftHouse.release();
		stateChanged();
	}

	@Override
	public void msgLeftOffice() {
		// TODO Auto-generated method stub
		leftOffice.release();
		stateChanged();
	}
	
	@Override
	public void msgAtMaintenance() {
		// TODO Auto-generated method stub
		atMaintenance.release();
		stateChanged();
	}
	
	@Override
	public void msgPlacedWorkOrder() {
		// TODO Auto-generated method stub
		placedWorkOrder.release();
		stateChanged();
	}
	
	
	
	
///Scheduler
	protected boolean pickAndExecuteAnAction(){
		for(House h: houses){
			if(h.hs == HouseState.needsMaintenance){
				maintainHouse(h);
				return true;
			}
		}
		return false;
	}


	
///Actions
	private void maintainHouse(final House h){
		/*maintenanceGui.DoLeaveOffice();
		try {
			leftOffice.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getPersonAgent().OutOfComponent(this);
		
		
		/////////////////////HACK///////////////////////
		
		
		maintenanceGui.DoGoToWasher();
		try {
			atWasher.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoGoToTable();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoGoToFridge();
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoGoToStove();
		try {
			atStove.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoGoToDishWasher();
		try {
			atDishWasher.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoGoToSink();
		try {
			atSink.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoGoToBed();
		try {
			atBed.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoGoToShower();
		try {
			atShower.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maintenanceGui.DoLeaveHouse();
		try {
			leftHouse.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getPersonAgent().OutOfComponent(this);
		
		
		////////////HACK////////////////////////
		//DoGoToHousingOffice()
		
		
		
		maintenanceGui.DoGoToMaintenance();
		try {
			atMaintenance.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		print("Placing Work Order for House Maintenance.");
		
		maintenanceGui.DoPlaceWorkOrder();
		try {
			placedWorkOrder.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t.schedule(new TimerTask() {
			public void run() {
				print("Work Order for House Maintenance fulfilled and maintenance is now complete.");
				h.owner.msgPayMaintenanceFee(h);
			}
		},288000);
		
		print("Placed Work Order.  Work Order should be fulfilled the next day.");
		
		maintenanceGui.DoGoToMaintenance();
		try {
			atMaintenance.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		houses.remove(h);
	}
	
}
