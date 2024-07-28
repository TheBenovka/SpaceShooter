package de.hsos.spiel.spielStatus;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.Iterator;

import de.hsos.MainActivity;
import de.hsos.R;
import de.hsos.daten.SpaceShooterRepository;
import de.hsos.spiel.helfer.RundenManager;
import de.hsos.spiel.sfx.SoundEffektManager;
import de.hsos.spiel.SpielActivity;
import de.hsos.spiel.helfer.Spielwerte;
import de.hsos.spiel.spielobjekt.Charakter;
import de.hsos.spiel.spielobjekt.GeschossImg;
import de.hsos.spiel.Spiel;
import de.hsos.spiel.spielobjekt.Spieler;
import de.hsos.spiel.spielobjekt.Bewegungsrichtung;
import de.hsos.spiel.ui.Bildschirm;
import de.hsos.spiel.ui.Hintergrund;
import de.hsos.spiel.ui.HintergrundImg;
import de.hsos.spiel.spielobjekt.GegnerSchiffImg;
import de.hsos.spiel.spielobjekt.SchiffImg;
import de.hsos.spiel.ui.SpielUI;

/**
 * Spielen steuert die Spiel Elemente und die Positionierung der Grafischen Elemente.
 * Implementiert ISpielUpdate da Spielen das Spiel aktualisiert.
 */
public class Spielen implements ISpielUpdate, ISpielerInteraktion {
    private Spieler spieler;
    private Spiel spiel;
    private SpielUI spielUI;
    private Hintergrund hintergrund;
    private Bewegungsrichtung bewegungsrichtung = Bewegungsrichtung.KEINE_BEWEGUNG;
    private int spielerVertikalerStoss = 0;
    private RundenManager rundenManager;
    private long startZeit;
    private int schuesse = 0;
    private int treffer = 0;
    private SchiffImg spielerSchiffImg;
    private SoundEffektManager sfxManager;

    public Spielen(Spiel spiel) {
        spielerSchiffImg = new SchiffImg(new SpaceShooterRepository(MainActivity.mainActivity.getApplication()).getAktuellesSchiff().bildNr);
        this.spiel = spiel;
        this.spielUI = new SpielUI(this);
        this.hintergrund = new Hintergrund(Bildschirm.MITTE_X.wert, Bildschirm.MITTE_Y.wert);
        rundenManager = new RundenManager();
        platziereSpieler(Bildschirm.MITTE_X.wert, Bildschirm.SPIELFELD_UNTEN.wert - Bildschirm.PADDING.wert);
        rundenManager.generiereGegner();
        startZeit = System.currentTimeMillis();
        sfxManager = SoundEffektManager.getInstance(SpielActivity.getSpielKontext());
    }

    private void platziereSpieler(float links, float unten) {
        spieler = new Spieler(links, unten, spielerSchiffImg.getSpriteSpieler().getWidth());
    }

    @Override
    public void update(float delta) {
        if (rundenManager.isGegnerInPosition()) {
            if (spieler != null) {
                if (legaleBewegung()) {
                    spieler.bewegen(bewegungsrichtung, delta);
                }
                aktualisiereSpielerGeschosse(delta);
                aktualisiereGegnerGeschosse(delta);
                rundenManager.update(delta); // updated die Gegner
                spielerAendereVertikalerStoss();
            }
        } else {
            rundenManager.bewegeGegnerAufStartPosition(delta);
        }
    }

    private boolean legaleBewegung() {
        return bewegungsrichtung != Bewegungsrichtung.KEINE_BEWEGUNG && !(
                bewegungsrichtung == Bewegungsrichtung.LINKS
                        && spieler.getHitbox().left <= 10
                        || bewegungsrichtung == Bewegungsrichtung.Rechts
                        && spieler.getHitbox().right >= SpielActivity.getScreenBreite() - 10);
    }

    // synchronized für geplantes Multithreading
    private void aktualisiereSpielerGeschosse(float delta) {
        synchronized (spieler.getGeschosse()) {
            // Mit Iterator, da das entfernen von Objekten in einer For-Each Taboo ist!
            Iterator<Charakter.Geschoss> iterator = spieler.getGeschosse().iterator();
            while (iterator.hasNext()) {
                Charakter.Geschoss geschoss = iterator.next();
                if (geschoss.getHitbox().bottom < Bildschirm.OBEN.wert) {
                    iterator.remove();
                } else if (rundenManager.getGegner().getHitbox().contains(geschoss.getHitbox())) {
                    sfxManager.spielTrefferSound();
                    iterator.remove();
                    treffer++;
                    rundenManager.reduziereGegnerLeben();
                    if (rundenManager.gegnerBesiegt()) {
                        rundenManager.generiereGegner();
                        spieler.getGeschosse().clear();
                    }
                } else {
                    geschoss.bewegen(Bewegungsrichtung.OBEN, delta);
                }
            }
        }
    }

