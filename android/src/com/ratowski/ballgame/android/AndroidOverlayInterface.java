package com.ratowski.ballgame.android;

import android.gesture.GestureOverlayView;
import com.ratowski.helpers.OverlayInterface;

public class AndroidOverlayInterface implements OverlayInterface {

    GestureOverlayView gestureOverlayView;
    AndroidLauncher launcher;

    AndroidOverlayInterface(GestureOverlayView gestureOverlayView, AndroidLauncher launcher) {
        this.gestureOverlayView = gestureOverlayView;
        this.launcher = launcher;
    }

    @Override
    public void enable() {
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gestureOverlayView.setEnabled(true);
            }
        });
    }

    @Override
    public void disable() {
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (launcher.wasGesturePerformed()) {
                    gestureOverlayView.cancelGesture();
                }
                gestureOverlayView.setEnabled(false);
            }
        });
    }
}
