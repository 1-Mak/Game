package com.my.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGame implements Screen {
	private final Texture enemyImage;
	private Texture laserImage;
	private Texture spaceshipImage;
	private Sound shotSound;
	private Sound explosionSound;
	private Music spaceMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle spaceship;
	private Array<Rectangle> enemies;
	private Array<Rectangle> lasershots;
	private long lastEnemySpawnTime;
	private long lastShotTime;
	private int score;
	final AmongStars game;
	int count;
	private final int spaceshipWidthPixels = 24;
	private final int spaceshipHeightPixels = 18;

	Animation[] rolls;
	int roll;

	float stateTime;
	public final float ShipAnimationSpeed = 1f/10f;
	private final float RollSwitchTime = 1f/20f;
	private final float BGanimationSpeed =  1f/12f;
	private final float ExpAnimationSpeed =  1f/4f;
	private float rollTimer = 0;
	private final int BGwidthPixels = 514;
	private final int BGheightPixels = 522 ;

	TextureRegion[] animationFramesBG;
	TextureRegion[] animationFramesExp;
	 Animation BGanimation;
	 Animation ExpAnimation;
	private final int ShipMovementSpeed = 500;
	private final int ExplosionWidth = 64;
	private final int ExplosionHeight = 64;

	public MyGame(final AmongStars game) {
		this.game = game;

		enemyImage = new Texture(Gdx.files.internal("enemy.png"));
		laserImage = new Texture(Gdx.files.internal("laser.png"));


		spaceMusic = Gdx.audio.newMusic(Gdx.files.internal("music.wav"));
		shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));


//		spaceMusic.setLooping(true);
//		spaceMusic.play();


		camera = new OrthographicCamera();
		camera.setToOrtho(false, AmongStars.minWidth,AmongStars.minHeight );
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
		spaceship.width = 64;
		spaceship.height = 72;
		spaceship.x = AmongStars.minWidth / 2 - spaceship.width / 2;
		spaceship.y = 20;
		enemies = new Array<Rectangle>();
		spawnEnemy();
		lasershots = new Array<Rectangle>();
		spawnlaser();


		TextureRegion[][] BgFrames = TextureRegion.split(new Texture("Game_BGround.png"),BGwidthPixels,BGheightPixels);
		animationFramesBG = new TextureRegion[9];
		int index = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 1; j++) {
				animationFramesBG[index++] = BgFrames[j][i];


			}
		}

		BGanimation = new Animation(BGanimationSpeed, animationFramesBG);

		TextureRegion[][] ExpFrames = TextureRegion.split(new Texture("Boom.png"),32,32);
		animationFramesExp = new TextureRegion[14];
		int index0 = 0;
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 1; j++) {
				animationFramesExp[index0++] = ExpFrames[j][i];


			}
		}
		ExpAnimation = new Animation(ExpAnimationSpeed, animationFramesExp);

	}

	private void spawnlaser() {
		Rectangle shot = new Rectangle();
		shot.x = spaceship.x+18;
		shot.y = spaceship.y+55;
		shot.width = 32;
		shot.height = 32;
		lasershots.add(shot);
		lastShotTime = TimeUtils.nanoTime();
	}

	private void spawnEnemy() {
		Rectangle enemy = new Rectangle();
		enemy.x = MathUtils.random(0, AmongStars.minWidth-64);
		enemy.y = AmongStars.minHeight;
		enemy.width = 88;
		enemy.height = 54;
		enemies.add(enemy);
		lastEnemySpawnTime = TimeUtils.nanoTime();
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		stateTime +=delta;
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw((TextureRegion) BGanimation.getKeyFrame(stateTime,true),0,0,AmongStars.minWidth,AmongStars.minHeight);
		game.font.draw(game.batch, "Enemies killed: " + count, 0, 470);
		game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime, true), spaceship.x, spaceship.y, spaceship.width, spaceship.height);
		for (Rectangle enemy : enemies) {
			game.batch.draw(enemyImage, enemy.x, enemy.y,enemy.width,enemy.height);
		}
		for (Rectangle shot : lasershots) {
			game.batch.draw(laserImage, shot.x, shot.y,shot.width,shot.height);
		}
		for (Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext(); ) {
			Rectangle enemy = iter.next();
			enemy.y -= 150 * Gdx.graphics.getDeltaTime();
			if (enemy.y + 64 < 0) {
				iter.remove();

			}



			for (Iterator<Rectangle> iten = lasershots.iterator(); iten.hasNext(); ) {
				Rectangle shot = iten.next();
				shot.y += 200 * Gdx.graphics.getDeltaTime();
				if (shot.y > AmongStars.minWidth) iten.remove();
				if (shot.overlaps(enemy)) {
					count++;
					explosionSound.play(0.5f);
					iter.remove();
					iten.remove();
					game.batch.draw(laserImage,enemy.x,enemy.y,88,54);
				}
			}

		}
		game.batch.end();

		if(Gdx.input.isKeyJustPressed(Keys.LEFT) && !Gdx.input.isKeyJustPressed(Keys.RIGHT) && roll > 0) {
			rollTimer = 0;
			roll--;
		}
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT) && !Gdx.input.isKeyJustPressed(Keys.LEFT) && roll < 4) {
			rollTimer = 0;
			roll++;
		}


		if (Gdx.input.isKeyPressed(Keys.UP)) spaceship.y += ShipMovementSpeed * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.DOWN)) spaceship.y -= ShipMovementSpeed * Gdx.graphics.getDeltaTime();

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

		if (TimeUtils.nanoTime() - lastShotTime > 700000000) shotSound.play(0.1f);


		if (spaceship.y < 0) spaceship.y = 0;
		if (spaceship.y > AmongStars.minHeight - spaceship.height) spaceship.y = AmongStars.minHeight - spaceship.height;
		if (spaceship.x < 0) spaceship.x = 0;
		if (spaceship.x > AmongStars.minWidth - spaceship.width) spaceship.x = AmongStars.minWidth - spaceship.width;
		if (TimeUtils.nanoTime() - lastEnemySpawnTime > 1000000000) spawnEnemy();
		if (TimeUtils.nanoTime() - lastShotTime > 700000000) spawnlaser();



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

		enemyImage.dispose();
		spaceshipImage.dispose();
		spaceMusic.dispose();
		batch.dispose();
	}
}