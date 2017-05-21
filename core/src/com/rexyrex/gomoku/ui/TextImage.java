package com.rexyrex.gomoku.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rexyrex.gomoku.Gomoku;

/**
 * Created by Rexyrex on 09/02/2016.
 */
public class TextImage extends Box{

    private String text;
    private TextureRegion[][] fontSheet;
    private final int fontWidth = 18;
    private final int fontLength = 32;
    public TextImage(String text, float x, float y){
        this.text = text;
        this.x = x;
        this.y = y;

        width = fontWidth * text.length();
        height = fontLength;

        TextureRegion sheet = Gomoku.res.getAtlas("pack").findRegion("Block Font");

        int numCols = sheet.getRegionWidth() / fontWidth;
        int numRows = sheet.getRegionHeight() / fontLength;
        fontSheet = new TextureRegion[numRows][numCols];


        for(int row=0; row<numRows; row++){
            for(int col=0; col<numCols;col++){
                fontSheet[row][col] = new TextureRegion(sheet,
                                                        fontWidth*col,
                                                        fontLength*row,
                                                        fontWidth,
                                                        fontLength );
            }
        }




    }

    public void changeText(String s){
        text = s;
    }

    public void render(SpriteBatch sb){
        int row = 0;
        int col = 0;
        for(int i=0; i<text.length(); i++){

            char c = text.charAt(i);
            //c is a number
            if("0123456789".contains(Character.toString(c))){
                int index = "123456789".indexOf(c);
                row = 1;
                col = index + 17;
                sb.draw(fontSheet[row][col],x - width / 2 + fontWidth * i, y - height / 2 );
            } else {
                //c is a char
                if (c == ' ') {
                    row = 1;
                    col = 0;
                } else {
                    c -= 'a';
                    int index = (int) c;

                    row = 2;
                    col = index + 1;
                }
                sb.draw(fontSheet[row][col], x - width / 2 + fontWidth * i, y - height / 2);
            }
        }
    }


}
