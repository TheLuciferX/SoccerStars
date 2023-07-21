package com.example.soccerstars;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

public class Lobby extends View {
    private Context c;
    private Button btnPlay;
    private Button btnCustom;
    private Button btnSettings;

    private Button pressing;

    public Lobby(Context context) {
        super(context);
        this.c = context;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        btnPlay.draw(Color.BLACK, Color.WHITE, canvas);
        btnCustom.draw(Color.BLACK, canvas);
        btnSettings.draw(Color.BLACK, canvas);
        drawText(canvas);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float playWidth = w / 5f;
        float playHeight = h / 4f;
        float playX = (w / 2f) - (playWidth / 2f);
        float playY = (h / 3f * 2f) - (playHeight / 2f);
        btnPlay = new Button(playX, playY, playWidth, playHeight, "Play");

        float shopWidth = w / 20f;
        float shopX = (w - shopWidth - 10f);
        float shopY = 10f;
        btnCustom = new Button(shopX, shopY, shopWidth, shopWidth, "", BitmapFactory.decodeResource(c.getResources(), R.drawable.shop), false);

        float formWidth = w / 20f;
        float formX = (w - formWidth - 10f);
        float formY = (h - formWidth - 10f);
        btnSettings = new Button(formX, formY, formWidth, formWidth, "", BitmapFactory.decodeResource(c.getResources(), R.drawable.menu), false);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(btnSettings.contains(x, y)) pressing = btnSettings;
            else if(btnPlay.contains(x, y)) pressing = btnPlay;
            else if(btnCustom.contains(x, y)) pressing = btnCustom;
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            if(pressing != null) {
                if(pressing.contains(x, y)) {
                    Intent i = null;
                    if (pressing == btnCustom) {
                        i = new Intent(c, CustomizationActivity.class);
                    } else if (pressing == btnPlay) {
                        i = new Intent(c, MainActivity.class);
                    } else if(pressing == btnSettings) {
                        Values.showDialog(c, true);
                    }

                    if(i != null) {
                        c.startActivity(i);
                        ((LobbyActivity) c).finish();
                    }
                }
                pressing = null;
            }
        }
        return true;
    }
    private void drawText(Canvas canvas) {
        float x = getWidth() / 2f;
        float y = getHeight() / 3f;
        Paint p = new Paint();
        p.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
        p.setFakeBoldText(true);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(getHeight() / 5f);

        canvas.drawText("Dream Stars", x, y, p);
    }
}
