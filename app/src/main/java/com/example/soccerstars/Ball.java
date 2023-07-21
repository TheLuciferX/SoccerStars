package com.example.soccerstars;

import android.content.Context;
import android.graphics.Bitmap;

public class Ball extends RoundSprite {
    private Vector2d speed;
    private final double FRICTION = 0.92;
    private final double MASS = 2;

    public Ball(float x, float y, float radius) {
        this(x, y, radius, null);
    }
    public Ball(float x, float y, float radius, Bitmap b) {
        super(x, y, radius, b);
        speed = new Vector2d();
    }
    public void move(Team myTeam, Team opTeam, Ball ball, Context context) {
        super.move(this.speed, FRICTION, myTeam, opTeam, ball, context);
    }
    public Vector2d getSpeed() {
        return this.speed;
    }

    public void reset() {
        speed.x = 0;
        speed.y = 0;
        x = Game.width / 2;
        y = Game.height / 2;
    }

    public boolean moving() {
        return speed.x != 0 || speed.y != 0;
    }
}
