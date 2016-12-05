package com.theironyard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {

	//class variables
	SpriteBatch batch;
	TextureRegion stand, north, south, tree;
	boolean goUp = true, faceRight = true;

	public static int mapWidth;
	public static int mapHeight;

	Animation walk;
	float time;

	static float x, y, xv, yv;
	static float MAX_VELOCITY = 200;

	static final int WIDTH = 18;
	static final int HEIGHT = 26;

	static final int DRAW_WIDTH = WIDTH * 3;
	static final int DRAW_HEIGHT = HEIGHT * 3;

	//create game images
	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture sheet = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(sheet, 16, 16);

		//TextureRegion down = grid[6][0];
		south = grid [6][0];
		TextureRegion up = grid[6][1];
		north = grid[6][1];
		TextureRegion right = grid[6][2];
		stand = grid[6][2];

		TextureRegion left = new TextureRegion(right);
		left.flip(true, false);

		TextureRegion down = new TextureRegion(up);
		down.flip(true, false);

		walk = new Animation(0.3f, grid[6][3], grid[6][2]);

		tree = grid[2][0];

	}//end create()

	//draw game images
	@Override
	public void render () {
		time += Gdx.graphics.getDeltaTime();

		move();

		//which image will be seen
		TextureRegion img;
		if(Gdx.input.isKeyPressed((Input.Keys.UP))){
			img = north;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			img = south;
		}
		else if (xv != 0) {
			img = walk.getKeyFrame(time, true);
		}
		else{
			img = stand;
		}

		//screen background and size
		Gdx.gl.glClearColor(.2f, 1, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mapWidth = Gdx.graphics.getWidth();
		mapHeight = Gdx.graphics.getHeight();

		//where on the screen
		batch.begin();
		if(faceRight){
			batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);
		}
		else{
			batch.draw(img, x, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
		}
		batch.end();

	}//end render()
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public float decelerate(float velocity){
		float deceleration = .75f;
		velocity *= deceleration;
		if(Math.abs(velocity) < 0){
			velocity = 0;
		}
		return velocity;

	}//end decelerate()

	//method for movement
	public void move(){

		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			yv = MAX_VELOCITY;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			yv = MAX_VELOCITY * -1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			xv = MAX_VELOCITY;
			faceRight = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			xv = MAX_VELOCITY * -1;
			faceRight = false;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			MAX_VELOCITY = MAX_VELOCITY + 5;
		}

		if(x > mapWidth){
			x=0;
		}
		if(x < 0){
			x=mapWidth;

		}
		if(y > mapHeight){
			y=0;

		}
		if(y < 0){
			y=mapHeight;

		}

		y = y + (yv * Gdx.graphics.getDeltaTime());
		x = x + (xv * Gdx.graphics.getDeltaTime());

		yv = decelerate(yv);
		xv = decelerate(xv);

	}//end move()

}//end class myGdxGame
