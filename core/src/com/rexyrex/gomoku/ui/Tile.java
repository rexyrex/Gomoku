package com.rexyrex.gomoku.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rexyrex.gomoku.Gomoku;

public class Tile extends Box{
	
	private TextureRegion blackTex;
	private TextureRegion boardTex;
	private TextureRegion whiteTex;
	private TextureRegion blackVictoryTex;
	private TextureRegion whiteVictoryTex;
	
	private boolean placed;
	private boolean victory;
	private int tileState;
	//0 = empty, 1 = white, 2 = black

	private int xPos;
	private int yPos;
	
	private float totalWidth;
	private float totalHeight;
	private float timer;
	private float maxTime = 0.5f;
	
	public Tile(float x, float y, float width, float height, int xPos, int yPos){
		boardTex = Gomoku.res.getAtlas("pack").findRegion("board");
		blackTex = Gomoku.res.getAtlas("pack").findRegion("black");
		whiteTex = Gomoku.res.getAtlas("pack").findRegion("white");
		blackVictoryTex = Gomoku.res.getAtlas("pack").findRegion("blackVictory");
		whiteVictoryTex = Gomoku.res.getAtlas("pack").findRegion("whiteVictory");

		this.x = x;
		this.y = y;
		this.totalWidth = width-width/10f;
		this.totalHeight = height-height/10f;
		placed = false;
		victory = false;

		this.xPos = xPos;
		this.yPos = yPos;
		
	}

	public int getXPos(){
		return xPos;
	}

	public int getYPos(){
		return yPos;
	}
	
	public void setTimer(float t) {
		timer = t;
	}
	
	public void update(float dt) {
		if(timer <0){
			width = height = 0;
		}
		
		if(width < totalWidth && height < totalHeight){
			timer += dt;
			width = (timer / maxTime) * totalWidth;
			height = (timer / maxTime) * totalHeight;
			
			if(width<0) width = 0;
			if(height<0) height = 0;
			
			if(width > totalWidth){
				width = totalWidth;
			}
			if(height > totalHeight){
				height = totalHeight;
			}
		}
	}

	public boolean isOccupied(){
		return placed;
	}

	public void placeWhite(){
		tileState = 1;
		placed = true;
	}

	public void placeBlack(){
		tileState = 2;
		placed=true;
	}

	public void setVictory(){
		victory = true;
	}

	public int getTileState(){
		return tileState;
	}
	
	public void render(SpriteBatch sb){
		if(tileState==0){
			sb.draw(boardTex, x-width/2,y-height/2,width,height);
		} else if(tileState==1){
			if(!victory) {
				sb.draw(whiteTex, x - width / 2, y - height / 2, width, height);
			} else {
				sb.draw(whiteVictoryTex, x - width / 2, y - height / 2, width, height);
			}
		} else {
			if(!victory) {
				sb.draw(blackTex, x - width / 2, y - height / 2, width, height);
			} else {
				sb.draw(blackVictoryTex, x - width / 2, y - height / 2, width, height);
			}
		}
	}
	
}
