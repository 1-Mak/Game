package com.my.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {

    public static final float AnimationSpeed = 0.05f;
    public static final int OFFSET = 8;
    public static final int Width = 96;
    public static final int Height = 64;
    public static final int WidthPixels = 32;
    public static final int HeightPixels = 32;


    private static Animation animation = null;
    float X, Y;
    float stateTime;

    public boolean remove = false;

    public Explosion (float X, float Y) {
        this.X = X - OFFSET;
        this.Y = Y - OFFSET;
        stateTime = 0;

        if (animation == null)
            animation = new Animation(AnimationSpeed, TextureRegion.split(new Texture("Boom.png"), WidthPixels, HeightPixels)[0]);
    }

    public void update () {
        stateTime += Gdx.graphics.getDeltaTime();
        if (animation.isAnimationFinished(stateTime))
            remove = true;
    }

    public void render (SpriteBatch batch) {
        batch.draw((TextureRegion) animation.getKeyFrame(stateTime), X, Y, Width, Height);
    }

}
