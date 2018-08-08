package com.ratowski.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 size;
    private float depth;
    private int type;
    private float color[] = {0};
    private boolean isAlive = true;
    private final int SCREEN_THRESHOLD = 480;

    public Enemy(Vector2 position, float depth, Vector2 size, int type) {
        this.position = position;
        this.depth = depth;
        this.size = size;
        this.velocity = new Vector2(0, 0);
        this.type = type;
        this.setColor(type);
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
        reverseVelocityIfWallHit();
    }

    public boolean hit(Spell spell) {
        Rectangle rectangle = new Rectangle(position.x, position.y, size.x, size.y);
        return (rectangle.contains(spell.getPosition().x, spell.getPosition().y) && spell.getDepth() >= depth);
    }

    public void isHit(Spell spell) {
        if (spell.getType() == this.type) {
            isAlive = false;
        }
    }

    public void setColor(int spellType) {
        if (spellType == 1) {
            color = new float[]{1, 0, 0};
        } else if (spellType == 2) {
            color = new float[]{0, 1, 0};
        } else if (spellType == 3) {
            color = new float[]{0, 0, 1};
        } else if (spellType == 4) {
            color = new float[]{1, 1, 1};
        }
    }

    private void reverseVelocityIfWallHit() {
        if (position.x < 0 || (position.x + size.x) > SCREEN_THRESHOLD) {
            velocity.x = -velocity.x;
        }
        if (position.y < 0 || (position.y + size.y) > SCREEN_THRESHOLD) {
            velocity.y = -velocity.y;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public float[] getColor() {
        return color;
    }
}
