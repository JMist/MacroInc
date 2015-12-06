package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class LevelTwoEndScreen implements Screen{

	final MacroInc game;
	
	OrthographicCamera camera;
	
	private Texture background;
	BitmapFont f;
	BitmapFont f2;
	int profit;
	Button submitButton;
	
	
	public LevelTwoEndScreen(MacroInc g, int p)
	{
		 f = new BitmapFont();
        f.setColor(0,0,0,1);
        f.getData().setScale(4f);
        f2 = new BitmapFont();
        f2.setColor(0,0,0,1);
        f2.getData().setScale(4f);
		game = g;
		profit = p;
		background = new Texture(Gdx.files.internal("recipebackground.png"));
		submitButton = new Button(game, new Rectangle(), new Texture(Gdx.files.internal("readybutton.png")), new Texture(Gdx.files.internal("readybutton.png")));
		submitButton.setX(400);
		submitButton.setY(50);
		submitButton.setHeight(80);
		submitButton.setWidth(200);
	}
	
	public void render(float delta)
	{
		//Clears screen to black
				Gdx.gl.glClearColor(1, 1, 1, 0);
		        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		        
		        
		        game.batch.begin();
		        
		        game.batch.draw(background, 0, 0);
		        
		        f.draw(game.batch, "Total Profit:", 300, 380);
		        /*if(profit%100 < 10)
		        f2.draw(game.batch, "$" + profit/100 + ".0"+ profit%100, 300, 100);
		        else
		        	f2.draw(game.batch, "$" + profit/100 + "."+ profit%100, 300, 100);*/
		        
		        
		        if(Math.abs(profit)%100 < 10)
		        	if(profit >=0)
		        		f2.draw(game.batch, "$" + profit/100 + ".0" + profit%100, 300, 280);
		            else
		            {
		            	f2.setColor(1f, 0, 0, 1);
		            	f2.draw(game.batch, "-$" + (-profit)/100 + ".0" + (-profit)%100, 300, 280);
		            	f2.setColor(0, 0,0, 1);
		            }
		        else
		            if(profit >=0)
		                f2.draw(game.batch, "$" + profit/100 + "." + profit%100, 300, 280);
		            else
		                {
		                	f2.setColor(1f, 0, 0, 1);
		                	f2.draw(game.batch, "-$" + (-profit)/100 + "." + (-profit)%100, 300, 280);
		                	f2.setColor(0,0,0, 1);
		                }
		        submitButton.draw();
		        
		      //FADE IN OR OUT
		        if(game.isFadeIn)
		        {
		        	if(game.fadeIn(Gdx.graphics.getDeltaTime()))
		        	{
		        		
		        		}
		        }
		        if(game.isFadeOut)
		        {
		        	if(game.fadeOut(Gdx.graphics.getDeltaTime()))
		        	{
		        		game.setScreen(new LevelSelectScreen(game));
		        	}
		        }
		        
		        
		        
		        game.batch.end();
		        if(Gdx.input.justTouched())
		        {
		        	Vector2 touchPos = new Vector2();
		        	touchPos.set(Gdx.input.getX(), Gdx.input.getY());
		        	touchPos = game.screenTransform(touchPos);
		        	
		        if(submitButton.getLocation().contains(touchPos))
	        	{//Move on? setScreen(new Cutscene...
	        		game.startFadeOut();
	        		
	        	}
		        }
		        
	}
	//SCREEN INHERITED METHODS
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
