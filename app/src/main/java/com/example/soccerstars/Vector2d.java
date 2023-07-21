package com.example.soccerstars;

public class Vector2d {
    public double x;
    public double y;

    public Vector2d() { }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void setZero() {
        x = 0;
        y = 0;
    }
    public void multiply(double scalar) {
        x *= scalar;
        y *= scalar;
    }
    public void divide(double scalar) {
        x /= scalar;
        y /= scalar;
    }
    @Override
    public Vector2d clone() {
        return new Vector2d(x, y);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vector2d) {
            Vector2d v = (Vector2d) obj;
            return (x == v.x) && (y == v.y);
        }
        return false;
    }
    @Override
    public String toString() {
        return "Vector2d[" + x + ", " + y + "]";
    }
}