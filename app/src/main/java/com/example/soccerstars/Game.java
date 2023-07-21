package com.example.soccerstars;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.soccerstars.enums.Formation;

import java.util.Random;

public class Game extends View {
    public static float width = 0;
    public static float height = 0;
    public static int turn = new Random().nextInt(2);

    SharedPreferences sp;

    DisplayMetrics metrics;
    Bitmap bitmap;
    Bitmap flipped;

    private final double GRAVITY = 5;

    private Team myTeam;
    private Team opTeam;
    private Ball ball;

    private Button btnMenu;
    private boolean pressing = false;

    private boolean inMove = true;

    private boolean dragging = false;
    private float startX = 0, startY = 0, endX = 0, endY = 0;
    private Pack moving;

    int ballColour;

    private Context con;
    private boolean started = false;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            myTeam.move(myTeam, opTeam, ball, con);
            opTeam.move(myTeam, opTeam, ball, con);
            ball.move(myTeam, opTeam, ball, con);
            invalidate();
            if(!myTeam.moving() && !opTeam.moving() && !ball.moving()) {
                if(inMove) {
                    inMove = false;
                    if(turn == 0) {
                        myTeam.highlight(false);
                        opTeam.highlight(true);
                        turn = 1;
                    } else {
                        myTeam.highlight(true);
                        opTeam.highlight(false);
                        turn = 0;
                    }
                }
            } else {
                inMove = true;
                myTeam.highlight(false);
                opTeam.highlight(false);
            }
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(runnable, 1000/60);
        }
    };
    public Game(Context context) {
        super(context);
        this.con = context;
        turn = new Random().nextInt(2);

        metrics = this.getResources().getDisplayMetrics();
        bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.field);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        flipped = getFlippedImage(bitmap, width, height);

        sp = context.getSharedPreferences("customs", 0);
        ballColour = sp.getInt("ball", Color.WHITE);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            if(btnMenu.contains(x, y)) {
                pressing = true;
            } else if(!inMove) {
                if (!dragging) {
                    if (turn == 0) {
                        moving = myTeam.getContains(x, y);
                    } else if (turn == 1) {
                        moving = opTeam.getContains(x, y);
                    }
                    if (moving != null) {
                        dragging = true;
                        startX = moving.x;
                        startY = moving.y;
                        endX = moving.x;
                        endY = moving.y;
                    }
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if(btnMenu.contains(x, y)) {
                if(pressing) {
                    final View view = new View(con);
                    final ViewGroup root = ((MainActivity)con).getWindow().getDecorView().findViewById(android.R.id.content);
                    view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
                    view.setBackgroundColor(Color.TRANSPARENT);
                    root.addView(view);
                    view.setX(width);
                    view.setY(0);
                    PopupMenu popupMenu = new PopupMenu(con, view, Gravity.CENTER);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_game, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getTitle().equals("Options")) {
                                Values.showDialog(con, false);
                            } else if(menuItem.getTitle().equals("Quit")) {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                Intent i = new Intent(con, LobbyActivity.class);
                                                con.startActivity(i);
                                                ((MainActivity)con).finish();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //Do nothing
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                                builder.setMessage("Are you sure you want to exit?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                }
            } else if(!inMove) {
                if (dragging) {
                    dragging = false;
                    endX = startX - (x - startX);
                    endY = startY - (y - startY);
                    Vector2d v = new Vector2d(endX - startX, endY - startY);
                    v.divide(GRAVITY);
                    moving.hit(v);
                    startX = 0;
                    endX = 0;
                    startY = 0;
                    endY = 0;
                    moving = null;
                }
            }
            pressing = false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if(!inMove) {
                if (dragging) {
                    startX = moving.x;
                    startY = moving.y;
                    endX = startX - (x - startX);
                    endY = startY - (y - startY);
                }
            }
        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawField(canvas);
        drawScore(canvas);
        myTeam.draw(canvas);
        opTeam.draw(canvas);
        ball.draw(ballColour, canvas);
        btnMenu.draw(Color.argb(0, 0, 0, 0), canvas);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        canvas.drawLine(startX, startY, endX, endY, p);
        if(!started)
        {
            started = true;
            handler.post(runnable);
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        int mine = sp.getInt("myColour", Color.BLUE);
        int opponent = sp.getInt("opColour", Color.RED);

        Formation myFormation = Formation.valueOf(sp.getString("myForm", "A") + "1");
        Formation opFormation = Formation.valueOf(sp.getString("opForm", "A") + "2");

        bitmap = Bitmap.createScaledBitmap(bitmap, (int)width, (int)height, false);
        flipped = Bitmap.createScaledBitmap(flipped, (int)width, (int)height, false);
        myTeam = new Team(mine, myFormation);
        opTeam = new Team(opponent, opFormation);
        ball = new Ball(w/2f, h/2f, h/30f);

        float btnWidth = w /20f;
        btnMenu = new Button(w - btnWidth - 10, 10, btnWidth, btnWidth, "", BitmapFactory.decodeResource(con.getResources(), R.drawable.menu), false);
    }
    private void drawField(Canvas c) {
        Paint p = new Paint();

        Rect frameToDraw = new Rect(0, 0, (int)width, (int)height);
        RectF whereToDraw = new RectF(0, 0, width/2, height);

        c.drawBitmap(bitmap,frameToDraw,whereToDraw, p);

        whereToDraw = new RectF(width/2, 0, width, height);
        c.drawBitmap(flipped,frameToDraw,whereToDraw, p);
    }
    private void drawScore(Canvas c) {
        Paint p = new Paint();
        p.setColor(Color.argb(120, 0, 0, 0));
        p.setTextSize(width / 5f);
        p.setTextAlign(Paint.Align.CENTER);

        float textWidth = (width / 2f) - (float)Values.getHorPosts();
        float x1 = (float)Values.getHorPosts() + (textWidth / 2f);
        float x2 = (width/2f) + (textWidth / 2f);
        float y =  (height / 2f) - ((p.descent() + p.ascent()) / 2f);

        c.drawText(String.valueOf(myTeam.getScore()), x1, y, p);
        c.drawText(String.valueOf(opTeam.getScore()), x2, y, p);
    }
    private Bitmap getFlippedImage(Bitmap b, int width, int height) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, width/2f, height/2f);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
    }
}
