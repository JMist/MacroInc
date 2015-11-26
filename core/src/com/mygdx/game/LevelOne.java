package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class LevelOne {
	Texture carImage;
	Texture pizzaImage;
	Texture pizzaBackground;
	Sound pizzaCollect;
	int pizzaScore;
	float pizzaMeter;
	float pizzaSpeed;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Rectangle car;
	private Array<Rectangle> pizzas;
	int pizzaDisplay;
	/*
	 * Creating variables for the car, pizza collection, the background, and
	 * controlling how the pizza's hitbox is accounted for. When I tried
	 * implementing a triangle array, it seemed like a massive pain, but we
	 * could try it out.
	 */

	private long lastSpawnTime;
	private long startTime;
	private long currentTime;

	// Controlling the spawning of the pizza using variables of the total time
	// of the game (scarcity) and the time since a pizza last spawned.

	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 480);
		// Maybe this should be a screen?

		batch = new SpriteBatch();
		car = new Rectangle();
		car.x = 50;
		car.y = 60;
		car.width = 100;
		// Width is mainly a placeholder at the moment, would hopefully have it
		// line up with the actual model of the
		car.height = 120;
		// Lanes should have height 120 each, so a total height of 360, and 120
		// left for the top of the stage, so the car's hitbox occupies the
		// entire lane.
		startTime = TimeUtils.nanoTime();
		pizzaImage = new Texture(Gdx.files.internal("pizza.png"));
		carImage = new Texture(Gdx.files.internal("pizzaCar.png"));
		pizzaBackground = new Texture(Gdx.files.internal("pizzaBackground.png"));
		// Getting textures.

		pizzas = new Array<Rectangle>();
		spawnPizza();
		// Makes the pizza array and puts some pizzas in there? Not too sure on
		// how it actually gets run with the iterator and all that.
	}

	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		batch.begin();
		batch.draw(carImage, car.x, car.y);
		for (Rectangle pizza : pizzas) {
			batch.draw(pizzaImage, pizza.x, pizza.y);

		}
		batch.end();
		// I barely have a clue what the render method even does, just
		// copy-pasted it.

		if (Gdx.input.isKeyJustPressed(Keys.DOWN))
			car.x -= 120;
		if (Gdx.input.isKeyJustPressed(Keys.UP))
			car.x += 120;
		// Only allowing the car to move between lanes

		if (car.y < 30)
			car.y = 30;
		if (car.y > 270)
			car.y = 270;
		if (isReady()) {
			spawnPizza();
		}
		// The time command to spawn pizzas. This should be modified with
		// current time in order to fade out the supply.

		Iterator<Rectangle> iter = pizzas.iterator();
		while (iter.hasNext()) {
			Rectangle pizza = iter.next();
			pizza.x -= 3000 * Gdx.graphics.getDeltaTime();
			pizzaMeter-=Gdx.graphics.getDeltaTime();
			pizzaDisplay=MathUtils.ceil(pizzaMeter);
			// Pizza moving across the screen and pizzaMeter going down probably
			if (pizzaMeter<=0){
				//End game. No idea what the code is, does the previous cutscene call the game object or something?
			}
			if (pizza.x + 50 < 0)
				iter.remove();
			if (pizza.overlaps(car)) {
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

	}

	private boolean isReady() {
		// TODO Auto-generated method stub
		currentTime = TimeUtils.timeSinceNanos(startTime);
		return ((TimeUtils.nanoTime() - lastSpawnTime)
				/ (MathUtils.log(16, currentTime)) > 100000000);

	}

	private void spawnPizza() {
		Rectangle pizza = new Rectangle();
		pizza.x = 950;
		// Not sure if this is right, but I think 800 might bug something out.

		pizza.y = (MathUtils.random(1, 3) * 120);
		// Spawns in a random y location in a legitimate lane area.

		pizza.width = 120;
		pizza.height = 120;
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
}
