package de.hsos.spiel.helfer;

import android.graphics.Bitmap;

import java.util.Random;

import de.hsos.spiel.spielobjekt.Bewegungsrichtung;
import de.hsos.spiel.spielobjekt.Gegner;
import de.hsos.spiel.ui.Bildschirm;
import de.hsos.spiel.spielobjekt.GegnerSchiffImg;

/**
 * Der Rundenmanager generiert Gegener, welche mit jeder weiteren Generierung an Statuswerte
 * (Geschwindigkeit, Schuss Abklingzeit, Leben) gewinnt. Zusätzlich dazu wird das Verhalten des Gegners definiert.
 */
public class RundenManager {
    private Gegner gegner;
    private int runde = 0;
    private final float MAX_GESCHWINDIGKEIT = 750f;
    private final float START_GESCHWINDIGKEIT = 150f;
    private final int MIN_ABKLINGZEIT = 10;
    private final int START_ABKLINGZEIT = 100;
    private int shussAbklingzeitGegner = 50;
    private int shussLetzterSchussGegner = 0;
    private boolean linkeGrenzeBeruehrt = false;
    private Bitmap sprite;
    private final float RAND_PADDING = 50f;
    private final int MIN_LEBEN = 3;
    private int moeglicheScore;
    private int gesamtScore = 0;
    public RundenManager() {

    }

    public void generiereGegner() {
        runde++;
        if (runde > 1) {
            gesamtScore += moeglicheScore;
        }
        gegner = new Gegner(Bildschirm.MITTE_X.wert, Bildschirm.OBEN.wert - 200, GegnerSchiffImg.BLUE3.getSprite().getWidth());
        gegner.setGeschwindigkeit(MAX_GESCHWINDIGKEIT);
        gegner.setMaxLeben(MIN_LEBEN+runde/3);
        moeglicheScore = runde*10;
        waehleSprite();
        berechneAbklingzeit();
    }

    private void berechneGeschwindigkeit() {
        float faktor = (float) (Math.log10(runde+1)/(Math.log10(10)+2));
        gegner.setGeschwindigkeit(START_GESCHWINDIGKEIT + (MAX_GESCHWINDIGKEIT-START_GESCHWINDIGKEIT)*faktor);
    }

    private void berechneAbklingzeit() {
        if (shussAbklingzeitGegner > MIN_ABKLINGZEIT) {
            shussAbklingzeitGegner--;
        }
    }

    private void waehleSprite() {
        Random rnd = new Random();
        int wert = rnd.nextInt(5);
        if  (wert == 0){
            sprite = GegnerSchiffImg.RED5.getSprite();
        }  else if (wert == 1){
            sprite = GegnerSchiffImg.DARKGREY4.getSprite();
        } else if (wert == 2){
            sprite = GegnerSchiffImg.BLUE3.getSprite();
        } else if (wert == 3){
            sprite = GegnerSchiffImg.GREEN1.getSprite();
        } else {
            sprite = GegnerSchiffImg.BLUE5.getSprite();
        }
    }

    public Gegner getGegner() {
        return gegner;
    }

    public void update(float delta) {
        gegnerVerhalten(delta);
    }

    public void gegnerVerhalten(float delta) {
        if (shussLetzterSchussGegner >= shussAbklingzeitGegner) {
            shussLetzterSchussGegner = 0;
            gegner.schiessen();
        } else {
            shussLetzterSchussGegner++;
        }
        if (!linkeGrenzeBeruehrt) {
            if (gegner.getHitbox().left > RAND_PADDING) {
                gegner.bewegen(Bewegungsrichtung.LINKS, delta);
            } else {
                linkeGrenzeBeruehrt = !linkeGrenzeBeruehrt;
            }
        } else {
            if (gegner.getHitbox().right <  Bildschirm.RECHTS.wert-RAND_PADDING) {
                gegner.bewegen(Bewegungsrichtung.Rechts, delta);
            } else {
                linkeGrenzeBeruehrt = !linkeGrenzeBeruehrt;
            }
        }
    }

    public boolean isGegnerInPosition() {
        return gegner.getHitbox().top > Bildschirm.OBEN.wert + 100;
    }

    public Bitmap getSprite() {
        return sprite;
    }
    public void bewegeGegnerAufStartPosition(float delta) {
        if (!isGegnerInPosition()) {
            gegner.bewegen(Bewegungsrichtung.UNTEN, delta);
        } else {
            berechneGeschwindigkeit();
        }
    }

    public boolean gegnerBesiegt() {
        return !gegner.isAmLeben();
    }

    public void reduziereGegnerLeben() {
        gegner.reduziereLeben();
    }

    public int getRunde() {
        return runde;
    }

    public int getScore() {
        return gesamtScore;
    }
}