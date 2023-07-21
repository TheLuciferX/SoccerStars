package com.example.soccerstars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Button extends Sprite {
    private String text;
    private int colour;
    public Button(float x, float y, float width, float height) {
        this(x, y, width, height, "", null, false);
    }
    public Button(float x, float y, float width, float height, String text) {
        this(x, y, width, height, text, null, false);
    }
    public Button(float x, float y, float width, float height, String text, Bitmap bit, boolean b) {
        super(x, y, width, height, bit);
        this.text = text;
        this.colour = 0;
        if(bit != null) {
            scaleDownBitmap(b);
        }
    }
    public void draw(int colour, float radius, Canvas c) {
        this.draw(Color.argb(200, 0, 0, 0), Color.BLACK, c);
        Paint p = new Paint();
        p.setColor(colour);
        c.drawCircle(x+(width/2f), y+(height/2f), radius, p);
        this.colour = colour;
    }
    public void draw(int colour1, int colour2, Canvas c) {
        super.draw(colour1, c);
        Paint p = new Paint();
        p.setColor(colour2);
        p.setTextSize(100);
        p.setTextAlign(Paint.Align.CENTER);

        scaleDownText(p);

        float xPos = this.x + (this.width / 2f);
        float yPos = this.y + ((this.height / 2f) - ((p.descent() + p.ascent()) / 2f));

        c.drawText(this.text, xPos, yPos, p);
    }
    public int getColour() {
        return colour;
    }
    public void drawBorder(int colour, Canvas c) {
        Paint p = new Paint();
        p.setColor(colour);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(20);
        c.drawRect(x, y, x + width, y + height, p);
    }
    public void drawBorder(int colour1, int colour2, Canvas c) {
        Paint p = new Paint();
        p.setColor(colour1);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        c.drawRect(x, y, x + width, y + height, p);
        p.setColor(colour2);
        c.drawRect(x + 10, y + 10, x + width - 10, y + height - 10, p);
    }
    private void scaleDownText(Paint p) {
        while(p.measureText(text) > width) {
            p.setTextSize(p.getTextSize() - 10);
        }
    }
    private void scaleDownBitmap(boolean b) {
        int width = bit.getWidth();
        int height = bit.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = b ? 1 : this.width / this.height;

        int finalWidth = (int)(b ? (Math.min(this.width, this.height)) : this.width);
        int finalHeight = (int)(b ? (Math.min(this.width, this.height)) : this.height);
        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((b ? (Math.min(this.width, this.height)) : this.width) * ratioBitmap);
        } else {
            finalHeight = (int) ((b ? (Math.min(this.width, this.height)) : this.height) / ratioBitmap);
        }
        bit = Bitmap.createScaledBitmap(bit, finalWidth, finalHeight, true);
    }
    public String getText() {
        return text;
    }
}
