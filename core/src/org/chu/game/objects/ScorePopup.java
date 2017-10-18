package org.chu.game.objects;

import org.chu.game.BeltMaster;
import org.chu.game.render.RenderQueue;
import org.chu.game.render.SpriteSheet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScorePopup extends Entity {

    private static float SPEED = 64f;
    private static float DEPTH = -0.5f;
    private static TextureRegion goodTex;
    private static TextureRegion missTex;

    private static Sound score;
    private static Sound miss;

    private TextureRegion type;
    private float timer;
    private float vy;

    public static void setupAnimations(BeltMaster beltMaster) {
        SpriteSheet sheet = beltMaster.getSpriteSheet("game-objects");
        goodTex = sheet.getRegion(8, 4, 4, 2);
        missTex = sheet.getRegion(8, 6, 4, 2);

        score = beltMaster.getSound("score");
        miss = beltMaster.getSound("miss");
    }

    public ScorePopup(float x, float y, boolean good) {
        super(x, y);
        type = good ? goodTex : missTex;
        vy = SPEED;
        timer = 0;
        Sound sound = good ? score : miss;
        sound.play();
    }

    @Override
    public void update(double dt) {
        y += vy * dt;
        vy -= 64f * dt;
        timer += dt;

        if(timer > 1.0f) screen.removeEntity(this);
    }

    @Override
    public void render(float time, RenderQueue queue) {
        queue.draw(type, x, y, DEPTH);
    }

}
