package com.corporatekoi.eaglesprout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Base {
	
	EagleSprout game;
	
	Texture baseSheet;
	Sprite baseSprite;
	Vector3 position;
	
	int mapWidth = 29;
	int mapHeight = 29;
	int scale = 64;
	
	float aiTime = 0;
	
	private Matrix4 isoTransform;
	private Vector3 screenPos = new Vector3();
	
	public Base(EagleSprout game, int type, int mapWidth, int mapHeight, float positionX, float positionY) {
		this.game = game;
		
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		loadSprite(type);
		position = clampMapSize(positionX - (baseSprite.getWidth() / 2), positionY - (baseSprite.getHeight() / 2));
		
		game.createFox(position.x + baseSprite.getWidth(), position.y);
		
		isoTransform = new Matrix4();
		isoTransform.idt();
		
		isoTransform.scale((float)(Math.sqrt(2.0) / 2.0), (float)(Math.sqrt(2.0) / 4.0), 1.0f);
		isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);
	}
	
	public Vector3 getCollision() {
		return position;
	}
	
	private Vector3 worldToIso(Vector3 vec) {
		screenPos.set(vec.x, vec.y, 0);
		screenPos.mul(isoTransform);
		
		return screenPos;
	}
	
	private Vector3 clampMapSize(float x, float y) {
		Vector3 position = new Vector3(x, y, 0);
		
		if (x < 0) {
			position.x = baseSprite.getWidth() / 2;
		} else if (x > mapWidth * scale) {
			position.x = mapWidth * scale - 15;
		}
		
		if (y < 0) {
			position.y = -(baseSprite.getHeight() / 2) + 10;
		} else if (y + baseSprite.getHeight() + 50 > mapHeight * scale) {
			position.y = mapHeight * scale - baseSprite.getHeight() - 40;
		}
		
		return position;
	}
	
	public void processBase() {
		aiTime += Gdx.graphics.getDeltaTime();
		
		if (aiTime > 10.0f) {
			game.createFox(position.x + baseSprite.getWidth(), position.y + baseSprite.getHeight());
			aiTime = 0;
		}
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(baseSprite, worldToIso(position).x, worldToIso(position).y);
	}
	
	private void loadSprite(int type) {
		
		switch (type) {
			case 0 :
				baseSheet = new Texture(Gdx.files.internal("graphics/bases/base0.png"));
				baseSprite = new Sprite(baseSheet);
		}
	}
	
	public void dispose() {
		baseSheet.dispose();
	}
}