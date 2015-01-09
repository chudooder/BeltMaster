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
	}

	private class RenderCall implements Comparable<RenderCall> {
		TextureRegion region;
		float x;
		float y;
		float z;
		Color color;
		
		public RenderCall(TextureRegion region, float x, float y, float z, Color color) {
			this.region = region;
			this.x = x;
			this.y = y;
			this.z = z;
			this.color = color;
		}
		
		@Override
		public int compareTo(RenderCall other) {
			if(this.z > other.z) {
				return -1;
			} else if(this.z < other.z) {
				return 1;
			} else {
				return region.hashCode() - other.region.hashCode();
			}
		}
	}

}

