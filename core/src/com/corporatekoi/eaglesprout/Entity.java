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
	Boolean moveRight = true;
	
	public Entity(int type) {
		position = new Vector3(400, 10, 0);
		loadSprite(type);
	}
	
	public void update() {
		aiTime += 10;
		
		if (aiTime > 30) {
			if (moveRight) {
				position.x += 2;
				if (position.x > 800) moveRight = false;
			} else if (!moveRight) {
    			position.x -= 2;
    			if (position.x < 400) moveRight = true;
			}
			aiTime = 0;
		}
	}
	
	public void render(SpriteBatch batch) {
		animTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		
		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = walkingAnim.getKeyFrame(animTime, true);
		batch.draw(currentFrame, position.x, position.y); // Draw current frame at (50, 50)
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