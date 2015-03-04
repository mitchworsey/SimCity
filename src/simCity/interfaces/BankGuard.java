package simCity.interfaces;

public interface BankGuard {
	
	public abstract void msgAnimationComplete();
	
	public abstract void msgStoppedBankRobber(BankRobber r);
	
	public abstract void msgFailedtoStopRobber(BankRobber r);

	public abstract void msgChaseRobber(BankRobber bankRobber);

}
