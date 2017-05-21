package com.rexyrex.gomoku.conceptual;

import com.rexyrex.gomoku.Gomoku;
import com.rexyrex.gomoku.ui.Coordinates;
import com.rexyrex.gomoku.ui.Tile;

/**
 * Created by Rexyrex on 11/02/2016.
 */
public class Board {
    private Tile[][] tiles;
    private int tileSize;
    private float boardOffset;
    private int boardHeight;
    private int tilesHeight;
    private int tilesWidth;
    private int lastColorPlayed; //White = 1, Black = 2
    private Coordinates[] whiteTileList; //List of white tiles
    private Coordinates[] blackTileList; // List of black tiles on board
    private int whiteTileListIndex;
    private int blackTileListIndex;
    int possibleMovesCounter;


    public Board(int x, int y, int lastColorPlayed){
        tiles = new Tile[y][x];
        tilesHeight = y;
        tilesWidth = x;
        this.lastColorPlayed = lastColorPlayed;
        tileSize = Gomoku.WIDTH/tiles[0].length;
        boardHeight = tileSize * tiles.length;
        boardOffset = (Gomoku.HEIGHT - boardHeight)/2;

        whiteTileListIndex = 0;
        blackTileListIndex = 0;
        whiteTileList = new Coordinates[200];
        blackTileList = new Coordinates[200];

        possibleMovesCounter = 0;

        for(int i =0; i<200; i++){
            whiteTileList[i] = new Coordinates(-1,-1);
            blackTileList[i] = new Coordinates(-1,-1);
        }

        for(int row = 0; row<tiles.length; row++){
            for(int col = 0; col <tiles[0].length; col++){


                tiles[row][col] = new Tile(
                        col*tileSize+tileSize/2,
                        row*tileSize+boardOffset+tileSize/2,
                        tileSize, tileSize, col, row);

                tiles[row][col].setTimer(-((tiles.length - row) + col) * 0.03f);
            }
        }
    }

    public Board cloneBoard(){
        Board clonedBoard = new Board(this.tilesWidth, this.tilesHeight, lastColorPlayed);
        for(int row = 0; row<tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                if(tiles[row][col].getTileState()==1) {
                    clonedBoard.getTiles()[row][col].placeWhite();
                } else if(tiles[row][col].getTileState()==2){
                    clonedBoard.getTiles()[row][col].placeBlack();
                }
            }
        }

        //Copy placed pieces lists and index
        clonedBoard.setBlackTileListIndex(blackTileListIndex);
        clonedBoard.setWhiteTileListIndex(whiteTileListIndex);

        for(int i=0; i<whiteTileListIndex; i++){

            clonedBoard.setWhiteTileListAtIndex(i, whiteTileList[i]);
        }
        for(int i=0; i<blackTileListIndex; i++){
            clonedBoard.setBlackTileListAtIndex(i, blackTileList[i]);

        }


        return clonedBoard;
    }

    public void printBoard(){
        for(int row = tiles.length-1; row>=0; row--) {
            for (int col = 0; col < tiles[0].length; col++) {
                System.out.print(tiles[row][col].getTileState() + " ");
            }
            System.out.println();
        }
    }

    public Coordinates[] getPossibleMoves(){
        Coordinates[] pMoves = new Coordinates[15*15];
        int pMovesCounter=0;

        for(int row=0; row<tiles.length; row++){
            for(int col =0; col<tiles[0].length; col++){
                if(tiles[row][col].getTileState()==0){
                    pMoves[pMovesCounter] = new Coordinates(col,row);
                    pMovesCounter += 1;
                }
            }
        }
        possibleMovesCounter = pMovesCounter;

        return pMoves;
    }

    //Run getPossibleMoves() before calling this method
    public int getPossibleMovesCounter(){
        return possibleMovesCounter;
    }

    public Tile[][] getTiles(){
        return tiles;
    }

    public int getLastColorPlayed(){ return lastColorPlayed; }

    public int getHeight(){
        return tilesHeight;
    }

    public int getWidth(){
        return tilesWidth;
    }

    public void updateBoard(Tile t, int color){
        if(color==1){
            whiteTileList[whiteTileListIndex] = new Coordinates(t.getXPos(),t.getYPos());
            whiteTileListIndex++;
        } else if(color ==2){
            blackTileList[blackTileListIndex] = new Coordinates(t.getXPos(),t.getYPos());
            blackTileListIndex++;
        }
    }

    public Coordinates[] getWhiteTileList(){
        return whiteTileList;
    }

    public Coordinates[] getBlackTileList(){
        return blackTileList;
    }

    public void setWhiteTileListIndex(int wtli){
        whiteTileListIndex = wtli;
    }

    public void setBlackTileListIndex(int btli){
        blackTileListIndex = btli;
    }

    public void setWhiteTileListAtIndex(int i, Coordinates val){
        whiteTileList[i] = val;
    }

    public void setBlackTileListAtIndex(int i, Coordinates val){
        blackTileList[i] = val;
    }

    public int getWhiteTileListIndex(){
        return whiteTileListIndex;
    }

    public int getBlackTileListIndex(){
        return blackTileListIndex;
    }

    public boolean isEmpty(int x, int y){

        if(tiles[y][x].getTileState() == 0){
            return true;
        }

        return false;
    }

}
