package application;

import java.io.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class ScoreSystem {
	
	static Label scoreList[][] = new Label[3][10];
	static private String name;
	static private int[] scores = new int[10];//hold score for ranking first = highest
	
	//**********************************************
	//Constructor
	//**********************************************
	public ScoreSystem(Pane root)
	{
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				scoreList[i][j] = new Label("something");
				scoreList[i][j].setFont(Font.font("System",FontWeight.BOLD,FontPosture.REGULAR,12));
				scoreList[i][j].setTextFill(Color.web("#bebebe"));
				//scoreList[i][j].setText("something");
				if(i < 2)
					scoreList[i][j].setLayoutX(90 + (i * 140));
				else
					scoreList[i][j].setLayoutX(80 + i * 140);
				scoreList[i][j].setLayoutY(100 + (j * 38));
				scoreList[i][j].setVisible(false);
				root.getChildren().add(scoreList[i][j]);
			}
		}
		//run readFromFile() here so that it will have some data to work with
		readFromFile();
	}
	//**********************************************
	//read from a file
	//**********************************************
	static void readFromFile()
	{
		String lines[] = new String[10];//first one first
		int counter = 0;
		//reading from a file line by line
		try (BufferedReader br = new BufferedReader(new FileReader("score.txt"))) {
		    String line; 
		    
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	lines[counter] = line;
		    	counter++;
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String words[] = new String[3];
		for(int i = 0; i < 10; i++)
		{
			words = split(lines[i]);
			for(int j = 0; j < 3; j++)
			{
				scoreList[j][i].setText(words[j]);
			}
			//save score info in scores??
			scores[i] = Integer.parseInt(words[1]);
		}
	}
	//**********************************************
	//writeToFile
	//**********************************************
	static void writeToFile()
	{
		//writing to a file line by line
		try {
			PrintWriter writer = new PrintWriter("score.txt", "UTF-8");
			
			//write updated lines to a file 
			for(int i = 0; i < 10; i++)
			{
				writer.println(formatting()[i]);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//***********************************************WORKING*********************************************//
	//**********************************************
	//ranking 
	//**********************************************
	/* NOTES:
	 * compare each line (top to bottom) with newScore rank where would it be
	 * if same replace get that row# and give it to formatting() which would then make th
	 */
	static int ranking(int newScore)
	{
		int rank = -1;
		//go through scores[] (first = highest) and get rank for newScore
		for(int i = 0; i < 10; i++)
		{
			//if newScore is lesser.. next
			if(scores[i] > newScore)
				continue;
			//if equal or greater
			else
			{
				rank = i;
				break;
			}
		}
		return rank;	
	}
	//**********************************************
	//formatting 
	//**********************************************
	/* NOTES:
	 * this should return all ten lines already sorting using ranking... to writeToFiles then write to files 
	 */
	static String[] formatting()
	{	
		//get rank form ranking, where should put it
		int rank = ranking(PointSystem.getScore());
		
		//lines will be stored here and return to writeToFile()
		String[] finalLines = new String[10];
		
		//for all lines
		for(int i = 0 ; i < 10; i++)
		{
			//if iteration is same as rank then replace with new score information...(!) this is wrong put new rank in its place and shift down scores...
			if(i == rank)
			{
				finalLines[i] = name+"|"+PointSystem.getScore() +"|"+ String.valueOf(LocalDate.now());
			}
			else
			{
				finalLines[i] = scoreList[0][i].getText() + "|" + scoreList[1][i].getText() + "|" + scoreList[2][i].getText();
			}
		}
		
		
		return finalLines;
	}
	//**********************************************
	//getName ->writeToFile
	//**********************************************
	static void getName()
	{
		//make a dialogue box
		String getName;
		getName = JOptionPane.showInputDialog("enter your name");
		name = getName;
		//this function should run writeToFile()
		writeToFile();
	}
	//**********************************************
	//split
	//**********************************************
	static String[] split(String line)
	{
		//make line into a array of Chars
		char charArray[] = line.toCharArray();
		
		//line will be splited into three words and put in here
		String threeWords[] = {"","",""};//make it initialize to this
		
		//delimiter
		char delimiter = '|';
		//for all char check for delimiter and make word
		
		//making one word at a time..
		int index = 0;
		for(int j = 0; j < 3;j++)
		{
			for(int i = index; i < charArray.length; i++)
			{
				//for the first one make it equal to threeWords[] itself b/c you can't concat otherwise
				if(charArray[i] != delimiter)
				{
					threeWords[j] += String.valueOf(charArray[i]);
					index++;
				}
					
				else{
					index++;
					break;
				}
			}
		}
		return threeWords;
	}   
	//**********************************************
	// labelSwitch
	//**********************************************
	//turn labels on and off
	static void labelSwitch()
	{
		if(scoreList[0][0].isVisible())
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 10; j++)
				{
					scoreList[i][j].setVisible(false);
				}
			}
		else
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 10; j++)
				{
					scoreList[i][j].setVisible(true);
				}
			}
	}
//	Alert a = new Alert(AlertType.INFORMATION);
//	a.setContentText(localDate.toString());
//	a.show();
}
