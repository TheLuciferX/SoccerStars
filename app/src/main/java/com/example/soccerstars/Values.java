package com.example.soccerstars;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Values {
    public static double getHorPosts() {
        double a = 583.0*2.0/143.0;
        double b = Game.width / a;
        return b;
    }
    public static double getVerPosts() {
        double a = 583.0/36.0;
        double b = Game.height / a;
        return b;
    }
    public static double getGoalDepth() {
        double a = 583.0*2.0/91.0;
        double b = Game.width / a;
        return b;
    }
    public static double getGoalPosts() {
        double a = 583.0/203.0;
        double b = Game.height / a;
        return b;
    }
    public static void showDialog(Context c, boolean b) {
        final SharedPreferences sp = c.getSharedPreferences("customs", 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        View mview = b ? ((LobbyActivity)c).getLayoutInflater().inflate(R.layout.dialog_options, null)
                : ((MainActivity)c).getLayoutInflater().inflate(R.layout.dialog_options, null);

        builder.setView(mview);
        final AlertDialog ad = builder.create();
        ad.setCancelable(false);

        Switch swSound = mview.findViewById(R.id.swSound);
        Switch swVibration = mview.findViewById(R.id.swVibration);
        android.widget.Button btnExit = mview.findViewById(R.id.btnExit);

        swSound.setChecked(sp.getBoolean("sound", true));
        swVibration.setChecked(sp.getBoolean("vibration", true));

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });

        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("sound", b);
                editor.apply();
            }
        });

        swVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("vibration", b);
                editor.apply();
            }
        });

        ad.show();
    }
}