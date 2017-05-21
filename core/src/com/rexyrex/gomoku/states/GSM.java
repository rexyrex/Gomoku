package com.rexyrex.gomoku.states;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GSM {

	private Stack<State> states;
	
	public GSM() {
		states = new Stack<State>();		
	}
	
	public void push(State s){
		states.push(s);
	}
	
	public void pop(){
		states.pop();
	}
	
	public void set(State s){
		states.pop();
		states.push(s);
	}
	
	public void update(float dt){
		states.peek().update(dt);
	}
	
	public void render(SpriteBatch sb){
		states.peek().render(sb);
	}
	
}
