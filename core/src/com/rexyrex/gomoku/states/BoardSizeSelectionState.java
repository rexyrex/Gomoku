package com.rexyrex.gomoku.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.rexyrex.gomoku.Gomoku;
import com.rexyrex.gomoku.ui.Graphic;
import com.rexyrex.gomoku.ui.TextImage;

/**
 * Created by Rexyrex on 24/04/2016.
 */
public class BoardSizeSelectionState extends State{

    private int aiDiff;
    private Array<TextImage> buttons;
    private Graphic title;
    private int destPage; //destination page 0=single player 1=multiplayer 2=ai vs ai

    public BoardSizeSelectionState(GSM gsm, int destPage){
        super(gsm);
        this.aiDiff = aiDiff;

        this.destPage = destPage;

        title = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion("boardSize"),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+200
        );

        String[] texts = {"7x7", "9x9", "11x11", "13x13",
                "back"};

        buttons = new Array<TextImage>();

        for(int i=0; i<texts.length; i++){
            buttons.add(
                    new TextImage(
                            texts[i],
                            Gomoku.WIDTH/2,
                            Gomoku.HEIGHT/2 - 40*i));
        }



    }

    public void handleInput(){
        if(Gdx.input.justTouched()){
            mouse.x = Gdx.input.getX();
            mouse.y = Gdx.input.getY();
            cam.unproject(mouse);
            for(int i=0; i<buttons.size; i++) {
                if (buttons.get(i).contains(mouse.x, mouse.y)) {
                    switch (i){
                        case 0:
                            if(destPage==0) {
                                gsm.push(new DifficultyState(gsm, 7, 7));
                            } else if(destPage==1) {
                                gsm.push(new MultiPlayerState(gsm,7,7));
                            } else {
                                gsm.push(new AiDifficultyState(gsm,7,7,1,-1,-1));
                            } break;
                        case 1:
                            if(destPage==0) {
                                gsm.push(new DifficultyState(gsm, 9, 9));
                            } else if(destPage==1) {
                                gsm.push(new MultiPlayerState(gsm,9,9));
                            } else {
                                gsm.push(new AiDifficultyState(gsm,9,9,1,-1,-1));
                            } break;
                        case 2:
                            if(destPage==0) {
                                gsm.push(new DifficultyState(gsm, 11, 11));
                            } else if(destPage==1) {
                                gsm.push(new MultiPlayerState(gsm,11,11));
                            } else {
                                gsm.push(new AiDifficultyState(gsm,11,11,1,-1,-1));
                            } break;
                        case 3:
                            if(destPage==0) {
                                gsm.push(new DifficultyState(gsm, 13, 13));
                            } else if(destPage==1) {
                                gsm.push(new MultiPlayerState(gsm,13,13));
                            } else {
                                gsm.push(new AiDifficultyState(gsm,13,13,1,-1,-1));
                            } break;
                        case 4: gsm.pop(); break;
                        default: System.out.println("Not a button"); break;
                    }

                }
            }
        }



    }
    public void update(float dt){
        handleInput();
    }
    public void render(SpriteBatch sb){

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        title.render(sb);
        for(int i=0; i<buttons.size; i++){
            buttons.get(i).render(sb);
        }
        sb.end();


    }
}
