package simCity.gui;

import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import simCity.Location;
import simCity.Role;
import simCity.interfaces.DeliveryTruck;
import simCity.market.MarketDeliveryTruck;
import simCity.market.MarketGrocerRole.MyRestaurant;

public class DeliveryTruckGui implements Gui{

	private MarketDeliveryTruck agent = null;
	private SimCityGui gui;
	String displayName = "";

	private Timer timer = new Timer();

	private Map<Integer, Location> deliveryMap = new HashMap<Integer, Location>();

	private aStar newAStar;
	private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
	private Node tempNode;

	final int agentWidth = 20;
	final int agentHeight = 20;
	Location nextStop;

	private int xPos, yPos; //default position
	private int xDestination, yDestination; //default start position

	private enum Command 
	{NoCommand, GoToNextBusStop, GoToTopHalfway, GoToBottomHalfway};
	private Command command = Command.NoCommand;
	private enum dCommand {
		NoCommand, GoToR1, GoToR2, GoToR3, GoToR4, GoToR5, GoToM, GoToM1, GoToM2
	}
	private dCommand dcommand = dCommand.NoCommand;
	
	
	BufferedImage image;    

	public DeliveryTruckGui(MarketDeliveryTruck agent, SimCityGui gui, int xStart, int yStart) {
		this.agent = agent;
		this.gui = gui;
		xPos = xStart;
		yPos = yStart;
		xDestination = xStart;
		yDestination = yStart;
		if(agent.getName().length() == 0) {
			displayName = "";
		}
		else if (agent.getName().length() < 5) {
			displayName = agent.getName();
		}
		else { // agent name >= 5
			displayName = agent.getName().substring(0, 5);
		}

		loadImage("src/simCity/gui/images/DeliveryTruck_small.jpg"); 
	}

	public DeliveryTruckGui(MarketDeliveryTruck agent) {
		this.agent = agent;
		if(agent.getName().length() == 0) {
			displayName = "";
		}
		else if (agent.getName().length() < 5) {
			displayName = agent.getName();
		}
		else { // agent name >= 5
			displayName = agent.getName().substring(0, 5);
		}

		loadImage("src/simCity/gui/images/DeliveryTruck_small.jpg");
	}


	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#addBusStopLocation(simCity.BusStopAgent, int, int)
	 */
	public void addRestaurantLocation(int index, int x, int y) {
		Location l = new Location(x, y);
		deliveryMap.put(index, l);
	}

	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#addBusStopLocation(simCity.BusStopAgent, simCity.Location)
	 */
	public void addRestaurantLocation(int index, Location l) {
		deliveryMap.put(index, l);
	}


	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#updatePosition()
	 */
	@Override
	public void updatePosition() {
		// Check to see if the bus has reached destination yet
		if ( (xPos/10 == xDestination/10) && (yPos/10 == yDestination/10) ) {
//			// Check to see if waiter's destination was home
//			if (command == Command.GoToNextBusStop) {
//				//agent.msgAtBusStop();
//				command = Command.NoCommand;
//			}
//			if (command == Command.GoToTopHalfway) {
//				DoGoFromTopHalfway(nextStop.x, nextStop.y);
//				command = Command.GoToNextBusStop;
//			}
//			if (command == Command.GoToBottomHalfway){
//				DoGoFromBottomHalfway(nextStop.x, nextStop.y);
//				command = Command.GoToNextBusStop;
//			}
		}
		
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		if (xPos == xDestination && yPos == yDestination) {
			/* DELIVERY TRUCK */
			if (dcommand == dCommand.GoToR1) {
				agent.msgAtRestaurant1();
				dcommand = dCommand.NoCommand;
			}
			if (dcommand == dCommand.GoToR2) {
				agent.msgAtRestaurant2();
				dcommand = dCommand.NoCommand;
			}
			if (dcommand == dCommand.GoToR3) {
				agent.msgAtRestaurant3();
				dcommand = dCommand.NoCommand;
			}
			if (dcommand == dCommand.GoToR4){
				agent.msgAtRestaurant4();
				dcommand = dCommand.NoCommand;
			}
			if (dcommand == dCommand.GoToR5){
				agent.msgAtRestaurant5();
				dcommand = dCommand.NoCommand;
			}
			if (dcommand == dCommand.GoToM){
				agent.msgAtMarket();
				dcommand = dCommand.NoCommand;
			}
//			if (dcommand == dCommand.GoToM1){
//				agent.msgAtRestaurant1();
//				dcommand = dCommand.GoToM1;
//			}
//			if (dcommand == dCommand.GoToM2){
//				agent.msgAtRestaurant1();
//				dcommand = dCommand.GoToM2;
//			}
		}

//		if (finalList != null){
//			if (!finalList.isEmpty()){
//				if (finalList.size()>=1){
//					//simCity.gui.CityPanel.grid.release(xPos/10, yPos/10);
//					xPos = finalList.get(finalList.size()-1).getX();
//					yPos = finalList.get(finalList.size()-1).getY();
//					//simCity.gui.CityPanel.grid.set(xPos/10, yPos/10);
//					finalList.remove(finalList.get(finalList.size()-1));
//				}
//			}
//		}

	}

	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, xPos, yPos, null);;

		g.setColor(Color.BLACK);
		g.drawString(displayName, xPos, yPos);
	}

	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#isPresent()
	 */
	@Override
	public boolean isPresent() {
		return true;
	}

//	/* (non-Javadoc)
//	 * @see simCity.gui.BusGuiInterface#DoGoFromTopHalfway(int, int)
//	 */
//	@Override
//	public void DoGoFromTopHalfway(int x, int y) {
//		setDestination(x, y);
//		command = Command.GoToNextBusStop;
//	}

	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#DoGoFromBottomHalfway(int, int)
	 */
//	@Override
//	public void DoGoFromBottomHalfway(int x, int y) {
//		setDestination(x, y);
//		command = Command.GoToNextBusStop;
//	}
//
//	/* (non-Javadoc)
//	 * @see simCity.gui.BusGuiInterface#DoGoToNextBusStop(simCity.interfaces.BusStop)
//	 */
//	@Override
//	public void DoGoToNextBusStop(BusStop b) {
//		nextStop = busStopMap.get(b);
//		if (xPos > 500) { // going to BusStop1
//			setDestination(xPos - 150, yPos);
//			command = Command.GoToTopHalfway;
//		}
//		else { //nextStop.x > 500, going to BusStop2
//			setDestination(xPos + 150, yPos);
//			command = Command.GoToBottomHalfway;
//		}
//	}

//	/* (non-Javadoc)
//	 * @see simCity.gui.BusGuiInterface#DoUnloadPassengers()
//	 */
//	@Override
//	public void DoUnloadPassengers() {
//
//		timer.schedule(new TimerTask() {
//			public void run() {
//				agent.msgActionComplete();
//			}
//		},
//		5000);
//	}


	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#getX()
	 */
	@Override
	public int getX() {
		return xPos;
	}


	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#getY()
	 */
	@Override
	public int getY() {
		return yPos;
	}

	/* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#setDestination(int, int)
	 */
	@Override
	public void setDestination(int x, int y){
		xDestination = x;
		yDestination = y;
		newAStar = new aStar(xPos, yPos, xDestination, yDestination, CityPanel.busGrid);
		finalList = newAStar.getBestPath();
	}

	public void loadImage(String fn) {
		try{
			image = ImageIO.read(new File(fn));
		} catch (IOException e) {}
	}
}
