package com.mygdx.game;
//This is the main container for screens.
//Test change to try to get on Timmy's Computer.
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MacroInc extends Game {
	SpriteBatch batch;
	
	BitmapFont font;
	
	FileHandle save;
	final static float[] FONT_COLOR = {.3f, .3f, .3f};
	
	
	public Vector2 screenTransform(Vector2 v)
	{
		return new Vector2(v.x, 480 - v.y);
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
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
}
