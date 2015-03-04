package simCity.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import simCity.Role;
import simCity.gui.BankGuardGui;
import simCity.interfaces.BankGuard;
import simCity.interfaces.BankRobber;
import simCity.interfaces.BankTeller;

public class BankGuardRole extends Role implements BankGuard {
	private String name;
	private BankGuardGui gui;
	private BankTeller teller;
	private List<Robber> robbers = new ArrayList<Robber>();
	
	private Semaphore animationComplete = new Semaphore(0, true);
	
	public enum robberState {nothing, beingChased, stopped, gotAway};
	
	public class Robber {
		robberState state = robberState.nothing;
		BankRobber robber;
		
		Robber(BankRobber robber, robberState state) {
			this.robber = robber;
			this.state = state;
		}
	}
	
	public BankGuardRole(String name) {
		super();
		
		this.name = name;
	}
	
	public void setTeller(BankTeller teller) {
		this.teller = teller;
	}
	
	public void setGui(BankGuardGui gui) {
		this.gui = gui;
	}
	
	public BankGuardGui getGui() {
		return gui;
	}
	
	public void msgAnimationComplete() { //from animation
		animationComplete.release();
		stateChanged();
	}
	
	public void msgChaseRobber(BankRobber robber) {
		print("Chasing robber");
		robbers.add(new Robber(robber, robberState.beingChased));
		stateChanged();
	}
	
	public void msgStoppedBankRobber(BankRobber robber) { //from animation
		print("Stopped bank robber");
		robbers.add(new Robber(robber, robberState.stopped));
		stateChanged();
	}
	
	public void msgFailedtoStopRobber(BankRobber robber) { //from animation
		print("Failed to stop bank robber");
		robbers.add(new Robber(robber, robberState.gotAway));
		stateChanged();
	}

	protected boolean pickAndExecuteAnAction() {
		for(Robber r : robbers) {
			if(r.state == robberState.beingChased) {
				protectTeller(r);
				return true;
			}
		}
		for(Robber r : robbers) {
			if(r.state == robberState.stopped) {
				disposeOfRobber(r);
				return true;
			}
		}
		for(Robber r : robbers) {
			if(r.state == robberState.gotAway) {
				allowBankRobbery(r);
				return true;
			}
		}
		return false;
	}
	
	private void protectTeller(Robber r) {
		gui.DoChaseRobber(r.robber);
		r.state = robberState.nothing;
	}
	
	private void disposeOfRobber(Robber r) {
		gui.DoDisposeOfRobber();
		r.robber.msgLeaveBank();
		robbers.remove(r);
		
		try  {
			animationComplete.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		gui.DoReturnToHomePos();
	}
	
	public void allowBankRobbery(Robber r) {
		teller.msgHandOverMoneyToRobber();
		r.robber.msgTakeMoney(((BankTellerRole) teller).bankMoney);
		robbers.remove(r);
		gui.DoReturnToHomePos();
	}

}
