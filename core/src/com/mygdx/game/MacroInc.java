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

public class MacroInc extends Game {
	SpriteBatch batch;
	
	BitmapFont font;
	
	final static float[] FONT_COLOR = {.3f, .3f, .3f};
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(FONT_COLOR[0], FONT_COLOR[1], FONT_COLOR[2], 1);
		this.setScreen(new TitleScreen(this));
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
