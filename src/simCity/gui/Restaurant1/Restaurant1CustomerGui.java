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
import simCity.Restaurant1.Restaurant1CustomerRole;
import simCity.gui.Gui;
import simCity.gui.Restaurant1Panel;
import simCity.gui.SimCityGui;
import simCity.gui.aStar;
import simCity.interfaces.Person;
import simCity.interfaces.Restaurant1Customer;

public class Restaurant1CustomerGui implements Gui{

                /* Other */
        public Restaurant1Panel gui;
        public Restaurant1Customer agent = null;
        private Timer timer = new Timer();
        
        /* Final variables */
        static final int STARTX = 200;
        static final int STARTY = 100;
        static final int ROW1 = 150;
        static final int ROW2 = 200;
        static final int ROWY = 200;
        static final int WIDTH = 27;
        static final int HEIGHT = 38;
        static final int MEETX = 110;
        static final int MEETY = 200;
        static final int TABLE = 170;
        public static final int XTABLE = 160;
            public static final int YTABLE = 470;
            
            /* Pathfinding */
        private aStar newAStar;
        private List<simCity.gui.Node> finalList = Collections.synchronizedList(new ArrayList<simCity.gui.Node>());

        /* Initialize variables */
        private int xPos = STARTX;
        private int yPos = STARTY;
        private int xDestination = STARTX;
        private int yDestination = STARTY;
        private boolean isPresent = true;
            private boolean isHungry = false;
        private boolean pizza = false;
        private boolean steak = false;
        private boolean chicken = false;
        private boolean salad = false;
        private boolean question = false;
        private int table = 0;
            
        /* Commands */
            private enum Command {noCommand, GoToSeat, LeaveRestaurant};
            private Command command = Command.noCommand;
        
        /* Images */
        private ImageIcon imageIcon = new ImageIcon("src/simCity/gui/images/customer.JPG");
            private Image image = imageIcon.getImage();
            private ImageIcon pizzaImageIcon = new ImageIcon("src/simCity/gui/images/pizzaPlate.JPG");
        private Image pizzaImage = pizzaImageIcon.getImage();
        private ImageIcon steakImageIcon = new ImageIcon("src/simCity/gui/images/steakPlate.JPG");
        private Image steakImage = steakImageIcon.getImage();
        private ImageIcon chickenImageIcon = new ImageIcon("src/simCity/gui/images/chickenPlate.JPG");
        private Image chickenImage = chickenImageIcon.getImage();
        private ImageIcon saladImageIcon = new ImageIcon("src/simCity/gui/images/saladPlate.JPG");
        private Image saladImage = saladImageIcon.getImage();
        private ImageIcon questionImageIcon = new ImageIcon("src/simCity/gui/images/csciQuestion.JPG");
        private Image questionImage = questionImageIcon.getImage();
        
        public Restaurant1CustomerGui(Restaurant1Customer agent, Restaurant1Panel restaurant1Panel, int ct) { 
                        if (ct<=6)
                                xDestination = ROW1;
                        else 
                                xDestination = ROW2;
                        yDestination = ROWY + (6-ct%6)*WIDTH;
                        setDestination(xDestination, yDestination);
                newAStar = null;
                this.gui = restaurant1Panel;
                this.agent = agent;
        }

        public void updatePosition() {
                
                if (finalList != null){
                if (!finalList.isEmpty()){
                        if (finalList.size()>=1){
                                //simCity.gui.CityPanel.grid.release(xPos/10, yPos/10);
                                xPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getX();
                                yPos = ((simCity.gui.Node) finalList.get(finalList.size()-1)).getY();
                                //simCity.gui.CityPanel.grid.set(xPos/10, yPos/10);
                                finalList.remove(finalList.get(finalList.size()-1));
                        }
                }
                }
                if (xPos/10 == xDestination/10 && yPos/10 == yDestination/10) {
                            if (command==Command.GoToSeat) {
                                    agent.msgAnimationFinishedGoToSeat();
                            }
                            else if (command==Command.LeaveRestaurant) {
                                    agent.msgAnimationFinishedLeaveRestaurant();
                                    isHungry = false;
                            }
                            command=Command.noCommand;
                    }
        }
        
            public void draw(Graphics2D g) {
                if (image != null)
                        g.drawImage(image, xPos, yPos, WIDTH, HEIGHT, null);
                    if (pizza) {
                            if (pizzaImage != null)
                                    g.drawImage(pizzaImage, xPos, yPos+HEIGHT, WIDTH, WIDTH, null);
                    }
                    if (steak) {
                            if (steakImage != null)
                                    g.drawImage(steakImage, xPos, yPos+HEIGHT, WIDTH, WIDTH, null);
                    }
                    if (chicken) {
                            if (chickenImage != null)
                                    g.drawImage(chickenImage, xPos, yPos+HEIGHT, WIDTH, WIDTH, null);
                    }
                    if (salad) {
                            if (saladImage != null)
                                    g.drawImage(saladImage, xPos, yPos+HEIGHT, WIDTH, WIDTH, null);
                    }
                    if (question) {
                            if (questionImage != null)
                                    g.drawImage(questionImage, xPos+WIDTH, yPos+HEIGHT, WIDTH, WIDTH, null);
                    }
            }

        public boolean isPresent() {
                return isPresent;
        }
        
        public void setPresent(boolean p) {
                isPresent = p;
        }
        
        public int getX() {
                return xPos;
        }
        
        public int getY() {
                return yPos;
        }

            public void setHungry() {
                    isHungry = true;
                    setPresent(true);
            }

            public boolean isHungry() {
                    return isHungry;
            }

            public void DoShowFood(String food) {
                    if(food.equals("pizza"))
                            pizza = true;
                    if(food.equals("steak"))
                            steak = true;
                    if(food.equals("salad"))
                            salad = true;
                    if(food.equals("chicken"))
                            chicken = true;
            }
            
            public void DoRemoveFood() {
                    pizza = false;
                    chicken = false;
                    salad = false;
                    steak = false;
            }
            
            public void DoShowQuestion() {
                    question = true;
            }
            
            public void DoRemoveQuestion() {
                    question = false;
            }
            
            public void DoGoToSeat(int tbl) {
                    setDestination(MEETX, MEETY);
                    table = tbl;
                timer.schedule(new TimerTask() {
                            public void run() {
                                    setDestination(XTABLE + (table-1)*TABLE + HEIGHT, YTABLE - HEIGHT);
                                    command = Command.GoToSeat;
                                    return;
                            }
                    },
                    1000);
            }

            public void DoExitRestaurant() {
                    timer.schedule(new TimerTask() {
                            public void run() {
                                    setDestination(STARTX, STARTY);
                    command = Command.LeaveRestaurant;
                            }
                    },
                    1000);
            }
        
        public void setDestination(int x, int y){
                xDestination = x;
                yDestination = y;
            newAStar = new aStar(xPos, yPos, xDestination, yDestination, gui.grid);
            finalList = newAStar.getBestPathSteps();
        }
                
}