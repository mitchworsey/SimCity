package simCity.restaurant5.gui;

import java.awt.*;
import java.util.concurrent.Semaphore;

import simCity.interfaces.*;
import simCity.restaurant5.*;
import simCity.restaurant5.interfaces.Restaurant5Cook;
import simCity.gui.*;

public class Restaurant5CookGui implements Gui{

	private Restaurant5Cook agent = null;
	private boolean isPresent = true;
	
	public int xPos = -10, yPos = 0;// default waiter position
	private int xDestination = 250, yDestination = 175;// default start position

	public int xPlate = 250, yPlate = 60;
	public int xCook = 250, yCook = 0;
	private enum cCommand {
		noCommand, GoToCook, GoToPlate
	};
	private cCommand ccommand = cCommand.noCommand;
	private Restaurant5Panel gui;

	public Restaurant5CookGui(Restaurant5Cook agent, Restaurant5Panel gui, int ct) {
		this.agent = agent; 
		agent.setGui(this);
		this.gui = gui;
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

		/*
		if (xPos == xDestination && yPos == yDestination) {
			if (ccommand == cCommand.GoToCook){
				System.out.println("GOTOCOOK");
				agent.msgAtCook();
			}}	
			if (ccommand == cCommand.GoToPlate) {
				agent.msgAtPlate();
			}	
			else if(ccommand == cCommand.noCommand) {
				agent.msgAtHome();
			}
 
			ccommand = cCommand.noCommand;

			if (xPos == 20 && yPos == 20) {
				agent.callStateChange();
			}
		}*/
		//if (xPos == 250 && yPos == 20) {
		//	agent.callStateChange();
		//}
		//System.out.println(xDestination + " "+ yDestination + "|" + xPos + " " + yPos );
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void DoGoPlate() {
		xDestination = xPlate;
		yDestination = yPlate;
		ccommand = cCommand.GoToPlate;
	}

	public void goSomewhere() {
		this.xDestination = 0;//xCook;
		this.yDestination = 0;//yCook;
	}
	public void DoGoCook() {
		this.xDestination = 0;//xCook;
		this.yDestination = 0;//yCook;
		//this.xPos = 30;
		//this.yPos = 30;
		ccommand = cCommand.GoToCook;
		System.out.println(xDestination + " "+ yDestination + "|" + xPos + " " + yPos );
		
	}
	
	public void GoHome() {
		xDestination = 250;
		yDestination = 20;
		ccommand = cCommand.noCommand;
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
