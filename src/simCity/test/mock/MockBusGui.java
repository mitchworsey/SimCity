package simCity.test.mock;

import java.awt.Graphics2D;

import simCity.BusStopAgent;
import simCity.Location;
import simCity.interfaces.BusGuiInterface;
import simCity.interfaces.BusStop;

public class MockBusGui extends Mock implements BusGuiInterface {

	public MockBusGui(String name) {
		super(name);
	}

	@Override
	public void addBusStopLocation(BusStopAgent b, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBusStopLocation(BusStopAgent b, Location l) {
		// TODO Auto-generated method stub
		
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
	public int getXPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void DoGoFromTopHalfway(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoFromBottomHalfway(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToNextBusStop(BusStop b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoUnloadPassengers() {
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
	
	

}
