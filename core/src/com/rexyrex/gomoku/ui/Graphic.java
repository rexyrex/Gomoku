package com.rexyrex.gomoku.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Rexyrex on 09/02/2016.
 */
public class Graphic extends Box{
    private TextureRegion image;

    public Graphic(TextureRegion image, float x, float y){
        this.image = image;
        this.x = x;
        this.y = y;
        width = image.getRegionWidth();
        height = image.getRegionHeight();

    }

    public void render(SpriteBatch sb){
        sb.draw(image, x-width/2, y-height/2);
    }
}
