package simCity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simCity.interfaces.HouseOwner;


public class HouseOwnerGui implements Gui{
	private HouseOwner role = null;
	private boolean atDestination = false;
	//private Restaurant2HostRole host;
	HousingOfficePanel gui;
	//private int xWaitPos = 10;
	//private int yWaitPos = 0;
	

    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	public static final int xMaintenancePos = 500;
	public static final int yMaintenancePos = 250;
	public static final int xOfficeDoorPos = 420;
    public static final int yOfficeDoorPos = 500;
    public static final int xRealEstatePos = 360;
    public static final int yRealEstatePos = 200;

	public HouseOwnerGui(HouseOwner ho, HousingOfficePanel gui){ //Restaurant2HostRole m) {
		role = ho;
		xPos = 360;
		yPos = 200;
		
		//Destination just for testing
		xDestination = 360;
		yDestination = 200;
        newAStar = null;
        //finalList = newAStar.getBestPath();
        
		//maitreD = m;
		this.gui = gui;
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
		atDestination = false;
		if(xPos == xDestination && yPos == yDestination && !atDestination)
			atDestination = true;
		
		if (xPos < xDestination)
			xPos+=2;
		else if (xPos > xDestination)
			xPos-=2;

		if (yPos < yDestination)
			yPos+=2;
		else if (yPos > yDestination)
			yPos-=2;

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

		
		if (xPos == xDestination && yPos == yDestination & (xPos == xMaintenancePos && yPos == yMaintenancePos)){
			if(!atDestination)
				role.msgAtMaintenance();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xRealEstatePos && yPos == yRealEstatePos)){
			if(!atDestination)
				role.msgAtRealEstate();
        }
		
		if (xPos == xDestination && yPos == yDestination & (xPos == xOfficeDoorPos && yPos == yOfficeDoorPos)){
			if(!atDestination)
				role.msgLeftOffice();
        }
		
		
		
	}

	public void draw(Graphics2D g) {
    	Graphics2D g1 = (Graphics2D)g;
		g1.setColor(Color.ORANGE);
		g1.fillRect(xPos, yPos, 40, 40);
	}

    public void setDestination(int x, int y){
    	xDestination = x;
    	yDestination = y;
        newAStar = new aStar(xPos, yPos, xDestination, yDestination, CityPanel.grid);
        finalList = newAStar.getBestPath();
    }
	
	public boolean isPresent() {
		return true;
	}

	
	public void DoGoToMaintenance(){
		xDestination = xMaintenancePos;
		yDestination = yMaintenancePos;
		setDestination(xDestination, yDestination);
	}

	public void DoLeaveOffice() {
		xDestination = xOfficeDoorPos;
		yDestination = yOfficeDoorPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToRealEstate() {
		xDestination = xRealEstatePos;
		yDestination = yRealEstatePos;
		setDestination(xDestination, yDestination);
	}
	
	
	public int getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

	public int getY() {
		// TODO Auto-generated method stub
		return yPos;
	}
}
