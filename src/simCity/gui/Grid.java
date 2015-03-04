package simCity.gui;

public class Grid{
        
		private int x;
		private int y;
        private boolean grid[][];
        
        Grid(int width, int height){
        	x = width/10;
        	y = height/10;
        	grid = new boolean[x][y];
        	for (int i = 0; i < x; i++){
        		for (int j = 0; j < y; j++){
        			grid[i][j] = true;
        		}
        	}
        }
        
        boolean acquire(int xLoc, int yLoc){
        	return !grid[xLoc][yLoc];
        }
        
        void release(int xLoc, int yLoc){
        	grid[xLoc][yLoc] = false;
        }
        
        void set(int xLoc, int yLoc){
        	grid[xLoc][yLoc] = true;
        }
        
        void setClosedBlock(int xLoc, int yLoc, int width, int height){
        	for (int i = xLoc; i < (xLoc + width); i++){
        		for (int j = yLoc; j < (yLoc + height); j++){
        			grid[i][j] = true;
        		}
        	}
        }
        
        void setOpenBlock(int xLoc, int yLoc, int width, int height){
        	for (int i = xLoc; i < (xLoc + width); i++){
        		for (int j = yLoc; j < (yLoc + height); j++){
        			grid[i][j] = false;
        		}
        	}
        }
}