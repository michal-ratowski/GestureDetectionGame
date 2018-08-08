package com.ratowski.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetManager {

    public static Preferences preferences;
    public static BitmapFont font;

    public static void loadResources() {
        preferences = Gdx.app.getPreferences("Player");
        if (!preferences.contains("HighScore")) {
            preferences.putInteger("HighScore", 0);
        }

        font = new BitmapFont(Gdx.files.internal("data/berlin.fnt"), true);
    }

    public static void dispose() {
    }

    public static void setHighScore(int val) {
        preferences.putInteger("HighScore", val);
        preferences.flush();
    }

    public static int getHighScore() {
        return preferences.getInteger("HighScore");
    }

}