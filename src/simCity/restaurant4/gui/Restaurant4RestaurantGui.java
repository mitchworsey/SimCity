package simCity.restaurant4.gui;

import simCity.OrdinaryPerson;

import simCity.gui.Restaurant4Panel;
import simCity.restaurant4.interfaces.Restaurant4Customer;
import simCity.restaurant4.interfaces.Restaurant4Waiter;
//import simCity.restaurant2.gui.AnimationPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class Restaurant4RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	//AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
	private boolean paused = false;
    private Restaurant4Panel restPanel = new Restaurant4Panel();
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JPanel namePanel;
    private ImageIcon icon;
    private JLabel infoLabel; //part of infoPanel
    private JLabel nameLabel;
    private JLabel iconLabel;
    private JCheckBox stateCB, waiterCB;//part of infoLabel
    private JButton pause = new JButton("Pause");

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public Restaurant4RestaurantGui() {
        int WINDOWX = 950;
        int WINDOWY = 650;

       /* animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel);*/
    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BorderLayout());
    	//setLayout(new BorderLayout());

        Dimension restDim = new Dimension((int) (WINDOWX * .5), (int) (WINDOWY * .3));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel, BorderLayout.NORTH);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension((int) (WINDOWX * .3), (int) (WINDOWY * .1));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        waiterCB = new JCheckBox();
        waiterCB.setVisible(false);
        waiterCB.addActionListener(this);
        
        pause.addActionListener(this);

        infoPanel.setLayout(new FlowLayout());
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(waiterCB);
        
        Dimension nameDim = new Dimension((int) (WINDOWX * .1), (int) (WINDOWY * .15));
        namePanel = new JPanel();
        namePanel.setPreferredSize(nameDim);
        nameLabel = new JLabel();
        nameLabel.setText("<html>Jessica Wang</html>");
        namePanel.add(nameLabel);
        icon = new ImageIcon("/Users/lemonzest73/Desktop/CS201/restaurant_wang336/src/restaurant/gui/PC250486.jpg");
        iconLabel = new JLabel("", icon, JLabel.CENTER);
        namePanel.add(iconLabel);
        
        add(namePanel, BorderLayout.SOUTH);
        add(infoPanel, BorderLayout.AFTER_LINE_ENDS);
      //  add(animationPanel, BorderLayout.CENTER);
        add(pause, BorderLayout.WEST);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        currentPerson = person;

        if (person instanceof Restaurant4Customer) {
        	waiterCB.setVisible(false);
        	stateCB.setVisible(true);
            Restaurant4Customer customer = (Restaurant4Customer) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(((Restaurant4CustomerGui) customer.getGui()).isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!((Restaurant4CustomerGui)customer.getGui()).isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + ((OrdinaryPerson)customer).getName() + " </pre></html>");
        }
        else if (person instanceof Restaurant4Waiter) {
        	stateCB.setVisible(false);
        	waiterCB.setVisible(true);
        	Restaurant4Waiter waiter = (Restaurant4Waiter) person;
        	waiterCB.setText("Break?");
        	waiterCB.setSelected(((Restaurant4WaiterGui) waiter.getGui()).onBreak());
        	//waiterCB.setEnabled(!waiter.getGui().onBreak());
        	infoLabel.setText(
                    "<html><pre>     Name: " + ((OrdinaryPerson)waiter).getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof Restaurant4Customer) {
                Restaurant4Customer c = (Restaurant4Customer) currentPerson;
                ((Restaurant4CustomerGui) c.getGui()).setHungry();
                stateCB.setEnabled(false);
            }
        }
        else if (e.getSource() == waiterCB) {
        	if (currentPerson instanceof Restaurant4Waiter) {
        		Restaurant4Waiter w = (Restaurant4Waiter) currentPerson;
        		if(!waiterCB.isSelected()) {
        			waiterCB.setSelected(false);
        			((Restaurant4WaiterGui) w.getGui()).resetOnBreak();
        			((Restaurant4WaiterGui) w.getGui()).returnToWork(w);
        		}
        		else {
        			waiterCB.setEnabled(false);
                    ((Restaurant4WaiterGui) w.getGui()).setOnBreak();
        		}
            }
        }
        /*else if (e.getSource() == pause) {
        	if(!paused) {
        		pause.setText("Resume");
        		restPanel.pauseAllAgents();
        		paused = true;
        	}
        	else {
        		pause.setText("Pause");
        		restPanel.resumeAllAgents();
        		paused = false;
        	}
        }*/
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Restaurant4Customer c) {
        if (currentPerson instanceof Restaurant4Customer) {
            Restaurant4Customer cust = (Restaurant4Customer) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    /**
     * Message sent from a waiter gui to enable that waiter's
     * "Break?" checkbox
     * 
     * @param w reference to the waiter
     */
    public void setWaiterEnabled(Restaurant4Waiter w) {
        if (currentPerson instanceof Restaurant4Waiter) {
            Restaurant4Waiter waiter = (Restaurant4Waiter) currentPerson;
            if (w.equals(waiter)) {
                waiterCB.setEnabled(true);
                waiterCB.setSelected(((Restaurant4WaiterGui) waiter.getGui()).onBreak());
            }
        }
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        Restaurant4RestaurantGui gui = new Restaurant4RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
