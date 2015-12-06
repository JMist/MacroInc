package com.mygdx.game;
//This is the main container for screens.
//Test change to try to get on Timmy's Computer.
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MacroInc extends Game {
	SpriteBatch batch;
	
	BitmapFont dialogFont;
	BitmapFont normalFont;
	
	BitmapFont font;
	final static float 	TEXT_DELAY = .06f;
	FileHandle save;
	final static float[] FONT_COLOR = {.3f, .3f, .3f};
	
	float fadeState;
	boolean isFadeOut;
	boolean isFadeIn;
	
	Screen toFollow;
	
	public Vector2 screenTransform(Vector2 v)
	{
		return new Vector2(v.x, 480 - v.y);
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		dialogFont = new BitmapFont();
		dialogFont.setColor(0, 0, 0, 1);
		dialogFont.getData().setScale(2f);
		
		normalFont = new BitmapFont();
		normalFont.setColor(FONT_COLOR[0], FONT_COLOR[1], FONT_COLOR[2], 1);
		
		font.setColor(FONT_COLOR[0], FONT_COLOR[1], FONT_COLOR[2], 1);
		this.setScreen(new TitleScreen(this));
			save = Gdx.files.local("playerdata.txt");
			System.out.println(save.path());
	}

	@Override
	public void render () {		
		super.render();		
	}
	
	public void dispose() {
		font.dispose();
		batch.dispose();
	}
	
	//Only works during batch
	//@var f = time into the fade out.
	//returns true if finished.
	public boolean fadeOut(float f)
	{
		
		if(batch.isDrawing())
		{
			fadeState += f;
			if(fadeState < 2)
			batch.draw(new Texture(Gdx.files.internal("fade.png")),(int)( -1000+500*fadeState), 0);
			else
			{
				batch.draw(new Texture(Gdx.files.internal("fade.png")),(int)( -1000+500*fadeState), 0);			
				isFadeOut = false;
				System.out.println(fadeState);
				return true;
			}
		}
		return false;
	}
	
	public boolean fadeIn(float f)
	{
		
		if(batch.isDrawing())
		{
			fadeState += f;
			if(fadeState < 2)
			batch.draw(new Texture(Gdx.files.internal("fade.png")),(int)(500*fadeState), 0);
			else
			{
				batch.draw(new Texture(Gdx.files.internal("fade.png")),(int)(500*fadeState), 0);			
				isFadeIn = false;
				return true;
			}
		}
		return false;
	}
	
	public void startFadeOut()
	{	
		fadeState = 0;
		isFadeOut = true;
		//play sound?
	}
	
	public void startFadeIn()
	{	fadeState = 0;
		isFadeIn = true;
		//Play sound?
	}
	
	public void startFadeOut(Screen t)
	{	
		toFollow = t;
		fadeState = 0;
		isFadeOut = true;
		//play sound?
	}
	
	public void startFadeIn(Screen t)
	{	toFollow = t;
		fadeState = 0;
		isFadeIn = true;
		//Play sound?
	}
}
