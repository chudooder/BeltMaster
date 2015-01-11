package org.chu.game.objects;

import java.util.Queue;

import org.chu.game.RenderQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spawner extends Entity {
	
	private static final float DEPTH = -0.5f;
	
	private float spawnTimer;			// internal clock
	private float spawnTime;			// time between block spawns
	private Queue<Color> spawnColors;	// queue of blocks to spawn
	
	private static TextureRegion sprite;
	
	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("game-objects.png", Texture.class);
		sprite = new TextureRegion(sheet, 32, 96, 32, 32);
	}

	public Spawner(float x, float y, Queue<Color> spawnColors, float spawnTime, float offset) {
		super(x, y);
		this.spawnColors = spawnColors;
		this.spawnTime = spawnTime;
		this.spawnTimer = offset;
	}

	@Override
	public void update() {
		spawnTimer += Gdx.graphics.getDeltaTime();
		if(spawnTimer > spawnTime) {
			// adjust timer
			spawnTimer -= spawnTime;
			// spawn a block
			if(!spawnColors.isEmpty()) {
				Color color = spawnColors.poll();
				if(color != null) {
					Box box = new Box(x + 8, y + 0, color);
					screen.addEntity(box);
				}
			}
		}
	}
	
	public void recycleBlock(Color color) {
		spawnColors.add(color);
	}

	@Override
	public void render(float time, RenderQueue queue) {
		float offsetY = 0;
		if(spawnTimer > spawnTime - 0.5f && spawnTimer < spawnTime - 0.1f) {
			offsetY = (spawnTimer + 0.5f - spawnTime) * 10;
		} else if (spawnTimer >= spawnTime - 0.1f) {
			offsetY = (spawnTime - spawnTimer) * 10;
		}
		queue.draw(sprite, x, y + offsetY, DEPTH);
	}

}
