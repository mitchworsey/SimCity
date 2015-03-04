package simCity.interfaces;

import java.awt.Graphics2D;

import simCity.BusStopAgent;
import simCity.Location;
import simCity.interfaces.BusStop;

public interface BusGuiInterface {

	public abstract void addBusStopLocation(BusStopAgent b, int x, int y);

	public abstract void addBusStopLocation(BusStopAgent b, Location l);

	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract int getXPos();

	public abstract int getYPos();

	public abstract void DoGoFromTopHalfway(int x, int y);

	public abstract void DoGoFromBottomHalfway(int x, int y);

	public abstract void DoGoToNextBusStop(BusStop b);

	public abstract void DoUnloadPassengers();

	public abstract int getX();

	public abstract int getY();

	public abstract void setDestination(int x, int y);

}