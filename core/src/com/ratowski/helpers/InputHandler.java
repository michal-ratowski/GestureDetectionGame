package com.ratowski.helpers;

import com.badlogic.gdx.InputProcessor;
import com.ratowski.world.GameWorld;

public class InputHandler implements InputProcessor {

    private GameWorld myWorld;
    public float scaleFactorX;
    public float scaleFactorY;

    public InputHandler(GameWorld myWorld, float scaleFactorX, float scaleFactorY) {
        this.myWorld = myWorld;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        if (myWorld.isGameOver()) {
            if (screenX > 400) {
                myWorld.ready();
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public void touchDragged(int screenX, int screenY) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        myWorld.addSpellEffect(screenX, screenY);
    }

    public void touchDown(int screenX, int screenY) {
        myWorld.timerOn = true;
    }

    public void touchUp(int screenX, int screenY) {
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

}
