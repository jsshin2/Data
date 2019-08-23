package application;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


//******************************************************
//Blocks class (?) (!)
//******************************************************
/* NOTES: 
* wait, before I continue the only functionality that this class has would be to
* be initialized|reseted, created, rotated, stopped?, return some value, and disabled
* (?) then what moves, checks for boundary|collision
* Above, I think moving should be done with Blocks' function that is called by outside element
* because controlling movement should be done in the same scope as grid checking...
*/
public class Blocks
{
	// member variables.. (!) something for stopping controls.. and timer
	boolean stopped = false;
	ImageView[] blocks = new ImageView[4];// <-this should only run once when it is created..
	ImageView[] cloneS = new ImageView[4];// <-this is for spacebar
	ImageView[] cloneR = new ImageView[4];// <-this is for rotation...
	Image brown = new Image("resources/images/brown_block.png");
	Image pink = new Image("resources/images/pink_block.png");
	Image blue = new Image("resources/images/blue_block.png");
	Image red = new Image("resources/images/red_block.png");
	Image green = new Image("resources/images/green_block.png");
	Image purple = new Image("resources/images/purple_block.png");
	Image yellow = new Image("resources/images/yellow_block.png");
	Image empty = new Image("resources/images/empty_block.png");
	//displaying one of the small blocks on the side...
	ImageView small = new ImageView();
	Image smallBrown = new Image("resources/images/brownSmall.png");
	Image smallPink = new Image("resources/images/pinkSmall.png");
	Image smallBlue = new Image("resources/images/blueSmall.png");
	Image smallRed = new Image("resources/images/redSmall.png");
	Image smallGreen = new Image("resources/images/greenSmall.png");
	Image smallPurple = new Image("resources/images/purpleSmall.png");
	Image smallYellow = new Image("resources/images/yellowSmall.png");
	Image tmp = new Image("resources/images/empty_block.png");
	int state;
	int color;
	static int next;//for storing last array element of color info for last block..?
	int nextCounter;
	int b_size = 18;//17 + 1 offset?
	private int blockNum;
	int whatNext[] = new int[7];//for randomizing color
	boolean visible;
	
			
	
