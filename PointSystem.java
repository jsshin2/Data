package application;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class PointSystem {
	
	//member variables..
	static private int level = 1;
	static private int score = 0;
	static private int poppedRow = 0;
	//level
	static Label lvl_lbl = new Label();
	//score
	static Label score_lbl = new Label();
	//next
	static Label next_lbl = new Label("next:");
	
	//***************************************
	//constructor
	//***************************************
	public PointSystem(Pane root)
	{
		//no need for this?
		lvl_lbl.setFont(Font.font("System",FontWeight.NORMAL,FontPosture.REGULAR,18));
		lvl_lbl.setTextFill(Color.web("#ededed"));
		lvl_lbl.setLayoutX(390);
		lvl_lbl.setLayoutY(25);
		lvl_lbl.setText("level: " + String.valueOf(getLevel()));
		root.getChildren().add(lvl_lbl);
		score_lbl.setFont(Font.font("System",FontWeight.NORMAL,FontPosture.REGULAR,18));
		score_lbl.setTextFill(Color.web("#ededed"));
		score_lbl.setLayoutX(390);
		score_lbl.setLayoutY(70);
		score_lbl.setText("score: " + String.valueOf(getScore()));
		root.getChildren().add(score_lbl);
		next_lbl.setFont(Font.font("System",FontWeight.NORMAL,FontPosture.REGULAR,18));
		next_lbl.setTextFill(Color.web("#ededed"));
		next_lbl.setLayoutX(390);
		next_lbl.setLayoutY(120);
		root.getChildren().add(next_lbl);
	}
	//***************************************
	//setLevel
	//***************************************
	/* NOTES:
	 * checks for #of rows popped and level up accordingly..
	 * # 0f rows to pop to level up = (level * 5)
	 */
	static void setLevel()
	{
		if( poppedRow == (5*level))
			level++;
	}
	//***************************************
	//getLevel
	//***************************************
	/* NOTES:
	 */
	static int getLevel()
	{
		return level;
	}
	//***************************************
	//setScore
	//***************************************
	/* NOTES:
	 */
	static void setScore()
	{
		score = poppedRow*100;
	}
	//***************************************
	//getScore
	//***************************************
	/* NOTES:
	 */
	static int getScore()
	{
		return score;
	}
	//***************************************
	// popRow
	//***************************************
	/* NOTES:
	 */
	static void popRow()
	{
		poppedRow++;
		update();
	}
	
	//***************************************
	// Update
	//***************************************
	/* NOTES:
	 */
	static void update()
	{
		setLevel();
		setScore();
		lvl_lbl.setText("level: " + String.valueOf(getLevel()));
		score_lbl.setText("score: " + String.valueOf(getScore()));
	}
	//***************************************
	// resetAll
	//***************************************
	/* NOTES:
	 */
	static void resetAll()
	{
		level = 1;
		score = 0;
		poppedRow = 0;
		lvl_lbl.setText("level: " + String.valueOf(getLevel()));
		score_lbl.setText("score: " + String.valueOf(getScore()));
	}
	

}
