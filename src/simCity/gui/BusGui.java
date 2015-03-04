package simCity.gui;

import java.awt.*;
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

import simCity.BusAgent;
import simCity.BusStopAgent;
import simCity.Location;
import simCity.gui.Gui;
import simCity.gui.SimCityGui;
import simCity.interfaces.Bus;
import simCity.interfaces.BusGuiInterface;
import simCity.interfaces.BusStop;


public class BusGui implements Gui, BusGuiInterface {
	
	static final int STOP1X = 260;
	static final int STOP1Y = 140;
	static final int STOP2X = 635;
	static final int STOP2Y = 410;
	
    private Bus agent = null;
    private SimCityGui gui;
    String displayName = "";
    
    private Timer timer = new Timer();
   
    private Map<BusStopAgent, Location> busStopMap = new HashMap<BusStopAgent, Location>();
    
    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;
    
    final int agentWidth = 20;
    final int agentHeight = 20;
    Location nextStop;
    
    private int xPos, yPos; //default position
    private int xDestination, yDestination; //default start position
    private int xPosOld, yPosOld;
    private int xPosSmall, yPosSmall;
    private int count = 0;
    private boolean move;
    
    private enum Command 
    {NoCommand, GoToNextBusStop, GoToTopHalfway, GoToBottomHalfway};
    private Command command = Command.NoCommand;
    
    BufferedImage image;    

    public BusGui(Bus agent, SimCityGui gui, int xStart, int yStart) {
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
        
        loadImage("src/simCity/gui/images/newCity/bus.png");
        //stopLightTimer();
    }
    
