package com.my.game;

public class Collision {
        float x, y;
        int width, height;

        public Collision (float x, float y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        public boolean collides (Collision rectangle) {
            return x < rectangle.x + rectangle.width && y < rectangle.y + rectangle.height && x + width > rectangle.x && y + height > rectangle.y;
        }

        public void move (float x, float y) {
            this.x = x;
            this.y = y;
        }



    }

