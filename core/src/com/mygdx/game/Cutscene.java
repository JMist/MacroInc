package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Cutscene implements Screen{
	final MacroInc game;
	final FileHandle script;
	//The Screen which the Cutscene switches into at the end.
	final Screen toFollow;
	
	OrthographicCamera camera;
	//We'll add this sound when we have a dialog sound;
	//private Sound dialog;
	Scanner read;
	//Size of the portrait animation
	private static final int        FRAME_COLS = 4;         // #1
    private static final int        FRAME_ROWS = 1; 
    //Frame rate for face animation
    private static final float		FRAME_RATE = .5f;
    
    //Delay times for key input and text timing, respectively.
    private static final float		KEY_DELAY = .5f;
    private static final float		TEXT_DELAY = .03f;
    private static final int		CHARS_PER_LINE = 55;
    float keyPressed;
    Animation                       faceAnimation;          // #3
    Texture                         faceSheet;              // #4
    TextureRegion[]                 faceFrames;             // #5        // #6
    TextureRegion                   currentFrame;           // #7
    float stateTime;                                        // #8

    Texture							background;
    
    int lineNumber = 1;
    //The container image for the text
    private static final Texture textContainer = new Texture(Gdx.files.internal("textpanel.png"));
    
    //Things for text display
    //The full line that is begin printed a character at a time.
    String							currentText = "";
    int currentTextInitialLength = 0;
    //The characters that are currently on the screen.
    String							displayText = "";
    //Should the face's mouth be closed?
    boolean							isDoneTalking = false;
    
    
	public Cutscene(MacroInc g, FileHandle s, Screen toFollowScreen)
    
	{
		this.game = g;
		this.script = s;
		this.toFollow = toFollowScreen;
		
        read = new Scanner(script.readString());
        
        //For testing:
        setFace("sampleface.png");
        //For testing:
        setBackground("bg1.png");
        System.out.println(script.readString());
        
        game.startFadeIn();
	}
    
	
    public void read()
    {	
    	//Check for end to cutscene  	
    	if(read.hasNext("/terminate"))
    	{
    		System.out.println("Cutscene terminated");
    		
    		
    		game.setScreen(toFollow);
    	}
    	//Check for background update
    	if(read.hasNext(Pattern.compile("/bg")))
    	{	System.out.println(read.next() + " changed");
    		//if(read.next().equals("/bg{"))
    	if(read.hasNext())
    		setBackground(read.next());
    		//read.skip("}");
    	}
    	
    	//Check for animation update: TO COME
    	
    	
    	//Check for face update
    	if(read.hasNext(Pattern.compile("/f")))
    	{
    		System.out.println(read.next() + " changed");
    		if(read.hasNext())
    			setFace(read.next());
    		currentText = "";
    		displayText = "";
    	}
    	
    	//Check for text update
    	if(read.hasNext(Pattern.compile("/t")))
    	{	isDoneTalking = false;
    		stateTime = 0;
    		currentText = "";
    		System.out.println(read.next() + " changed");
    		while(!read.hasNext(Pattern.compile("/e")))
    		{
    			currentText = currentText + read.next() + " ";
    		}
    		currentTextInitialLength = currentText.length();
    		System.out.println(currentText);
    		System.out.println(read.next() + " finished");
    	}
    	
    	//Check for thought update
    	if(read.hasNext(Pattern.compile("/th")))
    	{	
    		stateTime = 0;
    		currentText = "";
    		System.out.println(read.next() + " changed");
    		while(!read.hasNext(Pattern.compile("/e")))
    		{
    			currentText = currentText + read.next() + " ";
    		}
    		currentTextInitialLength = currentText.length();
    		System.out.println(currentText);
    		System.out.println(read.next() + " finished");
    	}
    }
    
    //Using String path, sets the current background to the background image at "path" in assets.
    public void setBackground(String path)
    {
    	background = new Texture(Gdx.files.internal(path));
    }
    
    //Using String path, sets the current facial animation to the spritesheet image at "path" in assets.
    public void setFace(String path)
    {
    	faceFrames = new TextureRegion[FRAME_COLS*FRAME_ROWS];
		faceSheet = new Texture(Gdx.files.internal(path));
		TextureRegion[][] tmp = TextureRegion.split(faceSheet, faceSheet.getWidth()/FRAME_COLS, faceSheet.getHeight()/FRAME_ROWS);
		int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                faceFrames[index++] = tmp[i][j];
            }
        }
        faceAnimation = new Animation(FRAME_RATE, faceFrames);
        stateTime = 0f;
    }
	
    //
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
				currentText = currentText.substring(CHARS_PER_LINE + 4, currentText.length());
			}
		}
		//if the text is over
	else if((int)(f/TEXT_DELAY)- (CHARS_PER_LINE + 3)*(lineNumber-1)   > currentText.length())
    {
    	displayText = currentText;
    	isDoneTalking = true;
    	lineNumber = 1;
    	//dialogCompletedTime = f;
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
    			lineNumber = 1;
    			isDoneTalking = true;
    		}
    	}
    		
    }
    	
    }
    public void render(float delta)
	{	Gdx.gl.glClearColor(1, 1, 1, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
    	
		stateTime += Gdx.graphics.getDeltaTime();           // #15
		if(!isDoneTalking)
		prepText(stateTime);
		
    	currentFrame = faceAnimation.getKeyFrame(stateTime, true);
		if(isDoneTalking)
			currentFrame = faceAnimation.getKeyFrame(0, true);
    	
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		game.batch.draw(textContainer, 0, 0);
		//WAS 195, 100
		game.dialogFont.draw(game.batch, displayText, 160, 120);
        game.batch.draw(currentFrame, 40, 25);
        game.batch.end();
        
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
        	if(TimeUtils.nanoTime() - keyPressed > 1000000000*KEY_DELAY)
        	{
        	read();
        	keyPressed = TimeUtils.nanoTime();
        	}
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
        	//Fade out?
        	System.out.println("Cutscene terminated");
    		fadeOut();
    		
    		game.setScreen(toFollow);
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
