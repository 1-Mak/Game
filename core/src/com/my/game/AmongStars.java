package com.my.game;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class AmongStars extends Game {
    SpriteBatch batch;
    BitmapFont font;
    SpriteBatch spriteBatch;
    public static final int minWidth = 800;
    public static final int minHeight = 480;
    public static final int maxWidth = 1920;
    public static final int maxHeight = 1080;
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        setScreen(new IntroScreen(this));

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}




