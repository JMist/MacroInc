package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
	private static final float		FRAME_RATE = .5f;
	private static final float		TEXT_DELAY = .03f;
	private static final float		POST_DIALOG_DELAY = 2f;
    float keyPressed;
    Animation                       faceAnimation;          // #3
    Texture                         faceSheet;              // #4
    TextureRegion[]                 faceFrames;             // #5        // #6
    TextureRegion                   currentFrame;        
    private float stateTime;
    
    Texture							textContainer;
    String							displayText;
    String							currentText;
    boolean							isDialogRunning;
    boolean							isDoneTalking;
    
    float							dialogCompletedTime;
	public LevelTwoStand(final MacroInc g)
	{
		game = g;
		final int FRAME_COLS = 1;
		final int FRAME_ROWS = 4;
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
        
		//This sets up the camera.
		camera = new OrthographicCamera();
		//To have this be the actual dimensions,
		//you must go into DesktopLauncher.java and edit.
		camera.setToOrtho(false, 1000, 480);
		background = new Texture(Gdx.files.internal("titlescreen.png"));
	}
	
	public void prepText(float f)
    {	if((int)(f/TEXT_DELAY)  > currentText.length())
    {
    	displayText = currentText;
    	isDoneTalking = true;
    	dialogCompletedTime = f;
    }
    else
    	displayText = currentText.substring(0, (int)(f/TEXT_DELAY) );
    }
	
	public void render(float delta)
	{	
		//Clears screen to black
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
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
        game.batch.draw(textContainer, 25, 25);
		game.font.draw(game.batch, displayText, 220, 125);
        game.batch.draw(currentFrame, 50, 50);
        if(stateTime - dialogCompletedTime > POST_DIALOG_DELAY)
        {
        	isDialogRunning = false;
        }
        }
        game.batch.end();
        //BATCH ENDS
        
	}
	
	public void addDialog(String text)
	{
		stateTime = 0f;
		currentText = text;
		isDialogRunning = true;
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
