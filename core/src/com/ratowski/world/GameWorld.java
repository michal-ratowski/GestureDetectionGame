package com.ratowski.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ratowski.helpers.OverlayInterface;
import com.ratowski.objects.Enemy;
import com.ratowski.objects.Spell;

import java.util.ArrayList;
import java.util.Random;

public class GameWorld {

    public OverlayInterface overlayInterface;
    private int score = 0;
    private float scaleFactorX, scaleFactorY;

    public boolean timerOn = false;
    private float time = 0;

    private ParticleEffect effect;
    ParticleEffectPool effectPool;
    Array<ParticleEffectPool.PooledEffect> effects = new Array();

    public enum GameState {READY, RUNNING, GAMEOVER}

    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<Spell> spells = new ArrayList<Spell>();
    private GameState currentState;

    public GameWorld(float scaleFactorX, float scaleFactorY, OverlayInterface overlayInterface) {
        currentState = GameState.RUNNING;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        this.overlayInterface = overlayInterface;
        setupEffects();
        spawnEnemies();
        resetTimer(1000);
    }
    
    public void update(float delta) {
        if (delta > .15f) {
            delta = .15f;
        }
        updateTimer(delta);
        updateSpells(delta);
        updateEnemies(delta);
        updateEffects();
    }

    public int getScore() {
        return score;
    }

    public void start() {
        currentState = GameState.RUNNING;
        restart();
    }

    public void ready() {
        currentState = GameState.READY;
        start();

    }

    public void restart() {
        currentState = GameState.RUNNING;
        score = 0;
        resetTimer(1000);
        timerOn = false;
        enemies.clear();
        spawnEnemies();
        overlayInterface.enable();
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    public void passGesture(double score, String name, float x, float y, float width, float height, Vector2[] points) {
        //System.out.println("Gesture received! " + name + " with score: " + score);
        for (int i = 0; i < points.length; i++) {
            points[i].x /= scaleFactorX;
            points[i].y /= scaleFactorY;
        }
        Spell spell = new Spell(getSpellType(name), new Vector2(x / scaleFactorX, y / scaleFactorY), new Vector2(width, height), 0.5f, points);
        spells.add(spell);
    }

    private void updateTimer(float delta) {
        if (timerOn) {
            time -= delta;
            if (time < 0) {
                timerOn = false;
                overlayInterface.disable();
                currentState = GameState.GAMEOVER;
            }
        }
    }

    private void updateSpells(float delta) {
        for (int i = 0; i < spells.size(); i++) {
            Spell spell = spells.get(i);
            spell.update(delta);
            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                if (enemy.hit(spell)) {
                    enemy.isHit(spell);
                }
            }
        }
        for (int i = 0; i < spells.size(); i++) {
            Spell spell = spells.get(i);
            if (spell.isFinished()) {
                spells.remove(i);
            }
        }
    }

    public void addSpellEffect(float x, float y) {
        ParticleEffectPool.PooledEffect effect = effectPool.obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }

    int getSpellType(String name) {
        if (name.equals("triangle")) {
            return 1;
        } else if (name.equals("square")) {
            return 2;
        } else if (name.equals("circle")) {
            return 3;
        } else {
            return 4;
        }
    }

    private void updateEnemies(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(delta);
        }
        if (allEnemiesDead()) {
            restart();
        }
    }

    private boolean allEnemiesDead() {
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).isAlive()) {
                return false;
            }
        }
        return true;
    }

    private void spawnEnemies() {
        resetTimer(1000);
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Enemy enemy = new Enemy(new Vector2(40 + 200 * i, 50 + 200 * j), 0, new Vector2(200, 200), random.nextInt(3) + 1);
                enemies.add(enemy);
            }
        }
    }

    private void updateEffects() {
        for (int i = effects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            if (effect.findEmitter("good").getPercentComplete() >= 0.5) {
                effect.free();
                effects.removeIndex(i);
            }
        }

    }

    private void setupEffects() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("data/effects/shatter.p"), Gdx.files.internal("data/effects/"));
        effectPool = new ParticleEffectPool(effect, 1, 1);
    }

    private void resetTimer(float time) {
        this.time = time;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

}