    // synchronized für geplantes Multithreading
    private void aktualisiereGegnerGeschosse(float delta) {
        // Mit Iterator, da das entfernen von Objekten in einer For-Each Taboo ist!
        synchronized (rundenManager.getGegner().getGeschosse()) {
            Iterator<Charakter.Geschoss> iterator = rundenManager.getGegner().getGeschosse().iterator();
            while (iterator.hasNext()) {
                Charakter.Geschoss geschoss = iterator.next();
                if (geschoss.getHitbox().top > Bildschirm.UNTEN.wert) {
                    iterator.remove();
                } else if (spieler.getHitbox().contains(geschoss.getHitbox())) {
                    sfxManager.spielTrefferSound();
                    iterator.remove();
                    spieler.reduziereLeben();
                    if (!spieler.isAmLeben()) {
                        long endZeit = System.currentTimeMillis();
                        spiel.stop(new Spielwerte(rundenManager.getScore(), rundenManager.getRunde(), startZeit, endZeit, schuesse, treffer));
                    }
                } else {
                    geschoss.bewegen(Bewegungsrichtung.UNTEN, delta);
                }
            }
        }
    }

    public void setSpielerBewegung(Bewegungsrichtung bewegungsrichtung) {
        this.bewegungsrichtung = bewegungsrichtung;
    }

    public void spielerAendereVertikalerStoss() {
        if (spielerVertikalerStoss < 28) {
            spielerVertikalerStoss++;
        } else {
            spielerVertikalerStoss = 0;
        }
    }

    public void spielerSchiessen() {
        if (rundenManager.isGegnerInPosition()) {
            sfxManager.spielSchussSound();
            spieler.schiessen();
            schuesse++;
        }
    }

    @Override
    public void render(Canvas c) {
        zeichneHintergrund(c);
        zeichneSpieler(c);
        zeichneGegener(c);
        zeichneGeschosse(c);
        zeichneSpielStats(c);
        spielUI.zeichnen(c);
    }

    private void zeichneHintergrund(Canvas canvas) {
        canvas.drawBitmap(HintergrundImg.STERNE.getSprite(), hintergrund.getHitbox().left, hintergrund.getHitbox().top, null);
    }

    private void zeichneSpieler(Canvas canvas) {
        zeichneVertikalerStossSpieler(canvas);
        canvas.drawBitmap(spielerSchiffImg.getSpriteSpieler(), spieler.getHitbox().left, spieler.getHitbox().top, null);
    }

    public void zeichneSpielStats(Canvas canvas) {
        float textSize = 30f;
        float padding = 20f;
        Paint paint = getPaint(textSize);
        // Links HealthBar
        zeichneSpielerLeben(canvas, textSize, padding);
        // Mitte Rundenzaehler
        zeichneRundenzaehler(canvas, textSize, padding, paint);
        // Rechts Scorezaehler
        zeichneScorezaehler(canvas, textSize, padding, paint);
    }

    private void zeichneScorezaehler(Canvas canvas, float textSize, float padding, Paint paint) {
        String txtScore = SpielActivity.getSpielKontext().getString(R.string.score) + " " + rundenManager.getScore();
        PointF posScore = new PointF(Bildschirm.MITTE_X.wert + Bildschirm.MITTE_X.wert / 1.5f, textSize + padding);
        textZentrieren(canvas, txtScore, paint, posScore.x, posScore.y);
    }

    private void zeichneRundenzaehler(Canvas canvas, float textSize, float padding, Paint paint) {
        String txtRunde = SpielActivity.getSpielKontext().getString(R.string.round) + " " + rundenManager.getRunde();
        PointF posRunde = new PointF(Bildschirm.MITTE_X.wert, textSize + padding);
        textZentrieren(canvas, txtRunde, paint, posRunde.x, posRunde.y);
    }

    @NonNull
    private static Paint getPaint(float textSize) {
        Paint paint = new Paint();
        Typeface font = ResourcesCompat.getFont(SpielActivity.getSpielKontext(), R.font.ethnocentric_rg);
        paint.setColor(Color.rgb(80, 120, 255));
        paint.setTypeface(font);
        paint.setTextSize(textSize);
        return paint;
    }

    private void textZentrieren(Canvas canvas, String text, Paint paint, float x, float y) {
        float textWidth = paint.measureText(text);
        canvas.drawText(text, x - textWidth / 2, y, paint);
    }


