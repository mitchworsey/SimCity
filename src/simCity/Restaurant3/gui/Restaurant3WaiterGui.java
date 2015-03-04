package simCity.Restaurant3.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.soap.Node;

import simCity.Restaurant3.interfaces.Restaurant3Waiter;
import simCity.gui.Gui;
import simCity.gui.Restaurant1Panel;
import simCity.gui.Restaurant3Panel;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;

public class Restaurant3WaiterGui implements Gui{
    private Restaurant3Waiter agent = null;
	private boolean atDestination = false;

	private aStar newAStar;
    private List<simCity.gui.Node> finalList = Collections.synchronizedList(new ArrayList<simCity.gui.Node>());
    private Node tempNode;
	
    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination = 0, yDestination = 0;//default start position

    Restaurant3Panel gui;
       
    private ArrayList<Restaurant3FoodGui> foodGuis = new ArrayList<Restaurant3FoodGui>();
    
    private int xWatchPos = 0;
    private int yWatchPos = 20;
    
    private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/waiter3.png");
    private Image image = imageIcon.getImage();
    
    private int xCustPos = 0;
    private int yCustPos = 0;
    
    public static final int xTable1 = 180;
	public static final int yTable1 = 220;
	public static final int xTable2 = 180;
	public static final int yTable2 = 100;
    public static final int xTable3 = 300;
    public static final int yTable3 = 220;
    public static final int xTable4 = 300;
    public static final int yTable4 = 100;


    
    public Restaurant3WaiterGui(Restaurant3Waiter w, Restaurant3Panel gui){ //HostAgent m) {
		agent = w;
		newAStar = null;
		xPos = 0;
		yPos = 20;
		xDestination = 0;
		yDestination = 20;
		//maitreD = m;
		this.gui = gui;
		setDestination(xDestination, yDestination);
	}
    
    public int getXWatchPos(){
    	return xWatchPos;
    }
    
    public void setXWatchPos(int x){
    	xWatchPos += x;
    	xPos = xWatchPos;
    }
    
    public void setWantsToGoOnBreak() {
		agent.msgWantsToGoOnBreak();
		//setPresent(true);
	}
    public void setBackToWork(){
    	agent.msgBackToWork();
    }
    
    public boolean wantToGoOnBreak(){
    	return agent.wantToGoOnBreak();
    }
    public boolean isOKToGoOnBreak(){
    	return agent.isOKToGoOnBreak();
    }
    public boolean isOnBreak(){
    	return agent.isOnBreak();
    }
    

    public void updatePosition() {
    	atDestination = false;
		if((xPos == xDestination && yPos == yDestination) && !atDestination)
			atDestination = true;
		/*
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
		
		
		
        if ((xPos/10 == xDestination/10 && yPos/10 == yDestination/10)
        		& ((xDestination/10 == (xTable1 + 20)/10) & (yDestination/10 == (yTable1 - 50)/10))) {
        	if(!atDestination)
				agent.msgAtTable();
        }
        else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10
        		& (xDestination/10 == (xTable2 + 20)/10) & (yDestination/10 == (yTable2 - 50)/10)) {
        	if(!atDestination)
				agent.msgAtTable();
        }
        else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10
        		& (xDestination/10 == (xTable3 + 20)/10) & (yDestination/10 == (yTable3 - 50)/10)) {
        	if(!atDestination)
				agent.msgAtTable();
        }
        else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10
        		& (xDestination/10 == (xTable4 + 20)/10) & (yDestination/10 == (yTable4 - 50)/10)) {
        	if(!atDestination)
				agent.msgAtTable();
        }
        
        else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == 390/10 && yPos/10 == 110/10)){
        	if(!atDestination)
				agent.msgAtCook();
        }
        
        else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == 390/10 && yPos/10 == 170/10)){
        	if(!atDestination)
				agent.msgAtCook();
        }
        
        else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xCustPos/10 && yPos/10 == yCustPos/10)){
        	if(!atDestination)
				agent.msgAtHost();
        }
        
        else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == 44/10 && yPos/10 == 250/10)){
        	if(!atDestination)
				agent.msgAtCashier();
        }
        
        
       /* if(xPos == -20 && yPos == -20){
        	agent.readyToBeSeated();
        	
        }*/
    }
    
    /*public void setFoodGui(String type){
    	foodGui = new FoodGui(type, gui);
    }
    */
    public void FoodGuiGoToCookingArea(String type, int table){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(type) && fg.table == table){
    			fg.DoGoToCookingArea();
    		}
    		
    	}
    }
    
    public void FoodGuiSetCounter(String choice, int tableNumber){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == tableNumber){
    			//fg.setCounter();
    		}
    		
    	}
    }
    public void FoodGuiGoToCook(String choice, int tableNumber){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == tableNumber){
    			fg.DoGoToCook();
    		}
    		
    	}
    }
    public void FoodGuiGoToPlatingArea(String type, int table){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(type) && fg.table == table){
    			fg.DoGoToPlatingArea();
    		}
    	}
    }
    
    public void createFoodGui(String type, int table){
    	Restaurant3FoodGui fg = new Restaurant3FoodGui(type, table, xPos, yPos);
    	foodGuis.add(fg);
    	gui.addGui(fg);
    }
    
    public void FoodGuiGoToCustomer(String choice, int tableNumber){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == tableNumber){
    			fg.DoGoToTable(tableNumber);
    		}
    		
    	}
    }
    
    public void foodOrdered(String choice, int table){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodOrdered();
    		}
    	}
    }
    
    public void foodCooked(String choice, int table){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodCooked();
    		}
    	}
    }
    
    public void foodAte(String choice, int table){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodEaten();
    		}
    		
    	}
    }
    public void removeFoodGui(String choice, int table){
    	for(Restaurant3FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodEaten();
    		}	
    	}
    }

    public void draw(Graphics2D g) {
    	if (image != null)
            g.drawImage(image, xPos, yPos, 35, 50, null);
    	
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(int tableNumber) {
    	if(tableNumber==1){
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 50;
		}
    	if(tableNumber==2){
			xDestination = xTable2 + 20;
			yDestination = yTable2 - 50;
		}
    	if(tableNumber==3){
			xDestination = xTable3 + 20;
			yDestination = yTable3 - 50;
		}
    	if(tableNumber==4){
			xDestination = xTable4 + 20;
			yDestination = yTable4 - 50;
		}
        //xDestination = xTable + 20;
        //yDestination = yTable - 20;
    	setDestination(xDestination, yDestination);
    }
    
    public void DoGoToCookingArea(){
    	xDestination = 390;
    	yDestination = 110;
    	setDestination(xDestination, yDestination);
    }
    
    public void DoGoToPlatingArea(){
    	xDestination = 390;
    	yDestination = 170;
    	setDestination(xDestination, yDestination);
    }    
    public void DoWatch(){
    	xDestination = xWatchPos;
    	yDestination = yWatchPos;
    	setDestination(xDestination, yDestination);
    }
    
    public void DoGoToHost(){
    	xDestination = 30;
    	yDestination = 30;
    	setDestination(xDestination, yDestination);
    }
    
    public void DoGoToCashier(){
    	xDestination = 44;
    	yDestination = 250;
    	setDestination(xDestination, yDestination);
    }
    
    public void DoGoToCustomer(int x, int y){
    	xCustPos = x;
    	yCustPos = y;
    	xDestination = xCustPos;
    	yDestination = yCustPos;
    	setDestination(xDestination, yDestination);
    }

    public void DoLeaveCustomer() {
        xDestination = 50;
        yDestination = 400;
        setDestination(xDestination, yDestination);
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
