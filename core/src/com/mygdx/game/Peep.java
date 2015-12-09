package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

public class Peep {
//0 means no lemonade, 1 means to the player, -1 means to the competitor
final int 				direction;
//From 1 to 10
final Texture 			outfit;
//Time at birth of the Peep
final long				birthTime;
//Time it takes to walk through the a step, in seconds
static final float 		WALK_SPEED = .06f;
static final float		STEP_LENGTH = 80;
//Locations of the bottom left corner of the Peep when he buys the lemonade.
static final int		PLAYER_X = LevelTwoStand.PLAYER_STAND_X + 50;
static final int		PLAYER_Y = LevelTwoStand.STAND_Y - 30;

static final int		COMPETITOR_X = LevelTwoStand.COMPETITOR_STAND_X + 20;
static final int		COMPETITOR_Y = LevelTwoStand.STAND_Y - 30;

static final int		PEEP_START_X = 0;
static final int		PEEP_START_Y = 100;
//bottom left x and y coordinates, in pixels
public int x;
public int y;

static final int		STEP_FRAMES = 4;

//Walk Right
Animation               rightAnimation;          
TextureRegion[]         rightFrames;
//Walk Up
Animation               upAnimation;          
TextureRegion[]         upFrames;
//Walk Down
Animation               downAnimation;          
TextureRegion[]         downFrames;

TextureRegion           currentFrame;
public Peep(int d, int o)
{
	x = PEEP_START_X;
	y = PEEP_START_Y;
	
	direction = d;
	//TEST CODE
	//outfit = new Texture(Gdx.files.internal("samplepeep.png"));
	outfit = new Texture(Gdx.files.internal("peep"+o+".png"));
	birthTime = TimeUtils.nanoTime();
	
	TextureRegion[][] tmp = TextureRegion.split(outfit, outfit.getWidth()/STEP_FRAMES, outfit.getHeight()/3);
	
	rightFrames = new TextureRegion[STEP_FRAMES];
	upFrames = new TextureRegion[STEP_FRAMES];
	downFrames = new TextureRegion[STEP_FRAMES];
	for(int i = 0; i < 4; i++)
	{
		rightFrames[i] = tmp[0][i];
		upFrames[i] = tmp[1][i];
		downFrames[i] = tmp[2][i];
	}
	
	rightAnimation = new Animation(WALK_SPEED/STEP_FRAMES, rightFrames);
	upAnimation = new Animation(WALK_SPEED/STEP_FRAMES, upFrames);
	downAnimation = new Animation(WALK_SPEED/STEP_FRAMES, downFrames);
	
}

public TextureRegion getFrame()
{
	long timeElapsed = TimeUtils.nanoTime() - birthTime;
	//Steps in this Peep's lifetime
	float stepCount = (float) ((timeElapsed/1000000000.0)/WALK_SPEED);
	
	if(direction == 1)
	{
		float stopRight = (PLAYER_X - PEEP_START_X)/STEP_LENGTH;
		float stopUp = stopRight + (PLAYER_Y - PEEP_START_Y)/STEP_LENGTH;
		float stopPause = stopUp + 2f;
		float stopDown = stopPause + (PLAYER_Y - PEEP_START_Y)/STEP_LENGTH;
		//Walking right, towards selected stand.
	if(stepCount < stopRight)
	{		
		this.x = (int) (PEEP_START_X + stepCount*STEP_LENGTH);
		return rightAnimation.getKeyFrame(stepCount, true);
	}
	else if(stepCount < stopUp)
	{
		this.x = PLAYER_X;
		this.y = (int) (PEEP_START_Y + (stepCount - stopRight)*STEP_LENGTH);
		return upAnimation.getKeyFrame(stepCount, true);
	}
	else if(stepCount < stopPause)
	{
		this.x = PLAYER_X;
		this.y = PLAYER_Y;
		return upAnimation.getKeyFrame(0);
	}
	else if(stepCount < stopDown)
	{
		this.x = PLAYER_X;
		this.y = (int) (PLAYER_Y - (stepCount - stopPause) *STEP_LENGTH);
		return downAnimation.getKeyFrame(stepCount, true);
	}
	else
	{
		this.y = PEEP_START_Y;
		this.x = (int) (PLAYER_X + (stepCount - stopDown)*STEP_LENGTH);
		return rightAnimation.getKeyFrame(stepCount, true);
	}
	}
	if(direction == -1)
	{
		float stopRight = (COMPETITOR_X - PEEP_START_X)/STEP_LENGTH;
		float stopUp = stopRight + (COMPETITOR_Y - PEEP_START_Y)/STEP_LENGTH;
		float stopPause = stopUp + 2f;
		float stopDown = stopPause + (COMPETITOR_Y - PEEP_START_Y)/STEP_LENGTH;
		//Walking right, towards selected stand.
	if(stepCount < stopRight)
	{		
		this.x = (int) (PEEP_START_X + stepCount*STEP_LENGTH);
		return rightAnimation.getKeyFrame(stepCount, true);
	}
	else if(stepCount < stopUp)
	{
		this.x = COMPETITOR_X;
		this.y = (int) (PEEP_START_Y + (stepCount - stopRight)*STEP_LENGTH);
		return upAnimation.getKeyFrame(stepCount, true);
	}
	else if(stepCount < stopPause)
	{
		this.x = COMPETITOR_X;
		this.y = COMPETITOR_Y;
		return upAnimation.getKeyFrame(0);
	}
	else if(stepCount < stopDown)
	{
		this.x = COMPETITOR_X;
		this.y = (int) (COMPETITOR_Y - (stepCount - stopPause)*STEP_LENGTH);
		return downAnimation.getKeyFrame(stepCount, true);
	}
	else
	{
		this.y = PEEP_START_Y;
		this.x = (int) (COMPETITOR_X + (stepCount - stopDown)*STEP_LENGTH);
		return rightAnimation.getKeyFrame(stepCount, true);
	}
	}
	else
	{
		this.x = (int) (PEEP_START_X + stepCount*STEP_LENGTH);
		return rightAnimation.getKeyFrame(stepCount, true);
	}
}

	public void dispose()
	{
		outfit.dispose();
	}
}
