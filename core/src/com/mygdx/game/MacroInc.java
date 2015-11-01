package com.mygdx.game;
//This is the main container for screens.
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
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		
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
