package de.hsos.spiel.spielobjekt;

/**
 * Spieler die Spezialisierung der Klasse Charakter ueberschreibt das schießen, da die hitbox
 * spawn Koordinaten nicht unbedingt mit anderen Spezialisierungen identisch sind.
 */
public class Spieler extends Charakter {
    public Spieler(float mitteX, float unten, float breite) {
        super(mitteX, unten, breite);
        setMaxLeben(3);
    }

    @Override
    public void schiessen() {
        synchronized (getGeschosse()) {
            getGeschosse().add(new Geschoss(getHitbox().centerX(), getHitbox().top));
        }
    }
}
