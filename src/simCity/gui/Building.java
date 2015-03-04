package simCity.gui;

public class Building{
	int x;
	int y;
	int width;
	int height;
	
	Building(int X, int Y, int Width, int Height){
		x = X; 
		y = Y;
		width = Width;
		height = Height;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}