	//***************************************************
	// constructor (?)
	//***************************************************
	/* NOTES:
	 * takes in color, pane and rndPos(?) to added to..
	 */
	Blocks(Pane root)// 1=line, 2= d(R), 3= d(L) 4=L(L) 5=L(R) 6=mountain 7=cube
	{
		int b_size = 17;//to resize some pictures
		for (int i = 0; i < 4; i++)
		{
			blocks[i] = new ImageView();
			blocks[i].setId("blue"+String.valueOf(i));
			cloneS[i] = new ImageView("resources/images/cloneBox.png");
			cloneS[i].setId("cloneS" +String.valueOf(i));
			cloneS[i].setFitHeight(b_size);
			cloneS[i].setFitWidth(b_size);
			cloneR[i] = new ImageView("resources/images/bound_block.png");
			cloneR[i].setId("cloneR" +String.valueOf(i));
			cloneR[i].setFitHeight(b_size);
			cloneR[i].setFitWidth(b_size);
		}
		
		//generate new randomNumbers
		whatNext = rndNumGen();
		next = -1;//to let initialize know that it's the first time//which would have to be reseted when game is over
		nextCounter = 0;
		
		//and one more for starting out the block's first block???
		Random firstRand = new Random();
		blockNum = firstRand.nextInt(7) + 1;
		
		initialize();
		//add to the root this has to be here and not inside initialize()
		root.getChildren().addAll(blocks);
		root.getChildren().addAll(cloneS);
		//root.getChildren().addAll(cloneR);
		root.getChildren().add(small);
	}

//***************************************************
// initialize | reset(!)
//***************************************************
/* NOTES:
 * later, this function needs to use blockGenerator(?) function to generate blocks according to given random color...
 * (!) there is something wrong with displaying next when reseted from the gameOver state!!! fix
 */
void initialize()
{
	state = 1;//reset rotation to default?
	
	for(int i = 0; i < 4; i++)
	{
		cloneS[i].setVisible(false);
	}
	visible = false;
	
	//if it is the first blocks after game starts!!
	if(next == -1)
	{
		color = blockNum;
		make(blockNum);
		next = whatNext[0];//first element of whatNext[]
		smalls(next);
		nextCounter = 1;
		color = blockNum;
	}
	else
	{
		//for randomizing blocks
		if(nextCounter == 6)//<-last element of whatNext[]
		{
			blockNum = next;
			whatNext = rndNumGen();//generate new set of random number 
			next = whatNext[0];//first element of whatNext[]
			nextCounter = 1;
		}
		else
		{
			blockNum = next;
			next = whatNext[nextCounter];
			nextCounter++;
		}
		color = blockNum;
		tmp = getColor();
		make(blockNum);
		smalls(next);
		
	}
	
}

//***************************************************
//smalls
//***************************************************
/* NOTES:
*/
void smalls(int number)
{
	switch(number)
	{
	case 1://brown
		small.setImage(smallBrown);
		small.setX(432);
		small.setY(205);
		break;
	case 2://pink
		small.setImage(smallPink);
		small.setX(440);
		small.setY(198);
		break;
	case 3://blue
		small.setImage(smallBlue);
		small.setX(440);
		small.setY(198);
		break;
	case 4://red
		small.setImage(smallRed);
		small.setX(440);
		small.setY(198);
		break;
	case 5://green
		small.setImage(smallGreen);
		small.setX(440);
		small.setY(198);
		break;
	case 6://purple
		small.setImage(smallPurple);
		small.setX(440);
		small.setY(198);
		break;
	case 7://yellow
		small.setImage(smallYellow);
		small.setX(445);
		small.setY(198);
		break;
	}
}

//***************************************************
//make
//***************************************************
/* NOTES:
* this takes in random int # and make blocks according to the number??? and return what type??
* also make takes x and y for moving...
* need to make cloneR in here as well..
*/
void make(int number)
{
	//initial stating position + offset
	int x = 97 + (b_size * 3);
	int y = 70 - (b_size);
	int size = 18;
	switch(number)
	{
	case 1://line-shaped (!) done with resizing
		for(int i = 0; i < 4; i++)
		{
			blocks[i].setImage(brown);
			blocks[i].setX(x + (i * 17 + i));
			blocks[i].setY(y);
		}
		//++++++++++++for.cloneS(==blocks)++++++++++++++//
		for(int i = 0; i < 4; i++)
		{
			cloneS[i].setX(blocks[i].getX());//blocks initial position
			cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
		}
		//++++++++++++for.cloneR(next_state)++++++++++++++//
			//first
			cloneR[0].setX(blocks[0].getX() + b_size*2);
			cloneR[0].setY(blocks[0].getY() + b_size);
			//rest
			for(int i = 1; i < 4; i++)
			{
				cloneR[i].setX(cloneR[0].getX());
				cloneR[i].setY(cloneR[i-1].getY() - b_size);
			}
		break;
		
	case 2://d?-shaped(R)
		//first
		blocks[0].setImage(pink);
		blocks[0].setX(x);
		blocks[0].setY(y);
		//second
		blocks[1].setImage(pink);
		blocks[1].setX(blocks[0].getX() + size);
		blocks[1].setY(blocks[0].getY());
		//third
		blocks[2].setImage(pink);
		blocks[2].setX(blocks[1].getX());
		blocks[2].setY(blocks[1].getY() - size);
		//fourth
		blocks[3].setImage(pink);
		blocks[3].setX(blocks[2].getX() + size);
		blocks[3].setY(blocks[2].getY());
		//++++++++++++for.cloneS(==blocks+offset)++++++++++++++//
		for(int i = 0; i < 4; i++)
		{
			cloneS[i].setX(blocks[i].getX());//blocks initial position
			cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
		}
		//++++++++++++for.cloneR(next_state)++++++++++++++//
			cloneR[0].setX(blocks[0].getX() + b_size*2);//(!) 3 here is an offset
			cloneR[0].setY(blocks[0].getY());
			//second
			cloneR[1].setX(cloneR[0].getX());
			cloneR[1].setY(cloneR[0].getY() - b_size);
			//third
			cloneR[2].setX(cloneR[1].getX() - b_size);
			cloneR[2].setY(cloneR[1].getY());
			//fourth
			cloneR[3].setX(cloneR[2].getX());
			cloneR[3].setY(cloneR[2].getY() - b_size);
		break;
	case 3://d?-shaped(L)
		//first
		blocks[0].setImage(blue);
		blocks[0].setX(x);
		blocks[0].setY(y - size);
		//second
		blocks[1].setImage(blue);
		blocks[1].setX(blocks[0].getX() + size);
		blocks[1].setY(blocks[0].getY());
		//third
		blocks[2].setImage(blue);
		blocks[2].setX(blocks[1].getX());
		blocks[2].setY(blocks[1].getY() + size);
		//fourth
		blocks[3].setImage(blue);
		blocks[3].setX(blocks[2].getX() + size);
		blocks[3].setY(blocks[2].getY());
		//++++++++++++for.cloneS(==blocks+offset)++++++++++++++//
		for(int i = 0; i < 4; i++)
		{
			cloneS[i].setX(blocks[i].getX());//blocks initial position
			cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
		}
		//++++++++++++for.cloneR(next_state)++++++++++++++//
		//first
			//first
			cloneR[0].setX(blocks[0].getX() + b_size);
			cloneR[0].setY(blocks[0].getY() + b_size);
			//second
			cloneR[1].setX(cloneR[0].getX());
			cloneR[1].setY(cloneR[0].getY() - b_size);
			//third
			cloneR[2].setX(cloneR[1].getX() + b_size);
			cloneR[2].setY(cloneR[1].getY());
			//fourth
			cloneR[3].setX(cloneR[2].getX());
			cloneR[3].setY(cloneR[2].getY() - b_size);
		break;
	case 4://L-shaped(L)red
		//first
		blocks[0].setImage(red);
		blocks[0].setX(x);
		blocks[0].setY(y - size);
		//rest
		for(int i = 1; i < 4; i++)
		{
			blocks[i].setImage(red);
			blocks[i].setX(blocks[0].getX() + ((i-1) * size));
			blocks[i].setY(blocks[0].getY() + size);
		}
		//++++++++++++for.cloneS(==blocks+offset)++++++++++++++//
		for(int i = 0; i < 4; i++)
		{
			cloneS[i].setX(blocks[i].getX());//blocks initial position
			cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
		}
		//++++++++++++for.cloneR(next_state)++++++++++++++//
		//first
			cloneR[0].setX(blocks[0].getX() + b_size);
			cloneR[0].setY(blocks[0].getY() + b_size);
			//rest
			for(int i = 1; i < 4; i++)
			{
				cloneR[i].setX(cloneR[0].getX() + b_size);
				cloneR[i].setY(cloneR[0].getY() + ((i-1) * - b_size));
			}
		break;
	case 5://L-shaped(R)green
		//first
		blocks[0].setImage(green);
		blocks[0].setX(x + (size*2));
		blocks[0].setY(y - size);
		//rest
		for(int i = 1; i < 4; i++)
		{
			blocks[i].setImage(green);
			blocks[i].setX(blocks[0].getX() - ((i-1) * size));
			blocks[i].setY(blocks[0].getY() + size);
		}
		//++++++++++++for.cloneS(==blocks+offset)++++++++++++++//
		for(int i = 0; i < 4; i++)
		{
			cloneS[i].setX(blocks[i].getX());//blocks initial position
			cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
		}
		//++++++++++++for.cloneR(next_state)++++++++++++++//
		//first
			cloneR[0].setX(blocks[0].getX() - b_size);
			cloneR[0].setY(blocks[0].getY() - b_size);
			//rest
			for(int i = 1; i < 4; i++)
			{
				cloneR[i].setX(cloneR[0].getX() + b_size);
				cloneR[i].setY(cloneR[0].getY() + ((i-1) * b_size));
			}
		break;
	case 6://Mountain-shaped
		//first
		blocks[0].setImage(purple);
		blocks[0].setX(x + size);
		blocks[0].setY(y - size);
		//second
		blocks[1].setImage(purple);
		blocks[1].setX(blocks[0].getX());
		blocks[1].setY(blocks[0].getY() + size);
		//third
		blocks[2].setImage(purple);
		blocks[2].setX(blocks[1].getX() - size);
		blocks[2].setY(blocks[1].getY());
		//fourth
		blocks[3].setImage(purple);
		blocks[3].setX(blocks[1].getX() + size);
		blocks[3].setY(blocks[1].getY());
		//++++++++++++for.cloneS(==blocks+offset)++++++++++++++//
		for(int i = 0; i < 4; i++)
		{
			cloneS[i].setX(blocks[i].getX());//blocks initial position
			cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
		}
		//++++++++++++for.cloneR(next_state)++++++++++++++//
		//first
			cloneR[0].setX(blocks[0].getX());
			cloneR[0].setY(blocks[0].getY());
			//second
			cloneR[1].setX(cloneR[0].getX() + b_size);
			cloneR[1].setY(cloneR[0].getY());
			//third
			cloneR[2].setX(cloneR[1].getX());
			cloneR[2].setY(cloneR[1].getY() + b_size);
			//fourth
			cloneR[3].setX(cloneR[1].getX());
			cloneR[3].setY(cloneR[1].getY() - b_size);
		break;
	case 7://cube-shaped
		//first
		blocks[0].setImage(yellow);
		blocks[0].setX(x);
		blocks[0].setY(y);
		//second
		blocks[1].setImage(yellow);
		blocks[1].setX(blocks[0].getX() + size);
		blocks[1].setY(blocks[0].getY());
		//third
		blocks[2].setImage(yellow);
		blocks[2].setX(blocks[1].getX());
		blocks[2].setY(blocks[1].getY() - size);
		//fourth
		blocks[3].setImage(yellow);
		blocks[3].setX(blocks[2].getX() - size);
		blocks[3].setY(blocks[2].getY());
		//++++++++++++for.cloneS(==blocks+offset)++++++++++++++//
		for(int i = 0; i < 4; i++)
		{
			cloneS[i].setX(blocks[i].getX());//blocks initial position
			cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
		}
		//++++++++++++for.cloneR(next_state)++++++++++++++//
		//first
			cloneR[0].setX(blocks[0].getX());
			cloneR[0].setY(blocks[0].getY());
			//second
			cloneR[1].setX(cloneR[0].getX() + b_size);
			cloneR[1].setY(cloneR[0].getY());
			//third
			cloneR[2].setX(cloneR[1].getX());
			cloneR[2].setY(cloneR[1].getY() - b_size);
			//fourth
			cloneR[3].setX(cloneR[2].getX() - b_size);
			cloneR[3].setY(cloneR[2].getY());
		break;
	}	
}

//******************************************************
// randomize() ->for colors and position
//******************************************************
/* NOTES:
 * get permutation 7P7 = 5040 possibilities of # of ways that blocks can be ordered
 * make an array of 7 spaces and fill it with first permutatioin made
 * when it is called, give out one then fill that spot(?) with 0<-means empty then, if all empty,
 * run permutation again...
 */
int[] rndNumGen()
{
	Random rand = new Random();
	int rndNum;
	int permutated[] = new int[7];//java initializes it into zeroes...
	int counter = 1;
	int diffCount = 0;
	//get first one into the array
	permutated[0] = rand.nextInt(7) + 1;
	
	//lets get the last
	while(counter < 7)
	{/*all filled with permutated values*/
		
		//make new rndNum
		rndNum = rand.nextInt(7) + 1;
		
		//checking system
		for(int i = 0; i < counter; i++)/*number of places to compare*/
		{
			//if they are the same which is BAD!
			if(permutated[i] == rndNum)
			{
				diffCount = 0;//resets diffCount because it doesn't matter anymore..
				break;//<- if same break out of the loop and run the while loop again..
			}
				
			else//if they are not the same
				diffCount++;
		}
		//if this # differs with every other value
		if(diffCount == counter)
		{
			permutated[counter] = rndNum;
			counter++;
			diffCount = 0;//reset diffCount to zero for other values
		}
	}
	return permutated;
}
//***************************************************
// updateClone
//***************************************************
/* NOTES:
 * 
 */
void updateClone()
{
	for (int i= 0; i < 4; i++)
	{
		//cloneS; also involves getCol
		cloneS[i].setX(blocks[i].getX());
		cloneS[i].setY((428 + blocks[i].getY() - 68/*tmp_solution*/) );
	}
}

//***************************************************
// rotation
//***************************************************
/* NOTES:
 */
void rotate()
{
	//get curX from the first element of blocks: blocks[0] b/c every other blocks' position depend on it...
	double curX = blocks[0].getX();
	double curY = blocks[0].getY();
	double curRX = cloneR[0].getX();
	double curRY = cloneR[0].getY();
	//changing switch value..
	state++;
	if(state > 4)
	{
		state = 1;
	}
	switch(color)
	{
		case 1://line-shaped
			switch(state)
			{
				case 1://initial state :from 4th
					//first
					blocks[0].setX(curX - b_size);
					blocks[0].setY(curY + b_size*2);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[i-1].getX() + b_size);
						blocks[i].setY(blocks[0].getY());
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)// I actually need this here...
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());//getCol's Algorithm needs to be applied here... 
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size*2);
					cloneR[0].setY(curRY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX());
						cloneR[i].setY(cloneR[i-1].getY() - b_size);
					}
					break;
				case 2://2nd state :from 1st
					//first
					blocks[0].setX(curX + b_size*2);
					blocks[0].setY(curY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX());
						blocks[i].setY(blocks[i-1].getY() - b_size);
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)// I actually need this here...
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size);
					cloneR[0].setY(curRY - b_size*2);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[i-1].getX()- b_size);
						cloneR[i].setY(cloneR[0].getY());
					}
					break;
				case 3://3rd state :from 2nd
					//first
					blocks[0].setX(curX + b_size);
					blocks[0].setY(curY - b_size*2);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[i-1].getX()- b_size);
						blocks[i].setY(blocks[0].getY());
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)// I actually need this here...
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size*2);
					cloneR[0].setY(curRY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX());
						cloneR[i].setY(cloneR[i-1].getY() + b_size);
					}
					break;
				case 4://4th state :from 3rd
					//first
					blocks[0].setX(curX - b_size*2);
					blocks[0].setY(curY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX());
						blocks[i].setY(blocks[i-1].getY() + b_size);
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)// I actually need this here...
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size);
					cloneR[0].setY(curRY + b_size*2);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[i-1].getX() + b_size);
						cloneR[i].setY(cloneR[0].getY());
					}
					break;
			}
			break;
		case 2://d?-shaped(R)
			switch(state)
			{
				case 1://initial state
					//first
					blocks[0].setX(curX);
					blocks[0].setY(curY + b_size*2);
					//second
					blocks[1].setX(blocks[0].getX() + b_size);
					blocks[1].setY(blocks[0].getY());
					//third
					blocks[2].setX(blocks[1].getX());
					blocks[2].setY(blocks[1].getY() - b_size);
					//fourth
					blocks[3].setX(blocks[2].getX() + b_size);
					blocks[3].setY(blocks[2].getY());
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)// I actually need this here...
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());//getCol's Algorithm needs to be applied here... 
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size*2);
					cloneR[0].setY(curRY);
					//second
					cloneR[1].setX(cloneR[0].getX() );
					cloneR[1].setY(cloneR[0].getY() - b_size );
					//third
					cloneR[2].setX(cloneR[1].getX() - b_size );
					cloneR[2].setY(cloneR[1].getY() );
					//fourth
					cloneR[3].setX(cloneR[2].getX() );
					cloneR[3].setY(cloneR[2].getY() - b_size );
					break;
				case 2://2nd state
					blocks[0].setX(curX + b_size*2);
					blocks[0].setY(curY);
					//second
					blocks[1].setX(blocks[0].getX());
					blocks[1].setY(blocks[0].getY() - b_size);
					//third
					blocks[2].setX(blocks[1].getX() - b_size);
					blocks[2].setY(blocks[1].getY());
					//fourth
					blocks[3].setX(blocks[2].getX());
					blocks[3].setY(blocks[2].getY() - b_size);
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					cloneR[0].setX(curRX);
					cloneR[0].setY(curRY - b_size*2 );
					//second
					cloneR[1].setX(cloneR[0].getX() - b_size );
					cloneR[1].setY(cloneR[0].getY() );
					//third
					cloneR[2].setX(cloneR[1].getX() );
					cloneR[2].setY(cloneR[1].getY() + b_size );
					//fourth
					cloneR[3].setX(cloneR[2].getX() - b_size );
					cloneR[3].setY(cloneR[2].getY() );
					break;
				case 3://3rd state
					blocks[0].setX(curX);
					blocks[0].setY(curY - b_size*2);
					//second
					blocks[1].setX(blocks[0].getX() - b_size);
					blocks[1].setY(blocks[0].getY());
					//third
					blocks[2].setX(blocks[1].getX());
					blocks[2].setY(blocks[1].getY() + b_size);
					//fourth
					blocks[3].setX(blocks[2].getX() - b_size);
					blocks[3].setY(blocks[2].getY());
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					cloneR[0].setX(curRX  - b_size*2 );
					cloneR[0].setY(curRY );
					//second
					cloneR[1].setX(cloneR[0].getX() );
					cloneR[1].setY(cloneR[0].getY() + b_size );
					//third
					cloneR[2].setX(cloneR[1].getX() + b_size );
					cloneR[2].setY(cloneR[1].getY() );
					//fourth
					cloneR[3].setX(cloneR[2].getX() );
					cloneR[3].setY(cloneR[2].getY() + b_size );
					break;
				case 4://4th state
					blocks[0].setX(curX  - b_size*2);
					blocks[0].setY(curY);
					//second
					blocks[1].setX(blocks[0].getX());
					blocks[1].setY(blocks[0].getY() + b_size);
					//third
					blocks[2].setX(blocks[1].getX() + b_size);
					blocks[2].setY(blocks[1].getY());
					//fourth
					blocks[3].setX(blocks[2].getX());
					blocks[3].setY(blocks[2].getY() + b_size);
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX );
					cloneR[0].setY(curRY + b_size*2 );
					//second
					cloneR[1].setX(cloneR[0].getX() + b_size );
					cloneR[1].setY(cloneR[0].getY() );
					//third
					cloneR[2].setX(cloneR[1].getX() );
					cloneR[2].setY(cloneR[1].getY() - b_size );
					//fourth
					cloneR[3].setX(cloneR[2].getX() + b_size );
					cloneR[3].setY(cloneR[2].getY() );
					break;
			}
			break;
		case 3://d?-shaped(L)
			switch(state)
			{
				case 1://initial state
					//first
					blocks[0].setX(curX - b_size);
					blocks[0].setY(curY + b_size);
					//second
					blocks[1].setX(blocks[0].getX() + b_size);
					blocks[1].setY(blocks[0].getY());
					//third
					blocks[2].setX(blocks[1].getX());
					blocks[2].setY(blocks[1].getY() + b_size);
					//fourth
					blocks[3].setX(blocks[2].getX() + b_size);
					blocks[3].setY(blocks[2].getY());
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size);
					cloneR[0].setY(curRY + b_size);
					//second
					cloneR[1].setX(cloneR[0].getX());
					cloneR[1].setY(cloneR[0].getY() - b_size);
					//third
					cloneR[2].setX(cloneR[1].getX() + b_size);
					cloneR[2].setY(cloneR[1].getY());
					//fourth
					cloneR[3].setX(cloneR[2].getX());
					cloneR[3].setY(cloneR[2].getY() - b_size);
					break;
				case 2://2nd state
					//first
					blocks[0].setX(curX + b_size);
					blocks[0].setY(curY + b_size);
					//second
					blocks[1].setX(blocks[0].getX());
					blocks[1].setY(blocks[0].getY() - b_size);
					//third
					blocks[2].setX(blocks[1].getX() + b_size);
					blocks[2].setY(blocks[1].getY());
					//fourth
					blocks[3].setX(blocks[2].getX());
					blocks[3].setY(blocks[2].getY() - b_size);
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size);
					cloneR[0].setY(curRY - b_size);
					//second
					cloneR[1].setX(cloneR[0].getX() - b_size);
					cloneR[1].setY(cloneR[0].getY());
					//third
					cloneR[2].setX(cloneR[1].getX());
					cloneR[2].setY(cloneR[1].getY() - b_size);
					//fourth
					cloneR[3].setX(cloneR[2].getX() - b_size);
					cloneR[3].setY(cloneR[2].getY());
					break;
				case 3://3rd state
					//first
					blocks[0].setX(curX + b_size);
					blocks[0].setY(curY - b_size);
					//second
					blocks[1].setX(blocks[0].getX() - b_size);
					blocks[1].setY(blocks[0].getY());
					//third
					blocks[2].setX(blocks[1].getX());
					blocks[2].setY(blocks[1].getY() - b_size);
					//fourth
					blocks[3].setX(blocks[2].getX() - b_size);
					blocks[3].setY(blocks[2].getY());
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size);
					cloneR[0].setY(curRY - b_size);
					//second
					cloneR[1].setX(cloneR[0].getX());
					cloneR[1].setY(cloneR[0].getY() + b_size);
					//third
					cloneR[2].setX(cloneR[1].getX() - b_size);
					cloneR[2].setY(cloneR[1].getY());
					//fourth
					cloneR[3].setX(cloneR[2].getX());
					cloneR[3].setY(cloneR[2].getY() + b_size);
					break;
				case 4://4th state
					//first
					blocks[0].setX(curX - b_size);
					blocks[0].setY(curY - b_size);
					//second
					blocks[1].setX(blocks[0].getX());
					blocks[1].setY(blocks[0].getY() + b_size);
					//third
					blocks[2].setX(blocks[1].getX() - b_size);
					blocks[2].setY(blocks[1].getY());
					//fourth
					blocks[3].setX(blocks[2].getX());
					blocks[3].setY(blocks[2].getY() + b_size);
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size);
					cloneR[0].setY(curRY + b_size);
					//second
					cloneR[1].setX(cloneR[0].getX() + b_size);
					cloneR[1].setY(cloneR[0].getY());
					//third
					cloneR[2].setX(cloneR[1].getX());
					cloneR[2].setY(cloneR[1].getY() + b_size);
					//fourth
					cloneR[3].setX(cloneR[2].getX() + b_size);
					cloneR[3].setY(cloneR[2].getY());
					break;
			}
			break;
		case 4://L-shaped(L)
			switch(state)
			{
				case 1://initial state
					//first
					blocks[0].setX(curX - b_size);
					blocks[0].setY(curY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() + ((i-1) * + b_size));
						blocks[i].setY(blocks[0].getY() + b_size);
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size);
					cloneR[0].setY(curRY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() + b_size);
						cloneR[i].setY(cloneR[0].getY() + ((i-1) * - b_size));
					}
					break;
				case 2://2nd state
					//first
					blocks[0].setX(curX + b_size);
					blocks[0].setY(curY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() + b_size);
						blocks[i].setY(blocks[0].getY() + ((i-1) * - b_size));
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size);
					cloneR[0].setY(curRY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() + ((i-1) * - b_size));
						cloneR[i].setY(cloneR[0].getY() - b_size);
					}
					break;
				case 3://3rd state
					//first
					blocks[0].setX(curX + b_size);
					blocks[0].setY(curY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() + ((i-1) * - b_size));
						blocks[i].setY(blocks[0].getY() - b_size);
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size);
					cloneR[0].setY(curRY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() - b_size);
						cloneR[i].setY(cloneR[0].getY() + ((i-1) * + b_size));
					}
					break;
				case 4://4th state
					//first
					blocks[0].setX(curX - b_size);
					blocks[0].setY(curY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() - b_size);
						blocks[i].setY(blocks[0].getY() + ((i-1) * + b_size));
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size);
					cloneR[0].setY(curRY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() + ((i-1) * + b_size));
						cloneR[i].setY(cloneR[0].getY() + b_size);
					}
					break;
			}
			break;
		case 5://L-sahped(R)
			switch(state)
			{
				case 1://initial state	
					//first
					blocks[0].setX(curX + b_size);
					blocks[0].setY(curY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() - ((i-1) * b_size));
						blocks[i].setY(blocks[0].getY() + b_size);
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size);
					cloneR[0].setY(curRY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() + b_size);
						cloneR[i].setY(cloneR[0].getY() + ((i-1) * b_size));
					}
					break;
				case 2://2nd state
					//first
					blocks[0].setX(curX - b_size);
					blocks[0].setY(curY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() + b_size);
						blocks[i].setY(blocks[0].getY() + ((i-1) * b_size));
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX - b_size);
					cloneR[0].setY(curRY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() + ((i-1) * b_size));
						cloneR[i].setY(cloneR[0].getY() - b_size);
					}
					break;
				case 3://3rd state
					//first
					blocks[0].setX(curX - b_size);
					blocks[0].setY(curY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() + ((i-1) * b_size));
						blocks[i].setY(blocks[0].getY() - b_size);
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size);
					cloneR[0].setY(curRY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() - b_size);
						cloneR[i].setY(cloneR[0].getY() - ((i-1) * b_size));
					}
					break;
				case 4://4th state
					//first
					blocks[0].setX(curX + b_size);
					blocks[0].setY(curY + b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						blocks[i].setX(blocks[0].getX() - b_size);
						blocks[i].setY(blocks[0].getY() - ((i-1) * b_size));
					}
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX + b_size);
					cloneR[0].setY(curRY - b_size);
					//rest
					for(int i = 1; i < 4; i++)
					{
						cloneR[i].setX(cloneR[0].getX() - ((i-1) * b_size));
						cloneR[i].setY(cloneR[0].getY() + b_size);
					}
					break;
			}
			break;
		case 6://Mountain-shaped(!) just know that this is not the conventional way!!!
			switch(state)
			{
				case 1://initial state
					//first
					blocks[0].setX(curX);
					blocks[0].setY(curY);
					//second
					blocks[1].setX(blocks[0].getX());
					blocks[1].setY(blocks[0].getY() + b_size);
					//third
					blocks[2].setX(blocks[1].getX() - b_size);
					blocks[2].setY(blocks[1].getY());
					//fourth
					blocks[3].setX(blocks[1].getX() + b_size);
					blocks[3].setY(blocks[1].getY());
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX);
					cloneR[0].setY(curRY);
					//second
					cloneR[1].setX(cloneR[0].getX() + b_size);
					cloneR[1].setY(cloneR[0].getY());
					//third
					cloneR[2].setX(cloneR[1].getX());
					cloneR[2].setY(cloneR[1].getY() + b_size);
					//fourth
					cloneR[3].setX(cloneR[1].getX());
					cloneR[3].setY(cloneR[1].getY() - b_size);
					break;
				case 2://2nd state
					//first
					blocks[0].setX(curX);
					blocks[0].setY(curY);
					//second
					blocks[1].setX(blocks[0].getX() + b_size);
					blocks[1].setY(blocks[0].getY());
					//third
					blocks[2].setX(blocks[1].getX());
					blocks[2].setY(blocks[1].getY() + b_size);
					//fourth
					blocks[3].setX(blocks[1].getX());
					blocks[3].setY(blocks[1].getY() - b_size);
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX);
					cloneR[0].setY(curRY);
					//second
					cloneR[1].setX(cloneR[0].getX());
					cloneR[1].setY(cloneR[0].getY() - b_size);
					//third
					cloneR[2].setX(cloneR[1].getX() + b_size);
					cloneR[2].setY(cloneR[1].getY());
					//fourth
					cloneR[3].setX(cloneR[1].getX() - b_size);
					cloneR[3].setY(cloneR[1].getY());
					break;
				case 3://3rd state
					//first
					blocks[0].setX(curX);
					blocks[0].setY(curY);
					//second
					blocks[1].setX(blocks[0].getX());
					blocks[1].setY(blocks[0].getY() - b_size);
					//third
					blocks[2].setX(blocks[1].getX() + b_size);
					blocks[2].setY(blocks[1].getY());
					//fourth
					blocks[3].setX(blocks[1].getX() - b_size);
					blocks[3].setY(blocks[1].getY());
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX);
					cloneR[0].setY(curRY);
					//second
					cloneR[1].setX(cloneR[0].getX() - b_size);
					cloneR[1].setY(cloneR[0].getY());
					//third
					cloneR[2].setX(cloneR[1].getX());
					cloneR[2].setY(cloneR[1].getY() - b_size);
					//fourth
					cloneR[3].setX(cloneR[1].getX());
					cloneR[3].setY(cloneR[1].getY() + b_size);
					break;
				case 4://4th state
					//first
					blocks[0].setX(curX);
					blocks[0].setY(curY);
					//second
					blocks[1].setX(blocks[0].getX() - b_size);
					blocks[1].setY(blocks[0].getY());
					//third
					blocks[2].setX(blocks[1].getX());
					blocks[2].setY(blocks[1].getY() - b_size);
					//fourth
					blocks[3].setX(blocks[1].getX());
					blocks[3].setY(blocks[1].getY() + b_size);
					//++++++++++++++add.cloneS+++++++++++++++++//
					for(int i = 0; i < 4; i++)
					{
						cloneS[i].setX(blocks[i].getX());
						cloneS[i].setY(blocks[i].getY());
					}
					//++++++++++++++add.cloneR+++++++++++++++++//
					//first
					cloneR[0].setX(curRX);
					cloneR[0].setY(curRY);
					//second
					cloneR[1].setX(cloneR[0].getX());
					cloneR[1].setY(cloneR[0].getY() + b_size);
					//third
					cloneR[2].setX(cloneR[1].getX() - b_size);
					cloneR[2].setY(cloneR[1].getY());
					//fourth
					cloneR[3].setX(cloneR[1].getX() + b_size);
					cloneR[3].setY(cloneR[1].getY());
					break;
			}
			break;
		case 7://cube-shaped
			//first
			blocks[0].setX(curX);
			blocks[0].setY(curY);
			//second
			blocks[1].setX(blocks[0].getX() + b_size);
			blocks[1].setY(blocks[0].getY());
			//third
			blocks[2].setX(blocks[1].getX());
			blocks[2].setY(blocks[1].getY() - b_size);
			//fourth
			blocks[3].setX(blocks[2].getX() - b_size);
			blocks[3].setY(blocks[2].getY());
			//++++++++++++++add.cloneS+++++++++++++++++//
			for(int i = 0; i < 4; i++)
			{
				cloneS[i].setX(blocks[i].getX());
				cloneS[i].setY(blocks[i].getY());
			}
			//++++++++++++++add.cloneR+++++++++++++++++//
			//first
			cloneR[0].setX(curRX);
			cloneR[0].setY(curRY);
			//second
			cloneR[1].setX(cloneR[0].getX() + b_size);
			cloneR[1].setY(cloneR[0].getY());
			//third
			cloneR[2].setX(cloneR[1].getX());
			cloneR[2].setY(cloneR[1].getY() - b_size);
			//fourth
			cloneR[3].setX(cloneR[2].getX() - b_size);
			cloneR[3].setY(cloneR[2].getY());
			break;
	}
}
//***************************************************
//getColor
//***************************************************
/* NOTES:
 * this function gives color information to other classes like GridSystem...
 * below these functions might not be needed...
*/
Image getColor()
{
	switch(color)
	{
	case 1:
		return brown;
	case 2:
		return pink;
	case 3:
		return blue;
	case 4:
		return red;
	case 5:
		return green;
	case 6:
		return purple;
	case 7:
		return yellow;
	}
	return empty;//default case???
}
String getColorName()
{
	switch(color)
	{
	case 1:
		return "brown";
	case 2:
		return "pink";
	case 3:
		return "blue";
	case 4:
		return "red";
	case 5:
		return "green";
	case 6:
		return "purple";
	case 7:
		return "yellow";
	}
	return "";
}
Image setColor(String color)
{
	if(color == "brown")
	{
		return brown;
	}
	else if(color == "pink")
	{
		return pink;
	}
	else if(color == "blue")
	{
		return blue;
	}
	else if(color == "red")
	{
		return red;
	}
	else if(color == "green")
	{
		return green;
	}
	else if(color == "purple")
	{
		return purple;
	}
	else if(color == "yellow")
	{
		return yellow;
	}
	return empty;
}
	//*******************************************
	//reset
	//*******************************************
	/* NOTES:
	 * lets write down everything that needs to be reseted
	 */
	void reset()
	{
		//don't really need to do anything, because it is not really resetting, so it will just continue as normal...
	}
}//***end_of_Block class