    public BusGui(Bus agent) {
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
        
        loadImage("src/simCity/gui/images/newCity/bus.png");
        //stopLightTimer();
    }
    
    
    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#addBusStopLocation(simCity.BusStopAgent, int, int)
	 */
    @Override
	public void addBusStopLocation(BusStopAgent b, int x, int y) {
    	Location l = new Location(x, y);
    	busStopMap.put(b, l);
    }
    
    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#addBusStopLocation(simCity.BusStopAgent, simCity.Location)
	 */
    @Override
	public void addBusStopLocation(BusStopAgent b, Location l) {
    	busStopMap.put(b, l);
    }
    

    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#updatePosition()
	 */
    @Override
	public void updatePosition() {
        // Check to see if the bus has reached destination yet
        if ( (xPos/10 == xDestination/10) && (yPos/10 == yDestination/10) ) {
        	// Check to see if waiter's destination was home
        	if (command == Command.GoToNextBusStop) {
        		agent.MsgAtBusStop();
        		command = Command.NoCommand;
        	}
        	if (command == Command.GoToTopHalfway) {
        		DoGoFromTopHalfway(nextStop.x, nextStop.y);
        		command = Command.GoToNextBusStop;
        	}
        	if (command == Command.GoToBottomHalfway){
        		DoGoFromBottomHalfway(nextStop.x, nextStop.y);
        		command = Command.GoToNextBusStop;
        	}
        }
        
        //If bus is at stop light and light is red, disable movement
        if (xPosSmall > STOP1X && xPosSmall < STOP1X+20 && yPosSmall > STOP1Y && yPosSmall < STOP1Y + 60 && SimCityGui.controlPanel.getRedLight()){
        	move = false;
        }
        else if (xPosSmall > STOP2X && xPosSmall < STOP2X+20 && yPosSmall > STOP2Y && yPosSmall < STOP2Y + 60 && SimCityGui.controlPanel.getRedLight()){
        	move = false;
        }
        else
        	move = true;
        
        if (move){
        if (finalList != null){
    		if (!finalList.isEmpty()){
    			if (finalList.size()>=1){
    				if (count % 10 == 0) {
    					if (CityPanel.semaphoreGrid.getSemaphore(finalList.get(finalList.size()-1).getX()*10, 
    							finalList.get(finalList.size()-1).getY()*10).tryAcquire()){
    						CityPanel.semaphoreGrid.getSemaphore(xPosOld, yPosOld).release();
    						if (CityPanel.semaphoreGrid.getSemaphore(finalList.get(finalList.size()-1).getX()*10, 
        							finalList.get(finalList.size()-1).getY()*10).getQueueLength() > 0){
    							CityPanel.semaphoreGrid.getSemaphore(finalList.get(finalList.size()-1).getX()*10, 
    	    							finalList.get(finalList.size()-1).getY()*10).release();
    							move = false;
    						}
    						else
    							move = true;
    						xPosOld = xPos;
    						yPosOld = yPos;
    						xPos = finalList.get(finalList.size()-1).getX()*10;
    						yPos = finalList.get(finalList.size()-1).getY()*10;
    						finalList.remove(finalList.get(finalList.size()-1));
    						xPosSmall = xPos;
    						yPosSmall = yPos;
    					}
    				}
    				else {
    			        if (xPosSmall < finalList.get(finalList.size()-1).getX()*10)
    			            xPosSmall = xPosSmall + 1;
    			        else if (xPosSmall > finalList.get(finalList.size()-1).getX()*10)
    			            xPosSmall = xPosSmall - 1;
    			        if (yPosSmall < finalList.get(finalList.size()-1).getY()*10)
    			            yPosSmall = yPosSmall + 1;
    			        else if (yPosSmall > finalList.get(finalList.size()-1).getY()*10)
    			            yPosSmall = yPosSmall -1;
    				}
    				count += 1;
    			}
    			if(finalList.size() == 0){
					CityPanel.semaphoreGrid.getSemaphore(xPos, yPos).release();
    			}
    		}
        }
    	}
 
    }

    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#draw(java.awt.Graphics2D)
	 */
    @Override
	public void draw(Graphics2D g) {
        g.drawImage(image, xPosSmall, yPosSmall, null);;
        
        g.setColor(Color.BLACK);
        g.drawString(displayName, xPosSmall, yPosSmall);
        
        /*
        for (Node node : finalList){
    		g.setColor(Color.BLUE);
    		g.drawRect(node.getX()*10, node.getY()*10, 10, 10);
    	}
    	*/
        
        /*
        g.setColor(Color.PINK);
        g.drawRect(260, 150, 20, 50);
        g.drawRect(635, 410, 20, 50);
        */
    }

    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#isPresent()
	 */
    @Override
	public boolean isPresent() {
        return true;
    }

    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#getXPos()
	 */
    @Override
	public int getXPos() {
        return xPos;
    }

    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#getYPos()
	 */
    @Override
	public int getYPos() {
        return yPos;
    }
        
    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#DoGoFromTopHalfway(int, int)
	 */
    @Override
	public void DoGoFromTopHalfway(int x, int y) {
    	setDestination(x, y);
    	command = Command.GoToNextBusStop;
    }
    
    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#DoGoFromBottomHalfway(int, int)
	 */
    @Override
	public void DoGoFromBottomHalfway(int x, int y) {
    	setDestination(x, y);
    	command = Command.GoToNextBusStop;
    }
    
    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#DoGoToNextBusStop(simCity.interfaces.BusStop)
	 */
    @Override
	public void DoGoToNextBusStop(BusStop b) {
    	nextStop = busStopMap.get(b);
    	if (xPos > 500) { // going to BusStop1
    		setDestination(xPos - 150, yPos);
    		command = Command.GoToTopHalfway;
    	}
    	else { //nextStop.x > 500, going to BusStop2
    		setDestination(xPos + 150, yPos);
    		command = Command.GoToBottomHalfway;
    	}
    }
    
    /* (non-Javadoc)
	 * @see simCity.gui.BusGuiInterface#DoUnloadPassengers()
	 */
    @Override
	public void DoUnloadPassengers() {
    	
    	timer.schedule(new TimerTask() {
			public void run() {
				agent.MsgActionComplete();
			}
		},
		5000);
    }


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
