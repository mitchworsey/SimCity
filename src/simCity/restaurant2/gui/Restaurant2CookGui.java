package simCity.restaurant2.gui;

import simCity.gui.Gui;

import simCity.restaurant2.Restaurant2CookRole;
import simCity.restaurant2.Restaurant2CookRole.Order;
import simCity.restaurant2.interfaces.Restaurant2Cook;

import java.awt.*;


/** COOKGUI WILL NOT BE USED FOR V2!!!
 * 	ALL MOVEMENTS AND FUNCTIONALITY BESIDES ACCESSORS DISABLED
 */
public class Restaurant2CookGui implements Gui {

    private Restaurant2Cook agent = null;

    private int xPos = 800, yPos = 350; //default start position
    private int xDestination = 800, yDestination = 350; //default start position
    
    private final int REFRIGERATORX = 545;
    private final int REFRIGERATORY = 175;
    private final int REFRIGERATORWIDTH = 45;
    private final int REFRIGERATORHEIGHT = 45;
    
    private final int CHEFGRILLX = 850;
    private final int CHEFGRILLY = 280;
    private final int CHEFGRILLHEIGHT = 40;
    private final int CHEFGRILLWIDTH = 40;
    
    private final int PLATEGRILLX = 730;
    private final int PLATEGRILLY = 280;
    private final int PLATEGRILLWIDTH = 40;
    private final int PLATEGRILLHEIGHT = 40;
    
    private final int agentWidth = 20;
    private final int agentHeight = 20;
    private final int foodWidth = 20;
    private final int foodHeight = 20;
    
    private enum Command 
    {NoCommand, SeatCustomer, GoHome, GoToTable, DeliverOrder, PickUpOrder, GoToCashier, GoOnBreak, GoToCustomer};
    private Command command = Command.NoCommand;
    
    private enum State
    {NoState, TakeOrder, OrderTaken, CarryFood, DeliverFood, FoodDelivered, CleanTable, TableCleaned}
    private State state = State.NoState;
    
    public String grill[] = new String[4];
    public String plate[] = new String[4];
    public Order order[] = new Order[4];

    public Restaurant2CookGui(Restaurant2Cook agent) {
        this.agent = agent;
        for (int i=0; i<4; i++) {
        	grill[i] = null;
        	plate[i] = null;
        }
    }

    public void updatePosition() {
    	 if (xPos < xDestination) {
             xPos++;
     	 }
         else if (xPos > xDestination) {
             xPos--;
         }

         if (yPos < yDestination) {
         	yPos++;
         }
         else if (yPos > yDestination) {
             yPos--;
         }

 
         if (xPos == xDestination && yPos == yDestination) {
      
         }
    }

    public void draw(Graphics2D g) {
    	g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, agentWidth, agentHeight);
		g.setColor(Color.BLACK);
        g.drawString(agent.getName().substring(0,2), xPos, yPos + 2*agentHeight/3);
    	
    	
    	for (int i=0; i<4; i++) {
    		if (grill[i] != null) {
    			g.setColor(Color.RED);
    			g.fillRect(CHEFGRILLX + foodWidth/2, CHEFGRILLY + foodHeight/2 + i*CHEFGRILLHEIGHT, foodWidth, foodHeight);
    	        g.setColor(Color.BLACK);
    	        g.drawString(grill[i].substring(0,2), CHEFGRILLX + foodWidth/2, CHEFGRILLY + foodHeight/2 + i*CHEFGRILLHEIGHT + 2*foodHeight/3);
    	        g.drawRect(CHEFGRILLX + foodWidth/2, CHEFGRILLY + foodHeight/2 + i*CHEFGRILLHEIGHT, foodWidth, foodHeight);
    		}
    		if (plate[i] != null) {
    			g.setColor(Color.CYAN);
    			g.fillRect(PLATEGRILLX + foodWidth/2, PLATEGRILLY + foodHeight/2 + i*PLATEGRILLHEIGHT, foodWidth, foodHeight);
    	        g.setColor(Color.BLACK);
    	        g.drawString(plate[i].substring(0,2), PLATEGRILLX + foodWidth/2, PLATEGRILLY + foodHeight/2 + i*PLATEGRILLHEIGHT + 2*foodHeight/3);
    	        g.drawRect(PLATEGRILLX + foodWidth/2, PLATEGRILLY + foodHeight/2 + i*PLATEGRILLHEIGHT, foodWidth, foodHeight);
    		}
    	}
    }
    
    public void DoCooking(Order o) {
		int index = 0;
		for (int i=0; i<4; i++) {
			if (grill[i] == null) {
				index = i;
				grill[i] = o.foodChoice;
				break;
			}
		}
		order[index] = o;
	}

	public void DoPlating(String foodChoice) {
		// No animation for v2, do nothing
	}
	
	public void DoServing(Order o) {
		for (int i=0; i<4; i++) {
			if (order[i] == o) {
				plate[i] = grill[i];
				grill[i] = null;
			}
		}
	}

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	@Override
	public int getX() {
		return xPos;
	}

	@Override
	public int getY() {
		return yPos;
	}

	@Override
	public void setDestination(int x, int y) {
		
	}

}
