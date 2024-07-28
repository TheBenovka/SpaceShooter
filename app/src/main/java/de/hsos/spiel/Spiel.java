package de.hsos.spiel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import de.hsos.spiel.helfer.IOnSpielEnde;
import de.hsos.spiel.helfer.Spielwerte;
import de.hsos.spiel.spielStatus.ISpielUpdate;
import de.hsos.spiel.spielStatus.Spielen;

/**
 * Spiel ist zustaendig fuer die Upadtes der Logik und der Renderung.
 */
public class Spiel {
    private SurfaceHolder halter;
    private SpielSchleife spielSchleife;
    private ISpielUpdate spielUpdate;
    private IOnSpielEnde onSpielEnde;

    public Spiel(@NonNull SurfaceHolder halter, @NonNull IOnSpielEnde onSpielEnde) {
        this.halter = halter;
        spielSchleife = new SpielSchleife(this);
        spielUpdate = new Spielen(this);
        this.onSpielEnde = onSpielEnde;
    }

    void render() {
        Canvas canvas = halter.lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawColor(Color.BLACK);
                spielUpdate.render(canvas);
            } finally {
                halter.unlockCanvasAndPost(canvas);
            }
        }
    }

    void update(float delta) {
        spielUpdate.update(delta);
    }

    public void start() {
        spielSchleife.start();
    }

    public void stop(Spielwerte spielwerte) {
        onSpielEnde.onSpielBeendet(spielwerte);
        spielSchleife.stop();
    }

    public void stop() {
        spielSchleife.stop();
    }

    public void pause() {
        spielSchleife.pausieren();
    }

    public void fortfuehren() {
        spielSchleife.fortfuehren();
    }

    public boolean touchEvent(MotionEvent event) {
        this.spielUpdate.touchEvents(event);
        return true;
    }
}
