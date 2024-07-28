package de.hsos.spiel.spielobjekt;

import de.hsos.spiel.sfx.SoundEffektManager;
import de.hsos.spiel.SpielActivity;

/**
 * Gegner die Spezialisierung der Klasse Charakter ueberschreibt das schießen, da die hitbox
 * spawn Koordinaten nicht unbedingt mit anderen Spezialisierungen identisch sind.
 */
public class Gegner extends Charakter{
    private SoundEffektManager sfxManager;

    public Gegner(float mitteX, float unten, float breite) {
        super(mitteX, unten, breite);
        sfxManager = SoundEffektManager.getInstance(SpielActivity.getSpielKontext());
    }

    @Override
    public void schiessen() {
        sfxManager.spielSchussSound();
        synchronized (getGeschosse()) {
            getGeschosse().add(new Geschoss(getHitbox().centerX(), getHitbox().bottom));
        }
    }
}
