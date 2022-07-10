package com.corporatekoi.eaglesprout;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	public static OrthographicCamera camera;
	TiledMap map;
	TiledMapRenderer mapRenderer;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        camera.update();
        
        map = new TmxMapLoader().load("maps/Map0.tmx");
        mapRenderer = new IsometricTiledMapRenderer(map);
        
        InputHandler inputProcessor = new InputHandler();
        Gdx.input.setInputProcessor(inputProcessor);
	}
	
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		camera.update();
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
	
	@Override
	public void dispose () {
		map.dispose();
	}
}