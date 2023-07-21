package com.example.soccerstars;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.example.soccerstars.enums.Formation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Customization extends View {
    Button btnBack;
    Button btnPacks;
    Button btnBalls;
    Button btnFormations;

    Button pressing;

    ArrayList<Button> packs;
    ArrayList<Button> balls;
    ArrayList<Button> formations;

    int state;

    Context c;

    SharedPreferences sp;

    Date pressed;

    public Customization(Context context) {
        super(context);
        this.c = context;
        pressing = null;
        state = 0;
        packs = new ArrayList<>();
        balls = new ArrayList<>();
        formations = new ArrayList<>();
        sp = c.getSharedPreferences("customs", 0);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(state == 0)
            drawGen(canvas);
        else if(state == 1)
            drawPacksCustom(canvas);
        else if(state == 2)
            drawBallsCustom(canvas);
        else if(state == 3)
            drawFormationsCustom(canvas);
        btnBack.draw(Color.BLACK, canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (btnBack.contains(x, y)) pressing = btnBack;
            else if(state == 0) {
                if (btnPacks.contains(x, y)) pressing = btnPacks;
                else if (btnBalls.contains(x, y)) pressing = btnBalls;
                else if (btnFormations.contains(x, y)) pressing = btnFormations;
            } else if(state == 1) {
                Button pressing1 = getPacksPressing(x, y);
                if(pressing1 != null) {
                    pressed = new Date();
                    pressing = pressing1;
                }
            } else if(state == 2) {
                Button pressing2 = getBallsPressing(x, y);
                if(pressing2 != null) {
                    pressing = pressing2;
                }
            } else if(state == 3) {
                Button pressing3 = getFormsPressing(x, y);
                if(pressing3 != null) {
                    pressed = new Date();
                    pressing = pressing3;
                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            if(pressing != null) {
                if(pressing.contains(x, y)) {
                    Button pressing1 = getPacksPressing(x, y);
                    Button pressing2 = getBallsPressing(x, y);
                    Button pressing3 = getFormsPressing(x, y);
                    if (pressing == btnBack) {
                        if(state == 0) {
                            Intent i = new Intent(c, LobbyActivity.class);
                            c.startActivity(i);
                            ((CustomizationActivity) c).finish();
                        }
                        else
                            setState(0);
                    } else if(pressing == btnPacks) {
                        setState(1);
                    } else if(pressing == btnBalls) {
                        setState(2);
                    } else if(pressing == btnFormations) {
                        setState(3);
                    } else if(pressing == pressing1) {
                        Date now = new Date();
                        long ms = now.getTime() - pressed.getTime();
                        SharedPreferences.Editor editor = sp.edit();
                        if(ms >= 500) {
                            editor.putInt("opColour", pressing.getColour());
                        } else {
                            editor.putInt("myColour", pressing.getColour());
                        }
                        editor.apply();
                        invalidate();
                    } else if(pressing == pressing2) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("ball", pressing.getColour());
                        editor.apply();
                        invalidate();
                    } else if(pressing == pressing3) {
                        Date now = new Date();
                        long ms = now.getTime() - pressed.getTime();
                        SharedPreferences.Editor editor = sp.edit();
                        if(ms >= 500) {
                            editor.putString("opForm", pressing.getText());
                        } else {
                            editor.putString("myForm", pressing.getText());
                        }
                        editor.apply();
                        invalidate();
                    }
                }
                pressing = null;
            }
        }
        return true;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Game.width = w;
        Game.height = h;

        float backDim = w/20f;

        btnBack = new Button(w - backDim - 10f, 10f, backDim, backDim, "", BitmapFactory.decodeResource(c.getResources(), R.drawable.back), false);

        float buttonHeight = h/1.5f;
        float buttonWidth = w/4f;

        float midX = w/2f - buttonWidth/2f;
        float y = h/2f - buttonHeight/2f;

        float space = w/4f/4f;

        btnPacks = new Button(midX - buttonWidth - space, y, buttonWidth, buttonHeight, "Packs");
        btnBalls = new Button(midX, y, buttonWidth, buttonHeight, "Balls");
        btnFormations = new Button(midX + buttonWidth + space, y, buttonWidth, buttonHeight, "Formations");
    }
    private void drawGen(Canvas c) {
        btnPacks.draw(Color.BLACK, Color.WHITE, c);
        btnBalls.draw(Color.BLACK, Color.WHITE, c);
        btnFormations.draw(Color.BLACK, Color.WHITE, c);
    }
    private void drawPacksCustom(Canvas c) {
        int mine = sp.getInt("myColour", Color.BLUE);
        int opponent = sp.getInt("opColour", Color.RED);
        fillArrayList(packs, true);
        int[] colours = new int[] {Color.YELLOW, Color.GREEN, Color.BLACK, Color.WHITE, Color.BLUE, Color.CYAN, Color.RED, Color.MAGENTA};
        for(int i = 0; i < packs.size(); i++) {
            packs.get(i).draw(colours[i], this.getHeight() / 10f, c);
            if(mine == colours[i] && opponent == colours[i])
                packs.get(i).drawBorder(Color.BLUE, Color.RED, c);
            else if(mine == colours[i])
                packs.get(i).drawBorder(Color.BLUE, c);
            else if(opponent == colours[i])
                packs.get(i).drawBorder(Color.RED, c);
        }
    }
    private void drawBallsCustom(Canvas c) {
        int ballC = sp.getInt("ball", Color.WHITE);
        fillArrayList(balls, false);
        int[] colours = new int[] {Color.YELLOW, Color.GREEN, Color.BLACK, Color.RED, Color.WHITE, Color.CYAN, Color.BLUE, Color.MAGENTA};
        for(int i = 0; i < balls.size(); i++) {
            balls.get(i).draw(colours[i], this.getHeight() / 15f, c);
            if(ballC == colours[i])
                balls.get(i).drawBorder(Color.parseColor("#FFB72B"), c);
        }
    }
    private void drawFormationsCustom(Canvas c) {
        String mine = sp.getString("myForm", "A");
        String opponent = sp.getString("opForm", "A");
        HashMap<Integer, String> values = new HashMap<>();
        values.put(0, "A");
        values.put(1, "B");
        values.put(2, "C");
        values.put(3, "D");
        values.put(4, "E");

        formations.clear();
        float x = 20;
        float width = this.getWidth() / 5f;
        float height = this.getHeight() / 3f;
        float y = (this.getHeight() / 4f) - (height / 2f);
        float widthSpace = this.getWidth() / 5f / 5f;
        for(int i = 0; i < Formation.values().length/2; i++) {
            Bitmap b = getBitmapFromAsset("formations/" + i + ".png");
            formations.add(new Button(x, y, width, height, values.get(i), b, true));
            x+= width + widthSpace;
            if(i == 3) {
                y = (this.getHeight() / 4f * 3f) - (height / 2f);
                x = 20;
            }
        }
        for(Button b : formations) {
            b.draw(Color.argb(200, 0, 0, 0), c);
            if(b.getText().equals(mine) && b.getText().equals(opponent)) {
                b.drawBorder(Color.BLUE, Color.RED, c);
            } else if(b.getText().equals(mine)) {
                b.drawBorder(Color.BLUE, c);
            } else if(b.getText().equals(opponent)) {
                b.drawBorder(Color.RED, c);
            }
        }
    }
    private void setState(int state) {
        this.state = state;
        invalidate();
    }
    private void fillArrayList(ArrayList<Button> btns, boolean type) { //type - true = packs, false = balls
        btns.clear();
        float x = 20;
        float width = this.getWidth() / 5f;
        float height = this.getHeight() / 3f;
        float y = (this.getHeight() / 4f) - (height / 2f);
        float widthSpace = this.getWidth() / 5f / 5f;
        for(int i = 0; i < 8; i++) {
            btns.add(new Button(x, y, width, height));
            x += width + widthSpace;
            if(i == 3) {
                y = (this.getHeight() / 4f * 3f) - (height / 2f);
                x = 20;
            }
        }
    }
    private Button getPacksPressing(float x, float y) {
        for(Button b : packs) {
            if (b.contains(x, y))
                return b;
        }
        return null;
    }
    private Button getBallsPressing(float x, float y) {
        for(Button b : balls) {
            if (b.contains(x, y))
                return b;
        }
        return null;
    }
    private Button getFormsPressing(float x, float y) {
        for(Button b : formations) {
            if(b.contains(x, y))
                return b;
        }
        return null;
    }
    public Bitmap getBitmapFromAsset(String filePath) {
        AssetManager assetManager = c.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }
}
