package org.chu.game.objects;

import java.util.HashMap;
import java.util.Map;

import org.chu.game.BeltMaster;
import org.chu.game.render.RenderQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Truck extends Entity {
	
	private static final float FRONT_DEPTH = -0.5f;
	private static final float BACK_DEPTH = 0.5f;
	
	private static Map<Color, TextureRegion> fronts;
	private static TextureRegion wheels;
	private static TextureRegion back;
	
	private Color color;
	private float bounceTimer = -1;
	private float bounceOffset = 0;
	
	public static void setupAnimations(BeltMaster beltMaster) {
		Texture sheet = beltMaster.getTexture("game-objects");
		fronts = new HashMap<Color, TextureRegion>();

		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/4, sheet.getHeight()/2);
		
		fronts.put(Box.RED, tmp[0][0]);
		fronts.put(Box.GREEN, tmp[0][1]);
		fronts.put(Box.BLUE, tmp[0][2]);
		fronts.put(Box.YELLOW, tmp[0][3]);
		
		wheels = new TextureRegion(sheet, sheet.getWidth()/4, 
				sheet.getHeight()/8*7, sheet.getWidth()/4, sheet.getHeight()/8);
		back = new TextureRegion(sheet, 0, sheet.getHeight()/4*2, sheet.getWidth()/4, sheet.getHeight()/4);
	}
	
	public Truck(int x, int y, Color color) {
		super(x, y);
		this.color = color;
		hitbox = new Rectangle(x, y, 64, 16);
	}

	@Override
	public void update() {
		if(bounceTimer >= 0) {
			bounceTimer += Gdx.graphics.getDeltaTime();
			bounceOffset = (float) (3 * Math.sin(bounceTimer * 2 * Math.PI));
			if(bounceTimer > 0.5f) {
				bounceTimer = -1;
				bounceOffset = 0;
			}
		}
	}

	@Override
	public void render(float time, RenderQueue queue) {
		queue.draw(wheels, x, y, FRONT_DEPTH);
		queue.draw(fronts.get(color), x, y - bounceOffset, FRONT_DEPTH);
		queue.draw(back, x, y+64 - bounceOffset, BACK_DEPTH);
	}

	public Color getColor() {
		return color;
	}
	
	public void startBounce() {
		bounceTimer = 0;
	}

}
