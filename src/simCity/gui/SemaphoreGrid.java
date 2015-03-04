package simCity.gui;

import java.util.concurrent.Semaphore;

public class SemaphoreGrid{
        
		private int x;
		private int y;
        private Semaphore grid[][];
        
        
        SemaphoreGrid(int width, int height){
        	x = width/10;
        	y = height/10;
        	grid = new Semaphore[x][y];
        	for (int i = 0; i < x; i++){
        		for (int j = 0; j < y; j++){
        			grid[i][j] = new Semaphore(0, true);
        			grid[i][j].release();
        		}
        	}
        }
        
        public Semaphore getSemaphore(int X, int Y){
        	return grid[X/10][Y/10];
        }
        
        void releaseAll() {
        	for (int i = 0; i < x; i++){
        		for (int j = 0; j < y; j++){
        			grid[i][j].release();
        		}
        	}
        }
}
