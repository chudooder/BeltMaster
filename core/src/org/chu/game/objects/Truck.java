package org.chu.game.objects;

import java.util.HashMap;
import java.util.Map;

import org.chu.game.RenderQueue;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Truck extends Entity {
	
	private static final float FRONT_DEPTH = -0.5f;
	private static final float BACK_DEPTH = 0.5f;
	
	private static Map<Color, TextureRegion> fronts;
	private static Map<Color, TextureRegion> boxes;
	private static TextureRegion wheels;
	private static TextureRegion back;
	
	private Color color;
	
	public static void setupAnimations(AssetManager assets) {
		Texture sheet = assets.get("game-objects.png", Texture.class);
		fronts = new HashMap<Color, TextureRegion>();
		boxes = new HashMap<Color, TextureRegion>();
		
		fronts.put(Box.RED, new TextureRegion(sheet, 0, 32, 32, 32));
		fronts.put(Box.GREEN, new TextureRegion(sheet, 32, 32, 32, 32));
		fronts.put(Box.BLUE, new TextureRegion(sheet, 64, 32, 32, 32));
		fronts.put(Box.YELLOW, new TextureRegion(sheet, 96, 32, 32, 32));
		
		boxes.put(Box.RED, new TextureRegion(sheet, 0, 0, 8, 8));
		boxes.put(Box.GREEN, new TextureRegion(sheet, 8, 0, 8, 8));
		boxes.put(Box.BLUE, new TextureRegion(sheet, 0, 8, 8, 8));
		boxes.put(Box.YELLOW, new TextureRegion(sheet, 8, 8, 8, 8));
		
		wheels = new TextureRegion(sheet, 32, 8, 32, 8);
		back = new TextureRegion(sheet, 32, 16, 32, 16);
	}
	
	public Truck(int x, int y, Color color) {
		super(x, y);
		this.color = color;
		hitbox = new Rectangle(x, y, 32, 8);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(float time, RenderQueue queue) {
		queue.draw(wheels, x, y, FRONT_DEPTH);
		queue.draw(fronts.get(color), x, y, FRONT_DEPTH);
		queue.draw(back, x, y+32, BACK_DEPTH);
	}

	public Color getColor() {
		return color;
	}

}
