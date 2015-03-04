/*package simCity.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import simCity.house.HouseResidentRole;
import simCity.interfaces.HouseResident;

public class HouseGui extends JFrame implements MouseListener, ActionListener {
	HousePanel housePanel = new HousePanel();    
    //Set static ints 
    static final int WINDOWX = 600;
    static final int WINDOWY = 400;
    static final int CITYPANELDIMX = 600;
    static final int CITYPANELDIMY = 400;
    static final int CONTROLPANELDIMX = 50;
    static final int CONTROLPANELDIMY = 200;
    static final int INFOPANELDIMX = 100;
    static final int INFOPANELDIMY = 100;


 
    private Object currentPerson; // Holds the agent that the info is about.
        
    //Info panel and components 
    private JPanel infoPanel; // The identification card for the selected person. 
    private ImageIcon bondGirl1Icon = new ImageIcon("src/simCity/gui/images/bondGirl3.png");
    private Image bondGirl1 = bondGirl1Icon.getImage() ;  
    private Image bondGirl11 = bondGirl1.getScaledInstance( INFOPANELDIMX, INFOPANELDIMY,  java.awt.Image.SCALE_SMOOTH ); 
    private JLabel picture;
    private JButton exitButton;
    private ImageIcon icon007 = new ImageIcon("src/simCity/gui/images/007.png");
    private Image image007 = icon007.getImage() ;  
    private Image image0072 = image007.getScaledInstance( 100, 30,  java.awt.Image.SCALE_SMOOTH ); 
    private JLabel label007;
    private JButton question;
    private JButton create;
    private ImageIcon questionIcon = new ImageIcon("src/simCity/gui/images/question.png");
    private Image questionImage = questionIcon.getImage() ;  
    private Image questionImage2 = questionImage.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ); 
    private ImageIcon createIcon = new ImageIcon("src/simCity/gui/images/create.png");
    private Image createImage = createIcon.getImage() ;  
    private Image createImage2 = createImage.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ); 
    
    private JOptionPane createPane;
    private JOptionPane questionPane;
    
    
    /**
     * Constructor for SimCityGui class.
     * Sets up all the gui components.
     */
   /* public HouseGui() {
    	//Main window size
    	setSize(WINDOWX, WINDOWY);
    	setLocationRelativeTo(null);
    	
    	addMouseListener(this);
    	

    	//Info Panel components
    	infoPanel = new JPanel();
    	infoPanel.setBackground(Color.WHITE);
    	infoPanel.setLayout(null);
    	Border raisedbevel = BorderFactory.createRaisedBevelBorder();
    	Border loweredbevel = BorderFactory.createLoweredBevelBorder();
    	infoPanel.setBorder(BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel));
    	
    	//Lower Panel 
    	bondGirl1Icon = new ImageIcon(bondGirl11);
    	picture = new JLabel(bondGirl1Icon);
    	picture.setVisible(true);
    	picture.setBounds(5, 5, INFOPANELDIMX-10, INFOPANELDIMY-10);
    	infoPanel.add(picture);
    	icon007 = new ImageIcon(image0072);
    	label007 = new JLabel(icon007);
    	questionIcon = new ImageIcon(questionImage2);
    	createIcon = new ImageIcon(createImage2);
    	question = new JButton(questionIcon);
    	question.addActionListener(this);
    	create = new JButton(createIcon);
    	
    	
    	//Info Panel layout
    	infoPanel.setLayout(null);
    	Insets insets = infoPanel.getInsets();
    	exitButton = new JButton("x");
    	exitButton.addActionListener(this);
    	
    	createPane = new JOptionPane();
    	questionPane = new JOptionPane();
 	
    	//Main window layout
    	setLayout(null);
    	question.setBounds(5, WINDOWY-115, 50, 50);
    	add(question);
    	create.setBounds(5, WINDOWY-170, 50, 50);
    	add(create);
    	label007.setBounds(5, WINDOWY-60, 100, 30);
    	add(label007);
    	infoPanel.setVisible(false);
    	add(infoPanel);
    	infoPanel.setBounds(5, 5, INFOPANELDIMX, INFOPANELDIMY);
    	exitButton.setVisible(false);
    	add(exitButton);
    	exitButton.setBounds(INFOPANELDIMX+10, 5, 20, 20);
    	add(housePanel);
    	housePanel.setBounds(0, 0, CITYPANELDIMX, CITYPANELDIMY);
    	    	    	
    	
    	
    	
    }
    
	/**
     * updateInfoPanel() takes the given person object and
     * changes the information panel to hold that person's info.
     *
     * @param person Person object
     */
    /*public void updateInfoPanel(Object person) {
        currentPerson = person;
    }
    
	public void mouseClicked(MouseEvent m) {
		if (m.getX() > 300 && m.getX() <=350 && m.getY() >=300 && m.getY() <= 350){
			infoPanel.setVisible(true);
			exitButton.setVisible(true);
		}
	}

    /**
     * Main routine to get gui started
     */
    /*public static void main(String[] args) {
        HouseGui gui = new HouseGui();
        gui.setTitle("HouseGui team007");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitButton) {
			infoPanel.setVisible(false);
			exitButton.setVisible(false);
		}
		if (e.getSource() == question){
        	questionPane.showMessageDialog(this,
        	    "This SimCity game was created by team07:\n   Christine Song \n   Marina Hierl\n   Jessica Wang"
        	    + "\nTo play the game, create a new character with the"
        	    + "\nplus button. Interact with characters by clicking on them.",
           	    "SimCity Information",
           	    JOptionPane.PLAIN_MESSAGE);
       	}
		
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

}*/
