package com.corporatekoi.eaglesprout;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.math.Matrix4;

public class EagleSprout extends ApplicationAdapter {
	OrthographicCamera camera;
	TiledMap map;
	TiledMapRenderer mapRenderer;
	SpriteBatch batch;
	Entity Fox;
	BitmapFont font;
	Matrix4 projectionDefault;
	
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
        
        map = new TmxMapLoader().load("maps/Map0.tmx");
        mapRenderer = new IsometricTiledMapRenderer(map);
        
        font = new BitmapFont();
        
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
		
		batch.setProjectionMatrix(projectionDefault);
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight() - 5);
		batch.end();
	}
	
	@Override
	public void dispose () {
		Fox.dispose();
		map.dispose();
		batch.dispose();
	}
}