package simCity.house;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.gui.HouseResidentGui;
import simCity.gui.SimCityGui;
import simCity.interfaces.BankTeller;
import simCity.interfaces.HouseResident;
import simCity.interfaces.Person;

public class HouseResidentRole extends Role implements HouseResident{
	String name, food;
	double money = 1000.00;
	House house;
	Timer timeCook = new Timer();
	Timer timeEat = new Timer();
	public HouseResidentGui residentGui;
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atStove = new Semaphore(0,true);
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atBed = new Semaphore(0,true);
	private Semaphore atSink = new Semaphore(0,true);
	private Semaphore atDishWasher = new Semaphore(0,true);
	private Semaphore atWasher = new Semaphore(0,true);
	private Semaphore atShower = new Semaphore(0,true);
	private Semaphore leftHouse = new Semaphore(0,true);
	
	public enum AgentEvent {none, hungry, makeFood, eatFood, cleanDishes, sleepy, doneCleaning, leftHouse}
	public AgentEvent event = AgentEvent.none;
	
	public enum AgentState {DoingNothing, aboutToMakeFood, cleaningDishes, madeFood}
	public AgentState state = AgentState.DoingNothing;
	
	
	public HouseResidentRole(String name){
		super();
		this.name = name;
	}
	
	public void setGui(HouseResidentGui g) {
		residentGui = g;
	}
	
	public void setHouse(House h){
		this.house = h;
	}

	public String getName(){
		return name;
	}
	
///Messages
	
	///////////////////////////Inside House Scenarios
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgGotSleepy()
	 */
	@Override
	public void msgGotSleepy() {//from animation
		print("Received msgGotSleepy");
		event = AgentEvent.sleepy;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgGotHungry()
	 */
	@Override
	public void msgGotHungry() {//from animation
		print("Received msgGotHungry");
		event = AgentEvent.hungry;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgMakeFood(String)
	 */
	@Override
	public void msgMakeFood(String type){
		print("Received msgMakeFood");
		event = AgentEvent.makeFood;
		food = type;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgFoodDone()
	 */
	@Override
	public void msgFoodDone(){
		print("Received msgFoodDone");
		event = AgentEvent.eatFood;
		stateChanged();
	}
	
	@Override
	public void msgCleanDishes(){
		print("Received msgCleanDishes");
		event = AgentEvent.cleanDishes;
		stateChanged();
	}
	
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgLeftHouse()
	 */
	@Override
	public void msgLeftHouse(){
		print("Received msgLeftHouse");
		leftHouse.release();
		event = AgentEvent.none;
		stateChanged();
	}
	
	//Semaphore Messages
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgAtFridge()
	 */
	@Override
	public void msgAtFridge(){
		atFridge.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgAtStove()
	 */
	@Override
	public void msgAtStove(){
		atStove.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgAtTable()
	 */
	@Override
	public void msgAtTable(){
		atTable.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see simCity.house.HouseResident#msgAtBed()
	 */
	@Override
	public void msgAtBed(){
		atBed.release();
		stateChanged();
	}
	
	@Override
	public void msgAtSink(){
		atSink.release();
		stateChanged();
	}
	
	@Override
	public void msgAtDishWasher(){
		atDishWasher.release();
		stateChanged();
	}
	
	@Override
	public void msgAtWasher(){
		atWasher.release();
		stateChanged();
	}
	
	@Override
	public void msgAtShower(){
		atShower.release();
		stateChanged();
	}
	
	

	
	
///Scheduler
	public boolean pickAndExecuteAnAction(){
		
		////STARTING HERE
		if(state == AgentState.DoingNothing && event == AgentEvent.sleepy){
			state = AgentState.DoingNothing;
			goToBed();
			return true;
		}
		
		if(state == AgentState.DoingNothing && event == AgentEvent.hungry){
			state = AgentState.aboutToMakeFood;
			goToFridge();
			return true;
		}
		
		if(state == AgentState.aboutToMakeFood && event == AgentEvent.makeFood){
			makeFood();
			return true;
		}
		
		if(state == AgentState.madeFood && event == AgentEvent.eatFood){
			eatFood();
			return true;
		}
		
		if(state == AgentState.cleaningDishes && event == AgentEvent.cleanDishes){
			state = AgentState.DoingNothing;
			cleanDishes();
			return true;
		}
		/*
		if(state == AgentState.DoingNothing && event == AgentEvent.leaveHouse){
			leaveHouse();
			return true;
		}
		*/
		
		//residentGui.DoGoToDesk();
		
		return false;
		
	}

	
	
	
///Actions
	
	//////////////////////////////////Inside House Scenario
	
	private void goToBed(){
		residentGui.DoGoToBed();
		try {
			atBed.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		residentGui.DoLeaveHouse();
		try {
			leftHouse.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = AgentEvent.leftHouse;
		state = AgentState.DoingNothing;
		getPersonAgent().OutOfComponent(this);
		SimCityGui.housePanel.removeGui(residentGui);
	}
	
	private void goToFridge(){
		state = AgentState.aboutToMakeFood;
		residentGui.DoGoToFridge();
		print("Going to Fridge");
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msgMakeFood("Chicken");
	}
	
	private void makeFood(){
		state = AgentState.madeFood;
		residentGui.DoGoToStove();
		print("Going to Stove");
		try {
			atStove.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//foods.get(o.choice).stock--;
		//print(o.choice + " stock = " + foods.get(o.choice).stock);
		
		
		timeCook.schedule(new TimerTask() {
			public void run() {
				msgFoodDone();
				print("Done cooking "+ food);
			}
		},5000);
		
	}
	
	private void eatFood(){
		state = AgentState.cleaningDishes;
		residentGui.DoGoToTable();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		timeEat.schedule(new TimerTask() {
			public void run() {
				print("Done eating " + food);
				msgCleanDishes();
			}
		},
		5000);
		
	}
	
	private void cleanDishes(){
		residentGui.DoGoToSink();
		try {
			atSink.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		residentGui.DoGoToDishWasher();
		try {
			atDishWasher.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		residentGui.DoGoToBed();
		try {
			atBed.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		residentGui.DoLeaveHouse();
		try {
			leftHouse.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Leaving house");
		event = AgentEvent.leftHouse;
		state = AgentState.DoingNothing;
		SimCityGui.apartments.removeGui(residentGui);
		getPersonAgent().OutOfComponent(this);
		print("Left house");
		
	}
	/*
	private void leaveHouse(){
		residentGui.DoLeaveHouse();
		getPersonAgent().OutOfComponent(this);
		event = AgentEvent.leftHouse;
		state = AgentState.DoingNothing;
	}
	*/
}
