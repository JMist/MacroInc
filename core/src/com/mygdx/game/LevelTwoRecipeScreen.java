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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LevelTwoRecipeScreen implements Screen{

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
    
    private Vector2 				touchPos;
    
    //recipe = {lemons, sugar (in 1/4 cups), ice (in cube count/glass), cost, hour (9-13 free market, 14, 15 are command), profitTotal}
    //If this is changed, be sure to change all of the uses of "recipe" in the code.
    final int[] recipe;
    
    //BUTTONS WILL BE MANAGED BY Button.java class
    Button[]						buttons;
    private static final float						BUTTON_HEIGHT = 50;
    private static final float						BUTTON_WIDTH = 50;
    private static final float						BUTTON_X_SPACE = 125;
    private static final float						BUTTON_Y_SPACE = 100;
	public LevelTwoRecipeScreen(final MacroInc g, int[] r)
	{
		game = g;
		//ADD THE BUTTONS
		buttons = new Button[8];
		for(int i = 0; i < buttons.length; i++)
		{	buttons[i] = new Button(game);
			buttons[i].setHeight(BUTTON_HEIGHT);
			buttons[i].setWidth(BUTTON_WIDTH);
			//Increment Buttons
			if(i % 2 == 0)
			{
				buttons[i].setPress(new Texture(Gdx.files.internal("upbuttonpressed.png")));
				buttons[i].setNotPress(new Texture(Gdx.files.internal("upbutton.png")));
				buttons[i].setX(150 + i/2*BUTTON_X_SPACE);
				buttons[i].setY(500 - 100 - BUTTON_HEIGHT);
			}
			//Decrement Buttons
			if(i % 2 == 1)
			{
				buttons[i].setPress(new Texture(Gdx.files.internal("downbuttonpressed.png")));
				buttons[i].setNotPress(new Texture(Gdx.files.internal("downbutton.png")));
				buttons[i].setX(150 + i/2*BUTTON_X_SPACE);
				buttons[i].setY(500 - 100 - BUTTON_HEIGHT - BUTTON_Y_SPACE);
			}
				
		}
		Button submitButton = new Button(game, new Rectangle(), new Texture(Gdx.files.internal("upbuttonpressed.png")), new Texture(Gdx.files.internal("upbuttonpressed.png")));
		//PREP GURU FACE
		final int FRAME_COLS = 4;
		final int FRAME_ROWS = 1;
		
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
		
		recipe = r;
	}
	
	//TEXT CREATION AND DIALOG
	public void addDialog(String text)
	{
		stateTime = 0f;
		currentText = text;
		isDoneTalking = false;
		isDialogRunning = true;
	}

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
	
	
	//RENDER
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
        //DRAW BACKGROUND
        game.batch.draw(background, 0, 0);
        //DRAW BUTTONS,ETC
        for(Button e: buttons)
        {
        	e.draw();
        }
        
        //DRAW DIALOG
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
        	game.batch.draw(textContainer, 0, 0);
    		game.font.draw(game.batch, displayText, 195, 100);
            game.batch.draw(currentFrame, 40, 25);
        if(stateTime - dialogCompletedTime > POST_DIALOG_DELAY)
        {
        	isDialogRunning = false;
        }
        }
        game.batch.end();
        //BATCH ENDS
        
        //CLICK MANAGAMENT
        if(Gdx.input.justTouched())
        {
        	touchPos = new Vector2();
        	touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        	
        	//IF RECIPE IS READY
        	if(touchPos.x > 800 && touchPos.x < 900 && touchPos.y > 50 && touchPos.y < 100)
        	{//Move on? setScreen(new Cutscene...
        		
        		return;
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
