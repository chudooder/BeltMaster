package org.chu.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.chu.game.objects.Box;
import org.chu.game.screen.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

public class LevelLoader {

    private BeltMaster game;
    private FileHandle levelsFile;
    private List<String> levels;

    /**
     * Levels are 25x15. Origin is bottom left.
     */

    public LevelLoader(BeltMaster game) {
        this.game = game;
        levelsFile = Gdx.files.internal("levels/levels.bml");
        levels = new ArrayList<String>();
        Scanner in = new Scanner(levelsFile.readString());
        while(in.hasNextLine()) {
            String line = in.nextLine();
            if(line.trim().length() == 0) continue;
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
        int maxMiss = Integer.MAX_VALUE;
        int maxScore = 0;

        while(in.hasNextLine()) {
            String line = in.nextLine();
            if(line.length() == 0) continue;
            if(line.startsWith("#")) continue;

            if(line.startsWith(".miss")) {
                maxMiss = Integer.parseInt(line.split(" ")[1]);
            }
            else if(line.startsWith(".pool")) {
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
                int x = Integer.parseInt(parse[1]) * Constants.GRID_SIZE;
                int y = (15-Integer.parseInt(parse[2])) * 32;
                int length = Integer.parseInt(parse[3]);
                int init = Integer.parseInt(parse[4]);
                boolean locked = Boolean.parseBoolean(parse[5]);
                screen.createBelt(x, y, length, init, locked);
            }
            else if(line.startsWith(".truck")) {
                String[] parse = line.split(" ");
                int x = Integer.parseInt(parse[1]) * Constants.GRID_SIZE - Constants.GRID_SIZE / 2;
                Color color = getColor(parse[2]);
                screen.createTruck(x, color);
            }
            else if(line.startsWith(".spawner")) {
                String[] parse = line.split(" ");
                int x = Integer.parseInt(parse[1]) * Constants.GRID_SIZE - Constants.GRID_SIZE / 2;
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
                maxScore += queue.size();
                screen.createSpawner(x, queue, spawnTime, offset);
            }

            else if(line.startsWith(".recycler")) {
                String[] parse = line.split(" ");
                int x = Integer.parseInt(parse[1]) * Constants.GRID_SIZE - Constants.GRID_SIZE / 2;
                screen.createRecycler(x);
            }

            else if(line.startsWith(".boxapult")) {
                String[] parse = line.split(" ");
                System.out.println(parse[1]);
                int x = Integer.parseInt(parse[1]) * Constants.GRID_SIZE;
                int y = (15-Integer.parseInt(parse[2])) * Constants.GRID_SIZE;
                int destX = Integer.parseInt(parse[3]) * Constants.GRID_SIZE;
                int destY = (15-Integer.parseInt(parse[4])) * Constants.GRID_SIZE;
                screen.createBoxapult(x, y, destX, destY);
            }
        }
        screen.setScore(maxScore, maxMiss);
        in.close();
        return screen;
    }

    public List<String> getLevelNames() {
        return levels;
    }

    private Color getColor(String col) {
        if(col.equals("R")) return Box.RED;
        if(col.equals("B")) return Box.BLUE;
        if(col.equals("G")) return Box.GREEN;
        if(col.equals("Y")) return Box.YELLOW;
        return null;
    }

}
