/**
 * 
 * Percolation
 * 
 * @author Emrah Yilgen
 * @author Ana Paula Centeno
 * @author Haolin (Daniel) Jin
 */

public class Percolation {

	private boolean[][] grid;          // gridSize by gridSize grid of sites; 
	                                   // true = open site, false = closed or blocked site
	private WeightedQuickUnionFind wquFind; // 
	private int 		gridSize;      // gridSize by gridSize is the size of the grid/system 
	private int         gridSquared;
	private int         virtualTop;    // virtual top    index on WeightedQuckUnionFind arrays
	private int         virtualBottom; // virtual bottom index on WeightedQuckUnionFind arrays

	/**
	* Constructor.
	* Initializes all instance variables
	*/
	public Percolation ( int n ){
		gridSize 	  = n;
		gridSquared   = gridSize * gridSize;
		wquFind       = new WeightedQuickUnionFind(gridSquared + 2);
		grid          = new boolean[gridSize][gridSize];   // every site is initialized to closed/blocked
		virtualTop    = gridSquared;
		virtualBottom = gridSquared + 1;
	} 
	
	/**
	 * Getter method for GridSize 
	 * @return integer representing the size of the grid.
	*/
	public int getGridSize () {
		return gridSize;
	}

	/**
	 * Returns the grid array
	 * @return grid array
	 */
	public boolean[][] getGridArray () {
		return grid;
	}

	/**
	* Open the site at postion (x,y) on the grid to true and add an edge
	* to any open neighbor (left, right, top, bottom) and/or top/bottom virtual sites
	* Note: diagonal sites are not neighbors
	*
	* @param row grid row
	* @param col grid column
	* @return void
	*/
	public void openSite (int row, int col) {
		int size = getGridSize();
		int tempRow = row;
		int tempCol = col;
		if(tempRow < 0 || tempCol < 0  || tempRow >= size || tempCol >= size) {
			throw new IndexOutOfBoundsException();
		}
		if(grid[tempRow][tempCol] == false) {
			grid[tempRow][tempCol] = true;
		}
		
		
		// union open sites at first row to virtualTop
		if(tempRow == 0) {
			wquFind.union(wquFind.find(tempRow * size + tempCol), wquFind.find(virtualTop));
			
		// union open sites at last row to virtualBottom
		} if( tempRow == size-1) {
			wquFind.union(wquFind.find(tempRow * size + tempCol), wquFind.find(virtualBottom));
			
		// union current site with open site above (row-1)
		} if(tempRow != 0 && grid[tempRow-1][tempCol] == true) {
			wquFind.union(wquFind.find(tempRow * size + tempCol), wquFind.find(tempRow-1) * size + tempCol);
			
		// union current site with open site left (col-1)
		} if(tempCol != 0 && grid[tempRow][tempCol-1] == true) {
			wquFind.union(wquFind.find(tempRow * size + tempCol), wquFind.find(tempRow * size + (tempCol-1)));
			
		// union current site with open site right (col+1)
		} if(tempCol < size-1 && grid[tempRow][tempCol+1] == true) {
			wquFind.union(wquFind.find(tempRow * size + tempCol), wquFind.find(tempRow * size + (tempCol+1)));
			
		// union current site with open site below (row+1)
		} if(tempRow < size-1 && grid[tempRow+1][tempCol] == true) {
			wquFind.union(wquFind.find(tempRow * size + tempCol), wquFind.find(tempRow+1) * size + tempCol);
			
		}
	}

	/**
	* Check if the system percolates (any top and bottom sites are connected by open sites)
	* @return true if system percolates, false otherwise
	*/
	public boolean percolationCheck () {
		return wquFind.find(virtualTop) == wquFind.find(virtualBottom);
	}

	/**
	 * Iterates over the grid array openning every site. 
	 * Starts at [0][0] and moves row wise 
	 * @param probability
	 * @param seed
	 */
	public void openAllSites (double probability, long seed) {
		// Setting the same seed before generating random numbers ensure that
		// the same numbers are generated between different runs
		StdRandom.setSeed(seed); // DO NOT remove this line

		for(int i = 0; i<grid.length; i++) {
			for(int j = 0; j<grid.length; j++) {
				double randomNumber = StdRandom.uniform();
				if(grid[i][j] == false && randomNumber < probability) {
					openSite(i,j);
				}

			}
		}
	}
	

	/**
	* Open up a new window and display the current grid using StdDraw library.
	* The output will be colored based on the grid array. Blue for open site, black for closed site.
	* @return: void 
	*/
	public void displayGrid () {
		double blockSize = 0.9 / gridSize;
		double zeroPt =  0.05+(blockSize/2), x = zeroPt, y = zeroPt;

		for ( int i = gridSize-1; i >= 0; i-- ) {
			x = zeroPt;
			for ( int j = 0; j < gridSize; j++) {
				if ( grid[i][j] ) {
					StdDraw.setPenColor( StdDraw.BOOK_LIGHT_BLUE );
					StdDraw.filledSquare( x, y ,blockSize/2);
					StdDraw.setPenColor( StdDraw.BLACK);
					StdDraw.square( x, y ,blockSize/2);		
				} else {
					StdDraw.filledSquare( x, y ,blockSize/2);
				}
				x += blockSize; 
			}
			y += blockSize;
		}
	}


	
	/**
	* Main method, for testing only, feel free to change it.
	*/
	public static void main ( String[] args ) {

		double p = 0.47;
		Percolation pl = new Percolation(7);

		/* 
		 * Setting a seed before generating random numbers ensure that
		 * the same numbers are generated between runs.
		 *
		 * If you would like to reproduce Autolab's output, update
		 * the seed variable to the value Autolab has used.
		 */
		long seed = System.currentTimeMillis();
		pl.openAllSites(p, seed);

		System.out.println("The system percolates: " + pl.percolationCheck());
		pl.displayGrid();
	}
}
