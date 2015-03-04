package simCity.Restaurant3.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.soap.Node;

import simCity.Restaurant3.interfaces.Restaurant3Cook;
import simCity.gui.Gui;
import simCity.gui.Restaurant1Panel;
import simCity.gui.Restaurant3Panel;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;

public class Restaurant3CookGui implements Gui {
	 private Restaurant3Cook agent = null;
	 
	 private aStar newAStar;
     private List<simCity.gui.Node> finalList = Collections.synchronizedList(new ArrayList<simCity.gui.Node>());
     private Node tempNode;
 
     private boolean atDestination = false;

     private int xPos = 0, yPos = 0;//default Cook position
     private int xDestination = -20, yDestination = -20;//default start position

     int counter = 0;
     private int xPlatingPos = 450;
     private int yPlatingPos;
     private int xCookingPos = 450;
     private int yCookingPos;
     private int xFridgePos = 520;
     private int yFridgePos = 70;
     
     
     Restaurant3Panel gui;
     
     private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/cook3.png");
     private Image image = imageIcon.getImage();
     
     private ArrayList<Restaurant3FoodGui> foodGuis = new ArrayList<Restaurant3FoodGui>();
     
     private int xWatchPos = 450;
     private int yWatchPos = 140;
     
     public Restaurant3CookGui(Restaurant3Cook c, Restaurant3Panel gui){// HostAgent m) {
    	 agent = c;
    	 newAStar = null;
    	 xPos = 450;
    	 yPos = 140;
    	 xDestination = 450;
    	 yDestination = 140;
    	 //maitreD = m;
    	 this.gui = gui;
    	 setDestination(xDestination, yDestination);
     }
     
     public int getXWatchPos(){
    	 return xWatchPos;
     }
     
     public void setXWatchPos(int x){
    	 xWatchPos += x;
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
    	 
    	 
    	 if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xCookingPos/10 && yPos/10 == yCookingPos/10)){
    		// if(!atDestination)
    		 	agent.msgAtCookingArea();
    	 }
    	 else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == xPlatingPos/10 && yPos/10 == yPlatingPos/10)){
    		 //if(!atDestination)
    		 	agent.msgAtPlatingArea();
    	 }
    	 else if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10 & (xPos/10 == 520/10 && yPos/10 == 70/10)){
    		 //if(!atDestination)
    		 	agent.msgAtFridge();
    	 }
     }
    
     public void createFoodGui(String type, int table){
    	 Restaurant3FoodGui fg = new Restaurant3FoodGui(type, table, xPos, yPos);
    	 foodGuis.add(fg);
    	 gui.addGui(fg);
     }
    
     public void FoodGuiGoToCookingArea(String type, int table){
    	 for(Restaurant3FoodGui fg: foodGuis){
    		 if(fg.type.equals(type) && fg.table == table){
    			 fg.DoGoToCookingArea();
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
     public void foodCooked(String choice, int table){
    	 for(Restaurant3FoodGui fg: foodGuis){
    		 if(fg.type.equals(choice) && fg.table == table){
    			 fg.foodCooked();
    		 }
    	 }
     }
     public void FoodGuiGoToFridge(String type, int table){
    	 for(Restaurant3FoodGui fg: foodGuis){
    		 if(fg.type.equals(type) && fg.table == table){
    			 fg.DoGoToFridge();
    		 }
    	 }
     }
     public void foodAtFridge(String choice, int table){
    	 for(Restaurant3FoodGui fg: foodGuis){
    		 if(fg.type.equals(choice) && fg.table == table){
    			 fg.foodAtFridge();
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
    		 g.drawImage(image, xPos, yPos-10, 20, 50, null);
     }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToCookingArea(){
    	//if((counter % 4) == 0){
		    yCookingPos = 90;
		/*}
		else if((counter % 4) == 1){
			yCookingPos = 110;	
		}
		else if((counter % 4) == 2){
			yCookingPos = 130;
		}
		else if((counter % 4) == 3){
			yCookingPos = 150;
		}
		counter++;*/
    	xDestination = xCookingPos;
    	yDestination = yCookingPos;
    	setDestination(xDestination, yDestination);
    }
    
    public void DoGoToPlatingArea(){
    	//if((counter % 4) == 0){
    		yPlatingPos = 170;
		/*}
		else if((counter % 4) == 1){
			yPlatingPos = 190;	
		}
		else if((counter % 4) == 2){
			yPlatingPos = 210;
		}
		else if((counter % 4) == 3){
			yPlatingPos = 220;
		}
    	counter++;*/
    	xDestination = xPlatingPos;
    	yDestination = yPlatingPos;
    	setDestination(xDestination, yDestination);
    }    
    public void DoWatch(){
    	xDestination = xWatchPos;
    	yDestination = yWatchPos;
    	setDestination(xDestination, yDestination);
    }
    public void DoGoToFridge(){
    	xDestination = xFridgePos;
    	yDestination = yFridgePos;
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
