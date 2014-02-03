package main;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public abstract class Entity {
	
	protected float x;																	//x position
	protected float y;																	//y position
	
	protected double size;																//size
	
	protected float dx;																	//speed in x
	protected float dy;																	//speed in y
	
	private static final int MAX_RIGHT = 800;											//these are the maximum values for the x and y
	private static final int MAX_LEFT = 0;
	private static final int MAX_UP = 600;
	private static final int MAX_DOWN = 0;
	
	protected float clicks = 0;
	protected float hits = 0;
	
	Random randNumber = new Random();
	
	protected Entity(int x, int y)
	{		
		this.x = x;
		this.y = y;		
		//System.out.println("this.x = " + x);
		//System.out.println("this.y = " + y);		
	}
	
	public void move(long delta)
	{	
		//this function moved the entity, and makes sure it doesnt go out of bounds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
		
		if((dx < 0) && (x < MAX_LEFT))
		{			
			dx = -dx;			
		}
		
		if((dx > 0) && ((x + size) > MAX_RIGHT))
		{			
			dx = -dx;			
		}
		
		if((dy < 0) && (y < MAX_DOWN))
		{			
			dy = -dy;			
		}
		
		if((dy > 0) && ((y + size) > MAX_UP))
		{			
			dy = -dy;			
		}		
	}
	
	public float getClicks()
	{		
		return clicks;		
	}
	
	public float getHits()
	{		
		return hits;		
	}
	
	public void isClickOnMe(int mouseX, int mouseY)
	{	
		/*
		 * mouse values pass from the game class
		 * IF the click was inside the bounds of the target,
		 * increment the number of hits
		 * if the user opted to enable teleporting, set our x and y to a random location within the bounds of the screen
		 * 
		 * function call to increase the score
		 * else => we have not hit the target, so decrease the score
		 * 
		 * finally, increment the number of clicks
		 */
		mouseY = 600 - mouseY;
		if((mouseX < (x + size)) && (mouseX > (x - size)) && (mouseY < (y + size)) && (mouseY > (y - size)))
		{			
			hits++;
			//System.out.println("I am clicked");
			
			if(Game.bEnableTeleport)
			{				
				this.x = randNumber.nextFloat() * 800;
				this.y = randNumber.nextFloat() * 600;				
			}
			
			scoreInc();
			
		} else 
		{			
			scoreDec();			
		}
		
		clicks++;
		//System.out.println("mouseX = " + mouseX);
		//System.out.println("mouseY = " + mouseY);
		//System.out.println("size = " + size);
		//System.out.println("x = " + x);
		//System.out.println("y = " + y);		
	}
	
	public void draw()
	{	
		//this function draws the target on the screem
		GL11.glColor3f(0.0f,0.0f,0.0f);
		glPushMatrix();

		// translate to the right location and prepare to draw
		glTranslatef(x, y, 0);

		glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(-size, -size);
			GL11.glVertex2d(-size, size);
			GL11.glVertex2d(size, size);
			GL11.glVertex2d(size, -size);
		}
		glEnd();

		// restore the model view matrix to prevent contamination
		glPopMatrix();
	}
	
	public void scoreInc()
	{	
		/*
		 * this function increases the users score
		 * first we get the ratio of the default size to the current size
		 * then the ratio of hits to misses
		 * 
		 * score is increased by (ratio of speed to size) multiplied by the other 2 ratios
		 */
		float sizeRatio = (float) (Game.iTargetSize/size);
		float hitsRatio = (hits + 1)/(clicks + 1);
		float inc;
					
		inc = (Game.iTargetSpeed / Game.iTargetSize) * sizeRatio * hitsRatio;
		
		//System.out.println("sizeRatio = " + sizeRatio);
		//System.out.println("hitsRatio = " + hitsRatio);
		//System.out.println("hits = " + hits);
		
		//System.out.println(inc);
		Game.score = Game.score + inc;		
	}
	
	public void scoreDec()
	{		
		/*
		 * similar to score increases, except we multiply by 1 over the hits ratio
		 */
		
		float sizeRatio = (float) (Game.iTargetSize/size);
		float hitsRatio = (hits + 1)/(clicks + 1);
		float dec;
		
		dec = (Game.iTargetSpeed / Game.iTargetSize) * sizeRatio * (1/hitsRatio);
		
		Game.score = Game.score - dec;		
	}
}
