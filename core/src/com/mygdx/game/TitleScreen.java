package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;



public class TitleScreen implements Screen{

	final MacroInc game;
	
	OrthographicCamera camera;
	
	private Texture background;
	
	
	public TitleScreen(final MacroInc g)
	{
		game = g;
		
		//This sets up the camera.
		camera = new OrthographicCamera();
		//To have this be the actual dimensions,
		//you must go into DesktopLauncher.java and edit.
		camera.setToOrtho(false, 1000, 480);
		
		background = new Texture(Gdx.files.internal("titlescreen.png"));
	}
	public void render(float delta)
	{	
		//Clears screen to black
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //Sets up camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.font.setColor(1,0,1,1);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        if(TimeUtils.nanoTime()/1000000000 % 2 == 0)
        	game.font.draw(game.batch, "Click to Start", 400, 240);
        game.batch.end();
        if(Gdx.input.isTouched())
        	game.setScreen(new LevelSelectScreen(game));
	}
	public void pause()
	 {
		
	 }
	 public void dispose()
	 {
		 
	 }
	 public void resize(int x, int y)
	 {
		 
	 }
	 public void resume()
	 {
		 
	 }
	 public void hide()
	 {
		
	 }
	 public void show(){
		 
	 }
}