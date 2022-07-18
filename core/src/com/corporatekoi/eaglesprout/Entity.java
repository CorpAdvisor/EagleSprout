package com.corporatekoi.eaglesprout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Entity {
	
	Texture spriteSheet;
	Animation<TextureRegion> walkingAnim;
	float animTime;
	float aiTime;
	Vector3 position;
	Boolean moving = true;
	Vector3 targetPosition;
	float waitToMove = 5.0f;
	
	int mapWidth = 29;
	int mapHeight = 29;
	int scale = 64;
	
	private Matrix4 isoTransform;
	private Vector3 screenPos = new Vector3();
	
	public Entity(int type, int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		position = new Vector3(randomInt(0, mapWidth * scale), randomInt(0, mapHeight * scale), 0);
		loadSprite(type);
		
		generateNewPosition();
		
		isoTransform = new Matrix4();
		isoTransform.idt();
		
		isoTransform.scale((float)(Math.sqrt(2.0) / 2.0), (float)(Math.sqrt(2.0) / 4.0), 1.0f);
		isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);
	}
	
	private Vector3 worldToIso(Vector3 vec) {
		screenPos.set(vec.x, vec.y, 0);
		screenPos.mul(isoTransform);
		screenPos.x += 16;
		screenPos.y += 8;
		
		return screenPos;
	}
	
	public void update() {
		if (!moving) {
    		aiTime += Gdx.graphics.getDeltaTime();
    		
    		if (aiTime > waitToMove) {
    			moving = true;
    			generateNewPosition();
    			aiTime = 0;
    		}
		} else if (moving) {
			aiTime += Gdx.graphics.getDeltaTime();
			
			if (aiTime > 0.06f) {
    			processMovement();
    			
    			if (position.x == targetPosition.x && position.y == targetPosition.y) {
    				moving = false;
    				waitToMove = (float)randomInt(3, 6);
    			}
    			
    			aiTime = 0;
			}
		}
	}
	
	private void generateNewPosition() {
		int randomX = 0, randomY = 0;
		
		if (position.x == 0) {
			randomX = randomInt(0, 800);
		} else {
			randomX = randomInt(-800, 800);
		}
		
		if (position.y == 0) {
			randomY = randomInt(0, 800);
		} else {
			randomY = randomInt(-800, 800);
		}
		
		targetPosition = clampMapSize(position.x + randomX, position.y + randomY);
	}
	
	private Vector3 clampMapSize(float x, float y) {
		Vector3 position = new Vector3(x, y, 0);
		
		if (x < 0) {
			position.x = 0;
		} else if (x > mapWidth * scale) {
			position.x = mapWidth * scale;
		}
		
		if (y < 0) {
			position.y = 0;
		} else if (y > mapHeight * scale) {
			position.y = mapHeight * scale;
		}
		
		return position;
	}
	
	private void processMovement() {
		if (targetPosition.x > position.x) {
			position.x += 4;
			if (targetPosition.x < position.x) position.x = targetPosition.x;
		} else if (targetPosition.x < position.x) {
			position.x -= 4;
			if (targetPosition.x > position.x) position.x = targetPosition.x;
		}
		
		if (targetPosition.y > position.y) {
			position.y += 4;
			if (targetPosition.y < position.y) position.y = targetPosition.y;
		} else if (targetPosition.y < position.y) {
			position.y -= 4;
			if (targetPosition.y > position.y) position.y = targetPosition.y;
		}
	}
	
	private int randomInt(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public void render(SpriteBatch batch) {
		animTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		
		// Get current frame of animation for the current stateTime
		if (moving) {
			TextureRegion currentFrame = walkingAnim.getKeyFrame(animTime, true);
			//transparency fun for use later - batch.setColor(1f, 1f, 1f, 0.5f);
			batch.draw(currentFrame, worldToIso(position).x, worldToIso(position).y);
			//reset the transparency - batch.setColor(1f, 1f, 1f, 1f);
		} else if (!moving) {
			TextureRegion currentFrame = walkingAnim.getKeyFrame(0, false);
			batch.draw(currentFrame, worldToIso(position).x, worldToIso(position).y);
		}
	}
	
	private void loadSprite(int type) {
		
		switch (type) {
			case 0 :
				spriteSheet = new Texture(Gdx.files.internal("graphics/sprites/fox.png"));
		}
		
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
				spriteSheet.getWidth() / 8,
				spriteSheet.getHeight());
		
		TextureRegion[] walkFrames = new TextureRegion[8];
		int index = 0;
		for (int i = 0; i < 8; i++) {
			walkFrames[index++] = tmp[0][i];
		}
		
		walkingAnim = new Animation<TextureRegion>(0.07f, walkFrames);
		
		animTime = 0f;
	}
	
	public void dispose() {
		spriteSheet.dispose();
	}
}