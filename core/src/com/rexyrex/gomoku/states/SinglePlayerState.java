package com.rexyrex.gomoku.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rexyrex.gomoku.Gomoku;
import com.rexyrex.gomoku.ai.Ai;
import com.rexyrex.gomoku.conceptual.Board;
import com.rexyrex.gomoku.conceptual.CoordiScore;
import com.rexyrex.gomoku.ui.Coordinates;
import com.rexyrex.gomoku.ui.Graphic;
import com.rexyrex.gomoku.ui.TextImage;
import com.rexyrex.gomoku.ui.VictoryLine;

import java.util.Random;

/**
 * Created by Rexyrex on 11/02/2016.
 */
public class SinglePlayerState extends State{

    private final int MAX_FINGERS = 1;


    private int tileSize;
    private float boardOffset;
    private int boardHeight;
    private int turn;
    private Board board;
    private boolean didWhiteWin;
    private boolean didBlackWin;
    private boolean isGameOver;
    Ai ai;
    public int aiNum; //Indicate color of AI (White = 1, Black = 2)
    private boolean isAiTurn;
    private boolean aiMadeMove;
    private int aiDifficulty;
    private Graphic whiteWinGraphic;
    private Graphic blackWinGraphic;
    private TextImage backButton;
    private Graphic aiThinkingGraphic;
    private boolean aiIsThinking;
    private boolean isDraw;
    private Graphic drawGraphic;



    public SinglePlayerState(GSM gsm, int aiDifficulty, int boardx, int boardy){
        super(gsm);
        //480 width
        //800 height
        //Randomize who goes first
        //aiDifficulty
        //0=random
        //1=arraybased
        //2=depth1
        //3=depth2
        //4=depth3
        whiteWinGraphic = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion("whitewin"),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+300
        );

