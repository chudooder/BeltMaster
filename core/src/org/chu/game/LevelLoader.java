package org.chu.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.chu.game.objects.Box;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

public class LevelLoader {
	
	private BeltMaster game;
	private FileHandle levelsFile;
	private List<String> levels;
	
	public LevelLoader(BeltMaster game) {
		this.game = game;
		levelsFile = Gdx.files.internal("levels/levels.bml");
		levels = new ArrayList<String>();
		Scanner in = new Scanner(levelsFile.readString());
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.isEmpty()) continue;
			if(line.startsWith("#")) continue;
			levels.add(line);
		}
		in.close();
	}
	
	public GameScreen loadLevel(int level) {
		GameScreen screen = new GameScreen(game);
		String name = levels.get(level);
		FileHandle levelFile = Gdx.files.internal("levels/"+name+".bml");
		Scanner in = new Scanner(levelFile.readString());
		
		List<Color> pool = new ArrayList<Color>();
		
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.isEmpty()) continue;
			if(line.startsWith("#")) continue;
			
			if(line.startsWith(".pool")) {
				String[] parse = line.split(" ");
				for(int i=1; i<parse.length; i++) {
					String[] block = parse[i].split("x");
					for(int j=0; j<Integer.parseInt(block[1]); j++) {
						pool.add(getColor(block[0]));
					}
				}
			}
			else if(line.startsWith(".belt")) {
				String[] parse = line.split(" ");
				int x = Integer.parseInt(parse[1]) * 16;
				int y = (15-Integer.parseInt(parse[2])) * 16;
				int length = Integer.parseInt(parse[3]);
				int init = Integer.parseInt(parse[4]);
				boolean locked = Boolean.parseBoolean(parse[5]);
				screen.createBelt(x, y, length, init, locked);
			}
			else if(line.startsWith(".truck")) {
				String[] parse = line.split(" ");
				int x = Integer.parseInt(parse[1]) * 16 - 8;
				Color color = getColor(parse[2]);
				screen.createTruck(x, color);
			}
			else if(line.startsWith(".spawner")) {
				String[] parse = line.split(" ");
				int x = Integer.parseInt(parse[1]) * 16 - 8;
				float spawnTime = Float.parseFloat(parse[2]);
				float offset = Float.parseFloat(parse[3]);
				Queue<Color> queue = new LinkedList<Color>();
				for(char c : parse[4].toCharArray()) {
					if(c == '-') {
						queue.add(null);
					} else {
						Color col = getColor(c+"");
						if(col == null) {
							queue.add(pool.remove((int)(Math.random()*pool.size())));
						} else {
							queue.add(col);
						}
					}
				}
				screen.createSpawner(x, queue, spawnTime, offset);
			}
		}
		in.close();
		return screen;
	}
	
	private Color getColor(String col) {
		if(col.equals("R")) return Box.RED;
		if(col.equals("B")) return Box.BLUE;
		if(col.equals("G")) return Box.GREEN;
		if(col.equals("Y")) return Box.YELLOW;
		return null;
	}
	
}