    public void zeichneSpielerLeben(Canvas c, float textSize, float padding) {
        Paint paint = new Paint();
        RectF spielerLebensleiste;
        RectF spielerLebensleisteHintergrund;
        float breite = 300f;
        float links = Bildschirm.LINKS.wert + padding;
        float unten = Bildschirm.OBEN.wert + textSize + padding;
        float oben = Bildschirm.OBEN.wert + padding + 5;
        float rechts = links + breite * spieler.getLeben() / spieler.getMaxLeben();
        spielerLebensleiste = new RectF(links, oben, rechts, unten);
        spielerLebensleisteHintergrund = new RectF(links - 2, oben - 2, links + breite + 2, unten + 2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        c.drawRect(spielerLebensleisteHintergrund, paint);
        paint.setColor(Color.RED);
        c.drawRect(spielerLebensleiste, paint);
    }

    private void zeichneVertikalerStossSpieler(Canvas canvas) {
        float links = spieler.getHitbox().centerX() - spielerSchiffImg.getVertikalerStossGegner(0).getWidth() / 2f;
        float oben = spieler.getHitbox().centerY() + spielerSchiffImg.getVertikalerStossGegner(0).getHeight();
        zeichneVertikalerStoss(spielerSchiffImg.getVertikalerStossSpieler(), canvas, links, oben);
    }

    // TODO: alle zeichne-Methoden in ein Animator oder aehnlichem auslagern!
    private void zeichneVertikalerStossGegner(Canvas canvas) {
        float links = rundenManager.getGegner().getHitbox().centerX() - GegnerSchiffImg.BLUE3.getVertikalerStoss(0).getWidth() / 2f;
        float oben = rundenManager.getGegner().getHitbox().centerY() - GegnerSchiffImg.BLUE3.getSchiffImg().getSpriteGegner().getHeight()
                + GegnerSchiffImg.BLUE3.getVertikalerStoss(0).getHeight();
        zeichneVertikalerStoss(GegnerSchiffImg.BLUE3.getVertikalerStoss(), canvas, links, oben);
    }

    private void zeichneVertikalerStoss(Bitmap[] vertikalerStoss, Canvas canvas, float links, float oben) {
        if (spielerVertikalerStoss < 7) {
            canvas.drawBitmap(vertikalerStoss[0], links, oben, null);
        } else if (spielerVertikalerStoss < 14) {
            canvas.drawBitmap(vertikalerStoss[1], links, oben, null);
        } else if (spielerVertikalerStoss < 21) {
            canvas.drawBitmap(vertikalerStoss[2], links, oben, null);
        } else {
            canvas.drawBitmap(vertikalerStoss[3], links, oben, null);
        }
    }

    private void zeichneGegener(Canvas canvas) {
        zeichneVertikalerStossGegner(canvas);
        canvas.drawBitmap(rundenManager.getSprite(), rundenManager.getGegner().getHitbox().left, rundenManager.getGegner().getHitbox().top, null);
    }

    // synchronized für geplantes Multithreading
    private void zeichneGeschosse(Canvas canvas) {
        synchronized (spieler.getGeschosse()) {
            for (Charakter.Geschoss geschoss : spieler.getGeschosse()) {
                zeichneGeschossSpieler(geschoss, canvas);
            }
        }
        synchronized (rundenManager.getGegner().getGeschosse()) {
            for (Charakter.Geschoss geschoss : rundenManager.getGegner().getGeschosse()) {
                zeichneGeschossGegner(geschoss, canvas);
            }
        }
    }

    private void zeichneGeschossGegner(Charakter.Geschoss geschoss, Canvas canvas) {
        canvas.drawBitmap(GeschossImg.FEUERBALL.getSpriteGegner(geschoss.getSpriteIndex()), geschoss.getHitbox().left, geschoss.getHitbox().top, null);
    }

    private void zeichneGeschossSpieler(Charakter.Geschoss geschoss, Canvas canvas) {
        canvas.drawBitmap(GeschossImg.FEUERBALL.getSpriteSpieler(geschoss.getSpriteIndex()), geschoss.getHitbox().left, geschoss.getHitbox().top, null);
    }

    // Multi-Touch: https://medium.com/@arj.sna/multi-touch-in-android-c87031d106b3
    @Override
    public void touchEvents(MotionEvent event) {
        final int aktion = event.getActionMasked();
        final int aktionIndex = event.getActionIndex();
        final int zeigerID = event.getPointerId(aktionIndex);

        float x = event.getX();
        float y = event.getY();
        switch (aktion) {
            case MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                float xDown = event.getX(aktionIndex);
                float yDown = event.getY(aktionIndex);
                spielUI.behandleAktionDown(xDown, yDown, zeigerID);
            }
            case MotionEvent.ACTION_MOVE -> {
                spielUI.behandleAktionMove(x, y, zeigerID, event.getPointerCount());
            }
            case MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                float xUp = event.getX(aktionIndex);
                float yUp = event.getY(aktionIndex);
                spielUI.behandleAktionUp(xUp, yUp, zeigerID);
            }
        }
    }

    public void animationZerstoereGegner() {
    }

    public void animationZerstoereSpieler() {
    }
}