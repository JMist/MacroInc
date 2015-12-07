package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;



public class TitleScreen implements Screen{

	final MacroInc game;
	
	OrthographicCamera camera;
	
	private Texture background;
	
	boolean openingScreen = true;
	boolean committingToNewGame = false;
	
	private Button newGameButton;
	private Button continueButton;
	
	final Texture newGameButtonImg = new Texture(Gdx.files.internal("newButton.png"));
	final Texture continueButtonImg = new Texture(Gdx.files.internal("continueButton.png"));
	public TitleScreen(final MacroInc g)
	{
		game = g;
		
		//This sets up the camera.
		camera = new OrthographicCamera();
		//To have this be the actual dimensions,
		//you must go into DesktopLauncher.java and edit.
		camera.setToOrtho(false, 1000, 480);
		background = new Texture(Gdx.files.internal("titlescreen.png"));
		
		newGameButton = new Button(game, new Rectangle(), newGameButtonImg, newGameButtonImg);
		continueButton = new Button(game, new Rectangle(), continueButtonImg, continueButtonImg);
		
		newGameButton.setX(200);
		newGameButton.setY(50);
		newGameButton.setWidth(200);
		newGameButton.setHeight(80);
		
		continueButton.setX(600);
		continueButton.setY(50);
		continueButton.setWidth(200);
		continueButton.setHeight(80);
	}
	public void render(float delta)
	{	
		//Clears screen to black
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //Sets up camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        if(openingScreen)
        {
        game.batch.draw(background, 0, 0);
        if(TimeUtils.nanoTime()/1000000000 % 2 == 0)
        	game.font.draw(game.batch, "Click to Start", 400, 240);
        }
        else
        {
        	FileHandle f = Gdx.files.local("playerdata.txt");
    		String text = f.readString();
        	newGameButton.draw();
        	if(!f.readString().equals("0"));
        	continueButton.draw();
        	if(Gdx.input.justTouched())
        	{	
        		
        		
        		
        		Vector2 touchPos = game.screenTransform(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        		//New game touched
        		if(newGameButton.getLocation().contains(touchPos))
        		{
        			if(text.equals("0"))
        			{
        				//Fade Out?
        				game.startFadeOut(new Cutscene(game, Gdx.files.internal("sceneOne.txt"), new LevelSelectScreen(game)));
        			}
        			else
        			{
        				if(!committingToNewGame)
        				{game.font.draw(game.batch, "Are you sure you'd like to overwrite your file? Click \"New\" again to confirm.", 300, 110);
        				committingToNewGame = true;
        				}
        				else
        				{f.writeString("0", false);
        				game.startFadeOut(new Cutscene(game, Gdx.files.internal("sceneOne.txt"), new LevelSelectScreen(game)));
        				}
        			}
        		}
        		else if(continueButton.getLocation().contains(touchPos))
        		{
        			//Fade Out?
        			game.startFadeOut(new LevelSelectScreen(game));
        		}
        	}
        }
      //FADE IN OR OUT
        if(game.isFadeIn)
        {
        	if(game.fadeIn(Gdx.graphics.getDeltaTime()))
        	{
        		//beginCutscene = true;
        		}
        }
        if(game.isFadeOut)
        {
        	if(game.fadeOut(Gdx.graphics.getDeltaTime()))
        	{
        		game.setScreen(game.toFollow);
        	}
        }
        game.batch.end();
        //This animation code is pretty great.
        
        //FOR TESTING recipescreen.JAVA
        	//if(Gdx.input.isTouched())
        	//game.setScreen(new LevelTwoRecipeScreen(game, new int[] {0, 0, 0, 1, 9, 0}));
            	//game.setScreen(new Cutscene(game, Gdx.files.internal("testScript.txt")));
            	//game.setScreen(new Cutscene(game, Gdx.files.internal("sceneOne.txt"), this));
        	//game.setScreen(new LevelTwoEndScreen(game, -149));
        	//FOR TESTING LEVEL TWO
        	//game.setScreen(new Cutscene(game, Gdx.files.internal("testScript.txt")));
        	//game.setScreen(new Cutscene(game, Gdx.files.internal("testScript.txt"), new LevelTwoRecipeScreen(game, new int[] {0, 0, 0, 1, 9, 0})));
        	//game.setScreen(new LevelTwoRecipeScreen(game, new int[] {6, 6, 2, 7, 15, 0}));
        //game.setScreen(new Cutscene(game, Gdx.files.internal("testScript.txt"), new LevelTwoStand(game, new int[] {0, 0, 0, 1, 9, 0})));
	
        if(Gdx.input.isTouched() && openingScreen)
    		{openingScreen = false;
    			
    		}
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
