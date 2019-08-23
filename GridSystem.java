package application;

import java.util.Vector;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GridSystem 
{
	
	//member variables:
	int ROW = 10, COL = 20;
	int size = 0;
	ImageView[][] gridBlocks = new ImageView[ROW][COL];
	ImageView[][] dummy = new ImageView[ROW][COL];//<- this is for color
	ImageView[][] dummyAni = new ImageView[ROW][COL];//<-this is for animation
	Vector<ImageView> bounds;
	Blocks b;
	int points;//???
	ImageView range = new ImageView("resources/images/range_block.png");
	Image empty = new Image("resources/images/empty_block.png");
	Image bound = new Image("resources/images/bound_block.png");

	//***************************************************
	// constructor done with resizing
	//***************************************************
	/* NOTES:
	 * 
	 */
	GridSystem(Blocks b, Pane root, Vector<ImageView> bounds)
	{
		size = ROW * COL;
		this.b = b;
		this.bounds = bounds;//?
		
		//add things to the root (!) this has to be below initialize an inside the constructor not in initialize()
		for (int r = 0; r < ROW; r++)
		{
			for (int c = 0; c < COL; c++)
			{
				gridBlocks[r][c] = new ImageView(empty);
				dummy[r][c] = new ImageView(empty);
				dummyAni[r][c] = new ImageView(empty);
				root.getChildren().add(gridBlocks[r][c]);
				root.getChildren().add(dummy[r][c]);
				root.getChildren().add(dummyAni[r][c]);
			}
		}
		initialize();
		//root.getChildren().add(range);
	}
	
	//***************************************************
	// initialize done with resizing..
	//***************************************************
	/* NOTES:
	 * 
	 */
	void initialize()
	{	
		for (int r = 0; r < ROW; r++)
		{
			for (int c = 0; c < COL; c++)
			{
				gridBlocks[r][c].setImage(empty);
				gridBlocks[r][c].setX(97 + r * 17 + r);
				gridBlocks[r][c].setY(70 + c * 17 + c);
				gridBlocks[r][c].setId(String.valueOf(r)+","+String.valueOf(c));
				dummy[r][c].setImage(empty);
				dummy[r][c].setX(97 + r * 17 + r);
				dummy[r][c].setY(70 + c * 17 + c);
				dummyAni[r][c].setImage(empty);
				dummyAni[r][c].setX(97 + r * 17 + r);
				dummyAni[r][c].setY(70 + c * 17 + c);
				dummyAni[r][c].setVisible(false);
			}
		}	
	}
	
	//***************************************************
	// setBlock 
	//***************************************************
	/* NOTES:
	 */
	void setBlock()
	{
		for (int i = 0; i < 4; i++)
		{
			//for endgame
			if(b.blocks[i].getBoundsInLocal().intersects(Main.top.getBoundsInLocal()))
			{
				//run gameOver
				gameOver();
				//getName for scoreBoard!!
				ScoreSystem.getName();
				return;
			}
				
		}
			
		for (int r = 0; r < ROW; r++)
		{
			for (int c = 0; c < COL; c++)
			{
				for (int i = 0; i < 4; i++)
				{		
				    if(gridBlocks[r][c].getBoundsInLocal().contains(b.blocks[i].getBoundsInLocal()))
					{
						//put current stopped block's position in bounds
						bounds.add(gridBlocks[r][c]);
						// change color of dummy[] in that area...
						dummy[r][c].setImage(b.getColor());
						dummy[r][c].setId(b.getColorName());
					}
				}	
			}
		}
		//copy all dummy's color info to dummyAni's
		for(int i = 0; i < 20; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				dummyAni[j][i].setImage(dummy[j][i].getImage());
				dummyAni[j][i].setVisible(false);
			}
		}
		getRows();
	}
	//***************************************************
	// getColumns
	//***************************************************
	/* NOTES:
	 * get clone's max length add all boundaries in X then find all the bounds in that columns only, then
	 * find the highest one and return it's Y-value?
	 */
	void getCols()
	{
		double MIN_X = b.cloneS[0].getX()
			  ,MAX_X = b.cloneS[0].getX();
		double distances[] = new double[4] , distance = 0,
			   highest = b.blocks[0].getY();
		
		Vector<ImageView> fromRange = new Vector<ImageView>();
		
		//getting MIN_X, MAX_X for width of range.. and highest Y-value for blocks
		for(int i = 1; i < 4; i++)
		{
			//getting minX
			if(MIN_X >= b.cloneS[i].getX())
			{
				MIN_X = b.cloneS[i].getX();
			}		
			//getting maxX get this straight getFitWidth is the width of the block...
			if(MAX_X <= b.cloneS[i].getX())
			{
				MAX_X = b.cloneS[i].getX();
			}	
			//getting highest
			if(highest >= b.blocks[i].getY())
			{
				highest = b.blocks[i].getY();
			}
		}
		
		//setting size of range according to the overall span in X-direction of blocks...
		range.setX(MIN_X);
		range.setY(highest);//<-this is well done!
		range.setFitHeight(400);
		range.setFitWidth(MAX_X - (MIN_X - 17));//150 <- this doesn't have to change unless it rotates..
		
		//get any of the bounds that intersects range
		for(int i = 2; i < bounds.size(); i++)// skip env. boundaries except the bottom one...
		{
			//if bounds 'range' contains bounds elements
			if(range.intersects(bounds.elementAt(i).getBoundsInLocal()))
			{
				fromRange.add(bounds.elementAt(i));
			}
		}
		
		//compute distance between each cloneS' and bounds' Y-value distance here should set to the shortest one...
		distance = bounds.elementAt(2).getY() - b.blocks[0].getY();//this should be bottom env bound
		for(int i = 1; i < 4; i++)
		{
			if(distance > bounds.elementAt(2).getY() - b.blocks[i].getY())
			{
				distance = bounds.elementAt(2).getY() - b.blocks[i].getY();
			}
		}

		//for each block check for which bounds below them has the shortest distance from them, then compare 
		//all four distances to see the shortest distance and apply that distance to all of the cloneS'
		for(int i = 0; i < 4; i++)
		{
			distances[i] = 500;//this is so that if will have something to compare with???

			//if in the same column as blocks[i] do something if not get to next..
			for(int j = 0; j < fromRange.size(); j++)
			{
				if(fromRange.elementAt(j).getX() == b.blocks[i].getX())
				{				
					if(distances[i] > fromRange.elementAt(j).getY() -b.blocks[i].getY())
					{
						distances[i] = fromRange.elementAt(j).getY() -b.blocks[i].getY();
					}
				}
			}
		}
		
		//figures out the shortest distance among four columns
		//distance = distances[0];

		for(int i = 0; i < 4; i++)
		{
			//get the shortest distance that is not negative, which gets us in trouble by going above the blocks..
			if((distance > distances[i] && distances[i] > 0) || distance <= 0)
			{
				distance = distances[i];
			}
		}
			
		//move clones to top of the highest bound
		for(int i = 0; i < 4; i++)
		{
			b.cloneS[i].setY(b.blocks[i].getY() + distance - 18);
		}

		//if clone is above or equal to blocks' Y position <-GOOD!!!
		for(int i = 0; i < 4; i++)
		{
			if(b.cloneS[i].getY() <= b.blocks[i].getY())
				b.cloneS[i].setVisible(false);
			else
				b.cloneS[i].setVisible(true);
		}
	}
	
	//***************************************************
	// getRows(!)
	//***************************************************
	/* NOTES:
	 * (!) either on this function or make new function that when popping fullRow make the things in the bound go down...
	 */
	void getRows()
	{
		int cols[] = new int[20];
		boolean removeCols[] = new boolean[20];
		
		//if bounds has at least one row amount of boxes filled..
		if(bounds.size() >= 14)
		{
			cols = updateCols();
			
			//check for one row if it is remove that row..
			for( int i = 0; i < 20; i++)
			{
				//initialize removeCols to false
				removeCols[i] = false;
				
				if(cols[i] == 10)
				{
					//call 
					removeCols[i]= true;
					//call PointSystem's popRow function to increment it by one?
					PointSystem.popRow();
				}
			}
			//**************************WORKING*****************************//
			
			animation(removeCols);
			// removing blocks used in animation and reset cols to zero
			for(int i = 0; i < 20; i++)
			{
				if(cols[i] == 10)
				{
					for(int j = 0; j < 10; j++)
					{
						//i need to remove bounds at the block location
						for(int c = 3; c < bounds.size(); c++)
						{
							if(Integer.parseInt(bounds.elementAt(c).getId().split(",")[1])== i)
							{
								bounds.removeElementAt(c);//<----	
							}	
						}
						// I'm not sure about this..
						dummy[j][i].setImage(empty);
						dummy[j][i].setId(null);
					}
				}cols[i] = 0;//<--resetting..
				
			}
			
			//update cols
			cols = updateCols();

			//this have to be called after removing block from the animation with updated cols...
			moveDown(cols);
		}
	}
	//***************************************************
	// updateCols
	//***************************************************
	/* NOTES:
	 * update Col information
	 */
	int[] updateCols()
	{
		int cols[] = new int[20];
		int col;
		
		for (int i = 3; i < bounds.size(); i++)//starting from 4 to skip all the environment boundaries
		{
			col =Integer.parseInt(bounds.elementAt(i).getId().split(",")[1]);
			
			for(int j = 0; j < 20; j++)
			{
				//add one to the column for every bounds it contains..
				if(j == col)
				{
					cols[j] += 1;
				}
			}
		}
		
		return cols;
	}
	

	//***************************************************
	// animation
	//***************************************************
	/* NOTES:
	 * f(x) = -(x + 1)^2 + 1/left || -(x - 1)^2 + 1/right or -4(x - 0.5)^2 + 1
	 */
	void animation(boolean[] fullCols)
	{	
		new AnimationTimer() {// this should be out of the loop so that it doesn't run for loop's span
			
			double inteval = 0;
			double MAX = 50; //<-this is for range
			long updateNow = 0;
			int count = 0;
			
			@Override
			public void handle(long now) 
			{
				if(now - updateNow >= 60_000_000)
				{
					updateNow = now;
					//get color information from it's dummy counterpart
					for(int i = 0; i < 20; i++)
					{
						for(int j = 0; j < 10; j++)
						{
							//if full column then set dummyAni's image to dummy's
							if(fullCols[i])
							{
								dummyAni[j][i].setVisible(true);
							}
						}
					}
					
					//for each items in full row
					for(int fc = 0; fc < fullCols.length;)
					{
						if(fullCols[fc])
						{
							/*THERE IS REASON WHY I HAD TO DO IT LIKE THIS... IF RUN IN LOOP, THAT COUNT AS ANIMATION!!!*/
							//left:
							dummyAni[0][fc].setX(dummyAni[0][fc].getX() - inteval);
							dummyAni[0][fc].setY(dummyAni[0][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							dummyAni[1][fc].setX(dummyAni[1][fc].getX() - inteval);
							dummyAni[1][fc].setY(dummyAni[1][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							dummyAni[2][fc].setX(dummyAni[2][fc].getX() - inteval);
							dummyAni[2][fc].setY(dummyAni[2][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							dummyAni[3][fc].setX(dummyAni[3][fc].getX() - inteval);
							dummyAni[3][fc].setY(dummyAni[3][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							
							//middle:
							dummyAni[4][fc].setX(dummyAni[4][fc].getX());
							dummyAni[4][fc].setY(dummyAni[4][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							
							//right:
							dummyAni[5][fc].setX(dummyAni[5][fc].getX() + inteval);
							dummyAni[5][fc].setY(dummyAni[5][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							dummyAni[6][fc].setX(dummyAni[6][fc].getX() + inteval);
							dummyAni[6][fc].setY(dummyAni[6][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							dummyAni[7][fc].setX(dummyAni[7][fc].getX() + inteval);
							dummyAni[7][fc].setY(dummyAni[7][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							dummyAni[8][fc].setX(dummyAni[8][fc].getX() + inteval);
							dummyAni[8][fc].setY(dummyAni[8][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							dummyAni[9][fc].setX(dummyAni[9][fc].getX() + inteval);
							dummyAni[9][fc].setY(dummyAni[9][fc].getY() + -/*flip*/(-((Math.pow((count-1/*depth*/), 2))) +20/*inteval*/));
							
							count++;
							if(count >= MAX)//<-for safety measures.. I don't think its working though..
							{
								//reset back to original
								for (int cc = 0; cc < fullCols.length; cc++)
								{
									if(fullCols[cc])
									{
										for(int i = 0; i < 10; i++)
										{
											dummyAni[i][cc].setX(gridBlocks[i][cc].getX());
											dummyAni[i][cc].setY(gridBlocks[i][cc].getY());
											dummyAni[i][cc].setImage(empty);
											dummyAni[i][cc].setId(null);
										}
									}
								}
								this.stop();
							}
						}fc++;		
					}
				}
			}
		}.start();
		
	}
	//***************************************************
	// moveDown
	//***************************************************
	/* NOTES:
	 * moves down a row for each row that's being removed 
	 */
	void moveDown(int[] eachRow)
	{
		//get rows that will be removed... 
		//out of 20 rows check only the rows that are above fullRows?
		//just move down any rows on top of that fullRow
		String[] rowStatus = new String[20];
		int filledRowCount = 0;
		
		Vector<Integer> filledRows = new Vector<Integer>();
		
		for(int i = 0; i < 20; i++)
		{
			//fullRows: contains all 10 blocks soon to be emptied
			if(eachRow[i] == 10)
			{
				rowStatus[i] = "fullRow";
			}	
			//filledRows: contains some but not full(10) blocks
			else if(eachRow[i] > 0)
			{
				rowStatus[i] = "filledRow";
				filledRowCount++;
			}
			//emptyRows: contains zero block(s)
			else
			{
				rowStatus[i] = "emptyRow";
			}	
		}
		
		//if the row is full row remove bounds and make it 'emptyRow status'
		for(int i = 19; i > -1; i--)//<-- do this in descending order THAT -1 MAN!!!
		{
			//if filledRow from bottom up store it's y-position inside Vector ***
			if(rowStatus[i] == "filledRow")
			{
				filledRows.add(i);
			}
		}
		
		//if there is at least one filledRow otherwise don't have to move down.. and at least one fullRow
		//otherwise it will cause an error b/c there won't be any row that will pop
		if(filledRowCount > 0)
		{
			//for every filledrow move it down til you can't no more...
			for(int i = 0; i < filledRowCount; i++)
			{
				int tmpPos = -1;
				int curPos = filledRows.elementAt(i);
				//start from filledRows[0] <- which will be the most bottom one and check until it reaches zero/bottom of the screen
				for(int j = curPos; j < 20; j++)//<- bottom is 19 not 0
				{
					//if emptyRow
					if(rowStatus[j] == "emptyRow")
					{
						tmpPos = j;
					}
					//this is where I stop?? also stop when j reaches 0
					else
					{
						continue;
					}
				}
				
				//move down to tmpPos
				if(tmpPos > -1)
				{
					//for all blocks in the row including ones that are empty(!)<- be careful w/ this..
					for(int j = 0; j < 10; j++)
					{
						//for every dummy blocks switch colors******need to fix this...
						Image tmpImg = b.setColor(dummy[j][curPos].getId());			
						dummy[j][tmpPos].setImage(tmpImg);
						dummy[j][tmpPos].setId(dummy[j][curPos].getId());//<--give it's id off to new one...!!!
						//reset original to empty
						dummy[j][curPos].setImage(empty);
						dummy[j][curPos].setId(null);

						//for every gridBlock location add bounds to new location, remove bounds from old
						
						//adding..
						for(int a = 3; a < bounds.size(); a++)
						{
							//if any bounds in that curPos
							if(Integer.parseInt(bounds.elementAt(a).getId().split(",")[1]) == curPos &&
							   Integer.parseInt(bounds.elementAt(a).getId().split(",")[0]) == j)
							{
								//add that bounds to emptyRow/tmpPos with that same X-value
								bounds.add(gridBlocks[j][tmpPos]);
							}
						}
						// make emptyRow to filledRow
						rowStatus[tmpPos] = "filledRow";
						
						//removing..
						for(int d = 3; d < bounds.size(); d++)
						{ 	// I think this is causing trouble, b/c when something is removed from a vector doesn't it movedown ?
							if(Integer.parseInt(bounds.elementAt(d).getId().split(",")[1])== curPos &&
							   Integer.parseInt(bounds.elementAt(d).getId().split(",")[0])== j)
								bounds.removeElementAt(d);//<----
						}

						//also make that row emptyRow
						rowStatus[curPos] = "emptyRow";
					}
				}
			}//for every filledRow?
		}//end of first if
		//empty filledRows?
		filledRows.clear();
	}
	
	//***************************************************
	// gameOver
	//***************************************************
	/* NOTES:
	 * check if any of the bounds intersect top game is over
	 */
	void gameOver()
	{
		//just go back to main and run a while loop?
		Main.currentState = "gameOver";
	}
	
	//*******************************************
	//reset
	//*******************************************
	/* NOTES:
	 * lets write down everything that needs to be reseted
	 */
	void reset()
	{
		for (int r = 0; r < ROW; r++)
		{
			for (int c = 0; c < COL; c++)
			{
				//(*) I also need to clear out it's id so it won't affect the color!!
				gridBlocks[r][c].setImage(empty);
				gridBlocks[r][c].setId(String.valueOf(r)+","+String.valueOf(c));
				dummy[r][c].setImage(empty);
				dummy[r][c].setId(null);
				dummyAni[r][c].setImage(empty);
				dummyAni[r][c].setId(null);
				dummyAni[r][c].setVisible(false);
			}
		}	
	}
	

}//***end_of_GridSystem class
