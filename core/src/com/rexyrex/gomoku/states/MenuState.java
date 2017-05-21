package com.rexyrex.gomoku.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.rexyrex.gomoku.Gomoku;
import com.rexyrex.gomoku.ui.Graphic;
import com.rexyrex.gomoku.ui.TextImage;

public class MenuState extends State{

    private Graphic title;
    private Array<TextImage> buttons;

    public MenuState(GSM gsm){
        super(gsm);
        title = new Graphic(
                Gomoku.res.getAtlas("pack").findRegion("gomokuTitle"),
                Gomoku.WIDTH/2,
                Gomoku.HEIGHT/2+200
                );

        String[] texts = {"single player", "multiplayer", "ai vs ai mode", "how to play",
        "credits","exit"};

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
                        case 0: gsm.push(new BoardSizeSelectionState(gsm,0)); break;
                        case 1: gsm.set(new BoardSizeSelectionState(gsm,1)); break;
                        case 2: gsm.push(new BoardSizeSelectionState(gsm,2)); break;
                        case 3: System.out.println("how to play"); break;
                        case 4: gsm.push(new CreditsState(gsm)); break;
                        case 5: System.exit(1); break;
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
