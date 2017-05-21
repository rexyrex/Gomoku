package com.rexyrex.gomoku.conceptual;

import com.rexyrex.gomoku.ui.Coordinates;

/**
 * Created by Rexyrex on 23/04/2016.
 */
public class CoordiScore {
    private Coordinates coordinates;
    private int score;

    public CoordiScore(Coordinates c, int s){
        coordinates = c;
        score = s;


    }

    public int getScore(){
        return score;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }




}
