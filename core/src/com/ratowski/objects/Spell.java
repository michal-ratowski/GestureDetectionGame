package com.ratowski.objects;

import com.badlogic.gdx.math.Vector2;
import com.ratowski.TweenAccessors.Value;
import com.ratowski.TweenAccessors.ValueAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class Spell {
    private Vector2 position;private float depth;
    private float speed;
    private float[] color = {0};
    private Vector2[] points;
    private Value scale = new Value();
    private TweenManager tweenManager;
    private int type;
    private boolean isFinished = false;
    private float SCALE_THRESHOLD = 1.6f;

    public Spell(int type, Vector2 position, Vector2 size, float speed, Vector2[] points) {
        this.position = position;
        this.speed = speed;
        this.type = type;
        this.points = points;
        scale.setValue(1.1f);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        tweenManager = new TweenManager();
        Tween.to(scale, -1, 0.5f).target(1.6f).delay(0).ease(TweenEquations.easeOutQuad).start(tweenManager);
        color = setColor(type);
    }

    public void update(float delta) {
        depth += speed;
        if (scale.getValue() >= SCALE_THRESHOLD) {
            isFinished = true;
        } else {
            tweenManager.update(delta);
        }
    }

    private float[] setColor(int type) {
        if (type == 1) {
            return new float[]{1, 0, 0};
        } else if (type == 2) {
            return new float[]{0, 1, 0};
        } else if (type == 3) {
            return new float[]{0, 0, 1};
        } else {
            return new float[]{1, 1, 1};
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getType() {
        return type;
    }

    public float getDepth() {
        return depth;
    }

    public float[] getColor() {
        return color;
    }

    public Value getScale(){
        return scale;
    }

    public Vector2[] getPoints(){
        return points;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
