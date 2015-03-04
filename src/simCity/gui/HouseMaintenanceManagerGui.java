package simCity.gui;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simCity.interfaces.HouseMaintenanceManager;
import simCity.interfaces.HouseResident;
import simCity.interfaces.Person;

public class HouseMaintenanceManagerGui implements Gui{
	private HouseMaintenanceManager role = null;
	private boolean atDestination = false;
	//private Restaurant2HostRole host;
	HousingOfficePanel officeGui;
	HousePanel houseGui;
	//private int xWaitPos = 10;
	//private int yWaitPos = 0;
	

    private aStar newAStar;
    private List<Node> finalList = Collections.synchronizedList(new ArrayList<Node>());
    private Node tempNode;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
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
	public static final int xOfficeDoorPos = 420;
    public static final int yOfficeDoorPos = 500;
    public static final int xRealEstatePos = 360;
    public static final int yRealEstatePos = 250;
    public static final int xWorkOrderPos = 650;
    public static final int yWorkOrderPos = 200;
    public static final int xMaintenancePos = 500;
	public static final int yMaintenancePos = 200;

	public HouseMaintenanceManagerGui(HouseMaintenanceManager hmm, HousingOfficePanel gui){ //Restaurant2HostRole m) {
		role = hmm;
		xPos = 500;
		yPos = 200;
		
		//Destination just for testing
		xDestination = 500;
		yDestination = 200;
        newAStar = null;
        //finalList = newAStar.getBestPath();
        
		//maitreD = m;
		this.officeGui = gui;
	}
	
	public HouseMaintenanceManagerGui(HouseMaintenanceManager hmm, HousePanel gui){ //Restaurant2HostRole m) {
		role = hmm;
		xPos = 500;
		yPos = 200;
		
		//Destination just for testing
		xDestination = 500;
		yDestination = 200;
        newAStar = null;
        finalList = newAStar.getBestPath();
        
		//maitreD = m;
		this.houseGui = gui;
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
			xPos+=1;
		else if (xPos > xDestination)
			xPos-=1;

		if (yPos < yDestination)
			yPos+=1;
		else if (yPos > yDestination)
			yPos-=1;

    	/*if (!finalList.isEmpty()){
    		if (finalList.size()>=1){
    			//simCity.gui.CityPanel.grid.release(xPos/10, yPos/10);
    			xPos = finalList.get(finalList.size()-1).getX();
    			yPos = finalList.get(finalList.size()-1).getY();
    			//simCity.gui.CityPanel.grid.set(xPos/10, yPos/10);
    			finalList.remove(finalList.get(finalList.size()-1));
    		}
    	}
*/
		
		if (xPos == xDestination && yPos == yDestination & (xPos == xStovePos && yPos == yStovePos)){
			if(!atDestination)
				role.msgAtStove();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xBedPos && yPos == yBedPos)){
			if(!atDestination)
				role.msgAtBed();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xFridgePos && yPos == yFridgePos)){
			if(!atDestination)
				role.msgAtFridge();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xTablePos && yPos == yTablePos)){
			if(!atDestination)
				role.msgAtTable();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xSinkPos && yPos == ySinkPos)){
			if(!atDestination)
				role.msgAtSink();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xDishWasherPos && yPos == yDishWasherPos)){
			if(!atDestination)
				role.msgAtDishWasher();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xWasherPos && yPos == yWasherPos)){
			if(!atDestination)
				role.msgAtWasher();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xShowerPos && yPos == yShowerPos)){
			if(!atDestination)
				role.msgAtShower();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xHouseDoorPos && yPos == yHouseDoorPos)){
			if(!atDestination)
				role.msgLeftHouse();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xOfficeDoorPos && yPos == yOfficeDoorPos)){
			if(!atDestination)
				role.msgLeftOffice();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xMaintenancePos && yPos == yMaintenancePos)){
			if(!atDestination)
				role.msgAtMaintenance();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xWorkOrderPos && yPos == yWorkOrderPos)){
			if(!atDestination)
				role.msgPlacedWorkOrder();
        }
		
	}

	public void draw(Graphics2D g) {
    	Graphics2D g1 = (Graphics2D)g;
		g1.setColor(Color.GREEN);
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

	
	public void DoPlaceWorkOrder(){
		xDestination = xWorkOrderPos;
		yDestination = yWorkOrderPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToBed(){
		xDestination = xBedPos;
		yDestination = yBedPos;
		setDestination(xDestination, yDestination);
	}

	public void DoLeaveHouse() {
		xDestination = xHouseDoorPos;
		yDestination = yHouseDoorPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoLeaveOffice() {
		xDestination = xOfficeDoorPos;
		yDestination = yOfficeDoorPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToStove(){
		xDestination = xStovePos;
		yDestination = yStovePos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToTable(){
		xDestination = xTablePos;
		yDestination = yTablePos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToFridge(){
		xDestination = xFridgePos;
		yDestination = yFridgePos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToSink(){
		xDestination = xSinkPos;
		yDestination = ySinkPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToDishWasher(){
		xDestination = xDishWasherPos;
		yDestination = yDishWasherPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToWasher(){
		xDestination = xWasherPos;
		yDestination = yWasherPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToShower(){
		xDestination = xShowerPos;
		yDestination = yShowerPos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToMaintenance(){
		xDestination = xMaintenancePos;
		yDestination = yMaintenancePos;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToRealEstate(){
		xDestination = xRealEstatePos;
		yDestination = yRealEstatePos;
		setDestination(xDestination, yDestination);
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
