package com.my.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Laser {
    public static final int Speed =  500;
    float X,Y;
    public static final int Width = 32;
    public static final int Height = 32;
    Collision rectangle;
    public static Texture Texture;
    public boolean remove = false;

    public Laser(float X, float Y) {
        this.X = X;
        this.Y = Y;
        this.rectangle = new Collision(X,Y,Width,Height);
        if (Texture == null) {
            Texture = new Texture("laser.png");
        }
    }
    public void update() {
            Y += Speed * Gdx.graphics.getDeltaTime();
            if (Y > Gdx.graphics.getHeight()) {
                remove = true;
            }
            rectangle.move(X,Y);
        }

    public void render(SpriteBatch batch) {
        batch.draw(Texture, X, Y, Width, Height);
    }
    public Collision getCollision () {
        return rectangle;


    }
}
