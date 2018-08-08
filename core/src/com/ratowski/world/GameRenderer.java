package com.ratowski.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.ratowski.TweenAccessors.Value;
import com.ratowski.helpers.AssetManager;
import com.ratowski.objects.Enemy;
import com.ratowski.objects.Spell;

public class GameRenderer {

    private GameWorld gameWorld;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Value alpha = new Value();
    private SpriteBatch spriteBatch;
    private int gameHeight;

    public GameRenderer(GameWorld world, int gameHeight) {
        gameWorld = world;
        this.gameHeight = gameHeight;
        camera = new OrthographicCamera();
        camera.setToOrtho(true, 480, gameHeight);
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public void render(float delta, float runTime) {
        resetScreen();
        if (gameWorld.isRunning()) {
            renderOpaqueShapes();
            renderTransparentShapes();
            renderSprites(delta);
        } else if (gameWorld.isGameOver()) {
            renderGameOver();
        }
    }

    private void resetScreen() {
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void renderOpaqueShapes() {
        shapeRenderer.begin(ShapeType.Filled);
        renderEnemies();
        shapeRenderer.end();
    }

    private void renderTransparentShapes() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeType.Filled);
        renderSpells();
        shapeRenderer.end();
    }

    private void renderSprites(float delta) {
        Gdx.gl.glDisable(GL20.GL_BLEND);
        spriteBatch.begin();
        spriteBatch.enableBlending();
        renderParticleEffects(delta);
        AssetManager.font.draw(spriteBatch, gameWorld.getScore() + "", 10, 10);
        AssetManager.font.draw(spriteBatch, String.format("%.2f"), 100, 10);
        spriteBatch.end();
    }

    private void renderGameOver() {
        spriteBatch.begin();
        AssetManager.font.draw(spriteBatch, "GAME OVER", 100, 100);
        spriteBatch.end();
    }

    private void renderSpells() {
        for (int i = 0; i < gameWorld.getSpells().size(); i++) {
            Spell spell = gameWorld.getSpells().get(i);
            shapeRenderer.setColor(spell.getColor()[0], spell.getColor()[1], spell.getColor()[2], (1.6f - spell.getScale().getValue()));
            for (int j = 0; j < spell.getPoints().length - 1; j++) {
                shapeRenderer.rectLine(
                        spell.getPoints()[j + 1].x - (spell.getPosition().x - spell.getPoints()[j + 1].x) * (spell.getScale().getValue() - 1),
                        spell.getPoints()[j + 1].y - (spell.getPosition().y - spell.getPoints()[j + 1].y) * (spell.getScale().getValue() - 1),
                        spell.getPoints()[j].x - (spell.getPosition().x - spell.getPoints()[j].x) * (spell.getScale().getValue() - 1),
                        spell.getPoints()[j].y - (spell.getPosition().y - spell.getPoints()[j].y) * (spell.getScale().getValue() - 1),
                        5 + 10 * (spell.getScale().getValue() - 1)
                );
            }
        }
    }

    private void renderParticleEffects(float delta) {
        for (int i = gameWorld.effects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = gameWorld.effects.get(i);
            effect.draw(spriteBatch, delta);
        }
    }

    private void renderEnemies() {
        for (int i = 0; i < gameWorld.getEnemies().size(); i++) {
            Enemy enemy = gameWorld.getEnemies().get(i);
            if (enemy.isAlive()) {
                shapeRenderer.setColor(enemy.getColor()[0], enemy.getColor()[1], enemy.getColor()[2], alpha.getValue());
            } else {
                shapeRenderer.setColor(0.1f, 0.1f, 0.1f, alpha.getValue());
            }
            shapeRenderer.rect(enemy.getPosition().x, enemy.getPosition().y, enemy.getSize().x, enemy.getSize().y);
        }
    }

}