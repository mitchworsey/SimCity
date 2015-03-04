package simCity.restaurant5.gui;

import java.awt.*;


import simCity.restaurant5.*;
import simCity.restaurant5.interfaces.*;
import simCity.restaurant5.gui.*;
import simCity.gui.*;

public class Restaurant5HostGui implements Gui{
	private Restaurant5Host agent = null;

	public int xPos = 590, yPos = 410;// default waiter position
	private int xDestination = 590, yDestination = 410;// default start position

	public static final int xTable1 = 195;
	public static final int yTable1 = 375;

	public static final int xTable2 = 300;
	public static final int yTable2 = 250;

	public static final int xTable3 = 200;
	public static final int yTable3 = 100;

	public static final int xTable4 = 300;
	public static final int yTable4 = 100;

	public Restaurant5HostGui(Restaurant5Host agent) {
		this.agent = agent;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == -20 && yPos ==-20) { agent.callStateChange(); }
		}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return true;
	}

	public void DoBringToTable(Restaurant5CustomerRole customer, int table) {
		switch (table) {
		case 1:
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;
			break;
		case 2:
			System.out.println("DoBringToTable #2");
			xDestination = xTable2 + 20;
			yDestination = yTable2 - 20;
			break;
		case 3:
			xDestination = xTable3 + 20;
			yDestination = yTable3 - 20;
			break;
		case 4:
			xDestination = xTable4 + 20;
			yDestination = yTable4 - 20;
			break;
		default:
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;
		}

	}

	public void DoLeaveCustomer() {
		xDestination = -20;
		yDestination = -20;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return yPos;
	}

	@Override
	public void setDestination(int x, int y) {
		// TODO Auto-generated method stub
		xDestination = x;
		yDestination = y;
	}
}
