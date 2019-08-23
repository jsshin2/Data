package application;

import java.awt.AWTException;
import java.awt.Robot;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class UI {
	//member variables
	private Pane root;
	Robot robot;
	Blocks b;
	GridSystem gs;
	
	//startButton & images
	Image startClicked = new Image("resources/images/startButton_on.png");
	Image startReleased = new Image("resources/images/startButton_off.png");
	Image startHovered = new Image("resources/images/startButton_hover.png");
	Image startDisabled = new Image("resources/images/startButton_disabled.png");
	ImageView startButton = new ImageView(startReleased);
	
	//helpScreen & images
	Image helpClicked = new Image("resources/images/helpButton_on.png");
	Image helpReleased = new Image("resources/images/helpButton_off.png");
	Image helpHovered = new Image("resources/images/helpButton_hover.png");
	Image helpDisabled = new Image("resources/images/helpButton_disabled.png");
	ImageView helpScreen = new ImageView("resources/images/helpScreen.png");
	
	//scoreButton & images
	Image scoreClicked = new Image("resources/images/scoreButton_on.png");
	Image scoreReleased = new Image("resources/images/scoreButton_off.png");
	Image scoreHovered = new Image("resources/images/scoreButton_hover.png");
	Image scoreDisabled = new Image("resources/images/scoreButton_disabled.png");
	ImageView scoreButton = new ImageView(scoreReleased);
	ImageView scoreBoard = new ImageView("resources/images/scoreBoard.png");
	
	//quitButton & images
	Image quitClicked = new Image("resources/images/quitButton_on.png");
	Image quitReleased = new Image("resources/images/quitButton_off.png");
	Image quitHovered = new Image("resources/images/quitButton_hover.png");
	ImageView quitButton = new ImageView(quitReleased);
			

	//empty constructor
	public UI(Pane root, Blocks b, GridSystem gs)
	{
		this.root = root;
		this.b = b;
		this.gs = gs;
		musicButton();
		quitButton();
		startButton();
		helpButton();
		scoreButton();		
	}
	//for playing music
	void musicButton()
	{
		String path = this.getClass().getResource("/resources/musics/melodyphony.mp3").toString();
		Media music = new Media(path);
		MediaPlayer player = new MediaPlayer(music);
		player.setVolume(1.0);
		Image musicClicked = new Image("resources/images/musicButton_on.png");
		Image musicReleased = new Image("resources/images/musicButton_off.png");
		Image musicHovered = new Image("resources/images/musicButton_hover.png");
		
		//musicButton
		ImageView musicButton = new ImageView(musicReleased);
		musicButton.setSmooth(false);
		musicButton.setX(395);
		musicButton.setY(433);
		root.getChildren().add(musicButton);
		
		//music label
		Label music_lbl = new Label("music: off");
		music_lbl.setFont(Font.font("System",FontWeight.NORMAL,FontPosture.REGULAR,18));
		music_lbl.setTextFill(Color.web("#ededed"));
		music_lbl.setLayoutX(390);
		music_lbl.setLayoutY(320);
		root.getChildren().add(music_lbl);
		
		musicButton.setOnMousePressed(new EventHandler<MouseEvent>(){
			int musicPlaying = 0;
			public void handle(MouseEvent evt) {
	            
	            if(musicPlaying == 1)
	            {
	            	player.pause();
	            	musicButton.setImage(musicReleased);
	            	music_lbl.setText("music: off");
	            	musicPlaying = 0;
	            }
	            else 
	            {
	            	player.play();
	            	musicButton.setImage(musicClicked);
	            	music_lbl.setText("music: on");
	            	musicPlaying = 1;
	            }
	        }
		});
		musicButton.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            musicButton.setImage(musicHovered);
	        }
		});
		musicButton.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            musicButton.setImage(musicReleased);
	        }
		});
	}
	//for starting
	void quitButton()
	{
		quitButton.setSmooth(false);
		quitButton.setX(463);
		quitButton.setY(433);
		root.getChildren().add(quitButton);
		
		quitButton.setOnMousePressed(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent evt) {
				quitButton.setImage(quitClicked);

	        }
		});
		quitButton.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            quitButton.setImage(quitReleased);
	            System.exit(0);
	        }
		});
		quitButton.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            quitButton.setImage(quitHovered);
	        }
		});
		quitButton.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            quitButton.setImage(quitReleased);
	        }
		});
	}
	//for start
	void startButton()
	{
		startButton.setSmooth(false);
		startButton.setX(19);
		startButton.setY(116);
		root.getChildren().add(startButton);
		
		startButton.setOnMousePressed(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
					
				{
					startButton.setImage(startClicked);
					
				}
			}
		});
		startButton.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
				{
					//set blocks invisible when gameOver
					for(int i = 0; i < 4; i++)
					{
						b.blocks[i].setVisible(true);
					}
					startButton.setImage(startReleased);
					Main.currentState = "runGame";
					Main.freeze = false;
					Main.inControl = false;
					Main.gameOver.setVisible(false);
					level(b, gs);
					scoreButton.setImage(scoreDisabled);
					startButton.setImage(startDisabled);
				}
	        }
		});
		startButton.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
					startButton.setImage(startHovered);
	        }
		});
		startButton.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
					startButton.setImage(startReleased);
	        }
		});
	}
	//for help
	void helpButton()
	{
		helpScreen.setX(100);
		helpScreen.setY(100);
		helpScreen.setVisible(false);
		root.getChildren().add(helpScreen);
		
		// [x] for turn off the helpScreen
		ImageView xButton = new ImageView("resources/images/xButton.png");
		xButton.setX(422);
		xButton.setY(108);
		xButton.setVisible(false);
		root.getChildren().add(xButton);

		//helpButton
		ImageView helpButton = new ImageView(helpReleased);
		helpButton.setSmooth(false);
		helpButton.setX(19);
		helpButton.setY(158);
		root.getChildren().add(helpButton);
		
		helpButton.setOnMousePressed(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent evt) {
				helpButton.setImage(helpClicked);
				
				helpScreen.setVisible(true);
				xButton.setVisible(true);
				
				if(Main.currentState == "runGame")
				{
					try {
						robot = new Robot();
						robot.keyPress(java.awt.event.KeyEvent.VK_P);
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }
		});
		helpButton.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            helpButton.setImage(helpReleased);
	        }
		});
		helpButton.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            helpButton.setImage(helpHovered);
	        }
		});
		helpButton.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            helpButton.setImage(helpReleased);
	        }
		});
		xButton.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            helpScreen.setVisible(false);
	            xButton.setVisible(false);
	        }
		});
		
	}
	//for score
	void scoreButton()
	{
		scoreButton.setSmooth(false);
		scoreButton.setX(19);
		scoreButton.setY(203);
		root.getChildren().add(scoreButton);
		
		//for scoreBoard
		scoreBoard.setVisible(false);
		root.getChildren().add(scoreBoard);
		
		// for back button
		ImageView backButton = new ImageView("resources/images/backButton.png");
		backButton.setX(500);
		backButton.setY(466);
		backButton.setVisible(false);
		root.getChildren().add(backButton);
		
		scoreButton.setOnMousePressed(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
					scoreButton.setImage(scoreClicked);
	        }
		});
		scoreButton.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
				{
					scoreButton.setImage(scoreReleased);
					//show scoardBoard
					scoreBoard.setVisible(true);
					ScoreSystem.readFromFile();
					ScoreSystem.labelSwitch();
					backButton.setVisible(true);
				}	
	        }
		});
		scoreButton.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
					scoreButton.setImage(scoreHovered);
	        }
		});
		scoreButton.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
				if(Main.currentState == "gameOver")
					scoreButton.setImage(scoreReleased);
	        }
		});
		backButton.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent evt) {
	            scoreBoard.setVisible(false);
	            ScoreSystem.labelSwitch();
	            backButton.setVisible(false);
	        }
		});
	}
	
	//******************************************************
	// level (!!!)
	//******************************************************
	/* NOTES:
	 * make the blocks go down with robot and timer
	 */
	void level(Blocks b, GridSystem gs)
	{
		new AnimationTimer() {
			long updateNow = 0;
			long speed;
			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub
				if(PointSystem.getLevel() < 15)
					speed = PointSystem.getLevel() * 65_000_000;
				else
					speed = 975_000_000;
					
				if(now - updateNow > 1065_000_000 - speed )//<-for testing purposes
				{
					updateNow = now;
					if(Main.currentState == "runGame")
					{
						try {
							robot = new Robot();
							robot.keyPress(java.awt.event.KeyEvent.VK_DOWN);
						} catch (AWTException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//(!) nextGame? uses up to much memory, and cpu!
					else if(Main.currentState == "gameOver")
					{
						//reset freeze and inControl
						Main.freeze = true;
						Main.inControl =true;
						//reset bounds(!) had to remove from the top to index[3] for env. bounds...
						for(int i = Main.bounds.size() - 1; i >= 3; i--)
							Main.bounds.removeElementAt(i);
						//reset Blocks
						b.reset();
						//reset GridSystem
						gs.reset();
						//reset UI
						reset();
						//reset PointSystem
						PointSystem.resetAll();
						//show gameover image
						Main.gameOver.setVisible(true);
						//set blocks invisible when gameOver
						for(int i = 0; i < 4; i++)
						{
							b.blocks[i].setVisible(false);
						}
						this.stop();
						return;//to get out of the loop
					}
				}
				//for hiding blocks when it is above game area!!!
				Image tmp = b.getColor();
				for(int i = 0; i < 4; i++)
				{
					if(b.blocks[i].getBoundsInLocal().intersects(Main.top.getBoundsInLocal()))
					{
						b.blocks[i].setImage(Main.empty);
					}
					else
					{
						b.blocks[i].setImage(tmp);
						for(int j = 0; j < 4; j++)
						{
							b.cloneS[j].setVisible(true);
						}
						b.visible = true;
					}
				}
			}
		}.start();
	}

	//*******************************************
	//reset
	//*******************************************
	/* NOTES:
	 * 
	 */
	void reset()
	{
		startButton.setImage(startReleased);
		scoreButton.setImage(scoreReleased);
	}
};
