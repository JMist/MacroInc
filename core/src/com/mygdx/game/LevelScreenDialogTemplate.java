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



public class LevelScreenDialogTemplate implements Screen{

	final MacroInc game;
	
	OrthographicCamera camera;
	
	private Texture background;
	
	//Copied from Cutscene.java
	private static final float		FRAME_RATE = .12f;
	private static final float		TEXT_DELAY = MacroInc.TEXT_DELAY;
	private static final float		POST_DIALOG_DELAY = 2f;
    float keyPressed;
    Animation                       faceAnimation;          // #3
    Texture                         faceSheet;              // #4
    TextureRegion[]                 faceFrames;             // #5        // #6
    TextureRegion                   currentFrame;        
    private float stateTime;
    
    private float					runningTime;
    private static final int		CHARS_PER_LINE = 55;
    int lineNumber = 1;
    
    Texture							textContainer;
    String							displayText;
    String							currentText;
    boolean							isDialogRunning;
    boolean							isDoneTalking = true;
    
    float							dialogCompletedTime;
	public LevelScreenDialogTemplate(final MacroInc g)
	{
		game = g;
		final int FRAME_COLS = 4;
		final int FRAME_ROWS = 1;
		
		faceFrames = new TextureRegion[FRAME_COLS*FRAME_ROWS];
		faceSheet = new Texture(Gdx.files.internal("guruFace.png"));
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
		
		//if the text has finished the line
		if(!((f/TEXT_DELAY) - (CHARS_PER_LINE + 3)*lineNumber  > currentText.length()) && currentText.length() > 0 && (int)(f/TEXT_DELAY) > CHARS_PER_LINE *lineNumber + 3*(lineNumber-1))
		{
			//If the line ends just before punctuation...
			if(currentText.substring(displayText.length(), displayText.length()+1).equals(".") || currentText.substring(displayText.length(), displayText.length()+1).equals(",") || currentText.substring(displayText.length(), displayText.length()+1).equals("!"))
			{
				currentText = currentText.substring(0, displayText.length() + 1) + "\n  " + currentText.substring( displayText.length() + 1, currentText.length()); 
				lineNumber++;
			}
				//If the line ends in space
		else if(currentText.substring(displayText.length(), displayText.length()+1).equals(" ") || currentText.substring(displayText.length()-1, displayText.length()).equals(" "))
			{
				currentText = currentText.substring(0, displayText.length()) + " \n " + currentText.substring( displayText.length(), currentText.length());
				lineNumber++;
			}
			//Line doesn't end in space
			else
			{
				
				currentText = currentText.substring(0, displayText.length()) + "-\n " + currentText.substring( displayText.length(), currentText.length());
				lineNumber++;
			}
			//Kill the upper text if you have to.
			if(lineNumber > 2)
			{
				currentText = currentText.substring(CHARS_PER_LINE + 3, currentText.length());
			}
		}
		//if the text is over
	else if((int)(f/TEXT_DELAY)- (CHARS_PER_LINE + 3)*(lineNumber-1)   > currentText.length())
    {
    	displayText = currentText;
    	isDoneTalking = true;
    	lineNumber = 1;
    	dialogCompletedTime = f;
    }
		//Text isn't over
    else
    {
    	if(currentText.length() > 0)
    	if(lineNumber ==1)
    	displayText = currentText.substring(0, (int)(f/TEXT_DELAY));
    	else
    	{
    		if(((int)(f/TEXT_DELAY) - (CHARS_PER_LINE + 3)*(lineNumber-2) + 3) <= currentText.length())
    			displayText = currentText.substring(0, (int)(f/TEXT_DELAY) - (CHARS_PER_LINE + 3)*(lineNumber-2) + 3);
    		else
    		{
    			displayText = currentText;
    			isDoneTalking = true;
    			lineNumber = 1;
    	    	dialogCompletedTime = f;
    		}
    	}
    		
    }
    	
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
        	game.batch.draw(textContainer, 0, 0);
    		game.dialogFont.draw(game.batch, displayText, 160, 120);
            game.batch.draw(currentFrame, 40, 25);
        if(stateTime - dialogCompletedTime > POST_DIALOG_DELAY)
        {
        	isDialogRunning = false;
        }
        }
        game.batch.end();
        //BATCH ENDS
        
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
