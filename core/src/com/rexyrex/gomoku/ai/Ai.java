package com.rexyrex.gomoku.ai;

import com.rexyrex.gomoku.conceptual.Board;
import com.rexyrex.gomoku.conceptual.CoordiScore;
import com.rexyrex.gomoku.ui.Coordinates;
import com.rexyrex.gomoku.ui.Tile;

import java.util.Random;

/**
 * Created by Rexyrex on 11/02/2016.
 */
public class Ai {

    private Board board;
    private int[][] valBoard;
    private int valBoardx;
    private int valBoardy;
    private int aiColor;
    private int opp5inARowScore = 1200;
    private int ai5inARowScore = 1000;

    public Ai(Board board, int aiColor){
        this.board = board;
        valBoardx = board.getTiles()[0].length;
        valBoardy = board.getTiles().length;
        valBoard = new int[valBoardy][valBoardx];
        this.aiColor = aiColor;
    }

    public void printValBoard(){
        for(int i=0; i<valBoardy; i++){
            for(int j=0; j<valBoardx; j++){
                System.out.print(valBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    //List all Pieces placed on board by color category. A test method before writing Eval function
    public void showPlacedLists(){
        System.out.print("White Pieces: ");
        for(int i=0; i<board.getWhiteTileListIndex(); i++){
            System.out.print("("+ board.getWhiteTileList()[i].getX() + ", " + board.getWhiteTileList()[i].getY() + ") ");
        }

        System.out.print("Black Pieces: ");
        for(int i=0; i<board.getBlackTileListIndex(); i++){
            System.out.print("(" + board.getBlackTileList()[i].getX() + ", " + board.getBlackTileList()[i].getY() + ") ");
        }
    }

    //Get Score of Board
    public int getScoreFromBoard(Board sBoard, int moveColor){
        Tile[][] tiles = sBoard.getTiles();
        Coordinates[] coordinates;
        int tileToCheckCount;

        int score = 0;




        if(moveColor==1){
            coordinates = sBoard.getWhiteTileList();
            tileToCheckCount = sBoard.getWhiteTileListIndex();
        } else {
            coordinates = sBoard.getBlackTileList();
            tileToCheckCount = sBoard.getBlackTileListIndex();
        }



        for(int c=0; c<tileToCheckCount; c++) {

            int lastX = coordinates[c].getX();
            int lastY = coordinates[c].getY();

            //System.out.println("lastX = "+ lastX);
            //System.out.println("lastY = "+ lastY);

            //Make 5 in a row
            int maxY = sBoard.getTiles().length;
            int maxX = sBoard.getTiles()[0].length;

            //Horizontal Check
            int nPiecesRight = 0;
            boolean rightBroken = false;
            int nPiecesLeft = 0;
            boolean leftBroken = false;
            int nPiecesTop = 0;
            boolean topBroken = false;
            int nPiecesBot = 0;
            boolean bottomBroken = false;

            int nPiecesLeftBotDiag = 0;
            boolean leftBotDiagBroken = false;
            int nPiecesRightTopDiag = 0;
            boolean rightTopDiagBroken = false;
            int nPiecesLeftTopDiag = 0;
            boolean leftTopDiagBroken = false;
            int nPiecesRightBotDiag = 0;
            boolean rightBotDiagBroken = false;

            int diagX = 0;
            int diagY = 0;

            for (int i = 1; i <= 4; i++) {
                //diag check
                diagX++;
                diagY++;

                if (lastX + diagX < maxX && lastY + diagY < maxY) {
                    if (sBoard.getTiles()[lastY + diagY][lastX + diagX].getTileState() == moveColor && !rightTopDiagBroken) {
                        nPiecesRightTopDiag++;
                    } else {
                        rightTopDiagBroken = true;
                    }
                }

                if (lastX - diagX >= 0 && lastY - diagY >= 0) {
                    if (sBoard.getTiles()[lastY - diagY][lastX - diagX].getTileState() == moveColor && !leftBotDiagBroken) {
                        nPiecesLeftBotDiag++;
                    } else {
                        leftBotDiagBroken = true;
                    }
                }

                if (lastX - diagX >= 0 && lastY + diagY < maxY) {
                    if (sBoard.getTiles()[lastY + diagY][lastX - diagX].getTileState() == moveColor && !leftTopDiagBroken) {
                        nPiecesLeftTopDiag++;
                    } else {
                        leftTopDiagBroken = true;
                    }
                }

                if (lastX + diagX < maxX && lastY - diagY >= 0) {
                    if (sBoard.getTiles()[lastY - diagY][lastX + diagX].getTileState() == moveColor && !rightBotDiagBroken) {
                        nPiecesRightBotDiag++;
                    } else {
                        rightBotDiagBroken = true;
                    }
                }

                if (lastX + i < maxX) {
                    if (sBoard.getTiles()[lastY][lastX + i].getTileState() == moveColor && !rightBroken) {
                        nPiecesRight++;
                    } else {
                        rightBroken = true;
                    }
                }
                if (lastX - i >= 0) {
                    if (sBoard.getTiles()[lastY][lastX - i].getTileState() == moveColor && !leftBroken) {
                        nPiecesLeft++;
                    } else {
                        leftBroken = true;
                    }
                }
                if (lastY + i < maxY) {
                    if (sBoard.getTiles()[lastY + i][lastX].getTileState() == moveColor && !topBroken) {
                        nPiecesTop++;
                    } else {
                        topBroken = true;
                    }
                }
                if (lastY - i >= 0) {
                    if (sBoard.getTiles()[lastY - i][lastX].getTileState() == moveColor && !bottomBroken) {

                        nPiecesBot++;
                    } else {
                        bottomBroken = true;
                    }
                }


            }
            //VictoryLine vl = new VictoryLine(1,2,55,65);

            for(int connectCount =1; connectCount <=4; connectCount++) {
                if (nPiecesRight + nPiecesLeft >= connectCount) {

                    //System.out.println("Piece 1 (x,y) = " + (lastX+nPiecesRight) +" "+lastY);
                    //System.out.println("Piece 2 (x,y) = " + (lastX - nPiecesLeft) + " "+lastY);
                    boolean rightOpen = false;
                    boolean leftOpen = false;
                    //open ends check
                    if(lastX+nPiecesRight+1 < maxX){
                        if(sBoard.getTiles()[lastY][lastX+nPiecesRight+1].getTileState() == 0){
                            rightOpen= true;
                        }
                    }

                    if(lastX-nPiecesRight-1 >=0 ){
                        if(sBoard.getTiles()[lastY][lastX-nPiecesRight-1].getTileState() == 0){
                            leftOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(rightOpen && leftOpen){
                            score += 10;
                        } else if(rightOpen || leftOpen){
                            score += 1;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==2){
                        if(rightOpen && leftOpen){
                            score += 110;
                        } else if(rightOpen || leftOpen){
                            score += 10;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==3){
                        if(rightOpen && leftOpen){
                            score += 1000;
                        } else if(rightOpen || leftOpen){
                            score += 100;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==4) {
                        score += ai5inARowScore;
                    }
                    // return true;
                }

                if (nPiecesBot + nPiecesTop >= connectCount) {

                    //System.out.println("Piece 1 (x,y) = " + (lastX) +" "+(lastY+nPiecesTop));
                    //System.out.println("Piece 2 (x,y) = " + (lastX) + " "+(lastY-nPiecesBot));

                    boolean topOpen = false;
                    boolean botOpen = false;
                    //open ends check
                    if(lastY+nPiecesTop+1 < maxY){
                        if(sBoard.getTiles()[lastY+nPiecesTop+1][lastX].getTileState() == 0){
                            topOpen= true;
                        }
                    }

                    if(lastY-nPiecesBot-1 >=0 ){
                        if(sBoard.getTiles()[lastY-nPiecesBot-1][lastX].getTileState() == 0){
                            botOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(topOpen && botOpen){
                            score += 10;
                        } else if(topOpen || botOpen){
                            score += 1;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==2){
                        if(topOpen && botOpen){
                            score += 110;
                        } else if(topOpen || botOpen){
                            score += 10;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==3){
                        if(topOpen && botOpen){
                            score += 1000;
                        } else if(topOpen || botOpen){
                            score += 100;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==4) {
                        score += ai5inARowScore;
                    }
                    //return true;
                }

                if (nPiecesLeftBotDiag + nPiecesRightTopDiag >= connectCount) {

                    //System.out.println("Piece 1 (x,y) = " + (lastX+nPiecesRightTopDiag) +" "+(lastY+nPiecesRightTopDiag));
                    // System.out.println("Piece 2 (x,y) = " + (lastX-nPiecesLeftBotDiag) + " "+(lastY-nPiecesLeftBotDiag));

                    boolean leftBotOpen = false;
                    boolean rightTopOpen = false;
                    
                    if(lastX+nPiecesRightTopDiag+1 < maxX && lastY+nPiecesRightTopDiag+1 <maxY ){
                        if(sBoard.getTiles()[lastY+nPiecesRightTopDiag+1][lastX+nPiecesRightTopDiag+1].getTileState() == 0){
                            rightTopOpen= true;
                        }
                    }

                    if(lastX-nPiecesLeftBotDiag-1 >=0 && lastY-nPiecesLeftBotDiag-1>=0){
                        if(sBoard.getTiles()[lastY-nPiecesLeftBotDiag-1][lastX-nPiecesLeftBotDiag-1].getTileState() == 0){
                            leftBotOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(leftBotOpen && rightTopOpen){
                            score += 10;
                        } else if(leftBotOpen || rightTopOpen){
                            score += 1;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==2){
                        if(leftBotOpen && rightTopOpen){
                            score += 110;
                        } else if(leftBotOpen || rightTopOpen){
                            score += 10;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==3){
                        if(leftBotOpen && rightTopOpen){
                            score += 1000;
                        } else if(leftBotOpen || rightTopOpen){
                            score += 100;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==4) {
                        score += ai5inARowScore;
                    }
                    //return true;
                }

                if (nPiecesLeftTopDiag + nPiecesRightBotDiag >= connectCount) {

                    //System.out.println("Piece 1 (x,y) = " + (lastX-nPiecesLeftTopDiag) +" "+(lastY+nPiecesLeftTopDiag));
                    //System.out.println("Piece 2 (x,y) = " + (lastX+nPiecesRightBotDiag) + " "+(lastY-nPiecesRightBotDiag));

                    boolean leftTopOpen = false;
                    boolean rightBotOpen = false;

                    if(lastX-nPiecesLeftTopDiag-1 >=0 && lastY+nPiecesLeftTopDiag+1 <maxY ){
                        if(sBoard.getTiles()[lastY+nPiecesLeftTopDiag+1][lastX-nPiecesLeftTopDiag-1].getTileState() == 0){
                            leftTopOpen= true;
                        }
                    }

                    if(lastX+nPiecesRightBotDiag+1 <maxX && lastY-nPiecesRightBotDiag-1>=0){
                        if(sBoard.getTiles()[lastY-nPiecesRightBotDiag-1][lastX+nPiecesRightBotDiag+1].getTileState() == 0){
                            rightBotOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(leftTopOpen && rightBotOpen){
                            score += 10;
                        } else if(leftTopOpen || rightBotOpen){
                            score += 1;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==2){
                        if(leftTopOpen && rightBotOpen){
                            score += 110;
                        } else if(leftTopOpen || rightBotOpen){
                            score += 10;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==3){
                        if(leftTopOpen && rightBotOpen){
                            score += 1000;
                        } else if(leftTopOpen || rightBotOpen){
                            score += 100;
                        } else {
                            score += 0;
                        }
                    }

                    if(connectCount==4) {
                        score += ai5inARowScore;
                    }
                    //return true;
                }
            }

            //4 in a row with 2 open ends

            //4 in a row with 1 open end

            //3 in a row with 2 open ends

            //--SCore 0 --
            //4 in a row with closed ends
            //3 in a row with closed ends
            //2 in a row with closed ends
            //1 in a row with closed ends

        }//end loop of coordinates




        if(moveColor==1){
            coordinates = sBoard.getBlackTileList();
            tileToCheckCount = sBoard.getBlackTileListIndex();
        } else {
            coordinates = sBoard.getWhiteTileList();
            tileToCheckCount = sBoard.getWhiteTileListIndex();
        }


        //Check for enemy combos
        for(int c=0; c<tileToCheckCount; c++) {

            int lastX = coordinates[c].getX();
            int lastY = coordinates[c].getY();

            //System.out.println("lastX = "+ lastX);
            //System.out.println("lastY = "+ lastY);

            //Make 5 in a row
            int maxY = sBoard.getTiles().length;
            int maxX = sBoard.getTiles()[0].length;

            //Horizontal Check
            int nPiecesRight = 0;
            boolean rightBroken = false;
            int nPiecesLeft = 0;
            boolean leftBroken = false;
            int nPiecesTop = 0;
            boolean topBroken = false;
            int nPiecesBot = 0;
            boolean bottomBroken = false;

            int nPiecesLeftBotDiag = 0;
            boolean leftBotDiagBroken = false;
            int nPiecesRightTopDiag = 0;
            boolean rightTopDiagBroken = false;
            int nPiecesLeftTopDiag = 0;
            boolean leftTopDiagBroken = false;
            int nPiecesRightBotDiag = 0;
            boolean rightBotDiagBroken = false;

            int diagX = 0;
            int diagY = 0;

            for (int i = 1; i <= 4; i++) {
                //diag check
                diagX++;
                diagY++;

                if (lastX + diagX < maxX && lastY + diagY < maxY) {
                    if (sBoard.getTiles()[lastY + diagY][lastX + diagX].getTileState() == getOtherPlayer(moveColor) && !rightTopDiagBroken) {
                        nPiecesRightTopDiag++;
                    } else {
                        rightTopDiagBroken = true;
                    }
                }

                if (lastX - diagX >= 0 && lastY - diagY >= 0) {
                    if (sBoard.getTiles()[lastY - diagY][lastX - diagX].getTileState() == getOtherPlayer(moveColor) && !leftBotDiagBroken) {
                        nPiecesLeftBotDiag++;
                    } else {
                        leftBotDiagBroken = true;
                    }
                }

                if (lastX - diagX >= 0 && lastY + diagY < maxY) {
                    if (sBoard.getTiles()[lastY + diagY][lastX - diagX].getTileState() == getOtherPlayer(moveColor) && !leftTopDiagBroken) {
                        nPiecesLeftTopDiag++;
                    } else {
                        leftTopDiagBroken = true;
                    }
                }

                if (lastX + diagX < maxX && lastY - diagY >= 0) {
                    if (sBoard.getTiles()[lastY - diagY][lastX + diagX].getTileState() == getOtherPlayer(moveColor) && !rightBotDiagBroken) {
                        nPiecesRightBotDiag++;
                    } else {
                        rightBotDiagBroken = true;
                    }
                }

                if (lastX + i < maxX) {
                    if (sBoard.getTiles()[lastY][lastX + i].getTileState() == getOtherPlayer(moveColor) && !rightBroken) {
                        nPiecesRight++;
                    } else {
                        rightBroken = true;
                    }
                }
                if (lastX - i >= 0) {
                    if (sBoard.getTiles()[lastY][lastX - i].getTileState() == getOtherPlayer(moveColor) && !leftBroken) {
                        nPiecesLeft++;
                    } else {
                        leftBroken = true;
                    }
                }
                if (lastY + i < maxY) {
                    if (sBoard.getTiles()[lastY + i][lastX].getTileState() == getOtherPlayer(moveColor) && !topBroken) {
                        nPiecesTop++;
                    } else {
                        topBroken = true;
                    }
                }
                if (lastY - i >= 0) {
                    if (sBoard.getTiles()[lastY - i][lastX].getTileState() == getOtherPlayer(moveColor) && !bottomBroken) {

                        nPiecesBot++;
                    } else {
                        bottomBroken = true;
                    }
                }


            }
            //VictoryLine vl = new VictoryLine(1,2,55,65);

            for(int connectCount =1; connectCount <=4; connectCount++) {
                if (nPiecesRight + nPiecesLeft >= connectCount) {

                    boolean rightOpen = false;
                    boolean leftOpen = false;
                    //open ends check
                    if(lastX+nPiecesRight+1 < maxX){
                        if(sBoard.getTiles()[lastY][lastX+nPiecesRight+1].getTileState() == 0){
                            rightOpen= true;
                        }
                    }

                    if(lastX-nPiecesRight-1 >=0 ){
                        if(sBoard.getTiles()[lastY][lastX-nPiecesRight-1].getTileState() == 0){
                            leftOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(rightOpen && leftOpen){
                            score -= 10;
                        } else if(rightOpen || leftOpen){
                            score -= 1;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==2){
                        if(rightOpen && leftOpen){
                            score -= 110;
                        } else if(rightOpen || leftOpen){
                            score -= 10;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==3){
                        if(rightOpen && leftOpen){
                            score -= 1000;
                        } else if(rightOpen || leftOpen){
                            score -= 100;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==4) {
                        score -= opp5inARowScore;
                    }
                    // return true;
                }

                if (nPiecesBot + nPiecesTop >= connectCount) {

                    //System.out.println("Piece 1 (x,y) = " + (lastX) +" "+(lastY+nPiecesTop));
                    //System.out.println("Piece 2 (x,y) = " + (lastX) + " "+(lastY-nPiecesBot));

                    boolean topOpen = false;
                    boolean botOpen = false;
                    //open ends check
                    if(lastY+nPiecesTop+1 < maxY){
                        if(sBoard.getTiles()[lastY+nPiecesTop+1][lastX].getTileState() == 0){
                            topOpen= true;
                        }
                    }

                    if(lastY-nPiecesBot-1 >=0 ){
                        if(sBoard.getTiles()[lastY-nPiecesBot-1][lastX].getTileState() == 0){
                            botOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(topOpen && botOpen){
                            score -= 10;
                        } else if(topOpen || botOpen){
                            score -= 1;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==2){
                        if(topOpen && botOpen){
                            score -= 110;
                        } else if(topOpen || botOpen){
                            score -= 10;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==3){
                        if(topOpen && botOpen){
                            score -= 1000;
                        } else if(topOpen || botOpen){
                            score -= 100;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==4) {
                        score -= opp5inARowScore;
                    }
                    //return true;
                }

                if (nPiecesLeftBotDiag + nPiecesRightTopDiag >= connectCount) {

                    //System.out.println("Piece 1 (x,y) = " + (lastX+nPiecesRightTopDiag) +" "+(lastY+nPiecesRightTopDiag));
                    // System.out.println("Piece 2 (x,y) = " + (lastX-nPiecesLeftBotDiag) + " "+(lastY-nPiecesLeftBotDiag));

                    boolean leftBotOpen = false;
                    boolean rightTopOpen = false;

                    if(lastX+nPiecesRightTopDiag+1 < maxX && lastY+nPiecesRightTopDiag+1 <maxY ){
                        if(sBoard.getTiles()[lastY+nPiecesRightTopDiag+1][lastX+nPiecesRightTopDiag+1].getTileState() == 0){
                            rightTopOpen= true;
                        }
                    }

                    if(lastX-nPiecesLeftBotDiag-1 >=0 && lastY-nPiecesLeftBotDiag-1>=0){
                        if(sBoard.getTiles()[lastY-nPiecesLeftBotDiag-1][lastX-nPiecesLeftBotDiag-1].getTileState() == 0){
                            leftBotOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(leftBotOpen && rightTopOpen){
                            score -= 10;
                        } else if(leftBotOpen || rightTopOpen){
                            score -= 1;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==2){
                        if(leftBotOpen && rightTopOpen){
                            score -= 110;
                        } else if(leftBotOpen || rightTopOpen){
                            score -= 10;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==3){
                        if(leftBotOpen && rightTopOpen){
                            score -= 1000;
                        } else if(leftBotOpen || rightTopOpen){
                            score -= 100;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==4) {
                        score -= opp5inARowScore;
                    }
                    //return true;
                }

                if (nPiecesLeftTopDiag + nPiecesRightBotDiag >= connectCount) {

                    //System.out.println("Piece 1 (x,y) = " + (lastX-nPiecesLeftTopDiag) +" "+(lastY+nPiecesLeftTopDiag));
                    //System.out.println("Piece 2 (x,y) = " + (lastX+nPiecesRightBotDiag) + " "+(lastY-nPiecesRightBotDiag));

                    boolean leftTopOpen = false;
                    boolean rightBotOpen = false;

                    if(lastX-nPiecesLeftTopDiag-1 >=0 && lastY+nPiecesLeftTopDiag+1 <maxY ){
                        if(sBoard.getTiles()[lastY+nPiecesLeftTopDiag+1][lastX-nPiecesLeftTopDiag-1].getTileState() == 0){
                            leftTopOpen= true;
                        }
                    }

                    if(lastX+nPiecesRightBotDiag+1 <maxX && lastY-nPiecesRightBotDiag-1>=0){
                        if(sBoard.getTiles()[lastY-nPiecesRightBotDiag-1][lastX+nPiecesRightBotDiag+1].getTileState() == 0){
                            rightBotOpen= true;
                        }
                    }

                    if(connectCount==1){
                        if(leftTopOpen && rightBotOpen){
                            score -= 10;
                        } else if(leftTopOpen || rightBotOpen){
                            score -= 1;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==2){
                        if(leftTopOpen && rightBotOpen){
                            score -= 110;
                        } else if(leftTopOpen || rightBotOpen){
                            score -= 10;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==3){
                        if(leftTopOpen && rightBotOpen){
                            score -= 1000;
                        } else if(leftTopOpen || rightBotOpen){
                            score -= 100;
                        } else {
                            score -= 0;
                        }
                    }

                    if(connectCount==4) {
                        score -= opp5inARowScore;
                    }
                    //return true;
                }
            }

            //4 in a row with 2 open ends

            //4 in a row with 1 open end

            //3 in a row with 2 open ends

            //--SCore 0 --
            //4 in a row with closed ends
            //3 in a row with closed ends
            //2 in a row with closed ends
            //1 in a row with closed ends

        }//end loop of coordinates


        //System.out.println("SCORE FOR " + moveColor + " is " + score);
        return score;
    }

    public int getOtherPlayer(int p){
        if(p==1){
            return 2;
        } else {
            return 1;
        }
    }

    public CoordiScore minimax(Board mBoard, int depth, int maxDepth, int turn, Coordinates cM, int alpha, int beta){
        // CoordiScore = (BestMove, score)
        //CoordiScore cs = new CoordiScore(new Coordinates(0,0), 0);
        int score=0;
        if(depth == maxDepth){
            //Coordinate does not matter. Return the score of current board
            return new CoordiScore(cM, getScoreFromBoard(mBoard, aiColor));
        }
        Coordinates[] coordinates = mBoard.getPossibleMoves();
        int coordinatesCounter = mBoard.getPossibleMovesCounter();
        if(turn == aiColor){
            Coordinates bestMove = new Coordinates(0,0);
            for(int cCounter=0; cCounter < coordinatesCounter; cCounter++){
                Coordinates c = new Coordinates(coordinates[cCounter].getX(), coordinates[cCounter].getY());
                Board changedBoard = mBoard.cloneBoard();
                if(turn == 1) {
                    changedBoard.getTiles()[c.getY()][c.getX()].placeWhite();
                } else {
                    changedBoard.getTiles()[c.getY()][c.getX()].placeBlack();
                }


                CoordiScore mCs = minimax(changedBoard, depth+1, maxDepth,
                                        changedTurn(turn), new Coordinates(c.getX(),c.getY()),
                                        alpha,beta);
                score = mCs.getScore();

                if(score > alpha) {
                    alpha = score;
                    bestMove = new Coordinates(c.getX(), c.getY());
                }
                if(alpha >= beta) {
                    break;
                }

            }
            return new CoordiScore(bestMove,alpha);
        } else{
            //find Min

            Coordinates bestMove = new Coordinates(0,0);
            for(int cCounter=0; cCounter < coordinatesCounter; cCounter++){
                Coordinates c = new Coordinates(coordinates[cCounter].getX(), coordinates[cCounter].getY());
                Board changedBoard = mBoard.cloneBoard();
                if(turn == 1) {
                    changedBoard.getTiles()[c.getY()][c.getX()].placeWhite();
                } else {
                    changedBoard.getTiles()[c.getY()][c.getX()].placeBlack();
                }
                CoordiScore mCs = minimax(changedBoard, depth+1, maxDepth, changedTurn(turn), new Coordinates(c.getX(),c.getY()),alpha,beta);
                score = mCs.getScore();

                if(score < beta) {
                    beta = score;
                    bestMove = new Coordinates(c.getX(), c.getY());

                }
                if(alpha >= beta) {
                    break;
                }
            }
            return new CoordiScore(bestMove,beta);

        }
    }


    public int changedTurn(int t){

        if(t == 1){
            t++;
        } else {
            t--;
        }

        return t;
    }

    public int[] moveWithValBoard(){
        int maxVal=0;
        int toMoveX=3;
        int toMoveY=3;


        for(int i=0; i<valBoardy; i++){
            for(int j=0; j<valBoardx; j++){
                if(valBoard[i][j] >maxVal && !board.getTiles()[i][j].isOccupied()){
                    maxVal = valBoard[i][j];
                    toMoveX = j;
                    toMoveY = i;
                }
            }
        }
        //System.out.println("ai move x:" + toMoveX);
        //System.out.println("ai move y:" + toMoveY);
        int[] bMove = {toMoveX, toMoveY};
        return bMove;
    }

    public void updateVal(int x, int y){
        for(int i=1; i<=4; i++){
                if(y + i < board.getTiles().length && x +i<board.getTiles()[0].length) {
                    valBoard[y + i][x + i] += (5-i);
                    //System.out.println("update 1");
                }

                if(y + i < board.getTiles().length) {
                    valBoard[y + i][x] += (5-i);
                    //System.out.println("update 2");
                }

                if(x +i<board.getTiles()[0].length) {
                    valBoard[y][x + i] += (5-i);
                    //System.out.println("update 3");
                }

                if(y - i >=0 && x -i >=0) {
                    valBoard[y - i][x - i] += (5-i);
                    //System.out.println("update 4");
                }

                if(y-i >=0){
                    valBoard[y - i][x] += (5-i);
                }

                if(x-i >=0){
                    valBoard[y][x-i] += (5-i);
                }

                if(x-i >=0 &&y + i < board.getTiles().length){
                    valBoard[y + i][x-i] += (5-i);
                }

                if(y-i >=0 &&x + i < board.getTiles()[0].length){
                    valBoard[y - i][x+i] += (5-i);
                }
            }
        }


    public int[] getRandomMove(){
        int x=0;
        int y=0;
        do {
            Random random = new Random();
            x = random.nextInt(board.getWidth() - 1 - 0 + 1) + 0;
            y = random.nextInt(board.getHeight() - 1 - 0 + 1) + 0;



        }while(movePossible() && board.getTiles()[y][x].getTileState()!=0);

        if(!movePossible()){
            System.out.println("no moves possible");
        }

        int[] bMove = {x,y};
        //System.out.println("ai move (x,y) = " + x + ", " + y);
        return bMove;
    }

    public boolean movePossible(){

        for(int i=0; i<board.getWidth(); i++){
            for(int j=0; j<board.getHeight(); j++){
                if(board.getTiles()[j][i].getTileState()==0){
                    return true;
                }
            }
        }
        return false;
    }

}
