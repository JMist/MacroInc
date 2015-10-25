package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class macroInc extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	BitmapFont font;
	int x;
	int y;
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		font.draw(batch, "Last click at: ("+x+", "+y+")", x, 490 - y);
		batch.end();
		if (Gdx.input.isTouched()) {
			  x = Gdx.input.getX();
			  y = Gdx.input.getY();
			  System.out.println("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY());
			}
		
		
	}
}
