package application;

import java.awt.AWTException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.awt.Robot;//for robot class
import java.util.Vector;//for using vectors
import javafx.scene.input.*;//for keyboard use
import javafx.event.EventHandler; // for keyboard events..
import javafx.geometry.Insets;
import javafx.animation.AnimationTimer;//2 for timer..
import javafx.concurrent.*;//for task

//******************************************************
// Main class
//******************************************************
/* NOTES:
 * to test, I think I will make multiple red_boxes in array
 * then make it turn solid(?) by me clicking on it with a mouse
 * and see if it will cause collision by the blocks...
 */
public class Main extends Application {
// member variables
	int x, y;
	int horM = 18;
	double verM = 18; //can also think of this as speed at which the block goes down..
	int level = 1;
	static Image empty = new Image("resources/images/empty_block.png");
    GridSystem gs;
	Robot robot; PointSystem ps;
	Blocks b; UI ui;ScoreSystem ss;
	static boolean freeze = true;//need to be in false in order for game to be controlled
	static boolean inControl = true;
	static ImageView paused = new ImageView("resources/images/paused.png");
	static ImageView gameOver = new ImageView("resources/images/gameOver.png");
	static String currentState;
	static ImageView top = new ImageView("resources/images/bound_block.png");
	//bounds for collision system..
	static Vector<ImageView> bounds = new Vector<ImageView>();



