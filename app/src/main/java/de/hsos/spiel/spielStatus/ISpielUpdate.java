package de.hsos.spiel.spielStatus;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * ISpielUpdate wird verwendet damit kein Vollzugriff auf Spielen erfolgt.
 */
public interface ISpielUpdate {
    void update(float delta);
    void render(Canvas c);
    void touchEvents(MotionEvent event);
}
