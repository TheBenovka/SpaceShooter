package de.hsos.daten.beziehungen;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.hsos.daten.entitaeten.ESchiff;
import de.hsos.daten.entitaeten.ESpieler;

/**
 * Beziehung Schiff -N- : -1- Spieler
 */

public class SpielerMitSchiff {
    @Embedded
    public ESchiff schiff;
    @Relation(
            parentColumn = "id",
            entityColumn = "aktivesSchiffID"
    )
    public List<ESpieler> spielerListe;
}
