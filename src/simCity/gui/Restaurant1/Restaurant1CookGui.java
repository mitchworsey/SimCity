package simCity.gui.Restaurant1;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.xml.soap.Node;

import simCity.BusStopAgent;
import simCity.Location;
import simCity.OrdinaryPerson;
import simCity.Role;
import simCity.Restaurant1.Restaurant1CookRole;
import simCity.gui.Gui;
import simCity.gui.Restaurant1Panel;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;
import simCity.interfaces.Person;
import simCity.interfaces.Restaurant1Cook;
import simCity.interfaces.Restaurant1Customer;

public class Restaurant1CookGui implements Gui{

        Restaurant1Panel gui;
        
        static final int HEIGHT = 38;
        static final int HEIGHTWF = 65;
        static final int WIDTH = 27;
        static final int DOORX = 200;
        static final int DOORY = 100;
        static final int STARTX = 720;
        static final int STARTY = 200;
        static final int FRIDGEX = 720;
        static final int FRIDGEY = 100;
        static final int COUNTERX = 720;
        static final int COUNTERY = 120;
        static final int STOVEX = 750;
        static final int STOVEY = 120;

        private boolean isPresent = true;
        private aStar newAStar;
        private List<simCity.gui.Node> finalList = Collections.synchronizedList(new ArrayList<simCity.gui.Node>());
        private Node tempNode;
        
        
        public Restaurant1Cook agent = null;
        
        private int xPos = DOORX;
    	private int yPos = DOORY;
        private int xDestination = STARTX;
        private int yDestination = STARTY;
        private int pizza = 0;
        private int chicken = 0;
        private int steak = 0;
        private int salad = 0;
        private int pizzaReady = 0;
        private int saladReady = 0;
        private int steakReady = 0;
        private int chickenReady = 0;
        private boolean open = false;
        
        //Images
    	private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/cook.PNG");
    	private Image image = imageIcon.getImage();
    	private ImageIcon imageIconCookSalad = new ImageIcon("src/simCity/gui/images/cookSalad.PNG");
    	private Image imageCookSalad = imageIconCookSalad.getImage();
    	private ImageIcon imageIconCookSteak = new ImageIcon("src/simCity/gui/images/cookSteak.PNG");
    	private Image imageCookSteak = imageIconCookSteak.getImage();
    	private ImageIcon imageIconCookPizza = new ImageIcon("src/simCity/gui/images/cookPizza.PNG");
    	private Image imageCookPizza = imageIconCookPizza.getImage();
    	private ImageIcon imageIconCookChicken = new ImageIcon("src/simCity/gui/images/cookChicken.PNG");
    	private Image imageCookChicken = imageIconCookChicken.getImage();
    	private ImageIcon imageIconSalad = new ImageIcon("src/simCity/gui/images/saladPlate.JPG");
    	private Image imageSalad = imageIconSalad.getImage();
    	private ImageIcon imageIconSteak = new ImageIcon("src/simCity/gui/images/steakPlate.JPG");
    	private Image imageSteak = imageIconSteak.getImage();
    	private ImageIcon imageIconPizza = new ImageIcon("src/simCity/gui/images/pizzaPlate.JPG");
    	private Image imagePizza = imageIconPizza.getImage();
    	private ImageIcon imageIconChicken = new ImageIcon("src/simCity/gui/images/chickenPlate.JPG");
    	private Image imageChicken = imageIconChicken.getImage();
    	
    	Timer timer = new Timer();

        final int agentWidth = 20;
        final int agentHeight = 20;
        

    	public static final int xTable = 50;
    	public static final int yTable = 250;
        
        public Restaurant1CookGui(Restaurant1Cook agent, Restaurant1Panel gui, int ct) { 
                newAStar = null;
                agent.setGui(this);
                this.gui = gui;
                this.agent = agent;
                
                setDestination(STARTX, STARTY);
        }

		public void updatePosition() {
        	if (finalList != null){
        		if (!finalList.isEmpty()){
        			if (finalList.size()>=1){
        				xPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getX();
        				yPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getY();
        				finalList.remove(finalList.get(finalList.size()-1));
        			}
        		}
        	}
        }
        

