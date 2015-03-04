package simCity.bank;

import java.util.concurrent.Semaphore;

import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.gui.BankRobberGui;
import simCity.interfaces.BankRobber;
import simCity.interfaces.BankTeller;

public class BankRobberRole extends Role implements BankRobber {
	private String name;
	private BankRobberGui gui;
	private BankTeller teller;
	
	private Semaphore animationComplete = new Semaphore(0, true);
	
	public enum State {nothing, stopped, gotAway};
	State state = State.nothing;
	
	public BankRobberRole(String name) {
		super();
		this.name = name;
	}
	
	public void setTeller(BankTeller teller) {
		this.teller = teller;
	}
	
	public void setGui(BankRobberGui gui) {
		this.gui = gui;
	}
	
	public BankRobberGui getGui() {
		return gui;
	}
	
	public void msgAnimationComplete() { //from animation
		animationComplete.release();
		stateChanged();
	}
	
	public void msgLeaveBank() { //from BankGuardGui
		print("Leaving bank with guard");
		state = State.stopped;
		stateChanged();
	}
	
	public void msgTakeMoney(double amt) {
		((OrdinaryPerson)personAgent).money += amt;
		print("Wasn't caught by the guard. Running away with "+((OrdinaryPerson)personAgent).money);
		state = State.gotAway;
		stateChanged();
	}

	protected boolean pickAndExecuteAnAction() {
		if(state == State.stopped) {
			exitBankWithGuard();
			return true;
		}
		else if(state == State.gotAway) {
			robBankAndRunAway();
			return true;
		}
		return false;
	}
	
	private void exitBankWithGuard() {
		gui.DoLeaveBank();
		try {
			animationComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = State.nothing;
		gui.removeGui(gui);
		gui = null;
		personAgent.OutOfComponent(this);
	}
	
	private void robBankAndRunAway() {
		gui.DoRunAway();
		try {
			animationComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = State.nothing;
		gui.removeGui(gui);
		gui = null;
		personAgent.OutOfComponent(this);
	}
}
