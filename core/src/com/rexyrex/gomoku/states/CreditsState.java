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
public class CreditsState extends State {

    private Graphic title;
    private Array<TextImage> buttons;

    public CreditsState(GSM gsm){
        super(gsm);
        title = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion("credits"),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+200
        );

        String[] texts = {"exit"};

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

                        case 0: gsm.pop(); break;
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
