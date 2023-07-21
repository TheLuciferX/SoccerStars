package com.example.soccerstars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Sprite {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected Bitmap bit;

    public Sprite(float x, float y, float width, float height, Bitmap bit) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bit = bit;
    }
    public boolean contains(float x, float y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y +this.height;
    }
    public void draw(int colour, Canvas c) {
        Paint p = new Paint();
        p.setColor(colour);
        if(bit != null) {
            c.drawRect(x, y, x + width, y + height, p);
            p.setColor(Color.BLACK);
            float startX = this.width == bit.getWidth() ? x : x + (this.width / 2f - bit.getWidth() / 2f);
            float startY = this.height == bit.getHeight() ? y : y + (this.height / 2f - bit.getHeight() / 2f);
            c.drawBitmap(bit, startX, startY, p);
        } else
            c.drawRect(x, y, x + width, y + height, p);
    }
    public int collidesWall() {
        if((this.x < 0 || this.x + this.width > Game.width) && (this.y < 0 || this.y + this.height > Game.height)) return 3;
        if(this.x < 0 || this.x + this.width > Game.width) return 1;
        if(this.y < 0 || this.y + this.height > Game.height) return 2;
        return 0;
    }
}
