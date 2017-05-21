package com.rexyrex.gomoku.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Rexyrex on 14/02/2016.
 */
public class VictoryLine {

    private ShapeRenderer sr;
    public VictoryLine(int x1, int y1, int x2, int y2){
        sr = new ShapeRenderer();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.line(x1,y1,x2,y2);
        sr.end();


    }



}
