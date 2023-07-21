package com.example.soccerstars.enums;

import com.example.soccerstars.Game;

public enum Formation {
    A1(new float[]{Game.width / 6f, Game.height / 4f, Game.width / 6f, Game.height / 4f * 3f, Game.width / 4f, Game.height / 3f, Game.width / 4f, Game.height / 3f * 2f, Game.width / 3f, Game.height / 2f}),
    A2(new float[]{Game.width / 6f * 5f, Game.height / 4f, Game.width / 6f * 5f, Game.height / 4f * 3f, Game.width / 4f * 3f, Game.height / 3f, Game.width / 4f * 3f, Game.height / 3f * 2f, Game.width / 3f * 2f, Game.height / 2f}),
    B1(new float[]{Game.width / 4f, Game.height / 5f, Game.width / 4f, Game.height / 5f * 4f, Game.width / 4f, Game.height / 3f, Game.width / 4f, Game.height / 3f * 2f, Game.width / 3f, Game.height / 2f}),
    B2(new float[]{Game.width / 4f * 3f, Game.height / 5f, Game.width / 4f * 3f, Game.height / 5f * 4f, Game.width / 4f * 3f, Game.height / 3f, Game.width / 4f * 3f, Game.height / 3f * 2f, Game.width / 3f * 2f, Game.height / 2f}),
    C1(new float[]{Game.width / 4f, Game.height / 3f, Game.width / 4f, Game.height / 3f * 2f, Game.width / 3f, Game.height / 3f, Game.width / 3f, Game.height / 3f * 2f, Game.width / 3f, Game.height / 2f}),
    C2(new float[]{Game.width / 4f * 3f, Game.height / 3f, Game.width / 4f * 3f, Game.height / 3f * 2f, Game.width / 3f * 2f, Game.height / 3f, Game.width / 3f * 2f, Game.height / 3f * 2f, Game.width / 3f * 2f, Game.height / 2f}),
    D1(new float[]{Game.width / 6f, Game.height / 2f, Game.width / 4f, Game.height / 3f, Game.width / 4f, Game.height / 3f * 2f, Game.width / 3f, Game.height / 4f, Game.width / 3f, Game.height / 4f * 3f}),
    D2(new float[]{Game.width / 6f * 5f, Game.height / 2f, Game.width / 4f * 3f, Game.height / 3f, Game.width / 4f * 3f, Game.height / 3f * 2f, Game.width / 3f * 2f, Game.height / 4f, Game.width / 3f * 2f, Game.height / 4f * 3f}),
    E1(new float[]{Game.width / 4f, Game.height / 6f, Game.width / 4f, Game.height / 2f, Game.width / 4f, Game.height / 6f * 5f, Game.width / 3f, Game.height / 6f * 2f, Game.width / 3f, Game.height / 6f * 4f}),
    E2(new float[]{Game.width / 4f * 3f, Game.height / 6f, Game.width / 4f * 3f, Game.height / 2f, Game.width / 4f * 3f, Game.height / 6f * 5f, Game.width / 3f * 2f, Game.height / 6f * 2f, Game.width / 3f * 2f, Game.height / 6f * 4f});

    public final float[] form;
    Formation(float[] form) {
        this.form = form;
    }
}

