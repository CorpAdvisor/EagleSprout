package com.corporatekoi.eaglesprout;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;

public class EagleSprout extends ApplicationAdapter {
	OrthographicCamera camera;
	TiledMap map;
	TiledMapRenderer mapRenderer;
	SpriteBatch batch;
	Entity Fox[] = new Entity[100];
	BitmapFont font;
	Matrix4 projectionDefault;
	
	final int mapWidth = 29;
	final int mapHeight = 29;
	final int scale = 64;
	
	Boolean keyUpPressed = false;
	Boolean keyDownPressed = false;
	Boolean keyLeftPressed = false;
	Boolean keyRightPressed = false;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        camera.update();
        
        projectionDefault = new Matrix4();
		projectionDefault.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
		batch = new SpriteBatch();
		
        map = new TmxMapLoader().load("maps/Map0.tmx");
        mapRenderer = new IsometricTiledMapRenderer(map, batch);
        
        font = new BitmapFont();
        
        for (int i = 0; i < Fox.length; i++) {
        	Fox[i] = new Entity(0, mapWidth, mapHeight);
        }
        
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
		
		for (int i = 0; i < Fox.length; i++) {
        	Fox[i].update();
        }
		
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		
		// start the main drawing
		batch.begin();
		
		mapRenderer.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Bottom"));
		mapRenderer.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Mid"));
		
		for (int i = 0; i < Fox.length; i++) {
        	Fox[i].render(batch);
        }
		
		mapRenderer.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Top"));
		
		// end the main drawing
		batch.end();
		
		// separate the top level drawing for UI
		batch.setProjectionMatrix(projectionDefault);
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight() - 5);
		batch.end();
	}
	
	@Override
	public void dispose () {
		for (int i = 0; i < Fox.length; i++) {
        	Fox[i].dispose();
        }
		map.dispose();
		batch.dispose();
	}
}