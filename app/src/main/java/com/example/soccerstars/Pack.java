package com.example.soccerstars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pack extends RoundSprite {
    private Vector2d speed;
    private final double FRICTION = 0.92;
    private final double MASS = 3;

    public Pack(float x, float y, float radius) {
        this(x, y, radius, null);
    }
    public Pack(float x, float y, float radius, Bitmap b) {
        super(x, y, radius, b);
        speed = new Vector2d(0, 0);
    }
    public void hit(Vector2d newSpeed) {
        speed = newSpeed.clone();
    }
    public void move(Team myTeam, Team opTeam, Ball ball, Context context) {
        super.move(this.speed, FRICTION, myTeam, opTeam, ball, context);
    }
    public Vector2d getSpeed() {
        return this.speed;
    }

    public void drawHighlight(Canvas c) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        c.drawCircle(x, y, radius + radius/7f, p);
    }
}
