package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simCity.interfaces.HouseCustomer;

public class HouseCustomerGui implements Gui{
	HouseCustomer role = null;
	private boolean atDestination = false;
	//private Restaurant2HostRole host;
	HousingOfficePanel gui;
	//private int xWaitPos = 10;
	//private int yWaitPos = 0;
	

    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;

	private int xPos = 420;
	private int yPos = 500;
	private int xDestination = 420;
	private int yDestination = 500;
	boolean isPresent = true;
    public static final int xOfficeDoorPos = 420;
    public static final int yOfficeDoorPos = 500;
    public static final int xRealEstatePos = 360;
    public static final int yRealEstatePos = 250;
    
    private enum Command 
    {NoCommand, LeaveOffice, GoToRealEstate};
    	
    private Command command = Command.NoCommand;

	public HouseCustomerGui(HouseCustomer hc, HousingOfficePanel gui){ //Restaurant2HostRole m) {
		role = hc;
		//maitreD = m;
		this.gui = gui;
		isPresent = true;
	}
	
	/*
	public void setYWaitPos(int y){
		yWaitPos += y;
	}
	public void setXWaitPos(int x){
		xWaitPos += x;
	}
	
	public int getXWaitPos(){
		return xWaitPos;
	}
	public int getYWaitPos(){
		return yWaitPos;
	}
	*/

	public void updatePosition() {
		/*
		if(xPos == xDestination && yPos == yDestination && !atDestination)
			atDestination = true;
		else
			atDestination = false;
			*/
		
		if (xPos < xDestination)
			xPos+=1;
		else if (xPos > xDestination)
			xPos-=1;

		if (yPos < yDestination)
			yPos+=1;
		else if (yPos > yDestination)
			yPos-=1;
		
	/*
    	if (!finalList.isEmpty()){
    		if (finalList.size()>=1){
    			//simCity.gui.CityPanel.grid.release(xPos/10, yPos/10);
    			xPos = finalList.get(finalList.size()-1).getX();
    			yPos = finalList.get(finalList.size()-1).getY();
    			//simCity.gui.CityPanel.grid.set(xPos/10, yPos/10);
    			finalList.remove(finalList.get(finalList.size()-1));
    		}
    	}
    	*/
		
		if (command != Command.NoCommand) { 
			if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xOfficeDoorPos/10 && yPos/10 == yOfficeDoorPos/10)){
				if(command == Command.LeaveOffice) {
					role.msgLeftOffice();
					command = Command.NoCommand;
				}
	        }
			else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xRealEstatePos/10 && yPos/10 == yRealEstatePos/10)){
				if(command == Command.GoToRealEstate) {
					role.msgAtRealEstate();
					command = Command.NoCommand;
				}
	        }
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
    	atDestination = false;
    	/*
        newAStar = new aStar(xPos, yPos, xDestination, yDestination, gui.grid);
        finalList = newAStar.getBestPath();
        */
    }
	
	public boolean isPresent() {
		return isPresent;
	}
	
	
	public void DoLeaveOffice() {
		xDestination = xOfficeDoorPos;
		yDestination = yOfficeDoorPos;
		command = Command.LeaveOffice;
	}
	
	public void DoGoToRealEstate() {
		xDestination = xRealEstatePos;
		yDestination = yRealEstatePos;
		command = Command.GoToRealEstate;
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

}
