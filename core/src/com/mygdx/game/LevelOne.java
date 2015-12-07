package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class LevelOne implements Screen{
	Texture carImage;
	Texture pizzaImage;
	Texture pizzaBackground;
	Sound pizzaCollect;
	int pizzaScore;
	double pizzaMeter;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Rectangle car;
	private Array<Rectangle> pizzas;
	int pizzaDisplay;
	final MacroInc game;
	/*
	 * Creating variables for the car, pizza collection, the background, and
	 * controlling how the pizza's hitbox is accounted for. When I tried
	 * implementing a triangle array, it seemed like a massive pain, but we
	 * could try it out.
	 */

	private long lastSpawnTime;
	private long startTime;
	private long currentTime;
int goon = 0;
	// Controlling the spawning of the pizza using variables of the total time
	// of the game (scarcity) and the time since a pizza last spawned.
	public LevelOne(final MacroInc gam){
		game=gam;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 480);
		// Maybe this should be a screen?

		batch=gam.batch;
		car = new Rectangle();
		car.x = 0;
		car.y = 61;
		car.width = 100;
		// Width is mainly a placeholder at the moment, would hopefully have it
		// line up with the actual model of the
		car.height = 100;
		// Lanes should have height 120 each, so a total height of 360, and 120
		// left for the top of the stage, so the car's hitbox occupies the
		// entire lane.
		startTime = TimeUtils.nanoTime();
		pizzaImage = new Texture(Gdx.files.internal("pizza.png"));
		carImage = new Texture(Gdx.files.internal("pizzaCar.png"));
		pizzaBackground = new Texture(Gdx.files.internal("pizzaBackground.png"));
		pizzaCollect=Gdx.audio.newSound(Gdx.files.internal("pizzaCollect.wav"));
		// Getting textures.
		pizzaMeter=100;
		pizzas = new Array<Rectangle>();
		
		// Makes the pizza array and puts some pizzas in there? Not too sure on
		// how it actually gets run with the iterator and all that.
	}
	

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		batch.begin();
		game.batch.draw(pizzaBackground, 0, 0);
		game.dialogFont.draw(batch, "Score:"+pizzaScore, 20, 440);
		game.dialogFont.draw(batch, "Gas:"+pizzaDisplay, 900, 440);
		
		batch.draw(carImage, car.x, car.y);
		for (Rectangle pizza : pizzas) {
			batch.draw(pizzaImage, pizza.x, pizza.y);

		}
		
		//FADE IN OR OUT
        if(game.isFadeIn)
        {
        	if(game.fadeIn(Gdx.graphics.getDeltaTime()))
        	{
        		spawnPizza();
        		}
        }
        else if(game.isFadeOut)
        {
        	if(game.fadeOut(Gdx.graphics.getDeltaTime()))
        	{
        		game.setScreen(new Cutscene(game, Gdx.files.internal("afterLevelOne.txt"), new LevelOneH(game)));
        	}
        }
        //game.batch.end();
		batch.end();
		// I barely have a clue what the render method even does, just
		// copy-pasted it.

		if (Gdx.input.isKeyJustPressed(Keys.DOWN))
			car.y -= 100;
		if (Gdx.input.isKeyJustPressed(Keys.UP))
			car.y += 100;
		// Only allowing the car to move between lanes

		if (car.y < 81)
			car.y = 81;
		if (car.y > 281)
			car.y = 281;
		if (isReady()) {
			spawnPizza();
		}
		// The time command to spawn pizzas. This should be modified with
		// current time in order to fade out the supply.

		Iterator<Rectangle> iter = pizzas.iterator();
		while (iter.hasNext()) {
			Rectangle pizza = iter.next();
			pizza.x -= 1400 * Gdx.graphics.getDeltaTime();
		
			if (pizza.x + 60 < 0)
				iter.remove();
			else if (pizza.overlaps(car)) {
				pizzaCollect.play();
				iter.remove();
				pizzaScore++;
				if (pizzaMeter > 95) {
					pizzaMeter = 100;
				} else {
					pizzaMeter += 5;
				}

				// Removes pizza and plays sound effect.
			}
			
			
		}
		pizzaMeter-=(.1);
		pizzaDisplay=(int)pizzaMeter;
		// Pizza moving across the screen and pizzaMeter going down probably
		if (pizzaMeter<=0 + goon){
			goon += 40;
			if(!game.isFadeOut)//End game. No idea what the code is, does the previous cutscene call the game object or something?
			game.startFadeOut();
		}

	}

	private boolean isReady() {
		// TODO Auto-generated method stub
		currentTime = TimeUtils.timeSinceNanos(startTime);
		return ((TimeUtils.nanoTime() - lastSpawnTime) / Math.pow(currentTime, .127) > 50000000);

	}

	private void spawnPizza() {
		Rectangle pizza = new Rectangle();
		pizza.x = 940;
		// Not sure if this is right, but I think 800 might bug something out.

		pizza.y = (MathUtils.random(1, 3) * 100);
		// Spawns in a random y location in a legitimate lane area.

		pizza.width = 120;
		pizza.height = 40;
		// Pizza hitbox takes up the entire lane as well.

		pizzas.add(pizza);
		lastSpawnTime = TimeUtils.nanoTime();
		//

		/*
		 * This is the total elapsed time since I started the counter, but I
		 * might have to initialize the counter someone else if there are any
		 * menus. This should help with slowly fading out the pizzas. Not
		 * entirely sure what type of slider to use thinking something like the
		 * square root of the hexadecimal log function, considering that the
		 * time is in nanos, which have a very numerical value. Could change all
		 * the times to millis? Would have to test the game a little bit in
		 * order to find an optimal balance.
		 */

	}

	public void dispose() {
		pizzaImage.dispose();
		carImage.dispose();
		pizzaCollect.dispose();
		batch.dispose();
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		game.startFadeIn();
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
