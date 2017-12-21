package org.chu.game.ui;

import org.chu.game.BeltMaster;
import org.chu.game.objects.Entity;
import org.chu.game.render.RenderQueue;
import org.chu.game.screen.GameScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScoreHUD extends Entity {

    private static final float DEPTH = -1.0f;
    private static TextureRegion overlay;
    private static TextureRegion fillTex;
    private static ShapeRenderer renderer;
    private static BitmapFont font;

    private int maxScore;
    private int maxMiss;
    private int score;
    private int miss;


    public static void setupAnimations(BeltMaster game) {
        overlay = new TextureRegion(game.getTexture("scorebar-overlay"));
        fillTex = new TextureRegion(game.getTexture("scorebar-fill"));
        renderer = new ShapeRenderer();
        font = game.getFont("vcr-osd-mono");
    }

    public ScoreHUD() {
        super(0, 0);
        this.maxScore = Integer.MAX_VALUE;
        this.maxMiss = Integer.MAX_VALUE;
        this.score = 0;
        this.miss = 0;
    }

    public ScoreHUD(int maxScore, int maxMiss) {
        super(0, 0);
        this.maxScore = maxScore;
        this.maxMiss = maxMiss;
        this.score = 0;
        this.miss = 0;
    }

    public void set(int maxScore, int maxMiss) {
        this.maxScore = maxScore;
        this.maxMiss = maxMiss;
    }

    @Override
    public void update(double dt) {

    }

    public void score() {
        score++;
        if(score + miss == maxScore) {
            screen.endGame(GameScreen.GameOutcome.WON);
        }
    }

    public void miss() {
        miss++;
        if(miss >= maxMiss) {
            screen.endGame(GameScreen.GameOutcome.LOST);
        } else if(score + miss == maxScore) {
            screen.endGame(GameScreen.GameOutcome.WON);
        }
    }

    public void reset() {
        score = 0;
        miss = 0;
    }

    @Override
    public void render(float time, RenderQueue queue) {
        float oX = 800 - 138;
        float oY = screen.getTopY() / screen.getGame().getScale() - 26;
        float barWidth = 128.0f / maxScore;
        float scoreWidth = score * barWidth;
        float missX = oX + (maxScore - maxMiss) * barWidth;
        float missWidth = (maxMiss - miss) * barWidth;
        float width = 128.0f - miss * barWidth;
        float height = 16f;



        queue.draw(fillTex, oX-1, oY-1, DEPTH, Color.BLACK, width+2, height+2);
//        queue.draw(fillTex, oX + scoreWidth, oY, DEPTH,
//                new Color(0.1f, 0.1f, 0.1f, 1f), missX - oX - scoreWidth, height);
        queue.draw(fillTex, missX, oY, DEPTH, Color.RED, missWidth, height);
        queue.draw(fillTex, oX, oY, DEPTH, Color.GREEN, scoreWidth, height);
        queue.draw(overlay, oX, oY, DEPTH, Color.WHITE, width, height);
        queue.print(font, String.format("%d/%d", score, maxScore), oX, oY, DEPTH, Color.GREEN);
        queue.print(font, String.format("%5s", String.format("%d/%d", miss, maxMiss)), oX + 80, oY, DEPTH, Color.RED);
    }
}
