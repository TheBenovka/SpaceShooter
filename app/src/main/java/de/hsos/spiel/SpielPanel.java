package de.hsos.spiel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import de.hsos.spiel.helfer.IOnSpielEnde;

/**
 * SpielPanel erweitert das SurfaceView und implementiert das SurfaceHolder.Callback,
 * mit welchen die UI Elemente fluessiger sind. Da das zeichnen in einem separaten Thread abgewicklet wird.
 * Es gibt noch weitere Vorteile, fuer alle Vorteile siehe:
 */
public class SpielPanel extends SurfaceView implements SurfaceHolder.Callback {
    private final Spiel spiel;

    public SpielPanel(Context kontext, IOnSpielEnde onSpielEnde) {
        super(kontext);
        SurfaceHolder halter = getHolder();
        halter.addCallback(this);
        spiel = new Spiel(halter, onSpielEnde);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return spiel.touchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        spiel.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        //spiel.pause();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        //spiel.stop();
    }

    public void pausiereSpiel() {
        spiel.pause();
    }

    public void setzeSpielfort() {
        spiel.fortfuehren();
    }

    public void stoppeSpiel() {
        spiel.stop();
    }
}


