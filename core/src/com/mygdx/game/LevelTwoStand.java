package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;



public class LevelTwoStand implements Screen{

	final MacroInc game;
	
	OrthographicCamera camera;
	
	private Texture background;
	
	//Copied from Cutscene.java
	private static final float		FRAME_RATE = .12f;
	private static final float		TEXT_DELAY = .03f;
	private static final float		POST_DIALOG_DELAY = 2f;
    float keyPressed;
    Animation                       faceAnimation;          // #3
    Texture                         faceSheet;              // #4
    TextureRegion[]                 faceFrames;             // #5        // #6
    TextureRegion                   currentFrame;        
    private float stateTime;
    
    private float					runningTime;
    
    Texture							textContainer;
    String							displayText;
    String							currentText;
    boolean							isDialogRunning;
    boolean							isDoneTalking = true;
    
    float							dialogCompletedTime;
    
    //Level specific values
    
    //recipe = {lemons, sugar (in 1/4 cups), ice (in cube count/glass), cost, hour (9-13 free market, 14, 15 are command), profitTotal}
    //If this is changed, be sure to change all of the uses of "recipe" in the code.
    final int[] recipe;
    
	public LevelTwoStand(final MacroInc g, int[] r)
	{	
		
		game = g;
		//Attain the recipe
		recipe = new int[6];
		if(r.length == 6)
			for(int i = 0; i < 6; i++)
				recipe[i] = r[i];
		
		final int FRAME_COLS = 4;
		final int FRAME_ROWS = 1;
		
		//Prepare the guru's face for dialog boxes
		faceFrames = new TextureRegion[FRAME_COLS*FRAME_ROWS];
		faceSheet = new Texture(Gdx.files.internal("sampleface.png"));
		TextureRegion[][] tmp = TextureRegion.split(faceSheet, faceSheet.getWidth()/FRAME_COLS, faceSheet.getHeight()/FRAME_ROWS);
		int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                faceFrames[index++] = tmp[i][j];
            }
        }
        faceAnimation = new Animation(FRAME_RATE, faceFrames);
        stateTime = 0f;
        runningTime = 0f;
		
		//This sets up the camera.
		camera = new OrthographicCamera();
		//To have this be the actual dimensions,
		//you must go into DesktopLauncher.java and edit.
		camera.setToOrtho(false, 1000, 480);
		
		
		textContainer = new Texture(Gdx.files.internal("textpanel.png"));
		background = new Texture(Gdx.files.internal("titlescreen.png"));
	}
	
	//DIALOG AND TEXT
	public void prepText(float f)
    {	
		if((int)(f/TEXT_DELAY)  > currentText.length())
    {
    	displayText = currentText;
    	isDoneTalking = true;
    	dialogCompletedTime = f;
    }
    else
    	displayText = currentText.substring(0, (int)(f/TEXT_DELAY) );
    }
	public void addDialog(String text)
	{
		stateTime = 0f;
		currentText = text;
		isDoneTalking = false;
		isDialogRunning = true;
	}
	
	//CALCULATIONS
	public double determineCustomerSatisfaction()
	{	
		//Tartness determines 55%, Sweetness determines 30% Temperature determines 15% 
		
		//Tartness should approach 10.66 lemons in 8 servings
		double tartFactor = .55/(1 + 2*Math.pow(2, 10.66 - recipe[0]));
		//Sweetness should be 2 cups in 8 servings
		double sweetFactor = .3/(1 + .2*Math.pow(2, 8 - recipe[1]));
		//There should be 3 cubes, plus an extra cube per hour past 9
		double tempFactor = .15/(1 + Math.pow(2, 6 + recipe[2] - recipe[4]));
		
		return tartFactor + sweetFactor + tempFactor;
	}
	
	public double determineCustomerAttraction()
	{
		if(recipe[4] == 9)
			return .8;
		else
		{
			double x = determineCustomerSatisfaction();
			double y = .3 + .14*(recipe[4] - 9);
			return x/(x+y);
		}
	}

	//
	//RENDERING
	public void render(float delta)
	{	
		//Clears screen to black
		Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        runningTime += Gdx.graphics.getDeltaTime();
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))              	
        		addDialog("Hey Bill, get the Will");     	
        //Sets up camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        
        //BATCH BEGINS
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        if(isDialogRunning)
        {
        	stateTime += Gdx.graphics.getDeltaTime();
        	currentFrame = faceAnimation.getKeyFrame(stateTime, true);
        	if(!isDoneTalking)
    		prepText(stateTime);
        	else
        	{
    			currentFrame = faceAnimation.getKeyFrame(0, true);
        	}	
        game.batch.draw(textContainer, 25, 480 - 25 - textContainer.getHeight());
		game.font.draw(game.batch, displayText, 220, 480 - 125);
        game.batch.draw(currentFrame, 50, 480 - 50 - currentFrame.getRegionHeight());
        if(stateTime - dialogCompletedTime > POST_DIALOG_DELAY)
        {
        	isDialogRunning = false;
        }
        }
        game.batch.end();
        //BATCH ENDS
        
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
