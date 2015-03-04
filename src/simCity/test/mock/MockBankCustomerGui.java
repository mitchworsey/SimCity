package simCity.test.mock;

import java.awt.Graphics2D;

import simCity.interfaces.BankCustomerGuiInterface;

public class MockBankCustomerGui extends Mock implements BankCustomerGuiInterface {

	public MockBankCustomerGui(String name) {
		super(name);
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoExitBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToTeller() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtLocation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGui(BankCustomerGuiInterface gui) {
		// TODO Auto-generated method stub
		
	}
}
