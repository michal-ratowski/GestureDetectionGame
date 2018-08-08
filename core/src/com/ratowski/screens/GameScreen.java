package com.ratowski.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.ratowski.helpers.OverlayInterface;
import com.ratowski.world.GameRenderer;
import com.ratowski.world.GameWorld;
import com.ratowski.helpers.InputHandler;

public class GameScreen implements Screen {

    private GameWorld gameWorld;
    private GameRenderer gameRenderer;
    private float runTime = 0;

    public GameScreen(OverlayInterface overlayInterface) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gameWidth = 480;
        float gameHeight = screenHeight / (screenWidth / gameWidth);

        gameWorld = new GameWorld(screenWidth / gameWidth, screenHeight / gameHeight, overlayInterface);
        Gdx.input.setInputProcessor(new InputHandler(gameWorld, screenWidth / gameWidth, screenHeight / gameHeight));
        gameRenderer = new GameRenderer(gameWorld, (int) gameHeight);
    }

    public GameScreen() {
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        gameWorld.update(delta);
        gameRenderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void passTouchEvent(int action, int x, int y) {
        switch (action) {
            case 0:
                ((InputHandler) Gdx.input.getInputProcessor()).touchDown(x, y);
            case 1:
                ((InputHandler) Gdx.input.getInputProcessor()).touchUp(x, y);
            case 2:
                ((InputHandler) Gdx.input.getInputProcessor()).touchDragged(x, y);
        }
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
