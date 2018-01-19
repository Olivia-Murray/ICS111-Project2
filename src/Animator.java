import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class Animator {

	public static void main(String[] args) throws IOException, InterruptedException{
		//Setup EZ graphics system
		EZ.initialize(1800, 1000);
		
		//Set background color to black
		EZ.setBackgroundColor(new Color(200,200,200)); 
		
		EZ.addImage("HalloweenScene.png", 1200, 700);
		
				//Number of actors
				int numactors = 3;
				//Array for actors with each actor assigned to a slot
				Actor[] actors = new Actor[3];
				actors[0] = new Actor("witch_1.png", 2400, 700, "WitchScript.txt");
				actors[1] = new Actor("ghost_1.png", 2400, 700, "GhostScript.txt");
				actors[2] = new Actor("cat_1.png", -600, 700, "CatScript.txt");
				
				boolean exit = false;
				//will continue until exit is true
				while (exit!=true){
					//Read and execute each script file for each actor
					
					if (exit == false){
					actors[0].FileRead();
					
					if (exit == false){
					actors[1].FileRead();
					
					if (exit == false){
					actors[2].FileRead();
					}
					}
					}
					exit = true;
				}
	}
}
		//Side note: There are so many files as I was going to make more animations but I ran out of time