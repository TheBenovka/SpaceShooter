package de.hsos.spiel.ui;

import de.hsos.spiel.SpielActivity;

/**
 * Bildschirm haelt statische Werte des Bildschirms, um die Lesbarkeit/Modularitaet des Codes zu erhoehen.
 */
public enum Bildschirm {
    CONTROLLER_OBEN(SpielActivity.getScreenHoehe() - SpielActivity.getScreenHoehe() * 0.15f),
    LINKS(0),
    OBEN(0),
    PADDING(SpielActivity.getScreenBreite() * 0.06f),
    RECHTS(SpielActivity.getScreenBreite()),
    UNTEN(SpielActivity.getScreenHoehe()),
    SPIELFELD_UNTEN(SpielActivity.getScreenHoehe() - SpielActivity.getScreenHoehe() * 0.15f),
    CONTROLLER_ANTEIL(0.15f),
    SPIELFELD_ANTEIL(0.85f),
    MITTE_X(SpielActivity.getScreenBreite()/2f),
    MITTE_Y(SpielActivity.getScreenHoehe()/2f);
    public final float wert;

    Bildschirm(float wert) {
        this.wert = wert;
    }
}