        public void draw(Graphics2D g) {
        	if (salad == 0 && steak == 0 && chicken == 0 && pizza == 0){
        		if (image != null)
        			g.drawImage(image, getxPos(), getyPos(), WIDTH, HEIGHT, null);
        	}
        	else if (salad == 1){
        		if (image != null)
        			g.drawImage(imageCookSalad, getxPos(), getyPos()-HEIGHT, WIDTH, HEIGHTWF, null);
        	}
        	else if (steak == 1){
        		if (image != null)
        			g.drawImage(imageCookSteak, getxPos(), getyPos()-HEIGHT, WIDTH, HEIGHTWF, null);
        	}
        	else if (pizza == 1){
        		if (image != null)
        			g.drawImage(imageCookPizza, getxPos(), getyPos()-HEIGHT, WIDTH, HEIGHTWF, null);
        	}
        	else if (chicken == 1){
        		if (image != null)
        			g.drawImage(imageCookChicken, getxPos(), getyPos()-HEIGHT, WIDTH, HEIGHTWF, null);
        	}
        	if (imageChicken != null) {
        		for (int i = 0; i < chickenReady; i++){
        			g.drawImage(imageChicken, STARTX-i*WIDTH, STARTY-WIDTH*2, WIDTH, WIDTH, null);
        		}
        	}
        	if (imageSteak != null) {
        		for (int i = 0; i < steakReady; i++){
        			g.drawImage(imageSteak, STARTX-i*WIDTH, STARTY-WIDTH, WIDTH, WIDTH, null);
        		}
        	}
        	if (imageSalad != null) {
        		for (int i = 0; i < saladReady; i++){
        			g.drawImage(imageSalad, STARTX-i*WIDTH, STARTY, WIDTH, WIDTH, null);
        		}
        	}
        	if (imagePizza != null) {
        		for (int i = 0; i < pizzaReady; i++){
        			g.drawImage(imagePizza, STARTX-i*WIDTH, STARTY+WIDTH, WIDTH, WIDTH, null);
        		}
        	}
        }
        
        public boolean isPresent() {
                return isPresent;
        }
        
        public void setPresent(boolean p) {
                isPresent = p;
        }
        
        public void getSalad() {
        	setDestination(FRIDGEX, FRIDGEY);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(COUNTERX, COUNTERY);
    				open = true;
    				salad = 1;
    			}
    		},
    		1000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				open = false;
    			}
    		},
    		1500);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(STARTX, STARTY);
    			}
    		},
    		4000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				saladReady += 1;
    				salad = 0;
    			}
    		},
    		5000);
        }
        
        public void getPizza() {
        	setDestination(FRIDGEX, FRIDGEY);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(STOVEX, STOVEY);
    				open = true;
    				pizza = 1;
    			}
    		},
    		1000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				open = false;
    			}
    		},
    		1500);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(STARTX, STARTY);
    			}
    		},
    		5000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				pizzaReady += 1;
    				pizza = 0;
    			}
    		},
    		6000);
        }
        
        public void getSteak() {
        	setDestination(FRIDGEX, FRIDGEY);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(STOVEX, STOVEY);
    				open = true;
    				steak = 1;
    			}
    		},
    		1000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				open = false;
    			}
    		},
    		1500);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(STARTX, STARTY);
    			}
    		},
    		7000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				steakReady += 1;
    				steak = 0;
    			}
    		},
    		8000);
        }
        
        public void getChicken() {
        	setDestination(FRIDGEX, FRIDGEY);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(STOVEX, STOVEY);
    				open = true;
    				chicken = 1;
    			}
    		},
    		1000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				open = false;
    			}
    		},
    		1500);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				setDestination(STARTX, STARTY);
    			}
    		},
    		5000);
        	timer.schedule(new TimerTask() {
    			public void run() {
    				chickenReady += 1;
    				chicken = 0;
    			}
    		},
    		6000);
        }
        
        public void clearChicken() {
        	chickenReady -= 1;
        }
        
        public void clearSteak() {
        	steakReady -= 1;
        }
        
        public void clearSalad() {
        	saladReady -= 1;
        }
        
        public void clearPizza() {
        	pizzaReady -=1;
        }

        public int getXPos() {
            return getxPos();
        }

        public int getYPos() {
            return getyPos();
        }

    	public int getxPos() {
    		return xPos;
    	}

    	public void setxPos(int xPos) {
    		this.xPos = xPos;
    	}

    	public int getyPos() {
    		return yPos;
    	}

    	public void setyPos(int yPos) {
    		this.yPos = yPos;
    	}
        
        public void setDestination(int x, int y){
        	xDestination = x;
        	yDestination = y;
            newAStar = new aStar(xPos, yPos, xDestination, yDestination, Restaurant1Panel.grid);
            finalList = newAStar.getBestPathSteps();
        }

		public int getX() {
			return xPos;
		}

		public int getY() {
			return yPos;
		}
		
}
