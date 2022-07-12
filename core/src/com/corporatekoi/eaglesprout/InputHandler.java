package com.corporatekoi.eaglesprout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
	EagleSprout game;
	
	public InputHandler(EagleSprout game) {
		this.game = game;
	}
	
    public boolean keyDown (int keycode) {
    	switch (keycode) {
    		case Input.Keys.UP :
    			game.keyUpPressed = true;
    		case Input.Keys.DOWN :
    			game.keyDownPressed = true;
    		case Input.Keys.LEFT :
    			game.keyLeftPressed = true;
    		case Input.Keys.RIGHT :
    			game.keyRightPressed = true;
    	}
    	return true;
    }
    
    public boolean keyUp (int keycode) {
    	switch (keycode) {
    		case Input.Keys.UP :
    			game.keyUpPressed = false;
    		case Input.Keys.DOWN :
    			game.keyDownPressed = false;
    		case Input.Keys.LEFT :
    			game.keyLeftPressed = false;
    		case Input.Keys.RIGHT :
    			game.keyRightPressed = false;
    	}
    	return true;
    }
    
    public boolean keyTyped (char character) {
    	return false;
    }
    
    public boolean touchDown (int x, int y, int pointer, int button) {
    	return false;
    }
    
    public boolean touchUp (int x, int y, int pointer, int button) {
    	return false;
    }
    
    public boolean touchDragged (int x, int y, int pointer) {
    	game.camera.translate(-Gdx.input.getDeltaX() * 2, Gdx.input.getDeltaY() * 2);
    	game.camera.update();
    	return true;
    }
    
    public boolean mouseMoved (int x, int y) {
    	return false;
    }
    
    public boolean scrolled (float amountX, float amountY) {
    	if (amountY > 0) {
    		if (game.camera.zoom < 2.2) game.camera.zoom += 0.2;
    		game.camera.update();
    	} else if (amountY < 0) {
    		if (game.camera.zoom > 0.8) game.camera.zoom -= 0.2;
    		game.camera.update();
    	}
    	return true;
    }
}