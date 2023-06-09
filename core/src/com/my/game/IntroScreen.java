package com.my.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IntroScreen implements Screen {
        private final int playButtonWidth = 200;
        private final int playButtonHeight = 75;
        private final int LogoHeight = 75;
        private final int LogoWidth = 550;
        private final int playButtonY = 200;
        private final int backgroundWidth = 800;
        private final int backgroundHeight = 480;
        private final float BGanimationSpeed = 1f/10f;
        private final int BGwidthPixels = 690;
        private final int BGheightPixels = 388;
        private final int exitButtonY = 100;
        private final int LogoY = 300;
        private int exitButtonWidth = 200;
        private int exitButtonHeight = 75;
        final AmongStars game;
        OrthographicCamera camera;
//        Texture bgTexture;
        Texture PlayButtonActive;
        Texture PlayButtonInactive;
        Texture ExitButtonActive;
        Texture ExitButtonInactive;
        Texture Logo;
        Animation animation;
        TextureRegion[] animationFrames;
        float stateTime;
        int change;
        public IntroScreen(final AmongStars game) {
            this.game = game;
            camera = new OrthographicCamera();
            camera.setToOrtho(false,800, 480);
            PlayButtonActive = new Texture(Gdx.files.internal("play_button_active.png"));
            PlayButtonInactive = new Texture(Gdx.files.internal("play_button_inactive.png"));
            ExitButtonActive = new Texture(Gdx.files.internal("exit_button_active.png"));
            ExitButtonInactive = new Texture(Gdx.files.internal("exit_button_inactive.png"));
            Logo = new Texture(Gdx.files.internal("Logo.png"));
//            bgTexture = new Texture(Gdx.files.internal("Main_menu_bground.png"));


            TextureRegion[][] tmpFrames = TextureRegion.split(new Texture("BackgroundFinal.png"),BGwidthPixels,BGheightPixels);
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
            int playButtonX = AmongStars.minWidth / 2 - playButtonWidth / 2;
            int exitButtonX = AmongStars.minWidth / 2 - exitButtonWidth / 2;
            int LogoX = AmongStars.minWidth / 2 - LogoWidth / 2;
            stateTime += delta;
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            camera.update();
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();

            //game.batch.draw(bgTexture,0,0,backgroundWidth,backgroundHeight);
          game.batch.draw((TextureRegion) animation.getKeyFrame(stateTime,true),0,0,backgroundWidth,backgroundHeight);
            game.batch.draw(Logo, LogoX, LogoY, LogoWidth, LogoHeight);
            if (Gdx.input.getX() < playButtonX + playButtonWidth && Gdx.input.getX() > playButtonX && AmongStars.minHeight - Gdx.input.getY() < playButtonY + playButtonHeight && AmongStars.minHeight - Gdx.input.getY() > playButtonY) {
                game.batch.draw(PlayButtonActive, playButtonX, playButtonY, playButtonWidth, playButtonHeight);
                if(Gdx.input.isTouched()){
                    game.setScreen(new MyGame(game));
                    dispose();
                }
            } else {
                game.batch.draw(PlayButtonInactive, playButtonX, playButtonY, playButtonWidth, playButtonHeight);
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

