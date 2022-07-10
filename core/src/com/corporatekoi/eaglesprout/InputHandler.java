package com.corporatekoi.eaglesprout;

import com.corporatekoi.eaglesprout.EagleSprout;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
	OrthographicCamera camera;
	
	public InputHandler() {
		camera = EagleSprout.camera;
	}
	
    public boolean keyDown (int keycode) {
    	return false;
    }
    
    public boolean keyUp (int keycode) {
    	return false;
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
    	return false;
    }
    
    public boolean mouseMoved (int x, int y) {
    	return false;
    }
    
    public boolean scrolled (float amountX, float amountY) {
    	if (amountY > 0) {
    		if (camera.zoom < 2.2) camera.zoom += 0.2;
    	} else if (amountY < 0) {
    		if (camera.zoom > 0.8) camera.zoom -= 0.2;
    	}
    	return true;
    }
}