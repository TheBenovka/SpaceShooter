package de.hsos.spiel.spielobjekt;

import android.graphics.RectF;

import java.util.ArrayList;

import de.hsos.spiel.sfx.SoundEffektManager;

/**
 * Die Generalisierungsklasse der Spieler und Gegner Objekte.
 * Behandelt die Logik der Schiffe.
 */
public abstract class Charakter {
    private RectF hitbox;
    private int leben;
    private int maxLeben;

    public float getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setGeschwindigkeit(float geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    public void setHitbox(RectF hitbox) {
        this.hitbox = hitbox;
    }

    private float geschwindigkeit = 760f; // default geschwindigkeit

    public ArrayList<Geschoss> getGeschosse() {
        return geschosse;
    }

    private ArrayList<Geschoss> geschosse = new ArrayList<>();

    public Charakter(float mitteX, float unten, float breite) {
        float links = mitteX - breite / 2f;
        this.hitbox = new RectF(links, unten - breite, breite + links, unten);
    }

    public void bewegen(Bewegungsrichtung bewegung, float delta) {
        float dx = 0;
        float dy = 0;
        if (bewegung == Bewegungsrichtung.LINKS) {
            dx = -geschwindigkeit * delta;
        } else if (bewegung == Bewegungsrichtung.Rechts) {
            dx = geschwindigkeit * delta;
        }
        if (bewegung == Bewegungsrichtung.UNTEN) {
            dy = geschwindigkeit * delta;
        } else if (bewegung == Bewegungsrichtung.OBEN) {
            dy = -geschwindigkeit * delta;
        }
        hitbox.offset(dx, dy);
    }

    public RectF getHitbox() {
        return hitbox;
    }

    abstract public void schiessen();

    public boolean isAmLeben() {
        return leben > 0;
    }

    public void reduziereLeben() {
        this.leben--;
    }

    public void setMaxLeben(int max) {
        this.maxLeben = max;
        this.leben = maxLeben;
    }

    public int getLeben() {
        return leben;
    }

    public int getMaxLeben() {
        return maxLeben;
    }

    /**
     * Geschoss ist logisch eng mit dem Charakter gekoppelt, wehalb diese als innere Klasse realisiert wurde.
     * Die Klasse behnadelt die Logik der Geschosse der Spieler.
     */
    public class Geschoss {
        public RectF getHitbox() {
            return hitbox;
        }
        private RectF hitbox;
        private int spriteIndex;
        public float geschwindigkeit = 900f;
        private int spriteWechselZaehler = 0;
        private final int spriteUpdateIntervall = 5;

        public Geschoss(float mitteX, float unten) {
            float breite = GeschossImg.getBreite();
            float links = mitteX - breite / 2f;
            this.hitbox = new RectF(links, unten - breite, breite + links, unten);
        }

        public int getSpriteIndex() {
            return spriteIndex;
        }

        public void bewegen(Bewegungsrichtung bewegungsrichtung, float delta) {
            float dy = geschwindigkeit * delta;
            if (bewegungsrichtung == Bewegungsrichtung.OBEN) {
                dy *= -1;
            } else if (bewegungsrichtung != Bewegungsrichtung.UNTEN) {
                return;
            }
            hitbox.offset(0, dy);
            if (spriteWechselZaehler < spriteUpdateIntervall) {
                spriteWechselZaehler++;
                spriteIndex = 0;
            } else if (spriteWechselZaehler < spriteUpdateIntervall * 2) {
                spriteWechselZaehler++;
                spriteIndex = 1;
            } else if (spriteWechselZaehler < spriteUpdateIntervall * 3) {
                spriteWechselZaehler++;
                spriteIndex = 2;
            } else if (spriteWechselZaehler < spriteUpdateIntervall * 4) {
                spriteWechselZaehler++;
                spriteIndex = 3;
            } else {
                spriteWechselZaehler = 0;
            }
        }
    }
}
