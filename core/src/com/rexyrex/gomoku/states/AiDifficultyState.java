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
public class AiDifficultyState extends State {

    private Array<TextImage> buttons;
    private int boardx;
    private int boardy;
    private Graphic title;
    private int aiN; //Ai number 1 = white 2 = black

    private int aiWhiteDifficulty;
    private int aiBlackDifficulty;

    public AiDifficultyState(GSM gsm, int boardx, int boardy, int aiN,int aiWhiteDifficulty, int aiBlackDifficulty){
        super(gsm);
        this.aiN = aiN;
        String[] pngNames = {"aiBlackDifficulty","aiWhiteDifficulty"};

        title = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion(pngNames[aiN-1]),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+200
        );

        this.boardx = boardx;
        this.boardy = boardy;

        this.aiWhiteDifficulty = aiWhiteDifficulty;
        this.aiBlackDifficulty = aiBlackDifficulty;

        String[] texts = {"not even trying", "easy", "medium", "competent","hard",
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
                        case 1: ;
                        case 2:
                        case 3:
                        case 4:
                            if(aiN==1){
                                gsm.push(new AiDifficultyState(gsm,boardx,boardy,2,aiWhiteDifficulty,i));
                                System.out.println("i = " + i);
                            } else {
                                gsm.set(new AiVsAiState(gsm, i, aiBlackDifficulty,boardx,boardy));
                            }
                            break;
                        case 5: gsm.pop(); break;
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
