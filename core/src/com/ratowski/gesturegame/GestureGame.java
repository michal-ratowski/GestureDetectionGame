package com.ratowski.gesturegame;

import com.badlogic.gdx.Game;
import com.ratowski.helpers.AssetManager;
import com.ratowski.helpers.OverlayInterface;
import com.ratowski.screens.GameScreen;

public class GestureGame extends Game {

    private GameScreen gameScreen;
    private OverlayInterface overlayInterface;

    public GestureGame(OverlayInterface overlayInterface) {
        this.overlayInterface = overlayInterface;
    }

    public GestureGame() {
    }

    @Override
    public void create() {
        AssetManager.loadResources();
        gameScreen = new GameScreen(overlayInterface);
        setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetManager.dispose();
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}
