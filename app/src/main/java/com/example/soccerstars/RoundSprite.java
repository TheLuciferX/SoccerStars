package com.example.soccerstars;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Vibrator;

import java.io.IOException;

public class RoundSprite extends Sprite {
    protected float radius;
    public RoundSprite(float x, float y, float radius, Bitmap b) {
        super(x, y, 0, 0, b);
        this.radius = radius;
    }
    @Override
    public boolean contains(float x, float y) {
        return x >= this.x - radius && x <= this.x + radius && y >= this.y - radius && y <= this.y + radius;
    }
    @Override
    public void draw(int colour, Canvas c) {
        Paint p = new Paint();
        p.setColor(colour);

        c.drawCircle(x, y, radius, p);
    }
    @Override
    public int collidesWall() {
        if((this.x - radius < 0 || this.x + radius > Game.width) && (this.y - radius < 0 || this.y + radius > Game.height)) return 3;
        if(this.x - radius < 0 || this.x + radius > Game.width) return 1;
        if(this.y - radius < 0 || this.y + radius > Game.height) return 2;
        return 0;
    }
    public Vector2d collidesBorder(Vector2d speed, float x, float y) {
        double horPost = Values.getHorPosts();
        double verPost = Values.getVerPosts();
        double goalDepth = Values.getGoalDepth();
        double goalPost = Values.getGoalPosts();

        if(this.x > horPost && this.x < Game.width - horPost) {
            if(this.y - verPost < radius) {
                this.y = (float)(verPost + radius);
                return new Vector2d(this.x, verPost);
            } else if(Game.height - verPost - this.y < radius) {
                this.y = (float)(Game.height - verPost - radius);
                return new Vector2d(this.x, Game.height - verPost);
            }
        } else {
            if(this.x - goalDepth < radius) {
                this.x = (float)(goalDepth + radius);
                return new Vector2d(goalDepth, this.y);
            } else if(Game.width - goalDepth - this.x < radius) {
                this.x = (float)(Game.width - goalDepth - radius);
                return new Vector2d(Game.width - goalDepth, this.y);
            }
        }

        double testX = x > horPost ? horPost : x;
        double testY = y > goalPost ? goalPost : y;
        double distX = x - testX;
        double distY = y - testY;
        if(distX*distX + distY*distY >= radius*radius) {
            testX = x < Game.width - horPost ? Game.width - horPost : x;
            testY = y > goalPost ? goalPost : y;
            distX = testX - x;
            distY = y - testY;
        }
        if(distX*distX + distY*distY >= radius*radius) {
            testX = x > horPost ? horPost : x;
            testY = y < Game.height - goalPost ? Game.height - goalPost : y;
            distX = x - testX;
            distY = testY - y;
        }
        if(distX*distX + distY*distY >= radius*radius) {
            testX = x < Game.width - horPost ? Game.width - horPost : x;
            testY = y < Game.height - goalPost ? Game.height - goalPost : y;
            distX = testX - x;
            distY = testY - y;
        }
        if(distX*distX + distY*distY < radius*radius) {
            if((testX == horPost && testY == goalPost) || (testX == Game.width - horPost && testY == goalPost) || (testX == horPost && testY == Game.height - goalPost) || (testX == Game.width - horPost && testY == Game.height - goalPost)) {

                Vector2d mySpeed = this instanceof Pack ? ((Pack) this).getSpeed() : ((Ball) this).getSpeed();
                Vector2d opSpeed = new Vector2d(0, 0);

                double xDist = testX == horPost ? this.x - (testX - 40) : this.x - (testX + 40);
                double yDist = testY == goalPost ? this.y - (testY - 40) : this.y - (testY + 40);
                double distSquared = xDist * xDist + yDist * yDist;

                double xVelocity = opSpeed.x - mySpeed.x;
                double yVelocity = opSpeed.y - mySpeed.y;
                double dotProduct = xDist * xVelocity + yDist * yVelocity;
                if (dotProduct > 0) {
                    double collisionScale = dotProduct / distSquared;
                    double xCollision = xDist * collisionScale;
                    double yCollision = yDist * collisionScale;
                    double combinedMass = 2*this.radius * 2*this.radius + 6*radius * 6*radius;
                    double collisionWeightA = 2* 6*radius * 6*radius / combinedMass;

                    float distance = (float)Math.sqrt(Math.pow(speed.x, 2) + Math.pow(speed.y, 2));
                    Vector2d singleVect = new Vector2d(speed.x / distance, speed.y / distance);
                    for(int i = 0; i < Math.ceil(distance); i++) {
                        this.x -= singleVect.x;
                        this.y -= singleVect.y;
                        distX = testX - this.x;
                        distY = testY - this.y;
                        if(distX*distX + distY*distY >= radius*radius) break;
                    }
                    mySpeed.x += collisionWeightA * xCollision;
                    mySpeed.y += collisionWeightA * yCollision;
                }
                return null;
            }
            if(testX == horPost)
                this.x = (float)(horPost + radius);
            else if(testX == Game.width - horPost)
                this.x = (float)(Game.width - horPost - radius);

            if(testY == goalPost)
                this.y = (float)(goalPost + radius);
            else if(testY == Game.height - goalPost)
                this.y = (float)(Game.height - goalPost - radius);
            return new Vector2d(testX, testY);
        }
        return null;
    }
    public boolean colliding(RoundSprite rs) {
        return (this.x - rs.x)*(this.x - rs.x) + (this.y - rs.y)*(this.y - rs.y) <= (this.radius + rs.radius)*(this.radius + rs.radius);
    }
    public RoundSprite getColliding(Team myTeam) {
        for(RoundSprite rs : myTeam.getPacks()) {
            if(rs == this) continue;
            if(this.colliding(rs)) {
                return rs;
            }
        }
        return null;
    }
    public RoundSprite isColliding(Ball ball) {
        if(this.colliding(ball)) return ball;
        return null;
    }
    public void move(Vector2d speed, double friction, Team myTeam, Team opTeam, Ball ball, Context context) {
        getSpot(speed, myTeam, opTeam, ball, context);
        speed.multiply(friction);
    }
    public Vector2d getSpot(Vector2d speed, Team myTeam, Team opTeam, Ball ball, Context context) {
        float distance = (float)Math.sqrt(Math.pow(speed.x, 2) + Math.pow(speed.y, 2));
        Vector2d singleVect = new Vector2d(speed.x / distance, speed.y / distance);
        Vector2d newSpeed = new Vector2d(0, 0);
        if(distance < 0.4) {
            speed.setZero();
            return newSpeed;
        }
        for(int i = 0; i < Math.ceil(distance); i++) {
            if(i > distance) {
                float newDistance = i - distance;
                singleVect.multiply(newDistance);
            }
            this.x += singleVect.x;
            this.y += singleVect.y;
            if(this instanceof Ball) {
                if(this.y > Values.getGoalPosts() && this.y < Game.height - Values.getGoalPosts()) {
                    if(this.x <= Values.getHorPosts()) {
                        opTeam.setScore(opTeam.getScore() + 1);
                        reset(myTeam, opTeam, ball, context);
                        Game.turn = 1;
                        break;
                    } else if(this.x >= Game.width - Values.getHorPosts()) {
                        myTeam.setScore(myTeam.getScore() + 1);
                        reset(myTeam, opTeam, ball, context);
                        Game.turn = 0;
                        break;
                    }
                }
            }
            int collidingWall = collidesWall();
            if(collidingWall != 0) {
                if (collidingWall == 3 || collidingWall == 2) {
                    speed.y = -speed.y;
                    if (y - radius < 0) this.y = radius;
                    else this.y = Game.height - radius;
                }
                if (collidingWall == 3 || collidingWall == 1) {
                    speed.x = -speed.x;
                    if (x - radius < 0) this.x = radius;
                    else this.x = Game.width - radius;
                }
                break;
            }
            Vector2d collidingBorder = collidesBorder(speed, this.x, this.y);
            if(collidingBorder != null) {
                float oldX = this.x;
                float oldY = this.y;
                if(collidingBorder.x != oldX) {
                    speed.x = -speed.x;
                }
                if(collidingBorder.y != oldY) {
                    speed.y = -speed.y;
                }
                break;
            }
            RoundSprite collidingSp = getColliding(myTeam);
            if(collidingSp == null)
                collidingSp = getColliding(opTeam);
            if(collidingSp == null && !(this instanceof Ball))
                collidingSp = isColliding(ball);
            if (collidingSp != null) {
                Vector2d mySpeed = this instanceof Pack ? ((Pack) this).getSpeed() : ((Ball) this).getSpeed();
                Vector2d opSpeed = collidingSp instanceof Pack ? ((Pack) collidingSp).getSpeed() : ((Ball) collidingSp).getSpeed();
                double xDist = this.x - collidingSp.x;
                double yDist = this.y - collidingSp.y;
                double distSquared = xDist * xDist + yDist * yDist;
                double xVelocity = opSpeed.x - mySpeed.x;
                double yVelocity = opSpeed.y - mySpeed.y;
                double dotProduct = xDist * xVelocity + yDist * yVelocity;
                if (dotProduct > 0) { // moving towards each other
                    double collisionScale = dotProduct / distSquared;
                    double xCollision = xDist * collisionScale;
                    double yCollision = yDist * collisionScale;
                    //The Collision vector is the speed difference projected on the Dist vector,
                    //thus it is the component of the speed difference needed for the collision.
                    double combinedMass = 2*this.radius * 2*this.radius + 2*collidingSp.radius * 2*collidingSp.radius; // mass porp. to area
                    double collisionWeightA = 2* 2*collidingSp.radius * 2*collidingSp.radius / combinedMass;
                    double collisionWeightB = 2* 2*this.radius * 2*this.radius / combinedMass;
                    mySpeed.x += collisionWeightA * xCollision;
                    mySpeed.y += collisionWeightA * yCollision;
                    opSpeed.x -= collisionWeightB * xCollision;
                    opSpeed.y -= collisionWeightB * yCollision;
                }

            }
        }
        return newSpeed;
    }
    private void reset(Team myTeam, Team opTeam, Ball b, Context context) {
        SharedPreferences sp = context.getSharedPreferences("customs", 0);
        try {
            if(sp.getBoolean("vibration", true)) {
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000);
            }
            if(sp.getBoolean("sound", true)) {
                AssetFileDescriptor afd = context.getAssets().openFd("sounds/whistle.mp3");
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                player.prepare();
                player.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        myTeam.reset();
        opTeam.reset();
        b.reset();
        if(myTeam.getScore() == 5) {
            MainActivity.end(context, true);
        } else if(opTeam.getScore() == 5) {
            MainActivity.end(context, false);
        }
    }
}
