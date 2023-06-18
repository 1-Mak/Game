package com.my.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class EnemyBlue {
    public static final int Speed = 100;
    public static final int Width = 96;
    public static final int Height = 96;
    public float X,Y;
    Collision rectangle;
    public static com.badlogic.gdx.graphics.Texture Texture;
    public boolean remove = false;

    public EnemyBlue() {
        this.X = MathUtils.random(0, AmongStars.minWidth-Width);
        this.Y = Gdx.graphics.getHeight();
        this.rectangle = new Collision(X,Y,Width,Height);
        if (Texture == null) {
            Texture = new Texture("EnemyBlue.png");
        }

    }

    public void update() {
        Y -= Speed * Gdx.graphics.getDeltaTime();
        if (Y < 0-Height) {
            remove = true;
        }
        rectangle.move(X,Y);

    }
    public float getX () {
        return X;
    }
    public float getY() {
        return Y;
    }
    public void render(SpriteBatch batch) {
        batch.draw(Texture, X, Y, Width, Height);
    }
    public Collision getCollision () {
        return rectangle;


    }
}