import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
import java.util.Date;

public class Actor {
	
	private EZImage Actor;			// Member variable to store actor picture
	private int x, y, startx, starty;	// Member variables to store x, y, startx, starty
	private int destx, desty;			// Member variables to store destination values
	private int Aposx, Aposy;			// Member variables to store Actor coordinates
	private int Arotation;
	private int currentAngle = 0;		// Member variable to store current angle
	private int desAngle;				// Member variable to store destination angle value
	private int startAngle;				// Member variable to store start angle value
	private long starttime;				// Member variable to store the start time
	private long rstarttime;			// Member variable to store the rotation start time
	private long duration;				// Member variable to store the duration
	private long rduration;				// Member variable to store the rotation duration
	private boolean interpolation;		// Member variable that is set to true if it is in the interpolation state
	private boolean interpolationRotate;// Member variable that is set to true if it is in the interpolation state for rotation
	private String ScriptName;			// String that stores name of script file

	
	// Make an InputStreamReader object which reads from the keyboard.
	// Make a Scanner object that uses the InputStreamReader
	Scanner scanner = new Scanner(new InputStreamReader(System.in));
	
	//Set actor and starting position
	public Actor(String filename, int posx, int posy, String Scriptname){
		//Variable x is the x position of actor
		x = posx;
		
		//Variable y is the y position of actor
		y = posy;
		
		//Call the file of the actor picture
		Actor = EZ.addImage(filename, posx, posy);
		
		//Set interpolation to true
		interpolation = true;
		
		//store script file name
		ScriptName = Scriptname;

	}
	public int posx(){
		Aposx = Actor.getXCenter();
		return Aposx;
	}
	
	public int posy(){
		Aposy = Actor.getYCenter();
		return Aposy;
	}
	
	public double getRotation(){
		Arotation = (int) Actor.getRotation();
		return Arotation;
	}
	
	// Set the destination by giving it 3 variables
	// Dur means duration and is measured in seconds
	public void MOVETO(int posx, int posy, long dur){
		
		// Set destination position and duration
		// Convert seconds to miliseconds
		destx = posx; desty = posy; duration = dur*1000;
		
		// Get the starting time (i.e. time according to your computer)
		starttime = System.currentTimeMillis();
		
		// Set the starting position of actor
		startx=x; starty=y;
		
		// Set interpolation mode to true
		interpolation = true;
	}
	
			
	// If you're in interpolation state then return true, else false.
	public boolean moving() {
	return interpolation;
	}
	
	//Set the rotate angle and duration
	public void TURNTO(int a, long dur){
		//Set rotate angle
		desAngle = a;
		
		//Convert seconds to miliseconds
		rduration = dur*1000;
		
		//Get the starting time
		rstarttime = System.currentTimeMillis();
		
		//Set startAngle to currentAngle
		startAngle = currentAngle;
		
		//Set interpolation mode to true
		interpolationRotate = true;
	}
	
	// If you're in interpolation state then return true, else false.
	public boolean rotating(){
		return interpolationRotate;
	}
	
	public void go(){
		
		// If interpolation mode is true then do interpolation
		if (interpolation == true) {
			
			// Normalize the time (i.e. make it a number from 0 to 1)
			float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
			
			// Calculate the interpolated position of the actor
			x = (int) (startx + ((float) (destx - startx) *  normTime));
			y = (int) (starty + ((float) (desty - starty) *  normTime));
			
			// If the difference between current time and start time has exceeded the duration
			// then the animation/interpolation is over.
			if ((System.currentTimeMillis() - starttime) >= duration) {
				
				// Set interpolation to false
				interpolation = false;
				
				// Move the actor all the way to the destination
				x = destx; y = desty;
			}
			// Don't forget to move the image itself.
			Actor.translateTo(x,y);	
		}
	
		// If interpolation mode is true then do interpolation
		if (interpolationRotate == true) {
			
			// Normalize the time (i.e. make it a number from 0 to 1)
			float rnormTime = (float) (System.currentTimeMillis() - rstarttime)/ (float) rduration;
			
			// Calculate the interpolated position of the bug
			currentAngle = (int) (startAngle + ((float) (desAngle - startAngle) *  rnormTime));
			
			
			// If the difference between current time and start time has exceeded the duration
			// then the animation/interpolation is over.
			if ((System.currentTimeMillis() - rstarttime) >= rduration) {
				
				// Set interpolation to false
				interpolationRotate = false;
				
				// Move the actor all the way to the destination
				currentAngle = desAngle;
			}
			// Don't forget to move the image itself.
			Actor.rotateTo(currentAngle);	
		}
	}
	
	//Function for file reader to read script
	public void FileRead() throws IOException, InterruptedException{
		
		//set scanner to read script file designated to actor
		Scanner s = new Scanner(new FileReader(ScriptName));
		
		//while there is a next string the while loop will keep going
		boolean exit = !s.hasNext();
			
		while (exit!=true){
			//if there is no next string, the while loop will exit
			if (!s.hasNext()){
				exit = true;
			}else{
				String command = s.next();
				
				switch (command){
				
				//If Move, it will move to coordinates in designated time
				case "Move":
					int posX = s.nextInt();
					int posY = s.nextInt();
					int mdur = s.nextInt();
					MOVETO(posX, posY, mdur);
					//This makes distance between each action
					synchronized (Actor){
						if(moving() && rotating()){
							Actor.wait();
						}
					}
					break;
				
				//If Rotate, it will turn to the desired angle in designated time
				case "Rotate":
					int angle = s.nextInt();
					int rdur = s.nextInt();
					TURNTO(angle, rdur);
					break;
				
				//If Play, will play sound file
				case "Play":
					String soundFile = s.next();
					EZSound sound = EZ.addSound(soundFile);
					sound.play();
				}
			}
			while(moving() || rotating()){
				// Call the go member function of the actor to make it move.
				go();
				// Refresh the screen
				EZ.refreshScreen();
			}
		}
		s.close();
	}

}
