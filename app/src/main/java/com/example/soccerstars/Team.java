package com.example.soccerstars;

import android.content.Context;
import android.graphics.Canvas;

import com.example.soccerstars.enums.Formation;

import java.util.ArrayList;

public class Team {
    private Formation formation;
    private int colour;
    private int score;
    private boolean highlight;
    ArrayList<Pack> packs;

    public Team(int colour, Formation formation) {
        packs = new ArrayList<>();
        this.colour = colour;
        this.formation = formation;
        score = 0;
        highlight = false;
        addPacks();
    }
    private void addPacks() {
        for(int i = 0; i < 5; i++) {
            Pack p = new Pack(formation.form[(i * 2)], formation.form[i * 2 + 1], Game.height/20f);
            packs.add(p);
        }
    }
    public void draw(Canvas c) {
        if(highlight)
            for(Pack p : packs)
                p.drawHighlight(c);
        for(Pack p : packs)
            p.draw(colour, c);
    }
    public void move(Team myTeam, Team opTeam, Ball ball, Context context) {
        for(Pack p : packs) {
            p.move(myTeam, opTeam, ball, context);
        }
    }
    public boolean contains(float x, float y) {
        for(Pack p : packs) {
            if (p.contains(x, y))
                return true;
        }
        return false;
    }
    public Pack getContains(float x, float y) {
        for(Pack p : packs) {
            if (p.contains(x, y))
                return p;
        }
        return null;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public ArrayList<Pack> getPacks() {
        return this.packs;
    }
    public void reset() {
        for(int i = 0; i < 5; i++) {
            packs.get(i).getSpeed().x = 0;
            packs.get(i).getSpeed().y = 0;
            packs.get(i).x = formation.form[(i * 2)];
            packs.get(i).y = formation.form[(i * 2) + 1];
        }
    }
    public boolean moving() {
        for(Pack p : packs) {
            if(p.getSpeed().x != 0 || p.getSpeed().y != 0) return true;
        }
        return false;
    }
    public void highlight(boolean b) {
        this.highlight = b;
    }
}