package main;

import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class Game {
	
	public static int iTargetSpeed;												//Target speed
	public static int iTargetSize;												//Target size
	
	private int clicks = 0;														//number of times the user has clicked the mouse
	private int hits = 0;														//number of times the user has hit the target
	
	static boolean bEnableTeleport = false;										//enable teleporting or not. off by default
	static boolean bEnableRecoil = false;										//enable recoil or not. off by default
	
	private boolean fireHasBeenPressed;											//these variables are used in a way that
	private boolean fireHasBeenReleased = true;									//the user cannot hold the fire button down
	private boolean fireAtTarget = false;
	
	public static float score = 0;												//user score

	private long lastLoopTime = getTime();										//these variables are used to handle time based events
	private static long timerTicksPerSecond = Sys.getTimerResolution();			//such as moving the target
	long timeStart;																//and calculating remaining time
	int timeSeconds = 0;
	int timeRemaining = 60;	
	long delta;
	
	TargetEntity targetEntity = new TargetEntity(400, 300);						//declare and initialise the target
	
	TrueTypeFont font1;
	
	public void start(int iTargetSpeedInput, int iTargetSizeInput, boolean bEnableTeleportInput, boolean bEnableRecoilInput)
	{
		/*
		 * we receive the values from the frames and put them to use in-game
		 * first we cap the speed and size
		 * then set all our variables declared earlier to those provided here
		 * finally we start the gameLoop function
		 */
		
		//System.out.println("Game Start command recieved");
		//System.out.println("iTargetSpeedInput = " + iTargetSpeedInput + ", iTargetSizeInput = " + iTargetSizeInput);
		//System.out.println("bEnableTeleportInput = " + bEnableTeleportInput + ", bEnableRecoilInput = " + bEnableRecoilInput);
		
		if(iTargetSpeedInput > 1000)
		{			
			iTargetSpeedInput = 1000;			
		}		
		if(iTargetSizeInput > 100)
		{			
			iTargetSizeInput = 100;			
		}		
		iTargetSpeed = iTargetSpeedInput;
		iTargetSize = iTargetSizeInput;		
		bEnableTeleport = bEnableTeleportInput;
		bEnableRecoil = bEnableRecoilInput;		
		gameLoop();
	}
	
	public void newEntity()
	{		
		targetEntity.dx = iTargetSpeed;
		targetEntity.dy = iTargetSpeed;		
		targetEntity.size = iTargetSize;		
	}
	
	public void gameLoop()
	{		
		//System.out.println("Loading game loop");		
		try
		{			
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();			
		} catch(LWJGLException e)
		{			
			e.printStackTrace();
			System.exit(0);			
		}		
		
		initOpenGL();
		initFonts();
		newEntity();
		timeStart = System.currentTimeMillis();									//we need to know the time the game starts
		//System.out.println("Starting main game loop")		
		while (!Display.isCloseRequested())
		{			
			long timeDuration = System.currentTimeMillis() - timeStart;			//the total elapsed milliseconds
			timeSeconds = (int) (timeDuration / 1000);							//convert to seconds
			timeRemaining = 60 - (int) timeDuration / 1000;						//seconds of time remaining
			delta = getTime() - lastLoopTime;									//used to move objects
			lastLoopTime = getTime();											//gets the time, we use this the next loop
			
			pollInput();														//check for input
			
			if(fireHasBeenPressed && fireAtTarget && timeRemaining > 0)
			{	
				/*
				 * if the fire button has been pressed AND we can fire AND we haven't hit the time limit
				 * call the isClickOnMe function, passing through mouse locations
				 * finally, disable the ability to fire. this will be re-enabled when the user releases the fire button
				*/
				
				targetEntity.isClickOnMe(Mouse.getX(), Mouse.getY());
				fireAtTarget = false;			
			}
			
			/*
			 * this is used for recoil
			 * if the size is smaller than our value for it...
			 * ...increase it slightly over time
			 */
			
			if(targetEntity.size < iTargetSize)
			{				
				targetEntity.size = targetEntity.size + delta * 0.01;				
			}
			
			//each loop, get the value for hits and clicks, stored in the target entity
			clicks = (int) targetEntity.getClicks();
			hits = (int) targetEntity.getHits();
			
			Display.update();
			Display.sync(60);
			drawQuads();														//simply draws the background
			
			/*
			 * if there is still time remaining...
			 * ...move the target by delta
			 * and draw the target
			 */
			
			if(timeRemaining > 0)
			{			
				targetEntity.move(delta);
				targetEntity.draw();			
			}		
			
			drawText();															//draws text on the screen while the game loop is active
			
			//if we have passed the time limit and the user presses space, break out of the game loop
			if(timeRemaining < 0 && Keyboard.isKeyDown(57))
			{
				break;
			}			
		}
		
		Display.destroy();
		Frames.scoreBox(score, hits, clicks);									//pass the values to be displayed to the user
		score = 0;																//reset them to 0 for the next round, if the user chooses
		hits = 0;
		clicks = 0;
	}
	
	public void initOpenGL()
	{
		//System.out.println("Initializing OpenGL");
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);		
	}
	
	public void drawQuads()
	{		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor3f(1.0f,1.0f,1.0f);		
		
		GL11.glBegin(GL11.GL_QUADS);		
	    	GL11.glVertex2f(0,0);
	    	GL11.glVertex2f(800,0);
	    	GL11.glVertex2f(800,600);
	    	GL11.glVertex2f(0,600);
	    GL11.glEnd();		
	}
	
	public static long getTime()
	{		
		return(Sys.getTime() * 1000 / timerTicksPerSecond);		
	}
	
	public void initFonts()
	{		
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
		font1 = new TrueTypeFont(awtFont, false);
		
		//System.out.println("Font loaded");
	}
	
	public void drawText()
	{		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(timeRemaining > 0)
		{		
			//font1.drawString(25, 25, "Clicks: " + clicks, Color.black);
			//font1.drawString(25, 50, "Hits: " + hits, Color.black);
			//font1.drawString(25, 75, "Mouse is at x: " + Mouse.getX() + ", Y: " + Mouse.getY(), Color.black);
			//font1.drawString(25, 100, "Score: " + score, Color.black);
			//font1.drawString(25, 125, "Elapsed Time: " + timeSeconds, Color.black);
			
			if(timeRemaining > 10)
			{
				font1.drawString(25, 25, "Time Remaining: " + timeRemaining, Color.black);
			} else
			{				
				font1.drawString(25, 25, "Time Remaining: " + timeRemaining, Color.red);				
			}
		} else 
		{			
			font1.drawString(200, 300, "Game Over. Press space to continue", Color.black);			
		}
				
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void pollInput()
	{		
		/*
		 * if the left mouse button is down AND we know it has been released...
		 * ...we have pressed fire
		 *...we have fired
		 *...fire has not been released
		 *
		 *these two if statements allow semi-auto fire only
		 */
		if(Mouse.isButtonDown(0) && fireHasBeenReleased)
		{			
			fireHasBeenPressed = true;
			fireAtTarget = true;
			fireHasBeenReleased = false;
						
			//System.out.println("Mouse down at X: " + Mouse.getX() + " Y: " + Mouse.getY());
			//System.out.println(Sys.getTime());										
		}
		/*
		 * if left mouse button is down AND fire has been pressed...
		 * ...fire has been released
		 *...fire has not been pressed
		 */
		if(!Mouse.isButtonDown(0) && fireHasBeenPressed)
		{			
			fireHasBeenReleased = true;
			fireHasBeenPressed = false;			
		}
		
		//if the user wanted to simulate recoil, call the recoil function while the fire button is down
		if(Mouse.isButtonDown(0) && bEnableRecoil)
		{			
			recoil();			
		}		
	}
	
	public void recoil()
	{		
		/*
		 * each loop, if the user has the fire button down, this is called
		 * if the size is greater than 1 (we are capping it)
		 * reduce the size by 2
		 */
		if(targetEntity.size > 1)
		{		
			targetEntity.size -= 2;
			//System.out.println(targetEntity.size)		
		}		
	}
}