        blackWinGraphic = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion("blackwin"),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+300
        );

        aiThinkingGraphic = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion("aithinking"),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+300
        );

        drawGraphic = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion("Draw"),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+300
        );

        backButton = new TextImage(
                "exit game",
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2 - 40*7);


        Random random = new Random();
        int goFirst = random.nextInt(1-0+1)+0;
        aiMadeMove = false;
        aiIsThinking = false;
        if(goFirst==1){
            isAiTurn = true;
            aiNum = 2;
        } else {
            isAiTurn = false;
            aiNum = 1;
        }

        isGameOver = false;
        board = new Board(boardx,boardy,2);
        ai = new Ai(board,aiNum);

        this.aiDifficulty = aiDifficulty;
        turn = 2;



        tileSize = Gomoku.WIDTH/board.getTiles()[0].length;
        boardHeight = tileSize * board.getTiles().length;
        boardOffset = (Gomoku.HEIGHT - boardHeight)/2;

        try{
            Thread.sleep(70);
        } catch (InterruptedException e){

        };
    }

    @Override
    public void handleInput() {
        // TODO Auto-generated method stub
        if(Gdx.input.justTouched()) {
            mouse.x = Gdx.input.getX();
            mouse.y = Gdx.input.getY();
            cam.unproject(mouse);

            if (backButton.contains(mouse.x, mouse.y)) {
                gsm.set(new MenuState(gsm));
            }
        }
        if(!isGameOver) {



            if (!isAiTurn) {
                for (int i = 0; i < MAX_FINGERS; i++) {
                    if (Gdx.input.isTouched(i)) {
                        mouse.x = Gdx.input.getX(i);
                        mouse.y = Gdx.input.getY(i);
                        cam.unproject(mouse);

                        if (mouse.x > 0 &&
                                mouse.x < Gomoku.WIDTH &&
                                mouse.y > boardOffset &&
                                mouse.y < boardOffset + boardHeight) {

                            int row = (int) ((mouse.y - boardOffset) / tileSize);
                            int col = (int) (mouse.x / tileSize);
                            if (!board.getTiles()[row][col].isOccupied()) {
                                System.out.println("(x,y)=" + col + ", " + row);
                                placePiece(row, col);
                                ai.updateVal(col, row);
                                //ai.printValBoard();
                                if (checkVictory(col, row)) {
                                    System.out.println("Victory for " + turn);
                                    isGameOver = true;
                                    if(turn==1){
                                        didWhiteWin=true;
                                    } else {
                                        didBlackWin=true;
                                    }
                                }
                                ai.showPlacedLists();
                                ai.getScoreFromBoard(board, turn);
                                aiIsThinking = true;
                                changeTurn();
                                changePlayerTurn();

                            }
                        }

                    }
                }
            }
        }
    }

    public void placePiece(int row, int col){
        if (turn == 1) {
            board.getTiles()[row][col].placeWhite();

        } else {
            board.getTiles()[row][col].placeBlack();
        }

        board.updateBoard(board.getTiles()[row][col], turn);
    }

    public void makeAiMove(){

        //int[] move = ai.getRandomMove();
        int col;
        int row;
        if(aiDifficulty==0){
            int[] move = ai.getRandomMove();
            col = move[0];
            row = move[1];
        } else if(aiDifficulty==1){
            int[] move = ai.moveWithValBoard();
            col = move[0];
            row = move[1];
        } else {
            CoordiScore cs = ai.minimax(board, 0, aiDifficulty-1, aiNum, new Coordinates(3, 3), Integer.MIN_VALUE, Integer.MAX_VALUE);
            col = cs.getCoordinates().getX();
            row = cs.getCoordinates().getY();
        }
        //System.out.println("Minimax Suggests " +cs.getCoordinates().getX() + ", " + cs.getCoordinates().getY());



        System.out.println("AI Move: ("+col+ ", "+row+")");
        ai.updateVal(col, row);
        placePiece(row, col);
        ai.getScoreFromBoard(board, turn);
        if (checkVictory(col, row)) {
            System.out.println("Victory for " + turn);
            isGameOver = true;
            if(turn==1){
                didWhiteWin=true;
            } else {
                didBlackWin=true;
            }
        }

        board.getPossibleMoves();
        System.out.println("spaces left: " + board.getPossibleMovesCounter());
        System.out.println("FREE SPACES:");
        for(int pm=0; pm<board.getPossibleMovesCounter(); pm++){
            System.out.print("("+board.getPossibleMoves()[pm].getX()+", "+board.getPossibleMoves()[pm].getY()+"), ");
        }

        System.out.println("The board:");
        board.printBoard();


        changePlayerTurn();
        changeTurn();

    }

    @Override
    public void update(float dt) {

        if(board.getPossibleMoves().equals(null) || board.getPossibleMovesCounter()==0){

            isGameOver = true;
            isDraw=true;
        }

        if(isAiTurn && !isGameOver){

            makeAiMove();
            aiIsThinking = false;

        }
        for(int row = 0; row<board.getTiles().length; row++){
            for(int col = 0; col <board.getTiles()[row].length; col++) {
                board.getTiles()[row][col].update(dt);

            }
        }

        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        // TODO Auto-generated method stub
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        for(int row = 0; row<board.getTiles().length; row++){
            for(int col = 0; col <board.getTiles()[row].length; col++){
                board.getTiles()[row][col].render(sb);
            }
        }

        backButton.render(sb);

        if(aiIsThinking && !isGameOver){
            aiThinkingGraphic.render(sb);
        }

        if(isGameOver) {
            backButton.changeText("continue");
            if (didBlackWin) {
                blackWinGraphic.render(sb);

            }

            if (didWhiteWin) {
                whiteWinGraphic.render(sb);

            }

            if(isDraw){
                drawGraphic.render(sb);
            }
        }

        sb.end();


    }

    public void changePlayerTurn(){
        if(isAiTurn){
            isAiTurn = false;
        } else {
            isAiTurn = true;
            aiMadeMove = false;
        }
    }

    public void changeTurn(){
        if(turn ==1){
            turn++;
        } else {
            turn --;
        }
    }

    public boolean checkVictory(int lastX, int lastY){
        int maxY = board.getTiles().length;
        int maxX = board.getTiles()[0].length;

        //Horizontal Check
        int nPiecesRight=0;
        boolean rightBroken=false;
        int nPiecesLeft =0;
        boolean leftBroken=false;
        int nPiecesTop=0;
        boolean topBroken=false;
        int nPiecesBot=0;
        boolean bottomBroken=false;

        int nPiecesLeftBotDiag=0;
        boolean leftBotDiagBroken = false;
        int nPiecesRightTopDiag=0;
        boolean rightTopDiagBroken = false;
        int nPiecesLeftTopDiag=0;
        boolean leftTopDiagBroken=false;
        int nPiecesRightBotDiag=0;
        boolean rightBotDiagBroken = false;

        int diagX = 0;
        int diagY = 0;

        for(int i=1; i<=4; i++){
            //diag check
            diagX++;
            diagY++;

            if(lastX + diagX < maxX && lastY + diagY < maxY){
                if(board.getTiles()[lastY+diagY][lastX+diagX].getTileState() == turn && !rightTopDiagBroken){
                    nPiecesRightTopDiag++;
                } else {
                    rightTopDiagBroken = true;
                }
            }

            if(lastX - diagX >=0 && lastY - diagY >=0){
                if(board.getTiles()[lastY-diagY][lastX-diagX].getTileState() == turn && !leftBotDiagBroken){
                    nPiecesLeftBotDiag++;
                } else {
                    leftBotDiagBroken = true;
                }
            }

            if(lastX - diagX >=0 && lastY + diagY <maxY){
                if(board.getTiles()[lastY+diagY][lastX-diagX].getTileState() == turn && !leftTopDiagBroken){
                    nPiecesLeftTopDiag++;
                } else {
                    leftTopDiagBroken = true;
                }
            }

            if(lastX + diagX <maxX && lastY - diagY >=0){
                if(board.getTiles()[lastY-diagY][lastX+diagX].getTileState() == turn && !rightBotDiagBroken){
                    nPiecesRightBotDiag++;
                } else {
                    rightBotDiagBroken = true;
                }
            }

            if(lastX + i < maxX){
                if(board.getTiles()[lastY][lastX+i].getTileState() == turn && !rightBroken){
                    nPiecesRight++;
                } else {
                    rightBroken = true;
                }
            }
            if(lastX -i >= 0){
                if(board.getTiles()[lastY][lastX-i].getTileState() == turn && !leftBroken){
                    nPiecesLeft++;
                } else {
                    leftBroken = true;
                }
            }
            if(lastY + i < maxY){
                if(board.getTiles()[lastY +i][lastX].getTileState() == turn && !topBroken){
                    nPiecesTop++;
                } else {
                    topBroken = true;
                }
            }
            if(lastY -i >=0 ){
                if(board.getTiles()[lastY -i][lastX].getTileState() == turn && !bottomBroken){

                    nPiecesBot++;
                } else {
                    bottomBroken = true;
                }
            }


        }
        //VictoryLine vl = new VictoryLine(1,2,55,65);


        if (nPiecesRight + nPiecesLeft >=4) {
            //System.out.println("Piece 1 (x,y) = " + (lastX+nPiecesRight) +" "+lastY);
            //System.out.println("Piece 2 (x,y) = " + (lastX - nPiecesLeft) + " "+lastY);

            for(int i=lastX-nPiecesLeft; i<(lastX+nPiecesRight+1); i++){
                board.getTiles()[lastY][i].setVictory();
            }

            return true;
        }

        if(nPiecesBot + nPiecesTop >=4){
            //System.out.println("Piece 1 (x,y) = " + (lastX) +" "+(lastY+nPiecesTop));
            //System.out.println("Piece 2 (x,y) = " + (lastX) + " "+(lastY-nPiecesBot));

            for(int i=lastY-nPiecesBot; i<(lastY+nPiecesTop+1); i++){
                board.getTiles()[i][lastX].setVictory();
            }

            return true;
        }

        if(nPiecesLeftBotDiag + nPiecesRightTopDiag >=4){
            //System.out.println("Piece 1 (x,y) = " + (lastX+nPiecesRightTopDiag) +" "+(lastY+nPiecesRightTopDiag));
           // System.out.println("Piece 2 (x,y) = " + (lastX-nPiecesLeftBotDiag) + " "+(lastY-nPiecesLeftBotDiag));

            int incY=0;
            for(int i=lastX-nPiecesLeftBotDiag; i<lastX+nPiecesRightTopDiag+1; i++){
                board.getTiles()[lastY-nPiecesLeftBotDiag+incY][i].setVictory();
                incY++;
            }

            return true;
        }

        if(nPiecesLeftTopDiag + nPiecesRightBotDiag >=4){
            //System.out.println("Piece 1 (x,y) = " + (lastX-nPiecesLeftTopDiag) +" "+(lastY+nPiecesLeftTopDiag));
            //System.out.println("Piece 2 (x,y) = " + (lastX+nPiecesRightBotDiag) + " "+(lastY-nPiecesRightBotDiag));

            int decY=0;
            for(int i=lastX-nPiecesLeftTopDiag; i<lastX+nPiecesRightBotDiag+1; i++){
                board.getTiles()[lastY+nPiecesLeftTopDiag-decY][i].setVictory();
                decY++;
            }

            return true;
        }









        return false;

    }


}
