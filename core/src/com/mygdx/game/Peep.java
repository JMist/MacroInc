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
static final float 		WALK_SPEED = 1f;
static final float		STEP_LENGTH = 5;
//Locations of the bottom left corner of the Peep when he buys the lemonade.
static final int		PLAYER_X = 50;
static final int		PLAYER_Y = 200;

static final int		COMPETITOR_X = 400;
static final int		COMPETITOR_Y = 200;

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
	direction = d;
	outfit = new Texture(Gdx.files.internal("peep"+o+".png"));
	birthTime = TimeUtils.nanoTime();
	
	TextureRegion[][] tmp = TextureRegion.split(outfit, outfit.getWidth()/STEP_FRAMES, outfit.getHeight()/3);
	
	rightFrames = new TextureRegion[STEP_FRAMES];
	upFrames = new TextureRegion[STEP_FRAMES];
	downFrames = new TextureRegion[STEP_FRAMES];
	for(int i = 0; i < STEP_FRAMES; i++)
	{
		rightFrames[i] = tmp[i][0];
		upFrames[i] = tmp[i][1];
		downFrames[i] = tmp[i][2];
	}
	
	rightAnimation = new Animation(WALK_SPEED/STEP_FRAMES, rightFrames);
	upAnimation = new Animation(WALK_SPEED/STEP_FRAMES, upFrames);
	downAnimation = new Animation(WALK_SPEED/STEP_FRAMES, downFrames);
	
}

public TextureRegion getFrame()
{
	long timeElapsed = TimeUtils.nanoTime() - birthTime;
	
	
	return null;
}

public int getX()
{
	return 0;
}

public int getY()
{
	return 0;
}
	
}
