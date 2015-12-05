package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
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
    
    private static final int		CHARS_PER_LINE = 55;
    int lineNumber = 1;
    
    final Texture					playerStand = new Texture(Gdx.files.internal("playerstand.png"));
    final Texture					redPlayerStand = new Texture(Gdx.files.internal("redplayerstand.png"));
    final Texture					competitorStand = new Texture(Gdx.files.internal("competitorstand.png"));
    final Texture					scoreCloud = new Texture(Gdx.files.internal("scorecloud.png"));
    public static final int 		STAND_Y = 200;
    public static final int			PLAYER_STAND_X = 320;
    public static final int			COMPETITOR_STAND_X = 830;
    
    private float					runningTime;
    
    Texture							textContainer;
    String							displayText;
    String							currentText;
    boolean							isDialogRunning;
    boolean							isDoneTalking = true;
    
    float							dialogCompletedTime;
    
    //THE PEOPLE!
    private Array<Peep>							peeps;
    private int 								peepsSpawned = 0;
    private long 								lastPeepTime;
    private int									peepsServed = 0;
    
    private boolean								spawning = false;
    private boolean								done	= false;
    //Level specific values
    
    //recipe = {lemons, sugar (in 1/4 cups), ice (in cube count/glass), cost (in .10 cent increments), hour (9-13 free market, 14, 15 are command), profitTotal in cents}
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
		
		//FOR TESTING
		peeps = new Array<Peep>();
		textContainer = new Texture(Gdx.files.internal("textpanel.png"));
		background = new Texture(Gdx.files.internal("leveltwostandbackground.png"));
		
		//START OF LEVEL DIALOG
		if(recipe[4] <14)
		addDialog("Alright! Here they come! Get selling!");
		else
			addDialog("Here they come! Get selling, comrade!");
	}
	
	//DIALOG AND TEXT
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
				currentText = currentText.substring(CHARS_PER_LINE + 4, currentText.length());
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
			if(recipe[3] > 10)
			return .8/(Math.pow(4, recipe[3]*.10));			
		else
			return 1 - .2*(Math.pow(3, recipe[3]*.10));
		{
			double x = Math.pow(4, (1 + recipe[4]*.1))*determineCustomerSatisfaction();
			double y = Math.pow(4, recipe[3]*.10)*(.3 + .14*(recipe[4] - 9));
			return x/(x+y);
		}
	}

	//
	//RENDERING
	public void render(float delta)
	{	
		if(peepsSpawned == 0 && !spawning && runningTime > 3)
			spawning = true;
		//Clears screen to black
		Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        runningTime += Gdx.graphics.getDeltaTime();
        
//        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))              	
//        		addDialog("Hey Bill, get the Will");     	
        //Sets up camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        
        //BATCH BEGINS
        game.batch.begin();
        //DRAW BACKGROUND
        game.batch.draw(background, 0, 0);
        //DRAW STANDS
        if(recipe[4] == 9)
        {
        	game.batch.draw(playerStand, PLAYER_STAND_X, STAND_Y);
        }
        else if(recipe[4] < 14)
        {
        	game.batch.draw(playerStand, PLAYER_STAND_X, STAND_Y);
        	game.batch.draw(competitorStand, COMPETITOR_STAND_X, STAND_Y);
        }
        else
        {
        	game.batch.draw(redPlayerStand, PLAYER_STAND_X, STAND_Y);
        	game.batch.draw(competitorStand, COMPETITOR_STAND_X, STAND_Y);
        }
        
        //DRAW SCORE
        double cost = recipe[0]*.25/8 + recipe[1]*.1/8 + recipe[2]*.01 ;        
        //Profit in cents
        int profitPerGlass = 10*recipe[3] - (int)(100*cost);
        int profit = recipe[5] + profitPerGlass*peepsServed;
        
        game.batch.draw(scoreCloud, 170, 380);
        game.font.draw(game.batch, "People served this hour: "+peepsServed, 210, 440);
        if(Math.abs(profit)%100 < 10)
        	if(profit >=0)
            game.font.draw(game.batch, "Total Profit Today: $" + profit/100 + ".0" + profit%100, 220, 420);
            else
            {
            	game.font.setColor(1f, 0, 0, 1);
            	game.font.draw(game.batch, "Total Profit Today: -$" + (-profit)/100 + ".0" + (-profit)%100, 220, 420);
            	game.font.setColor(game.FONT_COLOR[0], game.FONT_COLOR[1], game.FONT_COLOR[2], 1);
            }
        else
            if(profit >=0)
                game.font.draw(game.batch, "Total Profit Today: $" + profit/100 + "." + profit%100, 220, 420);
                else
                {
                	game.font.setColor(1f, 0, 0, 1);
                	game.font.draw(game.batch, "Total Profit Today: -$" + (-profit)/100 + "." + (-profit)%100, 220, 420);
                	game.font.setColor(game.FONT_COLOR[0], game.FONT_COLOR[1], game.FONT_COLOR[2], 1);
                }
        
        //RENDER PEEPS
        for(Peep person: peeps) {
		      game.batch.draw(person.getFrame(), person.x, person.y);
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
    		game.dialogFont.draw(game.batch, displayText, 160, 120);
            game.batch.draw(currentFrame, 40, 25);
        if(stateTime - dialogCompletedTime > POST_DIALOG_DELAY)
        {
        	isDialogRunning = false;
        }
        }
        
        
        
        	if(done)
        	{
        		//Fade out?
        		if(recipe[4] <= 14)
        		{int[] newR = {recipe[0], recipe[1], recipe[2], recipe[3], recipe[4] + 1, profit};
        		game.setScreen(new LevelTwoRecipeScreen(game, newR));
        		}
        		else
        		game.setScreen(new TitleScreen(game));
        	}
        //DRAW ACHIEVEMENT
        if(recipe[0]==0 && recipe[1] == 0 && recipe[3] > 0)
        	{Texture t = new Texture(Gdx.files.internal("waterachievement.png"));
        	if(runningTime < 1)
        		game.batch.draw(t, 0, 480 - runningTime*100);
        	else if( runningTime < 4)
        		game.batch.draw(t, 0, 380);
        	else if( runningTime < 5)
        		game.batch.draw(t, 0, 380 + (runningTime-4)*100);
        	}
        //BATCH ENDS
        game.batch.end();
        
		//SPAWN NECESSARY PEEPS
        if(TimeUtils.nanoTime() - lastPeepTime > 250000000d && spawning) spawnPeep();
        //ELIMINATE PEEPS THAT ARE OFFSCREEN
        Iterator<Peep> iter = peeps.iterator();
        while(iter.hasNext())
        {	Peep person = iter.next();
        	if (Math.abs(person.x) > 1000 || Math.abs(person.y) > 480)
        		iter.remove();
        }
        if(peepsSpawned == 60 && !iter.hasNext())
        	done = true;
	}
	
	
	private void spawnPeep() {
		if(peepsSpawned >= 60)
		{
			spawning = false;
			return;
		}
		int outfit = MathUtils.random(1,10);
		int direction = 0;
		if(recipe[4] == 9)
		{
			//If you're not going to get what you deserve:
			if(60 - peepsSpawned <= 60*determineCustomerAttraction() - peepsServed)
				direction = 1;
			//If you're getting too much:
			else if(peepsServed > 60*determineCustomerAttraction())
				direction = 0;
			else
			{
				if(Math.random() > determineCustomerAttraction())
					direction = 0;
				else
					direction = 1;
			}
		}
		else
		{
			//If you're not gonna get what you deserve:
			if(60 - peepsSpawned <= 60*determineCustomerAttraction() - peepsServed)
				direction = 1;
			//If you're getting too much:
			else if(peepsServed > 60*determineCustomerAttraction())
				direction = -1;
			else
			{
				if(Math.random() > determineCustomerAttraction())
					direction = -1;
				else
					direction = 1;
			}
		}
		if(direction ==1)
			peepsServed++;
		Peep guy = new Peep(direction, outfit);
		peeps.add(guy);
		lastPeepTime = TimeUtils.nanoTime();
		peepsSpawned++;
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
