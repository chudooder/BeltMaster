package org.chu.game;

import java.util.PriorityQueue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Receives and orders render calls so that sprites
 * are drawn in order of depth.
 * @author Shawn
 *
 */
public class RenderQueue {
	
	private PriorityQueue<RenderCall> queue;
	
	public RenderQueue() {
		queue = new PriorityQueue<RenderCall>();
	}
	
	public void draw(TextureRegion region, float x, float y, float z) {
		draw(region, x, y, z, Color.WHITE);
	}
	
	public void draw(TextureRegion region, float x, float y, float z, Color c) {
		queue.add(new RenderCall(region, x, y, z, c));
	}
	
	public void execute(SpriteBatch batch) {
		while(!queue.isEmpty()) {
			RenderCall call = queue.poll();
			batch.setColor(call.color);
			batch.draw(call.region, call.x, call.y);
		}
		RenderCall.callId = 0;
	}

	private static class RenderCall implements Comparable<RenderCall> {
		static int callId = 0;
		TextureRegion region;
		float x;
		float y;
		float z;
		Color color;
		int id;
		
		public RenderCall(TextureRegion region, float x, float y, float z, Color color) {
			this.region = region;
			this.x = x;
			this.y = y;
			this.z = z;
			this.color = color;
			id = callId++;
		}
		
		@Override
		public int compareTo(RenderCall other) {
			// sort by z-depth first
			if(this.z > other.z) {
				return -1;
			} else if(this.z < other.z) {
				return 1;
			}
			
			// group same textures together
			if(region.getTexture().hashCode() > other.region.getTexture().hashCode()) {
				return -1;
			} else if(region.getTexture().hashCode() < other.region.getTexture().hashCode()) {
				return 1;
			}
			
			// otherwise draw in call order
			return id - other.id;
		}
	}

}

