package simCity.gui;

import java.awt.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public interface Gui {
	
	public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
	public int getX();
	public int getY();
	public void setDestination(int x, int y);
}