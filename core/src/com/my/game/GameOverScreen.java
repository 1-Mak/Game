package com.my.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameOverScreen implements Screen {


    private final int RestartButtonWidth = 350;
    private final int RestartButtonHeight = 75;
    private final int GameOverLogoHeight = 75;
    private final int GameOverLogoWidth = 550;
    private final int RestartButtonY = 200;
    private final int backgroundWidth = 800;
    private final int backgroundHeight = 480;
    private final float BGanimationSpeed = 1f/10f;
    private final int BGwidthPixels = 400;
    private final int BGheightPixels = 400;
    private final int exitButtonY = 100;
    private final int GameOverLogoY = 300;
    private int exitButtonWidth = 200;
    private int exitButtonHeight = 75;
    final AmongStars game;
    OrthographicCamera camera;

    Texture RestartButtonActive;
    Texture RestartButtonInactive;
    Texture ExitButtonActive;
    Texture ExitButtonInactive;
    Texture GameOverLogo;
    Animation animation;
    TextureRegion[] animationFrames;
    float stateTime;
    int change;
    public Music GameOverMusic;
    public GameOverScreen(final AmongStars game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800, 480);
        RestartButtonActive = new Texture(Gdx.files.internal("RESTART_ACTIVE.png"));
        RestartButtonInactive = new Texture(Gdx.files.internal("RESTART_INACTIVE.png"));
        ExitButtonActive = new Texture(Gdx.files.internal("exit_button_active.png"));
        ExitButtonInactive = new Texture(Gdx.files.internal("exit_button_inactive.png"));
        GameOverLogo = new Texture(Gdx.files.internal("GAMEOVERLOGO1.png"));
        GameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameOverMusic.mp3"));
        GameOverMusic.setVolume(0.1f);
        GameOverMusic.setLooping(true);
        GameOverMusic.play();


        TextureRegion[][] tmpFrames = TextureRegion.split(new Texture("gameOverbackground.png"),BGwidthPixels,BGheightPixels);
        animationFrames = new TextureRegion[24];
        int index = 0;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 1; j++) {
                animationFrames[index++] = tmpFrames[j][i];


            }
        }
        animation = new Animation(BGanimationSpeed, animationFrames);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        int RestartButtonX = AmongStars.minWidth / 2 - RestartButtonWidth / 2;
        int exitButtonX = AmongStars.minWidth / 2 - exitButtonWidth / 2;
        int GameOverLogoX = AmongStars.minWidth / 2 - GameOverLogoWidth / 2;
        stateTime += delta;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        //game.batch.draw(bgTexture,0,0,backgroundWidth,backgroundHeight);
        game.batch.draw((TextureRegion) animation.getKeyFrame(stateTime,true),0,0,backgroundWidth,backgroundHeight);
        game.batch.draw(GameOverLogo, GameOverLogoX, GameOverLogoY, GameOverLogoWidth, GameOverLogoHeight);
        if (Gdx.input.getX() < RestartButtonX + RestartButtonWidth && Gdx.input.getX() > RestartButtonX && AmongStars.minHeight - Gdx.input.getY() < RestartButtonY + RestartButtonHeight && AmongStars.minHeight - Gdx.input.getY() > RestartButtonY) {
            game.batch.draw(RestartButtonActive, RestartButtonX, RestartButtonY, RestartButtonWidth, RestartButtonHeight);
            if(Gdx.input.isTouched()){
                GameOverMusic.pause();
                game.setScreen(new MyGame(game));
                dispose();
            }
        } else {
            game.batch.draw(RestartButtonInactive, RestartButtonX, RestartButtonY, RestartButtonWidth, RestartButtonHeight);
        }
        if (Gdx.input.getX() < exitButtonX + exitButtonWidth && Gdx.input.getX() > exitButtonX && AmongStars.minHeight - Gdx.input.getY() < exitButtonY + exitButtonHeight && AmongStars.minHeight - Gdx.input.getY() > exitButtonY) {
            game.batch.draw(ExitButtonActive, exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
            if(Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        } else {
            game.batch.draw(ExitButtonInactive, exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
        }
        game.batch.end();

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

    }
}
