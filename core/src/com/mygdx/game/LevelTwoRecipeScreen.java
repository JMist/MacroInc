package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	private static final float		TEXT_DELAY = MacroInc.TEXT_DELAY;
	private static final float		POST_DIALOG_DELAY = 2f;
    float keyPressed;
    Animation                       faceAnimation;          // #3
    Texture                         faceSheet;              // #4
    TextureRegion[]                 faceFrames;             // #5        // #6
    TextureRegion                   currentFrame;        
    private float stateTime;
    
    
    private static final int		CHARS_PER_LINE = 55;
    int lineNumber = 1;
    
    private float					runningTime;
    
    Texture							textContainer;
    String							displayText;
    String							currentText;
    boolean							isDialogRunning;
    boolean							isDoneTalking = true;
    
    float							dialogCompletedTime = 15;
    
    private Vector2 				touchPos;
    final Texture					scoreCloud = new Texture(Gdx.files.internal("scorecloud.png"));
    //recipe = {lemons, sugar (in 1/4 cups), ice (in cube count/glass), cost (in .10 increments), hour (9-13 free market, 14, 15 are command), profitTotal in cents}
    //If this is changed, be sure to change all of the uses of "recipe" in the code.
    final int[] recipe;
    
    //BUTTONS WILL BE MANAGED BY Button.java class
    Button[]						buttons;
    private static final float						BUTTON_HEIGHT = 50;
    private static final float						BUTTON_WIDTH = 50;
    private static final float						BUTTON_X_SPACE = 125;
    private static final float						BUTTON_Y_SPACE = 100;
    
    private Button 					submitButton;
    //ERROR SOUND
    private Sound 					error;
  //??
  	String[] info = new String[] {"Lemons \n per pitcher", "1/4 cups Sugar \n per pitcher", "Ice cubes \n per glass", "Cost \n per glass"};
  	BitmapFont f;
  	
	public LevelTwoRecipeScreen(final MacroInc g, int[] r)
	
	
	{
		//INSTANTIALIZE font for top left
		f = new BitmapFont();
        f.setColor(1f, .75f,0 ,1);
        f.getData().setScale(3f);
		//INSTANTIALIZE error sound
		error = Gdx.audio.newSound(Gdx.files.internal("error.wav"));
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
				buttons[i].setY(480 - 100 - BUTTON_HEIGHT);
			}
			//Decrement Buttons
			if(i % 2 == 1)
			{
				buttons[i].setPress(new Texture(Gdx.files.internal("downbuttonpressed.png")));
				buttons[i].setNotPress(new Texture(Gdx.files.internal("downbutton.png")));
				buttons[i].setX(150 + i/2*BUTTON_X_SPACE);
				buttons[i].setY(480 - 100 - BUTTON_HEIGHT - BUTTON_Y_SPACE);
			}
				
		}
		//DONE WITH RECIPE BUTTON
		submitButton = new Button(game, new Rectangle(), new Texture(Gdx.files.internal("readybutton.png")), new Texture(Gdx.files.internal("readybutton.png")));
		submitButton.setX(760);
		submitButton.setY(100);
		submitButton.setHeight(80);
		submitButton.setWidth(200);
		//PREP GURU FACE
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
		background = new Texture(Gdx.files.internal("recipebackground.png"));
		
		//DETERMINE RECIPE:
		if(r[4] < 13)
		recipe = r;
		else if(r[4] == 13)
		{int[] newRecipe = {9, 11,3,10, 14, r[5]} ;
		recipe = newRecipe;
		}
		else
			{int[] newRecipe = {19, 20, 18, 2, 15, r[5]};
		recipe = newRecipe;}
		
		
		
		
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
        {            	
        		if(recipe[4] < 14)addDialog("Edit your recipe here.");
        		else
			addDialog("Welcome to the command system, my comrade.");
        }
        
        //Sets up camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        
        //BATCH BEGINS
        game.batch.begin();
        
        //DRAW BACKGROUND        
        game.batch.draw(background, 0, 0);
        f.draw(game.batch, "Recipe Design", 20, 470);
        
        //DRAW  RECIPE VALUES, PROFIT PER GLASS
        double cost = recipe[0]*.25/8 + recipe[1]*.1/8 + recipe[2]*.01 ;
        //game.batch.draw(scoreCloud, 670, 360);
        game.font.draw(game.batch, "Hour: "+recipe[4]+":00", 710, 430);
        if(Math.abs(recipe[5])%100 < 10)
        	if(recipe[5] >=0)
        		game.font.draw(game.batch, "Total Profit Today: $" + recipe[5]/100 + ".0" + recipe[5]%100, 710, 410);
            else
            {
            	game.font.setColor(1f, 0, 0, 1);
            	game.font.draw(game.batch, "Total Profit Today: -$" + (-recipe[5])/100 + ".0" + (-recipe[5])%100, 710, 410);
            	game.font.setColor(game.FONT_COLOR[0], game.FONT_COLOR[1], game.FONT_COLOR[2], 1);
            }
        else
            if(recipe[5] >=0)
                game.font.draw(game.batch, "Total Profit Today: $" + recipe[5]/100 + "." + recipe[5]%100, 710, 410);
            else
                {
                	game.font.setColor(1f, 0, 0, 1);
                	game.font.draw(game.batch, "Total Profit Today: -$" + (-recipe[5])/100 + "." + (-recipe[5])%100, 710, 410);
                	game.font.setColor(game.FONT_COLOR[0], game.FONT_COLOR[1], game.FONT_COLOR[2], 1);
                }
        //Profit in cents
        int profitPerGlass = 10*recipe[3] - (int)(100*cost);
          
        
      //BUTTONS
        for(Button e: buttons)
        {
        	e.draw();
        }
        submitButton.draw();
        for(int i = 0; i < 3; i++)
        game.font.draw(game.batch, ""+ recipe[i], 150 + i*BUTTON_X_SPACE + 20, 480 - 100 - BUTTON_HEIGHT - BUTTON_Y_SPACE + 90);
        game.font.draw(game.batch, "$"+ recipe[3]/10 + "." + recipe[3]%10 + "0", 150 + 3*BUTTON_X_SPACE, 480 - 100 - BUTTON_HEIGHT - BUTTON_Y_SPACE + 90);
        for(int i = 0; i < 3; i++)
            game.font.draw(game.batch, info[i], 150 + i*BUTTON_X_SPACE + 20, 480 - 100 - 3*BUTTON_HEIGHT - BUTTON_Y_SPACE + 90);
            game.font.draw(game.batch, info[3], 150 + 3*BUTTON_X_SPACE, 480 - 100 - 3*BUTTON_HEIGHT - BUTTON_Y_SPACE + 90);
        
            
            if(Math.abs(profitPerGlass)%100 < 10)
        	if(profitPerGlass >=0)
        			game.font.draw(game.batch, "Profit per glass: $" + profitPerGlass/100 + ".0" + profitPerGlass%100, 800, 240);
        	else
        	{
        	game.font.setColor(1f, 0, 0, 1);
        	game.font.draw(game.batch, "Profit per glass: - $" + (-profitPerGlass)/100 + ".0" + (-profitPerGlass%100), 800, 240);
        	game.font.setColor(game.FONT_COLOR[0], game.FONT_COLOR[1], game.FONT_COLOR[2], 1);
        	}
        else
        	if(profitPerGlass >=0)
    			game.font.draw(game.batch, "Profit per glass: $" + profitPerGlass/100 + "." + profitPerGlass%100, 800, 240);
        	else
        	{
        		game.font.setColor(1f, 0, 0, 1);
        		game.font.draw(game.batch, "Profit per glass: - $" + (-profitPerGlass)/100 + "." + (-profitPerGlass%100), 800, 240);
        		game.font.setColor(game.FONT_COLOR[0], game.FONT_COLOR[1], game.FONT_COLOR[2], 1);
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
        
      //FADE IN OR OUT
        if(game.isFadeIn)
        {
        	if(game.fadeIn(Gdx.graphics.getDeltaTime()))
        	{
        		//MOVE THIS TO AFTER FADE IN
        		//FIRST TRY ADD DIALOG
        		if(recipe[4] == 9)
        			addDialog("Try to shoot for a tasty recipe, and a low price.");
        		if(recipe[4] == 10 && recipe[3] < 20)
        			addDialog("It looks like a competitor is setting up shop next door. Keep your price low to keep sales high!");
        		else if(recipe[4] == 10 && recipe[3] >= 20)
        			addDialog("It looks like a competitor is setting up shop next door. With that last price, you're not going to get ANY demand...");
        		else if(recipe[4] == 14)
        			addDialog("Welcome to the Command System, comrade. The Central Board has created a recipe for you.");
        		else if(recipe[4] == 15)
        			addDialog("Welcome to the well-functioning, never-failing Command System, comrade. The Central Board has created a flawless, unbeatable recipe for you.");
        	}
        }
        if(game.isFadeOut)
        {
        	if(game.fadeOut(Gdx.graphics.getDeltaTime()))
        	{
        		game.setScreen(new LevelTwoStand(game, recipe));
        	}
        }
        
        
        game.batch.end();
        //BATCH ENDS
        
        //CLICK MANAGAMENT - AM ONLY
        if(recipe[4] < 14)	
        if(Gdx.input.justTouched())
        {
        	touchPos = new Vector2();
        	touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        	touchPos = game.screenTransform(touchPos);
        	int buttonTouched = -1;
        	for(int i = 0; i < buttons.length; i++)
        		if(buttons[i].getLocation().contains(touchPos))
        			buttonTouched = i;
        	//cost per glass for production calculated above is "cost"
        	
        	switch(buttonTouched)
        	{
        	case 0:
        		if(recipe[0] < 20 && recipe[3]*.10 - cost >= .25/8)
        		recipe[0]++;
        		else
        			error.play();
        		break;
        	case 1:
        		if(recipe[0] > 0)
        			recipe[0]--;
        		else
        			error.play();
        		break;
        	case 2:
        		if(recipe[1] < 20 && recipe[3]*.10 - cost >= .1/8)
        		recipe[1]++;
        		else
        			error.play();
        		break;
        	case 3:
        		if(recipe[1] > 0)
        			recipe[1]--;
        		else
        			error.play();
        		break;
        	case 4:
        		if(recipe[2] < 20 && recipe[3]*.10 - cost >= .01)
        		recipe[2]++;
        		else
        			error.play();
        		break;
        	case 5:
        		if(recipe[2] > 0)
        			recipe[2]--;
        		else
        			error.play();
        		break;
        	case 6:
        		if(recipe[3]*.1 < 10)
        		recipe[3]++;
        		else
        			error.play();
        		break;
        	case 7:
        		if(recipe[3]*.1 > cost + .099)
        			recipe[3]--;
        		else
        			error.play();
        		break;
        		default:
        			break;
        	}
        	
        	//IF RECIPE IS READY
        	if(submitButton.getLocation().contains(touchPos))
        	{//Move on? setScreen(new Cutscene...
        		game.startFadeOut();
        		
        	}
        	
        	
        	
        }
        
        //CLICK MANAGEMENT COMMAND SYSTEM
        if(Gdx.input.justTouched() && recipe[4] >= 14)
        {
        	
        	
        	touchPos = new Vector2();
        	touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        	touchPos = game.screenTransform(touchPos);
        	int buttonTouched = -1;
        	for(int i = 0; i < buttons.length; i++)
        		if(buttons[i].getLocation().contains(touchPos))
        			buttonTouched = i;
        	if(buttonTouched != -1)
        		{
        		error.play();
        	     addDialog("This recipe, it is good. Trust the central board.");
        		}
        	if(submitButton.getLocation().contains(touchPos))
        	{//Move on? setScreen(new Cutscene...       		
        		game.setScreen(new LevelTwoStand(game, recipe));
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
		 game.startFadeIn();
	 }
}
