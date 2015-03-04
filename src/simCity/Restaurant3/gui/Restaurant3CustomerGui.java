package simCity.Restaurant3.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.soap.Node;

import simCity.Restaurant3.interfaces.Restaurant3Customer;
import simCity.gui.Gui;
import simCity.gui.Restaurant1Panel;
import simCity.gui.Restaurant3Panel;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;

public class Restaurant3CustomerGui implements Gui{
	private aStar newAStar;
    private List<simCity.gui.Node> finalList = Collections.synchronizedList(new ArrayList<simCity.gui.Node>());
    private Node tempNode;
	
	
	public Restaurant3Customer agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	private boolean atDestination = false;

	private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/customer3.png");
    private Image image = imageIcon.getImage();
	
	//private HostAgent host;
    Restaurant3Panel gui;
	private int xWaitPos = 10;
	private int yWaitPos = 0;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	//public static final int xTable = 200;
	//public static final int yTable = 250;
	
	public static final int xTable1 = 160;
	public static final int yTable1 = 220;
	public static final int xTable2 = 160;
	public static final int yTable2 = 100;
    public static final int xTable3 = 280;
    public static final int yTable3 = 220;
    public static final int xTable4 = 280;
    public static final int yTable4 = 100;

	public Restaurant3CustomerGui(Restaurant3Customer c, Restaurant3Panel gui){ //HostAgent m) {
		agent = c;
		newAStar = null;
		xPos = 0;
		yPos = 0;
		xDestination = 0;
		yDestination = 0;
		//maitreD = m;
		this.gui = gui;
		setDestination(xDestination, yDestination);
	}
	
	public void setYWaitPos(int y){
		yWaitPos += y;
		//xDestination = yWaitPos;
	}
	public void setXWaitPos(int x){
		xWaitPos += x;
		//xDestination = xWaitPos;
	}
	
	public int getXWaitPos(){
		return xWaitPos;
	}
	public int getYWaitPos(){
		return yWaitPos;
	}
	public int getYPos(){
		return yPos;
	}

	public void updatePosition() {
		/*
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
		*/
		
		if (finalList != null){
     		if (!finalList.isEmpty()){
     			if (finalList.size()>=1){
     				xPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getX();
     				yPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getY();
     				finalList.remove(finalList.get(finalList.size()-1));
     			}
     		}
     	}
		
		
		if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10) {
			if (command==Command.GoToSeat) 
				agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
		
		if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == 44/10 && yPos/10 == 250/10)){
			//if(!atDestination)
				agent.msgAtCashier();
        }
		if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xWaitPos/10 && yPos/10 == yWaitPos/10)){
			//if(!atDestination)
				agent.msgAtWaitingArea();
        }
	}

	public void draw(Graphics2D g) {
		if (image != null)
			g.drawImage(image, xPos, yPos, 20, 50, null);
	
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int tableNumber) {//later you will map seatnumber to table coordinates.
		//System.out.println("DoGoToSeat" + tableNumber);
		if(tableNumber==1){
			xDestination = xTable1;
			yDestination = yTable1;
		}
		if(tableNumber==2){
			xDestination = xTable2;
			yDestination = yTable2;
		}
		if(tableNumber==3){
			xDestination = xTable3;
			yDestination = yTable3;
		}
		if(tableNumber==4){
			xDestination = xTable4;
			yDestination = yTable4;
		}
		//xDestination = xTable;
		//yDestination = yTable;
		command = Command.GoToSeat;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToWaitingArea(){
		xDestination = xWaitPos;
		yDestination = yWaitPos;
		setDestination(xDestination, yDestination);
	}

	public void DoExitRestaurant() {
		xDestination = 10;
		yDestination = 10;
		command = Command.LeaveRestaurant;
		setDestination(xDestination, yDestination);
	}
	
	public void DoGoToCashier(){
		xDestination = 44;
		yDestination = 250;
		setDestination(xDestination, yDestination);
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
		xDestination = x;
    	yDestination = y;
        newAStar = new aStar(xPos, yPos, xDestination, yDestination, Restaurant3Panel.grid);
        finalList = newAStar.getBestPathSteps();
	}
}
