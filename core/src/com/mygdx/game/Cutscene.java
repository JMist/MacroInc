package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Scanner;

public class Cutscene implements Screen{
	final MacroInc game;
	final FileHandle script;
	OrthographicCamera camera;
	//We'll add this sound when we have a dialog sound;
	//private Sound dialog;
	Scanner read;
	//Size of the portrait animation
	private static final int        FRAME_COLS = 4;         // #1
    private static final int        FRAME_ROWS = 1;         // #2
    
    Animation                       faceAnimation;          // #3
    Texture                         faceSheet;              // #4
    TextureRegion[]                 faceFrames;             // #5        // #6
    TextureRegion                   currentFrame;           // #7
    String 							currentFace;
    float stateTime;                                        // #8

	public Cutscene(MacroInc g, FileHandle scrip)
    
	{
		this.game = g;
		this.script = scrip;
		
        read = new Scanner(script.readString());
	}
    
    public void readLines()
    {
    	
    }
    
    public void prepFace()
    {
    	faceFrames = new TextureRegion[FRAME_COLS*FRAME_ROWS];
		faceSheet = new Texture(Gdx.files.internal("sampleface.png"));
		TextureRegion[][] tmp = TextureRegion.split(faceSheet, faceSheet.getWidth()/FRAME_COLS, faceSheet.getHeight()/FRAME_ROWS);
		int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                faceFrames[index++] = tmp[i][j];
            }
        }
        faceAnimation = new Animation(.5f, faceFrames);
        stateTime = 0f;
    }
	public void render(float delta)
	{	Gdx.gl.glClearColor(1, 1, 1, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stateTime += Gdx.graphics.getDeltaTime();           // #15
    	currentFrame = faceAnimation.getKeyFrame(stateTime, true);
		
		game.batch.begin();
        game.batch.draw(currentFrame, 50, 50);
        game.batch.end();
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
