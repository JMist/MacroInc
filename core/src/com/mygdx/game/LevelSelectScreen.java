package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;



public class LevelSelectScreen implements Screen{
	MacroInc game;
	
	final Sound				error = Gdx.audio.newSound(Gdx.files.internal("error.wav"));
	
	final Texture			background = new Texture(Gdx.files.internal("levelSelectBackground.png"));
	final Button[]			levelButtons;
	final Texture[]			levelIcons;
	final String[]			levelIconPaths =
			{
			"levelOneIcon.png",
			"levelTwoIcon.png",
			"levelOneIcon.png",
			"levelOneIcon.png",
			"levelOneIcon.png",
			"levelOneIcon.png",
			"levelOneIcon.png",
			"levelOneIcon.png"
			};
	
	int				levelsComplete;
	
	public LevelSelectScreen(MacroInc g)
	{
		
		
		game = g;
		levelIcons = new Texture[8];
		for(int i = 0; i < levelIcons.length; i++)
		{
			levelIcons[i] = new Texture(Gdx.files.internal(levelIconPaths[i]));
		}
		levelsComplete = Integer.parseInt(Gdx.files.local("playerdata.txt").readString());
		levelButtons = new Button[8];
		for(int i = 0; i < levelButtons.length; i++)
		{
			levelButtons[i] = new Button(game, new Rectangle(0, 0, 150, 150), levelIcons[i], levelIcons[i]);
		
			levelButtons[i].setX(125 + (i%4)*200);
			levelButtons[i].setY(480 - 220 - (i/4)*200);
		}
		//game.startFadeIn();
	}
	
	
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	
    	game.batch.begin();
    	//DRAW BACKGROUND
    	game.batch.draw(background, 0, 0);
    	
    	//DRAW LEVEL SELECT BUTTONS
    	for(Button b: levelButtons)
    	{
    		b.draw();
    	}
    	
    	//DRAW LOCKS ON LOCKED LEVELS
    	for(int i = levelsComplete+1; i < levelButtons.length; i++)
    	{
    		game.batch.draw(new Texture(Gdx.files.internal("lock.png")), 125 + (i%4)*200, 480 - 220 - (i/4)*200);
    	}
    	
    	//CLICK MANAGEMENT FOR BUTTONS
    	if(Gdx.input.justTouched())
        {
        	
        	
        	Vector2 touchPos = game.screenTransform(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        	int buttonTouched = -1;
        	for(int i = 0; i < levelButtons.length; i++)
        		if(levelButtons[i].getLocation().contains(touchPos))
        			buttonTouched = i;
        	//System.out.println(buttonTouched);
        	//System.out.println(levelsComplete);
        	
        	//Add other level paths later:
        	if(levelsComplete >= buttonTouched)
        	{
        		
        		switch(buttonTouched)
        	{
        	case 0:
        		//Fade out?
        		//Go into LevelOne cutscene?
        		game.startFadeOut(new Cutscene(game, Gdx.files.internal("beforeLevelOne.txt"), new LevelOne(game)));
        		break;
        	case 1:
        		//Fade out?
        		//Go into LevelTwo cutscene        		
        		game.startFadeOut(new Cutscene(game, Gdx.files.internal("levelTwoIntro.txt"), new LevelTwoRecipeScreen(game, new int[] {5, 5, 5, 5, 9, 0})));
        		break;
        	default:
        		break;
        	}}
        	else if(buttonTouched > 0)
        		error.play();
        }
    	
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
        		game.setScreen(game.toFollow);
        	}
        }
    	
    	game.batch.end();
    	
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
        	
        	levelsComplete = 8;
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
		 game.startFadeIn();
	 }
}