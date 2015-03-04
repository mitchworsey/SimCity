package simCity.gui;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simCity.house.HouseResidentRole;
import simCity.interfaces.HouseResident;
import simCity.interfaces.Person;

public class HouseResidentGui implements Gui {
	HouseResident role = null;
	private boolean isHungry = false;
	private boolean atDestination = false;
	//private Restaurant2HostRole host;
	HousePanel gui;
	//private int xWaitPos = 10;
	//private int yWaitPos = 0;
	

    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;

	private int xPos = 200;
	private int yPos = 560;
	private int xDestination, yDestination;
	boolean isPresent = true;
	
	public static final int xTablePos = 320;
	public static final int yTablePos = 250;
	public static final int xStovePos = 230;
	public static final int yStovePos = 210;
    public static final int xFridgePos = 230;
    public static final int yFridgePos = 320;
    public static final int xBedPos = 600;
    public static final int yBedPos = 130;
    public static final int xDishWasherPos = 260;
    public static final int yDishWasherPos = 170;
    public static final int xWasherPos = 440;
    public static final int yWasherPos = 450;
    public static final int xSinkPos = 340;
    public static final int ySinkPos = 170;
    public static final int xShowerPos = 490;
    public static final int yShowerPos = 430;
    public static final int xHouseDoorPos = 200;
    public static final int yHouseDoorPos = 560;
    public static final int xDeskPos = 640;
    public static final int yDeskPos = 310;
    
    private enum Command 
    {NoCommand, GoToBed, LeaveHouse, GoToStove, GoToTable, GoToFridge, GoToSink, GoToDishWasher, GoToWasher, GoToShower, GoToDesk};
    	
    private Command command = Command.NoCommand;

	public HouseResidentGui(HouseResident hr, HousePanel gui){ //Restaurant2HostRole m) {
		role = hr;
		newAStar = null;
		xDestination = xPos;
		yDestination = yPos;
		//maitreD = m;
		this.gui = gui;
		isPresent = true;
		//setDestination(xDestination, yDestination);
	}
	

	public void updatePosition() {
		/*
		if(xPos == xDestination && yPos == yDestination && !atDestination)
			atDestination = true;
		else
			atDestination = false;
			
		
		if (xPos < xDestination)
			xPos+=1;
		else if (xPos > xDestination)
			xPos-=1;

		if (yPos < yDestination)
			yPos+=1;
		else if (yPos > yDestination)
			yPos-=1;
		*/
		if(finalList != null){
	    	if (!finalList.isEmpty()){
	    		if (finalList.size()>=1){
	    			xPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getX();
	    			yPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getY();
	    			finalList.remove(finalList.get(finalList.size()-1));
	    		}
	    	}
		}
    	
		
		if (command != Command.NoCommand) { 
			if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xHouseDoorPos/10 && yPos/10 == yHouseDoorPos/10)){
				if(command == Command.LeaveHouse) {
					role.msgLeftHouse();
					command = Command.NoCommand;
				}
	        }
			
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xStovePos/10 && yPos/10 == yStovePos/10)){
				if(command == Command.GoToStove) {
					role.msgAtStove();
					command = Command.NoCommand;
				}
	        }
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xBedPos/10 && yPos/10 == yBedPos/10)){
				if(command == Command.GoToBed) {
					role.msgAtBed();
					command = Command.NoCommand;
				}
	        }
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xFridgePos/10 && yPos/10 == yFridgePos/10)){
				if(command == Command.GoToFridge) {
					role.msgAtFridge();
					command = Command.NoCommand;
				}
	        }
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xTablePos/10 && yPos/10 == yTablePos/10)){
				if(command == Command.GoToTable) {
					role.msgAtTable();
					command = Command.NoCommand;
				}
	        }
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xSinkPos/10 && yPos/10 == ySinkPos/10)){
				if(command == Command.GoToSink) {
					role.msgAtSink();
					command = Command.NoCommand;
				}
	        }
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xDishWasherPos/10 && yPos/10 == yDishWasherPos/10)){
				if(command == Command.GoToDishWasher) {
					role.msgAtDishWasher();
					command = Command.NoCommand;
				}
	        }
			/*
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xWasherPos/10 && yPos/10 == yWasherPos/10)){
				if(command == Command.GoToWasher) {
					role.msgAtWasher();
					command = Command.NoCommand;
				}
	        }
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xShowerPos/10 && yPos/10 == yShowerPos/10)){
				if(command == Command.GoToShower) {
					role.msgAtShower();
					command = Command.NoCommand;
				}
	        }
	        */
		}
	}

	public void draw(Graphics2D g) {
    	Graphics2D g1 = (Graphics2D)g;
		g1.setColor(Color.BLUE);
		g1.fillRect(xPos, yPos, 40, 40);
	}

    public void setDestination(int x, int y){
    	xDestination = x;
    	yDestination = y;
        newAStar = new aStar(xPos, yPos, xDestination, yDestination, HousePanel.grid);
        finalList = newAStar.getBestPathSteps();
    }
	
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		role.msgGotHungry();
	}
	public boolean isHungry() {
		return isHungry;
	}

	
	public void DoGoToBed(){
		command = Command.GoToBed;
		setDestination(xBedPos, yBedPos);
	}

	public void DoLeaveHouse() {
		command = Command.LeaveHouse;
		setDestination(xHouseDoorPos, yHouseDoorPos);
	}
	
	
	
	public void DoGoToStove(){
		command = Command.GoToStove;
		setDestination(xStovePos, yStovePos);
	}
	
	public void DoGoToTable(){
		command = Command.GoToTable;
		setDestination(xTablePos, yTablePos);
	}
	
	public void DoGoToFridge(){
		command = Command.GoToFridge;
		setDestination(xFridgePos, yFridgePos);
	}
	
	public void DoGoToSink(){
		command = Command.GoToSink;
		setDestination(xSinkPos, ySinkPos);
	}
	
	public void DoGoToDishWasher(){
		command = Command.GoToDishWasher;
		setDestination(xDishWasherPos, yDishWasherPos);
	}
	
	
	/*
	public void DoGoToWasher(){
		command = Command.GoToWasher;
		setDestination(xWasherPos, yWasherPos);
	}
	
	public void DoGoToShower(){
		command = Command.GoToShower;
		setDestination(xShowerPos, yShowerPos);
	}
	
	public void DoGoToDesk(){
		command = Command.GoToDesk;
		setDestination(xDeskPos, yDeskPos);
	}
	*/
	

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

}
