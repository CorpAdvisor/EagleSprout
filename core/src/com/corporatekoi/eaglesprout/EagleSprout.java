package com.corporatekoi.eaglesprout;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class EagleSprout extends ApplicationAdapter {
	OrthographicCamera camera;
	TiledMap map;
	TiledMapRenderer mapRenderer;
	SpriteBatch batch;
	Entity Fox;
	
	Boolean keyUpPressed = false;
	Boolean keyDownPressed = false;
	Boolean keyLeftPressed = false;
	Boolean keyRightPressed = false;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        camera.update();
        
        map = new TmxMapLoader().load("maps/Map0.tmx");
        mapRenderer = new IsometricTiledMapRenderer(map);
        
        Fox = new Entity(0);
        
        batch = new SpriteBatch();
        
        InputHandler inputProcessor = new InputHandler(this);
        Gdx.input.setInputProcessor(inputProcessor);
	}
	
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		
		if (keyUpPressed) {
			camera.translate(0, 8);
			camera.update();
		} else if (keyDownPressed) {
			camera.translate(0, -8);
			camera.update();
		} else if (keyLeftPressed) {
			camera.translate(-8, 0);
			camera.update();
		} else if (keyRightPressed) {
			camera.translate(8, 0);
			camera.update();
		}
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		Fox.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Fox.render(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		Fox.dispose();
		map.dispose();
	}
}