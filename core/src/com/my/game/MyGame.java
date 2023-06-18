package com.my.game;



import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGame implements Screen {

	private Sound shotSound;
	private Sound explosionSound;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle spaceship;
	private ArrayList<Rectangle> enemies;
	private ArrayList<Rectangle> lasershots;
	final AmongStars game;
	int count;
	private final int spaceshipWidthPixels = 24;
	private final int spaceshipHeightPixels = 18;

	Animation[] rolls;
	int roll;

	float stateTime;
	public final float ShipAnimationSpeed = 1f/10f;
	private final float RollSwitchTime = 1f/20f;
	private final float BGanimationSpeed =  1f/10f;

	private float rollTimer = 0;
	private float shootTimer = 0;
	private float EnemyBlueSpawnTimer;
	private final float TimeBtwShot = 0.2f;
	private final int BGwidthPixels = 514;
	private final int BGheightPixels = 522 ;

	TextureRegion[] animationFramesBG;
	 Animation BGanimation;
	private final int ShipMovementSpeed = 500;
	ArrayList<Laser> Lasers;
	ArrayList<EnemyBlue> EnemiesBlue;
	ArrayList<Explosion> Explosions;
	public static final float MIN_ENEMY_SPAWN_TIME = 0.4f;
	public static final float MAX_ENEMY_SPAWN_TIME = 0.6f;
	Random random;
	Collision SpaceShipCollision;
	int SpaceshipHealth = 10;
	Texture pixel;
	public Music spaceMusic;


	public MyGame(final AmongStars game) {
		random = new Random();
		EnemyBlueSpawnTimer = random.nextFloat() * (MAX_ENEMY_SPAWN_TIME - MIN_ENEMY_SPAWN_TIME) + MIN_ENEMY_SPAWN_TIME;
		this.game = game;
		Lasers = new ArrayList<Laser>();
		EnemiesBlue = new ArrayList<EnemyBlue>();
		Explosions = new ArrayList<Explosion>();
		pixel = new Texture("square.png");

		spaceMusic = Gdx.audio.newMusic(Gdx.files.internal("GameMusic.mp3"));
		shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

		spaceMusic.setVolume(0.1f);
		spaceMusic.setLooping(true);
		spaceMusic.play();


		camera = new OrthographicCamera();
		camera.setToOrtho(false, AmongStars.minWidth, AmongStars.minHeight);
		batch = new SpriteBatch();

		rolls = new Animation[5];
		roll = 2;
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship_1.png"), spaceshipHeightPixels, spaceshipWidthPixels);
		rolls[0] = new Animation(ShipAnimationSpeed, rollSpriteSheet[2]);
		rolls[1] = new Animation(ShipAnimationSpeed, rollSpriteSheet[1]);
		rolls[2] = new Animation(ShipAnimationSpeed, rollSpriteSheet[0]);
		rolls[3] = new Animation(ShipAnimationSpeed, rollSpriteSheet[3]);
		rolls[4] = new Animation(ShipAnimationSpeed, rollSpriteSheet[4]);

		spaceship = new Rectangle();
		spaceship.width = 56;
		spaceship.height = 56;
		spaceship.x = AmongStars.minWidth / 2 - spaceship.width / 2;
		spaceship.y = 20;
		SpaceShipCollision = new Collision((float) 0, (float) 0, (int) spaceship.width, (int) spaceship.height);
		TextureRegion[][] BgFrames = TextureRegion.split(new Texture("Game_BGround.png"), BGwidthPixels, BGheightPixels);
		animationFramesBG = new TextureRegion[9];
		int index = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 1; j++) {
				animationFramesBG[index++] = BgFrames[j][i];


			}
		}

		BGanimation = new Animation(BGanimationSpeed, animationFramesBG);

	}




	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		stateTime += delta;
		shootTimer += delta;
		game.batch.begin();
		game.batch.draw((TextureRegion) BGanimation.getKeyFrame(stateTime,true),0,0,AmongStars.minWidth,AmongStars.minHeight);
		game.font.draw(game.batch, "Enemies killed: " + count, 0, 470);
		game.font.draw(game.batch, "HP", 0, 0);
		game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime, true), spaceship.x, spaceship.y, spaceship.width, spaceship.height);

		for (Laser laser: Lasers) {
			laser.render(game.batch);
		}
		for (EnemyBlue enemiesBlue: EnemiesBlue) {
			enemiesBlue.render(game.batch);

		}
		for (Explosion explosion: Explosions) {
			explosion.render(game.batch);
		}
		game.batch.draw(pixel, (float)0, (float)0, (float)(Gdx.graphics.getWidth() * SpaceshipHealth * 0.1), (float)15);
		game.font.draw(game.batch, "HP", 5, 13);
		game.batch.end();
		if (SpaceshipHealth == 0) {
			spaceMusic.pause();
			game.setScreen(new GameOverScreen(game));
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE) && shootTimer >= TimeBtwShot) {
			shootTimer = 0;
			Lasers.add(new Laser(spaceship.x+18, spaceship.y+55));
			shotSound.play(0.1f);
		}




		ArrayList<EnemyBlue> EnemiesBlueRemove = new ArrayList<EnemyBlue>();
		for (EnemyBlue enemyblue: EnemiesBlue) {
			enemyblue.update();
			if (enemyblue.remove == true) {
				EnemiesBlueRemove.add(enemyblue);

			}
		}
			EnemiesBlue.removeAll(EnemiesBlueRemove);
		ArrayList<Laser> LasersRemove = new ArrayList<Laser>();
		for (Laser laser: Lasers){
			laser.update();
			if (laser.remove == true) {
				LasersRemove.add(laser);
			}
		}
		Lasers.removeAll(LasersRemove);


		ArrayList<Explosion> ExplsoinsRemove = new ArrayList<Explosion>();
		for (Explosion explosion: Explosions) {
			explosion.update();
			if (explosion.remove == true) {
				ExplsoinsRemove.add(explosion);
			}
		}

		Explosions.removeAll(ExplsoinsRemove);


		if(Gdx.input.isKeyJustPressed(Keys.LEFT) && !Gdx.input.isKeyJustPressed(Keys.RIGHT) && roll > 0) {
			rollTimer = 0;
			roll--;
		}
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT) && !Gdx.input.isKeyJustPressed(Keys.LEFT) && roll < 4) {
			rollTimer = 0;
			roll++;
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			spaceship.y += ShipMovementSpeed * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			spaceship.y -= ShipMovementSpeed * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			spaceship.x -= ShipMovementSpeed * Gdx.graphics.getDeltaTime();
			rollTimer -= delta;
			if (Math.abs(rollTimer) > RollSwitchTime) {
				rollTimer = 0;
				roll--;
				if (roll < 0) {
					roll = 0;
				}
			}
		} else {
			if (roll < 2) {
				rollTimer += delta;
				if (Math.abs(rollTimer) > RollSwitchTime) {
					rollTimer = 0;
					roll++;
					if (roll > 4) {
						roll = 4;
					}
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			spaceship.x += ShipMovementSpeed * Gdx.graphics.getDeltaTime();
			rollTimer += delta;
			if (Math.abs(rollTimer) > RollSwitchTime) {
				rollTimer = 0;
				roll++;
				if (roll > 4) {
					roll = 4;
				}
			}

		} else {
			if (roll > 2) {
				rollTimer -= delta;
				if (Math.abs(rollTimer) > RollSwitchTime) {
					rollTimer = 0;
					roll--;
					if (roll < 0) {
						roll = 0;
					}
				}
			}
		}

		SpaceShipCollision.move(spaceship.x, spaceship.y);
		if (spaceship.y < 0) spaceship.y = 0;
		if (spaceship.y > AmongStars.minHeight - spaceship.height) spaceship.y = AmongStars.minHeight - spaceship.height;
		if (spaceship.x < 0) spaceship.x = 0;
		if (spaceship.x > AmongStars.minWidth - spaceship.width) spaceship.x = AmongStars.minWidth - spaceship.width;
		EnemyBlueSpawnTimer -= delta;
		if (EnemyBlueSpawnTimer <= 0) {
			EnemyBlueSpawnTimer = random.nextFloat() * (MAX_ENEMY_SPAWN_TIME - MIN_ENEMY_SPAWN_TIME) + MIN_ENEMY_SPAWN_TIME;
			EnemiesBlue.add(new EnemyBlue());
		}
		for (Laser laser: Lasers) {
			for (EnemyBlue enemyblue: EnemiesBlue) {
				if (laser.getCollision().collides(enemyblue.getCollision())) {
					LasersRemove.add(laser);
					EnemiesBlueRemove.add(enemyblue);
					Explosions.add(new Explosion(enemyblue.getX(), enemyblue.getY()));
					count++;
					explosionSound.play(0.5f);
				}
			}
		}
		for (EnemyBlue enemyblue: EnemiesBlue) {
			if (enemyblue.getCollision().collides(SpaceShipCollision)) {
				EnemiesBlueRemove.add(enemyblue);
				SpaceshipHealth -= 1;
				explosionSound.play(0.5f);
				Explosions.add(new Explosion(enemyblue.getX(), enemyblue.getY()));
				count++;
			}
		}
		for (EnemyBlue enemyblue: EnemiesBlue) {
			if (enemyblue.getY() + EnemyBlue.Height - 2 < 0) {
				SpaceshipHealth -= 1;
			}


		}
		EnemiesBlue.removeAll(EnemiesBlueRemove);
		Lasers.removeAll(LasersRemove);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}


	@Override
	public void dispose() {
		spaceMusic.dispose();
	}
}