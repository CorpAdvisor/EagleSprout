package com.corporatekoi.eaglesprout;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class EagleSprout extends ApplicationAdapter {
	OrthographicCamera camera;
	TiledMap map;
	TiledMapRenderer mapRenderer;
	SpriteBatch batch;
	ArrayList<Entity> Animal = new ArrayList<Entity>();
	Base Hub;
	BitmapFont font;
	Matrix4 projectionDefault;
	Music bgMusic;
	
	final int ANIMAL_TYPE_BIRD = 0;
	final int ANIMAL_TYPE_FOX = 1;
	
	final int mapWidth = 29;
	final int mapHeight = 29;
	final int scale = 64;
	
	Boolean keyUpPressed = false;
	Boolean keyDownPressed = false;
	Boolean keyLeftPressed = false;
	Boolean keyRightPressed = false;
	
	private Matrix4 isoTransform;
	private Vector3 screenPos = new Vector3();
	Vector3 testMouse = new Vector3(0, 0, 0);
	
	@Override
	public void create () {
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("music/ingame.mp3"));
		bgMusic.setLooping(true);
		bgMusic.setVolume(0.1f);
		bgMusic.play();
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        camera.update();
        
        projectionDefault = new Matrix4();
		projectionDefault.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
		batch = new SpriteBatch();
		
        map = new TmxMapLoader().load("maps/Map0.tmx");
        mapRenderer = new IsometricTiledMapRenderer(map, batch);
        
        font = new BitmapFont();
        
        InputHandler inputProcessor = new InputHandler(this);
        Gdx.input.setInputProcessor(inputProcessor);
        
        isoTransform = new Matrix4();
		isoTransform.idt();
		
		isoTransform.scale((float)(Math.sqrt(2.0) / 2.0), (float)(Math.sqrt(2.0) / 4.0), 1.0f);
		isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);
		isoTransform.inv();
	}
	
	public int randomInt(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	private Vector3 worldToIso(Vector3 vec) {
		screenPos.set(vec.x, vec.y, 0);
		screenPos.mul(isoTransform);
		
		return screenPos;
	}
	
	public void createBase(float x, float y) {
		if (Hub == null) {
			Vector3 position = new Vector3(x, y, 0);
			camera.unproject(position);
			position = worldToIso(position);
			Hub = new Base(this, 0, mapWidth, mapHeight, position.x, position.y);
		}
	}
	
	public void createEntity(int type, float x, float y) {
		Animal.add(new Entity(this, type, mapWidth, mapHeight, x, y));
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
		
		if (Hub != null) Hub.processBase();
		
		if (!Animal.isEmpty()) {
    		for (int i = 0; i < Animal.size(); i++) {
            	Animal.get(i).update();
            }
		}
		
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		
		// start the main drawing
		batch.begin();
		
		mapRenderer.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Bottom"));
		mapRenderer.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Mid"));
		
		if (!Animal.isEmpty()) {
    		for (int i = 0; i < Animal.size(); i++) {
            	Animal.get(i).render(batch);
            }
		}
		
		if (Hub != null) Hub.render(batch);
		
		mapRenderer.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Top"));
		
		// end the main drawing
		batch.end();
		
		// separate the top level drawing for UI
		batch.setProjectionMatrix(projectionDefault);
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight() - 5);
		font.draw(batch, "Mouse X: " + Gdx.input.getX() + ", Y: " + Gdx.input.getY(), 5, Gdx.graphics.getHeight() - 25);
		
		// testing mouse to iso coords
		testMouse.x = Gdx.input.getX();
		testMouse.y = Gdx.input.getY();
		
		camera.unproject(testMouse);
		
		font.draw(batch, "Mouse Iso X: " + worldToIso(testMouse).x + ", Y: " + worldToIso(testMouse).y, 5, Gdx.graphics.getHeight() - 45);
		
		font.draw(batch, "Camera X: " + camera.position.x + ", Y: " + camera.position.y, 5, Gdx.graphics.getHeight() - 65);
		batch.end();
	}
	
	@Override
	public void dispose () {
		if (!Animal.isEmpty()) {
    		for (int i = 0; i < Animal.size(); i++) {
            	Animal.get(i).dispose();
            }
		}
		if (Hub != null) Hub.dispose();
		map.dispose();
		batch.dispose();
		bgMusic.dispose();
	}
}