	//======================================================
	// start() //overwritten
	//======================================================	
	//GAMESPACE dimension and offset from the origin
			//dimension 180x360
			//x:96 y:69, x2:276? y2:428
	@Override
	public void start(Stage primaryStage){

		//set currentState to gameOver;
		currentState = "gameOver";//there is runGame, gameOver, pauseGame
		
		//make new pane and set its id
		final Pane root = new Pane();
		root.setId("pane");

		//make new block obj & clone(just to see )
		b = new Blocks(root);
	
		//set blocks invisible when gameOver
		for(int i = 0; i < 4; i++)
		{
			b.blocks[i].setVisible(false);
		}

		// I'm going to create four bounds left, right, bottom, top
		// don't really need image source, and need to add to the scene to work..
		top.setX(96);
		top.setY(69 - 17*3);
		top.setId("top_B");
		top.setFitWidth(181);
		top.setFitHeight(17*3);
		//root.getChildren().add(top);//remember this is not part of the bounds

		ImageView left = new ImageView("resources/images/bound_block.png");
		left.setX(96-17);
		left.setY(69-17*2);
		left.setId("left_B");
		left.setFitWidth(17);
		left.setFitHeight(360+17*2);
		bounds.add(left);
		//root.getChildren().add(left);
		ImageView right = new ImageView("resources/images/bound_block.png");
		right.setX(277);
		right.setY(69-17*2);
		right.setId("right_B");
		right.setFitWidth(17);
		right.setFitHeight(360+17*2);
		bounds.add(right);
		//root.getChildren().add(right);
		ImageView bottom = new ImageView("resources/images/bound_block.png");
		bottom.setX(96);
		bottom.setY(430);
		bottom.setId("bottom_B");
		bottom.setFitWidth(180);
		bottom.setFitHeight(17);
		bounds.add(bottom);
		//root.getChildren().add(bottom);

		//gridSystem
		gs = new GridSystem(b,root, bounds);

		//add images to root
		paused.setX(128);
		paused.setY(225);
		paused.setVisible(false);
		root.getChildren().add(paused);
		gameOver.setX(128);
		gameOver.setY(225);
		gameOver.setVisible(true);
		root.getChildren().add(gameOver);

		//pointSystem object
		ps = new PointSystem(root);

		//for User Interface
		ui = new UI(root, b, gs);

		//ScoreSystem
		 ss = new ScoreSystem(root);

		//control function for keyboard interaction...
		control(b,bounds);

		// Make new Scene and set the stage
		Scene scene = new Scene(root);
		scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());
		primaryStage.setWidth(543);
		primaryStage.setHeight(530);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Tetris Game");
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	//===================================================
	// control (!)
	//===================================================
	/* NOTES:
	 * (!) here is what needs to happen. when down key is pressed and status is *bad(meaning collision is triggered),
	 * first, stop any of the down key movement(can't press it anymore)...
	 * second, checks for any of the side(L,R) movement
	 * 		-> if left or right movement thaw freeze and do the same thing
	 * 		-> if no side movement is detected, while task is operating
	 * 			-> if detected terminate both the timer and task
	 * 			-> if not detected, then set the blocks to that position..
	 * third, reset all the movement controls; inControl, stop...
	 */
	void control(Blocks b, Vector<ImageView> bounds)
	{
		b.blocks[0].setOnKeyPressed(new EventHandler<KeyEvent>(){
			boolean leftMoving = false;
			boolean rightMoving = false;
			int status;

			@Override
			public void handle(KeyEvent ke){

				//left key
				if (ke.getCode().equals(KeyCode.LEFT) && inControl == false)
				{
					status = collision(b, bounds);
					leftMoving = true;
					freeze = false;// if I press either of left & right keys it will thaw(?) down movement downkey & spacebar
					if(status == -1 || status == 4 || status == 7 || status == 0)
					{
						//nothing
					}
					else if (inControl == false)
					{
						x--;
						for(int i = 0; i < 4; i++)
						{
							//b.blocks[i].setX(horM*x + (i * horM));// <-this is the same as up b/c this is how it is ordered..
							b.blocks[i].setX(b.blocks[i].getX() - (horM));
							//b.updateClone();
							b.cloneR[i].setX(b.cloneR[i].getX() - horM);
						}
						b.updateClone();//the order does matter?
						gs.getCols();
					}
					status = collision(b, bounds);
				}
				//right key
				else if (ke.getCode().equals(KeyCode.RIGHT) && inControl == false)
				{
					status = collision(b, bounds);
					rightMoving = true;
					freeze = false;
					if(status == 1 || status == 6 || status == 7 || status == 0)
					{
						//...
					}
					else if (inControl == false)
					{
						x++;// <-this is here because it doesn't have to loop four times..
						for(int i = 0; i < 4; i++)
						{
							b.blocks[i].setX(b.blocks[i].getX() + (horM));
							b.cloneR[i].setX(b.cloneR[i].getX() + horM);
						}
						b.updateClone();
						gs.getCols();
					}
					status = collision(b, bounds);
				}
				//down key & if collision is detected..
				//if there is a bound(s) that disables down movement press down, key once more, then freeze(!!!!!!!!))
				else if (ke.getCode().equals(KeyCode.DOWN) && freeze == false)
				{
					leftMoving = false;// <-resets these vars to default b/c this is only time that 'moving' really counts...
					rightMoving = false;

					status = collision(b, bounds);
					if(status == 5 || status == 4 || status == 6 || status == 7)
					{
						freeze = true;
						//start of new task this has to be here!!!
						Task<Void> sleeper = new Task<Void>() {
					        @Override
					        protected Void call() throws Exception {
					            try {
					                Thread.sleep(500);
					            } catch (InterruptedException e) {
					            	//I don't know what this does/ when it is called...
					            }
					            return null;
					        }
					    };

						// checking for side key pressed
						new AnimationTimer(){

							@Override
							public void handle(long now)
							{
								//if no side mvnt detected...
								if(leftMoving == false && rightMoving == false)
								{
									//start new task..
									new Thread(sleeper).start();
								}
								//if some side movement detected...
								else
								{
									sleeper.cancel();
									this.stop();
								}
							}
						}.start();//<- remember, this has to be at the end of new timer instance to work..

						//if task succeeds
						sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					        @Override
					        public void handle(WorkerStateEvent event) {
					        	gs.setBlock();//and then I need to reset the blocks...
					        	b.initialize();
					        	x = 0;
					        	y = 0;
					        	sleeper.cancel();
					        	inControl = false;
					        	freeze = false;
					        }
					    });
						//if task fails
						sleeper.setOnCancelled(new EventHandler<WorkerStateEvent>(){
							@Override
							public void handle(WorkerStateEvent event)
							{
					        	sleeper.cancel();
							}
						});
					}
					//down but  no collision
					else
					{
						y += verM;// going down is positive?
						for(int i = 0; i < 4; i++)
						{
							b.blocks[i].setY(b.blocks[i].getY() + verM);
							b.cloneR[i].setY(b.cloneR[i].getY() + verM);
						}
						b.updateClone();
						gs.getCols();
					}
				}


				//space key
				else if (ke.getCode().equals(KeyCode.SPACE) && freeze == false && b.visible == true)
				{
					for (int i = 0; i < 4; i++)
					{
						//(!)creating robot for pressing keys on its own
						try {
							robot = new Robot();
							robot.keyPress(java.awt.event.KeyEvent.VK_DOWN);
							inControl = true;
						} catch (AWTException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						b.blocks[i].setY(b.cloneS[i].getY());
						//and then press down once while disabling any user control

					}
				}

				//rotate
				else if (ke.getCode().equals(KeyCode.Z) && freeze == false)
				{
					//if canRotate, then rotate and move cloneS according to it...
					if(canRotate(b,bounds))
					{
						b.rotate();
						gs.getCols();
					}
				}

				//pause
				else if (ke.getCode().equals(KeyCode.P))
				{
					//if game is not paused
					if(currentState != "pauseGame")// and not gameOver only when game is running...
					{
						paused.setVisible(true);
						//stop any control and stop level()
						freeze = true;
						inControl = true;
						currentState = "pauseGame";
					}
					else
					{
						paused.setVisible(false);
						freeze = false;
						inControl = false;
						currentState = "runGame";
					}

				}
			}
		});b.blocks[0].setFocusTraversable(true);
	}

	//******************************************************
	// collision check
	//******************************************************
	/* NOTES:
	 * (!) i think this should be joined with control b/c its not gonna run multiple times.. or in timer itself
	 * later there should be a doppleganger(?) sensors for rotation
	 */
	int collision(Blocks b,Vector<ImageView> bounds)
	{
		//for all blocks check each of the bounds
		double centerX = 0;
		double centerY = 0;
		int width = 8;
		int height = 8;
		int offset = 16;
		int left = -1,right = 1,down = 5;//<- these #s will create 7 possible state that block's can be in for boundary..
		int total = 0;
		boolean left_or_right = false;//<- this is for checking if either left or right has been bounded b/c l&d&r == d\
		boolean leftCalled = false;//<- this is for checking multiple bounds at once... I didn't thought this through...
		boolean downCalled = false;
		boolean rightCalled = false;

		int blockSize = 17;//<- block size in pixel

		for (int i = 0; i < 4; i++)
		{
			centerX = b.blocks[i].getX() + (blockSize/2);
			centerY = b.blocks[i].getY() + (blockSize/2);
			//for each bounds check for each box...
			//(!) there is a problem when blocks encounter multiple bounds it adds up all which would return 100 as default..
			for(int j = 0; j < bounds.size(); j++)
			{
				//left
				if(bounds.elementAt(j).intersects(centerX-offset, centerY, width, height) && leftCalled == false)
				{
					total += left;
					left_or_right = true;
					leftCalled = true;
				}

				//down
				if(bounds.elementAt(j).intersects(centerX, centerY+offset, width, height) && downCalled == false)
				{
					total += down;
					downCalled = true;
				}

				//right
				if(bounds.elementAt(j).intersects(centerX+offset, centerY, width, height) && rightCalled == false)
				{
					total += right;
					left_or_right = true;
					rightCalled = true;
				}
			}
		}

		// to distinguish between left&right/total(default)
		if(total == 0 && left_or_right == false)
			return 100;
		else if(left_or_right && total==5)
		{
			return 7;// return special number 7 for L&D&R
		}
		else
			return total;// return as is
	}
	//******************************************************
	// canRotate
	//******************************************************
	/* NOTES:
	 * check if rotation is possible if not, return false if can, return true
	 */
	boolean canRotate(Blocks b, Vector<ImageView> bounds)
	{
		//get all the cloneRs' and bounds', check if they intersect if so then send false...
		int allFour = 0;

		for(int i = 0; i < 4; i++)
		{
			boolean ifAny = false;

			for(int j = 0; j < bounds.size(); j++)
			{
				if(b.cloneR[i].getBoundsInLocal().intersects(bounds.elementAt(j).getBoundsInLocal()))
				{
					ifAny = true;
					break;
				}
			}
			//if any of the intersection occurred, then add one to Four
			if(!ifAny)
			{
				allFour++;
			}
		}
		if(allFour == 4)
			return true;
		else
			return false;
	}

	//******************************************************
	// main()
	//******************************************************
	public static void main(String[] args) {
		launch(args);
	}
}//***end of Main Class
