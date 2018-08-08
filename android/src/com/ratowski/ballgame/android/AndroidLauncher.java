package com.ratowski.ballgame.android;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStore;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.ratowski.gesturegame.GestureGame;

import java.util.ArrayList;

public class AndroidLauncher extends AndroidApplication implements GestureOverlayView.OnGesturePerformedListener, GestureOverlayView.OnGestureListener {

    private GestureGame gestureGame;
    private View gameView;

    private AndroidOverlayInterface androidOverlayInterface;
    private GestureOverlayView gestureOverlayView;
    private GestureLibrary gestureLibrary;
    private Gesture gesture;

    private boolean gestureTimeout = false;
    private boolean gesturePerformed = false;
    private boolean justRefreshed = false;
    private int timer = 0;

    private static final int GESTURE_THRESHOLD = 30;
    private static final float STROKE_THRESHOLD = 100;
    private static final int COLOR_UNCERTAIN_GESTURE = 0x99dddddd;
    private static final int COLOR_BACKGROUND = 0x99dddddd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        setupGestureLibrary();
        setupOverlayView();
        androidOverlayInterface = new AndroidOverlayInterface(gestureOverlayView, this);
        gestureGame = new GestureGame(androidOverlayInterface);
        gameView = initializeForView(gestureGame, config);
        setupLayout();
    }

    @Override
    public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
    }

    @Override
    public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        timer++;
        gesturePerformed = true;
        checkCancelGestureThreshold();
    }

    @Override
    public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        timer = 0;
        gesture = this.gestureOverlayView.getGesture();
        if (gesturePerformed && !justRefreshed) {
            recognizeGesture();
        }
    }

    @Override
    public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

    }

    public void setupOverlayView() {
        gestureOverlayView = new GestureOverlayView(this);

        gestureOverlayView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                gestureGame.getGameScreen().passTouchEvent(event.getAction(), (int) event.getX(), (int) event.getY());
                return true;
            }
        });

        gestureOverlayView.addOnGestureListener(this);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureOverlayView.setGestureColor(COLOR_UNCERTAIN_GESTURE);
        gestureOverlayView.setUncertainGestureColor(COLOR_UNCERTAIN_GESTURE);
        gestureOverlayView.setFadeOffset(0);
        gestureOverlayView.setFadeEnabled(false);
        gestureOverlayView.setBackgroundColor(COLOR_BACKGROUND);
        gestureOverlayView.setEventsInterceptionEnabled(false);
        gestureOverlayView.setGestureStrokeLengthThreshold(STROKE_THRESHOLD);
    }

    public void setupGestureLibrary() {
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        gestureLibrary.setOrientationStyle(GestureStore.ORIENTATION_INVARIANT);
        gestureLibrary.setOrientationStyle(GestureStore.SEQUENCE_INVARIANT);
        if (!gestureLibrary.load()) {
            finish();
        }
    }

    public void setupLayout() {
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(gestureOverlayView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(layout);
    }

    private void recognizeGesture() {
        ArrayList<Prediction> predictionArray = gestureLibrary.recognize(gesture);
        if (predictionArray.size() > 0 && gesturePerformed) {
            handleGesture(predictionArray.get(0));
        }
    }

    private Vector2[] getGesturePoints() {
        GestureStroke gestureStroke = gesture.getStrokes().get(0);
        Vector2[] gesturePoints = new Vector2[gestureStroke.points.length / 2];
        for (int i = 0; i < gestureStroke.points.length / 2; i++) {
            gesturePoints[i] = new Vector2(gestureStroke.points[2 * i], gestureStroke.points[2 * i + 1]);
        }
        return gesturePoints;
    }

    public boolean wasGesturePerformed() {
        return gesturePerformed;
    }

    private void checkCancelGestureThreshold() {
        if (timer > GESTURE_THRESHOLD) {
            gestureOverlayView.cancelGesture();
            timer = 0;
        }
    }

    private void handleGesture(Prediction prediction) {
        gesturePerformed = false;
        if (prediction.score > 3) {
            handleRecognizedGesture(prediction);
        }
    }

    private void handleRecognizedGesture(Prediction prediction) {
        RectF gestureBox = gesture.getBoundingBox();
        Vector2[] gesturePoints = getGesturePoints();
        gestureGame.getGameScreen().getGameWorld().passGesture(prediction.score, prediction.name, gestureBox.centerX(), gestureBox.centerY(), gestureBox.width(), gestureBox.height(), gesturePoints);
    }

}
