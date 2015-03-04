package simCity.Restaurant3.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.xml.soap.Node;

import simCity.gui.Gui;
import simCity.gui.aStar;

public class Restaurant3FoodGui implements Gui{

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    private int counter;
    private int xPlatingPos = 424;
    private int yPlatingPos = 170;
    private int xCookingPos = 424;
    private int yCookingPos = 110;
    
    private ImageIcon imageIconChicken = new ImageIcon("src/simCity/gui/images/chicken3.png");
    private Image imageChicken = imageIconChicken.getImage();
    private ImageIcon imageIconSteak = new ImageIcon("src/simCity/gui/images/steak3.png");
    private Image imageSteak = imageIconSteak.getImage();
    private ImageIcon imageIconSalad = new ImageIcon("src/simCity/gui/images/salad3.png");
    private Image imageSalad = imageIconSalad.getImage();
    private ImageIcon imageIconPizza = new ImageIcon("src/simCity/gui/images/pizza3.gif");
    private Image imagePizza = imageIconPizza.getImage();
    
    
    //RestaurantGui gui;
    String type;
    private boolean ate = false;
    private boolean ordered = false;
    private boolean cooked = false;
    private boolean atFridge = false;
    int table;
    
    JLabel foodLabel;
    
    public static final int xTable1 = 180;
	public static final int yTable1 = 220;
	public static final int xTable2 = 180;
	public static final int yTable2 = 100;
    public static final int xTable3 = 300;
    public static final int yTable3 = 220;
    public static final int xTable4 = 300;
    public static final int yTable4 = 100;

    
    public Restaurant3FoodGui(String type, int table, int xPos, int yPos){ //HostAgent m) {
		this.xPos = xPos;
		this.yPos = yPos;
		xDestination = xPos;
		yDestination = yPos;
		this.type = type;
		this.table = table;
		//maitreD = m;
		//this.gui = gui;
	}
    

    public void foodEaten(){
    	ate = true;
    }
    public void foodAtFridge(){
    	atFridge = true;
    }
    
    public void foodCooked(){
    	cooked = true;
    }
    
    public void foodOrdered(){
    	ordered = true;
    }
    
    public void updatePosition() {
        if (xPos < xDestination)
            xPos+=1;
        else if (xPos > xDestination)
            xPos-=1;

        if (yPos < yDestination)
            yPos+=1;
        else if (yPos > yDestination)
            yPos-=1;
        
    }

    public void draw(Graphics2D g) {
    	if(!ate){
	    	if(cooked){
	    		if(type == "Steak"){
	    			if (imageSteak != null)
	    	            g.drawImage(imageSteak,xPos, yPos, 20, 20, null);
	    			//g.setColor(Color.BLACK);
	    			//g.fillRect(xPos, yPos, 10, 10);
	    			//g.drawString(type, xPos, yPos);
	    		}
	    		else if(type == "Chicken"){
	    			if (imageChicken != null)
	    	            g.drawImage(imageChicken,xPos, yPos, 20, 20, null);
	    			//g.setColor(Color.BLACK);
	    			//g.fillRect(xPos, yPos, 10, 10);
	    			//g.drawString(type, xPos, yPos);
	    		}
	    		else if(type == "Pizza"){
	    			if (imagePizza != null)
	    	            g.drawImage(imagePizza,xPos, yPos, 20, 20, null);
	    			//g.setColor(Color.BLACK);
	    			//g.fillRect(xPos, yPos, 10, 10);
	    			//g.drawString(type, xPos, yPos);
	    		}
	    		else if(type == "Salad"){
	    			if (imageSalad != null)
	    	            g.drawImage(imageSalad,xPos, yPos, 20, 20, null);
	    			//g.setColor(Color.BLACK);
	    			//g.fillRect(xPos, yPos, 10, 10);
	    			//g.drawString(type, xPos, yPos);
	    		}
	    	}
	    	else if(ordered){
	    		g.setColor(Color.BLACK);
				g.fillRect(xPos, yPos, 10, 10);
				g.drawString(type, xPos, yPos);
	    	}
	    	else if(atFridge){
	    		g.setColor(Color.BLACK);
				g.fillRect(xPos, yPos, 10, 10);
				g.drawString(type, xPos, yPos);
	    	}
    	}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(int tableNumber) {
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
        //xDestination = xTable + 20;
        //yDestination = yTable - 20;
    }
    
    public void DoGoToCookingArea(){
    	//if((counter % 4) == 0){
		//    yCookingPos = 90;
		//}
		//else if((counter % 4) == 1){
			//yCookingPos = 110;	
		//}
		/*else if((counter % 4) == 2){
			yCookingPos = 130;
		}
		else if((counter % 4) == 3){
			yCookingPos = 150;
		}*/
		//counter++;
    	xDestination = xCookingPos;
    	yDestination = yCookingPos;
    }
    
    public void DoGoToPlatingArea(){
	    //if((counter % 4) == 0){
			//yPlatingPos = 170;
		/*}
		else if((counter % 4) == 1){
			yPlatingPos = 190;	
		}
		else if((counter % 4) == 2){
			yPlatingPos = 210;
		}
		else if((counter % 4) == 3){
			yPlatingPos = 230;
		}
		counter++;*/
		xDestination = xPlatingPos;
		yDestination = yPlatingPos;
    }
    
    public void DoGoToCook(){
    	xDestination = 424;
    	yDestination = 140;
    }
    
    
    public void DoGoToFridge(){
    	xDestination = 520;
    	yDestination = 70;
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
		
	}
}
