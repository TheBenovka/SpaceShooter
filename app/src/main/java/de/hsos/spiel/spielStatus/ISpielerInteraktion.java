package de.hsos.spiel.spielStatus;

import de.hsos.spiel.spielobjekt.Bewegungsrichtung;

/**
 * Interface um mit dem Spieler Objekt zu interagieren/steuern.
 */
public interface ISpielerInteraktion {
    void setSpielerBewegung(Bewegungsrichtung bewegungsrichtung);
    void spielerSchiessen();
}
