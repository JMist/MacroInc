package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
	private Texture			notPress;
	private Texture			press;
	private Rectangle			location;
	final MacroInc			game;
	public Button(MacroInc g, Rectangle loc, Texture open, Texture depressed)
	{
		press = depressed;
		notPress = open;
		
		location = loc;
		game = g;
	}
	public Button(MacroInc g)
	{
		location = new Rectangle();
		game = g;
	}
	//DRAW
	public void draw()
	{	Vector2 touchPos = new Vector2();
		
	//convert screen coordinates to real coordinates.
		if(Gdx.input.isTouched())
			{touchPos = game.screenTransform(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		if(location.contains(touchPos))
		game.batch.draw(press, location.x, location.y);
		else
			game.batch.draw(notPress, location.x, location.y);
		}
		else
			game.batch.draw(notPress, location.x, location.y);
		
	}
	
	
	//GETTERS AND SETTERS
		public void setX(float x)
		{
			location.x = x;
		}
		public void setY(float y)
		{
			location.y = y;
		}
		public void setWidth(float w)
		{
			location.width = w;
		}
		public void setHeight(float h)
		{
			location.height = h;
		}
		public void setPress(Texture img)
		{
			press = img;
		}
		public void setNotPress(Texture img)
		{
			notPress = img;
		}
		public Rectangle getLocation()
		{
			return location;
		}
		public float getX()
		{
			return location.x;
		}
		public float getY()
		{
			return location.y;
		}
		public float getWidth()
		{
			return location.width;
		}
		public float getHeight()
		{
			return location.height;
		}
		public Texture getPress()
		{
			return press;
		}
		public Texture getNotPress()
		{
			return notPress;
		}
		
		public void dispose()
		{
			press.dispose();
			notPress.dispose();
		}
}
